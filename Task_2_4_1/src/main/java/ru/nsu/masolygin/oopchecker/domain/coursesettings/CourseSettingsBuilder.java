package ru.nsu.masolygin.oopchecker.domain.coursesettings;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import ru.nsu.masolygin.oopchecker.domain.SemesterInfo;

/**
 * Билдер CourseSettings.
 */
public class CourseSettingsBuilder {

    private final Map<String, Map<String, Integer>> extraPoints = new HashMap<>();
    private final TreeMap<Integer, String> gradeScale = new TreeMap<>();
    private final Map<Integer, SemesterInfo> semesters = new HashMap<>();
    private double latePenalty = 0.0;
    private long testTimeoutSeconds = 120;
    private long buildTimeoutSeconds = 300;
    private double activityWeight = 0.0;
    private double docsPenalty = 0.2;
    private double stylePenalty = 0.2;

    /**
     * Устанавливает коэффициент штрафа за просрочку.
     *
     * @param latePenalty коэффициент (0..1)
     */
    public void setLatePenalty(double latePenalty) {
        this.latePenalty = requireValidPercentage(latePenalty, "Штраф за просрочку");
    }

    /**
     * Устанавливает таймаут одного теста в секундах.
     *
     * @param testTimeoutSeconds таймаут в секундах
     */
    public void setTestTimeoutSeconds(long testTimeoutSeconds) {
        if (testTimeoutSeconds <= 0) {
            throw new IllegalArgumentException(
                "Таймаут должен быть > 0, передано: " + testTimeoutSeconds);
        }
        this.testTimeoutSeconds = testTimeoutSeconds;
    }

    /**
     * Устанавливает таймаут сборки (компиляция, документация, стиль) в секундах.
     *
     * @param buildTimeoutSeconds таймаут в секундах
     */
    public void setBuildTimeoutSeconds(long buildTimeoutSeconds) {
        if (buildTimeoutSeconds <= 0) {
            throw new IllegalArgumentException(
                "Таймаут сборки должен быть > 0, передано: " + buildTimeoutSeconds);
        }
        this.buildTimeoutSeconds = buildTimeoutSeconds;
    }

    /**
     * Устанавливает вес активности в итоговой оценке.
     *
     * @param activityWeight вес (0..1)
     */
    public void setActivityWeight(double activityWeight) {
        this.activityWeight = requireValidPercentage(activityWeight, "Вес активности");
    }

    /**
     * Устанавливает штраф за отсутствие документации.
     *
     * @param docsPenalty коэффициент (0..1)
     */
    public void setDocsPenalty(double docsPenalty) {
        this.docsPenalty = requireValidPercentage(docsPenalty, "Штраф за документацию");
    }

    /**
     * Устанавливает штраф за нарушение стиля кода.
     *
     * @param stylePenalty коэффициент (0..1)
     */
    public void setStylePenalty(double stylePenalty) {
        this.stylePenalty = requireValidPercentage(stylePenalty, "Штраф за стиль");
    }

    /**
     * Добавляет дополнительные баллы студенту за задачу.
     *
     * @param taskId        идентификатор задачи
     * @param studentGithub GitHub ник студента
     * @param points        дополнительные баллы
     */
    public void addExtraPoints(String taskId, String studentGithub, int points) {
        extraPoints.computeIfAbsent(taskId, k -> new HashMap<>())
            .merge(studentGithub, points, Integer::sum);
    }

    /**
     * Добавляет порог шкалы оценок.
     *
     * @param minPercent минимальный процент выполнения
     * @param grade      название оценки
     */
    public void addGradeThreshold(int minPercent, String grade) {
        gradeScale.put(minPercent, grade);
    }

    /**
     * Добавляет информацию о семестре.
     *
     * @param semesterId номер семестра
     * @param info       данные семестра
     */
    public void addSemester(int semesterId, SemesterInfo info) {
        Objects.requireNonNull(info, "info");
        semesters.put(semesterId, info);
    }

    /**
     * Проверяет, что значение находится в диапазоне [0, 1].
     *
     * @param value     проверяемое значение
     * @param fieldName имя поля для сообщения об ошибке
     * @return то же значение если проверка прошла
     */
    private double requireValidPercentage(double value, String fieldName) {
        if (value < 0.0 || value > 1.0) {
            throw new IllegalArgumentException(
                fieldName + " должен быть от 0 до 1, передано: " + value);
        }
        return value;
    }

    /**
     * Собирает неизменяемый CourseSettings.
     *
     * @return конфигурация настроек курса
     */
    public CourseSettings build() {
        return new CourseSettings(
            extraPoints, gradeScale, semesters,
            latePenalty, testTimeoutSeconds, buildTimeoutSeconds, activityWeight,
            docsPenalty, stylePenalty
        );
    }
}
