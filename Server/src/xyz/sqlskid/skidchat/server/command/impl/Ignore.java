package xyz.sqlskid.skidchat.server.command.impl;

import xyz.sqlskid.skidchat.server.Server;
import xyz.sqlskid.skidchat.server.client.Client;
import xyz.sqlskid.skidchat.server.client.Rank;
import xyz.sqlskid.skidchat.server.command.Command;

public class Ignore extends Command {

    public Ignore() {
        super("ignore", "ignore a player", Rank.GUEST.getLvl());
        addAlias("ignore");
    }

    @Override
    public void execute(Client client, String[] args) {
        if(args.length >= 1){
            String clientName = args[0];
            Client c = Server.instance.getClientByName(clientName);
            if(c != null){
                if(client.ignoreList.contains(c.getUuid())){
                    client.ignoreList.remove(c.getUuid());
                    respond(client, "Stopped ignoring " + c.getName());
                }
                else {
                    client.ignoreList.add(c.getUuid());
                    respond(client, "Now ignoring " + c.getName());
                }

            }
        }
    }
}
