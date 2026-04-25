package ru.nsu.masolygin.oopchecker.dsl;

import java.util.Map;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.CourseConfig;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Student;

/**
 * Делегат блока {@code assignments { ... }}. Синтаксис:
 * {@code check task: '2_1_1', student: 'ivanov'} или {@code check task: '2_1_1', group: '24216'}.
 */
public class AssignmentsDelegate {

    private final CourseConfig config;

    /**
     * Создаёт делегат, привязанный к конфигурации курса.
     *
     * @param config конфигурация курса
     */
    public AssignmentsDelegate(CourseConfig config) {
        this.config = config;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * Добавляет задание(я) по студенту или группе.
     *
     * @param args именованные аргументы: task (обязательно), student или group
     */
    public void check(Map<String, Object> args) {
        String taskId = (String) args.get("task");
        String studentGithub = (String) args.get("student");
        String groupName = (String) args.get("group");

        if (!hasText(taskId)) {
            throw new IllegalArgumentException("check requires non-empty 'task'");
        }

        boolean hasStudent = hasText(studentGithub);
        boolean hasGroup = hasText(groupName);
        if (hasStudent == hasGroup) {
            throw new IllegalArgumentException(
                "check requires exactly one of 'student' or 'group'");
        }

        if (hasStudent) {
            addAssignment(taskId, studentGithub);
            return;
        }

        Group group = config.groups().stream()
            .filter(g -> g.name().equals(groupName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "group '" + groupName + "' not found"));

        for (Student student : group.students()) {
            addAssignment(taskId, student.github());
        }
    }

    private void addAssignment(String taskId, String studentGithub) {
        config.addAssignment(new Assignment(taskId, studentGithub));
    }
}
