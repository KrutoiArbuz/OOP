package ru.nsu.masolygin.protocol.payload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Полезная нагрузка приветствия рабочего.
 *
 * @param workerId идентификатор рабочего процесса
 */
public record HelloPayload(long workerId) implements Payload {

    /**
     * Декодирует массив байтов в объект HelloPayload.
     *
     * @param payload массив байтов
     * @return декодированный объект
     * @throws IOException при ошибке декодирования
     */
    public static HelloPayload decode(byte[] payload) throws IOException {
        return new HelloPayload(new DataInputStream(new ByteArrayInputStream(payload)).readLong());
    }

    /**
     * Кодирует приветствие в массив байтов.
     *
     * @return закодированные байты
     * @throws IOException при ошибке кодирования
     */
    @Override
    public byte[] encode() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new DataOutputStream(baos).writeLong(workerId);
        return baos.toByteArray();
    }
}