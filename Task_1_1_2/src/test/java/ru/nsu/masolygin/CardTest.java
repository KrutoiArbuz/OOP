package ru.nsu.masolygin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardTest {

    @Test
    void testCardSuit() {
        Card card = new Card(Card.Suit.HEARTS, Card.Rank.TEN);
        assertEquals(Card.Suit.HEARTS, card.getSuit());
    }

    @Test
    void testCardRank() {
        Card card = new Card(Card.Suit.CLUBS, Card.Rank.KING);
        assertEquals(Card.Rank.KING, card.getRank());
    }

    @Test
    void testCardValue() {
        Card card = new Card(Card.Suit.DIAMONDS, Card.Rank.ACE);
        assertEquals(11, card.getValue());

        card = new Card(Card.Suit.SPADES, Card.Rank.TEN);
        assertEquals(10, card.getValue());
    }

    @Test
    void testCardToString() {
        Card card = new Card(Card.Suit.HEARTS, Card.Rank.JACK);
        String expected = "Jack of Hearts (10)";
        assertEquals(expected, card.toString());
    }

    @Test
    void testSuitGetEnglishName() {
        assertEquals("Hearts", Card.Suit.HEARTS.getEnglishName());
        assertEquals("Clubs", Card.Suit.CLUBS.getEnglishName());
    }

    @Test
    void testRankGetEnglishName() {
        assertEquals("Ace", Card.Rank.ACE.getEnglishName());
        assertEquals("Ten", Card.Rank.TEN.getEnglishName());
    }

    @Test
    void testRankGetValue() {
        assertEquals(2, Card.Rank.TWO.getValue());
        assertEquals(10, Card.Rank.JACK.getValue());
        assertEquals(11, Card.Rank.ACE.getValue());
    }
}
