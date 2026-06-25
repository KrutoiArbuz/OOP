package ru.nsu.masolygin.protocol;

/**
 * Тип сообщения для протокола взаимодействия мастера и рабочего.
 */
public enum MessageType {
    /**
     * Приветствие воркера при подключении.
     */
    HELLO(0x01),
    /**
     * Задача воркеру на обработку.
     */
    TASK(0x02),
    /**
     * Результат обработки от воркера.
     */
    RESULT(0x03),
    /**
     * Отмена текущей задачи.
     */
    CANCEL(0x04),
    /**
     * Проверка живости (запрос).
     */
    PING(0x05),
    /**
     * Ответ на проверку живости.
     */
    PONG(0x06);

    private final byte code;

    MessageType(int code) {
        this.code = (byte) code;
    }

    /**
     * Преобразует код байта в тип сообщения.
     *
     * @param code код типа сообщения
     * @return тип сообщения
     * @throws IllegalArgumentException если код неизвестен
     */
    public static MessageType fromCode(byte code) {
        for (MessageType t : values()) {
            if (t.code == code) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown message type: " + code);
    }

    /**
     * Возвращает код типа сообщения.
     *
     * @return код типа
     */
    public byte code() {
        return code;
    }
}
