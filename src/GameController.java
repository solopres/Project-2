import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameController {
    private Deck deck;
    private List<Card> stack;
    private List<Player> players;
    private Card topCard;
    private int currentPlayerIndex;
    private boolean isGameOver;
    private boolean isReversed;
    private Scanner scanner;
    private static final int MESSAGE_DELAY = 700; // Delay in milliseconds

    // Constructor
    public GameController(Scanner scanner) {
        this.scanner = scanner;
        this.deck = new Deck();
        this.stack = new ArrayList<>();
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.isGameOver = false;
        this.isReversed = false;
    }

    // Game setup
    public void setupGame() {
        displayWithDelay("Welcome to Zwei!");
        displayWithDelay("1. Player vs Player");
        displayWithDelay("2. Player vs Computer");

        int choice = getValidInput(1, 2);

        // Add player 1
        System.out.print("Enter Player 1 name: ");
        String p1Name = scanner.nextLine();
        players.add(new Player(p1Name, false));

        // Add player 2
        if (choice == 1) {
            System.out.print("Enter Player 2 name: ");
            String p2Name = scanner.nextLine();
            players.add(new Player(p2Name, false));
        } else {
            players.add(new Player("Computer", true));
        }

        // Shuffle and deal
        displayWithDelay("Shuffling deck...");
        deck.shuffle();
        displayWithDelay("Dealing initial cards...");
        dealInitialCards();

        // Start the game with a card from deck
        topCard = deck.deal();
        while (topCard.getAbility().contains("Skip") || topCard.getAbility().contains("Reverse") ||
                topCard.getAbility().contains("Draw")) {
            // If the first card is an action card, put it back and draw again
            deck.addCard(topCard);
            deck.shuffle();
            topCard = deck.deal();
        }
        stack.add(topCard);

        displayWithDelay("\nGame starts with: " + topCard);
    }

    // Deal initial cards
    private void dealInitialCards() {
        // Deal 7 cards to each player
        for (int i = 0; i < 7; i++) {
            for (Player player : players) {
                player.addCard(deck.deal());
            }
        }
    }

    // Main game loop
    public void playGame() {
        setupGame();

        while (!isGameOver) {
            Player currentPlayer = players.get(currentPlayerIndex);
            displayWithDelay("\n" + currentPlayer.getName() + "'s turn");

            // Handle player's turn
            handlePlayerTurn(currentPlayer);

            // Check win condition
            if (currentPlayer.hasWon()) {
                displayWithDelay("\n🎉 " + currentPlayer.getName() + " wins! 🎉");
                isGameOver = true;
            } else {
                // Move to next player
                moveToNextPlayer();
            }
        }

        displayWithDelay("\nGame Over!");
    }

    // Handle a player's turn
    private void handlePlayerTurn(Player player) {
        boolean hasPlayed = false;

        do {
            // Get playable cards
            List<Card> playableCards = player.getPlayableCards(topCard);

            if (playableCards.isEmpty()) {
                displayWithDelay(player.getName() + " has no playable cards. Drawing from deck...");
                Card drawnCard = drawCard();

                if (drawnCard == null) {
                    displayWithDelay("Deck is empty and cannot be replenished. Skipping turn.");
                    return;
                }

                player.addCard(drawnCard);
                displayWithDelay(player.getName() + " drew: " + drawnCard);

                // Check if the drawn card can be played
                if (drawnCard.isPlayable(topCard)) {
                    displayWithDelay("The drawn card can be played!");
                    playableCards = player.getPlayableCards(topCard);
                }
            }

            // If player can play after drawing
            if (!playableCards.isEmpty()) {
                Card playedCard;

                if (player.isComputer()) {
                    // Add a delay before computer plays
                    sleep(1000);
                    playedCard = player.playComputerCard(topCard);
                } else {
                    playedCard = player.playCard(topCard, scanner);
                }

                if (playedCard != null) {
                    // Add the played card to the stack
                    stack.add(playedCard);
                    topCard = playedCard;
                    displayWithDelay(player.getName() + " played: " + playedCard);

                    // Handle special card effects
                    handleSpecialCardEffects(playedCard);

                    hasPlayed = true;
                }
            } else {
                displayWithDelay(player.getName() + " still has no playable cards. Skipping turn.");
                hasPlayed = true;
            }

        } while (!hasPlayed);
    }

    // Handle special card effects
    private void handleSpecialCardEffects(Card card) {
        String ability = card.getAbility();

        if (ability.equals("Skip")) {
            displayWithDelay("⏭️ Skip next player's turn!");
            moveToNextPlayer(); // Skip the next player
        } else if (ability.equals("Reverse")) {
            displayWithDelay("🔄 Reverse direction!");
            isReversed = !isReversed;
        } else if (ability.equals("Draw2")) {
            int nextPlayerIndex = getNextPlayerIndex();
            Player nextPlayer = players.get(nextPlayerIndex);
            displayWithDelay("➕ " + nextPlayer.getName() + " must draw 2 cards!");

            // Draw 2 cards
            for (int i = 0; i < 2; i++) {
                Card drawnCard = drawCard();
                if (drawnCard != null) {
                    nextPlayer.addCard(drawnCard);
                }
            }
        }
    }

    // Draw a card from deck, reshuffle stack if needed
    private Card drawCard() {
        if (deck.isEmpty()) {
            if (stack.size() <= 1) {
                // Cannot reshuffle if stack has only the top card
                return null;
            }

            displayWithDelay("Deck is empty. Reshuffling played cards...");

            // Keep the top card
            Card lastPlayedCard = stack.remove(stack.size() - 1);

            // Add the rest of the stack back to the deck
            deck.addAllCards(stack);
            stack.clear();

            // Keep the last played card in the stack
            stack.add(lastPlayedCard);

            // Shuffle the deck
            deck.shuffle();
        }

        return deck.deal();
    }

    // Get next player index based on direction
    private int getNextPlayerIndex() {
        int nextIndex;

        if (!isReversed) {
            nextIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            nextIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }

        return nextIndex;
    }

    // Move to the next player
    private void moveToNextPlayer() {
        currentPlayerIndex = getNextPlayerIndex();
    }

    // Get valid input within a range
    private int getValidInput(int min, int max) {
        int choice = -1;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Enter your choice (" + min + "-" + max + "): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    validInput = true;
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        return choice;
    }

    // Helper method to display text with delay
    private void displayWithDelay(String message) {
        System.out.println(message);
        sleep(MESSAGE_DELAY);
    }

    // Helper method to sleep
    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}