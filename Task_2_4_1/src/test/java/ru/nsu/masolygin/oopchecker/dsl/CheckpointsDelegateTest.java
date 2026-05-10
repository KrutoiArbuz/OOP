package ru.nsu.masolygin.oopchecker.dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.oopchecker.domain.Checkpoint;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfigBuilder;

class CheckpointsDelegateTest {

    private CourseConfigBuilder builder;
    private CheckpointsDelegate delegate;

    @BeforeEach
    void setUp() {
        builder = new CourseConfigBuilder();
        delegate = new CheckpointsDelegate(builder);
    }

    @Test
    void minimalCheckpointHasOnlyDate() {
        Map<String, Object> args = new HashMap<>();
        args.put("name", "КТ1");
        args.put("date", "2024-06-01");
        delegate.checkpoint(args);

        Checkpoint cp = builder.build().checkpoints().get(0);
        assertEquals("КТ1", cp.name());
        assertEquals(LocalDate.of(2024, 6, 1), cp.date());
        assertFalse(cp.startDate().isPresent());
    }

    @Test
    void checkpointWithStartDateIsCreated() {
        Map<String, Object> args = new HashMap<>();
        args.put("name", "КТ2");
        args.put("startDate", "2024-02-01");
        args.put("date", "2024-06-01");
        delegate.checkpoint(args);

        Checkpoint cp = builder.build().checkpoints().get(0);
        assertEquals(LocalDate.of(2024, 2, 1), cp.startDate().orElseThrow());
    }

    @Test
    void invalidDateFormatThrows() {
        Map<String, Object> args = new HashMap<>();
        args.put("name", "КТ");
        args.put("date", "not-a-date");
        assertThrows(java.time.format.DateTimeParseException.class,
            () -> delegate.checkpoint(args));
    }

    @Test
    void missingDateThrows() {
        Map<String, Object> args = new HashMap<>();
        args.put("name", "КТ");
        assertThrows(NullPointerException.class, () -> delegate.checkpoint(args));
    }
}
