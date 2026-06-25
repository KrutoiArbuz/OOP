package ru.nsu.masolygin.protocol.payload;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class PayloadCodecTest {

    @Test
    void helloRoundTrip() throws IOException {
        HelloPayload original = new HelloPayload(123456789L);
        byte[] bytes = original.encode();
        HelloPayload decoded = HelloPayload.decode(bytes);
        assertEquals(original.workerId(), decoded.workerId());
    }

    @Test
    void helloNegativeId() throws IOException {
        HelloPayload original = new HelloPayload(-7L);
        HelloPayload decoded = HelloPayload.decode(original.encode());
        assertEquals(-7L, decoded.workerId());
    }

    @Test
    void resultTrueRoundTrip() throws IOException {
        ResultPayload original = new ResultPayload(42L, true);
        ResultPayload decoded = ResultPayload.decode(original.encode());
        assertEquals(42L, decoded.taskId());
        assertEquals(true, decoded.hasComposite());
    }

    @Test
    void resultFalseRoundTrip() throws IOException {
        ResultPayload original = new ResultPayload(7L, false);
        ResultPayload decoded = ResultPayload.decode(original.encode());
        assertEquals(7L, decoded.taskId());
        assertEquals(false, decoded.hasComposite());
    }

    @Test
    void taskRoundTrip() throws IOException {
        int[] numbers = {2, 3, 5, 7, 11, 13};
        TaskPayload original = new TaskPayload(100L, numbers);
        TaskPayload decoded = TaskPayload.decode(original.encode());
        assertEquals(100L, decoded.taskId());
        assertArrayEquals(numbers, decoded.numbers());
    }

    @Test
    void taskEmptyArrayRoundTrip() throws IOException {
        TaskPayload original = new TaskPayload(0L, new int[]{});
        TaskPayload decoded = TaskPayload.decode(original.encode());
        assertEquals(0L, decoded.taskId());
        assertEquals(0, decoded.numbers().length);
    }

    @Test
    void taskLargeArrayRoundTrip() throws IOException {
        int[] numbers = new int[10_000];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i;
        }
        TaskPayload original = new TaskPayload(1L, numbers);
        TaskPayload decoded = TaskPayload.decode(original.encode());
        assertArrayEquals(numbers, decoded.numbers());
    }

    @Test
    void emptyPayloadEncodeIsZeroBytes() {
        assertEquals(0, new EmptyPayload().encode().length);
    }

    @Test
    void emptyPayloadDecodeReturnsInstance() {
        assertEquals(new EmptyPayload(), EmptyPayload.decode());
    }
}
