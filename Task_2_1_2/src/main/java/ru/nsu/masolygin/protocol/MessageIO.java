package ru.nsu.masolygin.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import ru.nsu.masolygin.protocol.payload.EmptyPayload;
import ru.nsu.masolygin.protocol.payload.HelloPayload;
import ru.nsu.masolygin.protocol.payload.Payload;
import ru.nsu.masolygin.protocol.payload.ResultPayload;
import ru.nsu.masolygin.protocol.payload.TaskPayload;

/**
 * Утилита для сериализации и десериализации сообщений.
 */
public class MessageIO {

    private static final int MAX_PAYLOAD_LENGTH = 64 * 1024 * 1024;

    /**
     * Записывает сообщение в выходной поток.
     *
     * @param out     выходной поток
     * @param type    тип сообщения
     * @param payload полезная нагрузка сообщения
     * @throws IOException при ошибке записи
     */
    public static void write(DataOutputStream out, MessageType type, Payload payload)
        throws IOException {
        byte[] bytes = payload.encode();

        synchronized (out) {
            out.writeByte(type.code());
            out.writeInt(bytes.length);
            out.write(bytes);
            out.flush();
        }
    }

    /**
     * Читает сообщение из входного потока.
     *
     * @param in входной поток
     * @return прочитанное сообщение
     * @throws IOException при ошибке чтения
     */
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