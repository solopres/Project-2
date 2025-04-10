import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

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

    // Card back image
    private static Image cardBackImage = null;
    private static boolean cardBackInitialized = false;

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

        // Ensure we're not transparent
        setOpaque(false);

        // Set tooltip to display card info
        updateTooltip();

        // Create default card back image if not set
        if (!cardBackInitialized) {
            createDefaultCardBackImage();
            cardBackInitialized = true;
        }
    }

    // Method to create default card back image
    private void createDefaultCardBackImage() {
        BufferedImage img = new BufferedImage(CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded rectangle for card shape
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(
                0, 0, CARD_WIDTH, CARD_HEIGHT, CORNER_RADIUS, CORNER_RADIUS);

        // Fill with card back color
        g2d.setColor(CARD_BACK_COLOR);
        g2d.fill(roundedRectangle);

        // Draw pattern
        g2d.setColor(new Color(255, 255, 255, 50));
        for (int i = 0; i < CARD_HEIGHT; i += 10) {
            g2d.drawLine(0, i, CARD_WIDTH, i);
        }

        // Draw border
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(roundedRectangle);

        // Draw "UNO" text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "UNO";
        int textX = (CARD_WIDTH - fm.stringWidth(text)) / 2;
        int textY = CARD_HEIGHT / 2 + fm.getAscent() / 2 - fm.getDescent();
        g2d.drawString(text, textX, textY);

        g2d.dispose();
        cardBackImage = img;
    }

    // Method to set custom card back image
    public static void setCardBackImage(Image image) {
        if (image != null) {
            cardBackImage = image;
            cardBackInitialized = true;
        }
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smoother edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create card shape
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
        // Draw background first to create a solid card
        g2d.setColor(Color.WHITE);
        g2d.fill(shape);

        if (cardBackImage != null) {
            // Clip to card shape
            g2d.clip(shape);

            // Draw the image scaled to fit the card
            g2d.drawImage(cardBackImage, 2, 2, getWidth() - 4, getHeight() - 4, this);

            // Reset clip
            g2d.setClip(null);

            // Draw the border
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.draw(shape);
        } else {
            // Fall back to original drawing method if image is not available
            // Fill card back
            g2d.setColor(CARD_BACK_COLOR);
            g2d.fill(shape);

            // Draw pattern
            g2d.setColor(new Color(255, 255, 255, 50));
            for (int i = 0; i < getHeight(); i += 10) {
                g2d.drawLine(0, i, getWidth(), i);
            }

            // Draw "UNO" text
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 28));
            FontMetrics fm = g2d.getFontMetrics();
            String text = "UNO";
            int textX = (getWidth() - fm.stringWidth(text)) / 2;
            int textY = getHeight() / 2;
            g2d.drawString(text, textX, textY);

            // Draw the border
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.draw(shape);
        }
    }

    private void drawCardFront(Graphics2D g2d, RoundRectangle2D shape) {
        if (card == null) {
            // If no card, draw empty slot
            g2d.setColor(new Color(200, 200, 200, 100));
            g2d.fill(shape);
            g2d.setColor(Color.GRAY);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.draw(shape);
            return;
        }

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

        // Draw the border
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.draw(shape);
    }
}