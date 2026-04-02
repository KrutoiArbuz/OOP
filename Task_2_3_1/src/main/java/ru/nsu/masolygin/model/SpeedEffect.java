package ru.nsu.masolygin.model;

/**
 * Эффект временного изменения скорости.
 */
public class SpeedEffect {

    private static final int DURATION_TICKS = 30;

    private final int       baseSpeedMs;
    private volatile int    currentSpeedMs;
    private int             ticksLeft;

    /**
     * Конструктор.
     *
     * @param baseSpeedMs базовая скорость
     */
    public SpeedEffect(int baseSpeedMs) {
        this.baseSpeedMs    = baseSpeedMs;
        this.currentSpeedMs = baseSpeedMs;
        this.ticksLeft      = 0;
    }

    /**
     * Применяет изменение скорости.
     *
     * @param deltaMs изменение скорости
     */
    public void apply(int deltaMs) {
        if (deltaMs == 0) return;
        currentSpeedMs = Math.max(60, Math.min(800, baseSpeedMs + deltaMs));
        ticksLeft      = DURATION_TICKS;
    }

    /**
     * Выполняет один тик эффекта.
     */
    public void tick() {
        if (ticksLeft <= 0) return;
        if (--ticksLeft == 0) currentSpeedMs = baseSpeedMs;
    }

    /**
     * Сбрасывает эффект.
     */
    public void reset() {
        currentSpeedMs = baseSpeedMs;
        ticksLeft      = 0;
    }

    /**
     * Возвращает текущую скорость.
     *
     * @return скорость в миллисекундах
     */
    public int getSpeedMs()   { return currentSpeedMs; }

    /**
     * Возвращает оставшиеся тики эффекта.
     *
     * @return количество тиков
     */
    public int getTicksLeft() { return ticksLeft; }
}
