package me.hideri.ext.client.packet.impl;

import me.hideri.ext.client.packet.HeaderStorage;
import me.hideri.ext.client.packet.Packet;
import me.hideri.ext.client.packet.PacketReadException;
import me.hideri.ext.client.packet.PacketWriteException;
import xyz.sqlskid.skidchat.SkidChat;

public class KeepAlivePacket extends Packet
{
    public KeepAlivePacket(SkidChat client) {
        super(HeaderStorage.instance.KEEPALIVE, client);
    }

    @Override
    public void readPacket(String packet) throws PacketReadException, PacketWriteException {

    }

    @Override
    public void sendPacket(String packet) throws PacketWriteException {
        Value ping = Value.construct("ping", System.currentTimeMillis());
        this.defaultSendPacket(ping);
    }
}
