package ru.nsu.masolygin;

import java.util.Scanner;

public class Blackjack {
    private final Deck deck;
    private final Player player;
    private final Dealer dealer;
    private int round;

    public Blackjack() {
        this.deck = new Deck();
        this.player = new Player("Player");
        this.dealer = new Dealer();
        this.round = 1;
    }

    public void startGame() {
        System.out.println("Welcome to Blackjack!");
        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.println("\nRound " + round);
            deck.shuffleDeck();
            dealInitialCards();

            playerTurn(scanner);
            dealerTurn();
            determineWinner();

            player.clearHand();
            dealer.clearHand();

            round++;
        }
    }

    private void statusGame() {
        System.out.println("Your cards: ");
        player.showHand();
        System.out.println("Dealer's cards: ");
        dealer.showHand();
    }

    private void statusInitialGame() {
        System.out.println("Your cards: ");
        player.showHand();
        System.out.println("Dealer's cards: ");
        dealer.showInitialHand();
    }

    private void dealInitialCards() {
        System.out.println("The dealer dealt the cards");


        player.takeCard(deck.dealCard());
        dealer.takeCard(deck.dealCard());
        player.takeCard(deck.dealCard());
        dealer.takeCard(deck.dealCard());


        statusInitialGame();
    }

    private void playerTurn(Scanner scanner) {
        boolean playerStopped = false;
        while (!playerStopped) {
            System.out.println("Your turn");
            System.out.println("-------");
            System.out.println("Enter 1 to take a card, and 0 to stand.");
            int choice = scanner.nextInt();
            if (choice == 1) {
                Card card = deck.dealCard();
                player.takeCard(card);
                System.out.println("You drew " + card);
                statusInitialGame();
            } else if (choice == 0) {
                playerStopped = true;
            }

            if (player.isBusted()) {
                System.out.println("You busted!");
                break;
            } else if (player.isBlackjack()) {
                System.out.println("You got a blackjack!");
                break;
            }
        }
    }

    private void dealerTurn() {
        System.out.println("Dealer's turn");
        System.out.println("-------");

        while (dealer.shouldTakeCard()) {
            Card card = deck.dealCard();
            dealer.takeCard(card);
            System.out.println("The dealer reveals " + card);
            statusGame();
        }

        if (dealer.isBusted()) {
            System.out.println("The dealer busted!");
        }
    }

    public void determineWinner() {
        System.out.println("Player's score: " + player.getScore());
        System.out.println("Dealer's score: " + dealer.getScore());

        if (dealer.isBusted()) {
            System.out.println("You won the round! The dealer busted.");
        } else if (player.isBusted()) {
            System.out.println("You lost the round! You busted.");
        } else if (player.getScore() > dealer.getScore()) {
            System.out.println("You won the round!");
        } else if (player.getScore() < dealer.getScore()) {
            System.out.println("You lost the round!");
        } else {
            System.out.println("It's a tie!");
        }
    }
    
    public Player getPlayer() {
        return player;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Deck getDeck() {
        return deck;
    }

    public static void main(String[] args) {
        Blackjack blackjack = new Blackjack();
        blackjack.startGame();
    }
}
