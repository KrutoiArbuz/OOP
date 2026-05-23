package ru.nsu.masolygin.protocol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class MessageTypeTest {

    @Test
    void roundTripAllTypes() {
        for (MessageType t : MessageType.values()) {
            assertEquals(t, MessageType.fromCode(t.code()));
        }
    }

    @Test
    void helloCodeIsOne() {
        assertEquals(0x01, MessageType.HELLO.code());
    }

    @Test
    void taskCodeIsTwo() {
        assertEquals(0x02, MessageType.TASK.code());
    }

    @Test
    void resultCodeIsThree() {
        assertEquals(0x03, MessageType.RESULT.code());
    }

    @Test
    void cancelCodeIsFour() {
        assertEquals(0x04, MessageType.CANCEL.code());
    }

    @Test
    void pingCodeIsFive() {
        assertEquals(0x05, MessageType.PING.code());
    }

    @Test
    void pongCodeIsSix() {
        assertEquals(0x06, MessageType.PONG.code());
    }

    @Test
    void unknownCodeThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> MessageType.fromCode((byte) 0x7F));
    }
}
