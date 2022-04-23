import me.hideri.ext.client.packet.HeaderStorage;
import me.hideri.ext.client.packet.Packet;
import me.hideri.ext.client.packet.PacketWriteException;
import me.hideri.ext.client.packet.impl.ChatPacket;
import xyz.sqlskid.skidchat.SkidChat;
import xyz.sqlskid.skidchat.chatroom.Chatroom;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.Scanner;
import java.util.UUID;

public class Start {

    public static void main(String[] args) throws PacketWriteException {
        String fileName = "skidchatlogin";
        String ip = "82.202.65.61";
        int port = 3386;
        if(args.length >= 2){
            ip = args[0];
            port = Integer.parseInt(args[1]);
            fileName = ip.replace(".", "_") + "login";
        }

        File file = new File(System.getProperty("user.home") + FileSystems.getDefault().getSeparator() + fileName + ".uuid");
        UUID uuid = null;
        if(!file.exists())
        {
            uuid = UUID.randomUUID();
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(uuid.toString());
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                Scanner scanner = new Scanner(new FileReader(file));
                uuid = UUID.fromString(scanner.nextLine());
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        SkidChat skidChat = new SkidChat(uuid, ip, port);
        skidChat.start();

        Scanner scanner = new Scanner(System.in);

        String text;
        while (scanner.hasNextLine()){
            text = scanner.nextLine();
            if(text.startsWith("#")){
                System.out.println("Lol"
                );
                String chatroom = text.replace("#","");
                Chatroom cr = skidChat.chatroomManager.getChatroom(chatroom);
                if(cr != null){
                    skidChat.chatroomManager.setSelectedChatroom(cr);
                }
            }else {

                Packet.Value message = Packet.Value.construct("message", text);
                Packet.Value chatRoom = Packet.Value.construct("chatroom", skidChat.chatroomManager.getSelectedChatroom().getName());
                ChatPacket chatPacket = (ChatPacket) skidChat.packetManager.getPacketById(HeaderStorage.instance.CHAT.getId());
                chatPacket.sendPacket(chatPacket.constructPacket(message, chatRoom));
            }
        }
    }

}
