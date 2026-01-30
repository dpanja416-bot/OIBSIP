import java.util.Random;
import java.util.Scanner;
public class GuessTheNumber {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        int totalScore = 0;
        boolean playAgain = true;
        System.out.println("🎮 Welcome to Guess The Number Game!");
        while (playAgain) {
            int numberToGuess = random.nextInt(100) + 1;
            int attemptsLeft = 7;
            boolean isGuessed = false;

            System.out.println("\nI have generated a number between 1 and 100.");
            System.out.println("You have " + attemptsLeft + " attempts to guess it.");

            while (attemptsLeft > 0) {
                System.out.print("\nEnter your guess: ");
                int userGuess = sc.nextInt();
                attemptsLeft--;
                if (userGuess == numberToGuess) {
                    int roundScore = attemptsLeft * 10;
                    totalScore += roundScore;
                    System.out.println("🎉 Correct! You guessed the number.");
                    System.out.println("Round Score: " + roundScore);
                    isGuessed = true;
                    break;
                } 
                else if (userGuess < numberToGuess) {
                    System.out.println("📉 Too low!");
                } 
                else {
                    System.out.println("📈 Too high!");
                }
                System.out.println("Attempts left: " + attemptsLeft);
            }
            if (!isGuessed) {
                System.out.println("\n❌ You've used all attempts.");
                System.out.println("The correct number was: " + numberToGuess);
            }
            System.out.println("\nYour Total Score: " + totalScore);
            System.out.print("\nDo you want to play another round? (yes/no): ");
            playAgain = sc.next().equalsIgnoreCase("yes");
        }
        System.out.println("\n🏁 Game Over!");
        System.out.println("Final Score: " + totalScore);
        sc.close();
    }
}
