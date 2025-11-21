package ru.nsu.masolygin.markdown;

import java.util.Objects;

/**
 * Простой текст и его форматирование (жирный, курсив, зачеркнутый, код).
 */
public class Text extends Element {
    /** Содержимое текста. */
    protected final String content;

    /**
     * Создает простой текст.
     *
     * @param content текст
     */
    public Text(String content) {
        this.content = content != null ? content : "";
    }

    /**
     * Преобразует текст в формат Markdown.
     *
     * @return содержимое текста без форматирования
     */
    @Override
    public String toMarkdown() {
        return content;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Text text = (Text) obj;
        return Objects.equals(content, text.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    /**
     * Жирный текст: **текст**.
     */
    public static class Bold extends Text {
        /**
         * Создает жирный текст.
         *
         * @param content текст
         */
        public Bold(String content) {
            super(content);
        }

        /**
         * Преобразует текст в формат жирного Markdown.
         *
         * @return текст, обернутый в **
         */
        @Override
        public String toMarkdown() {
            return "**" + content + "**";
        }
    }

    /**
     * Курсивный текст: *текст*.
     */
    public static class Italic extends Text {
        /**
         * Создает курсивный текст.
         *
         * @param content текст
         */
        public Italic(String content) {
            super(content);
        }

        /**
         * Преобразует текст в формат курсивного Markdown.
         *
         * @return текст, обернутый в *
         */
        @Override
        public String toMarkdown() {
            return "*" + content + "*";
        }
    }

    /**
     * Зачеркнутый текст: ~~текст~~.
     */
    public static class Strikethrough extends Text {
        /**
         * Создает зачеркнутый текст.
         *
         * @param content текст
         */
        public Strikethrough(String content) {
            super(content);
        }

        /**
         * Преобразует текст в формат зачеркнутого Markdown.
         *
         * @return текст, обернутый в ~~
         */
        @Override
        public String toMarkdown() {
            return "~~" + content + "~~";
        }
    }

    /**
     * Однострочный код: `код`.
     */
    public static class Code extends Text {
        /**
         * Создает однострочный код.
         *
         * @param content код
         */
        public Code(String content) {
            super(content);
        }

        /**
         * Преобразует код в формат однострочного Markdown кода.
         *
         * @return код, обернутый в `
         */
        @Override
        public String toMarkdown() {
            return "`" + content + "`";
        }
    }
}
