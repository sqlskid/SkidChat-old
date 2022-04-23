package me.hideri.ext.server.packet.impl;

import me.hideri.ext.server.packet.HeaderStorage;
import me.hideri.ext.server.packet.Packet;
import me.hideri.ext.server.packet.PacketReadException;
import me.hideri.ext.server.packet.PacketWriteException;
import xyz.sqlskid.skidchat.server.Server;
import xyz.sqlskid.skidchat.server.client.Client;
import xyz.sqlskid.skidchat.server.client.ClientData;

import java.util.UUID;

public class LoginPacket extends Packet {

    public LoginPacket(Client client) {
        super(HeaderStorage.instance.LOGIN, client);
    }

    @Override
    public void readPacket(String packet) throws PacketReadException, PacketWriteException {
        Value uuid = this.readValue("uuid", this.readArguments(packet));

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
