package ru.nsu.masolygin.oopchecker.domain.courseconfig;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.Checkpoint;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.domain.coursesettings.CourseSettingsBuilder;

/**
 * Билдер CourseConfig, заполняемый DSL-делегатами.
 */
public class CourseConfigBuilder {

    private final List<Task> tasks = new ArrayList<>();
    private final Map<String, Group> groupsByName = new LinkedHashMap<>();
    private final List<Assignment> assignments = new ArrayList<>();
    private final List<Checkpoint> checkpoints = new ArrayList<>();
    private final CourseSettingsBuilder settingsBuilder = new CourseSettingsBuilder();

    /**
     * Добавляет задачу в конфигурацию.
     *
     * @param task задача
     */
    public void addTask(Task task) {
        Objects.requireNonNull(task, "task");
        tasks.add(task);
    }

    /**
     * Добавляет группу в конфигурацию.
     *
     * @param group группа
     */
    public void addGroup(Group group) {
        Objects.requireNonNull(group, "group");
        groupsByName.put(group.name(), group);
    }

    /**
     * Группа по имени.
     *
     * @param name имя группы
     * @return группа или пусто если не найдена
     */
    public Optional<Group> findGroup(String name) {
        return Optional.ofNullable(groupsByName.get(name));
    }

    /**
     * Добавляет задание в конфигурацию.
     *
     * @param assignment задание
     */
    public void addAssignment(Assignment assignment) {
        Objects.requireNonNull(assignment, "assignment");
        assignments.add(assignment);
    }

    /**
     * Добавляет контрольную точку в конфигурацию.
     *
     * @param checkpoint контрольная точка
     */
    public void addCheckpoint(Checkpoint checkpoint) {
        Objects.requireNonNull(checkpoint, "checkpoint");
        checkpoints.add(checkpoint);
    }

    /**
     * Возвращает билдер настроек курса.
     *
     * @return билдер настроек
     */
    public CourseSettingsBuilder getSettingsBuilder() {
        return settingsBuilder;
    }

    /**
     * Собирает неизменяемый CourseConfig.
     *
     * @return конфигурация курса
     */
    public CourseConfig build() {
        return new CourseConfig(
            tasks, List.copyOf(groupsByName.values()), assignments, checkpoints,
            settingsBuilder.build()
        );
    }
}
