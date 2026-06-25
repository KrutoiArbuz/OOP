package ru.nsu.masolygin.protocol;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.protocol.payload.EmptyPayload;
import ru.nsu.masolygin.protocol.payload.HelloPayload;
import ru.nsu.masolygin.protocol.payload.ResultPayload;
import ru.nsu.masolygin.protocol.payload.TaskPayload;

class MessageIOTest {

    private DataInputStream pipe(ByteArrayOutputStream baos) {
        return new DataInputStream(new ByteArrayInputStream(baos.toByteArray()));
    }

    @Test
    void writeReadHello() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MessageIO.write(new DataOutputStream(baos), MessageType.HELLO, new HelloPayload(42L));

        Message m = MessageIO.read(pipe(baos));
        assertEquals(MessageType.HELLO, m.type());
        assertEquals(42L, ((HelloPayload) m.payload()).workerId());
    }

    @Test
    void writeReadTask() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int[] nums = {2, 4, 6, 8};
        MessageIO.write(new DataOutputStream(baos), MessageType.TASK, new TaskPayload(7L, nums));

        Message m = MessageIO.read(pipe(baos));
        assertEquals(MessageType.TASK, m.type());
        TaskPayload p = (TaskPayload) m.payload();
        assertEquals(7L, p.taskId());
        assertArrayEquals(nums, p.numbers());
    }

    @Test
    void writeReadResult() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MessageIO.write(new DataOutputStream(baos), MessageType.RESULT,
            new ResultPayload(9L, true));

        Message m = MessageIO.read(pipe(baos));
        assertEquals(MessageType.RESULT, m.type());
        ResultPayload p = (ResultPayload) m.payload();
        assertEquals(9L, p.taskId());
        assertEquals(true, p.hasComposite());
    }

    @Test
    void writeReadCancel() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MessageIO.write(new DataOutputStream(baos), MessageType.CANCEL, new EmptyPayload());

        Message m = MessageIO.read(pipe(baos));
        assertEquals(MessageType.CANCEL, m.type());
    }

    @Test
    void writeReadPing() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MessageIO.write(new DataOutputStream(baos), MessageType.PING, new EmptyPayload());

        Message m = MessageIO.read(pipe(baos));
        assertEquals(MessageType.PING, m.type());
    }

    @Test
    void writeReadPong() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MessageIO.write(new DataOutputStream(baos), MessageType.PONG, new EmptyPayload());

        Message m = MessageIO.read(pipe(baos));
        assertEquals(MessageType.PONG, m.type());
    }

    @Test
    void multipleMessagesInRow() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        MessageIO.write(out, MessageType.HELLO, new HelloPayload(1L));
        MessageIO.write(out, MessageType.TASK, new TaskPayload(2L, new int[]{3, 5, 7}));
        MessageIO.write(out, MessageType.RESULT, new ResultPayload(2L, false));

        DataInputStream in = pipe(baos);
        assertEquals(MessageType.HELLO, MessageIO.read(in).type());
        assertEquals(MessageType.TASK, MessageIO.read(in).type());
        assertEquals(MessageType.RESULT, MessageIO.read(in).type());
    }

    @Test
    void negativeLengthThrows() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        try {
            out.writeByte(MessageType.HELLO.code());
            out.writeInt(-1);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThrows(IOException.class, () -> MessageIO.read(pipe(baos)));
    }

    @Test
    void hugeLengthThrows() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        try {
            out.writeByte(MessageType.HELLO.code());
            out.writeInt(Integer.MAX_VALUE);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThrows(IOException.class, () -> MessageIO.read(pipe(baos)));
    }
}
