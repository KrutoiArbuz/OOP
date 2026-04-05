package ru.nsu.masolygin.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import ru.nsu.masolygin.model.Direction;
import ru.nsu.masolygin.model.GameEngine;
import ru.nsu.masolygin.model.GameState;
import ru.nsu.masolygin.model.bot.BotSnake;

/**
 * Запускает и останавливает потоки ботов.
 */
public class BotRunner extends AbstractGameThread {

    private static final AtomicInteger THREAD_ID = new AtomicInteger(1);
    private final List<Thread> botThreads = new ArrayList<>();

    /**
     * Запускает потоки для ботов.
     *
     * @param bots   список ботов
     * @param engine игровой движок
     */
    public void start(List<BotSnake> bots, GameEngine engine) {
        running = true;
        for (BotSnake bot : bots) {
            int id = THREAD_ID.getAndIncrement();
            Thread t = new Thread(() -> runBot(bot, engine), "bot-runner-" + id);
            t.setDaemon(true);
            botThreads.add(t);
            t.start();
        }
    }

    /**
     * Останавливает потоки ботов.
     */
    @Override
    public void stop() {
        running = false;
        botThreads.forEach(Thread::interrupt);
        botThreads.clear();
    }

    /**
     * Перезапускает потоки с новым набором ботов.
     *
     * @param bots   список ботов
     * @param engine игровой движок
     */
    public void restart(List<BotSnake> bots, GameEngine engine) {
        stop();
        start(bots, engine);
    }

    @Override
    protected String threadName() {
        return "bot-runner";
    }

    /**
     * Выполняет цикл одного бота.
     *
     * @param bot    бот
     * @param engine игровой движок
     */
    private void runBot(BotSnake bot, GameEngine engine) {
        while (running) {
            long tickStart = System.currentTimeMillis();

            if (bot.isAlive() && engine.getState() == GameState.RUNNING) {
                Direction dir = bot.computeDirection(engine.getModel());
                engine.executeBotStep(bot, dir);
            }

            sleepRemaining(tickStart, bot.getSpeedMs());
        }
    }
}
