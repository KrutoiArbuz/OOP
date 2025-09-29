package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ParseExceptionTest {

    @Test
    void testMessageConstructor() {
        String message = "Invalid expression";
        ParseException exception = new ParseException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testMessageAndCauseConstructor() {
        String message = "Parse error occurred";
        Throwable cause = new IllegalArgumentException("Invalid character");
        ParseException exception = new ParseException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testThrowAndCatch() {
        String expectedMessage = "Test exception";

        ParseException thrown = assertThrows(ParseException.class, () -> {
            throw new ParseException(expectedMessage);
        });

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void testInheritanceFromRuntimeException() {
        ParseException exception = new ParseException("Test");

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testEmptyMessage() {
        ParseException exception = new ParseException("");

        assertEquals("", exception.getMessage());
    }

    @Test
    void testNullMessage() {
        ParseException exception = new ParseException(null);

        assertNull(exception.getMessage());
    }
}
