package ru.nsu.masolygin.oopchecker.grader;

/**
 * Активность студента за семестр: количество учебных недель с коммитами.
 *
 * @param studentGithub GitHub ник студента
 * @param activeWeeks   количество недель с коммитом
 * @param totalWeeks    общее количество учебных недель
 */
public record StudentActivity(String studentGithub, int activeWeeks, int totalWeeks) {

    /**
     * Возвращает долю активных недель.
     *
     * @return доля (0..1)
     */
    public double ratio() {
        return totalWeeks == 0 ? 0.0 : (double) activeWeeks / totalWeeks;
    }
}
