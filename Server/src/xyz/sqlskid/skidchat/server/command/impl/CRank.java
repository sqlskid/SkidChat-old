package xyz.sqlskid.skidchat.server.command.impl;

import xyz.sqlskid.skidchat.server.Server;
import xyz.sqlskid.skidchat.server.client.Client;
import xyz.sqlskid.skidchat.server.client.Rank;
import xyz.sqlskid.skidchat.server.command.Command;

public class CRank extends Command {

    public CRank() {
        super("rank", "set someone's rank", Rank.DEV.getLvl());
        addAlias("rank");
    }

    @Override
    public void execute(Client client, String[] args) {
        if(args.length >= 2){
            String clientName = args[0];
            Rank rank = Rank.valueOf(args[1]);

            if(rank == null)
                return;

            if(Server.instance.getClientByName(clientName) != null){
                Server.instance.getClientByName(clientName).setRank(rank);
                respond(client, "You set " + clientName + "'s rank to " + rank);
            }
        }
    }
}
