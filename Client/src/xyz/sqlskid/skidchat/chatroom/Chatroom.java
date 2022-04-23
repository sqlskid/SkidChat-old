package xyz.sqlskid.skidchat.chatroom;

import me.hideri.ext.client.packet.impl.ChatPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Chatroom {

    private final String name;
    private final HashMap<String, String> messages;

    public Chatroom(String name){
        this.name = name;
        this.messages = new HashMap<>();
    }

    public void message(String name, String message){
        getMessages().put(name,message);
    }

    public String getName() {
        return name;
    }

    public HashMap<String, String> getMessages() {
        return messages;
    }
}
