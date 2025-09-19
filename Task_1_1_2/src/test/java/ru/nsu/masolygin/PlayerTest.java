package ru.nsu.masolygin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;
    private GameConsole console;
    private Deck deck;

    @BeforeEach
    void setUp() {
        player = new Player("Test Player");
        console = new GameConsole();
        deck = new Deck();
    }

    @Test
    void testPlayerConstructor() {
        assertEquals("Test Player", player.getName());
        assertFalse(player.isDealer());
        assertEquals(0, player.getScore());
    }

    @Test
    void testShouldTakeCardWhenUserChoosesOne() {
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        GameConsole testConsole = new GameConsole();

        assertTrue(player.shouldTakeCard(testConsole));
    }

    @Test
    void testShouldNotTakeCardWhenUserChoosesZero() {
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        GameConsole testConsole = new GameConsole();

        assertFalse(player.shouldTakeCard(testConsole));
    }

    @Test
    void testPlayTurnPlayerStands() {
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        GameConsole testConsole = new GameConsole();

        int initialScore = player.getScore();
        player.playTurn(deck, testConsole);

        assertEquals(initialScore, player.getScore());
    }

    @Test
    void testPlayTurnPlayerTakesCard() {
        String input = "1\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        GameConsole testConsole = new GameConsole();

        int initialScore = player.getScore();
        player.playTurn(deck, testConsole);

        assertTrue(player.getScore() > initialScore);
    }

    @Test
    void testOnCardDrawn() {
        Card card = new Card(Suit.HEARTS, Rank.KING);

        assertDoesNotThrow(() -> player.onCardDrawn(card, console));
    }

    @Test
    void testBlackjack() {
        player.takeCard(new Card(Suit.HEARTS, Rank.ACE));
        player.takeCard(new Card(Suit.SPADES, Rank.KING));

        assertTrue(player.isBlackjack());
        assertEquals(21, player.getScore());
    }

    @Test
    void testBusted() {
        player.takeCard(new Card(Suit.HEARTS, Rank.KING));
        player.takeCard(new Card(Suit.SPADES, Rank.QUEEN));
        player.takeCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        assertTrue(player.isBusted());
        assertEquals(25, player.getScore());
    }

    @Test
    void testClearHand() {
        player.takeCard(new Card(Suit.HEARTS, Rank.KING));
        player.takeCard(new Card(Suit.SPADES, Rank.ACE));

        player.clearHand();

        assertEquals(0, player.getScore());
        assertFalse(player.isBlackjack());
        assertFalse(player.isBusted());
    }

    @Test
    void testShowMethods() {
        player.takeCard(new Card(Suit.HEARTS, Rank.KING));
        player.takeCard(new Card(Suit.SPADES, Rank.ACE));

        assertDoesNotThrow(() -> player.getHandDisplay());
        assertDoesNotThrow(() -> player.getInitialHandDisplay());
    }

    @Test
    void testTakeCard() {
        Card card = new Card(Suit.HEARTS, Rank.QUEEN);

        player.takeCard(card);
        assertEquals(10, player.getScore());
        assertFalse(player.isBusted());
        assertFalse(player.isBlackjack());
    }
}
