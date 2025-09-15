package ru.nsu.masolygin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {

    private Participant player;

    @BeforeEach
    void setUp() {
        player = new Participant("Player");
    }

    @Test
    void testGetName() {
        assertEquals("Player", player.getName());
    }


    @Test
    void testGetScore() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.TEN);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.FIVE);
        player.takeCard(card1);
        player.takeCard(card2);
        assertEquals(15, player.getScore());
    }

    @Test
    void testIsBusted() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.TEN);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.TEN);
        Card card3 = new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE);
        player.takeCard(card1);
        player.takeCard(card2);
        player.takeCard(card3);
        assertTrue(player.isBusted());
    }

    @Test
    void testIsBlackjack() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.ACE);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.KING);
        player.takeCard(card1);
        player.takeCard(card2);
        assertTrue(player.isBlackjack());
    }

    @Test
    void testClearHand() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.ACE);
        player.takeCard(card1);
        player.clearHand();
        assertEquals(0, player.getScore());
    }

    @Test
    void testShowInitialHand() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.ACE);
        player.takeCard(card1);
        player.showInitialHand();
    }
}
