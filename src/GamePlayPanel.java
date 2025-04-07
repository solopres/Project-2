import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GamePlayPanel extends JPanel {
    // Game components
    private GameController gameController;
    private Card topCard;
    private Player currentPlayer;

    // UI components
    private JPanel deckArea;
    private CardView deckView;
    private CardView stackView;
    private JPanel playerArea;
    private List<PlayerHandView> playerHandViews;
    private JLabel statusLabel;
    private JButton drawButton;

    public GamePlayPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // Initialize component lists
        playerHandViews = new ArrayList<>();

        // Create UI areas
        initGameAreas();
    }

    private void initGameAreas() {
        // Top panel for game information
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);

        statusLabel = new JLabel("Game starting...");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        infoPanel.add(statusLabel, BorderLayout.CENTER);

        // Center panel for opponents, deck, and play area
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        // Opponents area (top)
        JPanel opponentsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        opponentsPanel.setOpaque(false);
        centerPanel.add(opponentsPanel, BorderLayout.NORTH);

        // Deck and stack area (center)
        deckArea = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        deckArea.setOpaque(false);

        // Create deck view
        deckView = new CardView(null, false);
        deckView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleDeckClick();
            }
        });

        // Create stack view
        stackView = new CardView(null, true);

        // Add deck components
        deckArea.add(deckView);
        deckArea.add(stackView);

        // Add draw button
        drawButton = new JButton("Draw Card");
        drawButton.addActionListener(e -> handleDeckClick());
        drawButton.setEnabled(false);
        deckArea.add(drawButton);

        centerPanel.add(deckArea, BorderLayout.CENTER);

        // Player area (bottom)
        playerArea = new JPanel(new FlowLayout(FlowLayout.CENTER));
        playerArea.setOpaque(false);
        centerPanel.add(playerArea, BorderLayout.SOUTH);

        // Combine all panels
        add(infoPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    // Method to initialize the game with players
    public void initializeGame(List<Player> players) {
        // Clear existing player views
        playerArea.removeAll();
        playerHandViews.clear();

        // Create and add player hand views
        PlayerHandView mainPlayerView = new PlayerHandView(players.get(0), false);
        playerHandViews.add(mainPlayerView);
        playerArea.add(mainPlayerView);

        // Add computer/opponent player
        if (players.size() > 1) {
            Player opponent = players.get(1);
            PlayerHandView opponentView = new PlayerHandView(opponent, true);
            playerHandViews.add(opponentView);

            // Add to UI
            JPanel opponentsPanel = (JPanel) ((BorderLayout) ((JPanel) getComponent(1)).getLayout()).getLayoutComponent(BorderLayout.NORTH);
            opponentsPanel.add(opponentView);
        }

        revalidate();
        repaint();
    }

    // Method to update the game state
    public void updateGameState(Card topCard, Player currentPlayer) {
        this.topCard = topCard;
        this.currentPlayer = currentPlayer;

        // Update stack view
        stackView.setFaceUp(true);
        // Implementation for updating actual card to display needs GameController integration

        // Update player hand views
        for (PlayerHandView handView : playerHandViews) {
            handView.updateHand();
            handView.updatePlayableCards(topCard);
        }

        // Update status text
        statusLabel.setText(currentPlayer.getName() + "'s turn");

        // Enable/disable draw button based on current player
        drawButton.setEnabled(currentPlayer == playerHandViews.get(0).getPlayer());

        revalidate();
        repaint();
    }

    private void handleDeckClick() {
        // This would be connected to the GameController
        // For now, it's a placeholder
        statusLabel.setText("Clicked deck - drawing card");
    }

    // Method to handle card selection in player's hand
    public void selectCard(Card card) {
        // This would connect to the GameController to play a card
    }

    // Testing method to simulate a game setup
    public void setupTestGame() {
        // Create test players
        Player player1 = new Player("You", false);
        Player player2 = new Player("Computer", true);

        // Add some test cards
        player1.addCard(new Card("R", 5, ""));
        player1.addCard(new Card("B", 7, ""));
        player1.addCard(new Card("G", -1, "Skip"));
        player1.addCard(new Card("Y", 2, ""));

        player2.addCard(new Card("R", 5, ""));
        player2.addCard(new Card("B", 7, ""));
        player2.addCard(new Card("G", -1, "Skip"));

        // Initialize with test players
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        initializeGame(players);

        // Set top card
        updateGameState(new Card("R", 4, ""), player1);
    }
}