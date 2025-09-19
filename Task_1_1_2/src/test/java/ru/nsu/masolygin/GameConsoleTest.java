package ru.nsu.masolygin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameConsoleTest {
    private GameConsole console;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        console = new GameConsole();
    }

    @Test
    void testPrintWelcome() {
        console.printWelcome();

        String output = outputStream.toString();
        assertTrue(output.contains("Welcome to Blackjack!"));
    }

    @Test
    void testPrintRound() {
        console.printRound(5);

        String output = outputStream.toString();
        assertTrue(output.contains("Round 5"));
    }

    @Test
    void testPrintCardsDealt() {
        console.printCardsDealt();

        String output = outputStream.toString();
        assertTrue(output.contains("The dealer dealt the cards"));
    }

    @Test
    void testPrintPlayerCards() {
        Player player = new Player("Test Player");
        player.takeCard(new Card(Suit.HEARTS, Rank.KING));

        console.printPlayerCards(player);

        String output = outputStream.toString();
        assertTrue(output.contains("Test Player's cards:"));
    }

    @Test
    void testPrintDealerInitialCards() {
        Dealer dealer = new Dealer();
        dealer.takeCard(new Card(Suit.HEARTS, Rank.KING));
        dealer.takeCard(new Card(Suit.SPADES, Rank.ACE));

        console.printDealerInitialCards(dealer);

        String output = outputStream.toString();
        assertTrue(output.contains("Dealer's cards:"));
    }

    @Test
    void testPrintGameStatus() {
        Player player = new Player("Player 1");
        Dealer dealer = new Dealer();
        List<Participant> participants = Arrays.asList(player, dealer);

        console.printGameStatus(participants);

        String output = outputStream.toString();
        assertTrue(output.contains("Player 1's cards:"));
        assertTrue(output.contains("Dealer's cards:"));
    }

    @Test
    void testPrintInitialGameStatus() {
        Player player = new Player("Player 1");
        player.takeCard(new Card(Suit.HEARTS, Rank.KING));

        Dealer dealer = new Dealer();
        dealer.takeCard(new Card(Suit.SPADES, Rank.ACE));
        dealer.takeCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        List<Participant> participants = Arrays.asList(player, dealer);

        console.printInitialGameStatus(participants);

        String output = outputStream.toString();
        assertTrue(output.contains("Player 1's cards:"));
    }

    @Test
    void testPrintPlayerTurn() {
        console.printPlayerTurn("Test Player");

        String output = outputStream.toString();
        assertTrue(output.contains("Test Player's turn"));
        assertTrue(output.contains("Enter 1 to take a card, and 0 to stand."));
    }

    @Test
    void testPrintDealerTurn() {
        console.printDealerTurn();

        String output = outputStream.toString();
        assertTrue(output.contains("Dealer's turn"));
    }

    @Test
    void testPrintCardDrawn() {
        Card card = new Card(Suit.HEARTS, Rank.KING);

        console.printCardDrawn("Test Player", card);

        String output = outputStream.toString();
        assertTrue(output.contains("Test Player drew King of Hearts (10)"));
    }

    @Test
    void testPrintDealerReveals() {
        Card card = new Card(Suit.SPADES, Rank.ACE);

        console.printDealerReveals(card);

        String output = outputStream.toString();
        assertTrue(output.contains("The dealer reveals Ace of Spades (11)"));
    }

    @Test
    void testPrintBusted() {
        console.printBusted("Test Player");

        String output = outputStream.toString();
        assertTrue(output.contains("Test Player busted!"));
    }

    @Test
    void testPrintBlackjack() {
        console.printBlackjack("Test Player");

        String output = outputStream.toString();
        assertTrue(output.contains("Test Player got a blackjack!"));
    }

    @Test
    void testPrintScore() {
        console.printScore("Test Player", 18);

        String output = outputStream.toString();
        assertTrue(output.contains("Test Player's score: 18"));
    }

    @Test
    void testPrintWinner() {
        console.printWinner("Test Player");

        String output = outputStream.toString();
        assertTrue(output.contains("Test Player won the round!"));
    }

    @Test
    void testPrintLoser() {
        console.printLoser("Test Player");

        String output = outputStream.toString();
        assertTrue(output.contains("Test Player lost the round!"));
    }

    @Test
    void testPrintTie() {
        console.printTie();

        String output = outputStream.toString();
        assertTrue(output.contains("It's a tie!"));
    }

    @Test
    void testGetPlayerChoice() {
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        GameConsole testConsole = new GameConsole();
        int choice = testConsole.getPlayerChoice();

        assertEquals(1, choice);
    }

    @Test
    void testClose() {
        assertDoesNotThrow(() -> console.close());
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
}
