package xyz.sqlskid.skidchat.server.command;

import me.hideri.ext.server.packet.HeaderStorage;
import me.hideri.ext.server.packet.Packet;
import me.hideri.ext.server.packet.PacketWriteException;
import me.hideri.ext.server.packet.impl.ChatPacket;
import xyz.sqlskid.skidchat.server.client.Client;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    private String name = "";
    private String description = "";
    private List<String> aliases = new ArrayList<>();
    private int minLvl;

    public Command(String name, String description, int minlvl){
        this.name = name;
        this.description = description;
        this.minLvl = minlvl;
    }

    public void respond(Client client, String message){
        ChatPacket packet = (ChatPacket) client.packetManager.getPacketById(HeaderStorage.instance.CHAT.getId());
        Packet.Value username = Packet.Value.construct("username", "Server");
        Packet.Value messag = Packet.Value.construct("message", message);
        try {
            packet.sendPacket(packet.constructPacket(username,messag));
        } catch (PacketWriteException e) {
            e.printStackTrace();
        }
    }

    public int getMinLvl() {
        return minLvl;
    }

    public abstract void execute(Client client, String[] args);

    public void addAlias(String alias){
        this.aliases.add(alias);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAliases() {
        return aliases;
    }
}
