package ru.nsu.masolygin.protocol.payload;

import java.io.IOException;

public interface Payload {

    byte[] encode() throws IOException;
}
