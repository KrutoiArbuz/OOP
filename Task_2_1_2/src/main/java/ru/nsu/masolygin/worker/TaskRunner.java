package ru.nsu.masolygin.worker;

import java.io.DataOutputStream;
import java.io.IOException;
import ru.nsu.masolygin.protocol.MessageIO;
import ru.nsu.masolygin.protocol.MessageType;
import ru.nsu.masolygin.protocol.payload.ResultPayload;
import ru.nsu.masolygin.protocol.payload.TaskPayload;


/**
 * Запускает выполнение задачи по проверке простых чисел в отдельном потоке.
 */
public class TaskRunner {

    private final TaskPayload payload;
    private final DataOutputStream out;
    private final long workerId;
    private final ChunkProcessor processor;
    private final Thread thread;

    /**
     * Создает запуск задачи.
     *
     * @param payload  полезная нагрузка с данными задачи
     * @param out      выходной поток для отправки результата
     * @param workerId идентификатор рабочего процесса
     */
    public TaskRunner(TaskPayload payload, DataOutputStream out, long workerId) {
        this.payload = payload;
        this.out = out;
        this.workerId = workerId;
        this.processor = new ChunkProcessor(payload.numbers());
        this.thread = new Thread(this::run, "worker-task-" + workerId);
    }

    /**
     * Запускает выполнение задачи в отдельном потоке.
     */
    public void start() {
        thread.start();
    }

    /**
     * Отменяет выполнение задачи.
     */
    public void cancel() {
        processor.cancel();
    }

    /**
     * Ожидает завершения выполнения задачи.
     *
     * @throws InterruptedException если поток был прерван
     */
    public void join() throws InterruptedException {
        thread.join();
    }

    private void run() {
        ProcessResult result = processor.run();
        if (result == ProcessResult.CANCELLED) {
            return;
        }
        boolean hasComposite = result == ProcessResult.COMPOSITE_FOUND;
        try {
            MessageIO.write(out, MessageType.RESULT,
                new ResultPayload(payload.taskId(), hasComposite));
        } catch (IOException e) {
            System.err.println("Worker " + workerId
                + " failed to send RESULT for task " + payload.taskId()
                + ": " + e.getMessage());
        }
    }
}
