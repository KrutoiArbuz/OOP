package ru.nsu.masolygin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.nsu.masolygin.Expressions.Add;
import ru.nsu.masolygin.Expressions.Div;
import ru.nsu.masolygin.Expressions.Expression;
import ru.nsu.masolygin.Expressions.Mul;
import ru.nsu.masolygin.Expressions.Sub;
import ru.nsu.masolygin.Expressions.Variable;

/**
 * Калькулятор математических выражений.
 */
public class Calculator {
    private final Console console;
    private final Map<String, Integer> variables = new HashMap<>();

    /**
     * Создает новый калькулятор.
     */
    public Calculator() {
        this.console = new Console();
    }

    /**
     * Запускает калькулятор.
     */
    public void run() {
        console.printWelcome();

        while (true) {
            try {
                String input = console.readLine();

                if (console.isExitCommand(input)) {
                    break;
                }

                if (console.hasErrors(input)) {
                    console.printError("Invalid characters in expression.");
                    continue;
                }

                Expression result = processExpression(input);

                console.printExpression("Parsed expression");
                result.print();
                console.printNewLine();

                requestVariableValues(result);
                int value = evaluateExpression(result);
                console.printInfo("Result: " + value);

                offerDerivativeCalculation(result);

            } catch (ParseException e) {
                console.printError("Parse error: " + e.getMessage());
            } catch (Exception e) {
                console.printError("Error: " + e.getMessage());
            }
        }

        console.printGoodbye();
    }

    /**
     * Обрабатывает строку выражения.
     *
     * @param input строка выражения
     * @return объект выражения
     */
    public Expression processExpression(String input) {
        StringTokenizer tokenizer = new StringTokenizer(input);
        List<Token> tokens = tokenizer.getTokens();

        Postfixer postfixer = new Postfixer();
        List<Token> postfix = postfixer.toPostfix(tokens);

        ExpressionBuilder builder = new ExpressionBuilder();
        return builder.buildExpressionFromPostfix(postfix);
    }

    /**
     * Запрашивает значения переменных.
     *
     * @param expr выражение
     */
    private void requestVariableValues(Expression expr) {
        collectAndRequestVariables(expr);
    }

    /**
     * Собирает и запрашивает переменные.
     *
     * @param expr выражение
     */
    private void collectAndRequestVariables(Expression expr) {
        if (expr instanceof Variable) {
            String name = ((Variable) expr).getName();
            if (!variables.containsKey(name)) {
                variables.put(name, console.requestVariableValue(name));
            }
        } else if (expr instanceof Add) {
            collectAndRequestVariables(((Add) expr).getLeft());
            collectAndRequestVariables(((Add) expr).getRight());
        } else if (expr instanceof Sub) {
            collectAndRequestVariables(((Sub) expr).getLeft());
            collectAndRequestVariables(((Sub) expr).getRight());
        } else if (expr instanceof Mul) {
            collectAndRequestVariables(((Mul) expr).getLeft());
            collectAndRequestVariables(((Mul) expr).getRight());
        } else if (expr instanceof Div) {
            collectAndRequestVariables(((Div) expr).getLeft());
            collectAndRequestVariables(((Div) expr).getRight());
        }
    }

    /**
     * Вычисляет выражение.
     *
     * @param expr выражение
     * @return результат
     */
    private int evaluateExpression(Expression expr) {
        String assignments = buildAssignmentsString();
        return expr.eval(assignments);
    }

    /**
     * Строит строку присваиваний.
     *
     * @return строка присваиваний
     */
    private String buildAssignmentsString() {
        if (variables.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Integer> entry : variables.entrySet()) {
            if (!first) {
                sb.append("; ");
            }
            sb.append(entry.getKey()).append(" = ").append(entry.getValue());
            first = false;
        }
        return sb.toString();
    }

    /**
     * Предлагает вычислить производную.
     *
     * @param expr выражение
     */
    private void offerDerivativeCalculation(Expression expr) {
        String variable = console.requestDerivativeVariable();

        if (!variable.isEmpty()) {
            try {
                Expression derivative = expr.derivative(variable);
                console.printExpression("Derivative by '" + variable + "'");
                derivative.print();
                console.printNewLine();

                if (console.confirmDerivativeEvaluation()) {
                    int derivativeValue = derivative.eval(buildAssignmentsString());
                    console.printInfo("Derivative value: " + derivativeValue);
                }
            } catch (Exception e) {
                console.printError("Error calculating derivative: " + e.getMessage());
            }
        }
    }

    /**
     * Очищает переменные.
     */
    public void clearVariables() {
        variables.clear();
    }

    /**
     * Главный метод.
     *
     * @param args аргументы
     */
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.run();
    }
}
