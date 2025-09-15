package ru.nsu.masolygin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    @Test
    void testDeckCreation() {
        assertEquals(52, deck.size());
    }

    @Test
    void testShuffleDeck() {
        Deck shuffledDeck = new Deck();
        Deck newDeck = new Deck();
        assertNotEquals(newDeck, shuffledDeck);
    }

    @Test
    void testDealCard() {
        Card card = deck.dealCard();
        assertNotNull(card);
        assertEquals(51, deck.size());
    }

    @Test
    void testDealCardWhenEmpty() {
        while (deck.size() > 0) {
            deck.dealCard();
        }
        assertThrows(IllegalStateException.class, () -> deck.dealCard());
    }
}
