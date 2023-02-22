package assignment2;

import java.util.ArrayList;
import java.util.List;

//Class to manage history table
public class GuessHistory {

	List<String> history;
	
	public GuessHistory() {
		this.history = new ArrayList<String>();
	}
	
	//Adds element to history list
	public void addGuess(String newGuess) {
		this.history.add(newGuess);
	}
	
	//Prints history line by line
	public void printHistory() {
		for(String n:history) {
			System.out.println(n);
		}
	}
}