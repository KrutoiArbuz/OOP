package ru.nsu.masolygin.master;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TaskQueue {

    private final Deque<Chunk> pending = new ArrayDeque<>();
    private final Map<Long, Chunk> inProgress = new HashMap<>();
    private final Set<Long> done = new HashSet<>();
    private final int totalChunks;

    public TaskQueue(int[] data, int chunkSize) {
        long id = 0;
        for (int from = 0; from < data.length; from += chunkSize) {
            int to = Math.min(from + chunkSize, data.length);
            int[] part = Arrays.copyOfRange(data, from, to);
            pending.add(new Chunk(id++, part));
        }
        this.totalChunks = (int) id;
    }

    public synchronized Chunk pollPending() {
        Chunk c = pending.pollFirst();
        if (c != null) {
            inProgress.put(c.taskId(), c);
        }
        return c;
    }

    public synchronized void markDone(long taskId) {
        if (inProgress.remove(taskId) != null) {
            done.add(taskId);
        }
    }

    public synchronized void reassign(long taskId) {
        Chunk c = inProgress.remove(taskId);
        if (c != null && !done.contains(taskId)) {
            pending.addFirst(c);
        }
    }

    public synchronized boolean isAllDone() {
        return done.size() == totalChunks;
    }

    public synchronized boolean isKnown(long taskId) {
        return inProgress.containsKey(taskId) || done.contains(taskId);
    }
}
