package ru.nsu.masolygin;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A deck of playing cards for blackjack game.
 */
public class Deck {
    private final List<Card> cards;

    /**
     * Creates a new deck of 52 cards and shuffles it.
     */
    public Deck() {
        cards = new LinkedList<>();
        createDeck();
        shuffleDeck();
    }

    /**
     * Creates a full deck from all suits and ranks.
     */
    private void createDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    /**
     * Shuffles the deck of cards.
     */
    public void shuffleDeck() {
        Collections.shuffle(cards);
    }

    /**
     * Deals the top card from the deck.
     *
     * @return card from the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card dealCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Колода пуста");
        }
        return ((LinkedList<Card>) cards).removeLast();
    }

    /**
     * Returns the number of remaining cards in the deck.
     *
     * @return number of cards
     */
    public int size() {
        return cards.size();
    }
}
