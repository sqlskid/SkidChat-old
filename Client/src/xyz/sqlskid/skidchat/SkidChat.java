package xyz.sqlskid.skidchat;

import me.hideri.ext.client.packet.*;
import xyz.sqlskid.skidchat.chatroom.ChatroomManager;
import xyz.sqlskid.skidchat.client.KeepAlive;
import xyz.sqlskid.skidchat.encryption.AES;
import xyz.sqlskid.skidchat.encryption.RSA;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.UUID;

public class SkidChat extends Thread {


    public UUID uuid;
    public Socket socket;
    public boolean isConnected = false;
    public boolean loggedIn = false;
    public DataInputStream input;
    public DataOutputStream output;
    public PacketManager packetManager;
    public ChatroomManager chatroomManager;
    public KeepAlive keepAlive;
    private String ip;
    private int port;
    public RSA rsa;
    public AES aes;
    public int version = 3;

    public PublicKey theirKey;

    public SkidChat(UUID uuid, String ip, int port) {
        this.uuid = uuid;
        this.ip = ip;
        this.port = port;
    }

    public void run() {
        while (!isConnected) {
            try {
                socket = new Socket(ip, port);
                if (socket.isConnected())
                    isConnected = socket.isConnected();
            } catch (Exception e) {
                System.out.println("Connection error! Reconnecting in 5 seconds...");
                try {
                    sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        try {
            handle();
        } catch (Exception | PacketWriteException e) {
            System.out.println("Disconnected.");
            keepAlive.stop();
        }
    }

    public void handle() throws IOException, PacketWriteException, NoSuchAlgorithmException, InterruptedException {

        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());
        packetManager = new PacketManager(this);
        rsa = new RSA();
        aes = new AES();
        keepAlive = new KeepAlive(packetManager);
        chatroomManager = new ChatroomManager();
        String line;
        aes.generateKey();

        packetManager.getPacketById(HeaderStorage.instance.KEYEXCHANGE.getId()).sendPacket("");

        while (isConnected) {
            while (!(line = input.readUTF()).equals("")) {
                Packet.Header header = Packet.readHeader(line);
                Packet packet = packetManager.getPacketById(header.getId());

                if (packet == null) {
                    socket.close();
                    System.out.println("Client dropped. Reason: Invalid packet id");
                } else {
                    try {
                        packet.readPacket(line);
                    } catch (PacketReadException | PacketWriteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
