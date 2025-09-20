package ru.nsu.masolygin;

/**
 * Represents a dealer in blackjack game.
 */
public class Dealer extends Participant {
    /**
     * Creates a dealer with name "Dealer".
     */
    public Dealer() {
        super("Dealer");
    }

    /**
     * Determines if the dealer should take another card.
     * Dealer takes cards while score is less than 17.
     *
     * @param console console (not used for dealer)
     * @return true if score is less than 17
     */
    @Override
    public boolean shouldTakeCard(GameConsole console) {
        return getScore() < 17;
    }

    /**
     * Executes the dealer's turn.
     *
     * @param deck deck of cards
     * @param console console for output
     */
    @Override
    public void playTurn(Deck deck, GameConsole console) {
        console.printDealerTurn();

        while (shouldTakeCard(console)) {
            Card card = deck.dealCard();
            takeCard(card);
            onCardDrawn(card, console);
            console.printPlayerCards(this);
        }

        if (isBusted()) {
            console.printBusted(getName());
        }
    }

    /**
     * Handles the dealer receiving a new card.
     *
     * @param card received card
     * @param console console for output
     */
    @Override
    protected void onCardDrawn(Card card, GameConsole console) {
        console.printDealerReveals(card);
    }

    /**
     * Checks if the participant is a dealer.
     *
     * @return true, since this is a dealer
     */
    @Override
    public boolean isDealer() {
        return true;
    }
}
