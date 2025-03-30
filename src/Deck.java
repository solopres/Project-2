import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Deck {
    private List<Card> deckList;
    private String cardsFilePath;

    // Constructor with default file path
    public Deck() {
        this("cards.txt");
    }

    // Constructor with specified file path
    public Deck(String cardsFilePath) {
        this.cardsFilePath = cardsFilePath;
        deckList = new ArrayList<>();
        try {
            createDeckFromFile();
        } catch (FileNotFoundException e) {
            System.out.println("Cards file not found. Creating default deck.");
            createDefaultDeck();
        }
    }

    // Helper  
    private void createDeckFromFile() throws FileNotFoundException {
        File cardsFile = new File(cardsFilePath);
        Scanner scanner = new Scanner(cardsFile);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                String[] cardInfo = line.split(",");
                if (cardInfo.length >= 2) {
                    String color = cardInfo[0].trim();
                    String numberOrAbility = cardInfo[1].trim();

                    try {
                         
                        int number = Integer.parseInt(numberOrAbility);
                        String ability = cardInfo.length > 2 ? cardInfo[2].trim() : "";
                        deckList.add(new Card(color, number, ability));
                    } catch (NumberFormatException e) {
                        // If not a number, treat as ability
                        deckList.add(new Card(color, -1, numberOrAbility));
                    }
                }
            }
        }
        scanner.close();

        if (deckList.isEmpty()) {
            createDefaultDeck();
        }
    }

    // Helper method to create default deck
    private void createDefaultDeck() {
        String[] colors = {"R", "G", "B", "Y"};
        String[] abilities = {"Skip", "Reverse", "Draw2"};

        // Add cards for each color
        for (String color : colors) {
            deckList.add(new Card(color, 0, ""));

            // Add number cards 1-9
            for (int i = 1; i <= 9; i++) {
                deckList.add(new Card(color, i, ""));
                deckList.add(new Card(color, i, ""));
            }

            // Add special ability cards
            for (String ability : abilities) {
                deckList.add(new Card(color, -1, ability));
                deckList.add(new Card(color, -1, ability));
            }
        }
    }

    // Getters
    public List<Card> getDeckList() {
        return new ArrayList<>(deckList);
    }

    // Helper methods
    public void shuffle() {
        Collections.shuffle(deckList);
    }

    public Card deal() {
        if (isEmpty()) {
            System.out.println("Deck is empty!");
            return null;
        }
        return deckList.remove(deckList.size() - 1);
    }

    public void addCard(Card card) {
        if (card != null) {
            deckList.add(card);
        }
    }

    public void addAllCards(List<Card> cards) {
        if (cards != null) {
            deckList.addAll(cards);
        }
    }

    public boolean isEmpty() {
        return deckList.isEmpty();
    }

    public int size() {
        return deckList.size();
    }

    public void reset() {
        deckList.clear();
        try {
            createDeckFromFile();
        } catch (FileNotFoundException e) {
            createDefaultDeck();
        }
        shuffle();
    }

    // toString method for printing deck details
    public String toString() {
        StringBuilder sb = new StringBuilder("Deck Contents (" + deckList.size() + " cards):\n");
        for (Card card : deckList) {
            sb.append(card).append("\n");
        }
        return sb.toString();
    }
}
