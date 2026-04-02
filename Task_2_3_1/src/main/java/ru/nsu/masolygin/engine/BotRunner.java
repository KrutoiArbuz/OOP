package ru.nsu.masolygin.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import ru.nsu.masolygin.model.Direction;
import ru.nsu.masolygin.model.GameModel;
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
     * @param bots список ботов
     * @param model игровая модель
     */
    public void start(List<BotSnake> bots, GameModel model) {
        running = true;
        for (BotSnake bot : bots) {
            int    id = THREAD_ID.getAndIncrement();
            Thread t  = new Thread(
                    () -> runBot(bot, model),
                    "bot-runner-" + id
            );
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
     * @param bots список ботов
     * @param model игровая модель
     */
    public void restart(List<BotSnake> bots, GameModel model) {
        stop();
        start(bots, model);
    }

    /**
     * Заглушка для базового контракта.
     */
    @Override
    protected void loop() {}

    /**
     * Возвращает имя потока.
     *
     * @return имя потока
     */
    @Override
    protected String threadName() { return "bot-runner"; }

    /**
     * Выполняет цикл одного бота.
     *
     * @param bot бот
     * @param model модель
     */
    private void runBot(BotSnake bot, GameModel model) {
        while (running) {
            long tickStart = System.currentTimeMillis();

            if (bot.isAlive() && model.getState() == GameState.RUNNING) {
                Direction dir = bot.computeDirection(model);
                model.executeBotStep(bot, dir);
            }
            sleepRemaining(tickStart, bot.getSpeedMs());
        }
    }
}
