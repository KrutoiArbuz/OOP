package ru.nsu.masolygin.oopchecker.dsl;

import groovy.lang.Closure;
import java.util.Map;
import ru.nsu.masolygin.oopchecker.domain.coursesettings.CourseSettingsBuilder;

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

    private final CourseSettingsBuilder builder;


    public SettingsDelegate(CourseSettingsBuilder builder) {
        this.builder = builder;
    }

    /**
     * Устанавливает штраф за опоздание (множитель, например 0.5).
     *
     * @param value штраф
     */
    public void latePenalty(double value) {
        builder.setLatePenalty(value);
    }

    /**
     * Устанавливает таймаут выполнения тестов в секундах.
     *
     * @param value таймаут в секундах
     */
    public void testTimeoutSeconds(long value) {
        builder.setTestTimeoutSeconds(value);
    }

    /**
     * Устанавливает таймаут сборки (компиляция, документация, стиль) в секундах.
     *
     * @param value таймаут в секундах
     */
    public void buildTimeoutSeconds(long value) {
        builder.setBuildTimeoutSeconds(value);
    }

    /**
     * Устанавливает вес активности в итоговой оценке (0.0–1.0).
     *
     * @param value вес активности
     */
    public void activityWeight(double value) {
        builder.setActivityWeight(value);
    }


    /**
     * Устанавливает штраф за отсутствие документации (0.0–1.0).
     *
     * @param value коэффициент штрафа
     */
    public void docsPenalty(double value) {
        builder.setDocsPenalty(value);
    }

    /**
     * Устанавливает штраф за нарушение стиля кода (0.0–1.0).
     *
     * @param value коэффициент штрафа
     */
    public void stylePenalty(double value) {
        builder.setStylePenalty(value);
    }

    public void semester(int id, Closure<?> body) {
        SemesterDelegate delegate = new SemesterDelegate();
        ClosureBinder.bindAndCall(body, delegate);
        builder.addSemester(id, delegate.build());
    }


    /**
     * Добавляет пороговое значение для выставления оценки.
     *
     * @param args именованные аргументы: min (процент), grade (строка оценки)
     */
    public void gradeThreshold(Map<String, Object> args) {
        int min = ((Number) args.get("min")).intValue();
        String grade = (String) args.get("grade");
        builder.addGradeThreshold(min, grade);
    }

    /**
     * Добавляет дополнительные баллы студенту за задание.
     *
     * @param args именованные аргументы: task, student, points
     */
    public void extraPoints(Map<String, Object> args) {
        int points = ((Number) args.get("points")).intValue();
        builder.addExtraPoints(
            (String) args.get("task"),
            (String) args.get("student"),
            points
        );
    }
}
