import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CardView extends JPanel {
    private Card card;
    private boolean faceUp;
    private boolean isPlayable;

    // Constants for card dimensions
    private static final int CARD_WIDTH = 100;
    private static final int CARD_HEIGHT = 150;
    private static final int CORNER_RADIUS = 15;

    // Card colors
    private static final Color RED_COLOR = new Color(220, 53, 69);
    private static final Color GREEN_COLOR = new Color(40, 167, 69);
    private static final Color BLUE_COLOR = new Color(0, 123, 255);
    private static final Color YELLOW_COLOR = new Color(255, 193, 7);
    private static final Color CARD_BACK_COLOR = new Color(25, 25, 112);

    public CardView(Card card) {
        this(card, true);
    }

    public CardView(Card card, boolean faceUp) {
        this.card = card;
        this.faceUp = faceUp;
        this.isPlayable = false;

        setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        setMaximumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        setMinimumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));

        // Set tooltip to display card info
        updateTooltip();
    }

    private void updateTooltip() {
        if (card != null) {
            setToolTipText(card.toString());
        } else {
            setToolTipText("Deck");
        }
    }

    public void setCard(Card card) {
        this.card = card;
        updateTooltip();
        repaint();
    }

    public Card getCard() {
        return card;
    }

    public void setPlayable(boolean playable) {
        this.isPlayable = playable;
        repaint();
    }

    public boolean isPlayable() {
        return isPlayable;
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
        repaint();
    }

    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smoother edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw card outline
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(
                2, 2, getWidth() - 4, getHeight() - 4, CORNER_RADIUS, CORNER_RADIUS);

        // Draw card back if face down or card front if face up
        if (!faceUp || card == null) {
            drawCardBack(g2d, roundedRectangle);
        } else {
            drawCardFront(g2d, roundedRectangle);
        }

        // Draw highlight if card is playable
        if (isPlayable && faceUp) {
            g2d.setColor(new Color(255, 255, 0, 80));
            g2d.setStroke(new BasicStroke(4));
            g2d.draw(roundedRectangle);
        }
    }

    private void drawCardBack(Graphics2D g2d, RoundRectangle2D shape) {
        // Fill card back
        g2d.setColor(CARD_BACK_COLOR);
        g2d.fill(shape);

        // Draw pattern
        g2d.setColor(new Color(255, 255, 255, 50));
        for (int i = 0; i < getHeight(); i += 10) {
            g2d.drawLine(0, i, getWidth(), i);
        }

        // Draw "ZWEI" text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "ZWEI";
        int textX = (getWidth() - fm.stringWidth(text)) / 2;
        int textY = getHeight() / 2;
        g2d.drawString(text, textX, textY);
    }

    private void drawCardFront(Graphics2D g2d, RoundRectangle2D shape) {
        // Determine card color
        Color cardColor = Color.WHITE;
        String colorCode = card.getColor();

        switch (colorCode) {
            case "R":
                cardColor = RED_COLOR;
                break;
            case "G":
                cardColor = GREEN_COLOR;
                break;
            case "B":
                cardColor = BLUE_COLOR;
                break;
            case "Y":
                cardColor = YELLOW_COLOR;
                break;
        }

        // Fill card background
        g2d.setColor(cardColor);
        g2d.fill(shape);

        // Draw white oval in center
        g2d.setColor(Color.WHITE);
        int ovalWidth = getWidth() - 20;
        int ovalHeight = getHeight() - 40;
        g2d.fillOval((getWidth() - ovalWidth) / 2, (getHeight() - ovalHeight) / 2,
                ovalWidth, ovalHeight);

        // Draw card information
        g2d.setColor(cardColor);

        // Draw number or ability
        String displayText;
        if (!card.getAbility().isEmpty()) {
            displayText = card.getAbility();
        } else {
            displayText = String.valueOf(card.getNumber());
        }

        // Draw main text
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(displayText)) / 2;
        int textY = getHeight() / 2 + fm.getAscent() / 2;
        g2d.drawString(displayText, textX, textY);

        // Draw small indicators in corners
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        fm = g2d.getFontMetrics();

        // Top-left corner
        g2d.drawString(displayText, 5, 5 + fm.getAscent());

        // Bottom-right corner
        int smallTextWidth = fm.stringWidth(displayText);
        g2d.drawString(displayText, getWidth() - smallTextWidth - 5, getHeight() - 7);
    }
}
