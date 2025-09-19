package ru.nsu.masolygin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParticipantTest {

    private Player player;
    private Dealer dealer;

    @BeforeEach
    void setUp() {
        player = new Player("Test Player");
        dealer = new Dealer();
    }

    @Test
    void testPlayerConstructor() {
        assertEquals("Test Player", player.getName());
        assertEquals(0, player.getScore());
        assertFalse(player.isBusted());
        assertFalse(player.isBlackjack());
        assertFalse(player.isDealer());
    }

    @Test
    void testDealerConstructor() {
        assertEquals("Dealer", dealer.getName());
        assertEquals(0, dealer.getScore());
        assertFalse(dealer.isBusted());
        assertFalse(dealer.isBlackjack());
        assertTrue(dealer.isDealer());
    }

    @Test
    void testTakeCard() {
        Card card = new Card(Suit.HEARTS, Rank.KING);
        player.takeCard(card);

        assertEquals(10, player.getScore());
        assertFalse(player.isBusted());
        assertFalse(player.isBlackjack());
    }

    @Test
    void testGetScore() {
        assertEquals(0, player.getScore());

        player.takeCard(new Card(Suit.HEARTS, Rank.ACE));
        assertEquals(11, player.getScore());

        player.takeCard(new Card(Suit.SPADES, Rank.NINE));
        assertEquals(20, player.getScore());
    }

    @Test
    void testIsBusted() {
        assertFalse(player.isBusted());

        player.takeCard(new Card(Suit.HEARTS, Rank.KING));
        player.takeCard(new Card(Suit.SPADES, Rank.QUEEN));
        player.takeCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        assertTrue(player.isBusted());
        assertEquals(25, player.getScore());
    }

    @Test
    void testIsBlackjack() {
        assertFalse(player.isBlackjack());

        player.takeCard(new Card(Suit.HEARTS, Rank.ACE));
        player.takeCard(new Card(Suit.SPADES, Rank.KING));

        assertTrue(player.isBlackjack());
        assertEquals(21, player.getScore());
    }

    @Test
    void testClearHand() {
        player.takeCard(new Card(Suit.HEARTS, Rank.KING));
        player.takeCard(new Card(Suit.SPADES, Rank.ACE));

        assertEquals(21, player.getScore());
        assertTrue(player.isBlackjack());

        player.clearHand();

        assertEquals(0, player.getScore());
        assertFalse(player.isBusted());
        assertFalse(player.isBlackjack());
    }

    @Test
    void testShowMethods() {
        player.takeCard(new Card(Suit.HEARTS, Rank.KING));
        player.takeCard(new Card(Suit.SPADES, Rank.ACE));

        assertDoesNotThrow(() -> player.getHandDisplay());
        assertDoesNotThrow(() -> player.getInitialHandDisplay());
    }

    @Test
    void testDealerBehavior() {
        GameConsole console = new GameConsole();

        dealer.takeCard(new Card(Suit.HEARTS, Rank.TEN));
        dealer.takeCard(new Card(Suit.SPADES, Rank.FIVE));
        assertTrue(dealer.shouldTakeCard(console));

        dealer.takeCard(new Card(Suit.DIAMONDS, Rank.TWO));
        assertFalse(dealer.shouldTakeCard(console));
    }

    @Test
    void testPlayerShowInitialHand() {
        dealer.takeCard(new Card(Suit.HEARTS, Rank.KING));
        dealer.takeCard(new Card(Suit.SPADES, Rank.ACE));

        assertDoesNotThrow(() -> dealer.getInitialHandDisplay());
    }
}
