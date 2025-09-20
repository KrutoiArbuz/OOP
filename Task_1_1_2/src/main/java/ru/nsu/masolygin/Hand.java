package ru.nsu.masolygin;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a hand of cards for a game participant.
 */
public class Hand {
    private final List<Card> cards;

    /**
     * Creates a new empty hand.
     */
    public Hand() {
        cards = new LinkedList<>();
    }

    /**
     * Adds a card to the hand.
     *
     * @param card card to add
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Returns the list of cards in the hand.
     *
     * @return list of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Calculates the total score of the hand considering aces.
     *
     * @return hand score
     */
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

    /**
     * Checks if the hand score exceeds 21.
     *
     * @return true if score is greater than 21
     */
    public boolean isBusted() {
        return calculateScore() > 21;
    }

    /**
     * Checks if the hand is a blackjack.
     *
     * @return true if the hand is blackjack
     */
    public boolean isBlackjack() {
        return cards.size() == 2 && calculateScore() == 21;
    }

    /**
     * Returns initial hand display with a face-down card.
     *
     * @return string with first card and face-down card
     */
    public String getInitialHandDisplay() {
        if (cards.isEmpty()) {
            return "[<no cards>]";
        }
        return "[" + cards.get(0) + ", <face-down card>]";
    }

    /**
     * Returns full hand display with score.
     *
     * @return string with cards and score
     */
    public String getHandDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Card card : cards) {
            sb.append(card).append(", ");
        }
        sb.append("] > ").append(calculateScore());
        return sb.toString();
    }

    /**
     * Clears the hand of all cards.
     */
    public void clear() {
        cards.clear();
    }
}
