package ru.nsu.masolygin;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        cards = new LinkedList<>();
        createDeck();
        shuffleDeck();
    }

    private void createDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(cards);
    }

    public Card dealCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Колода пуста");
        }
        return ((LinkedList<Card>) cards).removeLast();
    }

    public int size() {
        return cards.size();
    }
}
