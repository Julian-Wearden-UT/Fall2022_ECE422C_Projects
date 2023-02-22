/*
 * CRITTERS Critter2.java
 * EE422C Project 5 submission by
 * Replace <...> with your actual data.
 * Julian Wearden
 * jfw864
 * 17615
 * Slip days used: <1>
 * Fall 2022
 */

package assignment5;

import assignment5.Critter.CritterShape;

/**
 * Politician, runs from media but fights everything else
 * Spends most of their time doing nothing
 * 
 */
public class Critter2 extends Critter{

	/**
	 * String character = 2
	 */
	@Override
    public String toString() {
        return "2";
    }
	
	/**
	 * Makes 0-3 children
	 */
	@Override
	public void doTimeStep() {
		Critter2 child = new Critter2();
		reproduce(child, Critter.getRandomInt(3));
	}
	
	/**
	 * Runs from media, fights everyone else
	 */
	@Override
	public boolean fight(String opponent) {
		look(2, true);
		if (opponent.equals("1")) {
			run(Critter.getRandomInt(8));
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public CritterShape viewShape() {
		return CritterShape.DIAMOND;
		
	}
	@Override
	public javafx.scene.paint.Color viewColor() {
        if (Critter.getRandomInt(2) == 0) {
        	return javafx.scene.paint.Color.RED;
        	
        }
        else {
        	return javafx.scene.paint.Color.BLUE;
        }
    }
	
	@Override
	 public javafx.scene.paint.Color viewOutlineColor(){
		return javafx.scene.paint.Color.web("#FDFD96");
	}
	

}
