package ru.nsu.masolygin;

public class Card {

    public enum Suit {
        HEARTS("Hearts"),
        DIAMONDS("Diamonds"),
        CLUBS("Clubs"),
        SPADES("Spades");

        private final String englishName;

        Suit(String englishName) {
            this.englishName = englishName;
        }

        public String getEnglishName() {
            return englishName;
        }
    }

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

    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public int getValue() {
        return rank.getValue();
    }


    @Override
    public String toString() {
        return rank.getEnglishName() + " of " + suit.getEnglishName() + " (" + getValue() + ")";
    }
}
