import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ZweiGUI extends JFrame {
    // Constants for window dimensions
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 700;

    // GUI components
    private JPanel mainPanel;
    private JPanel welcomePanel;
    private JPanel gamePanel;
    private CardLayout cardLayout;

    // Game components
    private GameController gameController;

    public ZweiGUI() {
        // Set up the main frame
        setTitle("Zwei Card Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        // Create card layout for switching between panels
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panels
        initWelcomePanel();
        initGamePanel();

        // Add panels to main panel with card layout
        mainPanel.add(welcomePanel, "Welcome");
        mainPanel.add(gamePanel, "Game");

        // Show welcome panel first
        cardLayout.show(mainPanel, "Welcome");

        // Add main panel to frame
        add(mainPanel);
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
        playButton.addActionListener(e -> cardLayout.show(mainPanel, "Game"));
        exitButton.addActionListener(e -> System.exit(0));

        // Add components to message panel with spacing
        messagePanel.add(Box.createVerticalGlue());
        messagePanel.add(titleLabel);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        messagePanel.add(subtitleLabel);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 50)));
        messagePanel.add(playButton);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        messagePanel.add(exitButton);
        messagePanel.add(Box.createVerticalGlue());

        welcomePanel.add(messagePanel, BorderLayout.CENTER);
    }

    private void initGamePanel() {
        gamePanel = new JPanel() {
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
        gamePanel.setLayout(new BorderLayout());

        // Main game areas
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        // Top area for opponents
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Top opponent (for 2 or 4 players)
        JPanel topOpponentPanel = createCardBackPanel("Back of cards here\n(if 2 or 4 players)", 500, 150);
        topPanel.add(topOpponentPanel, BorderLayout.CENTER);

        // Side panels for 4-player mode
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        JPanel leftOpponentPanel = createCardBackPanel("Back of cards here\n(if 4 players)", 150, 300);
        leftPanel.add(leftOpponentPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        JPanel rightOpponentPanel = createCardBackPanel("Back of cards here\n(if 4 players)", 150, 300);
        rightPanel.add(rightOpponentPanel, BorderLayout.CENTER);

        // Center area for deck and discard pile
        JPanel centerAreaPanel = new JPanel();
        centerAreaPanel.setOpaque(false);
        centerAreaPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));

        // Discard pile (stack)
        JPanel stackPanel = createCardPanel("Stack card goes here", 200, 150);
        centerAreaPanel.add(stackPanel);

        // Player's hand area (bottom)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        JPanel playerPanel = createCardPanel("Your cards", 700, 150);
        bottomPanel.add(playerPanel, BorderLayout.CENTER);

        // Combine all panels in the center layout
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(leftPanel, BorderLayout.WEST);
        centerPanel.add(rightPanel, BorderLayout.EAST);
        centerPanel.add(centerAreaPanel, BorderLayout.CENTER);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Control buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setOpaque(false);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Welcome"));
        controlPanel.add(backButton);

        gamePanel.add(centerPanel, BorderLayout.CENTER);
        gamePanel.add(controlPanel, BorderLayout.SOUTH);
    }

    private JPanel createCardPanel(String text, int width, int height) {
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Make the panel semi-transparent with a border
                g.setColor(new Color(80, 80, 80, 100)); // Semi-transparent gray
                g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                // Draw a border
                g.setColor(Color.WHITE);
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                // Draw centered text
                g.setColor(Color.WHITE);
                FontMetrics fm = g.getFontMetrics();
                String[] lines = text.split("\n");
                int lineHeight = fm.getHeight();
                int y = (getHeight() - (lineHeight * lines.length)) / 2 + fm.getAscent();

                for (String line : lines) {
                    int x = (getWidth() - fm.stringWidth(line)) / 2;
                    g.drawString(line, x, y);
                    y += lineHeight;
                }
            }
        };
        panel.setOpaque(false); // Make panel transparent
        panel.setPreferredSize(new Dimension(width, height));
        return panel;
    }

    private JPanel createCardBackPanel(String text, int width, int height) {
        JPanel panel = createCardPanel(text, width, height);
        return panel;
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