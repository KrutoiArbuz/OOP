package ru.nsu.masolygin;

import java.util.function.BiFunction;
import ru.nsu.masolygin.Expressions.Add;
import ru.nsu.masolygin.Expressions.Div;
import ru.nsu.masolygin.Expressions.Expression;
import ru.nsu.masolygin.Expressions.Mul;
import ru.nsu.masolygin.Expressions.Sub;

/**
 * Токен выражения.
 */
public class Token {
    /**
     * Тип токена.
     */
    public enum Type {
        /**
         * Число.
         */
        NUMBER,
        /**
         * Переменная.
         */
        VARIABLE,
        /**
         * Оператор.
         */
        OPERATOR,
        /**
         * Левая скобка.
         */
        LPAREN,
        /**
         * Правая скобка.
         */
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
        this.type = type;
        this.value = value;
        this.operation = null;
    }

    /**
     * Создает токен с операцией.
     *
     * @param type тип токена
     * @param value значение токена
     * @param operation функция операции
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
     * Возвращает операцию токена.
     *
     * @return операция токена
     */
    public BiFunction<Expression, Expression, Expression> getOperation() {
        return operation;
    }
}
