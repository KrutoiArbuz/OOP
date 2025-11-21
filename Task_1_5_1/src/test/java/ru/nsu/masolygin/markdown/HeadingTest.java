package ru.nsu.masolygin.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class HeadingTest {

    @Test
    void testHeadingLevel1() {
        Heading heading = new Heading(1, "Title");
        assertEquals("# Title", heading.toMarkdown());
    }

    @Test
    void testHeadingLevel2() {
        Heading heading = new Heading(2, "Subtitle");
        assertEquals("## Subtitle", heading.toMarkdown());
    }

    @Test
    void testHeadingLevel3() {
        Heading heading = new Heading(3, "Section");
        assertEquals("### Section", heading.toMarkdown());
    }

    @Test
    void testHeadingLevel6() {
        Heading heading = new Heading(6, "Small");
        assertEquals("###### Small", heading.toMarkdown());
    }

    @Test
    void testHeadingWithElement() {
        Heading heading = new Heading(1, new Text.Bold("Bold Title"));
        assertEquals("# **Bold Title**", heading.toMarkdown());
    }

    @Test
    void testHeadingWithItalic() {
        Heading heading = new Heading(2, new Text.Italic("Italic Title"));
        assertEquals("## *Italic Title*", heading.toMarkdown());
    }

    @Test
    void testHeadingWithNullString() {
        Heading heading = new Heading(1, (String) null);
        assertEquals("# ", heading.toMarkdown());
    }

    @Test
    void testHeadingWithNullElement() {
        Heading heading = new Heading(1, (Element) null);
        assertEquals("# ", heading.toMarkdown());
    }

    @Test
    void testHeadingLevelTooLow() {
        assertThrows(IllegalArgumentException.class, () -> new Heading(0, "Invalid"));
    }

    @Test
    void testHeadingLevelTooHigh() {
        assertThrows(IllegalArgumentException.class, () -> new Heading(7, "Invalid"));
    }

    @Test
    void testHeadingLevelNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Heading(-1, "Invalid"));
    }

    @Test
    void testEquals() {
        Heading heading1 = new Heading(1, "Title");
        Heading heading2 = new Heading(1, "Title");
        assertEquals(heading1, heading2);
    }

    @Test
    void testEqualsSameObject() {
        Heading heading = new Heading(1, "Title");
        assertEquals(heading, heading);
    }

    @Test
    void testEqualsDifferentLevel() {
        Heading heading1 = new Heading(1, "Title");
        Heading heading2 = new Heading(2, "Title");
        assertNotEquals(heading1, heading2);
    }

    @Test
    void testEqualsDifferentContent() {
        Heading heading1 = new Heading(1, "Title1");
        Heading heading2 = new Heading(1, "Title2");
        assertNotEquals(heading1, heading2);
    }

    @Test
    void testEqualsNull() {
        Heading heading = new Heading(1, "Title");
        assertNotEquals(heading, null);
    }

    @Test
    void testEqualsDifferentClass() {
        Heading heading = new Heading(1, "Title");
        assertNotEquals(heading, "Title");
    }

    @Test
    void testHashCode() {
        Heading heading1 = new Heading(1, "Title");
        Heading heading2 = new Heading(1, "Title");
        assertEquals(heading1.hashCode(), heading2.hashCode());
    }

    @Test
    void testHashCodeDifferent() {
        Heading heading1 = new Heading(1, "Title1");
        Heading heading2 = new Heading(1, "Title2");
        assertNotEquals(heading1.hashCode(), heading2.hashCode());
    }

    @Test
    void testToString() {
        Heading heading = new Heading(1, "Title");
        assertEquals("# Title", heading.toString());
    }

    @Test
    void testEmptyHeading() {
        Heading heading = new Heading(1, "");
        assertEquals("# ", heading.toMarkdown());
    }
}

