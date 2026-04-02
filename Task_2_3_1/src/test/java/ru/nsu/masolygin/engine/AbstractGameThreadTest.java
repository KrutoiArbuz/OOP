package ru.nsu.masolygin.engine;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbstractGameThreadTest {

    private TestGameThread testThread;

    @BeforeEach
    void setUp() {
        testThread = new TestGameThread();
    }

    @Test
    void testThreadCreation() {
        assertTrue(!testThread.running);
    }

    @Test
    void testInitialState() {
        assertFalse(testThread.running);
    }

    private static class TestGameThread extends AbstractGameThread {

        @Override
        protected String threadName() {
            return "TestThread";
        }

        @Override
        protected void loop() {
            while (running) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}

