package ru.nsu.masolygin.oopchecker.cli;

import java.util.Arrays;
import java.util.Optional;

/**
 * Флаги командной строки приложения oop-checker.
 *
 * <p>Флаги двух видов:
 * <ul>
 *   <li>булевые — присутствие означает true: {@link #TEXT}</li>
 *   <li>значения — передаются через {@code =}: {@link #DIR}, {@link #STUDENT}</li>
 * </ul>
 */
public enum Flag {

    DIR("--dir="),
    CONFIG("--config="),
    STUDENT("--student="),
    OUT("--out="),
    TEXT("--text");

    private final String token;

    Flag(String token) {
        this.token = token;
    }

    /**
     * Проверяет наличие флага среди аргументов.
     *
     * @param args аргументы командной строки
     * @return true если флаг присутствует
     */
    public boolean presentIn(String[] args) {
        return Arrays.stream(args).anyMatch(a -> a.equals(token) || a.startsWith(token));
    }

    /**
     * Извлекает значение флага вида {@code --key=value}.
     *
     * @param args аргументы командной строки
     * @return значение или пусто, если флаг не передан или значение пустое
     */
    public Optional<String> valueOf(String[] args) {
        return Arrays.stream(args)
            .filter(a -> a.startsWith(token) && a.length() > token.length())
            .map(a -> a.substring(token.length()))
            .filter(v -> !v.isBlank())
            .findFirst();
    }
}
