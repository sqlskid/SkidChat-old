package xyz.sqlskid.skidchat.server.encryption;

import xyz.sqlskid.skidchat.server.client.Client;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;

public class AES {

    public KeyGenerator generator;

    public HashMap<Client, SecretKey> keys = new HashMap<>();

    public AES() throws NoSuchAlgorithmException
    {
        generator = KeyGenerator.getInstance("AES");
        generator.init(128);
    }

    public void generateKey(Client client)
    {
        if(keys.containsKey(client))
            return;

        keys.put(client, generator.generateKey());
    }

    public SecretKey getSecretKey(Client client){
        return keys.get(client);
    }

    private String encode(byte[] data){
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data){
        return Base64.getDecoder().decode(data);
    }

    public String encrypt(String message, SecretKey key) throws Exception{
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] byteCipherText = aesCipher.doFinal(message.getBytes());
        return encode(byteCipherText);
    }

    public String decrypt(String encryptedMessage, byte[] decryptedKey) throws Exception{
        SecretKey originalKey = new SecretKeySpec(decryptedKey , 0, decryptedKey .length, "AES");
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
        byte[] bytePlainText = aesCipher.doFinal(decode(encryptedMessage));
        return new String(bytePlainText);
    }

    public void removeKey(Client client) {
        keys.remove(client);
    }
}
