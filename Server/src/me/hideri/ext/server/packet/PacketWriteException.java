package me.hideri.ext.server.packet;

public class PacketWriteException extends Throwable
{
    private final String reason;

    public PacketWriteException()
    {
        this.reason = "Unexpected Value";
    }

    public PacketWriteException(String reason)
    {
        this.reason = reason;
    }

    public PacketWriteException(Class<?> clazz)
    {
        this.reason = clazz.getSimpleName() + " | Unexpected Value";
    }

    public PacketWriteException(String reason, Class<?> clazz)
    {
        this.reason = clazz.getSimpleName() + " | " + reason;
    }

    public String getReason()
    {
        return reason;
    }
}
