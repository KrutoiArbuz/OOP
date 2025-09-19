package ru.nsu.masolygin;

public abstract class Participant {
    private final Hand hand;
    private final String name;

    public Participant(String name) {
        this.name = name;
        this.hand = new Hand();
    }

    public String getName() {
        return name;
    }

    public void takeCard(Card card) {
        hand.addCard(card);
    }

    public int getScore() {
        return hand.calculateScore();
    }

    public boolean isBusted() {
        return hand.isBusted();
    }

    public boolean isBlackjack() {
        return hand.isBlackjack();
    }

    public String getHandDisplay() {
        return hand.getHandDisplay();
    }

    public String getInitialHandDisplay() {
        return hand.getInitialHandDisplay();
    }

    public abstract boolean shouldTakeCard(GameConsole console);

    public abstract void playTurn(Deck deck, GameConsole console);

    protected abstract void onCardDrawn(Card card, GameConsole console);

    public abstract boolean isDealer();

    public void clearHand() {
        hand.clear();
    }
}
