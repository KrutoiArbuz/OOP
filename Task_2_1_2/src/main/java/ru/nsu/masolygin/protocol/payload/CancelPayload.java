package ru.nsu.masolygin.protocol.payload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public record CancelPayload(long taskId) implements Payload {

    @Override
    public byte[] encode() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new DataOutputStream(baos).writeLong(taskId);
        return baos.toByteArray();
    }

    public static CancelPayload decode(byte[] payload) throws IOException {
        DataInputStream d = new DataInputStream(new ByteArrayInputStream(payload));
        return new CancelPayload(d.readLong());
    }
}