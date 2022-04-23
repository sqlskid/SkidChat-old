import jdk.nashorn.internal.ir.WhileNode;
import xyz.sqlskid.skidchat.server.Server;
import xyz.sqlskid.skidchat.server.client.Client;

import java.io.IOException;
import java.util.Scanner;

public class Start {

    public static void main(String[] args) throws Exception {
        int port = 3386;
        if(args.length >1){
            port = Integer.parseInt(args[0]);
        }
        Server server = new Server(port);
        server.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.equals("save")){
                server.clientDataManager.saveAllClients();
                server.clientDataManager.saveJsonData();
                System.out.println("Saved!");
            }
            if(line.equals("end")){
                server.clientDataManager.saveAllClients();
                server.clientDataManager.saveJsonData();
                for(Client client: server.clientList){
                    client.getSocket().close();
                }
                server.stop();
                System.exit(-1);
            }
        }
    }

}
