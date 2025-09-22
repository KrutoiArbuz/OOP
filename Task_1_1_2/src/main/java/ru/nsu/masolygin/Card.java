package ru.nsu.masolygin;

/**
 * Playing card with suit and rank.
 */
public class Card {
    private final Suit suit;
    private final Rank rank;

    /**
     * Creates a new card with suit and rank.
     *
     * @param suit card suit
     * @param rank card rank
     */
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Returns the card suit.
     *
     * @return card suit
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Returns the card rank.
     *
     * @return card rank
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Returns numerical value card.
     *
     * @return card value
     */
    public int getValue() {
        return rank.getValue();
    }

    /**
     * Returns string card.
     *
     * @return card description
     */
    @Override
    public String toString() {
        return rank.getEnglishName() + " of " + suit.getEnglishName() + " (" + getValue() + ")";
    }
}
