package xyz.sqlskid.skidchat.server.room;

import xyz.sqlskid.skidchat.server.client.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Chatroom {

    private String name;
    private boolean anonymous;
    public boolean visible;
    public List<UUID> members;
    public boolean isPublic;
    public HashMap<UUID, String> messages;
    public UUID owner;

    public Chatroom(String name, boolean anonymous){
        this.name = name;
        this.anonymous = anonymous;
        this.visible = true;
        this.isPublic = true;
        members = new ArrayList<>();
        messages = new HashMap<>();
        this.owner = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public void destroy() {
        messages = null;
        owner = null;
        members = null;
    }
}
