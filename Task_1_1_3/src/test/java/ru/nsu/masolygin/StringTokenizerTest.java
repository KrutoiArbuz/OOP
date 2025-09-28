package ru.nsu.masolygin;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringTokenizerTest {

    @Test
    void testSimpleExpression() {
        StringTokenizer tokenizer = new StringTokenizer("(x+y)");
        List<Token> tokens = tokenizer.getTokens();

        assertEquals(5, tokens.size());
        assertEquals(Token.Type.LPAREN, tokens.get(0).getType());
        assertEquals(Token.Type.VARIABLE, tokens.get(1).getType());
        assertEquals("x", tokens.get(1).getValue());
        assertEquals(Token.Type.OPERATOR, tokens.get(2).getType());
        assertEquals("+", tokens.get(2).getValue());
        assertEquals(Token.Type.VARIABLE, tokens.get(3).getType());
        assertEquals("y", tokens.get(3).getValue());
        assertEquals(Token.Type.RPAREN, tokens.get(4).getType());
    }

    @Test
    void testNumberTokenization() {
        StringTokenizer tokenizer = new StringTokenizer("123");
        List<Token> tokens = tokenizer.getTokens();

        assertEquals(1, tokens.size());
        assertEquals(Token.Type.NUMBER, tokens.get(0).getType());
        assertEquals("123", tokens.get(0).getValue());
    }

    @Test
    void testNegativeNumberTokenization() {
        StringTokenizer tokenizer = new StringTokenizer("-5");
        List<Token> tokens = tokenizer.getTokens();

        assertEquals(1, tokens.size());
        assertEquals(Token.Type.NUMBER, tokens.get(0).getType());
        assertEquals("-5", tokens.get(0).getValue());
    }

    @Test
    void testMultiLetterVariable() {
        StringTokenizer tokenizer = new StringTokenizer("alpha");
        List<Token> tokens = tokenizer.getTokens();

        assertEquals(1, tokens.size());
        assertEquals(Token.Type.VARIABLE, tokens.get(0).getType());
        assertEquals("alpha", tokens.get(0).getValue());
    }

    @Test
    void testAllOperators() {
        StringTokenizer tokenizer = new StringTokenizer("+*/-^");
        List<Token> tokens = tokenizer.getTokens();

        assertEquals(5, tokens.size());
        assertEquals(Token.Type.OPERATOR, tokens.get(0).getType());
        assertEquals("+", tokens.get(0).getValue());
        assertEquals(Token.Type.OPERATOR, tokens.get(1).getType());
        assertEquals("*", tokens.get(1).getValue());
        assertEquals(Token.Type.OPERATOR, tokens.get(2).getType());
        assertEquals("/", tokens.get(2).getValue());
        assertEquals(Token.Type.OPERATOR, tokens.get(3).getType());
        assertEquals("-", tokens.get(3).getValue());
        assertEquals(Token.Type.OPERATOR, tokens.get(4).getType());
        assertEquals("^", tokens.get(4).getValue());
    }

    @Test
    void testWhitespaceHandling() {
        StringTokenizer tokenizer = new StringTokenizer("( x + y )");
        List<Token> tokens = tokenizer.getTokens();

        assertEquals(5, tokens.size());
        assertEquals(Token.Type.LPAREN, tokens.get(0).getType());
        assertEquals(Token.Type.VARIABLE, tokens.get(1).getType());
        assertEquals("x", tokens.get(1).getValue());
        assertEquals(Token.Type.OPERATOR, tokens.get(2).getType());
        assertEquals("+", tokens.get(2).getValue());
        assertEquals(Token.Type.VARIABLE, tokens.get(3).getType());
        assertEquals("y", tokens.get(3).getValue());
        assertEquals(Token.Type.RPAREN, tokens.get(4).getType());
    }

    @Test
    void testComplexExpression() {
        StringTokenizer tokenizer = new StringTokenizer("((a*b)+((c-d)/(1+2)))");
        List<Token> tokens = tokenizer.getTokens();

        assertTrue(tokens.size() > 10);
        assertEquals(Token.Type.LPAREN, tokens.get(0).getType());
        assertEquals(Token.Type.VARIABLE, tokens.get(2).getType());
        assertEquals("a", tokens.get(2).getValue());
    }

    @Test
    void testUnknownCharacterThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> new StringTokenizer("x # y"));
    }

    @Test
    void testEmptyString() {
        StringTokenizer tokenizer = new StringTokenizer("");
        List<Token> tokens = tokenizer.getTokens();

        assertEquals(0, tokens.size());
    }

    @Test
    void testOnlyWhitespace() {
        StringTokenizer tokenizer = new StringTokenizer("   ");
        List<Token> tokens = tokenizer.getTokens();

        assertEquals(0, tokens.size());
    }
}
