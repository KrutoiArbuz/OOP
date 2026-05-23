package ru.nsu.masolygin.protocol;

public enum MessageType {
    HELLO(0x01),
    TASK(0x02),
    RESULT(0x03),
    CANCEL(0x04),
    PING(0x05),
    PONG(0x06);

    private final byte code;

    MessageType(int code) { this.code = (byte) code; }
    public byte code() { return code; }

    public static MessageType fromCode(byte code) {
        for (MessageType t : values()) {
            if (t.code == code) return t;
        }
        throw new IllegalArgumentException("Unknown message type: " + code);
    }
}
