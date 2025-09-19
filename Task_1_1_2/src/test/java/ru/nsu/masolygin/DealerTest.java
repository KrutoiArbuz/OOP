package ru.nsu.masolygin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DealerTest {
    private Dealer dealer;
    private GameConsole console;
    private Deck deck;

    @BeforeEach
    void setUp() {
        dealer = new Dealer();
        console = new GameConsole();
        deck = new Deck();
    }

    @Test
    void testDealerConstructor() {
        assertEquals("Dealer", dealer.getName());
        assertTrue(dealer.isDealer());
        assertEquals(0, dealer.getScore());
    }

    @Test
    void testShouldTakeCardWhenScoreLessThan17() {
        dealer.takeCard(new Card(Suit.HEARTS, Rank.TEN));
        dealer.takeCard(new Card(Suit.SPADES, Rank.FIVE));

        assertEquals(15, dealer.getScore());
        assertTrue(dealer.shouldTakeCard(console));
    }

    @Test
    void testShouldNotTakeCardWhenScore17() {
        dealer.takeCard(new Card(Suit.HEARTS, Rank.TEN));
        dealer.takeCard(new Card(Suit.SPADES, Rank.SEVEN));

        assertEquals(17, dealer.getScore());
        assertFalse(dealer.shouldTakeCard(console));
    }

    @Test
    void testShouldNotTakeCardWhenScoreGreaterThan17() {
        dealer.takeCard(new Card(Suit.HEARTS, Rank.TEN));
        dealer.takeCard(new Card(Suit.SPADES, Rank.NINE));

        assertEquals(19, dealer.getScore());
        assertFalse(dealer.shouldTakeCard(console));
    }

    @Test
    void testPlayTurnWhenDealerStands() {
        dealer.takeCard(new Card(Suit.HEARTS, Rank.TEN));
        dealer.takeCard(new Card(Suit.SPADES, Rank.EIGHT));

        int initialScore = dealer.getScore();
        dealer.playTurn(deck, console);

        assertEquals(initialScore, dealer.getScore());
    }

    @Test
    void testPlayTurnWhenDealerTakesCards() {
        dealer.takeCard(new Card(Suit.HEARTS, Rank.FIVE));
        dealer.takeCard(new Card(Suit.SPADES, Rank.FIVE));

        int initialScore = dealer.getScore();
        dealer.playTurn(deck, console);

        assertTrue(dealer.getScore() >= 17 || dealer.isBusted());
        assertTrue(dealer.getScore() > initialScore);
    }

    @Test
    void testOnCardDrawn() {
        Card card = new Card(Suit.HEARTS, Rank.KING);

        assertDoesNotThrow(() -> dealer.onCardDrawn(card, console));
    }

    @Test
    void testBlackjack() {
        dealer.takeCard(new Card(Suit.HEARTS, Rank.ACE));
        dealer.takeCard(new Card(Suit.SPADES, Rank.KING));

        assertTrue(dealer.isBlackjack());
        assertEquals(21, dealer.getScore());
    }

    @Test
    void testBusted() {
        dealer.takeCard(new Card(Suit.HEARTS, Rank.KING));
        dealer.takeCard(new Card(Suit.SPADES, Rank.QUEEN));
        dealer.takeCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        assertTrue(dealer.isBusted());
        assertEquals(25, dealer.getScore());
    }

    @Test
    void testClearHand() {
        dealer.takeCard(new Card(Suit.HEARTS, Rank.KING));
        dealer.takeCard(new Card(Suit.SPADES, Rank.ACE));

        dealer.clearHand();

        assertEquals(0, dealer.getScore());
        assertFalse(dealer.isBlackjack());
        assertFalse(dealer.isBusted());
    }

    @Test
    void testShowMethods() {
        dealer.takeCard(new Card(Suit.HEARTS, Rank.KING));
        dealer.takeCard(new Card(Suit.SPADES, Rank.ACE));

        assertDoesNotThrow(() -> dealer.getHandDisplay());
        assertDoesNotThrow(() -> dealer.getInitialHandDisplay());
    }
}
