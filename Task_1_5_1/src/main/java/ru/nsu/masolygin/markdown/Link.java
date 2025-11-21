package ru.nsu.masolygin.markdown;

import java.util.Objects;

/**
 * Ссылка: [текст](url).
 */
public class Link extends Element {
    private final Element text;
    private final String url;

    /**
     * Создает ссылку.
     *
     * @param text текст ссылки
     * @param url адрес
     */
    public Link(Element text, String url) {
        this.text = text != null ? text : new Text("");
        this.url = url != null ? url : "";
    }

    /**
     * Создает ссылку.
     *
     * @param text текст ссылки
     * @param url адрес
     */
    public Link(String text, String url) {
        this(new Text(text), url);
    }

    @Override
    public String toMarkdown() {
        return "[" + text.toMarkdown() + "](" + url + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Link link = (Link) obj;
        return Objects.equals(text, link.text) && Objects.equals(url, link.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, url);
    }
}
