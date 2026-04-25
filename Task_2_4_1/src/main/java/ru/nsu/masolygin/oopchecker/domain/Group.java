package ru.nsu.masolygin.oopchecker.domain;

import java.util.List;

/**
 * Учебная группа со списком студентов.
 *
 * @param name     название группы
 * @param students неизменяемый список студентов в группе
 */
public record Group(String name, List<Student> students) {

    public Group {
        students = List.copyOf(students);
    }
}
