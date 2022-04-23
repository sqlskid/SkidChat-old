package me.hideri.ext.server.packet.impl;

import me.hideri.ext.server.packet.HeaderStorage;
import me.hideri.ext.server.packet.Packet;
import me.hideri.ext.server.packet.PacketReadException;
import me.hideri.ext.server.packet.PacketWriteException;
import xyz.sqlskid.skidchat.server.client.Client;

public class KeepAlivePacket extends Packet {

    public KeepAlivePacket(Client client) {
        super(HeaderStorage.instance.KEEPALIVE, client);
    }

    @Override
    public void readPacket(String packet) throws PacketReadException, PacketWriteException {
        Value ping = this.readValue("ping", this.readArguments(packet));
        client.ping = System.currentTimeMillis() - ping.getLong();
    }

    @Override
    public void sendPacket(String packet) throws PacketWriteException {
        Value ping = Value.construct("ping", client.ping);
        this.defaultSendPacket(ping);
    }
}
