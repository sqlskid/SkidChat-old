package me.hideri.ext.client.packet;

import me.hideri.ext.client.packet.impl.*;
import xyz.sqlskid.skidchat.SkidChat;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class PacketManager
{
    public final BlockingDeque<Packet> packetList = new LinkedBlockingDeque<>();

    public PacketManager(SkidChat client)
    {
        packetList.add(new EncryptedPacket(client));
        packetList.add(new KeyExchangePacket(client));
        packetList.add(new LoginPacket(client));
        packetList.add(new KeepAlivePacket(client));
        packetList.add(new ChatPacket(client));
        packetList.add(new ChatroomPacket(client));
    }

    public Packet getPacketById(long id)
    {
        for(Packet packet : packetList)
        {
            if(packet.headerMatches(HeaderStorage.instance.getByID(id)))
            {
                return packet;
            }
        }

        return null;
    }
}
