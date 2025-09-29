package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.Expressions.Add;
import ru.nsu.masolygin.Expressions.Expression;
import ru.nsu.masolygin.Expressions.Mul;
import ru.nsu.masolygin.Expressions.Variable;

class ExpressionBuilderTest {
    private ExpressionBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new ExpressionBuilder();
    }

    @Test
    void testBuildSimpleNumber() {
        Token token = new Token(Token.Type.NUMBER, "5");
        Expression result = builder.buildExpressionFromPostfix(Collections.singletonList(token));

        assertTrue(result instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(5, result.eval(""));
    }

    @Test
    void testBuildSimpleVariable() {
        Token token = new Token(Token.Type.VARIABLE, "x");
        Expression result = builder.buildExpressionFromPostfix(Collections.singletonList(token));

        assertTrue(result instanceof Variable);
        assertEquals(3, result.eval("x=3"));
    }

    @Test
    void testBuildAddition() {
        Expression result = builder.buildExpressionFromPostfix(Arrays.asList(
            new Token(Token.Type.VARIABLE, "x"),
            new Token(Token.Type.VARIABLE, "y"),
            new Token(Token.Type.OPERATOR, "+", Add::new)
        ));

        assertTrue(result instanceof Add);
        assertEquals(7, result.eval("x=3; y=4"));
    }

    @Test
    void testBuildComplexExpression() {
        Expression result = builder.buildExpressionFromPostfix(Arrays.asList(
            new Token(Token.Type.VARIABLE, "a"),
            new Token(Token.Type.VARIABLE, "b"),
            new Token(Token.Type.VARIABLE, "c"),
            new Token(Token.Type.OPERATOR, "*", Mul::new),
            new Token(Token.Type.OPERATOR, "+", Add::new)
        ));

        assertEquals(14, result.eval("a=2; b=3; c=4"));
    }

    @Test
    void testEmptyPostfixThrowsException() {
        assertThrows(ParseException.class,
            () -> builder.buildExpressionFromPostfix(Collections.emptyList()));
    }

    @Test
    void testInvalidNumberFormatThrowsException() {
        Token token = new Token(Token.Type.NUMBER, "abc");
        assertThrows(ParseException.class,
            () -> builder.buildExpressionFromPostfix(Collections.singletonList(token)));
    }

    @Test
    void testEmptyVariableNameThrowsException() {
        Token token = new Token(Token.Type.VARIABLE, "");
        assertThrows(ParseException.class,
            () -> builder.buildExpressionFromPostfix(Collections.singletonList(token)));
    }

    @Test
    void testNotEnoughOperandsThrowsException() {
        assertThrows(ParseException.class,
            () -> builder.buildExpressionFromPostfix(Arrays.asList(
                new Token(Token.Type.VARIABLE, "x"),
                new Token(Token.Type.OPERATOR, "+", Add::new)
            )));
    }

    @Test
    void testTooManyOperandsThrowsException() {
        assertThrows(ParseException.class,
            () -> builder.buildExpressionFromPostfix(Arrays.asList(
                new Token(Token.Type.VARIABLE, "x"),
                new Token(Token.Type.VARIABLE, "y"),
                new Token(Token.Type.VARIABLE, "z")
            )));
    }

    @Test
    void testNegativeNumber() {
        Token token = new Token(Token.Type.NUMBER, "-5");
        Expression result = builder.buildExpressionFromPostfix(Collections.singletonList(token));

        assertEquals(-5, result.eval(""));
    }
}
