package ru.nsu.masolygin.worker;


/**
 * Результат обработки блока чисел.
 */
public enum ProcessResult {
    /**
     * Найдено непростое число.
     */
    COMPOSITE_FOUND,
    /**
     * Все числа простые.
     */
    ALL_PRIME,
    /**
     * Обработка была отменена.
     */
    CANCELLED
}
