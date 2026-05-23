package ru.nsu.masolygin.protocol.payload;

import java.io.IOException;

/**
 * Интерфейс для полезной нагрузки сообщения.
 */
public interface Payload {

    /**
     * Кодирует полезную нагрузку в массив байтов.
     *
     * @return закодированные байты
     * @throws IOException при ошибке кодирования
     */
    byte[] encode() throws IOException;
}
