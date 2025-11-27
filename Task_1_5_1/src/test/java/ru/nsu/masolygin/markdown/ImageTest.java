package ru.nsu.masolygin.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class ImageTest {

    @Test
    void testSimpleImage() {
        Image image = new Image("Logo", "https://example.com/logo.png");
        assertEquals("![Logo](https://example.com/logo.png)", image.toMarkdown());
    }

    @Test
    void testImageWithNullAlt() {
        Image image = new Image(null, "https://example.com/logo.png");
        assertEquals("![](https://example.com/logo.png)", image.toMarkdown());
    }

    @Test
    void testImageWithNullUrl() {
        Image image = new Image("Logo", null);
        assertEquals("![Logo]()", image.toMarkdown());
    }

    @Test
    void testImageWithEmptyAlt() {
        Image image = new Image("", "https://example.com/logo.png");
        assertEquals("![](https://example.com/logo.png)", image.toMarkdown());
    }

    @Test
    void testImageWithEmptyUrl() {
        Image image = new Image("Logo", "");
        assertEquals("![Logo]()", image.toMarkdown());
    }

    @Test
    void testEquals() {
        Image image1 = new Image("Logo", "https://example.com/logo.png");
        Image image2 = new Image("Logo", "https://example.com/logo.png");
        assertEquals(image1, image2);
    }

    @Test
    void testEqualsSameObject() {
        Image image = new Image("Logo", "https://example.com/logo.png");
        assertEquals(image, image);
    }

    @Test
    void testEqualsDifferentAlt() {
        Image image1 = new Image("Logo", "https://example.com/logo.png");
        Image image2 = new Image("Icon", "https://example.com/logo.png");
        assertNotEquals(image1, image2);
    }

    @Test
    void testEqualsDifferentUrl() {
        Image image1 = new Image("Logo", "https://example.com/logo.png");
        Image image2 = new Image("Logo", "https://example.com/icon.png");
        assertNotEquals(image1, image2);
    }

    @Test
    void testEqualsNull() {
        Image image = new Image("Logo", "https://example.com/logo.png");
        assertNotEquals(image, null);
    }

    @Test
    void testEqualsDifferentClass() {
        Image image = new Image("Logo", "https://example.com/logo.png");
        assertNotEquals(image, "Logo");
    }

    @Test
    void testHashCode() {
        Image image1 = new Image("Logo", "https://example.com/logo.png");
        Image image2 = new Image("Logo", "https://example.com/logo.png");
        assertEquals(image1.hashCode(), image2.hashCode());
    }

    @Test
    void testToString() {
        Image image = new Image("Logo", "https://example.com/logo.png");
        assertEquals("![Logo](https://example.com/logo.png)", image.toString());
    }
}

