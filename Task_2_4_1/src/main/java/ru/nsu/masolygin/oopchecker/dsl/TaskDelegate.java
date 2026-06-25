package ru.nsu.masolygin.oopchecker.dsl;

import java.time.LocalDate;
import java.util.Objects;
import ru.nsu.masolygin.oopchecker.domain.Task;

/**
 * Делегат блока {@code task { ... }}. Синтаксис: {@code id '2_1_1'}, {@code maxPoints 10},
 * {@code path 'Task_2_1_1'}. Поле {@code labPath} необязательно — по умолчанию используется
 * {@code id}.
 */
public class TaskDelegate {

    private String id;
    private String name;
    private int maxPoints;
    private LocalDate softDeadline;
    private LocalDate hardDeadline;
    private String labPath;

    /**
     * Устанавливает идентификатор задачи.
     *
     * @param value идентификатор
     */
    public void id(String value) {
        this.id = value;
    }

    /**
     * Устанавливает название задачи.
     *
     * @param value название
     */
    public void name(String value) {
        this.name = value;
    }

    /**
     * Устанавливает максимальное количество баллов.
     *
     * @param value баллы
     */
    public void maxPoints(int value) {
        this.maxPoints = value;
    }

    /**
     * Устанавливает путь к каталогу задачи в репозитории.
     *
     * @param value путь относительно корня репозитория
     */
    public void labPath(String value) {
        this.labPath = value;
    }

    /**
     * Устанавливает мягкий дедлайн задачи.
     *
     * @param isoDate дата в формате ISO-8601 (yyyy-MM-dd)
     */
    public void softDeadline(String isoDate) {
        this.softDeadline = LocalDate.parse(isoDate);
    }

    /**
     * Устанавливает жёсткий дедлайн задачи.
     *
     * @param isoDate дата в формате ISO-8601 (yyyy-MM-dd)
     */
    public void hardDeadline(String isoDate) {
        this.hardDeadline = LocalDate.parse(isoDate);
    }

    Task build() {
        Objects.requireNonNull(id, "task.id необходим");
        Objects.requireNonNull(name, "task.name необходим");
        return new Task(id, name, maxPoints, softDeadline, hardDeadline, labPath);
    }
}
