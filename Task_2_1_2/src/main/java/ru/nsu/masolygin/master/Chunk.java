package ru.nsu.masolygin.master;

/**
 * Представляет блок данных для обработки рабочим процессом.
 *
 * @param taskId  идентификатор задачи
 * @param numbers массив чисел блока
 */
public record Chunk(long taskId, int[] numbers) {

}
