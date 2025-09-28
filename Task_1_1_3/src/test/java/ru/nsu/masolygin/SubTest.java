package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.Expressions.*;
import ru.nsu.masolygin.Expressions.Number;

class SubTest {

    @Test
    void testConstructorAndGetters() {
        Expression left = new ru.nsu.masolygin.Expressions.Number(10);
        Expression right = new Variable("x");
        Sub sub = new Sub(left, right);

        assertSame(left, sub.getLeft());
        assertSame(right, sub.getRight());
    }

    @Test
    void testPrint() {
        Expression left = new ru.nsu.masolygin.Expressions.Number(7);
        Expression right = new Variable("y");
        Sub sub = new Sub(left, right);

        assertDoesNotThrow(() -> sub.print());
    }

    @Test
    void testDerivative() {
        // (5 - x)' = -1
        Expression expr = new Sub(new ru.nsu.masolygin.Expressions.Number(5), new Variable("x"));
        Expression derivative = expr.derivative("x");

        assertTrue(derivative instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(-1, ((ru.nsu.masolygin.Expressions.Number) derivative).getValue());
    }

    @Test
    void testEval() {
        // 10 - x = 7  x = 3
        Expression expr = new Sub(new ru.nsu.masolygin.Expressions.Number(10), new Variable("x"));
        assertEquals(7, expr.eval("x = 3"));

        // x - y = 10  x = 15, y = 5
        Expression exprWithTwoVars = new Sub(new Variable("x"), new Variable("y"));
        assertEquals(10, exprWithTwoVars.eval("x = 15; y = 5"));

        // 20 - 8 = 12
        Expression numbers = new Sub(new ru.nsu.masolygin.Expressions.Number(20), new ru.nsu.masolygin.Expressions.Number(8));
        assertEquals(12, numbers.eval(""));
    }

    @Test
    void testSimplify() {
        // 10 - 3 = 7
        Expression numbers = new Sub(new ru.nsu.masolygin.Expressions.Number(10), new ru.nsu.masolygin.Expressions.Number(3));
        Expression simplified = numbers.simplify();
        assertTrue(simplified instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(7, ((ru.nsu.masolygin.Expressions.Number) simplified).getValue());

        // x - 0 = x
        Expression subZero = new Sub(new Variable("x"), new ru.nsu.masolygin.Expressions.Number(0));
        Expression simplifiedSubZero = subZero.simplify();
        assertTrue(simplifiedSubZero instanceof Variable);
        assertEquals("x", ((Variable) simplifiedSubZero).getName());

        // x - x = 0
        Variable x = new Variable("x");
        Expression sameSub = new Sub(x, x);
        Expression simplifiedSame = sameSub.simplify();
        assertTrue(simplifiedSame instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(0, ((ru.nsu.masolygin.Expressions.Number) simplifiedSame).getValue());

        // (3*x) - (3*x) = 0
        Expression mul1 = new Mul(new ru.nsu.masolygin.Expressions.Number(3), new Variable("x"));
        Expression mul2 = new Mul(new ru.nsu.masolygin.Expressions.Number(3), new Variable("x"));
        Expression complexSub = new Sub(mul1, mul2);
        Expression simplifiedComplex = complexSub.simplify();
        assertTrue(simplifiedComplex instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(0, ((ru.nsu.masolygin.Expressions.Number) simplifiedComplex).getValue());
    }

    @Test
    void testSimplifyNestedExpressions() {
        // (5 - 2) - x = 3 - x
        Expression nested = new Sub(new Sub(new ru.nsu.masolygin.Expressions.Number(5), new ru.nsu.masolygin.Expressions.Number(2)), new Variable("x"));
        Expression simplified = nested.simplify();

        assertTrue(simplified instanceof Sub);
        Sub result = (Sub) simplified;
        assertTrue(result.getLeft() instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(3, ((ru.nsu.masolygin.Expressions.Number) result.getLeft()).getValue());
        assertTrue(result.getRight() instanceof Variable);
    }

    @Test
    void testBinaryExpressionInheritance() {
        Expression expr = new Sub(new ru.nsu.masolygin.Expressions.Number(10), new Variable("x"));
        assertTrue(expr instanceof BinaryExpression);

        BinaryExpression binExpr = (BinaryExpression) expr;
        assertNotNull(binExpr.getLeft());
        assertNotNull(binExpr.getRight());
    }

    @Test
    void testGettersFromBinaryExpression() {
        Expression left = new Number(15);
        Expression right = new Variable("z");
        Sub sub = new Sub(left, right);

        BinaryExpression binExpr = sub;
        assertSame(left, binExpr.getLeft());
        assertSame(right, binExpr.getRight());
    }
}
