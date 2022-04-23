package xyz.sqlskid.skidchat.client;

import me.hideri.ext.client.packet.HeaderStorage;
import me.hideri.ext.client.packet.PacketManager;
import me.hideri.ext.client.packet.PacketWriteException;

public class KeepAlive extends Thread{

    private PacketManager packetManager;

    public KeepAlive(PacketManager packetManager){
        this.packetManager = packetManager;
    }

    public void run(){
        while (true){
            try{
                packetManager.getPacketById(HeaderStorage.instance.KEEPALIVE.getId()).sendPacket("");
                sleep(5000);
            }catch (Exception ignored){} catch (PacketWriteException e) {
                e.printStackTrace();
            }
        }
    }

}
