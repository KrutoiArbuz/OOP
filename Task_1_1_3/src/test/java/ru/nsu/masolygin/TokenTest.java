package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.BiFunction;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.Expressions.Add;
import ru.nsu.masolygin.Expressions.Div;
import ru.nsu.masolygin.Expressions.Expression;
import ru.nsu.masolygin.Expressions.Mul;
import ru.nsu.masolygin.Expressions.Number;
import ru.nsu.masolygin.Expressions.Sub;

class TokenTest {

    @Test
    void testNumberToken() {
        Token token = new Token(Token.Type.NUMBER, "42");

        assertEquals(Token.Type.NUMBER, token.getType());
        assertEquals("42", token.getValue());
        assertNull(token.getOperation());
    }

    @Test
    void testVariableToken() {
        Token token = new Token(Token.Type.VARIABLE, "x");

        assertEquals(Token.Type.VARIABLE, token.getType());
        assertEquals("x", token.getValue());
        assertNull(token.getOperation());
    }

    @Test
    void testOperatorTokenWithoutOperation() {
        Token token = new Token(Token.Type.OPERATOR, "+");

        assertEquals(Token.Type.OPERATOR, token.getType());
        assertEquals("+", token.getValue());
        assertNull(token.getOperation());
    }

    @Test
    void testOperatorTokenWithOperation() {
        BiFunction<Expression, Expression, Expression> addOperation = Add::new;
        Token token = new Token(Token.Type.OPERATOR, "+", addOperation);

        assertEquals(Token.Type.OPERATOR, token.getType());
        assertEquals("+", token.getValue());
        assertNotNull(token.getOperation());
        assertEquals(addOperation, token.getOperation());
    }

    @Test
    void testLeftParenthesisToken() {
        Token token = new Token(Token.Type.LPAREN, "(");

        assertEquals(Token.Type.LPAREN, token.getType());
        assertEquals("(", token.getValue());
        assertNull(token.getOperation());
    }

    @Test
    void testRightParenthesisToken() {
        Token token = new Token(Token.Type.RPAREN, ")");

        assertEquals(Token.Type.RPAREN, token.getType());
        assertEquals(")", token.getValue());
        assertNull(token.getOperation());
    }

    @Test
    void testOperationExecution() {
        Token token = new Token(Token.Type.OPERATOR, "+", Add::new);
        BiFunction<Expression, Expression, Expression> operation = token.getOperation();

        Expression left = new ru.nsu.masolygin.Expressions.Number(5);
        Expression right = new ru.nsu.masolygin.Expressions.Number(3);
        Expression result = operation.apply(left, right);

        assertTrue(result instanceof Add);
        assertEquals(8, result.eval(""));
    }

    @Test
    void testMultiplicationOperation() {
        Token token = new Token(Token.Type.OPERATOR, "*", Mul::new);
        BiFunction<Expression, Expression, Expression> operation = token.getOperation();

        Expression left = new ru.nsu.masolygin.Expressions.Number(4);
        Expression right = new ru.nsu.masolygin.Expressions.Number(7);
        Expression result = operation.apply(left, right);

        assertTrue(result instanceof Mul);
        assertEquals(28, result.eval(""));
    }
}
