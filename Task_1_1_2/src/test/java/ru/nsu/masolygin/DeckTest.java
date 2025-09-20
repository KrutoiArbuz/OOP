package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    void testDeckConsistency() {
        int[] suitCounts = new int[4];

        for (int i = 0; i < 52; i++) {
            Card card = deck.dealCard();
            Suit suit = card.getSuit();

            switch (suit) {
                case HEARTS:
                    suitCounts[0]++;
                    break;
                case CLUBS:
                    suitCounts[1]++;
                    break;
                case DIAMONDS:
                    suitCounts[2]++;
                    break;
                case SPADES:
                    suitCounts[3]++;
                    break;
                default:
                    break;
            }
        }

        for (int count : suitCounts) {
            assertEquals(13, count);
        }
    }
}
