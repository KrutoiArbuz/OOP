package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HashTableEntryTest {
    private HashTableEntry<String, Integer> entry;

    @BeforeEach
    void setUp() {
        entry = new HashTableEntry<>("key", 42, null);
    }

    @Test
    void testConstructorWithoutNext() {
        HashTableEntry<String, Integer> newEntry = new HashTableEntry<>("test", 100, null);
        assertEquals("test", newEntry.getKey());
        assertEquals(100, newEntry.getValue());
        assertNull(newEntry.getNext());
    }

    @Test
    void testConstructorWithNext() {
        HashTableEntry<String, Integer> nextEntry = new HashTableEntry<>("next", 200, null);
        HashTableEntry<String, Integer> newEntry = new HashTableEntry<>("test", 100, nextEntry);

        assertEquals("test", newEntry.getKey());
        assertEquals(100, newEntry.getValue());
        assertNotNull(newEntry.getNext());
        assertEquals(nextEntry, newEntry.getNext());
    }

    @Test
    void testGetKey() {
        assertEquals("key", entry.getKey());
    }

    @Test
    void testGetValue() {
        assertEquals(42, entry.getValue());
    }

    @Test
    void testSetValue() {
        entry.setValue(100);
        assertEquals(100, entry.getValue());
    }

    @Test
    void testSetValueToNull() {
        entry.setValue(null);
        assertNull(entry.getValue());
    }

    @Test
    void testGetNext() {
        assertNull(entry.getNext());
    }

    @Test
    void testSetNext() {
        HashTableEntry<String, Integer> nextEntry = new HashTableEntry<>("next", 200, null);
        entry.setNext(nextEntry);
        assertEquals(nextEntry, entry.getNext());
    }

    @Test
    void testSetNextToNull() {
        HashTableEntry<String, Integer> nextEntry = new HashTableEntry<>("next", 200, null);
        entry.setNext(nextEntry);
        entry.setNext(null);
        assertNull(entry.getNext());
    }

    @Test
    void testChainedEntries() {
        HashTableEntry<String, Integer> entry3 = new HashTableEntry<>("key3", 3, null);
        HashTableEntry<String, Integer> entry2 = new HashTableEntry<>("key2", 2, entry3);
        HashTableEntry<String, Integer> entry1 = new HashTableEntry<>("key1", 1, entry2);

        assertEquals("key1", entry1.getKey());
        assertEquals(1, entry1.getValue());
        assertEquals(entry2, entry1.getNext());

        assertEquals("key2", entry2.getKey());
        assertEquals(2, entry2.getValue());
        assertEquals(entry3, entry2.getNext());

        assertEquals("key3", entry3.getKey());
        assertEquals(3, entry3.getValue());
        assertNull(entry3.getNext());
    }

    @Test
    void testEntryWithNullKey() {
        HashTableEntry<String, Integer> nullKeyEntry = new HashTableEntry<>(null, 42, null);
        assertNull(nullKeyEntry.getKey());
        assertEquals(42, nullKeyEntry.getValue());
    }

    @Test
    void testEntryWithNullValue() {
        HashTableEntry<String, Integer> nullValueEntry = new HashTableEntry<>("key", null, null);
        assertEquals("key", nullValueEntry.getKey());
        assertNull(nullValueEntry.getValue());
    }

    @Test
    void testEntryWithNullKeyAndValue() {
        HashTableEntry<String, Integer> nullEntry = new HashTableEntry<>(null, null, null);
        assertNull(nullEntry.getKey());
        assertNull(nullEntry.getValue());
        assertNull(nullEntry.getNext());
    }

    @Test
    void testModifyValueInChain() {
        HashTableEntry<String, Integer> entry2 = new HashTableEntry<>("key2", 2, null);
        HashTableEntry<String, Integer> entry1 = new HashTableEntry<>("key1", 1, entry2);

        entry1.setValue(10);
        entry2.setValue(20);

        assertEquals(10, entry1.getValue());
        assertEquals(20, entry2.getValue());
    }

    @Test
    void testDifferentTypes() {
        HashTableEntry<Integer, String> intEntry = new HashTableEntry<>(1, "one", null);
        assertEquals(1, intEntry.getKey());
        assertEquals("one", intEntry.getValue());

        HashTableEntry<Double, Boolean> doubleEntry = new HashTableEntry<>(3.14, true, null);
        assertEquals(3.14, doubleEntry.getKey());
        assertEquals(true, doubleEntry.getValue());
    }

    @Test
    void testComplexTypeAsValue() {
        HashTable<String, String> innerTable = new HashTable<>();
        innerTable.put("inner", "value");

        HashTableEntry<String, HashTable<String, String>> complexEntry =
            new HashTableEntry<>("complex", innerTable, null);

        assertEquals("complex", complexEntry.getKey());
        assertEquals(innerTable, complexEntry.getValue());
        assertEquals("value", complexEntry.getValue().get("inner"));
    }

    @Test
    void testLongChain() {
        HashTableEntry<Integer, String> tail = null;

        for (int i = 9; i >= 0; i--) {
            tail = new HashTableEntry<>(i, "value" + i, tail);
        }

        HashTableEntry<Integer, String> current = tail;
        int count = 0;
        while (current != null) {
            assertEquals(count, current.getKey());
            assertEquals("value" + count, current.getValue());
            current = current.getNext();
            count++;
        }
        assertEquals(10, count);
    }

    @Test
    void testReplaceNextInChain() {
        HashTableEntry<String, Integer> entry3 = new HashTableEntry<>("key3", 3, null);
        HashTableEntry<String, Integer> entry2 = new HashTableEntry<>("key2", 2, entry3);
        HashTableEntry<String, Integer> entry1 = new HashTableEntry<>("key1", 1, entry2);

        HashTableEntry<String, Integer> newEntry = new HashTableEntry<>("newKey", 99, null);
        entry1.setNext(newEntry);

        assertEquals(newEntry, entry1.getNext());
        assertEquals("newKey", entry1.getNext().getKey());
        assertEquals(99, entry1.getNext().getValue());
    }
}

