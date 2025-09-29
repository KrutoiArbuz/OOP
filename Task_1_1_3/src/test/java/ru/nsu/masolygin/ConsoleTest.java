package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    void testRequestDerivativeVariable() {
        String input = "x\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Console testConsole = new Console();

        String result = testConsole.requestDerivativeVariable();
        assertEquals("x", result);

        String output = outputStream.toString();
        assertTrue(output.contains("Calculate derivative"));
    }

    @Test
    void testRequestDerivativeVariableEmpty() {
        String input = "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Console testConsole = new Console();

        String result = testConsole.requestDerivativeVariable();
        assertEquals("", result);
    }

    @Test
    void testConfirmDerivativeEvaluationYes() {
        String input = "y\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Console testConsole = new Console();

        boolean result = testConsole.confirmDerivativeEvaluation();
        assertTrue(result);

        String output = outputStream.toString();
        assertTrue(output.contains("Evaluate derivative"));
    }

    @Test
    void testConfirmDerivativeEvaluationNo() {
        String input = "n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Console testConsole = new Console();

        boolean result = testConsole.confirmDerivativeEvaluation();
        assertFalse(result);
    }

    @Test
    void testConfirmDerivativeEvaluationCaseInsensitive() {
        String inputYes = "Y\n";
        System.setIn(new ByteArrayInputStream(inputYes.getBytes()));
        Console testConsole1 = new Console();
        assertTrue(testConsole1.confirmDerivativeEvaluation());

        String inputNo = "N\n";
        System.setIn(new ByteArrayInputStream(inputNo.getBytes()));
        Console testConsole2 = new Console();
        assertFalse(testConsole2.confirmDerivativeEvaluation());
    }

    @Test
    void testConfirmDerivativeEvaluationInvalidInput() {
        String input = "invalid\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Console testConsole = new Console();

        boolean result = testConsole.confirmDerivativeEvaluation();
        assertFalse(result);
    }

    @Test
    void testHasErrorsNestedParentheses() {
        assertFalse(console.hasErrors("((x+y))"));
        assertFalse(console.hasErrors("(((x)))"));
        assertTrue(console.hasErrors("(((x))"));
        assertTrue(console.hasErrors("((x)))"));
    }

    @Test
    void testHasErrorsComplexValid() {
        assertFalse(console.hasErrors("((a+b)*(c-d))"));
        assertFalse(console.hasErrors("(x+y-z)"));
        assertFalse(console.hasErrors("(a*b+c*d)"));
        assertFalse(console.hasErrors("((x+y)/(z-w))"));
    }

    @Test
    void testHasErrorsInvalidPatterns() {
        assertTrue(console.hasErrors("(x++)"));
        assertTrue(console.hasErrors("(x--)"));
        assertTrue(console.hasErrors("(x**)"));
        assertTrue(console.hasErrors("(x//)"));
        assertTrue(console.hasErrors("(x+())"));
        assertTrue(console.hasErrors("(()x)"));
    }

    @Test
    void testHasErrorsNumbers() {
        assertFalse(console.hasErrors("(123+456)"));
        assertFalse(console.hasErrors("(x+123)"));
        assertFalse(console.hasErrors("(0*x)"));
        assertTrue(console.hasErrors("(x@123)"));
    }

    @Test
    void testPrintExpressionMultipleCalls() {
        console.printExpression("First");
        console.printExpression("Second");

        String output = outputStream.toString();
        assertTrue(output.contains("First: "));
        assertTrue(output.contains("Second: "));
    }

    @Test
    void testPrintInfoMultipleMessages() {
        console.printInfo("Message 1");
        console.printInfo("Message 2");
        console.printInfo("Message 3");

        String output = outputStream.toString();
        assertTrue(output.contains("Message 1"));
        assertTrue(output.contains("Message 2"));
        assertTrue(output.contains("Message 3"));

        String[] lines = output.split(System.lineSeparator());
        assertEquals(3, lines.length);
    }

    @Test
    void testPrintErrorMultipleErrors() {
        console.printError("Error 1");
        console.printError("Error 2");

        String errorOutput = errorStream.toString();
        assertTrue(errorOutput.contains("Error: Error 1"));
        assertTrue(errorOutput.contains("Error: Error 2"));
    }

    @Test
    void testIsExitCommandEdgeCases() {
        assertTrue(console.isExitCommand("  EXIT  "));
        assertTrue(console.isExitCommand("\texit\t"));
        assertTrue(console.isExitCommand("Exit"));
        assertTrue(console.isExitCommand("eXiT"));

        assertFalse(console.isExitCommand("exits"));
        assertFalse(console.isExitCommand("exitt"));
        assertFalse(console.isExitCommand("quit"));
        assertFalse(console.isExitCommand("stop"));
        assertFalse(console.isExitCommand(""));
        assertFalse(console.isExitCommand("   "));
    }

    @Test
    void testHasErrorsVariableNames() {
        assertFalse(console.hasErrors("(alpha+beta)"));
        assertFalse(console.hasErrors("(longVariableName+x)"));
        assertFalse(console.hasErrors("(a+b+c+d)"));
        assertTrue(console.hasErrors("(x+y@z)"));
    }
}
