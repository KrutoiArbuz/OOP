package ru.nsu.masolygin.Expressions;

/**
 * Математическое выражение.
 */
public abstract class Expression {

    /**
     * Создает выражение.
     */
    protected Expression() {
    }

    /**
     * Печатает выражение.
     */
    public abstract void print();

    /**
     * Вычисляет производную.
     *
     * @param variable переменная
     * @return производная
     */
    public abstract Expression derivative(String variable);

    /**
     * Вычисляет значение.
     *
     * @param assignments значения переменных
     * @return результат
     */
    public abstract int eval(String assignments);

    /**
     * Упрощает выражение.
     *
     * @return упрощенное выражение
     */
    public abstract Expression simplify();
}
