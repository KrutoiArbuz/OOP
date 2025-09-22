package ru.nsu.masolygin;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Handles console output and user interaction.
 */
public class GameConsole {
    private final Scanner scanner;

    /**
     * Creates a new game console.
     */
    public GameConsole() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints welcome message.
     */
    public void printWelcome() {
        System.out.println("Welcome to Blackjack!");
    }

    /**
     * Prints round number.
     *
     * @param round round number
     */
    public void printRound(int round) {
        System.out.println("\nRound " + round);
    }

    /**
     * Prints card dealing message.
     */
    public void printCardsDealt() {
        System.out.println("The dealer dealt the cards");
    }

    /**
     * Prints participant's cards.
     *
     * @param participant game participant
     */
    public void printPlayerCards(Participant participant) {
        System.out.println(participant.getName() + "'s cards: ");
        System.out.println(participant.getHandDisplay());
    }

    /**
     * Prints dealer's initial cards with one face-down card.
     *
     * @param dealer dealer
     */
    public void printDealerInitialCards(Dealer dealer) {
        System.out.println("Dealer's cards: ");
        System.out.println(dealer.getInitialHandDisplay());
    }

    /**
     * Prints game status for all participants.
     *
     * @param participants list of participants
     */
    public void printGameStatus(List<Participant> participants) {
        for (Participant participant : participants) {
            printPlayerCards(participant);
        }
    }

    /**
     * Prints initial game status.
     *
     * @param participants list of participants
     */
    public void printInitialGameStatus(List<Participant> participants) {
        for (Participant participant : participants) {
            if (participant.isDealer()) {
                printDealerInitialCards((Dealer) participant);
            } else {
                printPlayerCards(participant);
            }
        }
    }

    /**
     * Prints player turn information.
     *
     * @param playerName player name
     */
    public void printPlayerTurn(String playerName) {
        System.out.println(playerName + "'s turn");
        System.out.println("-------");
        System.out.println("Enter 1 to take a card, and 0 to stand.");
    }

    /**
     * Prints dealer turn information.
     */
    public void printDealerTurn() {
        System.out.println("Dealer's turn");
        System.out.println("-------");
    }

    /**
     * Prints information about player drawing a card.
     *
     * @param playerName player name
     * @param card drawn card
     */
    public void printCardDrawn(String playerName, Card card) {
        System.out.println(playerName + " drew " + card);
    }

    /**
     * Prints information about dealer revealing a card.
     *
     * @param card revealed card
     */
    public void printDealerReveals(Card card) {
        System.out.println("The dealer reveals " + card);
    }


    /**
     * Prints warning about incorrect input
     */
    public void printIncorrectInput(){
        System.out.println("Incorrect input");
    }

    /**
     * Prints bust message.
     *
     * @param playerName player name
     */
    public void printBusted(String playerName) {
        System.out.println(playerName + " busted!");
    }

    /**
     * Prints blackjack message.
     *
     * @param playerName player name
     */
    public void printBlackjack(String playerName) {
        System.out.println(playerName + " got a blackjack!");
    }

    /**
     * Prints player's score.
     *
     * @param playerName player name
     * @param score player's score
     */
    public void printScore(String playerName, int score) {
        System.out.println(playerName + "'s score: " + score);
    }

    /**
     * Prints winner message.
     *
     * @param winner winner name
     */
    public void printWinner(String winner) {
        System.out.println(winner + " won the round!");
    }

    /**
     * Prints loser message.
     *
     * @param loser loser name
     */
    public void printLoser(String loser) {
        System.out.println(loser + " lost the round!");
    }

    /**
     * Prints tie message.
     */
    public void printTie() {
        System.out.println("It's a tie!");
    }

    /**
     * Gets player's choice.
     *
     * @return player's choice (1 - take card, 0 - stand)
     */
    public int getPlayerChoice() {
        try {
            int value = scanner.nextInt();
            if (value != 0 && value != 1) {
                printIncorrectInput();
                return getPlayerChoice();
            }
            return value;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            printIncorrectInput();
            return getPlayerChoice();
        }
    }

    /**
     * Closes the scanner.
     */
    public void close() {
        scanner.close();
    }
}
