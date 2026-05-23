package ru.nsu.masolygin.protocol.payload;

public record EmptyPayload() implements Payload {

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    public static EmptyPayload decode(byte[] payload) {
        return new EmptyPayload();
    }
}