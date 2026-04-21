package ru.nsu.masolygin.model.food;

import java.util.Random;
import ru.nsu.masolygin.model.Point;

/**
 * Фабрика создания еды.
 */
public final class FoodFactory {

    private static final Random RANDOM = new Random();

    /**
     * Конструктор.
     */
    private FoodFactory() {
    }

    /**
     * Создает случайную еду.
     *
     * @param position позиция
     * @return объект еды
     */
    public static Food createRandom(Point position) {
        return new Food(position, pickWeightedRandom());
    }

    /**
     * Выбирает случайный тип еды с учетом веса.
     *
     * @return тип еды
     */
    private static FoodType pickWeightedRandom() {
        FoodType[] types = FoodType.values();
        int totalWeight = 0;
        for (FoodType type : types) {
            totalWeight += type.getWeight();
        }

        int roll = RANDOM.nextInt(totalWeight);
        for (FoodType type : types) {
            roll -= type.getWeight();
            if (roll < 0) {
                return type;
            }
        }
        return types[0];
    }
}