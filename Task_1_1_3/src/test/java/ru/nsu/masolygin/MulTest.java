package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.Expressions.*;
import ru.nsu.masolygin.Expressions.Number;

class MulTest {

    @Test
    void testConstructorAndGetters() {
        Expression left = new ru.nsu.masolygin.Expressions.Number(3);
        Expression right = new Variable("x");
        Mul mul = new Mul(left, right);

        assertSame(left, mul.getLeft());
        assertSame(right, mul.getRight());
    }

    @Test
    void testPrint() {
        Expression left = new ru.nsu.masolygin.Expressions.Number(2);
        Expression right = new Variable("y");
        Mul mul = new Mul(left, right);

        assertDoesNotThrow(() -> mul.print());
    }

    @Test
    void testDerivative() {
        // (3*x)' = 3
        Expression expr = new Mul(new ru.nsu.masolygin.Expressions.Number(3), new Variable("x"));
        Expression derivative = expr.derivative("x");

        assertTrue(derivative instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(3, ((ru.nsu.masolygin.Expressions.Number) derivative).getValue());

        // (x*y)' x = y
        Expression exprWithTwoVars = new Mul(new Variable("x"), new Variable("y"));
        Expression derivativeX = exprWithTwoVars.derivative("x");
        assertTrue(derivativeX instanceof Variable);
        assertEquals("y", ((Variable) derivativeX).getName());
    }

    @Test
    void testEval() {
        // 3 * x = 12  x = 4
        Expression expr = new Mul(new ru.nsu.masolygin.Expressions.Number(3), new Variable("x"));
        assertEquals(12, expr.eval("x = 4"));

        // x * y = 30  x = 5, y = 6
        Expression exprWithTwoVars = new Mul(new Variable("x"), new Variable("y"));
        assertEquals(30, exprWithTwoVars.eval("x = 5; y = 6"));

        // 7 * 8 = 56
        Expression numbers = new Mul(new ru.nsu.masolygin.Expressions.Number(7), new ru.nsu.masolygin.Expressions.Number(8));
        assertEquals(56, numbers.eval(""));
    }

    @Test
    void testSimplify() {
        // 4 * 5 = 20
        Expression numbers = new Mul(new ru.nsu.masolygin.Expressions.Number(4), new ru.nsu.masolygin.Expressions.Number(5));
        Expression simplified = numbers.simplify();
        assertTrue(simplified instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(20, ((ru.nsu.masolygin.Expressions.Number) simplified).getValue());

        // 0 * x = 0
        Expression zeroLeft = new Mul(new ru.nsu.masolygin.Expressions.Number(0), new Variable("x"));
        Expression simplifiedZeroLeft = zeroLeft.simplify();
        assertTrue(simplifiedZeroLeft instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(0, ((ru.nsu.masolygin.Expressions.Number) simplifiedZeroLeft).getValue());

        // x * 0 = 0
        Expression zeroRight = new Mul(new Variable("x"), new ru.nsu.masolygin.Expressions.Number(0));
        Expression simplifiedZeroRight = zeroRight.simplify();
        assertTrue(simplifiedZeroRight instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(0, ((ru.nsu.masolygin.Expressions.Number) simplifiedZeroRight).getValue());

        // 1 * x = x
        Expression oneLeft = new Mul(new ru.nsu.masolygin.Expressions.Number(1), new Variable("x"));
        Expression simplifiedOneLeft = oneLeft.simplify();
        assertTrue(simplifiedOneLeft instanceof Variable);
        assertEquals("x", ((Variable) simplifiedOneLeft).getName());

        // x * 1 = x
        Expression oneRight = new Mul(new Variable("x"), new ru.nsu.masolygin.Expressions.Number(1));
        Expression simplifiedOneRight = oneRight.simplify();
        assertTrue(simplifiedOneRight instanceof Variable);
        assertEquals("x", ((Variable) simplifiedOneRight).getName());

        Expression variables = new Mul(new Variable("x"), new Variable("y"));
        Expression simplifiedVars = variables.simplify();
        assertTrue(simplifiedVars instanceof Mul);
    }

    @Test
    void testSimplifyNestedExpressions() {
        // (2 * 3) * x = 6 * x
        Expression nested = new Mul(new Mul(new ru.nsu.masolygin.Expressions.Number(2), new ru.nsu.masolygin.Expressions.Number(3)), new Variable("x"));
        Expression simplified = nested.simplify();

        assertTrue(simplified instanceof Mul);
        Mul result = (Mul) simplified;
        assertTrue(result.getLeft() instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(6, ((ru.nsu.masolygin.Expressions.Number) result.getLeft()).getValue());
        assertTrue(result.getRight() instanceof Variable);
    }

    @Test
    void testBinaryExpressionInheritance() {
        Expression expr = new Mul(new ru.nsu.masolygin.Expressions.Number(2), new Variable("x"));
        assertTrue(expr instanceof BinaryExpression);

        BinaryExpression binExpr = (BinaryExpression) expr;
        assertNotNull(binExpr.getLeft());
        assertNotNull(binExpr.getRight());
    }

    @Test
    void testSimplifyComplexZeroMultiplication() {
        Expression complex = new Mul(new ru.nsu.masolygin.Expressions.Number(0), new Add(new Variable("x"), new Variable("y")));
        Expression simplified = complex.simplify();
        assertTrue(simplified instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(0, ((ru.nsu.masolygin.Expressions.Number) simplified).getValue());

        Expression nestedZero = new Mul(new Mul(new Variable("x"), new ru.nsu.masolygin.Expressions.Number(0)), new Variable("y"));
        Expression simplifiedNested = nestedZero.simplify();
        assertTrue(simplifiedNested instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(0, ((Number) simplifiedNested).getValue());
    }
}
