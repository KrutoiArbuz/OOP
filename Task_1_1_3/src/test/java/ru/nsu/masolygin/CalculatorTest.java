package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.Expressions.Add;
import ru.nsu.masolygin.Expressions.BinaryExpression;
import ru.nsu.masolygin.Expressions.Div;
import ru.nsu.masolygin.Expressions.Expression;
import ru.nsu.masolygin.Expressions.Mul;
import ru.nsu.masolygin.Expressions.Number;
import ru.nsu.masolygin.Expressions.Sub;
import ru.nsu.masolygin.Expressions.Variable;

class CalculatorTest {

    @Test
    void testProcessExpressionBasicVariable() {
        Calculator calculator = new Calculator();
        Expression result = calculator.processExpression("(((x)))");

        assertTrue(result instanceof Variable);
        assertEquals("x", ((Variable) result).getName());
    }

    @Test
    void testProcessExpressionSimpleAddition() {
        Calculator calculator = new Calculator();
        Expression result = calculator.processExpression("((x)+(y))");

        assertTrue(result instanceof Add);
        Add add = (Add) result;
        assertTrue(add.getLeft() instanceof Variable);
        assertTrue(add.getRight() instanceof Variable);
        assertEquals("x", ((Variable) add.getLeft()).getName());
        assertEquals("y", ((Variable) add.getRight()).getName());
    }

    @Test
    void testProcessExpressionComplexExpression() {
        Calculator calculator = new Calculator();
        Expression result = calculator.processExpression("((a)+((b)*(c)))");

        assertTrue(result instanceof Add);
        Add add = (Add) result;
        assertTrue(add.getLeft() instanceof Variable);
        assertTrue(add.getRight() instanceof Mul);
        assertEquals("a", ((Variable) add.getLeft()).getName());

        Mul mul = (Mul) add.getRight();
        assertTrue(mul.getLeft() instanceof Variable);
        assertTrue(mul.getRight() instanceof Variable);
        assertEquals("b", ((Variable) mul.getLeft()).getName());
        assertEquals("c", ((Variable) mul.getRight()).getName());
    }

    @Test
    void testProcessExpressionMultiCharacterVariables() {
        Calculator calculator = new Calculator();

        Expression result1 = calculator.processExpression("(alpha+beta)");
        assertTrue(result1 instanceof Add);
        Add add = (Add) result1;
        assertEquals("alpha", ((Variable) add.getLeft()).getName());
        assertEquals("beta", ((Variable) add.getRight()).getName());

        Expression result2 = calculator.processExpression("(veryLongVariableName * short)");
        assertTrue(result2 instanceof Mul);
        Mul mul = (Mul) result2;
        assertEquals("veryLongVariableName", ((Variable) mul.getLeft()).getName());
        assertEquals("short", ((Variable) mul.getRight()).getName());
    }

    @Test
    void testProcessExpressionWhitespaceHandling() {
        Calculator calculator = new Calculator();

        Expression result1 = calculator.processExpression("( x + y )");
        Expression result2 = calculator.processExpression("(x+ y)");
        Expression result3 = calculator.processExpression("(x +y)");

        assertTrue(result1 instanceof Add);
        assertTrue(result2 instanceof Add);
        assertTrue(result3 instanceof Add);

        assertEquals("x", ((Variable) ((Add) result1).getLeft()).getName());
        assertEquals("y", ((Variable) ((Add) result1).getRight()).getName());
        assertEquals("x", ((Variable) ((Add) result2).getLeft()).getName());
        assertEquals("y", ((Variable) ((Add) result2).getRight()).getName());
        assertEquals("x", ((Variable) ((Add) result3).getLeft()).getName());
        assertEquals("y", ((Variable) ((Add) result3).getRight()).getName());
    }

    @Test
    void testProcessExpressionNegativeNumbers() {
        Calculator calculator = new Calculator();

        Expression result1 = calculator.processExpression("(-5)");
        assertTrue(result1 instanceof Number);
        assertEquals(-5, ((Number) result1).getValue());

        Expression result2 = calculator.processExpression("(x + (-3))");
        assertTrue(result2 instanceof Add);
        Add add = (Add) result2;
        assertTrue(add.getLeft() instanceof Variable);
        assertTrue(add.getRight() instanceof Number);
        assertEquals("x", ((Variable) add.getLeft()).getName());
        assertEquals(-3, ((Number) add.getRight()).getValue());
    }

    @Test
    void testProcessExpressionVeryComplex() {
        Calculator calculator = new Calculator();
        String complexExpr = "((a*b)+((c-d)/(1+2)))";
        Expression result = calculator.processExpression(complexExpr);

        assertTrue(result instanceof Add);
        Add mainAdd = (Add) result;

        assertTrue(mainAdd.getLeft() instanceof Mul);
        assertTrue(mainAdd.getRight() instanceof Div);

        Mul leftMul = (Mul) mainAdd.getLeft();
        assertEquals("a", ((Variable) leftMul.getLeft()).getName());
        assertEquals("b", ((Variable) leftMul.getRight()).getName());

        Div rightDiv = (Div) mainAdd.getRight();
        assertTrue(rightDiv.getLeft() instanceof Sub);
        assertTrue(rightDiv.getRight() instanceof Add);
    }

    @Test
    void testProcessExpressionInvalidInput() {
        Calculator calculator = new Calculator();

        assertThrows(ParseException.class,
                   () -> calculator.processExpression("(x+y"));
        assertThrows(ParseException.class,
                   () -> calculator.processExpression("x+y)"));
        assertThrows(IllegalArgumentException.class,
                   () -> calculator.processExpression("(x # y)"));
        assertThrows(ParseException.class,
                   () -> calculator.processExpression("()"));
        assertThrows(ParseException.class,
                   () -> calculator.processExpression("(+)"));
        assertThrows(ParseException.class,
                   () -> calculator.processExpression("(x++)"));
    }

    @Test
    void testExpressionEvaluationBasic() {
        Expression e = new Add(
            new Mul(new Variable("x"), new Variable("y")),
            new Div(new Variable("z"), new Number(2))
        );

        assertEquals(17, e.eval("x=3; y=4; z=10"));
        assertEquals(4, e.eval("x=0; y=100; z=8"));
    }

    @Test
    void testExpressionEvaluationDifferentOrder() {
        Expression e = new Add(
            new Mul(new Variable("x"), new Variable("y")),
            new Div(new Variable("z"), new Number(2))
        );

        assertEquals(17, e.eval("z=10; x=3; y=4"));
    }

    @Test
    void testExpressionEvaluationExtraVariables() {
        Expression e = new Add(
            new Mul(new Variable("x"), new Variable("y")),
            new Div(new Variable("z"), new Number(2))
        );

        assertEquals(17, e.eval("x=3; y=4; z=10; w=100"));
    }

    @Test
    void testExpressionEvaluationMissingVariable() {
        Expression e = new Add(
            new Mul(new Variable("x"), new Variable("y")),
            new Div(new Variable("z"), new Number(2))
        );

        assertThrows(IllegalArgumentException.class, () -> e.eval("x=3; y=4"));
    }

    @Test
    void testExpressionEvaluationConstantsOnly() {
        Number num = new Number(5);
        assertEquals(5, num.eval(""));

        Add add = new Add(new Number(2), new Number(3));
        assertEquals(5, add.eval(""));
    }

    @Test
    void testExpressionEvaluationInvalidAssignments() {
        Expression e = new Variable("x");

        assertThrows(IllegalArgumentException.class, () -> e.eval("x=; y=5"));
        assertThrows(IllegalArgumentException.class, () -> e.eval("x=abc"));
        assertThrows(IllegalArgumentException.class, () -> e.eval("x=5 y=3"));
    }

    @Test
    void testExpressionEvaluationRobustParsing() {
        Expression e = new Add(new Variable("x"), new Variable("y"));
        assertEquals(7, e.eval(" x = 3 ; y = 4 ; ; "));
    }

    @Test
    void testExpressionEvaluationNonStandardSeparators() {
        Expression e = new Add(new Variable("x"), new Variable("y"));
        assertThrows(IllegalArgumentException.class, () -> e.eval("x=3, y=4"));
    }

    @Test
    void testDerivativeConstant() {
        Number num = new Number(5);
        Expression derivative = num.derivative("x");

        assertTrue(derivative instanceof Number);
        assertEquals(0, ((Number) derivative).getValue());
    }

    @Test
    void testDerivativeVariableByItself() {
        Variable var = new Variable("x");
        Expression derivative = var.derivative("x");

        assertTrue(derivative instanceof Number);
        assertEquals(1, ((Number) derivative).getValue());
    }

    @Test
    void testDerivativeVariableByOther() {
        Variable var = new Variable("x");
        Expression derivative = var.derivative("y");

        assertTrue(derivative instanceof Number);
        assertEquals(0, ((Number) derivative).getValue());
    }

    @Test
    void testDerivativeExpressionWithoutVariable() {
        Expression e = new Add(new Number(3), new Number(5));
        Expression derivative = e.derivative("x");

        assertTrue(derivative instanceof Number);
        assertEquals(0, ((Number) derivative).getValue());
    }

    @Test
    void testDerivativeMultiple() {
        Expression e = new Variable("x");
        Expression de1 = e.derivative("x");
        Expression de2 = de1.derivative("x");

        assertTrue(de1 instanceof Number);
        assertTrue(de2 instanceof Number);
        assertEquals(1, ((Number) de1).getValue());
        assertEquals(0, ((Number) de2).getValue());
    }

    @Test
    void testDerivativeComplexFunction() {
        Expression inner = new Add(
            new Mul(new Variable("x"), new Variable("x")),
            new Mul(new Number(3), new Variable("x"))
        );
        Expression f = new Mul(inner, new Mul(inner, inner));
        Expression derivative = f.derivative("x");

        assertNotNull(derivative);
    }

    @Test
    void testClearVariables() {
        Calculator calculator = new Calculator();

        assertDoesNotThrow(() -> calculator.clearVariables());
    }

    @Test
    void testBinaryExpressionInheritance() {
        Expression add = new Add(new Number(1), new Variable("x"));
        Expression sub = new Sub(new Number(2), new Variable("y"));
        Expression mul = new Mul(new Variable("a"), new Variable("b"));
        final Expression div = new Div(new Variable("c"), new Number(3));

        assertTrue(add instanceof BinaryExpression);
        assertTrue(sub instanceof BinaryExpression);
        assertTrue(mul instanceof BinaryExpression);
        assertTrue(div instanceof BinaryExpression);
    }

    @Test
    void testExpressionPrint() {
        Expression simple = new Variable("x");
        Expression complex = new Add(new Variable("x"), new Number(5));

        assertDoesNotThrow(() -> simple.print());
        assertDoesNotThrow(() -> complex.print());
    }
}
