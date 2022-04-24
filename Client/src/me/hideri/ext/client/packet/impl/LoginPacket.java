package me.hideri.ext.client.packet.impl;

import me.hideri.ext.client.packet.HeaderStorage;
import me.hideri.ext.client.packet.Packet;
import me.hideri.ext.client.packet.PacketReadException;
import me.hideri.ext.client.packet.PacketWriteException;
import xyz.sqlskid.skidchat.SkidChat;

public class LoginPacket extends Packet {

    public LoginPacket(SkidChat client) {
        super(HeaderStorage.instance.LOGIN, client);
    }

    @Override
    public void readPacket(String packet) throws PacketReadException, PacketWriteException {
        Value success = this.readValue("success", this.readArguments(packet));

        if(success.getBoolean()){
            client.loggedIn = true;
            System.out.println("Logged in!");
        }
    }

    @Override
    public void sendPacket(String packet) throws PacketWriteException {
        Value uuid = Value.construct("uuid", client.uuid);
        Value version = Value.construct("version", client.version);

        this.defaultSendPacket(uuid, version);
    }
}
