import java.util.Scanner;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Check if we should run in GUI or console mode
        if (args.length > 0 && args[0].equalsIgnoreCase("console")) {
            runConsoleMode();
        } else {
            runGUIMode();
        }
    }

    private static void runConsoleMode() {
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

    private static void runGUIMode() {
        SwingUtilities.invokeLater(() -> {
            ZweiGUI gui = new ZweiGUI();
            gui.setVisible(true);
        });
    }
}