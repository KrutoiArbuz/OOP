package ru.nsu.masolygin.markdown;

import java.util.Objects;

/**
 * Заголовок в Markdown (уровень от 1 до 6).
 */
public class Heading extends Element {
    private final int level;
    private final Element content;

    /**
     * Создает заголовок.
     *
     * @param level уровень (1-6)
     * @param content содержимое
     */
    public Heading(int level, Element content) {
        if (level < 1 || level > 6) {
            throw new IllegalArgumentException("Heading level must be between 1 and 6");
        }
        this.level = level;
        this.content = content != null ? content : new Text("");
    }

    /**
     * Создает заголовок.
     *
     * @param level уровень (1-6)
     * @param content текст
     */
    public Heading(int level, String content) {
        this(level, new Text(content));
    }

    /**
     * Преобразует заголовок в формат Markdown.
     * Количество символов # соответствует уровню заголовка.
     *
     * @return заголовок в формате Markdown (например, "### Заголовок")
     */
    @Override
    public String toMarkdown() {
        return "#".repeat(level) + " " + content.toMarkdown();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Heading heading = (Heading) obj;
        return level == heading.level && Objects.equals(content, heading.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, content);
    }
}
