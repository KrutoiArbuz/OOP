package ru.nsu.masolygin;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Итератор для хеш-таблицы
 */
public class HashTableIterator<K, V> implements Iterator<HashTableEntry<K, V>> {
    private HashTableEntry<K, V> current;
    private HashTableEntry<K, V> next;
    private int index;
    private final int expectedModCount;
    private final HashTableEntry<K, V>[] table;
    private final int size;
    private final HashTable<K, V> hashTable;

    public HashTableIterator(HashTableEntry<K, V>[] table, int size, int modCount, HashTable<K, V> hashTable) {
        this.table = table;
        this.size = size;
        this.expectedModCount = modCount;
        this.hashTable = hashTable;

        if (size > 0) {
            while (index < table.length && (next = table[index]) == null) {
                index++;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public HashTableEntry<K, V> next() {
        if (expectedModCount != hashTable.getModCount()) {
            throw new ConcurrentModificationException();
        }

        if (next == null) {
            throw new NoSuchElementException();
        }

        current = next;
        next = current.getNext();

        if (next == null) {
            while (++index < table.length && (next = table[index]) == null);
        }

        return current;
    }
}
