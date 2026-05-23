package ru.nsu.masolygin.master;

import java.io.IOException;
import ru.nsu.masolygin.protocol.Message;
import ru.nsu.masolygin.protocol.payload.HelloPayload;
import ru.nsu.masolygin.protocol.payload.ResultPayload;

public class WorkerReader {

    private final WorkerSession session;
    private final OnResult onResult;
    private final OnDisconnect onDisconnect;
    private final Thread thread;

    public WorkerReader(WorkerSession session,
        OnResult onResult,
        OnDisconnect onDisconnect) {
        this.session = session;
        this.onResult = onResult;
        this.onDisconnect = onDisconnect;
        this.thread = new Thread(this::loop, "reader-" + session.address());
    }

    public void start() {
        thread.start();
    }

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

    public interface OnResult {

        void handle(WorkerSession w, ResultPayload r);
    }

    public interface OnDisconnect {

        void handle(WorkerSession w);
    }
}
