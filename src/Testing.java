public class Testing {
    public static void main(String[] args) {
        String name = "joe";
        Player player = new Player("joe", true);
        boolean logic = true;
        int num = 0;
        Card testCard = new Card();
        testCard.setColor("B");
        testCard.setNumber(9);
        player.addCard(testCard);

        System.out.println("Testing player\nIs name joe?");
        System.out.println(returnName(player, name));
        System.out.println("Is player computer?");
        System.out.println(returnComputer(player, logic));
        System.out.println("Is hand size 0?\n" + returnHandSize(player, num));
        System.out.println("Is the card removed of the same value as the testCard variable?");
        System.out.println(testCard == player.removeCard(0));
        System.out.println("Is hand size 0?\n" + returnHandSize(player, num));
        returnCardTests(testCard);
    }
    public static boolean returnName(Player player, String name) {
        return player.getName().equals(name);
    }
    public static boolean returnComputer(Player player, boolean logic) {
        return player.isComputer() == logic;
    }
    public static boolean returnHandSize(Player player, int num) {
        return player.handSize() == num;
    }
    public static void returnCardTests(Card card) {
        System.out.println("\nTesting Card Class");
        System.out.println("Color: blue\n" + card.getColor().equals("B"));
        System.out.println("Number: 9\n" + (card.getNumber() == 9));
        System.out.println("Ability: nothing\n" + card.getAbility().isEmpty());
    }
}
