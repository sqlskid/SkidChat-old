package me.hideri.ext.client.packet.impl;

import me.hideri.ext.client.packet.HeaderStorage;
import me.hideri.ext.client.packet.Packet;
import me.hideri.ext.client.packet.PacketReadException;
import me.hideri.ext.client.packet.PacketWriteException;
import xyz.sqlskid.skidchat.SkidChat;
import xyz.sqlskid.skidchat.chatroom.Chatroom;

public class ChatroomPacket extends Packet {

    public ChatroomPacket(SkidChat client) {
        super(HeaderStorage.instance.CHATROOM, client);
    }

    @Override
    public void readPacket(String packet) throws PacketReadException, PacketWriteException {
        Value mode = this.readValue("mode", this.readArguments(packet));
        switch (mode.getString()){
            case "SEND":
                Value chatrooms = this.readValue("chatrooms", this.readArguments(packet));

                String[] crArray = chatrooms.getString().split(",");

                for(String string: crArray) {
                    Chatroom chatroom = new Chatroom(string);
                    client.chatroomManager.addChatroom(chatroom);

                    if (client.chatroomManager.getSelectedChatroom() == null) {
                        client.chatroomManager.setSelectedChatroom(chatroom);
                    }
                }
                break;
            case "JOIN":
                Value chatroomName = this.readValue("chatroom", this.readArguments(packet));
                Chatroom chatroom = new Chatroom(chatroomName.getString());
                client.chatroomManager.addChatroom(chatroom);
                client.chatroomManager.setSelectedChatroom(chatroom);
                break;
        }

    }

    @Override
    public void sendPacket(String packet) throws PacketWriteException {

    }
}
