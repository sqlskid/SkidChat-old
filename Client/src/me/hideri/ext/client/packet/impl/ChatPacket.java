package me.hideri.ext.client.packet.impl;

import me.hideri.ext.client.packet.HeaderStorage;
import me.hideri.ext.client.packet.Packet;
import me.hideri.ext.client.packet.PacketReadException;
import me.hideri.ext.client.packet.PacketWriteException;
import xyz.sqlskid.skidchat.SkidChat;
import xyz.sqlskid.skidchat.chatroom.Chatroom;

public class ChatPacket extends Packet {
    public ChatPacket(SkidChat client) {
        super(HeaderStorage.instance.CHAT, client);
    }

    @Override
    public void readPacket(String packet) throws PacketReadException, PacketWriteException {
        Header header = this.readHeader(packet);

        if(this.headerMatches(header)) {
            Value username = this.readValue("username", this.readArguments(packet));
            Value message = this.readValue("message", this.readArguments(packet));

            if(username.getString().equalsIgnoreCase("Server")){
                System.out.println(username.getString() + ": " + message.getString());
                return;
            }
            Value chatroom = this.readValue("chatroom", this.readArguments(packet));

            Chatroom cr = client.chatroomManager.getChatroom(chatroom.getString());

            if(cr == null)
                return;

            cr.message(username.getString(), message.getString());

            if(client.chatroomManager.getSelectedChatroom() == cr){
                System.out.println(username.getString() + ": " + message.getString());
            }
        }
    }

    @Override
    public void sendPacket(String packet) throws PacketWriteException {
        this.defaultSendPacket(this.readArguments(packet));
    }
}
