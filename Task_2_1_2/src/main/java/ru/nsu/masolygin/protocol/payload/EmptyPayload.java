package ru.nsu.masolygin.protocol.payload;

/**
 * Пустая полезная нагрузка для служебных сообщений.
 */
public record EmptyPayload() implements Payload {

    /**
     * Декодирует массив байтов в объект EmptyPayload.
     *
     * @param payload массив байтов (не используется)
     * @return новый объект EmptyPayload
     */
    public static EmptyPayload decode(byte[] payload) {
        return new EmptyPayload();
    }

    /**
     * Кодирует пустую полезную нагрузку в массив байтов.
     *
     * @return пустой массив байтов
     */
    @Override
    public byte[] encode() {
        return new byte[0];
    }
}