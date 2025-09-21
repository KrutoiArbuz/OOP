package ru.nsu.masolygin;

/**
 * Represents multiplication of two expressions.
 */
public class Mul extends BinaryExpression {

    /**
     * Creates a new multiplication expression.
     *
     * @param left the left operand
     * @param right the right operand
     */
    public Mul(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Prints the multiplication in format (left*right).
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
     * Computes the derivative of multiplication using product rule.
     *
     * @param variable the variable to differentiate by
     * @return (left' * right) + (left * right')
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(new Mul(left.derivative(variable), right),
            new Mul(left, right.derivative(variable)));
    }

    /**
     * Evaluates the multiplication.
     *
     * @param assignments variable assignments
     * @return product of left and right values
     */
    @Override
    public int eval(String assignments) {
        return left.eval(assignments) * right.eval(assignments);
    }

    /**
     * Simplifies the multiplication expression.
     *
     * @return simplified expression
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
