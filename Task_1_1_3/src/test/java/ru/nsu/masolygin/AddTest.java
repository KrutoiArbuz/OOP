package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.Expressions.Add;
import ru.nsu.masolygin.Expressions.BinaryExpression;
import ru.nsu.masolygin.Expressions.Expression;
import ru.nsu.masolygin.Expressions.Number;
import ru.nsu.masolygin.Expressions.Variable;

class AddTest {

    @Test
    void testConstructorAndGetters() {
        Expression left = new Number(5);
        Expression right = new Variable("x");
        Add add = new Add(left, right);

        assertSame(left, add.getLeft());
        assertSame(right, add.getRight());
    }

    @Test
    void testPrint() {
        Expression left = new Number(3);
        Expression right = new Variable("x");
        Add add = new Add(left, right);
        assertDoesNotThrow(() -> add.print());
    }

    @Test
    void testDerivative() {
        // (3 + x)' = 0 + 1 = 1
        Expression expr = new Add(new Number(3), new Variable("x"));
        Expression derivative = expr.derivative("x");

        assertTrue(derivative instanceof Number);
        assertEquals(1, ((Number) derivative).getValue());

        // (x + y)' by x = 1 + 0 = 1
        Expression exprWithTwoVars = new Add(new Variable("x"), new Variable("y"));
        Expression derivativeX = exprWithTwoVars.derivative("x");
        assertTrue(derivativeX instanceof Number);
        assertEquals(1, ((Number) derivativeX).getValue());
    }

    @Test
    void testEval() {
        // 3 + x  x = 5
        Expression expr = new Add(new Number(3), new Variable("x"));
        assertEquals(8, expr.eval("x = 5"));

        // x + y  x = 10, y = 20
        Expression exprWithTwoVars = new Add(new Variable("x"), new Variable("y"));
        assertEquals(30, exprWithTwoVars.eval("x = 10; y = 20"));

        // 5 + 7 = 12
        Expression numbers = new Add(new Number(5), new Number(7));
        assertEquals(12, numbers.eval(""));
    }

    @Test
    void testSimplify() {
        // 3 + 5 = 8
        Expression numbers = new Add(new Number(3), new Number(5));
        Expression simplified = numbers.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(8, ((Number) simplified).getValue());

        // 0 + x = x
        Expression zeroLeft = new Add(new Number(0), new Variable("x"));
        Expression simplifiedZeroLeft = zeroLeft.simplify();
        assertTrue(simplifiedZeroLeft instanceof Variable);
        assertEquals("x", ((Variable) simplifiedZeroLeft).getName());

        // x + 0 = x
        Expression zeroRight = new Add(new Variable("x"), new Number(0));
        Expression simplifiedZeroRight = zeroRight.simplify();
        assertTrue(simplifiedZeroRight instanceof Variable);
        assertEquals("x", ((Variable) simplifiedZeroRight).getName());

        // x + y
        Expression variables = new Add(new Variable("x"), new Variable("y"));
        Expression simplifiedVars = variables.simplify();
        assertTrue(simplifiedVars instanceof Add);
    }

    @Test
    void testNestedAdditions() {
        // (2 + 3) + x = 5 + x
        Expression nested = new Add(new Add(new Number(2), new Number(3)),
                                  new Variable("x"));
        Expression simplified = nested.simplify();

        assertTrue(simplified instanceof Add);
        Add result = (Add) simplified;
        assertTrue(result.getLeft() instanceof Number);
        assertEquals(5, ((Number) result.getLeft()).getValue());
        assertTrue(result.getRight() instanceof Variable);
    }

    @Test
    void testBinaryExpressionInheritance() {
        Expression expr = new Add(new Number(2), new Variable("x"));
        assertTrue(expr instanceof BinaryExpression);

        BinaryExpression binExpr = (BinaryExpression) expr;
        assertNotNull(binExpr.getLeft());
        assertNotNull(binExpr.getRight());
    }

    @Test
    void testGettersFromBinaryExpression() {
        Expression left = new Number(5);
        Expression right = new Variable("y");
        Add add = new Add(left, right);

        BinaryExpression binExpr = add;
        assertSame(left, binExpr.getLeft());
        assertSame(right, binExpr.getRight());
    }

    @Test
    void testAdditionCommutativity() {
        // x + 5 = 5 + x
        Expression expr1 = new Add(new Variable("x"), new Number(5));
        Expression expr2 = new Add(new Number(5), new Variable("x"));

        assertEquals(expr1.eval("x=10"), expr2.eval("x=10"));
    }
}
