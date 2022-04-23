package xyz.sqlskid.skidchat.server.command.impl;

import xyz.sqlskid.skidchat.server.Server;
import xyz.sqlskid.skidchat.server.client.Client;
import xyz.sqlskid.skidchat.server.client.Rank;
import xyz.sqlskid.skidchat.server.command.Command;

public class Name extends Command {

    public Name() {
        super("name", "set your nickname", Rank.GUEST.getLvl());
        addAlias("name");
    }

    @Override
    public void execute(Client client, String[] args) {

        if(args.length > 0) {
            String newName = args[0];
            if(!Server.instance.clientDataManager.isLegal(newName))
                return;
            client.setName(newName);
            respond(client, "Your name has been set to " + newName + "!");
        }
    }
}
