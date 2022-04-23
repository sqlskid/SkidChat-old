package xyz.sqlskid.skidchat.encryption;


import javax.crypto.Cipher;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

public class RSA {

    public KeyPair keyPair;

    public KeyPairGenerator generator;

    public RSA() throws NoSuchAlgorithmException {
        generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
    }

    public void generateKey(){
       keyPair = generator.generateKeyPair();
    }

    public KeyPair getKeyPair(){
        return keyPair;
    }

    private String encode(byte[] data){
        return Base64.getEncoder().encodeToString(data);
    }
    private byte[] decode(String data){
        return Base64.getDecoder().decode(data);
    }

    public String encrypt(String message, PublicKey publicKey) throws Exception{
        byte[] messageToBytes = message.getBytes();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        byte[] encryptedBytes = cipher.doFinal(messageToBytes);
        return encode(encryptedBytes);
    }

    public String decrypt(String encryptedMessage, PrivateKey privateKey) throws Exception{
        byte[] encryptedBytes = decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedBytes);
        return new String(decryptedMessage,"UTF8");
    }

}
