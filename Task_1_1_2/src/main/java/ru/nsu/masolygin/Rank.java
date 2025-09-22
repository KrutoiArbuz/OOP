package ru.nsu.masolygin;

/**
 * Playing card ranks with values.
 */
public enum Rank {


    /**
     * Two rank.
     */
    TWO("Two", 2),
    /**
     * Three rank.
     */
    THREE("Three", 3),
    /**
     * Four rank.
     */
    FOUR("Four", 4),
    /**
     * Five rank.
     */
    FIVE("Five", 5),
    /**
     * Six rank.
     */
    SIX("Six", 6),
    /**
     * Seven rank.
     */
    SEVEN("Seven", 7),
    /**
     * Eight rank.
     */
    EIGHT("Eight", 8),
    /**
     * Nine rank.
     */
    NINE("Nine", 9),
    /**
     * Ten rank.
     */
    TEN("Ten", 10),
    /**
     * Jack rank.
     */
    JACK("Jack", 10),
    /**
     * Queen rank.
     */
    QUEEN("Queen", 10),
    /**
     * King rank.
     */
    KING("King", 10),
    /**
     * Ace rank.
     */
    ACE("Ace", 11);

    private final String englishName;
    private final int value;

    /**
     * Creates a rank with English name and value.
     *
     * @param englishName English name of the rank
     * @param value numerical value of the rank
     */
    Rank(String englishName, int value) {
        this.englishName = englishName;
        this.value = value;
    }

    /**
     * Returns the English name of the rank.
     *
     * @return English name
     */
    public String getEnglishName() {
        return englishName;
    }

    /**
     * Returns numerical value of the rank.
     *
     * @return rank value
     */
    public int getValue() {
        return value;
    }
}
