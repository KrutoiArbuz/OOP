package ru.nsu.masolygin.oopchecker.dsl;

import java.time.LocalDate;
import java.util.Map;
import ru.nsu.masolygin.oopchecker.domain.Checkpoint;
import ru.nsu.masolygin.oopchecker.domain.CourseConfig;

/**
 * Делегат блока {@code checkpoints { ... }}. Синтаксис:
 * {@code checkpoint name: 'КТ1', date: '2023-11-01'}.
 */
public class CheckpointsDelegate {

    private final CourseConfig config;

    public CheckpointsDelegate(CourseConfig config) {
        this.config = config;
    }

    public void checkpoint(Map<String, Object> args) {
        String startDateStr = (String) args.get("startDate");
        LocalDate startDate = startDateStr != null ? LocalDate.parse(startDateStr) : null;
        config.addCheckpoint(new Checkpoint(
            (String) args.get("name"),
            startDate,
            LocalDate.parse((String) args.get("date"))
        ));
    }
}
