package ru.nsu.masolygin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HandTest {

    private Hand hand;

    @BeforeEach
    void setUp() {
        hand = new Hand();
    }

    @Test
    void testAddCard() {
        Card card = new Card(Card.Suit.HEARTS, Card.Rank.TEN);
        hand.addCard(card);
        assertEquals(1, hand.getCards().size());
    }

    @Test
    void testCalculateScoreWithNoAces() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.TEN);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.FIVE);
        hand.addCard(card1);
        hand.addCard(card2);
        assertEquals(15, hand.calculateScore());
    }

    @Test
    void testCalculateScoreWithAces() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.ACE);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.TEN);
        hand.addCard(card1);
        hand.addCard(card2);
        assertEquals(21, hand.calculateScore());
    }

    @Test
    void testCalculateScoreWithBust() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.TEN);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.TEN);
        Card card3 = new Card(Card.Suit.DIAMONDS, Card.Rank.TEN);
        hand.addCard(card1);
        hand.addCard(card2);
        hand.addCard(card3);
        assertTrue(hand.isBusted());
    }

    @Test
    void testIsBlackjack() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.ACE);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.KING);
        hand.addCard(card1);
        hand.addCard(card2);
        assertTrue(hand.isBlackjack());
    }

    @Test
    void testIsNotBlackjack() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.ACE);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.TEN);
        Card card3 = new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE);
        hand.addCard(card1);
        hand.addCard(card2);
        hand.addCard(card3);
        assertFalse(hand.isBlackjack());
    }

    @Test
    void testClearHand() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.ACE);
        hand.addCard(card1);
        hand.clear();
        assertTrue(hand.getCards().isEmpty());
    }
}
