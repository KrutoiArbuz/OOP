package ru.nsu.masolygin.oopchecker.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CheckpointTest {

    @Test
    void twoArgConstructorSetsNullStartDate() {
        Checkpoint cp = new Checkpoint("КТ1", LocalDate.of(2025, 12, 27));
        assertNull(cp.startDate());
        assertEquals(LocalDate.of(2025, 12, 27), cp.date());
        assertEquals("КТ1", cp.name());
    }

    @Test
    void threeArgConstructorSetsAllFields() {
        LocalDate start = LocalDate.of(2025, 9, 13);
        LocalDate end = LocalDate.of(2025, 12, 27);
        Checkpoint cp = new Checkpoint("КТ1", start, end);
        assertEquals("КТ1", cp.name());
        assertEquals(start, cp.startDate());
        assertEquals(end, cp.date());
    }

    @Test
    void checkpointWithoutStartDateIsNotNull() {
        Checkpoint cp = new Checkpoint("Итог", LocalDate.of(2026, 6, 30));
        assertNotNull(cp);
        assertNotNull(cp.name());
        assertNotNull(cp.date());
    }

    @Test
    void twoArgAndThreeArgWithNullStartAreEqual() {
        LocalDate date = LocalDate.of(2025, 12, 27);
        Checkpoint a = new Checkpoint("КТ1", date);
        Checkpoint b = new Checkpoint("КТ1", null, date);
        assertEquals(a, b);
    }

    @Test
    void startDateIsBeforeDate() {
        LocalDate start = LocalDate.of(2026, 2, 14);
        LocalDate end = LocalDate.of(2026, 5, 18);
        Checkpoint cp = new Checkpoint("КТ2", start, end);
        assertTrue(cp.startDate().isBefore(cp.date()));
    }

    @Test
    void sem1CheckpointCoversCorrectRange() {
        Checkpoint cp = new Checkpoint("КТ1",
            LocalDate.of(2025, 9, 13), LocalDate.of(2025, 12, 27));
        assertEquals(LocalDate.of(2025, 9, 13), cp.startDate());
        assertEquals(LocalDate.of(2025, 12, 27), cp.date());
    }

    @Test
    void sem2CheckpointCoversCorrectRange() {
        Checkpoint cp = new Checkpoint("КТ2",
            LocalDate.of(2026, 2, 14), LocalDate.of(2026, 5, 18));
        assertEquals(LocalDate.of(2026, 2, 14), cp.startDate());
        assertEquals(LocalDate.of(2026, 5, 18), cp.date());
    }
}
