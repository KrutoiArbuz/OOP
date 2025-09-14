package ru.nsu.masolygin;

public class Dealer extends Participant {
    public Dealer() {
        super("Dealer");
    }

    public boolean shouldTakeCard() {
        return getScore() < 17;
    }


}
