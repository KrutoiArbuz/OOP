package ru.nsu.masolygin.markdown;

import java.util.Objects;

/**
 * Цитата. Каждая строка начинается с '>'.
 */
public class Quote extends Element {
    private final Element content;

    /**
     * Создает цитату.
     *
     * @param content содержимое
     */
    public Quote(Element content) {
        this.content = content != null ? content : new Text("");
    }

    /**
     * Создает цитату.
     *
     * @param content текст
     */
    public Quote(String content) {
        this(new Text(content));
    }

    /**
     * Преобразует цитату в формат Markdown.
     * Каждая строка цитаты начинается с символа '>'.
     *
     * @return цитата в формате Markdown (например, "> текст цитаты")
     */
    @Override
    public String toMarkdown() {
        String markdown = content.toMarkdown();
        String[] lines = markdown.split("\n");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            result.append("> ").append(lines[i]);
            if (i < lines.length - 1) {
                result.append("\n");
            }
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Quote quote = (Quote) obj;
        return Objects.equals(content, quote.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
