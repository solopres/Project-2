public class Card {
    // Instance variables (private)
    private String color;
    private int number;
    private String ability;

    // Default Constructor
    public Card() {
        this("", -1, "");
    }

    // Constructor
    public Card(String color, int number, String ability) {
        setColor(color);
        setNumber(number);
        setAbility(ability);
    }

    // Getters
    public String getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }

    public String getAbility() {
        return ability;
    }

    // Setters
    public void setColor(String color) {
        this.color = color == null ? "" : color;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setAbility(String ability) {
        this.ability = ability == null ? "" : ability;
    }

    // Helper method to check if card is playable - updated to use single return statement
    public boolean isPlayable(Card topCard) {
        boolean isPlayable = false;

        // Check if colors match
        if (this.color.equals(topCard.getColor())) {
            isPlayable = true;
        }
        // Check if numbers match
        else if (this.number != -1 && this.number == topCard.getNumber()) {
            isPlayable = true;
        }
        // Check if abilities match
        else if (!this.ability.isEmpty() && this.ability.equals(topCard.getAbility())) {
            isPlayable = true;
        }

        return isPlayable;
    }

    // Equals method for comparing cards
    public boolean equalTo(Card other) {
        if (other == null) return false;
        return this.color.equals(other.color) &&
                this.number == other.number &&
                this.ability.equals(other.ability);
    }

    // toString method for printing card details
    public String toString() {
        if (ability.isEmpty()) {
            return color + "-" + number;
        } else {
            return color + "-" + (number == -1 ? ability : number + "-" + ability);
        }
    }
}