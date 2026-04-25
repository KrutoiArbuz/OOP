package ru.nsu.masolygin.oopchecker.runner;

import java.time.Duration;

/**
 * Результат запуска внешнего процесса с кодом возврата, потоками вывода и флагом таймаута.
 *
 * @param exitCode код возврата процесса
 * @param stdout   захваченный стандартный поток вывода
 * @param stderr   захваченный стандартный поток ошибок
 * @param timedOut true если выполнение было прервано по таймауту
 * @param duration фактическое время выполнения
 */
public record ProcessResult(
    int exitCode,
    String stdout,
    String stderr,
    boolean timedOut,
    Duration duration
) {

    /**
     * Проверяет успешное выполнение процесса.
     *
     * @return true если нет таймаута и код возврата == 0
     */
    public boolean success() {
        return !timedOut && exitCode == 0;
    }
}
