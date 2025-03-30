import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;

        try {
            while (playAgain) {
                // Create and start game
                GameController game = new GameController(scanner);
                game.playGame();

                // Ask to play again
                System.out.print("\nDo you want to play again? (yes/no): ");
                String response = scanner.nextLine().trim().toLowerCase();
                playAgain = response.equals("yes") || response.equals("y");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }

        System.out.println("Thanks for playing Zwei!");
    }
}