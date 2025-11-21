package ru.nsu.masolygin.markdown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Объединяет несколько элементов в один.
 */
public class Composite extends Element {
    private final List<Element> elements;

    /**
     * Создает композицию элементов.
     *
     * @param elements элементы для объединения
     */
    public Composite(Element... elements) {
        this.elements = new ArrayList<>(Arrays.asList(elements));
    }

    /**
     * Создает композицию элементов.
     *
     * @param elements список элементов
     */
    public Composite(List<Element> elements) {
        this.elements = new ArrayList<>(elements);
    }

    /**
     * Преобразует композитный элемент в формат Markdown.
     * Последовательно вызывает toMarkdown() для каждого вложенного элемента.
     *
     * @return объединенная строка Markdown всех элементов
     */
    @Override
    public String toMarkdown() {
        StringBuilder result = new StringBuilder();
        for (Element element : elements) {
            result.append(element.toMarkdown());
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Composite composite = (Composite) obj;
        return Objects.equals(elements, composite.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }
}
