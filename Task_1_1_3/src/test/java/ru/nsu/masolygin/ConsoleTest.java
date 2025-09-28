package ru.nsu.masolygin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleTest {
    private Console console;
    private ByteArrayOutputStream outputStream;
    private ByteArrayOutputStream errorStream;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        console = new Console();
        outputStream = new ByteArrayOutputStream();
        errorStream = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(errorStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testPrintError() {
        String message = "Test error message";
        console.printError(message);

        String expected = "Error: " + message + System.lineSeparator();
        assertEquals(expected, errorStream.toString());
    }

    @Test
    void testPrintInfo() {
        String message = "Test info message";
        console.printInfo(message);

        String expected = message + System.lineSeparator();
        assertEquals(expected, outputStream.toString());
    }

    @Test
    void testPrintWelcome() {
        console.printWelcome();

        String output = outputStream.toString();
        assertTrue(output.contains("Mathematical Expression Calculator"));
        assertTrue(output.contains("Enter 'exit' to quit"));
    }

    @Test
    void testPrintGoodbye() {
        console.printGoodbye();

        String expected = "Calculator closed." + System.lineSeparator();
        assertEquals(expected, outputStream.toString());
    }

    @Test
    void testPrintExpression() {
        String label = "Test expression";
        console.printExpression(label);

        String expected = label + ": ";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    void testPrintNewLine() {
        console.printNewLine();

        String expected = System.lineSeparator();
        assertEquals(expected, outputStream.toString());
    }

    @Test
    void testIsExitCommandTrue() {
        assertTrue(console.isExitCommand("exit"));
        assertTrue(console.isExitCommand("EXIT"));
        assertTrue(console.isExitCommand("ExIt"));
        assertTrue(console.isExitCommand("  exit  "));
    }

    @Test
    void testIsExitCommandFalse() {
        assertFalse(console.isExitCommand("quit"));
        assertFalse(console.isExitCommand("stop"));
        assertFalse(console.isExitCommand("x+y"));
        assertFalse(console.isExitCommand(""));
    }

    @Test
    void testHasErrorsValidExpressions() {
        assertFalse(console.hasErrors("(x+y)"));
        assertFalse(console.hasErrors("((a*b)+c)"));
        assertFalse(console.hasErrors("(alpha + beta)"));
        assertFalse(console.hasErrors("(1 + 2)"));
        assertFalse(console.hasErrors("(x - y * z / w)"));
        assertFalse(console.hasErrors("((x))"));
    }

    @Test
    void testHasErrorsInvalidCharacters() {
        assertTrue(console.hasErrors("(x # y)"));
        assertTrue(console.hasErrors("(x @ y)"));
        assertTrue(console.hasErrors("(x & y)"));
        assertTrue(console.hasErrors("(x | y)"));
    }

    @Test
    void testHasErrorsUnmatchedParentheses() {
        assertTrue(console.hasErrors("(x + y"));
        assertTrue(console.hasErrors("x + y)"));
        assertTrue(console.hasErrors("((x + y)"));
        assertTrue(console.hasErrors("(x + y))"));
        assertTrue(console.hasErrors(")x + y("));
    }

    @Test
    void testHasErrorsEmptyParentheses() {
        assertTrue(console.hasErrors("()"));
        assertTrue(console.hasErrors("(x + ())"));
        assertTrue(console.hasErrors("(() + x)"));
    }

    @Test
    void testHasErrorsBalancedParentheses() {
        assertFalse(console.hasErrors("((((x))))"));
        assertFalse(console.hasErrors("((x+y)*(a+b))"));
        assertFalse(console.hasErrors("(((x+y)+z)+w)"));
    }

    @Test
    void testHasErrorsComplexValidExpressions() {
        assertFalse(console.hasErrors("((a*b)+((c-d)/(e+f)))"));
        assertFalse(console.hasErrors("(x + y - z * w / v)"));
        assertFalse(console.hasErrors("(longVariableName + anotherVariable)"));
    }

    @Test
    void testHasErrorsWhitespaceHandling() {
        assertFalse(console.hasErrors("  (x + y)  "));
        assertFalse(console.hasErrors("( x + y )"));
        assertFalse(console.hasErrors("(x+y)"));
        assertFalse(console.hasErrors("(  x  +  y  )"));
    }

    @Test
    void testHasErrorsSpecialCases() {
        assertTrue(console.hasErrors("((x+y)"));
        assertTrue(console.hasErrors("(x+y))"));
        assertTrue(console.hasErrors(")(x+y)("));
    }
}
