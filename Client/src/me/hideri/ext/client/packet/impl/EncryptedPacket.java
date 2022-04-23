package me.hideri.ext.client.packet.impl;

import me.hideri.ext.client.packet.HeaderStorage;
import me.hideri.ext.client.packet.Packet;
import me.hideri.ext.client.packet.PacketReadException;
import me.hideri.ext.client.packet.PacketWriteException;
import xyz.sqlskid.skidchat.SkidChat;

import java.util.Base64;

public class EncryptedPacket extends Packet {

    public EncryptedPacket(SkidChat client) {
        super(HeaderStorage.instance.ENCRYPTED, client);
    }

    @Override
    public void readPacket(String packet) throws PacketReadException, PacketWriteException {
        Value data = this.readValue("data", this.readArguments(packet));
        Value key = this.readValue("key", this.readArguments(packet));

        try {
            String decryptedPacket = client.aes.decrypt(data.getString(), Base64.getDecoder().decode(client.rsa.decrypt(key.getString(), client.rsa.keyPair.getPrivate())));
            Packet.Header header = Packet.readHeader(decryptedPacket);
            Packet p = client.packetManager.getPacketById(header.getId());

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
            key = Value.construct("key", client.rsa.encrypt(Base64.getEncoder().encodeToString(client.aes.key.getEncoded()), client.theirKey));
        } catch (Exception e) {
            e.printStackTrace();
        };
        Value data = null;
        try {
            data = Value.construct("data",client.aes.encrypt(this.readValue("data", this.readArguments(packet)).getString(), client.aes.key));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.defaultSendPacket(key, data);
    }
}
