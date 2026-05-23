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

public class WorkerSession {

    private final InetSocketAddress address;
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private volatile long workerId = -1;
    private volatile long currentTaskId = -1;
    private volatile long lastResponseTime = System.currentTimeMillis();
    private volatile boolean alive = true;

    public WorkerSession(InetSocketAddress address, int connectTimeoutMs) throws IOException {
        this.address = address;
        this.socket = new Socket();
        this.socket.connect(address, connectTimeoutMs);
        this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public Message readMessage() throws IOException {
        return MessageIO.read(in);
    }

    public void sendTask(Chunk c) throws IOException {
        MessageIO.write(out, MessageType.TASK, new TaskPayload(c.taskId(), c.numbers()));
        currentTaskId = c.taskId();
    }

    public void sendCancel() {
        try {
            MessageIO.write(out, MessageType.CANCEL, new EmptyPayload());
        } catch (IOException ignored) {
        }
    }

    public void sendPing() {
        try {
            MessageIO.write(out, MessageType.PING, new EmptyPayload());
        } catch (IOException e) {
            alive = false;
        }
    }

    public void close() {
        alive = false;
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }

    public InetSocketAddress address() {
        return address;
    }

    public long workerId() {
        return workerId;
    }

    public void setWorkerId(long id) {
        this.workerId = id;
    }

    public long currentTaskId() {
        return currentTaskId;
    }

    public void clearCurrentTask() {
        this.currentTaskId = -1;
    }

    public boolean isAlive() {
        return alive;
    }

    public void markDead() {
        this.alive = false;
    }

    public long lastResponseTime() {
        return lastResponseTime;
    }

    public void touchResponseTime() {
        this.lastResponseTime = System.currentTimeMillis();
    }
}
