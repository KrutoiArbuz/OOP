package ru.nsu.masolygin.master;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TaskQueueTest {

    @Test
    void chunksCutBySize() {
        TaskQueue q = new TaskQueue(new int[]{1, 2, 3, 4, 5}, 2);
        assertEquals(2, q.pollPending().numbers().length);
        assertEquals(2, q.pollPending().numbers().length);
        assertEquals(1, q.pollPending().numbers().length);
        assertNull(q.pollPending());
    }

    @Test
    void taskIdsAreSequential() {
        TaskQueue q = new TaskQueue(new int[]{1, 2, 3, 4}, 1);
        assertEquals(0, q.pollPending().taskId());
        assertEquals(1, q.pollPending().taskId());
        assertEquals(2, q.pollPending().taskId());
        assertEquals(3, q.pollPending().taskId());
    }

    @Test
    void pollAllMakesAllInProgress() {
        TaskQueue q = new TaskQueue(new int[]{1, 2}, 1);
        Chunk a = q.pollPending();
        Chunk b = q.pollPending();
        assertTrue(q.isKnown(a.taskId()));
        assertTrue(q.isKnown(b.taskId()));
    }

    @Test
    void markDoneCompletesTask() {
        TaskQueue q = new TaskQueue(new int[]{1, 2}, 1);
        Chunk c = q.pollPending();
        q.markDone(c.taskId());
        assertTrue(q.isKnown(c.taskId()));
    }

    @Test
    void isAllDoneTrueAfterAllMarked() {
        TaskQueue q = new TaskQueue(new int[]{1, 2}, 1);
        q.markDone(q.pollPending().taskId());
        q.markDone(q.pollPending().taskId());
        assertTrue(q.isAllDone());
    }

    @Test
    void isAllDoneFalseInitially() {
        TaskQueue q = new TaskQueue(new int[]{1, 2}, 1);
        assertFalse(q.isAllDone());
    }

    @Test
    void reassignReturnsToPending() {
        TaskQueue q = new TaskQueue(new int[]{10, 20}, 1);
        Chunk c = q.pollPending();
        q.reassign(c.taskId());
        Chunk again = q.pollPending();
        assertNotNull(again);
        assertEquals(c.taskId(), again.taskId());
    }

    @Test
    void reassignDoneIsNoop() {
        TaskQueue q = new TaskQueue(new int[]{1}, 1);
        Chunk c = q.pollPending();
        q.markDone(c.taskId());
        q.reassign(c.taskId());
        assertNull(q.pollPending());
    }

    @Test
    void isKnownFalseForUnknownId() {
        TaskQueue q = new TaskQueue(new int[]{1, 2}, 1);
        assertFalse(q.isKnown(999L));
    }

    @Test
    void emptyArrayMakesNoChunks() {
        TaskQueue q = new TaskQueue(new int[]{}, 5);
        assertNull(q.pollPending());
        assertTrue(q.isAllDone());
    }

    @Test
    void chunkSizeBiggerThanArrayGivesOneChunk() {
        TaskQueue q = new TaskQueue(new int[]{1, 2, 3}, 100);
        Chunk c = q.pollPending();
        assertNotNull(c);
        assertEquals(3, c.numbers().length);
        assertNull(q.pollPending());
    }
}
