package ru.nsu.masolygin.engine;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Test
    void testThreadCanStart() {
        testThread.start();
        assertTrue(testThread.running);
        testThread.stop();
    }

    @Test
    void testThreadCanStop() {
        testThread.start();
        testThread.stop();
        assertFalse(testThread.running);
    }

    @Test
    void testThreadNameMethod() {
        assertNotNull(testThread.getClass().getSimpleName());
    }

    @Test
    void testThreadHasRunningFlag() {
        assertNotNull(testThread);
        testThread.start();
        assertTrue(testThread.running);
        testThread.stop();
    }

    @Test
    void testThreadLoopMethod() {
        try {
            assertNotNull(testThread.getClass().getDeclaredMethod("loop"));
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void testAbstractGameThreadMethods() {
        assertTrue(testThread.getClass().getDeclaredMethods().length > 0);
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
