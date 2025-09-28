package ru.nsu.masolygin.Expressions;

/**
 * Бинарное выражение с двумя операндами.
 */
public abstract class BinaryExpression extends Expression {
    /** Левый операнд. */
    protected final Expression left;
    /** Правый операнд. */
    protected final Expression right;

    /**
     * Создает бинарное выражение.
     *
     * @param left левый операнд
     * @param right правый операнд
     */
    public BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Возвращает левый операнд.
     *
     * @return левое выражение
     */
    public Expression getLeft() {
        return left;
    }

    /**
     * Возвращает правый операнд.
     *
     * @return правое выражение
     */
    public Expression getRight() {
        return right;
    }
}
