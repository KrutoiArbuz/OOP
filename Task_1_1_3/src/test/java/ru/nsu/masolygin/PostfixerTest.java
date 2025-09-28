package ru.nsu.masolygin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.nsu.masolygin.Expressions.Add;
import ru.nsu.masolygin.Expressions.Div;
import ru.nsu.masolygin.Expressions.Mul;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostfixerTest {
    private Postfixer postfixer;

    @BeforeEach
    void setUp() {
        postfixer = new Postfixer();
    }

    @Test
    void testSimpleVariable() {
        List<Token> input = Collections.singletonList(
            new Token(Token.Type.VARIABLE, "x")
        );
        List<Token> result = postfixer.toPostfix(input);

        assertEquals(1, result.size());
        assertEquals(Token.Type.VARIABLE, result.get(0).getType());
        assertEquals("x", result.get(0).getValue());
    }

    @Test
    void testSimpleAddition() {
        List<Token> input = Arrays.asList(
            new Token(Token.Type.VARIABLE, "x"),
            new Token(Token.Type.OPERATOR, "+", Add::new),
            new Token(Token.Type.VARIABLE, "y")
        );
        List<Token> result = postfixer.toPostfix(input);

        assertEquals(3, result.size());
        assertEquals("x", result.get(0).getValue());
        assertEquals("y", result.get(1).getValue());
        assertEquals("+", result.get(2).getValue());
    }

    @Test
    void testOperatorPrecedence() {
        List<Token> input = Arrays.asList(
            new Token(Token.Type.VARIABLE, "a"),
            new Token(Token.Type.OPERATOR, "+", Add::new),
            new Token(Token.Type.VARIABLE, "b"),
            new Token(Token.Type.OPERATOR, "*", Mul::new),
            new Token(Token.Type.VARIABLE, "c")
        );
        List<Token> result = postfixer.toPostfix(input);

        assertEquals(5, result.size());
        assertEquals("a", result.get(0).getValue());
        assertEquals("b", result.get(1).getValue());
        assertEquals("c", result.get(2).getValue());
        assertEquals("*", result.get(3).getValue());
        assertEquals("+", result.get(4).getValue());
    }

    @Test
    void testParentheses() {
        List<Token> input = Arrays.asList(
            new Token(Token.Type.LPAREN, "("),
            new Token(Token.Type.VARIABLE, "a"),
            new Token(Token.Type.OPERATOR, "+", Add::new),
            new Token(Token.Type.VARIABLE, "b"),
            new Token(Token.Type.RPAREN, ")"),
            new Token(Token.Type.OPERATOR, "*", Mul::new),
            new Token(Token.Type.VARIABLE, "c")
        );
        List<Token> result = postfixer.toPostfix(input);

        assertEquals(5, result.size());
        assertEquals("a", result.get(0).getValue());
        assertEquals("b", result.get(1).getValue());
        assertEquals("+", result.get(2).getValue());
        assertEquals("c", result.get(3).getValue());
        assertEquals("*", result.get(4).getValue());
    }

    @Test
    void testEmptyInputThrowsException() {
        assertThrows(ParseException.class,
            () -> postfixer.toPostfix(Collections.emptyList()));
    }

    @Test
    void testExpressionStartingWithOperatorThrowsException() {
        List<Token> input = Arrays.asList(
            new Token(Token.Type.OPERATOR, "+", Add::new),
            new Token(Token.Type.VARIABLE, "x")
        );
        assertThrows(ParseException.class,
            () -> postfixer.toPostfix(input));
    }

    @Test
    void testExpressionEndingWithOperatorThrowsException() {
        List<Token> input = Arrays.asList(
            new Token(Token.Type.VARIABLE, "x"),
            new Token(Token.Type.OPERATOR, "+", Add::new)
        );
        assertThrows(ParseException.class,
            () -> postfixer.toPostfix(input));
    }

    @Test
    void testUnmatchedClosingParenthesisThrowsException() {
        List<Token> input = Arrays.asList(
            new Token(Token.Type.VARIABLE, "x"),
            new Token(Token.Type.OPERATOR, "+", Add::new),
            new Token(Token.Type.VARIABLE, "y"),
            new Token(Token.Type.RPAREN, ")")
        );
        assertThrows(ParseException.class,
            () -> postfixer.toPostfix(input));
    }

    @Test
    void testUnmatchedOpeningParenthesisThrowsException() {
        List<Token> input = Arrays.asList(
            new Token(Token.Type.LPAREN, "("),
            new Token(Token.Type.VARIABLE, "x"),
            new Token(Token.Type.OPERATOR, "+", Add::new),
            new Token(Token.Type.VARIABLE, "y")
        );
        assertThrows(ParseException.class,
            () -> postfixer.toPostfix(input));
    }

    @Test
    void testComplexExpression() {
        List<Token> input = Arrays.asList(
            new Token(Token.Type.VARIABLE, "a"),
            new Token(Token.Type.OPERATOR, "*", Mul::new),
            new Token(Token.Type.VARIABLE, "b"),
            new Token(Token.Type.OPERATOR, "+", Add::new),
            new Token(Token.Type.VARIABLE, "c"),
            new Token(Token.Type.OPERATOR, "/", Div::new),
            new Token(Token.Type.VARIABLE, "d")
        );
        List<Token> result = postfixer.toPostfix(input);

        assertEquals(7, result.size());
        assertEquals("a", result.get(0).getValue());
        assertEquals("b", result.get(1).getValue());
        assertEquals("*", result.get(2).getValue());
        assertEquals("c", result.get(3).getValue());
        assertEquals("d", result.get(4).getValue());
        assertEquals("/", result.get(5).getValue());
        assertEquals("+", result.get(6).getValue());
    }
}
