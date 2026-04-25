package ru.nsu.masolygin.oopchecker.dsl;

import java.time.LocalDate;
import java.util.Map;
import ru.nsu.masolygin.oopchecker.domain.CourseSettings;

/**
 * Делегат блока {@code settings { ... }}. Каждый параметр — отдельный DSL-метод, чтобы не
 * придумывать свой parse-DSL для значений.
 *
 * <pre>
 *     settings {
 *         latePenalty 0.5
 *         testTimeoutSeconds 60
 *         activityWeight 0.1
 *         gradeThreshold min: 85, grade: 'отлично'
 *         extraPoints task: '2_1_1', student: 'ivanov', points: 2
 *     }
 * </pre>
 */
public class SettingsDelegate {

    private final CourseSettings settings;

    /**
     * Создаёт делегат настроек.
     *
     * @param settings настройки курса для изменения
     */
    public SettingsDelegate(CourseSettings settings) {
        this.settings = settings;
    }

    /**
     * Устанавливает штраф за опоздание (множитель, например 0.5).
     *
     * @param value штраф
     */
    public void latePenalty(double value) {
        settings.setLatePenalty(value);
    }

    /**
     * Устанавливает таймаут выполнения тестов в секундах.
     *
     * @param value таймаут в секундах
     */
    public void testTimeoutSeconds(long value) {
        settings.setTestTimeoutSeconds(value);
    }

    /**
     * Устанавливает вес активности в итоговой оценке (0.0–1.0).
     *
     * @param value вес активности
     */
    public void activityWeight(double value) {
        settings.setActivityWeight(value);
    }

    /**
     * Устанавливает дату начала семестра.
     *
     * @param isoDate дата в формате ISO-8601 (yyyy-MM-dd)
     */
    public void semesterStart(String isoDate) {
        settings.setSemesterStart(LocalDate.parse(isoDate));
    }

    /**
     * Устанавливает количество недель в семестре.
     *
     * @param value количество недель
     */
    public void semesterWeeks(int value) {
        settings.setSemesterWeeks(value);
    }

    /**
     * Добавляет пороговое значение для выставления оценки.
     *
     * @param args именованные аргументы: min (процент), grade (строка оценки)
     */
    public void gradeThreshold(Map<String, Object> args) {
        int min = ((Number) args.get("min")).intValue();
        String grade = (String) args.get("grade");
        settings.addGradeThreshold(min, grade);
    }

    /**
     * Добавляет дополнительные баллы студенту за задание.
     *
     * @param args именованные аргументы: task, student, points
     */
    public void extraPoints(Map<String, Object> args) {
        int points = ((Number) args.get("points")).intValue();
        settings.addExtraPoints(
            (String) args.get("task"),
            (String) args.get("student"),
            points
        );
    }
}
