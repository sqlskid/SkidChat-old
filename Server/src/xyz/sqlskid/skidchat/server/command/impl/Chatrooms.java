package xyz.sqlskid.skidchat.server.command.impl;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import me.hideri.ext.server.packet.HeaderStorage;
import me.hideri.ext.server.packet.Packet;
import me.hideri.ext.server.packet.impl.ChatroomPacket;
import xyz.sqlskid.skidchat.server.Server;
import xyz.sqlskid.skidchat.server.client.Client;
import xyz.sqlskid.skidchat.server.client.Rank;
import xyz.sqlskid.skidchat.server.command.Command;
import xyz.sqlskid.skidchat.server.room.Chatroom;

import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;

public class Chatrooms extends Command {

    public Chatrooms() {
        super("chatrooms", "command for chatrooms", Rank.GUEST.getLvl());
        addAlias("chatroom");
        addAlias("chatrooms");
        addAlias("cr");
    }

    @Override
    public void execute(Client client, String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()){
                case "create":
                    if(Server.instance.chatroomManager.getChatroomByOwner(client.getUuid()) != null)
                        return;

                    boolean anonymous = false;
                    if (args.length > 1) {
                        anonymous = args[1].equalsIgnoreCase("anonymous");
                    }
                    Chatroom chatroom = new Chatroom("FunnyChatroom" + ThreadLocalRandom.current().nextInt(100000,999999) , anonymous);
                    chatroom.visible = false;
                    chatroom.isPublic = false;
                    chatroom.owner = client.getUuid();
                    chatroom.members.add(client.getUuid());
                    Server.instance.chatroomManager.addChatroom(chatroom);
                    ChatroomPacket chatroomPacket = (ChatroomPacket) client.packetManager.getPacketById(HeaderStorage.instance.CHATROOM.getId());
                    Packet.Value mode = Packet.Value.construct("mode", "JOIN");
                    Packet.Value chatroomname = Packet.Value.construct("chatroom", chatroom.getName());
                    chatroomPacket.defaultSendPacket(mode,chatroomname);
                    break;
                case "remove":
                    if(Server.instance.chatroomManager.getChatroomByOwner(client.getUuid()) == null)
                        return;

                    Server.instance.chatroomManager.getChatroomByOwner(client.getUuid()).destroy();
                    break;
                case "join":
                    if(args.length > 1){
                        String name = args[1];
                        Chatroom cr = Server.instance.chatroomManager.getChatroom(name);
                        if(cr != null){
                            if(!cr.isPublic){
                                cr.members.add(client.getUuid());
                                ChatroomPacket cp = (ChatroomPacket) client.packetManager.getPacketById(HeaderStorage.instance.CHATROOM.getId());
                                Packet.Value m = Packet.Value.construct("mode", "JOIN");
                                Packet.Value crn = Packet.Value.construct("chatroom", cr.getName());
                                cp.defaultSendPacket(m,crn);
                            }
                        }
                    }
                    break;
            }
        } else {

            StringJoiner joiner = new StringJoiner("\n");

            for (Chatroom chatroom : Server.instance.chatroomManager.getChatrooms()) {
                if (chatroom.isPublic || chatroom.members.contains(client.getUuid())) {
                    joiner.add(chatroom.getName());
                }
            }

            respond(client, "Available chatrooms: " + joiner);
        }
    }
}
