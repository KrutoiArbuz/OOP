package ru.nsu.masolygin.Expressions;

/**
 * Число в выражении.
 */
public class Number extends Expression {
    private final int value;

    /**
     * Создает число.
     *
     * @param value значение числа
     */
    public Number(int value) {
        this.value = value;
    }

    /**
     * Возвращает значение числа.
     *
     * @return значение числа
     */
    public int getValue() {
        return value;
    }

    /**
     * Печатает число.
     */
    @Override
    public void print() {
        System.out.print(value);
    }

    /**
     * Вычисляет производную числа.
     *
     * @param variable переменная
     * @return ноль
     */
    @Override
    public Expression derivative(String variable) {
        return new Number(0);
    }

    /**
     * Вычисляет значение числа.
     *
     * @param assignments не используется
     * @return значение числа
     */
    @Override
    public int eval(String assignments) {
        return value;
    }

    /**
     * Упрощает число.
     *
     * @return число
     */
    @Override
    public Expression simplify() {
        return this;
    }
}
