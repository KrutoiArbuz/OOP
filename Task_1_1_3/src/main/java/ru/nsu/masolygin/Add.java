package ru.nsu.masolygin;

/**
 * Represents addition of two expressions.
 */
public class Add extends BinaryExpression {

    /**
     * Creates a new addition expression.
     * @param left the left operand
     * @param right the right operand
     */
    public Add(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Prints the addition in format (left+right).
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
     * Computes the derivative of addition.
     * @param variable the variable to differentiate by
     * @return derivative of left + derivative of right
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(getLeft().derivative(variable), getRight().derivative(variable));
    }

    /**
     * Evaluates the addition.
     * @param assignments variable assignments
     * @return sum of left and right values
     */
    @Override
    public int eval(String assignments) {
        return getLeft().eval(assignments) + getRight().eval(assignments);
    }

    /**
     * Simplifies the addition expression.
     * @return simplified expression
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
