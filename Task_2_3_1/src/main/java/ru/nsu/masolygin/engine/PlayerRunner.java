package ru.nsu.masolygin.engine;

import ru.nsu.masolygin.model.GameModel;
import ru.nsu.masolygin.model.GameState;

/**
 * Поток выполнения шагов игрока.
 */
public class PlayerRunner extends AbstractGameThread {

    private final GameModel model;

    /**
     * Конструктор.
     *
     * @param model игровая модель
     */
    public PlayerRunner(GameModel model) {
        this.model = model;
    }

    /**
     * Возвращает имя потока.
     *
     * @return имя потока
     */
    @Override
    protected String threadName() {
        return "player-runner";
    }

    /**
     * Выполняет цикл хода игрока.
     */
    @Override
    protected void loop() {
        while (running) {
            long tickStart = System.currentTimeMillis();

            if (model.getState() == GameState.RUNNING) {
                model.executePlayerStep();
            }
            sleepRemaining(tickStart, model.getSpeedMs());
        }
    }
}
