package ru.nsu.masolygin.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import ru.nsu.masolygin.protocol.payload.EmptyPayload;
import ru.nsu.masolygin.protocol.payload.HelloPayload;
import ru.nsu.masolygin.protocol.payload.Payload;
import ru.nsu.masolygin.protocol.payload.ResultPayload;
import ru.nsu.masolygin.protocol.payload.TaskPayload;

public class MessageIO {
    private static final int MAX_PAYLOAD_LENGTH = 64 * 1024 * 1024;

    public static void write(DataOutputStream out, MessageType type, Payload payload) throws IOException {
        byte[] bytes = payload.encode();

        synchronized (out) {
            out.writeByte(type.code());
            out.writeInt(bytes.length);
            out.write(bytes);
            out.flush();
        }
    }


    public static Message read(DataInputStream in) throws IOException {
        byte code = in.readByte();
        int len = in.readInt();

        if (len < 0 || len > MAX_PAYLOAD_LENGTH) {
            throw new IOException("Invalid payload length: " + len);
        }

        byte[] bytes = new byte[len];
        in.readFully(bytes);

        MessageType type = MessageType.fromCode(code);

        Payload payload = switch (type) {
            case HELLO -> HelloPayload.decode(bytes);
            case TASK -> TaskPayload.decode(bytes);
            case RESULT -> ResultPayload.decode(bytes);
            case CANCEL, PING, PONG -> EmptyPayload.decode(bytes);
        };

        return new Message(type, payload);
    }
}