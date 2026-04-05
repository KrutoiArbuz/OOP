package ru.nsu.masolygin.engine;

import ru.nsu.masolygin.model.GameEngine;
import ru.nsu.masolygin.model.GameState;

/**
 * Поток выполнения шагов игрока.
 */
public class PlayerRunner extends AbstractGameThread {

    private final GameEngine engine;

    /**
     * Конструктор.
     *
     * @param engine игровой движок
     */
    public PlayerRunner(GameEngine engine) {
        this.engine = engine;
    }

    @Override
    protected String threadName() {
        return "player-runner";
    }

    @Override
    protected void loop() {
        while (running) {
            long tickStart = System.currentTimeMillis();

            if (engine.getState() == GameState.RUNNING) {
                engine.executePlayerStep();
            }
            sleepRemaining(tickStart, engine.getSpeedMs());
        }
    }
}
