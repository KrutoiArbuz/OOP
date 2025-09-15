package ru.nsu.masolygin;

import java.util.LinkedList;
import java.util.List;

public class Hand {
    private final List<Card> cards;

    public Hand() {
        cards = new LinkedList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return cards;
    }

    public int calculateScore() {
        int score = 0;
        int aceCount = 0;
        for (Card card : cards) {
            score += card.getValue();
            if (card.getRank() == Card.Rank.ACE) {
                aceCount++;
            }
        }
        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }
        return score;
    }

    public boolean isBusted() {
        return calculateScore() > 21;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && calculateScore() == 21;
    }

    public void showHand() {
        System.out.print("[");
        for (Card card : cards) {
            System.out.print(card + ", ");
        }
        System.out.println("] > " + calculateScore());
    }

    public void clear() {
        cards.clear();
    }
}
