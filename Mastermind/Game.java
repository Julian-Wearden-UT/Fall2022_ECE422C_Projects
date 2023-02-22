package assignment2;

import java.util.Scanner;

public class Game {
	
	private Boolean isTesting;
	private String secret;
	private int guessesLeft;
	
	public String[] validColors;
	public int pegNumber;
	Scanner inputScanner;
	
	
	public Game (Boolean isTesting, GameConfiguration config, SecretCodeGenerator generator, Scanner inputScanner) {
		this.isTesting = isTesting;
		this.secret = generator.getNewSecretCode();	//Create Secret Code for game
		this.guessesLeft = config.guessNumber;
		this.inputScanner = inputScanner;
		this.validColors = config.colors;
		this.pegNumber = config.pegNumber;
	}
	
	public void runGame() {
		
		//Print secret code if testing
		if (isTesting) {
			System.out.println("Secret code: " + secret);
		}
		
		//Class for history list
		GuessHistory history = new GuessHistory();
		
		while (guessesLeft > 0) {
			System.out.println();
			System.out.println("You have " + guessesLeft + " guess(es) left.");
			System.out.println("Enter guess: ");
			String guess = inputScanner.nextLine();
			
			//Create instance of class to check guesses
			GuessChecker check = new GuessChecker(validColors, pegNumber);
			
			//Make sure guess is valid
			if (check.requestHistory(guess)) {
				history.printHistory();
			}
			else if(!check.checkGuess(guess)){
				System.out.println("INVALID_GUESS");
			}
			else {
				guessesLeft --;
				
				//Check how many pegs
				Pegs pegs = new Pegs(guess, secret);
				pegs.calculatePegs();
				
				//Add to History
				String pegsAsString = pegs.toString();
				String showGuess = (guess + " -> " + pegsAsString);
				history.addGuess(showGuess);
				
				//Print user's guess & result
				System.out.println(showGuess);
				
				//Check if user has won
				Boolean hasWon = pegs.pegsFilled();
				
				//Check if user has won
				if (hasWon) {
					System.out.println("You win!");
					System.out.println();
					break;
				}
				//If out of guesses user loses
				else if(!hasWon && guessesLeft == 0) {
					System.out.println("You lose! The pattern was " + secret);
					System.out.println();
				}
			}
			}
		
	}
}
