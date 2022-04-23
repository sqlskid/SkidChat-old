package xyz.sqlskid.skidchat.encryption;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class AES {

    public SecretKey key;
    public KeyGenerator generator;

    public AES() throws NoSuchAlgorithmException
    {
        generator = KeyGenerator.getInstance("AES");
        generator.init(128);

    }

    public void generateKey()
    {
        key = generator.generateKey();
    }

    public SecretKey getSecretKey(){
        return key;
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

}
