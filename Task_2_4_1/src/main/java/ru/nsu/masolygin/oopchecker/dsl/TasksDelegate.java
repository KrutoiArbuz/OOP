package ru.nsu.masolygin.oopchecker.dsl;

import groovy.lang.Closure;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfigBuilder;

/**
 * Делегат блока {@code tasks { ... }}, собирающий задачи через {@code task { ... }}.
 */
public class TasksDelegate {

    private final CourseConfigBuilder builder;


    public TasksDelegate(CourseConfigBuilder builder) {
        this.builder = builder;
    }

    /**
     * Обрабатывает один блок {@code task { ... }} и добавляет задачу в конфигурацию.
     *
     * @param body тело блока с описанием задачи
     */
    public void task(Closure<?> body) {
        TaskDelegate delegate = new TaskDelegate();
        ClosureBinder.bindAndCall(body, delegate);
        builder.addTask(delegate.build());
    }
}
