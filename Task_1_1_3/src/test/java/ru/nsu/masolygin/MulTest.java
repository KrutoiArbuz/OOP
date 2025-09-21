package ru.nsu.masolygin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MulTest {

    @Test
    void testConstructorAndGetters() {
        Expression left = new Number(3);
        Expression right = new Variable("x");
        Mul mul = new Mul(left, right);

        assertSame(left, mul.getLeft());
        assertSame(right, mul.getRight());
    }

    @Test
    void testPrint() {
        Expression left = new Number(2);
        Expression right = new Variable("y");
        Mul mul = new Mul(left, right);

        assertDoesNotThrow(() -> mul.print());
    }

    @Test
    void testDerivative() {
        // (3*x)' = 3'*x + 3*x' = 0*x + 3*1 = 3
        Expression expr = new Mul(new Number(3), new Variable("x"));
        Expression derivative = expr.derivative("x");

        assertTrue(derivative instanceof Add);

        // (x*y)'  x = x'*y + x*y' = 1*y + x*0 = y
        Expression exprXY = new Mul(new Variable("x"), new Variable("y"));
        Expression derivativeX = exprXY.derivative("x");
        assertTrue(derivativeX instanceof Add);
    }

    @Test
    void testEval() {
        // 3 * x  x = 4   12
        Expression expr = new Mul(new Number(3), new Variable("x"));
        assertEquals(12, expr.eval("x = 4"));

        // x * y  x = 5, y = 6   30
        Expression exprXY = new Mul(new Variable("x"), new Variable("y"));
        assertEquals(30, exprXY.eval("x = 5; y = 6"));

        // 7 * 8 = 56
        Expression numbers = new Mul(new Number(7), new Number(8));
        assertEquals(56, numbers.eval(""));
    }

    @Test
    void testSimplify() {
        // 4 * 5 = 20
        Expression numbers = new Mul(new Number(4), new Number(5));
        Expression simplified = numbers.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(20, ((Number) simplified).getValue());

        // 0 * x = 0
        Expression zeroLeft = new Mul(new Number(0), new Variable("x"));
        Expression simplifiedZeroLeft = zeroLeft.simplify();
        assertTrue(simplifiedZeroLeft instanceof Number);
        assertEquals(0, ((Number) simplifiedZeroLeft).getValue());

        // x * 0 = 0
        Expression zeroRight = new Mul(new Variable("x"), new Number(0));
        Expression simplifiedZeroRight = zeroRight.simplify();
        assertTrue(simplifiedZeroRight instanceof Number);
        assertEquals(0, ((Number) simplifiedZeroRight).getValue());

        // 1 * x = x
        Expression oneLeft = new Mul(new Number(1), new Variable("x"));
        Expression simplifiedOneLeft = oneLeft.simplify();
        assertTrue(simplifiedOneLeft instanceof Variable);
        assertEquals("x", ((Variable) simplifiedOneLeft).getName());

        // x * 1 = x
        Expression oneRight = new Mul(new Variable("x"), new Number(1));
        Expression simplifiedOneRight = oneRight.simplify();
        assertTrue(simplifiedOneRight instanceof Variable);
        assertEquals("x", ((Variable) simplifiedOneRight).getName());

        // x * y
        Expression variables = new Mul(new Variable("x"), new Variable("y"));
        Expression simplifiedVars = variables.simplify();
        assertTrue(simplifiedVars instanceof Mul);
    }

    @Test
    void testSimplifyNestedExpressions() {
        // (2 * 3) * x = 6 * x
        Expression nested = new Mul(new Mul(new Number(2), new Number(3)), new Variable("x"));
        Expression simplified = nested.simplify();

        assertTrue(simplified instanceof Mul);
        Mul result = (Mul) simplified;
        assertTrue(result.getLeft() instanceof Number);
        assertEquals(6, ((Number) result.getLeft()).getValue());
        assertTrue(result.getRight() instanceof Variable);
    }

    @Test
    void testBinaryExpressionInheritance() {
        Expression expr = new Mul(new Number(2), new Variable("x"));
        assertTrue(expr instanceof BinaryExpression);

        BinaryExpression binExpr = (BinaryExpression) expr;
        assertNotNull(binExpr.getLeft());
        assertNotNull(binExpr.getRight());
    }

    @Test
    void testSimplifyComplexZeroMultiplication() {
        // 0 * (x + y) = 0
        Expression complex = new Mul(new Number(0), new Add(new Variable("x"), new Variable("y")));
        Expression simplified = complex.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(0, ((Number) simplified).getValue());

        // (x * 0) * y = 0
        Expression nestedZero = new Mul(new Mul(new Variable("x"), new Number(0)), new Variable("y"));
        Expression simplifiedNested = nestedZero.simplify();
        assertTrue(simplifiedNested instanceof Number);
        assertEquals(0, ((Number) simplifiedNested).getValue());
    }
}
