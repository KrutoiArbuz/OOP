package ru.nsu.masolygin.exception;

/**
 * Базовое исключение пиццерии.
 */
public abstract class PizzeriaException extends Exception {

    /**
     * Конструктор.
     *
     * @param message сообщение об ошибке
     */
    public PizzeriaException(String message) {
        super(message);
    }

    /**
     * Конструктор с причиной.
     *
     * @param message сообщение об ошибке
     * @param cause   причина исключения
     */
    public PizzeriaException(String message, Throwable cause) {
        super(message, cause);
    }
}
