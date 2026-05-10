package ru.nsu.masolygin.oopchecker.dsl;

import groovy.lang.Closure;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfigBuilder;


/**
 * Делегат блока {@code course { ... }}, маршрутизирующий вложенные блоки DSL.
 */
public class CourseDelegate {

    private final CourseConfigBuilder builder;


    public CourseDelegate(CourseConfigBuilder builder) {
        this.builder = builder;
    }

    /**
     * Обрабатывает блок {@code tasks { ... }}.
     *
     * @param body тело блока
     */
    public void tasks(Closure<?> body) {
        ClosureBinder.bindAndCall(body, new TasksDelegate(builder));
    }

    /**
     * Обрабатывает блок {@code groups { ... }}.
     *
     * @param body тело блока
     */
    public void groups(Closure<?> body) {
        ClosureBinder.bindAndCall(body, new GroupsDelegate(builder));
    }

    /**
     * Обрабатывает блок {@code assignments { ... }}.
     *
     * @param body тело блока
     */
    public void assignments(Closure<?> body) {
        ClosureBinder.bindAndCall(body, new AssignmentsDelegate(builder));
    }

    /**
     * Обрабатывает блок {@code checkpoints { ... }}.
     *
     * @param body тело блока
     */
    public void checkpoints(Closure<?> body) {
        ClosureBinder.bindAndCall(body, new CheckpointsDelegate(builder));
    }

    /**
     * Обрабатывает блок {@code settings { ... }}.
     *
     * @param body тело блока
     */
    public void settings(Closure<?> body) {
        ClosureBinder.bindAndCall(body, new SettingsDelegate(
            builder.getSettingsBuilder()));
    }
}
