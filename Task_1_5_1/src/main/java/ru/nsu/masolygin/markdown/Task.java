package ru.nsu.masolygin.markdown;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Список задач с чекбоксами: - [x] или - [ ]
 */
public class Task extends Element {
    private final List<TaskItem> items;

    private Task(List<TaskItem> items) {
        this.items = new ArrayList<>(items);
    }

    @Override
    public String toMarkdown() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            TaskItem item = items.get(i);
            result.append("- [").append(item.checked ? "x" : " ").append("] ").append(item.text);
            if (i < items.size() - 1) {
                result.append("\n");
            }
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(items, task.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    /**
     * Одна задача.
     */
    private static class TaskItem {
        final String text;
        final boolean checked;

        TaskItem(String text, boolean checked) {
            this.text = text;
            this.checked = checked;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TaskItem taskItem = (TaskItem) obj;
            return checked == taskItem.checked && Objects.equals(text, taskItem.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(text, checked);
        }
    }

    /**
     * Строитель для создания списка задач.
     */
    public static class Builder {
        private final List<TaskItem> items = new ArrayList<>();

        /**
         * Создает новый строитель списка задач.
         */
        public Builder() {
        }

        /**
         * Добавляет задачу.
         *
         * @param text текст задачи
         * @param checked выполнена (true) или нет (false)
         * @return строитель для цепочки вызовов
         */
        public Builder addTask(String text, boolean checked) {
            this.items.add(new TaskItem(text, checked));
            return this;
        }

        /**
         * Добавляет невыполненную задачу.
         *
         * @param text текст задачи
         * @return строитель для цепочки вызовов
         */
        public Builder addTask(String text) {
            return addTask(text, false);
        }

        /**
         * Создает список задач.
         *
         * @return готовый список задач
         */
        public Task build() {
            return new Task(items);
        }
    }
}
