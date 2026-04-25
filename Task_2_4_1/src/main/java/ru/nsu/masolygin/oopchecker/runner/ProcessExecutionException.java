package ru.nsu.masolygin.oopchecker.runner;

/**
 * Исключение, возникающее при ошибке запуска или выполнения внешнего процесса.
 */
public class ProcessExecutionException extends RuntimeException {

    /**
     * Создаёт исключение с сообщением об ошибке.
     *
     * @param message описание ошибки
     */
    public ProcessExecutionException(String message) {
        super(message);
    }

    /**
     * Создаёт исключение с сообщением об ошибке и причиной.
     *
     * @param message описание ошибки
     * @param cause   исходное исключение
     */
    public ProcessExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
