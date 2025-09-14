package ru.nsu.masolygin;

public class Participant {
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

    public void showHand() {
        hand.showHand();
    }

    public void showInitialHand() {
        System.out.print("[" + hand.getCards().getFirst() + ", <face-down card> ]\n");
    }

    public void clearHand() {
        hand.clear();
    }
}
