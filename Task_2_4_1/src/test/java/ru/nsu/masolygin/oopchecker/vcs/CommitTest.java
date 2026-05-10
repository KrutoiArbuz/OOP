package ru.nsu.masolygin.oopchecker.vcs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class CommitTest {

    @Test
    void allFieldsAreAccessible() {
        Instant ts = Instant.parse("2024-03-15T10:30:00Z");
        Commit c = new Commit("abc", ts, "Ivan", "ivan@example.com", "subject");
        assertEquals("abc", c.hash());
        assertEquals(ts, c.timestamp());
        assertEquals("Ivan", c.authorName());
        assertEquals("ivan@example.com", c.authorEmail());
        assertEquals("subject", c.subject());
    }

    @Test
    void recordEqualityByValue() {
        Instant ts = Instant.parse("2024-03-15T10:30:00Z");
        Commit a = new Commit("h", ts, "n", "e", "s");
        Commit b = new Commit("h", ts, "n", "e", "s");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void emptySubjectIsAllowed() {
        Commit c = new Commit("h", Instant.now(), "n", "e", "");
        assertEquals("", c.subject());
    }
}
