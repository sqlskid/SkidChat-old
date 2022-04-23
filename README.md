# SkidChat
Java encrypted chat. Good for very private conversations.

## How to use
The best way to use SkidChat is to host it yourself but if you can't portforward or you don't have a vps then the only solution left is to connecting to an existing server. You should only connect to trusted servers. (You can still make your own anonymous chatroom for only you)

## Connecting
If you have your client downloaded, you can start by opening your favourite terminal emulator (or cmd).
and then typing

```java -jar Client.jar``` and It will automatically connect to the default server.

If you wanna connect to a different server simply do:

```java -jar Client.jar 192.168.0.1 3386``` and just change the IP and port to your likings.

## Creating a server
This part is really easy, just download Server.jar and run it like this:

```java -jar Server.jar 3386``` and It will start the server on port 3386.

## Trusted servers
The only trusted server we have right now is the default one. (sqlskid) However this may change in the future!

## Commands
```
name - set a nickname
rank - set a rank for a user (DEV ONLY)
kick - kick a user (DEV ONLY)
chatroom - chatroom (create, remove, show all chatrooms)
```

```
console only commands:
end
save
```

## Credits
[Foreheadchann](https://github.com/Foreheadchann) - Packet system
