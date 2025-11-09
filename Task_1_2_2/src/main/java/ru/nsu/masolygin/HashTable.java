package ru.nsu.masolygin;

import java.util.Iterator;

/**
 * Параметризованный контейнер HashTable<K, V> для добавления, поиска и удаления
 * объектов V по ключу K за константное время.
 */
public class HashTable<K, V> implements Iterable<HashTableEntry<K, V>> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAX_CAPACITY = 1 << 30;

    private HashTableEntry<K, V>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;
    private int modCount;

    /**
     * Создает пустую хеш-таблицу с размером по умолчанию
     */
    public HashTable() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Создает пустую хеш-таблицу с указанной емкостью и коэффициентом загрузки
     *
     * @param initialCapacity начальная емкость
     * @param loadFactor      коэффициент загрузки
     */
    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Недопустимая начальная емкость: " + initialCapacity);
        }
        if (initialCapacity > MAX_CAPACITY) {
            initialCapacity = MAX_CAPACITY;
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Недопустимый коэффициент загрузки: " + loadFactor);
        }

        this.loadFactor = loadFactor;
        table = (HashTableEntry<K, V>[]) new HashTableEntry<?, ?>[initialCapacity];
        threshold = (int) (initialCapacity * loadFactor);
    }

    /**
     * Вычисляет индекс в таблице для ключа
     */
    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    /**
     * Добавляет новую пару ключ-значение в таблицу
     *
     * @param key   ключ
     * @param value значение
     * @return предыдущее значение или null, если такого ключа не было
     */
    public V put(K key, V value) {
        if (size >= threshold) {
            resize(2 * table.length);
        }

        int index = hash(key);
        HashTableEntry<K, V> current = table[index];

        while (current != null) {
            if ((key == null && current.getKey() == null) ||
                    (key != null && key.equals(current.getKey()))) {
                V oldValue = current.getValue();
                current.setValue(value);
                return oldValue;
            }
            current = current.getNext();
        }

        table[index] = new HashTableEntry<>(key, value, table[index]);
        size++;
        modCount++;
        return null;
    }

    /**
     * Обновляет значение для указанного ключа
     *
     * @param key   ключ
     * @param value новое значение
     * @return true если значение было обновлено, false если ключ не найден
     */
    public boolean update(K key, V value) {
        int index = hash(key);
        HashTableEntry<K, V> current = table[index];

        while (current != null) {
            if ((key == null && current.getKey() == null) ||
                    (key != null && key.equals(current.getKey()))) {
                current.setValue(value);
                return true;
            }
            current = current.getNext();
        }

        return false;
    }

    /**
     * Возвращает значение по ключу
     *
     * @param key ключ
     * @return значение или null, если ключ не найден
     */
    public V get(K key) {
        int index = hash(key);
        HashTableEntry<K, V> current = table[index];

        while (current != null) {
            if ((key == null && current.getKey() == null) ||
                    (key != null && key.equals(current.getKey()))) {
                return current.getValue();
            }
            current = current.getNext();
        }

        return null;
    }

    /**
     * Проверяет наличие ключа в таблице
     *
     * @param key ключ
     * @return true если ключ найден
     */
    public boolean containsKey(K key) {
        int index = hash(key);
        HashTableEntry<K, V> current = table[index];

        while (current != null) {
            if ((key == null && current.getKey() == null) ||
                    (key != null && key.equals(current.getKey()))) {
                return true;
            }
            current = current.getNext();
        }

        return false;
    }

    /**
     * Удаляет запись с указанным ключом
     *
     * @param key ключ
     * @return удаленное значение или null, если ключ не найден
     */
    public V remove(K key) {
        int index = hash(key);
        HashTableEntry<K, V> current = table[index];
        HashTableEntry<K, V> prev = null;

        while (current != null) {
            if ((key == null && current.getKey() == null) ||
                    (key != null && key.equals(current.getKey()))) {
                V removedValue = current.getValue();

                if (prev == null) {
                    table[index] = current.getNext();
                } else {
                    prev.setNext(current.getNext());
                }

                size--;
                modCount++;
                return removedValue;
            }

            prev = current;
            current = current.getNext();
        }

        return null;
    }

    /**
     * Возвращает количество элементов в таблице
     */
    public int size() {
        return size;
    }

    /**
     * Проверяет, пуста ли таблица
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Очищает таблицу
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        table = (HashTableEntry<K, V>[]) new HashTableEntry<?, ?>[DEFAULT_CAPACITY];
        size = 0;
        modCount++;
        threshold = (int) (DEFAULT_CAPACITY * loadFactor);
    }

    /**
     * Изменяет размер хеш-таблицы
     */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        if (newCapacity > MAX_CAPACITY) {
            newCapacity = MAX_CAPACITY;
        }

        HashTableEntry<K, V>[] oldTable = table;
        HashTableEntry<K, V>[] newTable = (HashTableEntry<K, V>[]) new HashTableEntry<?, ?>[newCapacity];

        for (HashTableEntry<K, V> entry : oldTable) {
            while (entry != null) {
                HashTableEntry<K, V> next = entry.getNext();
                int newIndex = (entry.getKey() == null) ? 0 : Math.abs(entry.getKey().hashCode() % newCapacity);

                entry.setNext(newTable[newIndex]);
                newTable[newIndex] = entry;
                entry = next;
            }
        }

        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    /**
     * Возвращает счетчик модификаций для итератора
     *
     * @return счетчик модификаций
     */
    public int getModCount() {
        return modCount;
    }

    /**
     * Возвращает итератор для обхода элементов таблицы
     *
     * @return итератор
     */
    @Override
    public Iterator<HashTableEntry<K, V>> iterator() {
        return new HashTableIterator<>(table, size, modCount, this);
    }

    /**
     * Сравнивает эту хеш-таблицу с другим объектом на равенство
     *
     * @param obj объект для сравнения
     * @return true если таблицы равны
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        @SuppressWarnings("unchecked")
        HashTable<K, V> other = (HashTable<K, V>) obj;

        if (size != other.size) {
            return false;
        }

        for (HashTableEntry<K, V> entry : this) {
            K key = entry.getKey();
            V value = entry.getValue();
            V otherValue = other.get(key);

            if (otherValue == null && !other.containsKey(key)) {
                return false;
            }

            if (value == null) {
                if (otherValue != null) {
                    return false;
                }
            } else if (!value.equals(otherValue)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Возвращает хеш-код таблицы
     *
     * @return хеш-код
     */
    @Override
    public int hashCode() {
        int hash = 0;
        for (HashTableEntry<K, V> entry : this) {
            hash += (entry.getKey() == null ? 0 : entry.getKey().hashCode()) ^
                    (entry.getValue() == null ? 0 : entry.getValue().hashCode());
        }
        return hash;
    }

    /**
     * Возвращает строковое представление хеш-таблицы
     *
     * @return строковое представление
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;

        for (HashTableEntry<K, V> entry : this) {
            if (!first) {
                sb.append(", ");
            }
            first = false;

            K key = entry.getKey();
            V value = entry.getValue();

            sb.append(key == this ? "(this)" : key);
            sb.append("=");
            sb.append(value == this ? "(this)" : value);
        }

        sb.append("}");
        return sb.toString();
    }
}
