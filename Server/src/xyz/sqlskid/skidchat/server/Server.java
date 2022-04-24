package xyz.sqlskid.skidchat.server;

import xyz.sqlskid.skidchat.server.client.Client;
import xyz.sqlskid.skidchat.server.client.ClientDataManager;
import xyz.sqlskid.skidchat.server.client.ClientHandler;
import xyz.sqlskid.skidchat.server.command.CommandManager;
import xyz.sqlskid.skidchat.server.encryption.AES;
import xyz.sqlskid.skidchat.server.encryption.RSA;
import xyz.sqlskid.skidchat.server.room.Chatroom;
import xyz.sqlskid.skidchat.server.room.ChatroomManager;
import xyz.sqlskid.skidchat.server.util.AutoSave;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{


    public ServerSocket serverSocket;

    public static Server instance;

    public List<Client> clientList = new ArrayList<>();
    public ClientDataManager clientDataManager = new ClientDataManager();
    public AutoSave autoSave = new AutoSave();
    public CommandManager commandManager = new CommandManager();
    public ChatroomManager chatroomManager = new ChatroomManager();
    public RSA rsa;
    public AES aes;
    private int port = 1;
    public int version = 2;

    public Server(int port) throws IOException {
        this.port = port;
    }

    public void run() {
        instance = this;
        try {
            rsa = new RSA();
            aes = new AES();
            this.serverSocket = new ServerSocket(port);

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("Server started on port " + port + "!");

        autoSave.start();

        while (true){
            try {
                Socket socket = serverSocket.accept();
                Client client = new Client(socket);
                Server.instance.clientList.add(client);
                new ClientHandler(client).start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public Client getClientByName(String clientName) {
        for(Client client: clientList){
            if(client.getName().equalsIgnoreCase(clientName)){
                return client;
            }
        }
        return null;
    }
}
