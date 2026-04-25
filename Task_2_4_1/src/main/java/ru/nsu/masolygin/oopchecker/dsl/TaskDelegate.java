package ru.nsu.masolygin.oopchecker.dsl;

import java.time.LocalDate;
import java.util.Objects;
import ru.nsu.masolygin.oopchecker.domain.Task;

/**
 * Делегат блока {@code task { ... }}. Синтаксис: {@code id '2_1_1'}, {@code maxPoints 10},
 * {@code path 'Task_2_1_1'}. Поле {@code path} необязательно — по умолчанию используется
 * {@code id}.
 */
public class TaskDelegate {

    private String id;
    private String name;
    private int maxPoints;
    private LocalDate softDeadline;
    private LocalDate hardDeadline;
    private String labPath;

    public void id(String value) {
        this.id = value;
    }

    public void name(String value) {
        this.name = value;
    }

    public void maxPoints(int value) {
        this.maxPoints = value;
    }

    public void path(String value) {
        this.labPath = value;
    }

    public void softDeadline(String isoDate) {
        this.softDeadline = LocalDate.parse(isoDate);
    }

    public void hardDeadline(String isoDate) {
        this.hardDeadline = LocalDate.parse(isoDate);
    }

    Task build() {
        Objects.requireNonNull(id, "task.id is required");
        Objects.requireNonNull(name, "task.name is required");
        return new Task(id, name, maxPoints, softDeadline, hardDeadline, labPath);
    }
}
