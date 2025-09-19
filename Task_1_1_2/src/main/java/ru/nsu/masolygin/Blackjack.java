package ru.nsu.masolygin;

import java.util.ArrayList;
import java.util.List;

public class Blackjack {
    private final Deck deck;
    private final List<Participant> participants;
    private final GameConsole console;
    private int round;

    public Blackjack() {
        this.deck = new Deck();
        this.participants = new ArrayList<>();
        this.console = new GameConsole();
        this.round = 1;

        participants.add(new Player("Player 1"));
        participants.add(new Player("Player 2"));
        participants.add(new Dealer());
    }

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

    private void dealInitialCards() {
        console.printCardsDealt();

        for (int i = 0; i < 2; i++) {
            for (Participant participant : participants) {
                participant.takeCard(deck.dealCard());
            }
        }

        console.printInitialGameStatus(participants);
    }

    private void showGameStatus() {
        console.printInitialGameStatus(participants);
    }

    private void showFinalStatus() {
        console.printGameStatus(participants);
    }

    public Dealer getDealer() {
        return (Dealer) participants.stream()
                .filter(Participant::isDealer)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No dealer found"));
    }

    private void clearAllHands() {
        for (Participant participant : participants) {
            participant.clearHand();
        }
    }

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

    public List<Participant> getParticipants() {
        return participants;
    }

    public Deck getDeck() {
        return deck;
    }

    public static void main(String[] args) {
        Blackjack blackjack = new Blackjack();
        blackjack.startGame();
    }
}
