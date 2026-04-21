package ru.nsu.masolygin.engine;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javafx.application.Platform;
import ru.nsu.masolygin.model.GameModel;
import ru.nsu.masolygin.model.GameSnapshot;

/**
 * Поток отрисовки кадров.
 */
public class RenderLoop extends AbstractGameThread {

    public static final int TARGET_FPS = 60;
    private static final long FRAME_MS = 1000L / TARGET_FPS;

    private final GameModel model;
    private final Consumer<GameSnapshot> renderer;
    private final AtomicBoolean renderPending = new AtomicBoolean(false);

    /**
     * Конструктор.
     *
     * @param model    игровая модель
     * @param renderer функция отрисовки
     */
    public RenderLoop(GameModel model, Consumer<GameSnapshot> renderer) {
        this.model = model;
        this.renderer = renderer;
    }

    /**
     * Возвращает имя потока.
     *
     * @return имя потока
     */
    @Override
    protected String threadName() {
        return "render-loop";
    }

    /**
     * Выполняет цикл отрисовки.
     */
    @Override
    protected void loop() {
        while (running) {
            long frameStart = System.currentTimeMillis();

            if (renderPending.compareAndSet(false, true)) {
                GameSnapshot snap = model.getLatestSnapshot();
                if (snap != null) {
                    Platform.runLater(() -> {
                        renderer.accept(snap);
                        renderPending.set(false);
                    });
                } else {
                    renderPending.set(false);
                }
            }
            sleepRemaining(frameStart, FRAME_MS);
        }
    }
}
