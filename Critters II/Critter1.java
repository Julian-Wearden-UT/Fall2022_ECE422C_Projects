/*
 * CRITTERS Critter1.java
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
 * Media, fights everything but if it encounters a politician
 * will get extra mad and gain 20% more energy. Walks in the direction
 * of nearest politicians during time step.
 * 
 */
public class Critter1 extends Critter{

	/**
	 * String character = 1
	 */
	@Override
    public String toString() {
        return "1";
    }
	
	/**
	 * Moved closer to Politician
	 */
	@Override
	public void doTimeStep() {
		int dir;
		boolean foundPolitician = false;
		int mediaX = this.getX_coord();
		int mediaY = this.getY_coord();
		Integer distance = Integer.MAX_VALUE;
		int politicianX = 0;
		int politicianY = 0;
		for (Critter crit: super.getPopulation()) {
			if(crit.toString() == "2") {
				foundPolitician = true;
				int currPoliticianX = crit.getX_coord();
				int currPoliticianY = crit.getY_coord();
				int distanceTo = (int) Math.sqrt((currPoliticianY - mediaY) * (currPoliticianY - mediaY) + (currPoliticianX - mediaX) * (currPoliticianX - mediaX));
				if (distanceTo < distance) {
					distance = distanceTo;
					politicianX = currPoliticianX;
					politicianY = currPoliticianY;
				}
			}
		}
	
		//If not already found a politician
	    if (!(distance == 0) && foundPolitician) {
	    	// Go in direction of nearest politician
	    	if(politicianX > mediaX && politicianY == mediaY) {
				dir = 0;
			}
	    	else if(politicianX > mediaX && politicianY > mediaY) {
	    		dir = 1;
	    	}
	    	else if(politicianX == mediaX && politicianY > mediaY) {
	    		dir = 2;
	    	}
	    	else if(politicianX < mediaX && politicianY > mediaY) {
	    		dir = 3;
	    	}
	    	else if(politicianX < mediaX && politicianY == mediaY) {
	    		dir = 4;
	    	}
	    	else if(politicianX < mediaX && politicianY < mediaY) {
	    		dir = 5;
	    	}
	    	else if(politicianX == mediaX && politicianY < mediaY) {
	    		dir = 6;
	    	}
	    	else if(politicianX > mediaX && politicianY < mediaY) {
	    		dir = 7;
	    	}
	    	else {
	    		dir = 0;
	    	}
	    	look(dir, true);
	    	if(distance <= 1) {
	    		walk(dir);
	    	}
	    	else {
	    		run(dir);
	    	}
	    }
		
		
	}
	
	/**
	 * Always fights. Increases energy by 20% if fighting politician
	 */
	@Override
	public boolean fight(String opponent) {
		if (opponent.equals("2")) {
			this.setEnergy(this.getEnergy() + (this.getEnergy() / 5));
		}
		return true;
	}

	@Override
	public CritterShape viewShape() {
		return CritterShape.STAR;
	}
	@Override
	public javafx.scene.paint.Color viewColor() {
        return javafx.scene.paint.Color.YELLOW;
    }
	
}
