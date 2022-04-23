package xyz.sqlskid.skidchat.server.client;

import org.json.JSONArray;
import org.json.JSONObject;
import xyz.sqlskid.skidchat.server.Server;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientDataManager {

    //public File saveFile = new File("table.data");
    public File saveJsonFile = new File("table.json");
    public List<ClientData> clientDataList = new ArrayList<>();

    public ClientDataManager(){
        /*if(!saveFile.exists()){
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        if(!saveJsonFile.exists()){
            try {
                saveJsonFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                loadJsonData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveDataByClient(Client client){
        if(getClientDataByUUID(client.getUuid()) == null){
            ClientData clientData = new ClientData();
            clientData.uuid = client.getUuid();
            clientDataList.add(clientData);
        }


        for(ClientData clientData: clientDataList){
            if(clientData.uuid.equals(client.getUuid())){
                clientData.nick = client.getName();
                clientData.rank = client.getRank();
            }
        }
    }

    public void saveAllClients(){
        for(Client client: Server.instance.clientList){
            ClientData clientData = getClientDataByUUID(client.getUuid());
            clientData.nick = client.getName();
            clientData.rank = client.getRank();
        }
    }

    public ClientData getClientDataByUsername(String username){
        for(ClientData clientData: clientDataList){
            if(clientData.nick.equalsIgnoreCase(username)){
                return clientData;
            }
        }
        return null;
    }

    public ClientData getClientDataByUUID(UUID uuid){
        for(ClientData clientData: clientDataList){
            if(clientData.uuid.equals(uuid)){
                return clientData;
            }
        }
        return null;
    }

   /* public void loadData() throws FileNotFoundException {
        Scanner myReader = new Scanner(saveFile);
        while (myReader.hasNextLine()) {
            String text = myReader.nextLine();
            String[] data = text.split(":");
            ClientData clientData = new ClientData();
            clientData.username = data[0];
            clientData.password = data[1];
            clientData.x = Float.parseFloat(data[2]);
            clientData.y = Float.parseFloat(data[3]);
            clientDataList.add(clientData);
            System.out.println("Loaded data for " + data[0]);
        }
        myReader.close();
    }*/

    public void loadJsonData() throws Exception {
        BufferedReader buf = new BufferedReader(new FileReader(saveJsonFile));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line=buf.readLine()) != null)
        {
            sb.append(line);
        }
        JSONObject json = new JSONObject(sb.toString());
        ArrayList<String> list = new ArrayList<>(json.keySet());
        for(String string: list){
            JSONArray array = json.getJSONArray(string);
            JSONObject object = array.getJSONObject(0);
            ClientData clientData = new ClientData();
            clientData.nick = object.getString("nick");
            clientData.rank = object.getEnum(Rank.class, "rank");
            clientData.uuid = UUID.fromString(string);
            clientDataList.add(clientData);
        }
    }

    public void saveJsonData() throws Exception {
        FileWriter pw = new FileWriter(saveJsonFile);
        JSONObject jsonObject = new JSONObject();

        System.out.println("Saving user data...");
        long currentMS = System.currentTimeMillis();
        for(ClientData clientData: clientDataList){
            JSONObject obj = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            obj.put("nick", clientData.nick);
            obj.put("rank", clientData.rank);
            jsonArray.put(obj);
            jsonObject.put(clientData.uuid.toString(), jsonArray);
        }

        System.out.println("Saved! Done in " + (System.currentTimeMillis() - currentMS) + "ms!");

        pw.write(jsonObject.toString(2));
        pw.close();
    }

    public boolean isLegal(String string) {
        Pattern checkPattern;
        Matcher match;

        if(string.contains(" "))
            return false;

        if(Server.instance.getClientByName(string) != null)
            return false;

        checkPattern = Pattern.compile("[^a-zA-Z0-9]");

        match = checkPattern.matcher(string);

        return !match.find();
    }

}
