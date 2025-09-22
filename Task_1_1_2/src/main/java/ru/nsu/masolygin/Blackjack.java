package ru.nsu.masolygin;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class for the blackjack game.
 * Manages the game process and interaction between participants.
 */
public class Blackjack {
    private final Deck deck;
    private final List<Participant> participants;
    private final GameConsole console;
    private int round;

    /**
     * Creates a new blackjack game with two players and a dealer.
     */
    public Blackjack() {
        this.deck = new Deck();
        this.participants = new ArrayList<>();
        this.console = new GameConsole();
        this.round = 1;

        participants.add(new Player("Player 1"));
        participants.add(new Player("Player 2"));
        participants.add(new Dealer());
    }

    /**
     * Starts the game and manages the game loop.
     */
    public void startGame() {
        console.printWelcome();

        while (true) {
            console.printRound(round);
            deck.shuffleDeck();
            dealInitialCards();

            for (Participant participant : participants) {
                participant.playTurn(deck, console);
                if (participant.isDealer()) {
                    showFinalStatus();
                } else {
                    showGameStatus();
                }
            }

            determineWinner();
            clearAllHands();
            round++;
        }
    }

    /**
     * Deals initial cards to all participants.
     */
    private void dealInitialCards() {
        console.printCardsDealt();

        for (int i = 0; i < 2; i++) {
            for (Participant participant : participants) {
                participant.takeCard(deck.dealCard());
            }
        }

        console.printInitialGameStatus(participants);
    }

    /**
     * Shows initial game status.
     */
    private void showGameStatus() {
        console.printInitialGameStatus(participants);
    }

    /**
     * Shows final game status.
     */
    private void showFinalStatus() {
        console.printGameStatus(participants);
    }

    /**
     * Returns the dealer from the participants list.
     *
     * @return dealer
     * @throws IllegalStateException if no dealer is found
     */
    public Dealer getDealer() {
        return (Dealer) participants.stream()
                .filter(Participant::isDealer)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No dealer found"));
    }

    /**
     * Clears all participants' hands.
     */
    private void clearAllHands() {
        for (Participant participant : participants) {
            participant.clearHand();
        }
    }

    /**
     * Determines the round winner and displays results.
     */
    public void determineWinner() {
        for (Participant participant : participants) {
            console.printScore(participant.getName(), participant.getScore());
        }

        Dealer dealer = getDealer();

        for (Participant participant : participants) {
            if (participant.isDealer()) {
                continue;
            }

            if (dealer.isBusted()) {
                console.printWinner(participant.getName() + " won! The dealer busted.");
            } else if (participant.isBusted()) {
                console.printLoser(participant.getName() + " lost! You busted.");
            } else if (participant.getScore() > dealer.getScore()) {
                console.printWinner(participant.getName());
            } else if (participant.getScore() < dealer.getScore()) {
                console.printLoser(participant.getName());
            } else {
                console.printTie();
            }
        }
    }

    /**
     * Returns the list of all game participants.
     *
     * @return list of participants
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * Returns the deck of cards.
     *
     * @return deck of cards
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Main method to start the game.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Blackjack blackjack = new Blackjack();
        blackjack.startGame();
    }
}
