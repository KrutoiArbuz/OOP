package ru.nsu.masolygin.oopchecker.dsl;

import groovy.lang.Closure;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfigBuilder;

/**
 * Делегат для блока конфигурации групп студентов.
 */
public class GroupsDelegate {

    private final CourseConfigBuilder builder;

    /**
     * Создает делегата с привязкой к билдеру конфигурации курса.
     *
     * @param builder билдер конфигурации
     */
    public GroupsDelegate(CourseConfigBuilder builder) {
        this.builder = builder;
    }

    /**
     * Создаёт группу с именем {@code name} и добавляет её в конфигурацию.
     *
     * @param name имя группы
     * @param body тело блока с описанием студентов
     */
    public void group(String name, Closure<?> body) {
        GroupDelegate delegate = new GroupDelegate(name);
        ClosureBinder.bindAndCall(body, delegate);
        builder.addGroup(delegate.build());
    }
}
