package me.hideri.ext.server.packet.impl;

import me.hideri.ext.server.packet.HeaderStorage;
import me.hideri.ext.server.packet.Packet;
import me.hideri.ext.server.packet.PacketReadException;
import me.hideri.ext.server.packet.PacketWriteException;
import xyz.sqlskid.skidchat.server.Server;
import xyz.sqlskid.skidchat.server.client.Client;
import xyz.sqlskid.skidchat.server.client.ClientData;

import java.io.IOException;
import java.util.UUID;

public class LoginPacket extends Packet {

    public LoginPacket(Client client) {
        super(HeaderStorage.instance.LOGIN, client);
    }

    @Override
    public void readPacket(String packet) throws PacketReadException, PacketWriteException {
        Value uuid = this.readValue("uuid", this.readArguments(packet));
        Value version = this.readValue("version", this.readArguments(packet));

        if(version != null){
            if(version.getInteger() != Server.instance.version) {
                respond(client, "Your client version is old! Please download the latest release from: https://github.com/sqlskid/SkidChat");
                try {
                    client.getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }

        client.setUuid(UUID.fromString(uuid.getString()));

        ClientData clientData = Server.instance.clientDataManager.getClientDataByUUID(client.getUuid());

        if(clientData == null){
            Server.instance.clientDataManager.saveDataByClient(client);
        }
        else
        {
            client.setName(clientData.nick);
            client.setRank(clientData.rank);
        }


        client.loggedIn = true;


        System.out.println("Client logged in!");
        Value success = Value.construct("success", true);
        this.defaultSendPacket(success);

        client.packetManager.getPacketById(HeaderStorage.instance.CHATROOM.getId()).sendPacket("");
    }

    @Override
    public void sendPacket(String packet) throws PacketWriteException {
        this.defaultSendPacket(this.readArguments(packet));
    }
}
