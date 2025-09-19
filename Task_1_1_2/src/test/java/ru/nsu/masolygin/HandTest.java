package ru.nsu.masolygin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HandTest {

    private Hand hand;

    @BeforeEach
    void setUp() {
        hand = new Hand();
    }

    @Test
    void testAddCard() {
        Card card = new Card(Suit.HEARTS, Rank.TEN);
        hand.addCard(card);
        assertEquals(1, hand.getCards().size());
    }

    @Test
    void testCalculateScoreWithNoAces() {
        Card card1 = new Card(Suit.HEARTS, Rank.TEN);
        Card card2 = new Card(Suit.CLUBS, Rank.FIVE);
        hand.addCard(card1);
        hand.addCard(card2);
        assertEquals(15, hand.calculateScore());
    }

    @Test
    void testCalculateScoreWithAces() {
        Card card1 = new Card(Suit.HEARTS, Rank.ACE);
        Card card2 = new Card(Suit.CLUBS, Rank.TEN);
        hand.addCard(card1);
        hand.addCard(card2);
        assertEquals(21, hand.calculateScore());
    }

    @Test
    void testCalculateScoreWithMultipleAces() {
        Card card1 = new Card(Suit.HEARTS, Rank.ACE);
        Card card2 = new Card(Suit.CLUBS, Rank.ACE);
        Card card3 = new Card(Suit.DIAMONDS, Rank.NINE);
        hand.addCard(card1);
        hand.addCard(card2);
        hand.addCard(card3);
        assertEquals(21, hand.calculateScore());
    }

    @Test
    void testCalculateScoreWithBust() {
        Card card1 = new Card(Suit.HEARTS, Rank.TEN);
        Card card2 = new Card(Suit.CLUBS, Rank.TEN);
        Card card3 = new Card(Suit.DIAMONDS, Rank.TEN);
        hand.addCard(card1);
        hand.addCard(card2);
        hand.addCard(card3);
        assertTrue(hand.isBusted());
    }

    @Test
    void testCalculateScoreWithAceAndBust() {
        Card card1 = new Card(Suit.HEARTS, Rank.ACE);
        Card card2 = new Card(Suit.CLUBS, Rank.QUEEN);
        Card card3 = new Card(Suit.DIAMONDS, Rank.TEN);
        Card card4 = new Card(Suit.DIAMONDS, Rank.TEN);

        hand.addCard(card1);
        hand.addCard(card2);
        hand.addCard(card3);
        hand.addCard(card4);
        assertTrue(hand.isBusted());
    }

    @Test
    void testIsBlackjack() {
        Card card1 = new Card(Suit.HEARTS, Rank.ACE);
        Card card2 = new Card(Suit.CLUBS, Rank.KING);
        hand.addCard(card1);
        hand.addCard(card2);
        assertTrue(hand.isBlackjack());
    }

    @Test
    void testIsNotBlackjack() {
        Card card1 = new Card(Suit.HEARTS, Rank.ACE);
        Card card2 = new Card(Suit.CLUBS, Rank.TEN);
        Card card3 = new Card(Suit.DIAMONDS, Rank.FIVE);
        hand.addCard(card1);
        hand.addCard(card2);
        hand.addCard(card3);
        assertFalse(hand.isBlackjack());
    }

    @Test
    void testClearHand() {
        Card card1 = new Card(Suit.HEARTS, Rank.ACE);
        hand.addCard(card1);
        hand.clear();
        assertTrue(hand.getCards().isEmpty());
    }

    @Test
    void testShowHand() {
        Card card1 = new Card(Suit.HEARTS, Rank.KING);
        Card card2 = new Card(Suit.SPADES, Rank.ACE);

        hand.addCard(card1);
        hand.addCard(card2);

        assertDoesNotThrow(() -> hand.getHandDisplay());
    }

    @Test
    void testMultipleCards() {
        Card card1 = new Card(Suit.HEARTS, Rank.TWO);
        Card card2 = new Card(Suit.CLUBS, Rank.THREE);
        Card card3 = new Card(Suit.DIAMONDS, Rank.FOUR);
        hand.addCard(card1);
        hand.addCard(card2);
        hand.addCard(card3);
        assertEquals(9, hand.calculateScore());
    }
}
