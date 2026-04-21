package ru.nsu.masolygin.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class ConfigLoadExceptionTest {

    @Test
    void testExceptionCreationWithMessage() {
        String message = "Test error message";
        ConfigLoadException exception = new ConfigLoadException(message, null);
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionCreationWithCause() {
        String message = "Test error";
        Throwable cause = new RuntimeException("Root cause");
        ConfigLoadException exception = new ConfigLoadException(message, cause);
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testIsRuntimeException() {
        ConfigLoadException exception = new ConfigLoadException("Test", null);
        assertNotNull(exception);
    }
}

