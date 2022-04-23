package me.hideri.ext.client.packet;

import me.hideri.ext.client.packet.impl.EncryptedPacket;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.sqlskid.skidchat.SkidChat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public abstract class Packet
{
    private final Header header;
    public final SkidChat client;
    public static SkidChat staticClient;

    public Packet(Header header, SkidChat client)
    {
        this.header = header;
        this.client = client;
        staticClient = client;
    }

    public abstract void readPacket(String packet) throws PacketReadException, PacketWriteException;

    public abstract void sendPacket(String packet) throws PacketWriteException;

    public String constructPacket(Value... arguments)
    {
        JSONObject json = new JSONObject();

        json.put("header", this.constructHeader(header));
        json.put("arguments", this.constructArguments(arguments));

        // System.out.println(json.toString(2));

        return json.toString();
    }

    public String constructEncrypted(Value... arguments)
    {
        JSONObject json = new JSONObject();

        json.put("header", this.constructHeader(header));
        json.put("arguments", this.constructArguments(arguments));

        EncryptedPacket encryptedPacket = (EncryptedPacket) client.packetManager.getPacketById(HeaderStorage.instance.ENCRYPTED.getId());
        Value key = null;
        Value data = null;
        try {
            key = Value.construct("key", client.rsa.encrypt(Base64.getEncoder().encodeToString(client.aes.getSecretKey().getEncoded()), client.theirKey));
            data = Value.construct("data", client.aes.encrypt(json.toString(), client.aes.getSecretKey()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedPacket.constructPacket(key, data);
    }

    public void defaultSendPacket(Value... arguments)
    {
        try
        {
            if(client.theirKey == null) {
                this.client.output.writeUTF(this.constructPacket(arguments));
            }
            else {
                this.client.output.writeUTF(this.constructEncrypted(arguments));
            }
            //this.client.output.writeUTF(Server.instance.rsa.encrypt(this.constructPacket(arguments), client.theirKey));
            this.client.output.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void defaultSendPacket(List<Value> arguments)
    {
        try
        {
            Value[] values = new Value[arguments.size()];

            for(int i = 0; i < arguments.size(); i++)
            {
                values[i] = arguments.get(i);
            }
            if(client.theirKey == null) {
                this.client.output.writeUTF(this.constructPacket(values));
            }
            else {
                this.client.output.writeUTF(this.constructEncrypted(values));
            }
            this.client.output.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONArray constructHeader(Header header)
    {
        JSONArray jsonHeader = new JSONArray();
        JSONObject tempHeader = new JSONObject();
        tempHeader.put("type", header.getType());
        tempHeader.put("id", header.getId());
        jsonHeader.put(tempHeader);
        return jsonHeader;
    }

    private JSONArray constructArguments(Value... arguments)
    {
        JSONArray jsonArguments = new JSONArray();
        JSONObject tempArguments = new JSONObject();

        for(Value argument : arguments)
        {
            tempArguments.put(argument.getKey(), argument.getValue());
        }

        tempArguments.put("timestamp", System.currentTimeMillis());

        jsonArguments.put(tempArguments);
        return jsonArguments;
    }

    public static Header readHeader(String packet) {
        JSONObject json = null;
        json = new JSONObject(packet);
        JSONObject header = json.getJSONArray("header").getJSONObject(0);
        return new Header(header.getString("type"), header.getLong("id"));
    }

    public static List<Value> readArguments(String packet)
    {
        final List<Value> values = new ArrayList<>();

        JSONObject json = new JSONObject(packet);

        JSONObject args = json.getJSONArray("arguments").getJSONObject(0);

        for(int i = 0; i < args.keySet().size(); i++)
        {
            String key = new ArrayList<>(args.keySet()).get(i);
            values.add(Value.construct(key, args.get(key)));
        }

        return values;
    }

    public static Value readValue(String key, List<Value> values)
    {
        for(Value value : values)
        {
            if(value.getKey().equals(key))
            {
                return value;
            }
        }

        return null;
    }

    public boolean headerMatches(Header header)
    {
        return this.header.getId() == header.getId() && this.header.getType().equals(header.getType());
    }

    public static class Value
    {
        private final String key;
        private final Object value;

        public Value(String key, Object value)
        {
            this.key = key;
            this.value = value;
        }

        public static Value construct(String key, Object value)
        {
            return new Value(key, value);
        }

        public String getKey()
        {
            return key;
        }

        public Object getValue()
        {
            return value;
        }

        public String getString()
        {
            return String.valueOf(value);
        }

        public Object[] getArray()
        {
            return this.getString().replace("[", "").replace("]", "").split(",");
        }

        public int getInteger()
        {
            return Integer.parseInt(this.getString());
        }

        public short getShort()
        {
            return Short.parseShort(this.getString());
        }

        public long getLong()
        {
            return Long.parseLong(this.getString());
        }

        public float getFloat()
        {
            return Float.parseFloat(this.getString());
        }

        public double getDouble()
        {
            return Double.parseDouble(this.getString());
        }

        public boolean getBoolean()
        {
            return Boolean.parseBoolean(this.getString());
        }

        public byte[] getBytes()
        {
            Object[] array = this.getArray();
            byte[] byteArray = new byte[array.length];

            for(int i = 0; i < array.length; i++)
            {
                int b = Integer.parseInt(String.valueOf(array[i]));
                boolean exceeds_max = b < Byte.MIN_VALUE || b > Byte.MAX_VALUE;
                byteArray[i] = (exceeds_max ? (byte) clamp(b, -128, 127) : (byte) Integer.parseInt(String.valueOf(array[i])));
            }

            return byteArray;
        }

        private int clamp(int val, int min, int max)
        {
            return val < min ? min : Math.min(val, max);
        }
    }

    public static class Header
    {
        private final String type;
        private final long id;

        public Header(String type, long id)
        {
            this.type = type;
            this.id = id;
        }

        public String getType()
        {
            return type;
        }

        public long getId()
        {
            return id;
        }
    }
}
