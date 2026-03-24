package ru.nsu.masolygin.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Тест для PizzeriaException.
 */
class PizzeriaExceptionTest {

    @Test
    void testExceptionWithMessage() {
        TestPizzeriaException exception = new TestPizzeriaException("Test error");

        assertNotNull(exception);
        assertEquals("Test error", exception.getMessage());
    }

    @Test
    void testExceptionWithMessageAndCause() {
        Exception cause = new Exception("Root cause");
        TestPizzeriaException exception = new TestPizzeriaException("Test error", cause);

        assertNotNull(exception);
        assertEquals("Test error", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testExceptionInheritance() {
        TestPizzeriaException exception = new TestPizzeriaException("Test");

        assertTrue(exception instanceof Exception);
    }

    @Test
    void testConfigLoadExceptionIsSubclass() {
        ConfigLoadException exception =
            new ConfigLoadException("Config error", new Exception());

        assertTrue(exception instanceof PizzeriaException);
    }

    @Test
    void testPizzeriaInitializationExceptionIsSubclass() {
        PizzeriaInitializationException exception =
            new PizzeriaInitializationException("Init error");

        assertTrue(exception instanceof PizzeriaException);
    }

    /**
     * Конкретная реализация для тестирования абстрактного класса.
     */
    private static class TestPizzeriaException extends PizzeriaException {

        public TestPizzeriaException(String message) {
            super(message);
        }

        public TestPizzeriaException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}

