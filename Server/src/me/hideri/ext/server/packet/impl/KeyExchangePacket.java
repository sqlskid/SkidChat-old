package me.hideri.ext.server.packet.impl;

import me.hideri.ext.server.packet.HeaderStorage;
import me.hideri.ext.server.packet.Packet;
import me.hideri.ext.server.packet.PacketReadException;
import me.hideri.ext.server.packet.PacketWriteException;
import xyz.sqlskid.skidchat.server.Server;
import xyz.sqlskid.skidchat.server.client.Client;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyExchangePacket extends Packet {

    public KeyExchangePacket(Client client) {
        super(HeaderStorage.instance.KEYEXCHANGE, client);
    }

    @Override
    public void readPacket(String packet) throws PacketReadException, PacketWriteException {
        if (client.theirKey != null)
            return;

        Value publicKey = this.readValue("publicKey", this.readArguments(packet));
        byte[] encodedPb = Base64.getDecoder().decode(publicKey.getBytes());
        try {
            KeyFactory RSA = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpecPb = new X509EncodedKeySpec(encodedPb);
            this.sendPacket("");
            client.theirKey = RSA.generatePublic(keySpecPb);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPacket(String packet) throws PacketWriteException {
       Server.instance.rsa.generateKey(client);
       Value publicKey = Value.construct("publicKey", Base64.getEncoder().encode(Server.instance.rsa.getKeyPair(client).getPublic().getEncoded()));
       this.defaultSendPacket(publicKey);
    }
}
