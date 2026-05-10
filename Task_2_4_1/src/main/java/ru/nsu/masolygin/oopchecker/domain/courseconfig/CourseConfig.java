package ru.nsu.masolygin.oopchecker.domain.courseconfig;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.Checkpoint;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Student;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.domain.coursesettings.CourseSettings;

/**
 * Агрегат всей конфигурации курса, заполняемый DSL-парсером.
 */
public record CourseConfig(List<Task> tasks,
                           List<Group> groups,
                           List<Assignment> assignments,
                           List<Checkpoint> checkpoints,
                           CourseSettings settings) {

    public CourseConfig {
        tasks = List.copyOf(tasks);
        groups = List.copyOf(groups);
        assignments = List.copyOf(assignments);
        checkpoints = List.copyOf(checkpoints);
    }

    /**
     * Возвращает задачу по идентификатору.
     *
     * @param id идентификатор
     * @return задача или пусто
     */
    public Optional<Task> findTask(String id) {
        return tasks.stream().filter(t -> t.id().equals(id)).findFirst();
    }

    /**
     * Возвращает студента по github-нику.
     *
     * @param github GitHub ник
     * @return студент или пусто
     */
    public Optional<Student> findStudent(String github) {
        return groups.stream()
            .flatMap(g -> g.students().stream())
            .filter(s -> s.github().equals(github))
            .findFirst();
    }

    /**
     * Возвращает список задач, назначенных студентам группы.
     *
     * @param group группа студентов
     * @return уникальные задачи из заданий группы
     */
    public List<Task> tasksFor(Group group) {
        Objects.requireNonNull(group, "group must not be null");
        return assignments.stream()
            .filter(a -> group.students().stream()
                .anyMatch(s -> s.github().equals(a.studentGithub())))
            .flatMap(a -> findTask(a.taskId()).stream())
            .distinct()
            .toList();
    }
}
