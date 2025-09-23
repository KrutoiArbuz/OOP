package ru.nsu.masolygin;

/**
 * Represents a number in a mathematical expression.
 */
public class Number extends Expression {
    /** The constant integer value stored in this expression. */
    private final int value;

    /**
     * Creates a new number with the given value.
     *
     * @param value the number value
     */
    public Number(int value) {
        this.value = value;
    }

    /**
     * Gets the number value.
     *
     * @return the number value
     */
    public int getValue() {
        return value;
    }

    /**
     * Prints the number value.
     */
    @Override
    public void print() {
        System.out.print(value);
    }

    /**
     * Returns the derivative of a constant (always 0).
     *
     * @param variable the variable to differentiate by
     * @return zero
     */
    @Override
    public Expression derivative(String variable) {
        return new Number(0);
    }

    /**
     * Evaluates the number (returns its value).
     *
     * @param assignments not used for constants
     * @return the number value
     */
    @Override
    public int eval(String assignments) {
        return value;
    }

    /**
     * Simplifies the number (returns itself).
     *
     * @return this number
     */
    @Override
    public Expression simplify() {
        return this;
    }
}
