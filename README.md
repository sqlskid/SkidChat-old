# SkidChat
Java encrypted chatting program, optimal for anonymous & unmoderated chatting/chatrooms.

## How to use
The best way to use SkidChat, is by hosting your own public/private server. If you cannot portforward/you don't have access to a VPS, you can still use the base server, or connect to a different by editing the joining command. You can still happily use the basic server, and create your own Anonymous chatroom on our server.

## Connecting
After you have downloaded the newest relase, you can join our default server by typing in the following command;

```java -jar Client.jar``` and It will automatically connect to the default server.

If you want to join a private server hosted by someone else, use the following command;

```java -jar Client.jar [EXAMPLE IP] [EXAMPLE PORT]``` and just change the IP and port to your likings.

## Creating a server
This part is very easy, just download the Server.jar file, and run the following command;

```java -jar Server.jar [EXAMPLE PORT]``` If you want to start a server on a custom port, change the example port to your liking. Otherwise you can use the default port, (3386). (DISCLAIMER: There can only be one server p/port. You cannot run multiple servers on the default port <3386>, thus we recommend using a random number whilst creating your own server.)

## Trusted servers
The only trusted server we have right now is the default one. (sqlskid) However this may change in the future!

## Commands
```
/name - Change your username for the application.
/rank - (DEV ONLY), Rank a member.
/kick - (DEV ONLY), Kick a member. 
/cr - Chatroom, following commands;

/cr create - Creates an anonymous chatroom.
/cr join - Joins a chatroom. e.g. FunnyChatroom69420
/cr remove - Deletes the following chatroom.
```

```
Host only commands, (for hosting your own server);
End - Ends your server. (Closes it, it can be re-opened later on a different port.)
Save - Saves all given ranks. 
```

## Credits
[Foreheadchann](https://github.com/Foreheadchann) - Packet system

[mythicalmane](https://github.com/mythicalmane) - Writing README.md :)
