package ru.nsu.masolygin;

public class Dealer extends Participant {
    public Dealer() {
        super("Dealer");
    }

    @Override
    public boolean shouldTakeCard(GameConsole console) {
        return getScore() < 17;
    }

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

    @Override
    protected void onCardDrawn(Card card, GameConsole console) {
        console.printDealerReveals(card);
    }

    @Override
    public boolean isDealer() {
        return true;
    }
}
