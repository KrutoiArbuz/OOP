package ru.nsu.masolygin.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class ListElementTest {

    @Test
    void testUnorderedList() {
        ListElement list = new ListElement.Builder()
            .unordered()
            .addItem("Item 1")
            .addItem("Item 2")
            .addItem("Item 3")
            .build();
        assertEquals("- Item 1\n- Item 2\n- Item 3", list.toMarkdown());
    }

    @Test
    void testOrderedList() {
        ListElement list = new ListElement.Builder()
            .ordered()
            .addItem("First")
            .addItem("Second")
            .addItem("Third")
            .build();
        assertEquals("1. First\n2. Second\n3. Third", list.toMarkdown());
    }

    @Test
    void testListWithElements() {
        ListElement list = new ListElement.Builder()
            .unordered()
            .addItem(new Text.Bold("Bold item"))
            .addItem(new Text.Italic("Italic item"))
            .build();
        assertEquals("- **Bold item**\n- *Italic item*", list.toMarkdown());
    }

    @Test
    void testListWithMixedTypes() {
        ListElement list = new ListElement.Builder()
            .unordered()
            .addItem("Plain text")
            .addItem(new Text.Bold("Bold"))
            .addItem(123)
            .build();
        assertEquals("- Plain text\n- **Bold**\n- 123", list.toMarkdown());
    }

    @Test
    void testEmptyList() {
        ListElement list = new ListElement.Builder()
            .unordered()
            .build();
        assertEquals("", list.toMarkdown());
    }

    @Test
    void testSingleItemList() {
        ListElement list = new ListElement.Builder()
            .unordered()
            .addItem("Only item")
            .build();
        assertEquals("- Only item", list.toMarkdown());
    }

    @Test
    void testOrderedListSingleItem() {
        ListElement list = new ListElement.Builder()
            .ordered()
            .addItem("First")
            .build();
        assertEquals("1. First", list.toMarkdown());
    }

    @Test
    void testDefaultIsUnordered() {
        ListElement list = new ListElement.Builder()
            .addItem("Item 1")
            .addItem("Item 2")
            .build();
        assertEquals("- Item 1\n- Item 2", list.toMarkdown());
    }

    @Test
    void testSwitchOrderedToUnordered() {
        ListElement list = new ListElement.Builder()
            .ordered()
            .unordered()
            .addItem("Item")
            .build();
        assertEquals("- Item", list.toMarkdown());
    }

    @Test
    void testEquals() {
        ListElement list1 = new ListElement.Builder()
            .unordered()
            .addItem("Item 1")
            .addItem("Item 2")
            .build();
        ListElement list2 = new ListElement.Builder()
            .unordered()
            .addItem("Item 1")
            .addItem("Item 2")
            .build();
        assertEquals(list1, list2);
    }

    @Test
    void testEqualsSameObject() {
        ListElement list = new ListElement.Builder()
            .unordered()
            .addItem("Item")
            .build();
        assertEquals(list, list);
    }

    @Test
    void testEqualsDifferentItems() {
        ListElement list1 = new ListElement.Builder()
            .unordered()
            .addItem("Item 1")
            .build();
        ListElement list2 = new ListElement.Builder()
            .unordered()
            .addItem("Item 2")
            .build();
        assertNotEquals(list1, list2);
    }

    @Test
    void testEqualsDifferentOrder() {
        ListElement list1 = new ListElement.Builder()
            .ordered()
            .addItem("Item")
            .build();
        ListElement list2 = new ListElement.Builder()
            .unordered()
            .addItem("Item")
            .build();
        assertNotEquals(list1, list2);
    }

    @Test
    void testEqualsNull() {
        ListElement list = new ListElement.Builder()
            .unordered()
            .addItem("Item")
            .build();
        assertNotEquals(list, null);
    }

    @Test
    void testEqualsDifferentClass() {
        ListElement list = new ListElement.Builder()
            .unordered()
            .addItem("Item")
            .build();
        assertNotEquals(list, "Item");
    }

    @Test
    void testHashCode() {
        ListElement list1 = new ListElement.Builder()
            .unordered()
            .addItem("Item")
            .build();
        ListElement list2 = new ListElement.Builder()
            .unordered()
            .addItem("Item")
            .build();
        assertEquals(list1.hashCode(), list2.hashCode());
    }

    @Test
    void testToString() {
        ListElement list = new ListElement.Builder()
            .unordered()
            .addItem("Item")
            .build();
        assertEquals("- Item", list.toString());
    }
}

