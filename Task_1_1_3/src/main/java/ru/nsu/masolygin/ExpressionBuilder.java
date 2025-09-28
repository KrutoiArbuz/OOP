package ru.nsu.masolygin;

import ru.nsu.masolygin.Expressions.Expression;
import ru.nsu.masolygin.Expressions.Variable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Строит выражения из токенов.
 */
public class ExpressionBuilder {

    /**
     * Создает новый строитель выражений.
     */
    public ExpressionBuilder() {
    }

    /**
     * Строит выражение из постфиксной записи.
     *
     * @param postfix токены в постфиксной записи
     * @return объект выражения
     */
    public Expression buildExpressionFromPostfix(List<Token> postfix) {
        if (postfix.isEmpty()) {
            throw new ParseException("Cannot build expression from empty postfix");
        }

        Stack<Expression> stack = new Stack<>();
        for (Token token : postfix) {
            switch (token.getType()) {
                case NUMBER:
                    try {
                        stack.push(new ru.nsu.masolygin.Expressions.Number(Integer.parseInt(token.getValue())));
                    } catch (NumberFormatException e) {
                        throw new ParseException("Invalid number format: " + token.getValue());
                    }
                    break;
                case VARIABLE:
                    if (token.getValue().trim().isEmpty()) {
                        throw new ParseException("Variable name cannot be empty");
                    }
                    stack.push(new Variable(token.getValue()));
                    break;
                case OPERATOR:
                    if (stack.size() < 2) {
                        throw new ParseException("Not enough operands for operator: " + token.getValue());
                    }
                    if (token.getOperation() != null) {
                        Expression right = stack.pop();
                        Expression left = stack.pop();
                        stack.push(token.getOperation().apply(left, right));
                    } else {
                        throw new IllegalArgumentException("Unsupported operator: " + token.getValue());
                    }
                    break;
            }
        }

        if (stack.size() != 1) {
            throw new ParseException("Invalid expression: expected single result, got " + stack.size() + " results");
        }

        return stack.pop();
    }
}
