package ru.nsu.masolygin;

public enum Rank {
    TWO("Two", 2),
    THREE("Three", 3),
    FOUR("Four", 4),
    FIVE("Five", 5),
    SIX("Six", 6),
    SEVEN("Seven", 7),
    EIGHT("Eight", 8),
    NINE("Nine", 9),
    TEN("Ten", 10),
    JACK("Jack", 10),
    QUEEN("Queen", 10),
    KING("King", 10),
    ACE("Ace", 11);

    private final String englishName;
    private final int value;

    Rank(String englishName, int value) {
        this.englishName = englishName;
        this.value = value;
    }

    public String getEnglishName() {
        return englishName;
    }

    public int getValue() {
        return value;
    }
}
