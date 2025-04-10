import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ZweiGUI extends JFrame {
    // Constants for window dimensions
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 700;

    // GUI components
    private JPanel mainPanel;
    private JPanel welcomePanel;
    private GamePlayPanel gamePlayPanel;
    private CardLayout cardLayout;

    // Game components
    private ImageIcon logoIcon;

    public ZweiGUI() {
        // Set up the main frame
        setTitle("Zwei Card Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        // Load game logo
        try {
            logoIcon = new ImageIcon(getClass().getResource("/images/zwei_logo.png"));
            if (logoIcon.getIconWidth() == -1) {
                // If image not found, create a default icon
                logoIcon = createDefaultLogo();
            }
        } catch (Exception e) {
            logoIcon = createDefaultLogo();
        }

        // Create card layout for switching between panels
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panels
        initWelcomePanel();
        initGamePanel();

        // Add panels to main panel with card layout
        mainPanel.add(welcomePanel, "Welcome");
        mainPanel.add(gamePlayPanel, "Game");

        // Show welcome panel first
        cardLayout.show(mainPanel, "Welcome");

        // Add main panel to frame
        add(mainPanel);
    }

    private ImageIcon createDefaultLogo() {
        // Create a default logo if image cannot be loaded
        int size = 100;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Draw background
        g2d.setColor(new Color(25, 25, 112)); // Dark blue
        g2d.fillRect(0, 0, size, size);

        // Draw text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "ZWEI";
        int textX = (size - fm.stringWidth(text)) / 2;
        int textY = size / 2 + fm.getAscent() / 2 - fm.getDescent();
        g2d.drawString(text, textX, textY);

        g2d.dispose();
        return new ImageIcon(img);
    }

    private void initWelcomePanel() {
        welcomePanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Create gradient background
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 99, 99),
                        getWidth(), getHeight(), new Color(130, 80, 220));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        welcomePanel.setLayout(new BorderLayout());

        // Create welcome message components
        JPanel messagePanel = new JPanel();
        messagePanel.setOpaque(false);
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Welcome to Zwei!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("The exciting UNO game");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 24));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add logo if available
        if (logoIcon != null) {
            JLabel logoLabel = new JLabel(logoIcon);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            messagePanel.add(Box.createVerticalGlue());
            messagePanel.add(logoLabel);
            messagePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        // Player selection panel
        JPanel playerPanel = new JPanel();
        playerPanel.setOpaque(false);
        playerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel playerLabel = new JLabel("Number of players: ");
        playerLabel.setForeground(Color.WHITE);
        JComboBox<Integer> playerCount = new JComboBox<>(new Integer[]{2, 3, 4});

        playerPanel.add(playerLabel);
        playerPanel.add(playerCount);

        // Create buttons
        JButton playButton = new JButton("Play Game");
        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setMaximumSize(new Dimension(200, 50));

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setMaximumSize(new Dimension(200, 50));

        // Add action listeners to buttons
        playButton.addActionListener(e -> {
            int numPlayers = (Integer) playerCount.getSelectedItem();
            startNewGame(numPlayers);
            cardLayout.show(mainPanel, "Game");
        });

        exitButton.addActionListener(e -> System.exit(0));

        // Add components to message panel with spacing
        messagePanel.add(Box.createVerticalGlue());
        messagePanel.add(titleLabel);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        messagePanel.add(subtitleLabel);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 50)));
        messagePanel.add(playerPanel);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        messagePanel.add(playButton);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        messagePanel.add(exitButton);
        messagePanel.add(Box.createVerticalGlue());

        welcomePanel.add(messagePanel, BorderLayout.CENTER);
    }

    private void initGamePanel() {
        // Use the GamePlayPanel instead of creating placeholders
        gamePlayPanel = new GamePlayPanel();
    }

    private void startNewGame(int numPlayers) {
        // Create players
        List<Player> players = new ArrayList<>();

        // Add human player
        players.add(new Player("You", false));

        // Add computer players
        for (int i = 1; i < numPlayers; i++) {
            players.add(new Player("Computer " + i, true));
        }

        // Initialize the game with players
        gamePlayPanel.initializeGame(players);
    }

    // Method to update card back image in CardView
    public static void setCardBackImage(Image backImage) {
        CardView.setCardBackImage(backImage);
    }

    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and show GUI
        SwingUtilities.invokeLater(() -> {
            ZweiGUI gui = new ZweiGUI();
            gui.setVisible(true);
        });
    }
}