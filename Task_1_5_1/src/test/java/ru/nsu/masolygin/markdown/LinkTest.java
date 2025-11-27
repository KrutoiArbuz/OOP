package ru.nsu.masolygin.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class LinkTest {

    @Test
    void testSimpleLink() {
        Link link = new Link("GitHub", "https://github.com");
        assertEquals("[GitHub](https://github.com)", link.toMarkdown());
    }

    @Test
    void testLinkWithElement() {
        Link link = new Link(new Text.Bold("Bold Link"), "https://example.com");
        assertEquals("[**Bold Link**](https://example.com)", link.toMarkdown());
    }

    @Test
    void testLinkWithItalicElement() {
        Link link = new Link(new Text.Italic("Italic Link"), "https://example.com");
        assertEquals("[*Italic Link*](https://example.com)", link.toMarkdown());
    }

    @Test
    void testLinkWithNullText() {
        Link link = new Link((String) null, "https://example.com");
        assertEquals("[](https://example.com)", link.toMarkdown());
    }

    @Test
    void testLinkWithNullUrl() {
        Link link = new Link("Text", null);
        assertEquals("[Text]()", link.toMarkdown());
    }

    @Test
    void testLinkWithNullElement() {
        Link link = new Link((Element) null, "https://example.com");
        assertEquals("[](https://example.com)", link.toMarkdown());
    }

    @Test
    void testLinkWithEmptyText() {
        Link link = new Link("", "https://example.com");
        assertEquals("[](https://example.com)", link.toMarkdown());
    }

    @Test
    void testLinkWithEmptyUrl() {
        Link link = new Link("Text", "");
        assertEquals("[Text]()", link.toMarkdown());
    }

    @Test
    void testEquals() {
        Link link1 = new Link("GitHub", "https://github.com");
        Link link2 = new Link("GitHub", "https://github.com");
        assertEquals(link1, link2);
    }

    @Test
    void testEqualsSameObject() {
        Link link = new Link("GitHub", "https://github.com");
        assertEquals(link, link);
    }

    @Test
    void testEqualsDifferentText() {
        Link link1 = new Link("GitHub", "https://github.com");
        Link link2 = new Link("GitLab", "https://github.com");
        assertNotEquals(link1, link2);
    }

    @Test
    void testEqualsDifferentUrl() {
        Link link1 = new Link("GitHub", "https://github.com");
        Link link2 = new Link("GitHub", "https://gitlab.com");
        assertNotEquals(link1, link2);
    }

    @Test
    void testEqualsNull() {
        Link link = new Link("GitHub", "https://github.com");
        assertNotEquals(link, null);
    }

    @Test
    void testEqualsDifferentClass() {
        Link link = new Link("GitHub", "https://github.com");
        assertNotEquals(link, "GitHub");
    }

    @Test
    void testHashCode() {
        Link link1 = new Link("GitHub", "https://github.com");
        Link link2 = new Link("GitHub", "https://github.com");
        assertEquals(link1.hashCode(), link2.hashCode());
    }

    @Test
    void testToString() {
        Link link = new Link("GitHub", "https://github.com");
        assertEquals("[GitHub](https://github.com)", link.toString());
    }
}

