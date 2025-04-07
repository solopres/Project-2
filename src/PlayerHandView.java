import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerHandView extends JPanel {
    private Player player;
    private List<CardView> cardViews;
    private boolean isCurrentPlayer;
    private boolean isOpponent;

    public Player getPlayer() {
        return player;
    }

    // Card layout constants
    private static final int CARD_OVERLAP = 30;
    private static final int VERTICAL_MARGIN = 10;

    public PlayerHandView(Player player, boolean isOpponent) {
        this.player = player;
        this.isOpponent = isOpponent;
        this.isCurrentPlayer = false;
        this.cardViews = new ArrayList<>();

        setOpaque(false);
        setLayout(null);
        updateHand();
    }

    public void setCurrentPlayer(boolean isCurrentPlayer) {
        this.isCurrentPlayer = isCurrentPlayer;
        updateHand();
        repaint();
    }

    public void updateHand() {
        removeAll();
        cardViews.clear();

        List<Card> cards = player.getHand();

        // Create card views
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            CardView cardView = new CardView(card, !isOpponent);

            // Calculate position for card
            int x = i * CARD_OVERLAP;
            int y = VERTICAL_MARGIN;

            cardView.setBounds(x, y, cardView.getPreferredSize().width, cardView.getPreferredSize().height);
            add(cardView);
            cardViews.add(cardView);
        }

        // Update panel size
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

        // Draw a border if this is the current player
        if (isCurrentPlayer) {
            g2d.setColor(new Color(255, 255, 0, 120));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 15, 15);
        }

        // Draw player name
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        String playerText = player.getName() + " (" + player.handSize() + " cards)";
        g2d.drawString(playerText, 10, getHeight() - 10);
    }
}