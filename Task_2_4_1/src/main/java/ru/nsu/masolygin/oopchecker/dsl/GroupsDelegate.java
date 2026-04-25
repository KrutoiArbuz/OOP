package ru.nsu.masolygin.oopchecker.dsl;

import groovy.lang.Closure;
import ru.nsu.masolygin.oopchecker.domain.CourseConfig;

/**
 * Делегат блока {@code groups { ... }}. Разрешает вызовы {@code group('12345') { ... }} — имя
 * группы передаётся первым позиционным аргументом, тело — замыканием.
 */
public class GroupsDelegate {

    private final CourseConfig config;

    /**
     * Создаёт делегат, привязанный к конфигурации курса.
     *
     * @param config конфигурация курса
     */
    public GroupsDelegate(CourseConfig config) {
        this.config = config;
    }

    /**
     * Создаёт группу с именем {@code name} и добавляет её в конфигурацию.
     *
     * @param name имя группы
     * @param body тело блока с описанием студентов
     */
    public void group(String name, Closure<?> body) {
        GroupDelegate delegate = new GroupDelegate(name);
        DslSupport.runClosure(body, delegate);
        config.addGroup(delegate.build());
    }
}
