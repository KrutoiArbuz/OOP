package ru.nsu.masolygin;

/**
 * Player in blackjack game.
 */
public class Player extends Participant {
    /**
     * Creates a new player with the name.
     *
     * @param name player name
     */
    public Player(String name) {
        super(name);
    }

    /**
     * Determines if the player wants to take another card.
     *
     * @param console console for player interaction
     * @return true if player wants to take a card
     */
    @Override
    public boolean shouldTakeCard(GameConsole console) {
        console.printPlayerTurn(getName());
        int choice = console.getPlayerChoice();
        if (choice != 1 && choice != 0) {
            console.printIncorrectInput();
            return shouldTakeCard(console);
        }
        return choice == 1;
    }

    /**
     * Executes the player's turn.
     *
     * @param deck deck of cards
     * @param console console for interaction
     */
    @Override
    public void playTurn(Deck deck, GameConsole console) {
        boolean playerStopped = false;
        while (!playerStopped && !isBusted() && !isBlackjack()) {
            if (shouldTakeCard(console)) {
                Card card = deck.dealCard();
                takeCard(card);
                onCardDrawn(card, console);

                console.printPlayerCards(this);
            } else {
                playerStopped = true;
            }
        }

        if (isBusted()) {
            console.printBusted(getName());
        } else if (isBlackjack()) {
            console.printBlackjack(getName());
        }
    }

    /**
     * Handles the player receiving a new card.
     *
     * @param card received card
     * @param console console for output
     */
    @Override
    protected void onCardDrawn(Card card, GameConsole console) {
        console.printCardDrawn(getName(), card);
    }

    /**
     * Checks if the participant is a dealer.
     *
     * @return false, since this is a player
     */
    @Override
    public boolean isDealer() {
        return false;
    }
}
