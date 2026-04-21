package ru.nsu.masolygin.config;

/**
 * Исключение загрузки конфигурации.
 */
public class ConfigLoadException extends RuntimeException {

    /**
     * Конструктор.
     *
     * @param message сообщение об ошибке
     * @param cause   исходная причина
     */
    public ConfigLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
