package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HashTableTest {
    private HashTable<String, Integer> table;

    @BeforeEach
    void setUp() {
        table = new HashTable<>();
    }

    @Test
    void testDefaultConstructor() {
        HashTable<String, String> newTable = new HashTable<>();
        assertEquals(0, newTable.size());
        assertTrue(newTable.isEmpty());
    }

    @Test
    void testConstructorWithCapacityAndLoadFactor() {
        HashTable<String, String> newTable = new HashTable<>(32, 0.8f);
        assertEquals(0, newTable.size());
        assertTrue(newTable.isEmpty());
    }

    @Test
    void testConstructorWithNegativeCapacity() {
        assertThrows(IllegalArgumentException.class,
                () -> new HashTable<String, String>(-1, 0.75f));
    }

    @Test
    void testConstructorWithInvalidLoadFactor() {
        assertThrows(IllegalArgumentException.class,
                () -> new HashTable<String, String>(16, 0.0f));
        assertThrows(IllegalArgumentException.class,
                () -> new HashTable<String, String>(16, -0.5f));
        assertThrows(IllegalArgumentException.class,
                () -> new HashTable<String, String>(16, Float.NaN));
    }

    @Test
    void testPutSingleElement() {
        table.put("one", 1);
        assertEquals(1, table.size());
        assertEquals(1, table.get("one"));
    }

    @Test
    void testPutMultipleElements() {
        table.put("apple", 10);
        table.put("banana", 20);
        table.put("cherry", 30);
        assertEquals(3, table.size());
        assertEquals(10, table.get("apple"));
        assertEquals(20, table.get("banana"));
        assertEquals(30, table.get("cherry"));
    }

    @Test
    void testPutUpdatesExistingKey() {
        table.put("one", 1);
        table.put("one", 100);
        assertEquals(100, table.get("one"));
        assertEquals(1, table.size());
    }

    @Test
    void testPutWithNullKey() {
        table.put(null, 42);
        assertEquals(42, table.get(null));
        assertTrue(table.containsKey(null));
    }

    @Test
    void testPutWithNullValue() {
        table.put("key", null);
        assertNull(table.get("key"));
        assertTrue(table.containsKey("key"));
    }

    @Test
    void testPutTriggersResize() {
        for (int i = 0; i < 100; i++) {
            table.put("key" + i, i);
        }
        assertEquals(100, table.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(i, table.get("key" + i));
        }
    }

    @Test
    void testUpdateExistingKey() {
        table.put("one", 1);
        assertTrue(table.update("one", 100));
        assertEquals(100, table.get("one"));
    }

    @Test
    void testUpdateNonExistentKey() {
        assertFalse(table.update("nonexistent", 42));
    }

    @Test
    void testUpdateWithNullKey() {
        table.put(null, 1);
        assertTrue(table.update(null, 2));
        assertEquals(2, table.get(null));
    }

    @Test
    void testGetExistingKey() {
        table.put("apple", 10);
        assertEquals(10, table.get("apple"));
    }

    @Test
    void testGetNonExistentKey() {
        assertNull(table.get("nonexistent"));
    }

    @Test
    void testGetWithNullKey() {
        table.put(null, 42);
        assertEquals(42, table.get(null));
    }

    @Test
    void testContainsKeyExists() {
        table.put("apple", 10);
        assertTrue(table.containsKey("apple"));
    }

    @Test
    void testContainsKeyNotExists() {
        assertFalse(table.containsKey("nonexistent"));
    }

    @Test
    void testContainsKeyWithNull() {
        table.put(null, 42);
        assertTrue(table.containsKey(null));
    }

    @Test
    void testRemoveExistingKey() {
        table.put("apple", 10);
        assertEquals(10, table.remove("apple"));
        assertNull(table.get("apple"));
        assertEquals(0, table.size());
    }

    @Test
    void testRemoveNonExistentKey() {
        assertNull(table.remove("nonexistent"));
        assertEquals(0, table.size());
    }

    @Test
    void testRemoveWithNullKey() {
        table.put(null, 42);
        assertEquals(42, table.remove(null));
        assertFalse(table.containsKey(null));
    }

    @Test
    void testRemoveFromChain() {
        HashTable<Integer, String> smallTable = new HashTable<>(4, 0.75f);
        smallTable.put(1, "one");
        smallTable.put(5, "five");
        smallTable.put(9, "nine");

        assertEquals(3, smallTable.size());
        assertEquals("five", smallTable.remove(5));
        assertEquals(2, smallTable.size());
        assertNull(smallTable.get(5));
        assertEquals("one", smallTable.get(1));
        assertEquals("nine", smallTable.get(9));
    }

    @Test
    void testSize() {
        assertEquals(0, table.size());
        table.put("one", 1);
        assertEquals(1, table.size());
        table.put("two", 2);
        assertEquals(2, table.size());
        table.remove("one");
        assertEquals(1, table.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(table.isEmpty());
        table.put("one", 1);
        assertFalse(table.isEmpty());
        table.remove("one");
        assertTrue(table.isEmpty());
    }

    @Test
    void testClear() {
        table.put("apple", 10);
        table.put("banana", 20);
        table.put("cherry", 30);
        table.clear();
        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
        assertNull(table.get("apple"));
    }

    @Test
    void testIterator() {
        table.put("apple", 10);
        table.put("banana", 20);
        table.put("cherry", 30);

        int count = 0;
        for (HashTableEntry<String, Integer> entry : table) {
            assertTrue(entry.getKey().equals("apple") ||
                    entry.getKey().equals("banana") ||
                    entry.getKey().equals("cherry"));
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    void testIteratorOnEmptyTable() {
        int count = 0;
        for (HashTableEntry<String, Integer> entry : table) {
            count++;
        }
        assertEquals(0, count);
    }

    @Test
    void testIteratorConcurrentModification() {
        table.put("apple", 10);
        table.put("banana", 20);

        assertThrows(ConcurrentModificationException.class, () -> {
            for (HashTableEntry<String, Integer> entry : table) {
                if (entry.getKey().equals("apple")) {
                    table.put("cherry", 30);
                }
            }
        });
    }

    @Test
    void testEquals() {
        HashTable<String, Integer> table2 = new HashTable<>();

        table.put("apple", 10);
        table.put("banana", 20);

        table2.put("apple", 10);
        table2.put("banana", 20);

        assertEquals(table, table2);
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(table, table);
    }

    @Test
    void testEqualsNull() {
        assertNotEquals(null, table);
    }

    @Test
    void testEqualsDifferentType() {
        assertNotEquals(table, "string");
    }

    @Test
    void testEqualsDifferentSize() {
        HashTable<String, Integer> table2 = new HashTable<>();
        table.put("apple", 10);
        table2.put("apple", 10);
        table2.put("banana", 20);

        assertNotEquals(table, table2);
    }

    @Test
    void testEqualsDifferentValues() {
        HashTable<String, Integer> table2 = new HashTable<>();
        table.put("apple", 10);
        table2.put("apple", 20);

        assertNotEquals(table, table2);
    }

    @Test
    void testEqualsWithNullValues() {
        HashTable<String, Integer> table2 = new HashTable<>();
        table.put("apple", null);
        table2.put("apple", null);

        assertEquals(table, table2);
    }

    @Test
    void testEqualsOneNullValue() {
        HashTable<String, Integer> table2 = new HashTable<>();
        table.put("apple", null);
        table2.put("apple", 10);

        assertNotEquals(table, table2);
    }

    @Test
    void testHashCode() {
        HashTable<String, Integer> table2 = new HashTable<>();

        table.put("apple", 10);
        table.put("banana", 20);

        table2.put("apple", 10);
        table2.put("banana", 20);

        assertEquals(table.hashCode(), table2.hashCode());
    }

    @Test
    void testHashCodeEmptyTable() {
        HashTable<String, Integer> table2 = new HashTable<>();
        assertEquals(table.hashCode(), table2.hashCode());
    }

    @Test
    void testToString() {
        assertTrue(table.toString().equals("{}"));

        table.put("apple", 10);
        String str = table.toString();
        assertTrue(str.contains("apple"));
        assertTrue(str.contains("10"));
        assertTrue(str.startsWith("{"));
        assertTrue(str.endsWith("}"));
    }

    @Test
    void testToStringMultipleElements() {
        table.put("apple", 10);
        table.put("banana", 20);
        String str = table.toString();
        assertTrue(str.contains("apple"));
        assertTrue(str.contains("banana"));
        assertTrue(str.contains("10"));
        assertTrue(str.contains("20"));
    }

    @Test
    void testToStringWithNullKey() {
        table.put(null, 10);
        String str = table.toString();
        assertTrue(str.contains("null") || str.contains("10"));
    }

    @Test
    void testMixedOperations() {
        HashTable<String, Number> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.update("one", 1.0);
        assertEquals(1.0, hashTable.get("one"));
    }

    @Test
    void testGetModCount() {
        int initialModCount = table.getModCount();
        table.put("key", 1);
        assertNotEquals(initialModCount, table.getModCount());
    }

    @Test
    void testModCountIncreasesOnPut() {
        int modCount1 = table.getModCount();
        table.put("key1", 1);
        int modCount2 = table.getModCount();
        assertTrue(modCount2 > modCount1);
    }

    @Test
    void testModCountIncreasesOnRemove() {
        int modCount1 = table.getModCount();
        table.put("key1", 1);
        int modCount2 = table.getModCount();
        table.remove("key1");
        int modCount3 = table.getModCount();
        assertTrue(modCount2 > modCount1);
        assertTrue(modCount3 > modCount2);
    }
}
