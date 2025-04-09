import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GamePlayPanel extends JPanel implements GUIGameController.GameEventListener {
    // Game components
    private GUIGameController gameController;
    private Card topCard;
    private Player currentPlayer;

    // UI components
    private JPanel deckArea;
    private CardView deckView;
    private CardView stackView;
    private JPanel playerArea;
    private JPanel opponentArea;
    private List<PlayerHandView> playerHandViews;
    private JLabel statusLabel;
    private JButton drawButton;
    private Timer computerMoveTimer;

    public GamePlayPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // Initialize component lists
        playerHandViews = new ArrayList<>();
        gameController = new GUIGameController();
        gameController.addGameEventListener(this);

        // Initialize computer move timer
        computerMoveTimer = new Timer(1000, e -> {
            if (currentPlayer != null && currentPlayer.isComputer()) {
                gameController.playComputerTurn();
            }
        });
        computerMoveTimer.setRepeats(false);

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
        opponentArea = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        opponentArea.setOpaque(false);
        centerPanel.add(opponentArea, BorderLayout.NORTH);

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
        opponentArea.removeAll();
        playerHandViews.clear();

        if (players.size() >= 1) {
            // Create main player view
            Player mainPlayer = players.get(0);
            PlayerHandView mainPlayerView = new PlayerHandView(mainPlayer, false);

            // Add click listeners to cards
            mainPlayerView.addCardClickListener(card -> {
                if (currentPlayer == mainPlayer && card.isPlayable(topCard)) {
                    gameController.playCard(card);
                }
            });

            playerHandViews.add(mainPlayerView);
            playerArea.add(mainPlayerView);
        }

        // Add opponent players
        for (int i = 1; i < players.size(); i++) {
            Player opponent = players.get(i);
            PlayerHandView opponentView = new PlayerHandView(opponent, true);
            playerHandViews.add(opponentView);
            opponentArea.add(opponentView);
        }

        // Initialize game with players
        gameController.setupGame(players);

        revalidate();
        repaint();
    }

    // Handle deck click (draw card)
    private void handleDeckClick() {
        if (currentPlayer != null && !currentPlayer.isComputer() &&
                currentPlayer == gameController.getCurrentPlayer()) {
            gameController.drawCard();
        }
    }

    // Update player hand view playable cards
    private void updatePlayableCards() {
        for (PlayerHandView handView : playerHandViews) {
            if (!handView.getPlayer().isComputer()) {
                handView.updatePlayableCards(topCard);
            }
        }
    }

    // GameEventListener implementation
    public void onGameStateChanged() {
        topCard = gameController.getTopCard();

        // Update stack view with top card
        stackView.setCard(topCard);
        stackView.setFaceUp(true);

        // Update all player hands
        for (PlayerHandView handView : playerHandViews) {
            handView.updateHand();
        }

        updatePlayableCards();

        revalidate();
        repaint();
    }


    public void onPlayerTurnChanged(Player player) {
        currentPlayer = player;

        // Update UI to show current player
        for (PlayerHandView handView : playerHandViews) {
            handView.setCurrentPlayer(handView.getPlayer() == currentPlayer);
        }

        // Update status label
        statusLabel.setText(currentPlayer.getName() + "'s turn");

        // Enable/disable draw button based on current player
        boolean isHumanTurn = !currentPlayer.isComputer() &&
                currentPlayer == gameController.getPlayers().get(0);
        drawButton.setEnabled(isHumanTurn);

        // Update playable cards
        updatePlayableCards();

        // If computer's turn, start timer for computer move
        if (currentPlayer.isComputer()) {
            computerMoveTimer.start();
        }

        revalidate();
        repaint();
    }


    public void onCardPlayed(Player player, Card card) {
        // Update status
        statusLabel.setText(player.getName() + " played " + card.toString());
    }


    public void onCardDrawn(Player player, Card card) {
        // Update status
        statusLabel.setText(player.getName() + " drew a card");
    }


    public void onSpecialCardEffect(String effect) {
        // Update status based on effect
        switch (effect) {
            case "Skip":
                statusLabel.setText("⏭️ Skip next player's turn!");
                break;
            case "Reverse":
                statusLabel.setText("🔄 Reverse direction!");
                break;
            case "Draw2":
                statusLabel.setText("➕ Next player must draw 2 cards!");
                break;
        }
    }


    public void onGameOver(Player winner) {
        statusLabel.setText("🎉 " + winner.getName() + " wins! 🎉");
        drawButton.setEnabled(false);
        computerMoveTimer.stop();

        // Show game over dialog
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    winner.getName() + " wins the game!",
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    // Add method to stop timers when panel is hidden
    public void stopTimers() {
        if (computerMoveTimer != null) {
            computerMoveTimer.stop();
        }
    }
}