package me.hideri.ext.client.packet.impl;

import me.hideri.ext.client.packet.HeaderStorage;
import me.hideri.ext.client.packet.Packet;
import me.hideri.ext.client.packet.PacketReadException;
import me.hideri.ext.client.packet.PacketWriteException;
import xyz.sqlskid.skidchat.SkidChat;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class KeyExchangePacket extends Packet {

    public KeyExchangePacket(SkidChat client) {
        super(HeaderStorage.instance.KEYEXCHANGE, client);
    }

    @Override
    public void readPacket(String packet) throws PacketReadException, PacketWriteException {

        Value publicKey = this.readValue("publicKey", this.readArguments(packet));
        byte[] encodedPb = Base64.getDecoder().decode(publicKey.getBytes());
        try {
            KeyFactory RSA = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpecPb = new X509EncodedKeySpec(encodedPb);
            client.theirKey = RSA.generatePublic(keySpecPb);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        System.out.println("Key exchange successful!");
        client.packetManager.getPacketById(HeaderStorage.instance.LOGIN.getId()).sendPacket("");
        client.keepAlive.start();
    }

    @Override
    public void sendPacket(String packet) throws PacketWriteException {
        System.out.println("Key exchange in progress...");
        client.rsa.generateKey();
        Value publicKey = Value.construct("publicKey", Base64.getEncoder().encode(client.rsa.keyPair.getPublic().getEncoded()));
        this.defaultSendPacket(publicKey);
    }
}
