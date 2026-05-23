package ru.nsu.masolygin.protocol;

import ru.nsu.masolygin.protocol.payload.Payload;

/**
 * Структура сообщения для обмена между мастером и рабочим.
 *
 * @param type    тип сообщения
 * @param payload полезная нагрузка
 */
public record Message(MessageType type, Payload payload) {

}
