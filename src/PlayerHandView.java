import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlayerHandView extends JPanel {
    private Player player;
    private List<CardView> cardViews;
    private boolean isCurrentPlayer;
    private boolean isOpponent;
    private Consumer<Card> cardClickListener;

    // Card layout constants
    private static final int CARD_OVERLAP = 30;
    private static final int VERTICAL_MARGIN = 10;

    public PlayerHandView(Player player, boolean isOpponent) {
        this.player = player;
        this.isOpponent = isOpponent;
        this.isCurrentPlayer = false;
        this.cardViews = new ArrayList<>();

        setOpaque(false);
        setLayout(null); // Allows absolute positioning
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        updateHand();
    }

    public Player getPlayer() {
        return player;
    }

    public void setCurrentPlayer(boolean isCurrentPlayer) {
        this.isCurrentPlayer = isCurrentPlayer;
        updateHand();
        repaint();
    }

    public void addCardClickListener(Consumer<Card> listener) {
        this.cardClickListener = listener;
    }

    public void updateHand() {
        removeAll();
        cardViews.clear();

        List<Card> cards = player.getHand();

        // Create card views
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            CardView cardView = new CardView(card, !isOpponent);

            // Add click listener if not opponent and we have a listener
            if (!isOpponent && cardClickListener != null) {
                final Card cardToPlay = card;
                cardView.addMouseListener(new MouseAdapter() {

                    public void mouseClicked(MouseEvent e) {
                        if (isCurrentPlayer) {
                            cardClickListener.accept(cardToPlay);
                        }
                    }


                    public void mouseEntered(MouseEvent e) {
                        if (isCurrentPlayer && cardView.isPlayable()) {
                            setCursor(new Cursor(Cursor.HAND_CURSOR));
                        }
                    }


                    public void mouseExited(MouseEvent e) {
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                });
            }

            // Calculate position for card
            int x = i * CARD_OVERLAP;
            int y = VERTICAL_MARGIN;

            cardView.setBounds(x, y, cardView.getPreferredSize().width, cardView.getPreferredSize().height);
            add(cardView);
            cardViews.add(cardView);
        }

        // Update panel size based on card count
        int width = cards.isEmpty() ? 120 :
                (cards.size() - 1) * CARD_OVERLAP + 100;
        int height = 150 + (2 * VERTICAL_MARGIN);
        setPreferredSize(new Dimension(width, height));

        revalidate();
        repaint();
    }

    public void updatePlayableCards(Card topCard) {
        if (isOpponent) return; // Don't show playable for opponents

        List<Card> playableCards = player.getPlayableCards(topCard);

        for (CardView cardView : cardViews) {
            cardView.setPlayable(playableCards.contains(cardView.getCard()));
        }

        repaint();
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw a semi-transparent panel background
        if (isCurrentPlayer) {
            g2d.setColor(new Color(255, 255, 255, 50));
        } else {
            g2d.setColor(new Color(0, 0, 0, 30));
        }
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        // Draw a border if this is the current player
        if (isCurrentPlayer) {
            g2d.setColor(new Color(255, 215, 0, 220)); // Gold color
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 15, 15);
        }

        // Draw player name and card count
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        String playerText = player.getName() + " (" + player.handSize() + " cards)";
        g2d.drawString(playerText, 10, getHeight() - 10);
    }
}