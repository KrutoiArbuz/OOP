package ru.nsu.masolygin.oopchecker.domain.coursesettings;

import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import ru.nsu.masolygin.oopchecker.domain.SemesterInfo;

/**
 * Настройки курса, заполняемые DSL пошагово.
 *
 * @param extraPoints         дополнительные баллы по студентам и задачам
 * @param gradeScale          шкала оценок в зависимости от процента выполнения
 * @param semesters           информация о семестрах
 * @param latePenalty         штраф за сдачу после дедлайна
 * @param testTimeoutSeconds  таймаут выполнения тестов в секундах
 * @param buildTimeoutSeconds таймаут сборки в секундах
 * @param activityWeight      вес активности в итоговой оценке
 * @param docsPenalty         штраф за отсутствие документации
 * @param stylePenalty        штраф за нарушение стиля кода
 */
public record CourseSettings(Map<String, Map<String, Integer>> extraPoints,
                             NavigableMap<Integer, String> gradeScale,
                             Map<Integer, SemesterInfo> semesters,
                             double latePenalty,
                             long testTimeoutSeconds,
                             long buildTimeoutSeconds,
                             double activityWeight,
                             double docsPenalty,
                             double stylePenalty) {

    /**
     * Создает настройки курса, обеспечивая неизменяемость коллекций.
     */
    public CourseSettings {
        extraPoints = extraPoints.entrySet().stream()
            .collect(Collectors.toUnmodifiableMap(
                Map.Entry::getKey,
                e -> Map.copyOf(e.getValue())
            ));
        gradeScale = Collections.unmodifiableNavigableMap(new TreeMap<>(gradeScale));
        semesters = Map.copyOf(semesters);
    }


    /**
     * Возвращает дополнительные баллы студента за задачу.
     *
     * @param taskId        идентификатор задачи
     * @param studentGithub GitHub ник студента
     * @return дополнительные баллы (0 если не установлены)
     */
    public int getExtraPoints(String taskId, String studentGithub) {
        return extraPoints.getOrDefault(taskId, Map.of()).getOrDefault(studentGithub, 0);
    }


    /**
     * Возвращает оценку по проценту выполнения.
     *
     * @param percent процент выполнения
     * @return оценка (ближайший порог снизу вверх)
     */
    public String gradeFor(int percent) {
        Map.Entry<Integer, String> e = gradeScale.floorEntry(percent);
        return e == null ? "неудовлетворительно" : e.getValue();
    }


}
