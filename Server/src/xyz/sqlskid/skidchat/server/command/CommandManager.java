package xyz.sqlskid.skidchat.server.command;

import xyz.sqlskid.skidchat.server.client.Client;
import xyz.sqlskid.skidchat.server.command.impl.CRank;
import xyz.sqlskid.skidchat.server.command.impl.Chatrooms;
import xyz.sqlskid.skidchat.server.command.impl.Kick;
import xyz.sqlskid.skidchat.server.command.impl.Name;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommandManager {

    public final List<Command> commands = new ArrayList<>();

    public CommandManager(){
        addCommand(new Name());
        addCommand(new CRank());
        addCommand(new Kick());
        addCommand(new Chatrooms());
        System.out.println("Command manager successfully loaded!");
    }

    public boolean execute(String message, Client client){
        String[] arguments = message.split(" ");

        String commandName = arguments[0].substring(1);

        String[] args = new String[]{};
        if(arguments.length >= 2) {
            args = message.substring(arguments[0].length() + 1).split(" ");
        }
        for(Command command: commands){
            if(command.getAliases().contains(commandName) && client.getRank().getLvl() >= command.getMinLvl()){
                command.execute(client,args);
                return true;
            }
        }
        return false;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void addCommand(Command command){
        this.commands.add(command);
        System.out.println(command.getName() + "");
    }


}
