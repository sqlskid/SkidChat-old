package me.hideri.ext.server.packet.impl;

import me.hideri.ext.server.packet.HeaderStorage;
import me.hideri.ext.server.packet.Packet;
import me.hideri.ext.server.packet.PacketReadException;
import me.hideri.ext.server.packet.PacketWriteException;
import xyz.sqlskid.skidchat.server.Server;
import xyz.sqlskid.skidchat.server.client.Client;
import xyz.sqlskid.skidchat.server.room.Chatroom;

import java.util.StringJoiner;

public class ChatroomPacket extends Packet
{
    public ChatroomPacket(Client client) {
        super(HeaderStorage.instance.CHATROOM, client);
    }

    @Override
    public void readPacket(String packet) throws PacketReadException, PacketWriteException {
    }

    @Override
    public void sendPacket(String packet) throws PacketWriteException {
        StringJoiner joiner = new StringJoiner(",");

        for(Chatroom chatroom: Server.instance.chatroomManager.getChatrooms()){
            joiner.add(chatroom.getName());
        }

        Value mode = Value.construct("mode", "SEND");
        Value chatrooms = Value.construct("chatrooms", joiner.toString());

        this.defaultSendPacket(mode, chatrooms);
    }
}
