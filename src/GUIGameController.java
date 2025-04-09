import java.util.ArrayList;
import java.util.List;

public class GUIGameController {
    private Deck deck;
    private List<Card> stack;
    private List<Player> players;
    private Card topCard;
    private int currentPlayerIndex;
    private boolean isGameOver;
    private boolean isReversed;

    // Listener interface for game events
    public interface GameEventListener {
        void onGameStateChanged();
        void onPlayerTurnChanged(Player player);
        void onCardPlayed(Player player, Card card);
        void onCardDrawn(Player player, Card card);
        void onSpecialCardEffect(String effect);
        void onGameOver(Player winner);
    }

    private List<GameEventListener> listeners = new ArrayList<>();

    // Constructor
    public GUIGameController() {
        this.deck = new Deck();
        this.stack = new ArrayList<>();
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.isGameOver = false;
        this.isReversed = false;
    }

    // Add/remove listeners
    public void addGameEventListener(GameEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeGameEventListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    // Notify listeners
    private void notifyGameStateChanged() {
        for (GameEventListener listener : listeners) {
            listener.onGameStateChanged();
        }
    }

    private void notifyPlayerTurnChanged(Player player) {
        for (GameEventListener listener : listeners) {
            listener.onPlayerTurnChanged(player);
        }
    }

    private void notifyCardPlayed(Player player, Card card) {
        for (GameEventListener listener : listeners) {
            listener.onCardPlayed(player, card);
        }
    }

    private void notifyCardDrawn(Player player, Card card) {
        for (GameEventListener listener : listeners) {
            listener.onCardDrawn(player, card);
        }
    }

    private void notifySpecialCardEffect(String effect) {
        for (GameEventListener listener : listeners) {
            listener.onSpecialCardEffect(effect);
        }
    }

    private void notifyGameOver(Player winner) {
        for (GameEventListener listener : listeners) {
            listener.onGameOver(winner);
        }
    }

    // Game setup with players
    public void setupGame(List<Player> players) {
        this.players = new ArrayList<>(players);

        // Shuffle and deal
        deck.shuffle();
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

        // Notify listeners about initial state
        notifyGameStateChanged();
        notifyPlayerTurnChanged(getCurrentPlayer());
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

    // Get current player
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    // Get top card
    public Card getTopCard() {
        return topCard;
    }

    // Play a card from the current player's hand
    public boolean playCard(Card card) {
        Player currentPlayer = getCurrentPlayer();

        if (!card.isPlayable(topCard)) {
            return false;
        }

        // Find and remove card from player's hand
        List<Card> hand = currentPlayer.getHand();
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).equalTo(card)) {
                currentPlayer.removeCard(i);
                break;
            }
        }

        // Add the played card to the stack
        stack.add(card);
        topCard = card;

        // Notify about card played
        notifyCardPlayed(currentPlayer, card);

        // Handle special card effects
        handleSpecialCardEffects(card);

        // Check win condition
        if (currentPlayer.hasWon()) {
            isGameOver = true;
            notifyGameOver(currentPlayer);
            return true;
        }

        // Move to next player
        moveToNextPlayer();

        return true;
    }

    // Draw a card for the current player
    public Card drawCard() {
        Player currentPlayer = getCurrentPlayer();

        // Draw a card
        Card drawnCard = getCardFromDeck();
        if (drawnCard == null) {
            return null;
        }

        currentPlayer.addCard(drawnCard);
        notifyCardDrawn(currentPlayer, drawnCard);

        // Check if drawn card can be played
        boolean canPlay = drawnCard.isPlayable(topCard);

        // If can't play, move to next player
        if (!canPlay) {
            moveToNextPlayer();
        }

        return drawnCard;
    }

    // Get a card from the deck, reshuffling if necessary
    private Card getCardFromDeck() {
        if (deck.isEmpty()) {
            if (stack.size() <= 1) {
                // Cannot reshuffle if stack has only the top card
                return null;
            }

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

    // Handle special card effects
    private void handleSpecialCardEffects(Card card) {
        String ability = card.getAbility();

        if (ability.equals("Skip")) {
            notifySpecialCardEffect("Skip");
            moveToNextPlayer(); // Skip the next player
        } else if (ability.equals("Reverse")) {
            notifySpecialCardEffect("Reverse");
            isReversed = !isReversed;
        } else if (ability.equals("Draw2")) {
            notifySpecialCardEffect("Draw2");
            int nextPlayerIndex = getNextPlayerIndex();
            Player nextPlayer = players.get(nextPlayerIndex);

            // Draw 2 cards
            for (int i = 0; i < 2; i++) {
                Card drawnCard = getCardFromDeck();
                if (drawnCard != null) {
                    nextPlayer.addCard(drawnCard);
                }
            }
        }
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
        notifyPlayerTurnChanged(getCurrentPlayer());
    }

    // Play a turn for a computer player
    public void playComputerTurn() {
        Player computer = getCurrentPlayer();

        if (!computer.isComputer()) {
            return;
        }

        // Get playable cards
        List<Card> playableCards = computer.getPlayableCards(topCard);

        if (playableCards.isEmpty()) {
            // No playable cards, draw from deck
            Card drawnCard = drawCard();

            if (drawnCard != null && drawnCard.isPlayable(topCard)) {
                // If drawn card is playable, play it
                playCard(drawnCard);
            }
        } else {
            // Play the first playable card
            Card cardToPlay = playableCards.get(0);
            playCard(cardToPlay);
        }
    }

    // Get the deck
    public Deck getDeck() {
        return deck;
    }

    // Check if game is over
    public boolean isGameOver() {
        return isGameOver;
    }

    // Get all players
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    // Reset the game
    public void resetGame() {
        deck.reset();
        stack.clear();
        currentPlayerIndex = 0;
        isGameOver = false;
        isReversed = false;

        for (Player player : players) {
            player.getHand().clear();
        }
    }
}