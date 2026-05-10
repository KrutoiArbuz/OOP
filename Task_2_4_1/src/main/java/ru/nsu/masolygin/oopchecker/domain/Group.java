package ru.nsu.masolygin.oopchecker.domain;

import java.util.List;
import java.util.Objects;

/**
 * Учебная группа со списком студентов.
 *
 * @param name     название группы
 * @param students неизменяемый список студентов в группе
 */
public record Group(String name, List<Student> students) {

    /**
     * Канонический конструктор: делает список студентов неизменяемым.
     */
    public Group {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(students, "students");
        students = List.copyOf(students);
    }
}
