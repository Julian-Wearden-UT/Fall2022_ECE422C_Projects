/* If you submit this file, it will be ignored. Do not modify. */
package assignment2;

public class GameConfiguration {
    final int guessNumber;	//Number of Guesses Allowed
    final String[] colors;	//Available colors
    final int pegNumber;	//Number of pegs

    GameConfiguration(int guessNumber, String[] colors, int pegNumber) {
        this.guessNumber = guessNumber;
        this.colors = colors;
        this.pegNumber = pegNumber;
    }
}
