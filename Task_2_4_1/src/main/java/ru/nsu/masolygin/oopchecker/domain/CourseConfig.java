package ru.nsu.masolygin.oopchecker.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Агрегат всей конфигурации курса, заполняемый DSL-парсером.
 */
public class CourseConfig {

    private final List<Task> tasks = new ArrayList<>();
    private final List<Group> groups = new ArrayList<>();
    private final List<Assignment> assignments = new ArrayList<>();
    private final List<Checkpoint> checkpoints = new ArrayList<>();
    private final CourseSettings settings = new CourseSettings();
    /**
     * Создаёт пустую конфигурацию курса.
     */
    public CourseConfig() {
    }

    /**
     * Добавляет задачу в конфигурацию.
     *
     * @param task задача
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Добавляет группу в конфигурацию.
     *
     * @param group группа
     */
    public void addGroup(Group group) {
        groups.add(group);
    }

    /**
     * Добавляет задание в конфигурацию.
     *
     * @param assignment задание
     */
    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    /**
     * Добавляет контрольную точку в конфигурацию.
     *
     * @param checkpoint контрольная точка
     */
    public void addCheckpoint(Checkpoint checkpoint) {
        checkpoints.add(checkpoint);
    }

    /**
     * Возвращает неизменяемый список задач.
     *
     * @return список задач
     */
    public List<Task> tasks() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Возвращает неизменяемый список групп.
     *
     * @return список групп
     */
    public List<Group> groups() {
        return Collections.unmodifiableList(groups);
    }

    /**
     * Возвращает неизменяемый список заданий.
     *
     * @return список заданий
     */
    public List<Assignment> assignments() {
        return Collections.unmodifiableList(assignments);
    }

    /**
     * Возвращает неизменяемый список контрольных точек.
     *
     * @return список контрольных точек
     */
    public List<Checkpoint> checkpoints() {
        return Collections.unmodifiableList(checkpoints);
    }

    /**
     * Возвращает настройки курса.
     *
     * @return настройки
     */
    public CourseSettings settings() {
        return settings;
    }

    /**
     * Возвращает задачу по идентификатору.
     *
     * @param id идентификатор
     * @return задача или пусто
     */
    public Optional<Task> findTask(String id) {
        return tasks.stream().filter(t -> t.id().equals(id)).findFirst();
    }

    /**
     * Возвращает студента по github-нику.
     *
     * @param github GitHub ник
     * @return студент или пусто
     */
    public Optional<Student> findStudent(String github) {
        return groups.stream()
            .flatMap(g -> g.students().stream())
            .filter(s -> s.github().equals(github))
            .findFirst();
    }

    /**
     * Возвращает группу, в которой состоит студент.
     *
     * @param github GitHub ник студента
     * @return группа или пусто
     */
    public Optional<Group> findGroupOf(String github) {
        return groups.stream()
            .filter(g -> g.students().stream().anyMatch(s -> s.github().equals(github)))
            .findFirst();
    }
}
