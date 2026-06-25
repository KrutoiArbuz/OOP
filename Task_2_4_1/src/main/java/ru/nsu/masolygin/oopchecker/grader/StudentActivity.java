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
     * Создаёт запись для студента без активности.
     *
     * @param github GitHub ник студента
     * @return активность с нулевыми счётчиками
     */
    public static StudentActivity absent(String github) {
        return new StudentActivity(github, 0, 0);
    }

    /**
     * Возвращает долю активных недель.
     *
     * @return доля (0..1)
     */
    public double ratio() {
        return totalWeeks == 0 ? 0.0 : (double) activeWeeks / totalWeeks;
    }
}
