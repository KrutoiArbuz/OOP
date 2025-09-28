package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.Expressions.*;
import ru.nsu.masolygin.Expressions.Number;

class DivTest {

    @Test
    void testConstructorAndGetters() {
        Expression left = new ru.nsu.masolygin.Expressions.Number(10);
        Expression right = new Variable("x");
        Div div = new Div(left, right);

        assertSame(left, div.getLeft());
        assertSame(right, div.getRight());
    }

    @Test
    void testPrint() {
        Expression left = new ru.nsu.masolygin.Expressions.Number(8);
        Expression right = new Variable("y");
        Div div = new Div(left, right);

        assertDoesNotThrow(() -> div.print());
    }

    @Test
    void testDerivative() {
        // (x/2)' = 0
        Expression expr = new Div(new Variable("x"), new ru.nsu.masolygin.Expressions.Number(2));
        Expression derivative = expr.derivative("x");

        assertTrue(derivative instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(0, ((ru.nsu.masolygin.Expressions.Number) derivative).getValue());
    }

    @Test
    void testEval() {
        // 12 / x  x = 3
        Expression expr = new Div(new ru.nsu.masolygin.Expressions.Number(12), new Variable("x"));
        assertEquals(4, expr.eval("x = 3"));

        // x / y  x = 20, y = 4
        Expression exprWithTwoVars = new Div(new Variable("x"), new Variable("y"));
        assertEquals(5, exprWithTwoVars.eval("x = 20; y = 4"));

        // 15 / 3 = 5
        Expression numbers = new Div(new ru.nsu.masolygin.Expressions.Number(15), new ru.nsu.masolygin.Expressions.Number(3));
        assertEquals(5, numbers.eval(""));
    }

    @Test
    void testEvalDivisionByZero() {
        Expression expr = new Div(new ru.nsu.masolygin.Expressions.Number(10), new Variable("x"));
        assertThrows(ArithmeticException.class, () -> expr.eval("x = 0"));

        Expression numbers = new Div(new ru.nsu.masolygin.Expressions.Number(5), new ru.nsu.masolygin.Expressions.Number(0));
        assertThrows(ArithmeticException.class, () -> numbers.eval(""));
    }

    @Test
    void testSimplify() {
        // 20 / 4 = 5
        Expression numbers = new Div(new ru.nsu.masolygin.Expressions.Number(20), new ru.nsu.masolygin.Expressions.Number(4));
        Expression simplified = numbers.simplify();
        assertTrue(simplified instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(5, ((ru.nsu.masolygin.Expressions.Number) simplified).getValue());

        // x / 1 = x
        Expression divByOne = new Div(new Variable("x"), new ru.nsu.masolygin.Expressions.Number(1));
        Expression simplifiedDivOne = divByOne.simplify();
        assertTrue(simplifiedDivOne instanceof Variable);
        assertEquals("x", ((Variable) simplifiedDivOne).getName());

        // 0 / x = 0
        Expression zeroDiv = new Div(new ru.nsu.masolygin.Expressions.Number(0), new Variable("x"));
        Expression simplifiedZero = zeroDiv.simplify();
        assertTrue(simplifiedZero instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(0, ((ru.nsu.masolygin.Expressions.Number) simplifiedZero).getValue());

        // x / y
        Expression variables = new Div(new Variable("x"), new Variable("y"));
        Expression simplifiedVars = variables.simplify();
        assertTrue(simplifiedVars instanceof Div);
    }

    @Test
    void testSimplifyDivisionByZero() {
        // 5 / 0
        Expression divByZero = new Div(new ru.nsu.masolygin.Expressions.Number(5), new ru.nsu.masolygin.Expressions.Number(0));
        assertThrows(ArithmeticException.class, () -> divByZero.simplify());
    }

    @Test
    void testSimplifyNestedExpressions() {
        // (20 / 4) / x = 5 / x
        Expression nested = new Div(new Div(new ru.nsu.masolygin.Expressions.Number(20), new ru.nsu.masolygin.Expressions.Number(4)), new Variable("x"));
        Expression simplified = nested.simplify();

        assertTrue(simplified instanceof Div);
        Div result = (Div) simplified;
        assertTrue(result.getLeft() instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(5, ((ru.nsu.masolygin.Expressions.Number) result.getLeft()).getValue());
        assertTrue(result.getRight() instanceof Variable);
    }

    @Test
    void testSimplifyComplexZeroDivision() {
        // 0 / (x + y) = 0
        Expression complex = new Div(new ru.nsu.masolygin.Expressions.Number(0), new Add(new Variable("x"), new Variable("y")));
        Expression simplified = complex.simplify();
        assertTrue(simplified instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(0, ((ru.nsu.masolygin.Expressions.Number) simplified).getValue());
    }

    @Test
    void testIntegerDivision() {
        Expression expr = new Div(new ru.nsu.masolygin.Expressions.Number(7), new ru.nsu.masolygin.Expressions.Number(3));
        Expression simplified = expr.simplify();
        assertTrue(simplified instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(2, ((ru.nsu.masolygin.Expressions.Number) simplified).getValue()); // 7/3 = 2

        Expression negativeDiv = new Div(new ru.nsu.masolygin.Expressions.Number(-10), new ru.nsu.masolygin.Expressions.Number(3));
        Expression simplifiedNegative = negativeDiv.simplify();
        assertTrue(simplifiedNegative instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(-3, ((ru.nsu.masolygin.Expressions.Number) simplifiedNegative).getValue()); // -10/3 = -3
    }

    @Test
    void testComplexDerivative() {
        // (x*y)/z
        Expression numerator = new Mul(new Variable("x"), new Variable("y"));
        Expression denominator = new Variable("z");
        Expression complexDiv = new Div(numerator, denominator);

        Expression derivative = complexDiv.derivative("x");
        assertTrue(derivative instanceof Div);


        assertNotNull(derivative);
    }

    @Test
    void testEvalWithComplexExpressions() {
        // (x + y) / (z - 1) x=6, y=4, z=3 (6+4)/(3-1) = 10/2 = 5
        Expression numerator = new Add(new Variable("x"), new Variable("y"));
        Expression denominator = new Sub(new Variable("z"), new ru.nsu.masolygin.Expressions.Number(1));
        Expression complexDiv = new Div(numerator, denominator);

        assertEquals(5, complexDiv.eval("x = 6; y = 4; z = 3"));
    }

    @Test
    void testBinaryExpressionInheritance() {
        Expression expr = new Div(new ru.nsu.masolygin.Expressions.Number(10), new Variable("x"));
        assertTrue(expr instanceof BinaryExpression);

        BinaryExpression binExpr = (BinaryExpression) expr;
        assertNotNull(binExpr.getLeft());
        assertNotNull(binExpr.getRight());
    }

    @Test
    void testGettersFromBinaryExpression() {
        Expression left = new Number(20);
        Expression right = new Variable("w");
        Div div = new Div(left, right);

        BinaryExpression binExpr = div;
        assertSame(left, binExpr.getLeft());
        assertSame(right, binExpr.getRight());

        assertSame(left, div.getLeft());
        assertSame(right, div.getRight());
    }
}
