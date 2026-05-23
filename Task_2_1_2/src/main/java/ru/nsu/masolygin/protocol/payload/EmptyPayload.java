package ru.nsu.masolygin.protocol.payload;

public record EmptyPayload() implements Payload {

    public static EmptyPayload decode(byte[] payload) {
        return new EmptyPayload();
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}