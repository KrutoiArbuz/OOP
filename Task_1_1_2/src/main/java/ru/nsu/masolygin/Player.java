package ru.nsu.masolygin;

public class Player extends Participant {
    public Player(String name) {
        super(name);
    }

    @Override
    public boolean shouldTakeCard(GameConsole console) {
        console.printPlayerTurn(getName());
        int choice = console.getPlayerChoice();
        return choice == 1;
    }

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

    @Override
    protected void onCardDrawn(Card card, GameConsole console) {
        console.printCardDrawn(getName(), card);
    }

    @Override
    public boolean isDealer() {
        return false;
    }
}
