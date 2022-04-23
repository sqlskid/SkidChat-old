package xyz.sqlskid.skidchat.server.command.impl;

import xyz.sqlskid.skidchat.server.Server;
import xyz.sqlskid.skidchat.server.client.Client;
import xyz.sqlskid.skidchat.server.client.Rank;
import xyz.sqlskid.skidchat.server.command.Command;

import javax.crypto.SecretKey;
import java.io.IOException;

public class Kick extends Command {
    public Kick() {
        super("kick", "kick someone from a server", Rank.DEV.getLvl());
        addAlias("kick");
    }

    @Override
    public void execute(Client client, String[] args) {
        if(args.length >= 1) {
            String name = args[0];
            Client c = Server.instance.getClientByName(name);
            if(c != null){
                try {
                    c.getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
