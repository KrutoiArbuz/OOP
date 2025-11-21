package ru.nsu.masolygin.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class CompositeTest {

    @Test
    void testCompositeWithMultipleElements() {
        Composite composite = new Composite(
            new Text("Plain "),
            new Text.Bold("bold"),
            new Text(" and "),
            new Text.Italic("italic")
        );
        assertEquals("Plain **bold** and *italic*", composite.toMarkdown());
    }

    @Test
    void testCompositeWithSingleElement() {
        Composite composite = new Composite(new Text("Single"));
        assertEquals("Single", composite.toMarkdown());
    }

    @Test
    void testCompositeEmpty() {
        Composite composite = new Composite();
        assertEquals("", composite.toMarkdown());
    }

    @Test
    void testCompositeWithList() {
        Composite composite = new Composite(Arrays.asList(
            new Text("First "),
            new Text.Bold("Second"),
            new Text(" Third")
        ));
        assertEquals("First **Second** Third", composite.toMarkdown());
    }

    @Test
    void testCompositeInHeading() {
        Composite composite = new Composite(
            new Text("Normal "),
            new Text.Bold("bold"),
            new Text(" text")
        );
        Heading heading = new Heading(1, composite);
        assertEquals("# Normal **bold** text", heading.toMarkdown());
    }

    @Test
    void testCompositeInLink() {
        Composite composite = new Composite(
            new Text.Code("GitHub"),
            new Text(" - "),
            new Text.Italic("platform")
        );
        Link link = new Link(composite, "https://github.com");
        assertEquals("[`GitHub` - *platform*](https://github.com)", link.toMarkdown());
    }

    @Test
    void testCompositeInQuote() {
        Composite composite = new Composite(
            new Text("This is "),
            new Text.Bold("important")
        );
        Quote quote = new Quote(composite);
        assertEquals("> This is **important**", quote.toMarkdown());
    }

    @Test
    void testNestedComposite() {
        Composite inner = new Composite(
            new Text.Bold("bold"),
            new Text(" and "),
            new Text.Italic("italic")
        );
        Composite outer = new Composite(
            new Text("Start "),
            inner,
            new Text(" end")
        );
        assertEquals("Start **bold** and *italic* end", outer.toMarkdown());
    }

    @Test
    void testEquals() {
        Composite comp1 = new Composite(
            new Text("Test"),
            new Text.Bold("Bold")
        );
        Composite comp2 = new Composite(
            new Text("Test"),
            new Text.Bold("Bold")
        );
        assertEquals(comp1, comp2);
    }

    @Test
    void testEqualsSameObject() {
        Composite composite = new Composite(new Text("Test"));
        assertEquals(composite, composite);
    }

    @Test
    void testEqualsDifferentElements() {
        Composite comp1 = new Composite(new Text("Test1"));
        Composite comp2 = new Composite(new Text("Test2"));
        assertNotEquals(comp1, comp2);
    }

    @Test
    void testEqualsDifferentOrder() {
        Composite comp1 = new Composite(
            new Text("A"),
            new Text("B")
        );
        Composite comp2 = new Composite(
            new Text("B"),
            new Text("A")
        );
        assertNotEquals(comp1, comp2);
    }

    @Test
    void testEqualsNull() {
        Composite composite = new Composite(new Text("Test"));
        assertNotEquals(composite, null);
    }

    @Test
    void testEqualsDifferentClass() {
        Composite composite = new Composite(new Text("Test"));
        assertNotEquals(composite, "Test");
    }

    @Test
    void testHashCode() {
        Composite comp1 = new Composite(new Text("Test"));
        Composite comp2 = new Composite(new Text("Test"));
        assertEquals(comp1.hashCode(), comp2.hashCode());
    }

    @Test
    void testToString() {
        Composite composite = new Composite(
            new Text("Test "),
            new Text.Bold("Bold")
        );
        assertEquals("Test **Bold**", composite.toString());
    }
}

