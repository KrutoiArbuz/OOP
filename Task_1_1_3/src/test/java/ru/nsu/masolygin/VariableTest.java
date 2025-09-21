package ru.nsu.masolygin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VariableTest {

    @Test
    void testConstructorAndGetName() {
        Variable var = new Variable("x");
        assertEquals("x", var.getName());

        Variable multiChar = new Variable("variable");
        assertEquals("variable", multiChar.getName());

        Variable singleChar = new Variable("y");
        assertEquals("y", singleChar.getName());
    }

    @Test
    void testPrint() {
        Variable var = new Variable("test");

        assertDoesNotThrow(() -> var.print());
    }

    @Test
    void testDerivative() {
        Variable x = new Variable("x");
        Variable y = new Variable("y");


        Expression dxdx = x.derivative("x");
        assertTrue(dxdx instanceof Number);
        assertEquals(1, ((Number) dxdx).getValue());


        Expression dxdy = x.derivative("y");
        assertTrue(dxdy instanceof Number);
        assertEquals(0, ((Number) dxdy).getValue());

        Expression dydy = y.derivative("y");
        assertTrue(dydy instanceof Number);
        assertEquals(1, ((Number) dydy).getValue());
    }

    @Test
    void testEval() {
        Variable x = new Variable("x");
        Variable y = new Variable("y");


        assertEquals(10, x.eval("x = 10; y = 5"));
        assertEquals(5, y.eval("x = 10; y = 5"));
        assertEquals(42, x.eval("x = 42"));


        assertEquals(15, x.eval("x=15"));
        assertEquals(20, x.eval(" x = 20 "));
    }

    @Test
    void testEvalVariableNotFound() {
        Variable x = new Variable("x");


        assertThrows(IllegalArgumentException.class, () -> x.eval("y = 5"));
        assertThrows(IllegalArgumentException.class, () -> x.eval(""));
        assertThrows(IllegalArgumentException.class, () -> x.eval("z = 10; y = 20"));
    }

    @Test
    void testSimplify() {
        Variable var = new Variable("x");
        Expression simplified = var.simplify();


        assertSame(var, simplified);
    }

    @Test
    void testNotBinaryExpression() {
        Expression var = new Variable("x");
        assertFalse(var instanceof BinaryExpression);

        assertThrows(ClassCastException.class, () -> {
            BinaryExpression binExpr = (BinaryExpression) var;
        });
    }


}
