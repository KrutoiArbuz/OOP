package ru.nsu.masolygin;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Преобразует инфиксную запись в постфиксную.
 */
public class Postfixer {

    /**
     * Создает новый преобразователь.
     */
    public Postfixer() {
    }

    /**
     * Преобразует токены в постфиксную запись.
     *
     * @param tokens список токенов
     * @return список токенов в постфиксной записи
     */
    public List<Token> toPostfix(List<Token> tokens) {
        if (tokens.isEmpty()) {
            throw new ParseException("Empty expression");
        }

        List<Token> output = new ArrayList<>();
        Stack<Token> stack = new Stack<>();

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            switch (token.getType()) {
                case NUMBER:
                case VARIABLE:
                    output.add(token);
                    break;
                case OPERATOR:
                    if (i == 0 && !token.getValue().equals("-")) {
                        throw new ParseException("Expression cannot start with operator: " + token.getValue());
                    }
                    if (i == tokens.size() - 1) {
                        throw new ParseException("Expression cannot end with operator: " + token.getValue());
                    }

                    while (!stack.isEmpty() && stack.peek().getType() == Token.Type.OPERATOR &&
                            precedence(stack.peek().getValue()) >= precedence(token.getValue())) {
                        output.add(stack.pop());
                    }
                    stack.push(token);
                    break;
                case LPAREN:
                    stack.push(token);
                    break;
                case RPAREN:
                    boolean foundLeftParen = false;
                    while (!stack.isEmpty() && stack.peek().getType() != Token.Type.LPAREN) {
                        output.add(stack.pop());
                    }
                    if (!stack.isEmpty() && stack.peek().getType() == Token.Type.LPAREN) {
                        stack.pop();
                        foundLeftParen = true;
                    }
                    if (!foundLeftParen) {
                        throw new ParseException("Unmatched closing parenthesis");
                    }
                    break;
            }
        }

        while (!stack.isEmpty()) {
            Token token = stack.pop();
            if (token.getType() == Token.Type.LPAREN) {
                throw new ParseException("Unmatched opening parenthesis");
            }
            output.add(token);
        }

        if (output.isEmpty()) {
            throw new ParseException("Invalid expression");
        }

        return output;
    }

    /**
     * Возвращает приоритет операции.
     *
     * @param op операция
     * @return приоритет
     */
    private int precedence(String op) {
        switch (op) {
            case "+": case "-": return 1;
            case "*": case "/": return 2;
            case "^": return 3;
            default: return 0;
        }
    }
}
