package me.hideri.ext.server.packet.impl;

import me.hideri.ext.server.packet.HeaderStorage;
import me.hideri.ext.server.packet.Packet;
import me.hideri.ext.server.packet.PacketReadException;
import me.hideri.ext.server.packet.PacketWriteException;
import xyz.sqlskid.skidchat.server.Server;
import xyz.sqlskid.skidchat.server.client.Client;

import java.util.Base64;

public class EncryptedPacket extends Packet {
    public EncryptedPacket(Client client) {
        super(HeaderStorage.instance.ENCRYPTED, client);
    }

    @Override
    public void readPacket(String packet) throws PacketReadException, PacketWriteException {
        Value data = this.readValue("data", this.readArguments(packet));
        Value key = this.readValue("key", this.readArguments(packet));

        try {
            String decryptedPacket = Server.instance.aes.decrypt(data.getString(), Base64.getDecoder().decode(Server.instance.rsa.decrypt(key.getString(), Server.instance.rsa.getKeyPair(client).getPrivate())));
            Packet.Header header = Packet.readHeader(decryptedPacket);
            Packet p = client.packetManager.getPacketById(header.getId());

            if (!client.loggedIn) {
                if (!(p instanceof LoginPacket)) {
                    client.getSocket().close();
                    Server.instance.clientList.remove(client);
                    Server.instance.rsa.removeKey(client);
                    Server.instance.aes.removeKey(client);
                    System.out.println("Client dropped. Reason: Failed to login");
                    return;
                }
            }

            if(p == null)
                return;

            p.readPacket(decryptedPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendPacket(String packet) throws PacketWriteException {
        Value key = null;
        try {
            key = Value.construct("key", Server.instance.rsa.encrypt(Base64.getEncoder().encodeToString(Server.instance.aes.getSecretKey(client).getEncoded()), client.theirKey));
        } catch (Exception e) {
            e.printStackTrace();
        };
        Value data = null;
        try {
            data = Value.construct("data",Server.instance.aes.encrypt(this.readValue("data", this.readArguments(packet)).getString(), Server.instance.aes.getSecretKey(client)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.defaultSendPacket(key, data);
    }
}
