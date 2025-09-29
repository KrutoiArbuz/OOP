package ru.nsu.masolygin.Expressions;

/**
 * Сложение двух выражений.
 */
public class Add extends BinaryExpression {

    /**
     * Создает сложение.
     *
     * @param left левое выражение
     * @param right правое выражение
     */
    public Add(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Печатает сложение.
     */
    @Override
    public void print() {
        System.out.print("(");
        getLeft().print();
        System.out.print("+");
        getRight().print();
        System.out.print(")");
    }

    /**
     * Вычисляет производную сложения.
     *
     * @param variable переменная
     * @return производная
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(getLeft().derivative(variable), getRight().derivative(variable)).simplify();
    }

    /**
     * Вычисляет значение сложения.
     *
     * @param assignments значения переменных
     * @return результат
     */
    @Override
    public int eval(String assignments) {
        return getLeft().eval(assignments) + getRight().eval(assignments);
    }

    /**
     * Упрощает сложение.
     *
     * @return упрощенное выражение
     */
    @Override
    public Expression simplify() {
        Expression simplifiedLeft = getLeft().simplify();
        Expression simplifiedRight = getRight().simplify();

        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            int sum = ((Number) simplifiedLeft).getValue() + ((Number) simplifiedRight).getValue();
            return new Number(sum);
        }

        if (simplifiedLeft instanceof Number && ((Number) simplifiedLeft).getValue() == 0) {
            return simplifiedRight;
        }
        if (simplifiedRight instanceof Number && ((Number) simplifiedRight).getValue() == 0) {
            return simplifiedLeft;
        }

        return new Add(simplifiedLeft, simplifiedRight);
    }


}
