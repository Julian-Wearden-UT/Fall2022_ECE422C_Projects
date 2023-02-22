package assignment2;

import java.util.Arrays;

//Class to check validity of guess
public class GuessChecker {
	
	String[] validColors;
	int validLength;

	public GuessChecker(String[] validColors, int validLength) {
		this.validColors = validColors;
		this.validLength = validLength;
	}
	
	// Is user asking to see History?
	public Boolean requestHistory(String guess) {
		return guess.equals("HISTORY");
	}
	
	//Is guess valid?
	public Boolean checkGuess(String guess) {
		
		String[] guessArray = guess.split("");	//Convert guess to same type as validColors
		
		//For all values of guess, check if validColors has that value
		for (int i = 0; i < guessArray.length; i++) {
			//A guessed character is not in valid colors
			if(!Arrays.asList(validColors).contains(guessArray[i])) {
				return false;
			}
		}
		//Check length
		if (guess.length() != this.validLength) {
			return false;
		}
		//Check capitalization
		else if (!guess.equals(guess.toUpperCase())) {
			return false;
		}
		else {
			return true;
		}
	}

}
