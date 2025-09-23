package ru.nsu.masolygin;

/**
 * Represents subtraction of two expressions.
 */
public class Sub extends BinaryExpression {

    /**
     * Creates a new subtraction expression.
     *
     * @param left the left operand
     * @param right the right operand
     */
    public Sub(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Prints the subtraction in format (left-right).
     */
    @Override
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print("-");
        right.print();
        System.out.print(")");
    }

    /**
     * Computes the derivative of subtraction.
     *
     * @param variable the variable to differentiate by
     * @return derivative of left - derivative of right
     */
    @Override
    public Expression derivative(String variable) {
        return new Sub(left.derivative(variable), right.derivative(variable));
    }

    /**
     * Evaluates the subtraction.
     *
     * @param assignments variable assignments
     * @return difference of left and right values
     */
    @Override
    public int eval(String assignments) {
        return left.eval(assignments) - right.eval(assignments);
    }

    /**
     * Simplifies the subtraction expression.
     *
     * @return simplified expression
     */
    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            int result = ((Number) simplifiedLeft).getValue()
                - ((Number) simplifiedRight).getValue();
            return new Number(result);
        }

        if (areExpressionsEqual(simplifiedLeft, simplifiedRight)) {
            return new Number(0);
        }

        if (simplifiedRight instanceof Number && ((Number) simplifiedRight).getValue() == 0) {
            return simplifiedLeft;
        }

        return new Sub(simplifiedLeft, simplifiedRight);
    }

    /**
     * Checks if two expressions are equal.
     *
     * @param expr1 first expression
     * @param expr2 second expression
     * @return true if expressions are equal
     */
    private boolean areExpressionsEqual(Expression expr1, Expression expr2) {
        if (expr1.getClass() != expr2.getClass()) {
            return false;
        }

        if (expr1 instanceof Number) {
            return ((Number) expr1).getValue() == ((Number) expr2).getValue();
        }

        if (expr1 instanceof Variable) {
            return ((Variable) expr1).getName().equals(((Variable) expr2).getName());
        }

        if (expr1 instanceof Add) {
            Add add1 = (Add) expr1;
            Add add2 = (Add) expr2;
            return areExpressionsEqual(add1.getLeft(), add2.getLeft())
                && areExpressionsEqual(add1.getRight(), add2.getRight());
        }

        if (expr1 instanceof Sub) {
            Sub sub1 = (Sub) expr1;
            Sub sub2 = (Sub) expr2;
            return areExpressionsEqual(sub1.getLeft(), sub2.getLeft())
                && areExpressionsEqual(sub1.getRight(), sub2.getRight());
        }

        if (expr1 instanceof Mul) {
            Mul mul1 = (Mul) expr1;
            Mul mul2 = (Mul) expr2;
            return areExpressionsEqual(mul1.getLeft(), mul2.getLeft())
                && areExpressionsEqual(mul1.getRight(), mul2.getRight());
        }

        if (expr1 instanceof Div) {
            Div div1 = (Div) expr1;
            Div div2 = (Div) expr2;
            return areExpressionsEqual(div1.getLeft(), div2.getLeft())
                && areExpressionsEqual(div1.getRight(), div2.getRight());
        }

        return false;
    }
}
