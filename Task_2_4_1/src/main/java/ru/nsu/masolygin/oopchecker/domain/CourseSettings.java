package ru.nsu.masolygin.oopchecker.domain;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Настройки курса, заполняемые DSL пошагово.
 */
public class CourseSettings {

    private final Map<String, Map<String, Integer>> extraPoints = new HashMap<>();
    private final TreeMap<Integer, String> gradeScale = new TreeMap<>();
    private double latePenalty = 0.0;
    private long testTimeoutSeconds = 120;
    private double activityWeight = 0.0;
    private LocalDate semesterStart;
    private int semesterWeeks = 16;

    /**
     * Возвращает долю баллов, снимаемых при сдаче после мягкого дедлайна.
     *
     * @return коэффициент штрафа (0..1)
     */
    public double getLatePenalty() {
        return latePenalty;
    }

    /**
     * Устанавливает коэффициент штрафа за просрочку.
     *
     * @param latePenalty коэффициент (0..1)
     */
    public void setLatePenalty(double latePenalty) {
        this.latePenalty = latePenalty;
    }

    /**
     * Возвращает таймаут одного теста в секундах.
     *
     * @return таймаут в секундах
     */
    public long getTestTimeoutSeconds() {
        return testTimeoutSeconds;
    }

    /**
     * Устанавливает таймаут одного теста в секундах.
     *
     * @param testTimeoutSeconds таймаут в секундах
     */
    public void setTestTimeoutSeconds(long testTimeoutSeconds) {
        this.testTimeoutSeconds = testTimeoutSeconds;
    }

    /**
     * Возвращает вес активности в итоговой оценке.
     *
     * @return вес активности (0..1)
     */
    public double getActivityWeight() {
        return activityWeight;
    }

    /**
     * Устанавливает вес активности в итоговой оценке.
     *
     * @param activityWeight вес (0..1)
     */
    public void setActivityWeight(double activityWeight) {
        this.activityWeight = activityWeight;
    }

    /**
     * Возвращает дату начала семестра.
     *
     * @return дата начала семестра
     */
    public LocalDate getSemesterStart() {
        return semesterStart;
    }

    /**
     * Устанавливает дату начала семестра.
     *
     * @param semesterStart дата начала семестра
     */
    public void setSemesterStart(LocalDate semesterStart) {
        this.semesterStart = semesterStart;
    }

    /**
     * Возвращает длину семестра в неделях.
     *
     * @return количество недель
     */
    public int getSemesterWeeks() {
        return semesterWeeks;
    }

    /**
     * Устанавливает длину семестра в неделях.
     *
     * @param semesterWeeks количество недель
     */
    public void setSemesterWeeks(int semesterWeeks) {
        this.semesterWeeks = semesterWeeks;
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
     * Добавляет порог шкалы оценок.
     *
     * @param minPercent минимальный процент выполнения
     * @param grade      название оценки
     */
    public void addGradeThreshold(int minPercent, String grade) {
        gradeScale.put(minPercent, grade);
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
