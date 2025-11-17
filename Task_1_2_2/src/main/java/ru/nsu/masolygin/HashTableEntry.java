package ru.nsu.masolygin;

/**
 * Внутренний класс для представления записи хеш-таблицы (пара ключ-значение).
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public class HashTableEntry<K, V> {
    private final K key;
    private V value;
    private HashTableEntry<K, V> next;

    /**
     * Создает новую запись.
     *
     * @param key   ключ
     * @param value значение
     * @param next  следующая запись в цепочке
     */
    public HashTableEntry(K key, V value, HashTableEntry<K, V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    /**
     * Возвращает ключ.
     *
     * @return ключ
     */
    public K getKey() {
        return key;
    }

    /**
     * Возвращает значение.
     *
     * @return значение
     */
    public V getValue() {
        return value;
    }

    /**
     * Устанавливает новое значение.
     *
     * @param value новое значение
     */
    public void setValue(V value) {
        this.value = value;
    }

    /**
     * Возвращает следующую запись в цепочке.
     *
     * @return следующая запись
     */
    public HashTableEntry<K, V> getNext() {
        return next;
    }

    /**
     * Устанавливает следующую запись в цепочке.
     *
     * @param next следующая запись
     */
    public void setNext(HashTableEntry<K, V> next) {
        this.next = next;
    }
}
