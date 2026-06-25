package ru.nsu.masolygin.protocol.payload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Полезная нагрузка с результатом проверки простых чисел.
 *
 * @param taskId       идентификатор задачи
 * @param hasComposite true, если найдено составное число
 */
public record ResultPayload(long taskId, boolean hasComposite) implements Payload {

    /**
     * Декодирует массив байтов в объект ResultPayload.
     *
     * @param payload массив байтов
     * @return декодированный объект
     * @throws IOException при ошибке декодирования
     */
    public static ResultPayload decode(byte[] payload) throws IOException {
        DataInputStream d = new DataInputStream(new ByteArrayInputStream(payload));
        return new ResultPayload(d.readLong(), d.readBoolean());
    }

    /**
     * Кодирует результат в массив байтов.
     *
     * @return закодированные байты
     * @throws IOException при ошибке кодирования
     */
    @Override
    public byte[] encode() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream d = new DataOutputStream(baos);
        d.writeLong(taskId);
        d.writeBoolean(hasComposite);
        return baos.toByteArray();
    }
}