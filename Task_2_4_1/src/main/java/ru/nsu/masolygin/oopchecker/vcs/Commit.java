package ru.nsu.masolygin.oopchecker.vcs;

import java.time.Instant;

/**
 * Данные коммита из git log, необходимые для оценки и анализа активности.
 *
 * @param hash        хеш коммита
 * @param timestamp   время создания коммита
 * @param authorName  имя автора
 * @param authorEmail email автора
 * @param subject     заголовок коммита
 */
public record Commit(String hash,
                     Instant timestamp,
                     String authorName,
                     String authorEmail,
                     String subject
) {

}
