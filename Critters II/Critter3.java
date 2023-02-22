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
 * Champion, beats all other critters and is the strongest critter of all, but runs from the agent. 
 * 
 * 
 */
public class Critter3 extends Critter {
	/*
	 * Champion's energy is set to the highest energy out of all critters
	 */
	public Critter3() {
		super.setEnergy(9999999);
	}
	/*
	 * Symbol of Critter 3 
	 * is 3
	 */
	@Override
    public String toString() {
        return "3";
    }
	/*
	 * 
	 */
	@Override
	public void doTimeStep() {
		walk((int) (Math.random()*7));
	}
	/*
	 * Fights everyone, but runs away from Critter 4
	 */
	@Override
	public boolean fight(String opponent) {
		if (opponent.equals("4")) {
			look(0, true);
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
		return CritterShape.DIAMOND;
	}
@Override
	public javafx.scene.paint.Color viewColor() {
        return javafx.scene.paint.Color.WHITE;
    }
}
