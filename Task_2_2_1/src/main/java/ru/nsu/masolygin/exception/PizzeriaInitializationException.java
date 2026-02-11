package ru.nsu.masolygin.exception;

/**
 * Исключение при инициализации пиццерии.
 */
public class PizzeriaInitializationException extends PizzeriaException {

    /**
     * Конструктор.
     *
     * @param message сообщение об ошибке
     */
    public PizzeriaInitializationException(String message) {
        super(message);
    }

    /**
     * Конструктор с причиной.
     *
     * @param message сообщение об ошибке
     * @param cause   причина исключения
     */
    public PizzeriaInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
