package ru.nsu.masolygin.oopchecker.dsl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Student;

/**
 * Делегат блока {@code group('name') { ... }}. Принимает студентов через именованные параметры:
 * {@code student(github: 'ivanov', name: '...', repo: '...')}.
 */
public class GroupDelegate {

    private final String name;
    private final List<Student> students = new ArrayList<>();

    /**
     * Создаёт делегат группы с заданным именем.
     *
     * @param name имя группы
     */
    public GroupDelegate(String name) {
        this.name = name;
    }

    /**
     * Вызывается из Groovy как {@code student(github: 'x', name: 'y', repo: 'z')}. В Groovy
     * именованные аргументы склеиваются в {@link Map}, который Groovy передаёт первым параметром.
     *
     * @param args именованные аргументы: github, name, repo
     */
    public void student(Map<String, Object> args) {
        students.add(new Student(
            (String) args.get("github"),
            (String) args.get("name"),
            (String) args.get("repo")
        ));
    }

    Group build() {
        return new Group(name, List.copyOf(students));
    }
}
