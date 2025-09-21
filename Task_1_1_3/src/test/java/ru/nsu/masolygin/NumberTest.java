package ru.nsu.masolygin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NumberTest {

    @Test
    void testConstructorAndGetValue() {
        Number num = new Number(42);
        assertEquals(42, num.getValue());

        Number negativeNum = new Number(-15);
        assertEquals(-15, negativeNum.getValue());

        Number zero = new Number(0);
        assertEquals(0, zero.getValue());
    }

    @Test
    void testPrint() {
        Number num = new Number(123);

        assertDoesNotThrow(() -> num.print());
    }

    @Test
    void testDerivative() {
        Number num = new Number(42);
        Expression derivative = num.derivative("x");

        assertTrue(derivative instanceof Number);
        assertEquals(0, ((Number) derivative).getValue());


        Expression derivativeY = num.derivative("y");
        assertTrue(derivativeY instanceof Number);
        assertEquals(0, ((Number) derivativeY).getValue());
    }

    @Test
    void testEval() {
        Number num = new Number(100);


        assertEquals(100, num.eval("x = 5; y = 10"));
        assertEquals(100, num.eval(""));
        assertEquals(100, num.eval("a = 1"));
    }

    @Test
    void testSimplify() {
        Number num = new Number(77);
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
