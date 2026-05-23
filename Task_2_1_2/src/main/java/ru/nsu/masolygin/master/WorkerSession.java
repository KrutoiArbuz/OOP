package ru.nsu.masolygin.master;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import ru.nsu.masolygin.protocol.Message;
import ru.nsu.masolygin.protocol.MessageIO;
import ru.nsu.masolygin.protocol.MessageType;
import ru.nsu.masolygin.protocol.payload.EmptyPayload;
import ru.nsu.masolygin.protocol.payload.TaskPayload;

/**
 * Представляет сессию взаимодействия мастера с рабочим процессом.
 */
public class WorkerSession {

    private final InetSocketAddress address;
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private volatile long workerId = -1;
    private volatile long currentTaskId = -1;
    private volatile long lastResponseTime = System.currentTimeMillis();
    private volatile boolean alive = true;

    /**
     * Создает новую сессию с рабочим процессом.
     *
     * @param address          адрес рабочего процесса
     * @param connectTimeoutMs таймаут подключения в миллисекундах
     * @throws IOException если подключение не удалось
     */
    public WorkerSession(InetSocketAddress address, int connectTimeoutMs) throws IOException {
        this.address = address;
        this.socket = new Socket();
        this.socket.connect(address, connectTimeoutMs);
        this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    /**
     * Читает сообщение от рабочего процесса.
     *
     * @return прочитанное сообщение
     * @throws IOException при ошибке чтения
     */
    public Message readMessage() throws IOException {
        return MessageIO.read(in);
    }

    /**
     * Отправляет задачу рабочему процессу.
     *
     * @param c блок с задачей
     * @throws IOException при ошибке отправки
     */
    public void sendTask(Chunk c) throws IOException {
        MessageIO.write(out, MessageType.TASK, new TaskPayload(c.taskId(), c.numbers()));
        currentTaskId = c.taskId();
    }

    /**
     * Отправляет сообщение об отмене выполнения задачи.
     */
    public void sendCancel() {
        try {
            MessageIO.write(out, MessageType.CANCEL, new EmptyPayload());
        } catch (IOException ignored) {
        }
    }

    /**
     * Отправляет сообщение проверки живости.
     */
    public void sendPing() {
        try {
            MessageIO.write(out, MessageType.PING, new EmptyPayload());
        } catch (IOException e) {
            alive = false;
        }
    }

    /**
     * Закрывает сессию.
     */
    public void close() {
        alive = false;
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * Возвращает адрес рабочего процесса.
     *
     * @return адрес рабочего процесса
     */
    public InetSocketAddress address() {
        return address;
    }

    /**
     * Возвращает идентификатор воркера.
     *
     * @return идентификатор воркера (-1 до получения HELLO)
     */
    public long workerId() {
        return workerId;
    }

    /**
     * Устанавливает идентификатор воркера (вызывается при получении HELLO).
     *
     * @param id идентификатор воркера
     */
    public void setWorkerId(long id) {
        this.workerId = id;
    }

    /**
     * Возвращает идентификатор текущей задачи воркера.
     *
     * @return идентификатор текущей задачи или -1, если воркер свободен
     */
    public long currentTaskId() {
        return currentTaskId;
    }

    /**
     * Сбрасывает идентификатор текущей задачи (воркер снова свободен).
     */
    public void clearCurrentTask() {
        this.currentTaskId = -1;
    }

    /**
     * Проверяет, активна ли сессия.
     *
     * @return true, если сессия ещё активна
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Помечает воркера как мёртвого.
     */
    public void markDead() {
        this.alive = false;
    }

    /**
     * Возвращает время последнего ответа от воркера.
     *
     * @return время последнего ответа в миллисекундах (epoch)
     */
    public long lastResponseTime() {
        return lastResponseTime;
    }

    /**
     * Обновляет время последнего ответа на текущий момент.
     */
    public void touchResponseTime() {
        this.lastResponseTime = System.currentTimeMillis();
    }
}
