package ru.nsu.masolygin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BlackjackTest {

    private Blackjack blackjack;
    private Deck deck;
    private Player player;
    private Dealer dealer;

    @BeforeEach
    void setUp() {
        deck = new Deck();
        player = new Player("Player");
        dealer = new Dealer();
        blackjack = new Blackjack();
    }

    @Test
    void testPlayerBusts() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.TEN);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.KING);
        Card card3 = new Card(Card.Suit.DIAMONDS, Card.Rank.EIGHT);

        player.takeCard(card1);
        player.takeCard(card2);
        player.takeCard(card3);

        assertTrue(player.isBusted());
    }

    @Test
    void testPlayerBlackjack() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.ACE);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.KING);

        player.takeCard(card1);
        player.takeCard(card2);

        assertTrue(player.isBlackjack());
    }

    @Test
    void testDealerBusts() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.TEN);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.TEN);
        Card card3 = new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE);

        dealer.takeCard(card1);
        dealer.takeCard(card2);
        dealer.takeCard(card3);

        assertTrue(dealer.isBusted());
    }

    @Test
    void testDealerDecisionToTakeCard() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.TEN);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.SIX);
        dealer.takeCard(card1);
        dealer.takeCard(card2);

        assertTrue(dealer.shouldTakeCard());
    }

    @Test
    void testDealerStand() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.TEN);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.SEVEN);
        dealer.takeCard(card1);
        dealer.takeCard(card2);

        assertFalse(dealer.shouldTakeCard());
    }

    @Test
    void testDetermineWinner() {
        Card playerCard1 = new Card(Card.Suit.HEARTS, Card.Rank.TEN);
        Card playerCard2 = new Card(Card.Suit.CLUBS, Card.Rank.SEVEN);
        Card dealerCard1 = new Card(Card.Suit.DIAMONDS, Card.Rank.NINE);
        Card dealerCard2 = new Card(Card.Suit.SPADES, Card.Rank.EIGHT);

        player.takeCard(playerCard1);
        player.takeCard(playerCard2);
        dealer.takeCard(dealerCard1);
        dealer.takeCard(dealerCard2);

        blackjack.determineWinner();

        assertEquals(player.getScore(), dealer.getScore());
    }

    @Test
    void testPlayerScoreCalculation() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.ACE);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.TEN);

        player.takeCard(card1);
        player.takeCard(card2);

        assertEquals(21, player.getScore());
    }

    @Test
    void testDealerScoreCalculation() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.TEN);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.EIGHT);
        dealer.takeCard(card1);
        dealer.takeCard(card2);

        assertEquals(18, dealer.getScore());
    }
}
