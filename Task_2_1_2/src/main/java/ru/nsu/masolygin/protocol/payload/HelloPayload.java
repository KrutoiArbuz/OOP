package ru.nsu.masolygin.protocol.payload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public record HelloPayload(long workerId) implements Payload {

    @Override
    public byte[] encode() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new DataOutputStream(baos).writeLong(workerId);
        return baos.toByteArray();
    }

    public static HelloPayload decode(byte[] payload) throws IOException {
        return new HelloPayload(new DataInputStream(new ByteArrayInputStream(payload)).readLong());
    }
}