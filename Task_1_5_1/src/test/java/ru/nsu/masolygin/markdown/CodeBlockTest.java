package ru.nsu.masolygin.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class CodeBlockTest {

    @Test
    void testCodeBlockWithLanguage() {
        CodeBlock codeBlock = new CodeBlock("int x = 5;", "java");
        assertEquals("```java\nint x = 5;\n```", codeBlock.toMarkdown());
    }

    @Test
    void testCodeBlockWithoutLanguage() {
        CodeBlock codeBlock = new CodeBlock("int x = 5;");
        assertEquals("```\nint x = 5;\n```", codeBlock.toMarkdown());
    }

    @Test
    void testCodeBlockWithEmptyLanguage() {
        CodeBlock codeBlock = new CodeBlock("int x = 5;", "");
        assertEquals("```\nint x = 5;\n```", codeBlock.toMarkdown());
    }

    @Test
    void testCodeBlockWithNullCode() {
        CodeBlock codeBlock = new CodeBlock(null, "java");
        assertEquals("```java\n\n```", codeBlock.toMarkdown());
    }

    @Test
    void testCodeBlockWithNullLanguage() {
        CodeBlock codeBlock = new CodeBlock("int x = 5;", null);
        assertEquals("```\nint x = 5;\n```", codeBlock.toMarkdown());
    }

    @Test
    void testCodeBlockMultiline() {
        String code = "public static void main(String[] args) {\n" +
                      "    System.out.println(\"Hello\");\n" +
                      "}";
        CodeBlock codeBlock = new CodeBlock(code, "java");
        assertEquals("```java\n" + code + "\n```", codeBlock.toMarkdown());
    }

    @Test
    void testCodeBlockEmpty() {
        CodeBlock codeBlock = new CodeBlock("");
        assertEquals("```\n\n```", codeBlock.toMarkdown());
    }

    @Test
    void testEquals() {
        CodeBlock block1 = new CodeBlock("code", "java");
        CodeBlock block2 = new CodeBlock("code", "java");
        assertEquals(block1, block2);
    }

    @Test
    void testEqualsSameObject() {
        CodeBlock block = new CodeBlock("code", "java");
        assertEquals(block, block);
    }

    @Test
    void testEqualsDifferentCode() {
        CodeBlock block1 = new CodeBlock("code1", "java");
        CodeBlock block2 = new CodeBlock("code2", "java");
        assertNotEquals(block1, block2);
    }

    @Test
    void testEqualsDifferentLanguage() {
        CodeBlock block1 = new CodeBlock("code", "java");
        CodeBlock block2 = new CodeBlock("code", "python");
        assertNotEquals(block1, block2);
    }

    @Test
    void testEqualsNull() {
        CodeBlock block = new CodeBlock("code", "java");
        assertNotEquals(block, null);
    }

    @Test
    void testEqualsDifferentClass() {
        CodeBlock block = new CodeBlock("code", "java");
        assertNotEquals(block, "code");
    }

    @Test
    void testHashCode() {
        CodeBlock block1 = new CodeBlock("code", "java");
        CodeBlock block2 = new CodeBlock("code", "java");
        assertEquals(block1.hashCode(), block2.hashCode());
    }

    @Test
    void testToString() {
        CodeBlock block = new CodeBlock("code", "java");
        assertEquals("```java\ncode\n```", block.toString());
    }
}

