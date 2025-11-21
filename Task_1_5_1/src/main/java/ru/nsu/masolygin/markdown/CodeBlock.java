package ru.nsu.masolygin.markdown;

import java.util.Objects;

/**
 * Блок кода в ``` ```.
 */
public class CodeBlock extends Element {
    private final String code;
    private final String language;

    /**
     * Создает блок кода.
     *
     * @param code код
     */
    public CodeBlock(String code) {
        this(code, "");
    }

    /**
     * Создает блок кода с указанием языка.
     *
     * @param code код
     * @param language язык программирования
     */
    public CodeBlock(String code, String language) {
        this.code = code != null ? code : "";
        this.language = language != null ? language : "";
    }

    /**
     * Преобразует блок кода в формат Markdown.
     * Блок оборачивается в ``` ```.
     *
     * @return блок кода в формате Markdown
     */
    @Override
    public String toMarkdown() {
        return "```" + language + "\n" + code + "\n```";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CodeBlock codeBlock = (CodeBlock) obj;
        return Objects.equals(code, codeBlock.code) && Objects.equals(language, codeBlock.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, language);
    }
}
