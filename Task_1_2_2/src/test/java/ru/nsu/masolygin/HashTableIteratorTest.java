package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HashTableIteratorTest {
    private HashTable<String, Integer> table;

    @BeforeEach
    void setUp() {
        table = new HashTable<>();
    }

    @Test
    void testIteratorOnEmptyTable() {
        Iterator<HashTableEntry<String, Integer>> iterator = table.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIteratorHasNextOnSingleElement() {
        table.put("one", 1);
        Iterator<HashTableEntry<String, Integer>> iterator = table.iterator();
        assertTrue(iterator.hasNext());
    }

    @Test
    void testIteratorNextOnSingleElement() {
        table.put("one", 1);
        Iterator<HashTableEntry<String, Integer>> iterator = table.iterator();

        HashTableEntry<String, Integer> entry = iterator.next();
        assertNotNull(entry);
        assertEquals("one", entry.getKey());
        assertEquals(1, entry.getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIteratorOnMultipleElements() {
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);

        Iterator<HashTableEntry<String, Integer>> iterator = table.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            HashTableEntry<String, Integer> entry = iterator.next();
            assertNotNull(entry);
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    void testIteratorNoSuchElementException() {
        table.put("one", 1);
        Iterator<HashTableEntry<String, Integer>> iterator = table.iterator();

        iterator.next();
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    void testIteratorNoSuchElementOnEmpty() {
        Iterator<HashTableEntry<String, Integer>> iterator = table.iterator();
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    void testIteratorConcurrentModificationOnPut() {
        table.put("one", 1);
        table.put("two", 2);

        Iterator<HashTableEntry<String, Integer>> iterator = table.iterator();
        iterator.next();

        table.put("three", 3);

        assertThrows(ConcurrentModificationException.class, () -> iterator.next());
    }

    @Test
    void testIteratorConcurrentModificationOnRemove() {
        table.put("one", 1);
        table.put("two", 2);

        Iterator<HashTableEntry<String, Integer>> iterator = table.iterator();
        iterator.next();

        table.remove("one");

        assertThrows(ConcurrentModificationException.class, () -> iterator.next());
    }

    @Test
    void testIteratorConcurrentModificationOnClear() {
        table.put("one", 1);
        table.put("two", 2);

        Iterator<HashTableEntry<String, Integer>> iterator = table.iterator();
        iterator.next();

        table.clear();

        assertThrows(ConcurrentModificationException.class, () -> iterator.next());
    }

    @Test
    void testIteratorNoConcurrentModificationOnUpdate() {
        table.put("one", 1);
        table.put("two", 2);

        Iterator<HashTableEntry<String, Integer>> iterator = table.iterator();
        iterator.next();

        table.update("one", 100);

        if (iterator.hasNext()) {
            iterator.next();
        }
    }

    @Test
    void testIteratorWithForEachLoop() {
        table.put("apple", 10);
        table.put("banana", 20);
        table.put("cherry", 30);

        int sum = 0;
        int count = 0;
        for (HashTableEntry<String, Integer> entry : table) {
            sum += entry.getValue();
            count++;
        }

        assertEquals(3, count);
        assertEquals(60, sum);
    }

    @Test
    void testIteratorWithNullKey() {
        table.put(null, 42);
        table.put("key", 100);

        Iterator<HashTableEntry<String, Integer>> iterator = table.iterator();
        int count = 0;
        boolean foundNull = false;

        while (iterator.hasNext()) {
            HashTableEntry<String, Integer> entry = iterator.next();
            if (entry.getKey() == null) {
                foundNull = true;
                assertEquals(42, entry.getValue());
            }
            count++;
        }

        assertEquals(2, count);
        assertTrue(foundNull);
    }

    @Test
    void testIteratorWithNullValue() {
        table.put("key", null);

        Iterator<HashTableEntry<String, Integer>> iterator = table.iterator();
        assertTrue(iterator.hasNext());

        HashTableEntry<String, Integer> entry = iterator.next();
        assertEquals("key", entry.getKey());
        assertEquals(null, entry.getValue());
    }

    @Test
    void testMultipleIterators() {
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);

        Iterator<HashTableEntry<String, Integer>> iterator1 = table.iterator();
        Iterator<HashTableEntry<String, Integer>> iterator2 = table.iterator();

        assertTrue(iterator1.hasNext());
        assertTrue(iterator2.hasNext());

        iterator1.next();
        assertTrue(iterator2.hasNext());
    }

    @Test
    void testIteratorAfterResize() {
        for (int i = 0; i < 50; i++) {
            table.put("key" + i, i);
        }

        Iterator<HashTableEntry<String, Integer>> iterator = table.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }

        assertEquals(50, count);
    }

    @Test
    void testIteratorOrderIsConsistent() {
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);

        StringBuilder order1 = new StringBuilder();
        for (HashTableEntry<String, Integer> entry : table) {
            order1.append(entry.getKey()).append(",");
        }

        StringBuilder order2 = new StringBuilder();
        for (HashTableEntry<String, Integer> entry : table) {
            order2.append(entry.getKey()).append(",");
        }

        assertEquals(order1.toString(), order2.toString());
    }

    @Test
    void testIteratorWithCollisions() {
        HashTable<Integer, String> smallTable = new HashTable<>(2, 0.75f);
        smallTable.put(1, "one");
        smallTable.put(2, "two");
        smallTable.put(3, "three");
        smallTable.put(4, "four");

        Iterator<HashTableEntry<Integer, String>> iterator = smallTable.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            HashTableEntry<Integer, String> entry = iterator.next();
            assertNotNull(entry);
            count++;
        }

        assertEquals(4, count);
    }

    @Test
    void testIteratorDoesNotSkipElements() {
        for (int i = 0; i < 20; i++) {
            table.put("key" + i, i);
        }

        java.util.Set<String> visitedKeys = new java.util.HashSet<>();
        for (HashTableEntry<String, Integer> entry : table) {
            visitedKeys.add(entry.getKey());
        }

        assertEquals(20, visitedKeys.size());
        for (int i = 0; i < 20; i++) {
            assertTrue(visitedKeys.contains("key" + i));
        }
    }

    @Test
    void testIteratorAfterClearAndRepopulate() {
        table.put("one", 1);
        table.put("two", 2);
        table.clear();
        table.put("three", 3);

        Iterator<HashTableEntry<String, Integer>> iterator = table.iterator();
        assertTrue(iterator.hasNext());

        HashTableEntry<String, Integer> entry = iterator.next();
        assertEquals("three", entry.getKey());
        assertEquals(3, entry.getValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    void testIteratorWithLargeDataset() {
        for (int i = 0; i < 1000; i++) {
            table.put("key" + i, i);
        }

        Iterator<HashTableEntry<String, Integer>> iterator = table.iterator();
        int count = 0;
        int sum = 0;

        while (iterator.hasNext()) {
            HashTableEntry<String, Integer> entry = iterator.next();
            sum += entry.getValue();
            count++;
        }

        assertEquals(1000, count);
        assertEquals(999 * 1000 / 2, sum);
    }

    @Test
    void testConcurrentModificationDetectionImmediately() {
        table.put("one", 1);
        Iterator<HashTableEntry<String, Integer>> iterator = table.iterator();

        table.put("two", 2);

        assertThrows(ConcurrentModificationException.class, () -> iterator.next());
    }
}
