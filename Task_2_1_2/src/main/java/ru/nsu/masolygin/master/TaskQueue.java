package ru.nsu.masolygin.master;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Управляет очередью задач для распределённой обработки.
 */
public class TaskQueue {

    private final Deque<Chunk> pending = new ArrayDeque<>();
    private final Map<Long, Chunk> inProgress = new HashMap<>();
    private final Set<Long> done = new HashSet<>();
    private final int totalChunks;

    /**
     * Создает очередь задач из массива данных.
     *
     * @param data      массив чисел для обработки
     * @param chunkSize размер одной задачи
     */
    public TaskQueue(int[] data, int chunkSize) {
        long id = 0;
        for (int from = 0; from < data.length; from += chunkSize) {
            int to = Math.min(from + chunkSize, data.length);
            int[] part = Arrays.copyOfRange(data, from, to);
            pending.add(new Chunk(id++, part));
        }
        this.totalChunks = (int) id;
    }

    /**
     * Извлекает очередную задачу из очереди.
     *
     * @return следующая задача или null, если очередь пуста
     */
    public synchronized Chunk pollPending() {
        Chunk c = pending.pollFirst();
        if (c != null) {
            inProgress.put(c.taskId(), c);
        }
        return c;
    }

    /**
     * Отмечает задачу как выполненную.
     *
     * @param taskId идентификатор задачи
     */
    public synchronized void markDone(long taskId) {
        if (inProgress.remove(taskId) != null) {
            done.add(taskId);
        }
    }

    /**
     * Переназначает задачу обратно в очередь.
     *
     * @param taskId идентификатор задачи
     */
    public synchronized void reassign(long taskId) {
        Chunk c = inProgress.remove(taskId);
        if (c != null && !done.contains(taskId)) {
            pending.addFirst(c);
        }
    }

    /**
     * Проверяет, все ли задачи выполнены.
     *
     * @return true, если все задачи выполнены, иначе false
     */
    public synchronized boolean isAllDone() {
        return done.size() == totalChunks;
    }

    /**
     * Проверяет, знакома ли задача очереди.
     *
     * @param taskId идентификатор задачи
     * @return true, если задача известна, иначе false
     */
    public synchronized boolean isKnown(long taskId) {
        return inProgress.containsKey(taskId) || done.contains(taskId);
    }
}
