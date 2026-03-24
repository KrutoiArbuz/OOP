package ru.nsu.masolygin.exception;

/**
 * Исключение при загрузке конфигурации.
 */
public class ConfigLoadException extends PizzeriaException {

    /**
     * Конструктор.
     *
     * @param message сообщение об ошибке
     * @param cause   причина исключения
     */
    public ConfigLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
