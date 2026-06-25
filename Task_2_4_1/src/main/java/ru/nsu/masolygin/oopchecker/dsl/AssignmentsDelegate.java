package ru.nsu.masolygin.oopchecker.dsl;

import java.util.Map;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Student;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfigBuilder;

/**
 * Делегат блока {@code assignments { ... }}. Синтаксис:
 * {@code check task: '2_1_1', student: 'ivanov'} или {@code check task: '2_1_1', group: '24216'}.
 */
public class AssignmentsDelegate {

    private final CourseConfigBuilder builder;

    public AssignmentsDelegate(CourseConfigBuilder builder) {
        this.builder = builder;
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

        if (taskId == null || taskId.isBlank()) {
            throw new IllegalArgumentException("check requires non-empty 'task'");
        }

        boolean hasStudent = studentGithub != null && !studentGithub.isBlank();
        boolean hasGroup = groupName != null && !groupName.isBlank();
        if (hasStudent == hasGroup) {
            throw new IllegalArgumentException(
                "check requires exactly one of 'student' or 'group'");
        }

        if (hasStudent) {
            addAssignment(taskId, studentGithub);
            return;
        }

        Group group = builder.findGroup(groupName)
            .orElseThrow(() -> new IllegalArgumentException(
                "group '" + groupName + "' not found"));

        group.students().stream()
            .map(Student::github)
            .forEach(github -> addAssignment(taskId, github));
    }

    private void addAssignment(String taskId, String studentGithub) {
        builder.addAssignment(new Assignment(taskId, studentGithub));
    }
}
