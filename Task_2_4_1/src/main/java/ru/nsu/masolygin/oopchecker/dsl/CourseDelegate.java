package ru.nsu.masolygin.oopchecker.dsl;

import groovy.lang.Closure;
import ru.nsu.masolygin.oopchecker.domain.CourseConfig;

/**
 * Делегат блока {@code course { ... }}, маршрутизирующий вложенные блоки DSL.
 */
public class CourseDelegate {

    private final CourseConfig config;

    /**
     * Конструктор.
     *
     * @param config конфигурация курса, в которую пишутся все данные
     */
    public CourseDelegate(CourseConfig config) {
        this.config = config;
    }

    /**
     * Обрабатывает блок {@code tasks { ... }}.
     *
     * @param body тело блока
     */
    public void tasks(Closure<?> body) {
        DslSupport.runClosure(body, new TasksDelegate(config));
    }

    /**
     * Обрабатывает блок {@code groups { ... }}.
     *
     * @param body тело блока
     */
    public void groups(Closure<?> body) {
        DslSupport.runClosure(body, new GroupsDelegate(config));
    }

    /**
     * Обрабатывает блок {@code assignments { ... }}.
     *
     * @param body тело блока
     */
    public void assignments(Closure<?> body) {
        DslSupport.runClosure(body, new AssignmentsDelegate(config));
    }

    /**
     * Обрабатывает блок {@code checkpoints { ... }}.
     *
     * @param body тело блока
     */
    public void checkpoints(Closure<?> body) {
        DslSupport.runClosure(body, new CheckpointsDelegate(config));
    }

    /**
     * Обрабатывает блок {@code settings { ... }}.
     *
     * @param body тело блока
     */
    public void settings(Closure<?> body) {
        DslSupport.runClosure(body, new SettingsDelegate(config.settings()));
    }
}
