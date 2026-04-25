package ru.nsu.masolygin.oopchecker.dsl;

import groovy.lang.Closure;
import ru.nsu.masolygin.oopchecker.domain.CourseConfig;

/**
 * Делегат блока {@code groups { ... }}. Разрешает вызовы {@code group('12345') { ... }} — имя
 * группы передаётся первым позиционным аргументом, тело — замыканием.
 */
public class GroupsDelegate {

    private final CourseConfig config;

    public GroupsDelegate(CourseConfig config) {
        this.config = config;
    }

    public void group(String name, Closure<?> body) {
        GroupDelegate delegate = new GroupDelegate(name);
        DslSupport.runClosure(body, delegate);
        config.addGroup(delegate.build());
    }
}
