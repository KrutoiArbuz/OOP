package ru.nsu.masolygin;

/**
 * Playing card suits.
 */
public enum Suit {
    /**
     * Hearts suit.
     */
    HEARTS("Hearts"),
    /**
     * Diamonds suit.
     */
    DIAMONDS("Diamonds"),
    /**
     * Clubs suit.
     */
    CLUBS("Clubs"),
    /**
     * Spades suit.
     */
    SPADES("Spades");

    private final String englishName;

    /**
     * Creates a suit with English name.
     *
     * @param englishName English name of the suit
     */
    Suit(String englishName) {
        this.englishName = englishName;
    }

    /**
     * Returns the English name of the suit.
     *
     * @return English name
     */
    public String getEnglishName() {
        return englishName;
    }
}
