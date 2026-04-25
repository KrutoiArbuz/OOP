package ru.nsu.masolygin.oopchecker.runner;

/**
 * Сводка по результатам JUnit тестов для отчёта.
 *
 * @param passed  количество успешных тестов
 * @param failed  количество провалившихся тестов
 * @param skipped количество пропущенных тестов
 */
public record TestReport(int passed, int failed, int skipped) {

    /**
     * Пустой отчёт без результатов тестов.
     */
    public static final TestReport EMPTY = new TestReport(0, 0, 0);

    /**
     * Возвращает общее количество тестов.
     *
     * @return passed + failed + skipped
     */
    public int total() {
        return passed + failed + skipped;
    }

    /**
     * Возвращает долю успешных тестов из выполненных.
     *
     * @return доля успешных (0.0 если тестов не было)
     */
    public double passRatio() {
        int executed = passed + failed;
        return executed == 0 ? 0.0 : (double) passed / executed;
    }
}
