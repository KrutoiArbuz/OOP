package ru.nsu.masolygin;

import java.util.ArrayList;
import java.util.List;

import ru.nsu.masolygin.Expressions.Add;
import ru.nsu.masolygin.Expressions.Div;
import ru.nsu.masolygin.Expressions.Mul;
import ru.nsu.masolygin.Expressions.Sub;

/**
 * Разбивает строку на токены.
 */
public class StringTokenizer {
    private final List<Token> tokens = new ArrayList<>();

    /**
     * Создает токенизатор и разбивает строку.
     *
     * @param input входная строка
     */
    public StringTokenizer(String input) {
        tokenize(input);
    }

    /**
     * Разбивает строку на токены.
     *
     * @param input входная строка
     */
    private void tokenize(String input) {
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }
            if (Character.isDigit(c) || (c == '-' && i + 1 < input.length()
                    && Character.isDigit(input.charAt(i + 1)))) {
                int start = i;
                if (c == '-') {
                    i++;
                }
                while (i < input.length() && Character.isDigit(input.charAt(i))) {
                    i++;
                }
                tokens.add(new Token(Token.Type.NUMBER, input.substring(start, i)));
                continue;
            }
            if (Character.isLetter(c)) {
                int start = i;
                while (i < input.length() && Character.isLetter(input.charAt(i))) {
                    i++;
                }
                tokens.add(new Token(Token.Type.VARIABLE, input.substring(start, i)));
                continue;
            }
            switch (c) {
                case '+':
                    tokens.add(new Token(Token.Type.OPERATOR, "+", Add::new));
                    break;
                case '-':
                    tokens.add(new Token(Token.Type.OPERATOR, "-", Sub::new));
                    break;
                case '*':
                    tokens.add(new Token(Token.Type.OPERATOR, "*", Mul::new));
                    break;
                case '/':
                    tokens.add(new Token(Token.Type.OPERATOR, "/", Div::new));
                    break;
                case '^':
                    tokens.add(new Token(Token.Type.OPERATOR, "^"));
                    break;
                case '(':
                    tokens.add(new Token(Token.Type.LPAREN, "("));
                    break;
                case ')':
                    tokens.add(new Token(Token.Type.RPAREN, ")"));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown character: " + c);
            }
            i++;
        }
    }

    /**
     * Возвращает список токенов.
     *
     * @return список токенов
     */
    public List<Token> getTokens() {
        return tokens;
    }
}
