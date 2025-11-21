package ru.nsu.masolygin.markdown;

import java.util.Objects;

/**
 * Изображение: ![alt](url)
 */
public class Image extends Element {
    private final String altText;
    private final String url;

    /**
     * Создает изображение.
     *
     * @param altText описание
     * @param url адрес
     */
    public Image(String altText, String url) {
        this.altText = altText != null ? altText : "";
        this.url = url != null ? url : "";
    }

    @Override
    public String toMarkdown() {
        return "![" + altText + "](" + url + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Image image = (Image) obj;
        return Objects.equals(altText, image.altText) && Objects.equals(url, image.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(altText, url);
    }
}
