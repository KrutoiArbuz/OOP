package ru.nsu.masolygin;

import java.util.function.BiFunction;

import ru.nsu.masolygin.Expressions.Expression;

/**
 * Токен для парсинга выражений.
 */
public class Token {
    /**
     * Тип токена.
     */
    public enum Type {
        /** Число. */
        NUMBER,
        /** Переменная. */
        VARIABLE,
        /** Операция. */
        OPERATOR,
        /** Левая скобка. */
        LPAREN,
        /** Правая скобка. */
        RPAREN
    }

    private final Type type;
    private final String value;
    private final BiFunction<Expression, Expression, Expression> operation;

    /**
     * Создает токен.
     *
     * @param type тип токена
     * @param value значение токена
     */
    public Token(Type type, String value) {
        this(type, value, null);
    }

    /**
     * Создает токен с операцией.
     *
     * @param type тип токена
     * @param value значение токена
     * @param operation операция
     */
    public Token(Type type, String value,
                 BiFunction<Expression, Expression, Expression> operation) {
        this.type = type;
        this.value = value;
        this.operation = operation;
    }

    /**
     * Возвращает тип токена.
     *
     * @return тип токена
     */
    public Type getType() {
        return type;
    }

    /**
     * Возвращает значение токена.
     *
     * @return значение токена
     */
    public String getValue() {
        return value;
    }

    /**
     * Возвращает операцию.
     *
     * @return операция
     */
    public BiFunction<Expression, Expression, Expression> getOperation() {
        return operation;
    }
}
