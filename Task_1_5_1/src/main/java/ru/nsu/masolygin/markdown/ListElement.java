package ru.nsu.masolygin.markdown;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Список (упорядоченный или неупорядоченный).
 */
public class ListElement extends Element {
    private final List<Object> items;
    private final boolean ordered;

    private ListElement(List<Object> items, boolean ordered) {
        this.items = new ArrayList<>(items);
        this.ordered = ordered;
    }

    @Override
    public String toMarkdown() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (ordered) {
                result.append((i + 1)).append(". ");
            } else {
                result.append("- ");
            }

            Object item = items.get(i);
            if (item instanceof Element) {
                result.append(((Element) item).toMarkdown());
            } else {
                result.append(item.toString());
            }

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
        ListElement that = (ListElement) obj;
        return ordered == that.ordered && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, ordered);
    }

    /**
     * Строитель для создания списка.
     */
    public static class Builder {
        private final List<Object> items = new ArrayList<>();
        private boolean ordered = false;

        /**
         * Создает новый строитель списка.
         */
        public Builder() {
        }

        /**
         * Делает список упорядоченным (с номерами).
         *
         * @return строитель для цепочки вызовов
         */
        public Builder ordered() {
            this.ordered = true;
            return this;
        }

        /**
         * Делает список неупорядоченным (с маркерами).
         *
         * @return строитель для цепочки вызовов
         */
        public Builder unordered() {
            this.ordered = false;
            return this;
        }

        /**
         * Добавляет элемент в список.
         *
         * @param item элемент списка
         * @return строитель для цепочки вызовов
         */
        public Builder addItem(Object item) {
            this.items.add(item);
            return this;
        }

        /**
         * Создает список.
         *
         * @return готовый список
         */
        public ListElement build() {
            return new ListElement(items, ordered);
        }
    }
}
