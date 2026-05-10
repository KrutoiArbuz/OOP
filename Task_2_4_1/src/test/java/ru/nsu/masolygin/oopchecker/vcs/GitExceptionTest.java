package ru.nsu.masolygin.oopchecker.vcs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GitExceptionTest {

    @Test
    void messageOnlyConstructorPreservesMessage() {
        GitException e = new GitException("oops");
        assertEquals("oops", e.getMessage());
    }

    @Test
    void messageAndCauseConstructorPreservesBoth() {
        Throwable cause = new RuntimeException("inner");
        GitException e = new GitException("outer", cause);
        assertEquals("outer", e.getMessage());
        assertSame(cause, e.getCause());
    }

    @Test
    void isRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(GitException.class));
    }
}
