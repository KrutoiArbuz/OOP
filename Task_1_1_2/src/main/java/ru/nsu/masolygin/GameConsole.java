package ru.nsu.masolygin;

import java.util.List;
import java.util.Scanner;

public class GameConsole {
    private final Scanner scanner;

    public GameConsole() {
        this.scanner = new Scanner(System.in);
    }

    public void printWelcome() {
        System.out.println("Welcome to Blackjack!");
    }

    public void printRound(int round) {
        System.out.println("\nRound " + round);
    }

    public void printCardsDealt() {
        System.out.println("The dealer dealt the cards");
    }

    public void printPlayerCards(Participant participant) {
        System.out.println(participant.getName() + "'s cards: ");
        System.out.println(participant.getHandDisplay());
    }

    public void printDealerInitialCards(Dealer dealer) {
        System.out.println("Dealer's cards: ");
        System.out.println(dealer.getInitialHandDisplay());
    }

    public void printGameStatus(List<Participant> participants) {
        for (Participant participant : participants) {
            printPlayerCards(participant);
        }

    }

    public void printInitialGameStatus(List<Participant> participants) {
        for (Participant participant : participants) {
            if (participant.isDealer()) {
                printDealerInitialCards((Dealer) participant);
            } else {
                printPlayerCards(participant);
            }
        }
    }

    public void printPlayerTurn(String playerName) {
        System.out.println(playerName + "'s turn");
        System.out.println("-------");
        System.out.println("Enter 1 to take a card, and 0 to stand.");
    }

    public void printDealerTurn() {
        System.out.println("Dealer's turn");
        System.out.println("-------");
    }

    public void printCardDrawn(String playerName, Card card) {
        System.out.println(playerName + " drew " + card);
    }

    public void printDealerReveals(Card card) {
        System.out.println("The dealer reveals " + card);
    }

    public void printBusted(String playerName) {
        System.out.println(playerName + " busted!");
    }

    public void printBlackjack(String playerName) {
        System.out.println(playerName + " got a blackjack!");
    }

    public void printScore(String playerName, int score) {
        System.out.println(playerName + "'s score: " + score);
    }

    public void printWinner(String winner) {
        System.out.println(winner + " won the round!");
    }

    public void printLoser(String loser) {
        System.out.println(loser + " lost the round!");
    }

    public void printTie() {
        System.out.println("It's a tie!");
    }

    public int getPlayerChoice() {
        return scanner.nextInt();
    }

    public void close() {
        scanner.close();
    }
}
