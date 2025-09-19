package ru.nsu.masolygin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BlackjackTest {

    private Blackjack blackjack;
    private Deck deck;
    private Player player;
    private Dealer dealer;

    @BeforeEach
    void setUp() {
        blackjack = new Blackjack();
        deck = blackjack.getDeck();

        List<Participant> participants = blackjack.getParticipants();
        player = (Player) participants.stream().filter(p -> !p.isDealer()).findFirst().orElse(null);
        dealer = blackjack.getDealer();
    }

    @Test
    void testPlayerBusts() {
        Card card1 = new Card(Suit.HEARTS, Rank.TEN);
        Card card2 = new Card(Suit.CLUBS, Rank.KING);
        Card card3 = new Card(Suit.DIAMONDS, Rank.EIGHT);

        player.takeCard(card1);
        player.takeCard(card2);
        player.takeCard(card3);

        assertTrue(player.isBusted());
    }

    @Test
    void testPlayerBlackjack() {
        Card card1 = new Card(Suit.HEARTS, Rank.ACE);
        Card card2 = new Card(Suit.CLUBS, Rank.KING);

        player.takeCard(card1);
        player.takeCard(card2);

        assertTrue(player.isBlackjack());
    }

    @Test
    void testDealerBusts() {
        Card card1 = new Card(Suit.HEARTS, Rank.TEN);
        Card card2 = new Card(Suit.CLUBS, Rank.TEN);
        Card card3 = new Card(Suit.DIAMONDS, Rank.FIVE);

        dealer.takeCard(card1);
        dealer.takeCard(card2);
        dealer.takeCard(card3);

        assertTrue(dealer.isBusted());
    }

    @Test
    void testDealerDecisionToTakeCard() {
        Card card1 = new Card(Suit.HEARTS, Rank.TEN);
        Card card2 = new Card(Suit.CLUBS, Rank.SIX);
        dealer.takeCard(card1);
        dealer.takeCard(card2);

        GameConsole console = new GameConsole();
        assertTrue(dealer.shouldTakeCard(console));
    }

    @Test
    void testDealerStand() {
        Card card1 = new Card(Suit.HEARTS, Rank.TEN);
        Card card2 = new Card(Suit.CLUBS, Rank.SEVEN);
        dealer.takeCard(card1);
        dealer.takeCard(card2);

        GameConsole console = new GameConsole();
        assertFalse(dealer.shouldTakeCard(console));
    }

    @Test
    void testDetermineWinnerPlayerWins() {
        Card playerCard1 = new Card(Suit.HEARTS, Rank.TEN);
        Card playerCard2 = new Card(Suit.CLUBS, Rank.KING);
        Card dealerCard1 = new Card(Suit.DIAMONDS, Rank.NINE);
        Card dealerCard2 = new Card(Suit.SPADES, Rank.EIGHT);

        player.takeCard(playerCard1);
        player.takeCard(playerCard2);
        dealer.takeCard(dealerCard1);
        dealer.takeCard(dealerCard2);

        blackjack.determineWinner();

        assertEquals(20, player.getScore());
        assertEquals(17, dealer.getScore());
        assertTrue(player.getScore() > dealer.getScore());
    }

    @Test
    void testDetermineWinnerDealerWins() {
        Card playerCard1 = new Card(Suit.HEARTS, Rank.TEN);
        Card playerCard2 = new Card(Suit.CLUBS, Rank.FIVE);
        Card dealerCard1 = new Card(Suit.DIAMONDS, Rank.QUEEN);
        Card dealerCard2 = new Card(Suit.SPADES, Rank.SEVEN);

        player.takeCard(playerCard1);
        player.takeCard(playerCard2);
        dealer.takeCard(dealerCard1);
        dealer.takeCard(dealerCard2);

        blackjack.determineWinner();

        assertEquals(15, player.getScore());
        assertEquals(17, dealer.getScore());
        assertTrue(dealer.getScore() > player.getScore());
    }

    @Test
    void testDetermineWinnerTie() {
        Card playerCard1 = new Card(Suit.HEARTS, Rank.TEN);
        Card playerCard2 = new Card(Suit.CLUBS, Rank.SEVEN);
        Card dealerCard1 = new Card(Suit.DIAMONDS, Rank.NINE);
        Card dealerCard2 = new Card(Suit.SPADES, Rank.EIGHT);

        player.takeCard(playerCard1);
        player.takeCard(playerCard2);
        dealer.takeCard(dealerCard1);
        dealer.takeCard(dealerCard2);

        blackjack.determineWinner();

        assertEquals(17, player.getScore());
        assertEquals(17, dealer.getScore());
        assertEquals(player.getScore(), dealer.getScore());
    }

    @Test
    void testPlayerScoreCalculation() {
        Card card1 = new Card(Suit.HEARTS, Rank.ACE);
        Card card2 = new Card(Suit.CLUBS, Rank.TEN);

        player.takeCard(card1);
        player.takeCard(card2);

        assertEquals(21, player.getScore());
    }

    @Test
    void testDealerScoreCalculation() {
        Card card1 = new Card(Suit.HEARTS, Rank.TEN);
        Card card2 = new Card(Suit.CLUBS, Rank.EIGHT);
        dealer.takeCard(card1);
        dealer.takeCard(card2);

        assertEquals(18, dealer.getScore());
    }

    @Test
    void testDeckDepletion() {
        for (int i = 0; i < 52; i++) {
            deck.dealCard();
        }
        assertThrows(RuntimeException.class, () -> deck.dealCard());
    }

    @Test
    void testDetermineWinnerOutputsDealerBusts() {
        dealer.takeCard(new Card(Suit.HEARTS, Rank.TEN));
        dealer.takeCard(new Card(Suit.CLUBS, Rank.TEN));
        dealer.takeCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        blackjack.determineWinner();

        String output = outContent.toString();
        assertTrue(output.contains("won! The dealer busted."));
    }

    @Test
    void testDetermineWinnerOutputsPlayerBusts() {
        player.takeCard(new Card(Suit.HEARTS, Rank.TEN));
        player.takeCard(new Card(Suit.CLUBS, Rank.TEN));
        player.takeCard(new Card(Suit.DIAMONDS, Rank.TWO));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        blackjack.determineWinner();

        String output = outContent.toString();
        assertTrue(output.contains("lost! You busted."));
    }

    @Test
    void testDetermineWinnerOutputsPlayerWins() {
        player.takeCard(new Card(Suit.HEARTS, Rank.TEN));
        player.takeCard(new Card(Suit.SPADES, Rank.TEN));
        dealer.takeCard(new Card(Suit.CLUBS, Rank.NINE));
        dealer.takeCard(new Card(Suit.DIAMONDS, Rank.EIGHT));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        blackjack.determineWinner();

        String output = outContent.toString();
        assertTrue(output.contains("won the round!"));
    }

    @Test
    void testDetermineWinnerOutputsDealerWins() {
        dealer.takeCard(new Card(Suit.HEARTS, Rank.TEN));
        dealer.takeCard(new Card(Suit.SPADES, Rank.NINE));
        player.takeCard(new Card(Suit.CLUBS, Rank.EIGHT));
        player.takeCard(new Card(Suit.DIAMONDS, Rank.SEVEN));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        blackjack.determineWinner();

        String output = outContent.toString();
        assertTrue(output.contains("lost the round!"));
    }

    @Test
    void testDetermineWinnerOutputsTie() {
        player.takeCard(new Card(Suit.HEARTS, Rank.TEN));
        player.takeCard(new Card(Suit.SPADES, Rank.SEVEN));
        dealer.takeCard(new Card(Suit.CLUBS, Rank.TEN));
        dealer.takeCard(new Card(Suit.DIAMONDS, Rank.SEVEN));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        blackjack.determineWinner();

        String output = outContent.toString();
        assertTrue(output.contains("It's a tie!"));
    }
}
