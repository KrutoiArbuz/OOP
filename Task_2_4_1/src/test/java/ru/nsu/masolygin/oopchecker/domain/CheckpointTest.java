package ru.nsu.masolygin.oopchecker.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CheckpointTest {

    private static final LocalDate START = LocalDate.of(2025, 9, 13);
    private static final LocalDate END = LocalDate.of(2025, 12, 27);

    @Test
    void startDateIsPresentWhenProvided() {
        Checkpoint cp = new Checkpoint("КТ1", START, END);
        assertEquals(Optional.of(START), cp.startDate());
    }

    @Test
    void startDateIsEmptyWhenNotProvided() {
        Checkpoint cp = new Checkpoint("КТ1", null, END);
        assertFalse(cp.startDate().isPresent());
    }

    @Test
    void allFieldsAreStored() {
        Checkpoint cp = new Checkpoint("КТ2", START, END);
        assertAll(
            () -> assertEquals("КТ2", cp.name()),
            () -> assertEquals(START, cp.internalStartDate()),
            () -> assertEquals(END, cp.date())
        );
    }

    @Test
    void nameIsRequired() {
        assertThrows(NullPointerException.class,
            () -> new Checkpoint(null, START, END));
    }

    @Test
    void dateIsRequired() {
        assertThrows(NullPointerException.class,
            () -> new Checkpoint("КТ1", START, null));
    }

    @Test
    void recordEqualityWithSameStartDate() {
        Checkpoint a = new Checkpoint("КТ1", START, END);
        Checkpoint b = new Checkpoint("КТ1", START, END);
        assertEquals(a, b);
    }

    @Test
    void differentNamesProduceDifferentObjects() {
        Checkpoint a = new Checkpoint("КТ1", null, END);
        Checkpoint b = new Checkpoint("КТ2", null, END);
        assertFalse(a.equals(b));
    }

    @Test
    void startBeforeDateIsTypicalUseCase() {
        Checkpoint cp = new Checkpoint("КТ1", START, END);
        assertTrue(cp.startDate().orElseThrow().isBefore(cp.date()));
    }
}
