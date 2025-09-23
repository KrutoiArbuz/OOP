package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DivTest {

    @Test
    void testConstructorAndGetters() {
        Expression left = new Number(10);
        Expression right = new Variable("x");
        Div div = new Div(left, right);

        assertSame(left, div.getLeft());
        assertSame(right, div.getRight());
    }

    @Test
    void testPrint() {
        Expression left = new Number(8);
        Expression right = new Variable("y");
        Div div = new Div(left, right);

        assertDoesNotThrow(() -> div.print());
    }

    @Test
    void testDerivative() {
        // (x/2)' = (x'*2 - x*2')/(2^2) = (1*2 - x*0)/4 = 2/4 = 1/2
        Expression expr = new Div(new Variable("x"), new Number(2));
        Expression derivative = expr.derivative("x");

        assertTrue(derivative instanceof Div);

        // (f/g)' = (f'*g - f*g')/g^2
        Div divDerivative = (Div) derivative;
        assertTrue(divDerivative.getLeft() instanceof Sub);
        assertTrue(divDerivative.getRight() instanceof Mul);
    }

    @Test
    void testEval() {
        // 12 / x when x = 3 should be 4
        Expression expr = new Div(new Number(12), new Variable("x"));
        assertEquals(4, expr.eval("x = 3"));

        // x / y when x = 20, y = 4 should be 5
        Expression exprWithTwoVars = new Div(new Variable("x"), new Variable("y"));
        assertEquals(5, exprWithTwoVars.eval("x = 20; y = 4"));

        // 15 / 3 = 5
        Expression numbers = new Div(new Number(15), new Number(3));
        assertEquals(5, numbers.eval(""));
    }

    @Test
    void testEvalDivisionByZero() {
        Expression expr = new Div(new Number(10), new Variable("x"));
        assertThrows(ArithmeticException.class, () -> expr.eval("x = 0"));

        Expression numbers = new Div(new Number(5), new Number(0));
        assertThrows(ArithmeticException.class, () -> numbers.eval(""));
    }

    @Test
    void testSimplify() {
        // 20 / 4 = 5
        Expression numbers = new Div(new Number(20), new Number(4));
        Expression simplified = numbers.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(5, ((Number) simplified).getValue());

        // x / 1 = x
        Expression divByOne = new Div(new Variable("x"), new Number(1));
        Expression simplifiedDivOne = divByOne.simplify();
        assertTrue(simplifiedDivOne instanceof Variable);
        assertEquals("x", ((Variable) simplifiedDivOne).getName());

        // 0 / x = 0
        Expression zeroDiv = new Div(new Number(0), new Variable("x"));
        Expression simplifiedZero = zeroDiv.simplify();
        assertTrue(simplifiedZero instanceof Number);
        assertEquals(0, ((Number) simplifiedZero).getValue());

        // x / y
        Expression variables = new Div(new Variable("x"), new Variable("y"));
        Expression simplifiedVars = variables.simplify();
        assertTrue(simplifiedVars instanceof Div);
    }

    @Test
    void testSimplifyDivisionByZero() {
        // 5 / 0
        Expression divByZero = new Div(new Number(5), new Number(0));
        assertThrows(ArithmeticException.class, () -> divByZero.simplify());
    }

    @Test
    void testSimplifyNestedExpressions() {
        // (20 / 4) / x = 5 / x
        Expression nested = new Div(new Div(new Number(20), new Number(4)), new Variable("x"));
        Expression simplified = nested.simplify();

        assertTrue(simplified instanceof Div);
        Div result = (Div) simplified;
        assertTrue(result.getLeft() instanceof Number);
        assertEquals(5, ((Number) result.getLeft()).getValue());
        assertTrue(result.getRight() instanceof Variable);
    }

    @Test
    void testSimplifyComplexZeroDivision() {
        // 0 / (x + y) = 0
        Expression complex = new Div(new Number(0), new Add(new Variable("x"), new Variable("y")));
        Expression simplified = complex.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(0, ((Number) simplified).getValue());
    }

    @Test
    void testIntegerDivision() {
        Expression expr = new Div(new Number(7), new Number(3));
        Expression simplified = expr.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(2, ((Number) simplified).getValue()); // 7/3 = 2

        Expression negativeDiv = new Div(new Number(-10), new Number(3));
        Expression simplifiedNegative = negativeDiv.simplify();
        assertTrue(simplifiedNegative instanceof Number);
        assertEquals(-3, ((Number) simplifiedNegative).getValue()); // -10/3 = -3
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
        Expression denominator = new Sub(new Variable("z"), new Number(1));
        Expression complexDiv = new Div(numerator, denominator);

        assertEquals(5, complexDiv.eval("x = 6; y = 4; z = 3"));
    }

    @Test
    void testBinaryExpressionInheritance() {
        Expression expr = new Div(new Number(10), new Variable("x"));
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
