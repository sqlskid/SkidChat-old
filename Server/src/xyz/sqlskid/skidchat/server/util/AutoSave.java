package xyz.sqlskid.skidchat.server.util;

import xyz.sqlskid.skidchat.server.Server;

public class AutoSave extends Thread{

    public void run(){
        while (true){
            try{
                Server.instance.clientDataManager.saveAllClients();
                Server.instance.clientDataManager.saveJsonData();
                sleep(300000);
            }catch (Exception ignored){}
        }
    }

}
