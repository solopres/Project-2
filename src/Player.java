import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {
    private String name;
    private List<Card> hand;
    private boolean isComputer;

    // Constructor
    public Player(String name, boolean isComputer) {
        this.name = name;
        this.isComputer = isComputer;
        this.hand = new ArrayList<>();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }

    public boolean isComputer() {
        return isComputer;
    }

    // Hand management methods
    public void addCard(Card card) {
        if (card != null) {
            hand.add(card);
        }
    }

    public Card removeCard(int index) {
        if (index >= 0 && index < hand.size()) {
            return hand.remove(index);
        }
        return null;
    }

    public int handSize() {
        return hand.size();
    }

    public boolean hasWon() {
        return hand.isEmpty();
    }

    // Find playable cards based on top card
    public List<Card> getPlayableCards(Card topCard) {
        List<Card> playableCards = new ArrayList<>();

        for (Card card : hand) {
            if (card.isPlayable(topCard)) {
                playableCards.add(card);
            }
        }

        return playableCards;
    }

    // Play a card (for human players)
    public Card playCard(Card topCard, Scanner scanner) {
        List<Card> playableCards = getPlayableCards(topCard);

        if (playableCards.isEmpty()) {
            return null; // No playable cards
        }

        // Display hand and playable cards
        System.out.println("\n" + name + "'s turn");
        System.out.println("Top Card: " + topCard);
        System.out.println("Your Hand:");

        // Create a list of playable card indices for easier validation
        List<Integer> playableIndices = new ArrayList<>();

        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            System.out.print((i + 1) + ". " + card);
            if (playableCards.contains(card)) {
                System.out.print(" [PLAYABLE]");
                playableIndices.add(i);
            }
            System.out.println();
        }

        // Show instructions more clearly
        System.out.println("\nEnter the NUMBER (1-" + hand.size() + ") of the card you want to play.");
        System.out.println("You can only play cards marked [PLAYABLE].");

        // Get player's choice
        int choice = -1;
        boolean validChoice = false;

        while (!validChoice) {
            System.out.print("Your choice: ");
            try {
                String input = scanner.nextLine().trim();
                choice = Integer.parseInt(input) - 1; // Convert to 0-based index

                if (playableIndices.contains(choice)) {
                    validChoice = true;
                } else {
                    System.out.println("Card #" + (choice + 1) + " is not playable. Choose a card marked [PLAYABLE].");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter only the number of the card (1-" + hand.size() + ").");
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }

        return removeCard(choice);
    }

    // Play a card (for computer players)
    public Card playComputerCard(Card topCard) {
        List<Card> playableCards = getPlayableCards(topCard);

        if (playableCards.isEmpty()) {
            return null; // No playable cards
        }

        // Computer strategy: play the first playable card
        Card cardToPlay = playableCards.get(0);
        int cardIndex = hand.indexOf(cardToPlay);

        System.out.println("\n" + name + " plays: " + cardToPlay);

        return removeCard(cardIndex);
    }

    // toString method
    public String toString() {
        StringBuilder sb = new StringBuilder(name + "'s Hand (" + hand.size() + " cards):\n");
        for (int i = 0; i < hand.size(); i++) {
            sb.append((i + 1)).append(". ").append(hand.get(i)).append("\n");
        }
        return sb.toString();
    }
}