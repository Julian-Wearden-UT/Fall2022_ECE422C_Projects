/* EE422C Project 5 submission by
* Replace <...> with your actual data.
* Benjamin Huynh
* bth882
* 17620
* Slip days used: <1>
* Fall 2022
*/
package assignment5;

import assignment5.Critter.CritterShape;

/**
 * Agent, runs from everyone but fights only with the Champion
 * 
 * 
 */
public class Critter4 extends Critter{
	/*
	 * Symbol of Critter 4 is 4 
	 */
	@Override
    public String toString() {
        return "4";
    }
	
	@Override
	public void doTimeStep() {
		look(5, true);
		walk((int) (Math.random() * 5));
	}
	/*
	 * Runs from everyone, but attempts to fight Critter 3
	 * @param opponent 
	 */
	@Override
	public boolean fight(String opponent) {
		if (!opponent.equals("3")) {
			run(Critter.getRandomInt(8));
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public CritterShape viewShape() {
		// TODO Auto-generated method stub
		return CritterShape.TRIANGLE;
	}
@Override
	public javafx.scene.paint.Color viewColor() {
        return javafx.scene.paint.Color.BLUE;
    }
}
