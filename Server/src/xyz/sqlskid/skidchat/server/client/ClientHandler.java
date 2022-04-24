package xyz.sqlskid.skidchat.server.client;

import me.hideri.ext.server.packet.*;
import me.hideri.ext.server.packet.impl.KeyExchangePacket;
import me.hideri.ext.server.packet.impl.LoginPacket;
import xyz.sqlskid.skidchat.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ClientHandler extends Thread {
    private final Client client;

    public ClientHandler(Client client)
    {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            client.input = new DataInputStream(client.getSocket().getInputStream());
            client.output = new DataOutputStream(client.getSocket().getOutputStream());
            client.packetManager = new PacketManager(client);
        }
        catch (Exception e)
        {
            System.out.println("Client dropped. Reason: " + e.getMessage());

            if(Server.instance.chatroomManager.getChatroomByOwner(client.getUuid()) != null){
                Server.instance.chatroomManager.getChatroomByOwner(client.getUuid()).destroy();
                Server.instance.chatroomManager.removeChatroom(Server.instance.chatroomManager.getChatroomByOwner(client.getUuid()) );
            }
            Server.instance.rsa.removeKey(client);
            Server.instance.aes.removeKey(client);
            if(client.loggedIn) {
                Server.instance.clientDataManager.saveDataByClient(client);
            }
            Server.instance.clientList.remove(client);
            stop();
        }

        try {
            handleClient();
        } catch (Exception e) {
            System.out.println("Client dropped. Reason: " + e.getMessage());

            if(Server.instance.chatroomManager.getChatroomByOwner(client.getUuid()) != null){
                Server.instance.chatroomManager.getChatroomByOwner(client.getUuid()).destroy();
                Server.instance.chatroomManager.removeChatroom(Server.instance.chatroomManager.getChatroomByOwner(client.getUuid()) );
            }
            Server.instance.rsa.removeKey(client);
            Server.instance.aes.removeKey(client);
            if(client.loggedIn) {
                Server.instance.clientDataManager.saveDataByClient(client);
            }
                Server.instance.clientList.remove(client);

            stop();
        }
    }

    private void handleClient() throws Exception
    {
        String line;

        Server.instance.aes.generateKey(client);

        while (true)
        {
            while (!(line = client.input.readUTF()).equals(""))
            {
                Packet.Header header = Packet.readHeader(line);

                Packet packet = client.packetManager.getPacketById(header.getId());

                if(client.theirKey == null)
                {
                    if(!(packet instanceof KeyExchangePacket))
                    {
                        client.getSocket().close();
                        Server.instance.clientList.remove(client);
                        System.out.println("Client dropped. Reason: Failed to exchange keys");
                        return;
                    }
                }

                if(packet == null)
                {
                    client.getSocket().close();
                    Server.instance.clientList.remove(client);
                    if(Server.instance.chatroomManager.getChatroomByOwner(client.getUuid()) != null){
                        Server.instance.chatroomManager.getChatroomByOwner(client.getUuid()).destroy();
                        Server.instance.chatroomManager.removeChatroom(Server.instance.chatroomManager.getChatroomByOwner(client.getUuid()) );
                    }

                    System.out.println("Client dropped. Reason: Invalid packet id");
                    stop();
                }
                else
                {
                    try
                    {
                        packet.readPacket(line);
                    }
                    catch (PacketReadException | PacketWriteException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
