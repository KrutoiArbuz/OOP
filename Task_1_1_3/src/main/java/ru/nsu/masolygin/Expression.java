package ru.nsu.masolygin;

/**
 * Abstract base class for mathematical expressions.
 */
public abstract class Expression {

    /**
     * Creates a new expression.
     */
    protected Expression() {
        // Default constructor for subclasses
    }

    /**
     * Prints the expression to console.
     */
    public abstract void print();

    /**
     * Computes the derivative with respect to the given variable.
     * @param variable the variable to differentiate by
     * @return the derivative expression
     */
    public abstract Expression derivative(String variable);

    /**
     * Evaluates the expression with given variable assignments.
     * @param assignments variable assignments in format "x = 5; y = 10"
     * @return the result value
     */
    public abstract int eval(String assignments);

    /**
     * Simplifies the expression using algebraic rules.
     * @return the simplified expression
     */
    public abstract Expression simplify();
}
