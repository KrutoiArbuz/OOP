package ru.nsu.masolygin.oopchecker.vcs;

/**
 * Исключение, возникающее при ошибке выполнения git-команды.
 */
public class GitException extends RuntimeException {

    /**
     * Создаёт исключение с сообщением об ошибке.
     *
     * @param message описание ошибки
     */
    public GitException(String message) {
        super(message);
    }

    /**
     * Создаёт исключение с сообщением об ошибке и причиной.
     *
     * @param message описание ошибки
     * @param cause   исходное исключение
     */
    public GitException(String message, Throwable cause) {
        super(message, cause);
    }
}
