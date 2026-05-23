package ru.nsu.masolygin.worker;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;
import ru.nsu.masolygin.protocol.Message;
import ru.nsu.masolygin.protocol.MessageIO;
import ru.nsu.masolygin.protocol.MessageType;
import ru.nsu.masolygin.protocol.payload.EmptyPayload;
import ru.nsu.masolygin.protocol.payload.HelloPayload;
import ru.nsu.masolygin.protocol.payload.TaskPayload;

public class Worker {

    private final int port;
    private final long workerId;
    private volatile TaskRunner currentRunner;

    public Worker(int port) {
        this.port = port;
        this.workerId = UUID.randomUUID().getMostSignificantBits();
    }

    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 5001;
        new Worker(port).start();
    }

    private void start() throws IOException {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Worker " + workerId + " listening on " + port);
            while (!Thread.currentThread().isInterrupted()) {
                Socket client = server.accept();
                handleMaster(client);
            }
        }
    }

    private void handleMaster(Socket socket) {
        try (socket;
            DataInputStream in = new DataInputStream(
                new BufferedInputStream(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(socket.getOutputStream()))) {
            MessageIO.write(out, MessageType.HELLO, new HelloPayload(workerId));

            while (true) {
                Message msg = MessageIO.read(in);
                switch (msg.type()) {
                    case TASK -> handleTask(msg, out);
                    case CANCEL -> {
                        if (currentRunner != null) {
                            currentRunner.cancel();
                        }
                    }
                    case PING -> MessageIO.write(out, MessageType.PONG, new EmptyPayload());
                }
            }
        } catch (EOFException | SocketException e) {
            System.out.println("Worker " + workerId + ": master disconnected");
        } catch (IOException e) {
            System.err.println("Worker IO error: " + e.getMessage());
        }
    }

    private void handleTask(Message msg, DataOutputStream out) {
        TaskPayload payload = (TaskPayload) msg.payload();

        if (currentRunner != null) {
            currentRunner.cancel();
            try {
                currentRunner.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        currentRunner = new TaskRunner(payload, out, workerId);
        currentRunner.start();
    }
}
