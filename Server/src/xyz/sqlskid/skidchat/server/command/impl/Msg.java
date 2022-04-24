package xyz.sqlskid.skidchat.server.command.impl;

import xyz.sqlskid.skidchat.server.Server;
import xyz.sqlskid.skidchat.server.client.Client;
import xyz.sqlskid.skidchat.server.client.Rank;
import xyz.sqlskid.skidchat.server.command.Command;

import java.util.StringJoiner;

public class Msg extends Command {

    public Msg() {
        super("msg", "send someone a message", Rank.GUEST.getLvl());
        addAlias("msg");
        addAlias("dm");
    }

    @Override
    public void execute(Client client, String[] args) {
        if(args.length < 2)
            return;

        String name = args[0];
        Client c = Server.instance.getClientByName(name);
        if(c == null)
            return;

        if(c.ignoreList.contains(client.getUuid()))
        {
            respond(client, "You can't message this user!");
            return;
        }

        StringJoiner message = new StringJoiner(" ");
        for(int i=1;i<args.length;i++){
            message.add(args[i]);
        }

        respond(c, "[" + client.getName() + " -> You]",message.toString());
        respond(client, "[You -> " + client.getName() + "]" ,message.toString());
    }
}
