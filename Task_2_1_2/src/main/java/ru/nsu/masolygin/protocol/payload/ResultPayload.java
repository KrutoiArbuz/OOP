package ru.nsu.masolygin.protocol.payload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public record ResultPayload(long taskId, boolean hasComposite) implements Payload {

    @Override
    public byte[] encode() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream d = new DataOutputStream(baos);
        d.writeLong(taskId);
        d.writeBoolean(hasComposite);
        return baos.toByteArray();
    }

    public static ResultPayload decode(byte[] payload) throws IOException {
        DataInputStream d = new DataInputStream(new ByteArrayInputStream(payload));
        return new ResultPayload(d.readLong(), d.readBoolean());
    }
}