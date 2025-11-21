package ru.nsu.masolygin.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class QuoteTest {

    @Test
    void testSimpleQuote() {
        Quote quote = new Quote("This is a quote");
        assertEquals("> This is a quote", quote.toMarkdown());
    }

    @Test
    void testMultilineQuote() {
        Quote quote = new Quote("Line 1\nLine 2\nLine 3");
        assertEquals("> Line 1\n> Line 2\n> Line 3", quote.toMarkdown());
    }

    @Test
    void testQuoteWithElement() {
        Quote quote = new Quote(new Text.Italic("Italic quote"));
        assertEquals("> *Italic quote*", quote.toMarkdown());
    }

    @Test
    void testQuoteWithBoldElement() {
        Quote quote = new Quote(new Text.Bold("Bold quote"));
        assertEquals("> **Bold quote**", quote.toMarkdown());
    }

    @Test
    void testQuoteWithNullString() {
        Quote quote = new Quote((String) null);
        assertEquals("> ", quote.toMarkdown());
    }

    @Test
    void testQuoteWithNullElement() {
        Quote quote = new Quote((Element) null);
        assertEquals("> ", quote.toMarkdown());
    }

    @Test
    void testQuoteWithEmptyString() {
        Quote quote = new Quote("");
        assertEquals("> ", quote.toMarkdown());
    }

    @Test
    void testEquals() {
        Quote quote1 = new Quote("Test");
        Quote quote2 = new Quote("Test");
        assertEquals(quote1, quote2);
    }

    @Test
    void testEqualsSameObject() {
        Quote quote = new Quote("Test");
        assertEquals(quote, quote);
    }

    @Test
    void testEqualsDifferentContent() {
        Quote quote1 = new Quote("Test1");
        Quote quote2 = new Quote("Test2");
        assertNotEquals(quote1, quote2);
    }

    @Test
    void testEqualsNull() {
        Quote quote = new Quote("Test");
        assertNotEquals(quote, null);
    }

    @Test
    void testEqualsDifferentClass() {
        Quote quote = new Quote("Test");
        assertNotEquals(quote, "Test");
    }

    @Test
    void testHashCode() {
        Quote quote1 = new Quote("Test");
        Quote quote2 = new Quote("Test");
        assertEquals(quote1.hashCode(), quote2.hashCode());
    }

    @Test
    void testToString() {
        Quote quote = new Quote("Test");
        assertEquals("> Test", quote.toString());
    }
}

