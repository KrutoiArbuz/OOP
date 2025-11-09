package ru.nsu.masolygin;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Итератор для хеш-таблицы.
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public class HashTableIterator<K, V> implements Iterator<HashTableEntry<K, V>> {
    private HashTableEntry<K, V> current;
    private HashTableEntry<K, V> next;
    private int index;
    private final int expectedModCount;
    private final HashTableEntry<K, V>[] table;
    private final int size;
    private final HashTable<K, V> hashTable;

    /**
     * Создает новый итератор для хеш-таблицы.
     *
     * @param table массив корзин хеш-таблицы
     * @param size размер таблицы
     * @param modCount счетчик модификаций
     * @param hashTable ссылка на хеш-таблицу
     */
    public HashTableIterator(HashTableEntry<K, V>[] table, int size, int modCount,
                           HashTable<K, V> hashTable) {
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
            while (++index < table.length && (next = table[index]) == null) {
                // Поиск следующего непустого элемента
            }
        }

        return current;
    }
}
