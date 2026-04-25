package ru.nsu.masolygin.oopchecker.dsl;

import groovy.lang.Closure;
import ru.nsu.masolygin.oopchecker.domain.CourseConfig;

/**
 * Делегат блока {@code tasks { ... }}, собирающий задачи через {@code task { ... }}.
 */
public class TasksDelegate {

    private final CourseConfig config;

    /**
     * Конструктор.
     *
     * @param config конфигурация курса, в которую добавляются задачи
     */
    public TasksDelegate(CourseConfig config) {
        this.config = config;
    }

    /**
     * Обрабатывает один блок {@code task { ... }} и добавляет задачу в конфигурацию.
     */
    public void task(Closure<?> body) {
        TaskDelegate delegate = new TaskDelegate();
        DslSupport.runClosure(body, delegate);
        config.addTask(delegate.build());
    }
}
