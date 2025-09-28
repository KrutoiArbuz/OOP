package ru.nsu.masolygin.Expressions;

/**
 * Умножение двух выражений.
 */
public class Mul extends BinaryExpression {

    /**
     * Создает умножение.
     *
     * @param left левое выражение
     * @param right правое выражение
     */
    public Mul(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Печатает умножение.
     */
    @Override
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print("*");
        right.print();
        System.out.print(")");
    }

    /**
     * Вычисляет производную умножения.
     *
     * @param variable переменная
     * @return производная
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(new Mul(left.derivative(variable), right),
            new Mul(left, right.derivative(variable))).simplify();
    }

    /**
     * Вычисляет значение умножения.
     *
     * @param assignments значения переменных
     * @return результат
     */
    @Override
    public int eval(String assignments) {
        return left.eval(assignments) * right.eval(assignments);
    }

    /**
     * Упрощает умножение.
     *
     * @return упрощенное выражение
     */
    @Override
    public Expression simplify() {
        Expression leftSimplified = left.simplify();
        Expression rightSimplified = right.simplify();

        if (leftSimplified instanceof Number && rightSimplified instanceof Number) {
            int result = ((Number) leftSimplified).getValue()
                * ((Number) rightSimplified).getValue();
            return new Number(result);
        }

        if ((leftSimplified instanceof Number && ((Number) leftSimplified).getValue() == 0)
            || (rightSimplified instanceof Number && ((Number) rightSimplified).getValue() == 0)) {
            return new Number(0);
        }

        if (leftSimplified instanceof Number && ((Number) leftSimplified).getValue() == 1) {
            return rightSimplified;
        }
        if (rightSimplified instanceof Number && ((Number) rightSimplified).getValue() == 1) {
            return leftSimplified;
        }

        return new Mul(leftSimplified, rightSimplified);
    }
}
