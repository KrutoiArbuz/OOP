package ru.nsu.masolygin;

/**
 * Ошибка парсинга выражений.
 */
public class ParseException extends RuntimeException {
    /**
     * Создает исключение с сообщением.
     *
     * @param message сообщение об ошибке
     */
    public ParseException(String message) {
        super(message);
    }

    /**
     * Создает исключение с сообщением и причиной.
     *
     * @param message сообщение об ошибке
     * @param cause причина ошибки
     */
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
