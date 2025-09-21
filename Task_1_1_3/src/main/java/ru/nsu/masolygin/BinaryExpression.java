package ru.nsu.masolygin;

/**
 * Abstract class for binary operations that have left and right operands.
 */
public abstract class BinaryExpression extends Expression {
    /** The left operand of the binary expression. */
    protected final Expression left;
    /** The right operand of the binary expression. */
    protected final Expression right;

    /**
     * Creates a new binary expression.
     *
     * @param left the left operand
     * @param right the right operand
     */
    public BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Gets the left operand.
     *
     * @return the left expression
     */
    public Expression getLeft() {
        return left;
    }

    /**
     * Gets the right operand.
     *
     * @return the right expression
     */
    public Expression getRight() {
        return right;
    }
}
