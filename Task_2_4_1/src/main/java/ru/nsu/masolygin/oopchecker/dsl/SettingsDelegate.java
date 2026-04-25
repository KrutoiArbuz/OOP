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

    public SettingsDelegate(CourseSettings settings) {
        this.settings = settings;
    }

    public void latePenalty(double value) {
        settings.setLatePenalty(value);
    }

    public void testTimeoutSeconds(long value) {
        settings.setTestTimeoutSeconds(value);
    }

    public void activityWeight(double value) {
        settings.setActivityWeight(value);
    }

    public void semesterStart(String isoDate) {
        settings.setSemesterStart(LocalDate.parse(isoDate));
    }

    public void semesterWeeks(int value) {
        settings.setSemesterWeeks(value);
    }

    public void gradeThreshold(Map<String, Object> args) {
        int min = ((Number) args.get("min")).intValue();
        String grade = (String) args.get("grade");
        settings.addGradeThreshold(min, grade);
    }

    public void extraPoints(Map<String, Object> args) {
        int points = ((Number) args.get("points")).intValue();
        settings.addExtraPoints(
            (String) args.get("task"),
            (String) args.get("student"),
            points
        );
    }
}
