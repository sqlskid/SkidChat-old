package xyz.sqlskid.skidchat.chatroom;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatroomManager  {

    private final List<Chatroom> chatrooms = new ArrayList<>();

    private Chatroom selectedChatroom;

    public ChatroomManager(){

    }

    public void setSelectedChatroom(Chatroom selectedChatroom) {
        this.selectedChatroom = selectedChatroom;
        System.out.print("\033\143");
        System.out.flush();
        System.out.println("Currently talking in: " + selectedChatroom.getName());
        for(String string: selectedChatroom.getMessages().keySet()){
            System.out.println(string + ": " + selectedChatroom.getMessages().get(string));
        }
    }

    public Chatroom getSelectedChatroom() {
        return selectedChatroom;
    }

    public void addChatroom(Chatroom chatroom){
        chatrooms.add(chatroom);
    }

    public void message(String chatroom, String name, String message){
        Chatroom cr = getChatroom(chatroom);
        if(cr == null)
            return;

        cr.getMessages().put(name,message);
    }

    public Chatroom getChatroom(String name){
        for (Chatroom chatroom: getChatrooms()){
            if(chatroom.getName().equalsIgnoreCase(name)) {
                return chatroom;
            }
        }
        return null;
    }

    public List<Chatroom> getChatrooms() {
        return chatrooms;
    }
}
