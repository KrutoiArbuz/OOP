package ru.nsu.masolygin.master;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ChunkTest {

    @Test
    void storesTaskId() {
        Chunk c = new Chunk(42L, new int[]{1, 2, 3});
        assertEquals(42L, c.taskId());
    }

    @Test
    void storesNumbers() {
        int[] nums = {7, 11, 13};
        Chunk c = new Chunk(1L, nums);
        assertArrayEquals(nums, c.numbers());
    }

    @Test
    void emptyChunkIsAllowed() {
        Chunk c = new Chunk(0L, new int[]{});
        assertEquals(0, c.numbers().length);
    }
}
