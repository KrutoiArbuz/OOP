package ru.nsu.masolygin.worker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ChunkProcessorTest {

    @Test
    void allPrimesReturnsAllPrime() {
        ChunkProcessor p = new ChunkProcessor(new int[]{2, 3, 5, 7, 11});
        assertEquals(ProcessResult.ALL_PRIME, p.run());
    }

    @Test
    void containsCompositeReturnsCompositeFound() {
        ChunkProcessor p = new ChunkProcessor(new int[]{2, 3, 4, 5});
        assertEquals(ProcessResult.COMPOSITE_FOUND, p.run());
    }

    @Test
    void emptyChunkReturnsAllPrime() {
        ChunkProcessor p = new ChunkProcessor(new int[]{});
        assertEquals(ProcessResult.ALL_PRIME, p.run());
    }

    @Test
    void cancelBeforeRunReturnsCancelled() {
        ChunkProcessor p = new ChunkProcessor(new int[]{2, 3, 5});
        p.cancel();
        assertEquals(ProcessResult.CANCELLED, p.run());
    }

    @Test
    void singleCompositeReturnsCompositeFound() {
        ChunkProcessor p = new ChunkProcessor(new int[]{4});
        assertEquals(ProcessResult.COMPOSITE_FOUND, p.run());
    }

    @Test
    void singlePrimeReturnsAllPrime() {
        ChunkProcessor p = new ChunkProcessor(new int[]{7});
        assertEquals(ProcessResult.ALL_PRIME, p.run());
    }

    @Test
    void oneIsNotPrime() {
        ChunkProcessor p = new ChunkProcessor(new int[]{1});
        assertEquals(ProcessResult.COMPOSITE_FOUND, p.run());
    }

    @Test
    void zeroIsNotPrime() {
        ChunkProcessor p = new ChunkProcessor(new int[]{0});
        assertEquals(ProcessResult.COMPOSITE_FOUND, p.run());
    }

    @Test
    void cancelFromAnotherThreadStopsRun() throws InterruptedException {
        int[] huge = new int[200_000];
        for (int i = 0; i < huge.length; i++) {
            huge[i] = Integer.MAX_VALUE;
        }
        ChunkProcessor p = new ChunkProcessor(huge);
        ProcessResult[] result = new ProcessResult[1];
        Thread t = new Thread(() -> result[0] = p.run());
        t.start();
        Thread.sleep(50);
        p.cancel();
        t.join(5000);
        assertEquals(ProcessResult.CANCELLED, result[0]);
    }
}
