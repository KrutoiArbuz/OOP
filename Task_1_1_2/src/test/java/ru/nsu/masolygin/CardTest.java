package ru.nsu.masolygin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardTest {

    @Test
    void testCardSuit() {
        Card card = new Card(Suit.HEARTS, Rank.TEN);
        assertEquals(Suit.HEARTS, card.getSuit());
    }

    @Test
    void testCardRank() {
        Card card = new Card(Suit.CLUBS, Rank.KING);
        assertEquals(Rank.KING, card.getRank());
    }

    @Test
    void testCardValue() {
        Card card = new Card(Suit.DIAMONDS, Rank.ACE);
        assertEquals(11, card.getValue());

        card = new Card(Suit.SPADES, Rank.TEN);
        assertEquals(10, card.getValue());

        assertEquals(2, new Card(Suit.HEARTS, Rank.TWO).getValue());
        assertEquals(3, new Card(Suit.HEARTS, Rank.THREE).getValue());
        assertEquals(4, new Card(Suit.HEARTS, Rank.FOUR).getValue());
        assertEquals(5, new Card(Suit.HEARTS, Rank.FIVE).getValue());
        assertEquals(6, new Card(Suit.HEARTS, Rank.SIX).getValue());
        assertEquals(7, new Card(Suit.HEARTS, Rank.SEVEN).getValue());
        assertEquals(8, new Card(Suit.HEARTS, Rank.EIGHT).getValue());
        assertEquals(9, new Card(Suit.HEARTS, Rank.NINE).getValue());
        assertEquals(10, new Card(Suit.HEARTS, Rank.JACK).getValue());
        assertEquals(10, new Card(Suit.HEARTS, Rank.QUEEN).getValue());
        assertEquals(10, new Card(Suit.HEARTS, Rank.KING).getValue());
    }

    @Test
    void testCardToString() {
        Card card = new Card(Suit.HEARTS, Rank.JACK);
        String expected = "Jack of Hearts (10)";
        assertEquals(expected, card.toString());

        Card aceCard = new Card(Suit.SPADES, Rank.ACE);
        assertEquals("Ace of Spades (11)", aceCard.toString());

        Card twoCard = new Card(Suit.DIAMONDS, Rank.TWO);
        assertEquals("Two of Diamonds (2)", twoCard.toString());
    }

    @Test
    void testAllSuitNames() {
        assertEquals("Hearts", Suit.HEARTS.getEnglishName());
        assertEquals("Clubs", Suit.CLUBS.getEnglishName());
        assertEquals("Diamonds", Suit.DIAMONDS.getEnglishName());
        assertEquals("Spades", Suit.SPADES.getEnglishName());
    }

    @Test
    void testAllRankNames() {
        assertEquals("Ace", Rank.ACE.getEnglishName());
        assertEquals("Two", Rank.TWO.getEnglishName());
        assertEquals("Three", Rank.THREE.getEnglishName());
        assertEquals("Four", Rank.FOUR.getEnglishName());
        assertEquals("Five", Rank.FIVE.getEnglishName());
        assertEquals("Six", Rank.SIX.getEnglishName());
        assertEquals("Seven", Rank.SEVEN.getEnglishName());
        assertEquals("Eight", Rank.EIGHT.getEnglishName());
        assertEquals("Nine", Rank.NINE.getEnglishName());
        assertEquals("Ten", Rank.TEN.getEnglishName());
        assertEquals("Jack", Rank.JACK.getEnglishName());
        assertEquals("Queen", Rank.QUEEN.getEnglishName());
        assertEquals("King", Rank.KING.getEnglishName());
    }

    @Test
    void testAllRankValues() {
        assertEquals(11, Rank.ACE.getValue());
        assertEquals(2, Rank.TWO.getValue());
        assertEquals(3, Rank.THREE.getValue());
        assertEquals(4, Rank.FOUR.getValue());
        assertEquals(5, Rank.FIVE.getValue());
        assertEquals(6, Rank.SIX.getValue());
        assertEquals(7, Rank.SEVEN.getValue());
        assertEquals(8, Rank.EIGHT.getValue());
        assertEquals(9, Rank.NINE.getValue());
        assertEquals(10, Rank.TEN.getValue());
        assertEquals(10, Rank.JACK.getValue());
        assertEquals(10, Rank.QUEEN.getValue());
        assertEquals(10, Rank.KING.getValue());
    }
}
