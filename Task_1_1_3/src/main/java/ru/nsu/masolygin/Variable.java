package ru.nsu.masolygin;

/**
 * Represents a variable in a mathematical expression.
 */
public class Variable extends Expression {
    /** The name of this variable. */
    private final String name;

    /**
     * Creates a new variable with the given name.
     * @param name the variable name
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Gets the variable name.
     * @return the variable name
     */
    public String getName() {
        return name;
    }

    /**
     * Prints the variable name.
     */
    @Override
    public void print() {
        System.out.print(name);
    }

    /**
     * Computes the derivative of the variable.
     *
     * @param variable the variable to differentiate by
     * @return 1 if same variable, 0 otherwise
     */
    @Override
    public Expression derivative(String variable) {
        if (name.equals(variable)) {
            return new Number(1);
        } else {
            return new Number(0);
        }
    }

    /**
     * Evaluates the variable using assignments.
     *
     * @param assignments variable assignments string
     * @return the variable value
     */
    @Override
    public int eval(String assignments) {
        String[] pairs = assignments.split(";");
        for (String pair : pairs) {
            String[] keyValue = pair.trim().split("=");
            if (keyValue.length == 2) {
                String varName = keyValue[0].trim();
                if (varName.equals(name)) {
                    return Integer.parseInt(keyValue[1].trim());
                }
            }
        }
        throw new IllegalArgumentException("Variable " + name + " not found in assignments");
    }

    /**
     * Simplifies the variable (returns itself).
     *
     * @return this variable
     */
    @Override
    public Expression simplify() {
        return this;
    }
}
