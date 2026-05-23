package ru.nsu.masolygin.protocol.payload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Полезная нагрузка с задачей на проверку простых чисел.
 *
 * @param taskId  идентификатор задачи
 * @param numbers массив чисел для проверки
 */
public record TaskPayload(long taskId, int[] numbers) implements Payload {

    /**
     * Декодирует массив байтов в объект TaskPayload.
     *
     * @param payload массив байтов
     * @return декодированный объект
     * @throws IOException при ошибке декодирования
     */
    public static TaskPayload decode(byte[] payload) throws IOException {
        DataInputStream d = new DataInputStream(new ByteArrayInputStream(payload));
        long taskId = d.readLong();
        int len = d.readInt();
        int[] nums = new int[len];
        for (int i = 0; i < len; i++) {
            nums[i] = d.readInt();
        }
        return new TaskPayload(taskId, nums);
    }

    /**
     * Кодирует задачу в массив байтов.
     *
     * @return закодированные байты
     * @throws IOException при ошибке кодирования
     */
    @Override
    public byte[] encode() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream d = new DataOutputStream(baos);
        d.writeLong(taskId);
        d.writeInt(numbers.length);
        for (int n : numbers) {
            d.writeInt(n);
        }
        return baos.toByteArray();
    }
}
