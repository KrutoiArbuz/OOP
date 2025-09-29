package ru.nsu.masolygin.Expressions;

/**
 * Деление двух выражений.
 */
public class Div extends BinaryExpression {

    /**
     * Создает деление.
     *
     * @param left левое выражение
     * @param right правое выражение
     */
    public Div(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Печатает деление.
     */
    @Override
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print("/");
        right.print();
        System.out.print(")");
    }

    /**
     * Вычисляет производную деления.
     *
     * @param variable переменная
     * @return производная
     */
    @Override
    public Expression derivative(String variable) {
        return new Div(
            new Sub(
                new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable))
            ),
            new Mul(right, right)
        ).simplify();
    }

    /**
     * Вычисляет значение деления.
     *
     * @param assignments значения переменных
     * @return результат
     */
    @Override
    public int eval(String assignments) {
        int rightValue = right.eval(assignments);
        if (rightValue == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return left.eval(assignments) / rightValue;
    }

    /**
     * Упрощает деление.
     *
     * @return упрощенное выражение
     */
    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            int rightValue = ((Number) simplifiedRight).getValue();
            if (rightValue == 0) {
                throw new ArithmeticException("Division by zero");
            }
            int result = ((Number) simplifiedLeft).getValue() / rightValue;
            return new Number(result);
        }

        if (simplifiedRight instanceof Number
            && ((Number) simplifiedRight).getValue() == 1) {
            return simplifiedLeft;
        }

        if (simplifiedLeft instanceof Number && ((Number) simplifiedLeft).getValue() == 0) {
            if (!(simplifiedRight instanceof Number)
                || ((Number) simplifiedRight).getValue() != 0) {
                return new Number(0);
            }
        }

        return new Div(simplifiedLeft, simplifiedRight);
    }
}
