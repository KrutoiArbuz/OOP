package ru.nsu.masolygin.oopchecker.dsl;

import java.time.LocalDate;
import java.util.Map;
import ru.nsu.masolygin.oopchecker.domain.Checkpoint;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfigBuilder;

/**
 * Делегат блока {@code checkpoints { ... }}. Синтаксис:
 * {@code checkpoint name: 'КТ1', date: '2023-11-01'}.
 */
public class CheckpointsDelegate {

    private final CourseConfigBuilder builder;


    public CheckpointsDelegate(CourseConfigBuilder builder) {
        this.builder = builder;
    }

    /**
     * Добавляет контрольную точку в конфигурацию.
     *
     * @param args именованные аргументы: name, date (обязательно), startDate (опционально)
     */
    public void checkpoint(Map<String, Object> args) {
        String startDateStr = (String) args.get("startDate");
        LocalDate startDate = startDateStr != null ? LocalDate.parse(startDateStr) : null;
        builder.addCheckpoint(new Checkpoint(
            (String) args.get("name"),
            startDate,
            LocalDate.parse((String) args.get("date"))
        ));
    }
}
