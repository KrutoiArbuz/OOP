package ru.nsu.masolygin.protocol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.protocol.payload.EmptyPayload;
import ru.nsu.masolygin.protocol.payload.HelloPayload;

class MessageTest {

    @Test
    void typeIsStored() {
        Message m = new Message(MessageType.PING, new EmptyPayload());
        assertEquals(MessageType.PING, m.type());
    }

    @Test
    void payloadIsStored() {
        EmptyPayload p = new EmptyPayload();
        Message m = new Message(MessageType.CANCEL, p);
        assertSame(p, m.payload());
    }

    @Test
    void equalsByValue() {
        Message a = new Message(MessageType.HELLO, new HelloPayload(5L));
        Message b = new Message(MessageType.HELLO, new HelloPayload(5L));
        assertEquals(a, b);
    }
}
