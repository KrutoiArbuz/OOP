package ru.nsu.masolygin;

/**
 * Abstract class for a blackjack game participant.
 * Contains common functionality for player and dealer.
 */
public abstract class Participant {
    private final Hand hand;
    private final String name;

    /**
     * Creates a new participant with the specified name.
     *
     * @param name participant name
     */
    public Participant(String name) {
        this.name = name;
        this.hand = new Hand();
    }

    /**
     * Returns the participant's name.
     *
     * @return participant name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a card to the participant's hand.
     *
     * @param card card to add
     */
    public void takeCard(Card card) {
        hand.addCard(card);
    }

    /**
     * Returns the participant's current score.
     *
     * @return participant score
     */
    public int getScore() {
        return hand.calculateScore();
    }

    /**
     * Checks if the participant has exceeded 21 points.
     *
     * @return true if score is greater than 21
     */
    public boolean isBusted() {
        return hand.isBusted();
    }

    /**
     * Checks if the participant has blackjack.
     *
     * @return true if participant has blackjack
     */
    public boolean isBlackjack() {
        return hand.isBlackjack();
    }

    /**
     * Returns string representation of the participant's hand.
     *
     * @return hand display
     */
    public String getHandDisplay() {
        return hand.getHandDisplay();
    }

    /**
     * Returns initial hand display for dealer.
     *
     * @return initial hand display
     */
    public String getInitialHandDisplay() {
        return hand.getInitialHandDisplay();
    }

    /**
     * Determines if the participant should take another card.
     *
     * @param console console for interaction
     * @return true if should take card
     */
    public abstract boolean shouldTakeCard(GameConsole console);

    /**
     * Executes the participant's turn.
     *
     * @param deck deck of cards
     * @param console console for interaction
     */
    public abstract void playTurn(Deck deck, GameConsole console);

    /**
     * Handles receiving a new card.
     *
     * @param card received card
     * @param console console for output
     */
    protected abstract void onCardDrawn(Card card, GameConsole console);

    /**
     * Checks if the participant is a dealer.
     *
     * @return true if participant is dealer
     */
    public abstract boolean isDealer();

    /**
     * Clears the participant's hand for a new game.
     */
    public void clearHand() {
        hand.clear();
    }
}
