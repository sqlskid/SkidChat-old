package me.hideri.ext.server.packet.impl;

import me.hideri.ext.server.packet.HeaderStorage;
import me.hideri.ext.server.packet.Packet;
import me.hideri.ext.server.packet.PacketReadException;
import me.hideri.ext.server.packet.PacketWriteException;
import xyz.sqlskid.skidchat.server.Server;
import xyz.sqlskid.skidchat.server.client.Client;
import xyz.sqlskid.skidchat.server.room.Chatroom;

public class ChatPacket extends Packet {

    public ChatPacket(Client client) {
        super(HeaderStorage.instance.CHAT, client);
    }

    @Override
    public void readPacket(String packet) throws PacketReadException, PacketWriteException {
        Value username = Value.construct("username", "[" + client.getRank() + "] " + client.getName());
        Value message = this.readValue("message", this.readArguments(packet));
        Value chatroom = this.readValue("chatroom", this.readArguments(packet));

        if (message.getString().replaceAll(" ", "").length() < 1)
            return;

        if (!Server.instance.chatroomManager.getChatroom(chatroom.getString()).isPublic && !Server.instance.chatroomManager.getChatroom(chatroom.getString()).members.contains(this.client.getUuid()))
            return;

        if (message.getString().startsWith("/")) {
            Server.instance.commandManager.execute(message.getString(), client);
            return;
        }

        Chatroom cr = Server.instance.chatroomManager.getChatroom(chatroom.getString());

        if (cr == null)
            return;

        Server.instance.chatroomManager.message(cr.getName(), message.getString(), this.client.getUuid());

        if(cr.isAnonymous()){
            username = Value.construct("username", "Anonymous");
        }

        if(client.getUuid().equals(cr.owner)){
            username = Value.construct("username", "Host");
        }
        for (Client client : Server.instance.clientList) {
            try {
                if ((cr.members.contains(client.getUuid()) || cr.isPublic)) {
                    client.packetManager.getPacketById(HeaderStorage.instance.CHAT.getId()).sendPacket(this.constructPacket(username, message, chatroom));
                }
            } catch (PacketWriteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendPacket(String packet) throws PacketWriteException {
        this.defaultSendPacket(this.readArguments(packet));
    }
}
