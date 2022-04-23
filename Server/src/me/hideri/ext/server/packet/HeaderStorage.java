package me.hideri.ext.server.packet;

import java.lang.reflect.Field;

public class HeaderStorage
{
    public static final HeaderStorage instance = new HeaderStorage();

    public final Packet.Header KEYEXCHANGE = new Packet.Header("KEYEXCHANGE", -2);
    public final Packet.Header ENCRYPTED = new Packet.Header("ENCRYPTED", -1);
    public final Packet.Header LOGIN = new Packet.Header("LOGIN", 0);
    public final Packet.Header KEEPALIVE = new Packet.Header("CHAT", 1);
    public final Packet.Header CHAT = new Packet.Header("CHAT", 2);
    public final Packet.Header CHATROOM = new Packet.Header("CHATROOM", 3);


    public Packet.Header getByID(long id)
    {
        for(Field field : this.getClass().getDeclaredFields())
        {
            field.setAccessible(true);

            if(field.getType().equals(Packet.Header.class))
            {
                try
                {
                    Packet.Header header = (Packet.Header) field.get(this);

                    if(header.getId() == id)
                    {
                        return header;
                    }
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
