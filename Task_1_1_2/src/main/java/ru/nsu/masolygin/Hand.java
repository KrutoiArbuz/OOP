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
            if (card.getRank() == Rank.ACE) {
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

    public String getInitialHandDisplay() {
        if (cards.isEmpty()) {
            return "[<no cards>]";
        }
        return "[" + cards.get(0) + ", <face-down card>]";
    }

    public String getHandDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Card card : cards) {
            sb.append(card).append(", ");
        }
        sb.append("] > ").append(calculateScore());
        return sb.toString();
    }

    public void clear() {
        cards.clear();
    }
}
