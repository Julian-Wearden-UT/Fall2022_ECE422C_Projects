/*
 * Mastermind
 * Jun 14, 2021
 */

package assignment2;

import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
    	// Use this for your testing.  We will not be calling this method.z
    	//Test Method
    	GameConfiguration config = new GameConfiguration(12, new String[]{"B","G","O","P","R","Y"}, 4);
        SecretCodeGenerator generator = new SecretCodeGenerator(config);
        start(false, config, generator);
    }

    public static void start(Boolean isTesting, GameConfiguration config, SecretCodeGenerator generator) {
		// We will call this method from our JUnit test cases.
    	
    	//Setup & Initial Greeting
    	Scanner inputScanner = new Scanner(System.in);
    	String newGame = "Y";
    	
    	System.out.println("Welcome to Mastermind.");
    	//Play game until "No"
    	while (newGame.equals("Y")) {
        	System.out.println("Do you want to play a new game? (Y/N): ");
        	newGame = inputScanner.nextLine();
        	
        	if(newGame.equals("Y")) {
	    		Game startGame = new Game(isTesting, config, generator, inputScanner);
	    		startGame.runGame();
        	}
    	}
    }
}
