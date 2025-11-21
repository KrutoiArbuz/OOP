package ru.nsu.masolygin.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class TextTest {

    @Test
    void testPlainText() {
        Text text = new Text("Hello World");
        assertEquals("Hello World", text.toMarkdown());
    }

    @Test
    void testPlainTextWithNull() {
        Text text = new Text(null);
        assertEquals("", text.toMarkdown());
    }

    @Test
    void testBoldText() {
        Text.Bold bold = new Text.Bold("Bold Text");
        assertEquals("**Bold Text**", bold.toMarkdown());
    }

    @Test
    void testBoldTextWithNull() {
        Text.Bold bold = new Text.Bold(null);
        assertEquals("****", bold.toMarkdown());
    }

    @Test
    void testItalicText() {
        Text.Italic italic = new Text.Italic("Italic Text");
        assertEquals("*Italic Text*", italic.toMarkdown());
    }

    @Test
    void testItalicTextWithNull() {
        Text.Italic italic = new Text.Italic(null);
        assertEquals("**", italic.toMarkdown());
    }

    @Test
    void testStrikethroughText() {
        Text.Strikethrough strikethrough = new Text.Strikethrough("Strikethrough Text");
        assertEquals("~~Strikethrough Text~~", strikethrough.toMarkdown());
    }

    @Test
    void testStrikethroughTextWithNull() {
        Text.Strikethrough strikethrough = new Text.Strikethrough(null);
        assertEquals("~~~~", strikethrough.toMarkdown());
    }

    @Test
    void testInlineCode() {
        Text.Code code = new Text.Code("int x = 5;");
        assertEquals("`int x = 5;`", code.toMarkdown());
    }

    @Test
    void testInlineCodeWithNull() {
        Text.Code code = new Text.Code(null);
        assertEquals("``", code.toMarkdown());
    }

    @Test
    void testToString() {
        Text text = new Text("Test");
        assertEquals("Test", text.toString());
    }

    @Test
    void testEqualsReflexive() {
        Text text = new Text("Hello");
        assertEquals(text, text);
    }

    @Test
    void testEqualsSymmetric() {
        Text text1 = new Text("Hello");
        Text text2 = new Text("Hello");
        assertEquals(text1, text2);
        assertEquals(text2, text1);
    }

    @Test
    void testEqualsDifferentContent() {
        Text text1 = new Text("Hello");
        Text text2 = new Text("World");
        assertNotEquals(text1, text2);
    }

    @Test
    void testEqualsNull() {
        Text text = new Text("Hello");
        assertNotEquals(text, null);
    }

    @Test
    void testEqualsDifferentClass() {
        Text text = new Text("Hello");
        assertNotEquals(text, "Hello");
    }

    @Test
    void testEqualsDifferentSubclass() {
        Text text = new Text("Hello");
        Text.Bold bold = new Text.Bold("Hello");
        assertNotEquals(text, bold);
    }

    @Test
    void testBoldEquals() {
        Text.Bold bold1 = new Text.Bold("Bold");
        Text.Bold bold2 = new Text.Bold("Bold");
        assertEquals(bold1, bold2);
    }

    @Test
    void testItalicEquals() {
        Text.Italic italic1 = new Text.Italic("Italic");
        Text.Italic italic2 = new Text.Italic("Italic");
        assertEquals(italic1, italic2);
    }

    @Test
    void testHashCodeConsistency() {
        Text text1 = new Text("Test");
        Text text2 = new Text("Test");
        assertEquals(text1.hashCode(), text2.hashCode());
    }

    @Test
    void testHashCodeDifferent() {
        Text text1 = new Text("Test1");
        Text text2 = new Text("Test2");
        assertNotEquals(text1.hashCode(), text2.hashCode());
    }

    @Test
    void testEmptyText() {
        Text text = new Text("");
        assertEquals("", text.toMarkdown());
    }

    @Test
    void testTextWithSpecialCharacters() {
        Text text = new Text("Hello & <World>");
        assertEquals("Hello & <World>", text.toMarkdown());
    }
}
