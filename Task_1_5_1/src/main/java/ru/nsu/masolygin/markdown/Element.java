package ru.nsu.masolygin.markdown;

/**
 * Базовый класс для всех элементов Markdown.
 */
public abstract class Element {
    /**
     * Создает новый элемент Markdown.
     */
    public Element() {
    }

    /**
     * Преобразует элемент в строку Markdown.
     *
     * @return строка в формате Markdown
     */
    public abstract String toMarkdown();

    @Override
    public String toString() {
        return toMarkdown();
    }

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();
}
