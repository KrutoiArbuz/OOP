package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.Expressions.BinaryExpression;
import ru.nsu.masolygin.Expressions.Expression;
import ru.nsu.masolygin.Expressions.Number;

class NumberTest {

    @Test
    void testConstructorAndGetValue() {
        ru.nsu.masolygin.Expressions.Number num = new ru.nsu.masolygin.Expressions.Number(42);
        assertEquals(42, num.getValue());

        ru.nsu.masolygin.Expressions.Number negativeNum = new ru.nsu.masolygin.Expressions.Number(-15);
        assertEquals(-15, negativeNum.getValue());

        ru.nsu.masolygin.Expressions.Number zero = new ru.nsu.masolygin.Expressions.Number(0);
        assertEquals(0, zero.getValue());
    }

    @Test
    void testPrint() {
        ru.nsu.masolygin.Expressions.Number num = new ru.nsu.masolygin.Expressions.Number(123);
        assertDoesNotThrow(() -> num.print());
    }

    @Test
    void testDerivative() {
        ru.nsu.masolygin.Expressions.Number num = new ru.nsu.masolygin.Expressions.Number(42);
        Expression derivative = num.derivative("x");

        assertTrue(derivative instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(0, ((ru.nsu.masolygin.Expressions.Number) derivative).getValue());

        Expression derivativeY = num.derivative("y");
        assertTrue(derivativeY instanceof ru.nsu.masolygin.Expressions.Number);
        assertEquals(0, ((ru.nsu.masolygin.Expressions.Number) derivativeY).getValue());
    }

    @Test
    void testEval() {
        ru.nsu.masolygin.Expressions.Number num = new ru.nsu.masolygin.Expressions.Number(100);

        assertEquals(100, num.eval("x = 5; y = 10"));
        assertEquals(100, num.eval(""));
        assertEquals(100, num.eval("a = 1"));
    }

    @Test
    void testSimplify() {
        ru.nsu.masolygin.Expressions.Number num = new ru.nsu.masolygin.Expressions.Number(77);
        Expression simplified = num.simplify();

        assertSame(num, simplified);
    }

    @Test
    void testNotBinaryExpression() {
        Expression num = new Number(42);
        assertFalse(num instanceof BinaryExpression);

        assertThrows(ClassCastException.class, () -> {
            BinaryExpression binExpr = (BinaryExpression) num;
        });
    }

}
