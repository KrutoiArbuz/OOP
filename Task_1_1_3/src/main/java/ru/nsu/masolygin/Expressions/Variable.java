package ru.nsu.masolygin.Expressions;

/**
 * Переменная в выражении.
 */
public class Variable extends Expression {
    private final String name;

    /**
     * Создает переменную.
     *
     * @param name имя переменной
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Возвращает имя переменной.
     *
     * @return имя переменной
     */
    public String getName() {
        return name;
    }

    /**
     * Печатает переменную.
     */
    @Override
    public void print() {
        System.out.print(name);
    }

    /**
     * Вычисляет производную переменной.
     *
     * @param variable переменная
     * @return производная
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
     * Вычисляет значение переменной.
     *
     * @param assignments значения переменных
     * @return значение
     */
    @Override
    public int eval(String assignments) {
        if (assignments == null) {
            throw new IllegalArgumentException("Assignments cannot be null");
        }

        if (assignments.trim().isEmpty()) {
            throw new IllegalArgumentException("Variable " + name + " not found in assignments");
        }

        String[] pairs = assignments.split(";");
        for (String pair : pairs) {
            pair = pair.trim();
            if (pair.isEmpty()) {
                continue;
            }

            if (!pair.contains("=")) {
                throw new IllegalArgumentException("Invalid assignment format: " + pair);
            }

            String[] keyValue = pair.split("=", 2);
            if (keyValue.length != 2) {
                throw new IllegalArgumentException("Invalid assignment format: " + pair);
            }

            String varName = keyValue[0].trim();
            String valueStr = keyValue[1].trim();

            if (varName.isEmpty()) {
                throw new IllegalArgumentException("Variable name cannot be empty in: " + pair);
            }

            if (valueStr.isEmpty()) {
                throw new IllegalArgumentException("Variable value cannot be empty in: " + pair);
            }

            if (varName.equals(name)) {
                try {
                    return Integer.parseInt(valueStr);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number format for variable " + name + ": " + valueStr);
                }
            }
        }
        throw new IllegalArgumentException("Variable " + name + " not found in assignments");
    }

    /**
     * Упрощает переменную.
     *
     * @return переменная
     */
    @Override
    public Expression simplify() {
        return this;
    }
}
