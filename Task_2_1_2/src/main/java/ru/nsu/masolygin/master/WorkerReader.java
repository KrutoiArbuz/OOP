package ru.nsu.masolygin.master;

import java.io.IOException;
import ru.nsu.masolygin.protocol.Message;
import ru.nsu.masolygin.protocol.payload.HelloPayload;
import ru.nsu.masolygin.protocol.payload.ResultPayload;

/**
 * Читает входящие сообщения от рабочего процесса в отдельном потоке.
 */
public class WorkerReader {

    private final WorkerSession session;
    private final OnResult onResult;
    private final OnDisconnect onDisconnect;
    private final Thread thread;

    /**
     * Создает читатель сообщений от рабочего процесса.
     *
     * @param session      сессия с рабочим процессом
     * @param onResult     обработчик результатов
     * @param onDisconnect обработчик отключения
     */
    public WorkerReader(WorkerSession session,
        OnResult onResult,
        OnDisconnect onDisconnect) {
        this.session = session;
        this.onResult = onResult;
        this.onDisconnect = onDisconnect;
        this.thread = new Thread(this::loop, "reader-" + session.address());
    }

    /**
     * Запускает чтение сообщений в отдельном потоке.
     */
    public void start() {
        thread.start();
    }

    /**
     * Останавливает чтение сообщений.
     */
    public void stop() {
        thread.interrupt();
    }

    private void loop() {
        try {
            while (session.isAlive()) {
                Message msg = session.readMessage();
                session.touchResponseTime();
                switch (msg.type()) {
                    case HELLO -> session.setWorkerId(((HelloPayload) msg.payload()).workerId());
                    case RESULT -> onResult.handle(session, (ResultPayload) msg.payload());
                }
            }
        } catch (IOException e) {
            session.markDead();
            onDisconnect.handle(session);
        }
    }

    /**
     * Обработчик результатов от рабочего процесса.
     */
    public interface OnResult {

        /**
         * Обрабатывает результат выполнения задачи.
         *
         * @param w сессия рабочего процесса
         * @param r результат выполнения задачи
         */
        void handle(WorkerSession w, ResultPayload r);
    }

    /**
     * Обработчик отключения рабочего процесса.
     */
    public interface OnDisconnect {

        /**
         * Обрабатывает отключение рабочего процесса.
         *
         * @param w сессия рабочего процесса
         */
        void handle(WorkerSession w);
    }
}
