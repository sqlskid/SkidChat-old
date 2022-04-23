package me.hideri.ext.client.packet;

public class PacketReadException extends Throwable
{
    private final String reason;

    public PacketReadException()
    {
        this.reason = "Unexpected Value";
    }

    public PacketReadException(String reason)
    {
        this.reason = reason;
    }

    public PacketReadException(Class<?> clazz)
    {
        this.reason = clazz.getSimpleName() + " | Unexpected Value";
    }

    public PacketReadException(String reason, Class<?> clazz)
    {
        this.reason = clazz.getSimpleName() + " | " + reason;
    }

    public String getReason()
    {
        return reason;
    }
}
