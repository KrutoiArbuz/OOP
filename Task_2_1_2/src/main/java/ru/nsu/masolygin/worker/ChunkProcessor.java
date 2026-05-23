package ru.nsu.masolygin.worker;

import ru.nsu.masolygin.solutions.PrimeChecker;

/**
 * Обрабатывает блок чисел на предмет наличия непростых чисел.
 */
public class ChunkProcessor {

    private final int[] chunk;
    private volatile boolean cancelled = false;

    /**
     * Создает обработчик блока.
     *
     * @param chunk массив чисел для проверки
     */
    public ChunkProcessor(int[] chunk) {
        this.chunk = chunk;
    }

    /**
     * Выполняет проверку блока чисел.
     *
     * @return результат проверки
     */
    public ProcessResult run() {
        for (int n : chunk) {
            if (cancelled) {
                return ProcessResult.CANCELLED;
            }
            if (!PrimeChecker.isPrime(n)) {
                return ProcessResult.COMPOSITE_FOUND;
            }
        }
        return ProcessResult.ALL_PRIME;
    }

    /**
     * Отменяет выполнение проверки.
     */
    public void cancel() {
        cancelled = true;
    }
}
