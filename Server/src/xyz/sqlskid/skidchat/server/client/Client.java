package xyz.sqlskid.skidchat.server.client;

import me.hideri.ext.server.packet.PacketManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Client {

    public boolean loggedIn = false;
    private String name;
    private UUID uuid;
    private Rank rank;
    private Socket socket;

    public DataInputStream input;
    public DataOutputStream output;

    public PacketManager packetManager;
    public long ping = 0;

    public PublicKey theirKey;

    public Client(Socket socket) {
        this.socket = socket;
        this.name = "Cat" + ThreadLocalRandom.current().nextInt(10000, 99999);
        this.uuid = null;
        this.rank = Rank.GUEST;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Rank getRank() {
        return rank;
    }

    public Socket getSocket() {
        return socket;
    }
}
