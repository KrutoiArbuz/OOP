package ru.nsu.masolygin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DealerTest {

    private Dealer dealer;
    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
        dealer = new Dealer();
    }

    @Test
    void testShouldTakeCardWhenScoreLessThan17() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.TWO);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.THREE);
        dealer.takeCard(card1);
        dealer.takeCard(card2);

        assertTrue(dealer.shouldTakeCard());
    }

    @Test
    void testShouldNotTakeCardWhenScore17OrMore() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.TEN);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.SEVEN);
        dealer.takeCard(card1);
        dealer.takeCard(card2);

        assertFalse(dealer.shouldTakeCard());
    }

    @Test
    void testDealerScoreCalculation() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.TEN);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.KING);
        dealer.takeCard(card1);
        dealer.takeCard(card2);

        assertEquals(20, dealer.getScore());
    }
}
