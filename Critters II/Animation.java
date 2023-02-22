/*
 * CRITTERS Animation.java
 * EE422C Project 5 submission by
 * Replace <...> with your actual data.
 * Julian Wearden
 * jfw864
 * 17615
 * Benjamin Huynh
 * bth882
 * 17620
 * Slip days used: <1>
 * Fall 2022
 */

package assignment5;

import javafx.animation.AnimationTimer;

public class Animation extends AnimationTimer{

	protected static int animatingSpeed = 0;
	protected static Boolean animating = false;
	
	@Override
	public void handle(long arg0) {
		if(!animating) {
			animatingSpeed = 1;
		}
		animating = true;
		for(int i = 0; i < animatingSpeed; i++) {
			try {
				Critter.worldTimeStep();
			} catch (InvalidCritterException | Error e) {
				//Insert error message
			}
		}
		Critter.displayWorld(Main.worldGrid);
	}
	
	@Override
	public void stop() {
		super.stop();
		animating = false;
		animatingSpeed = 0;
	}
	
	public void increaseSpeed() {
		animatingSpeed += 5;
	}
	
	public static Boolean isAnimating() {
		return animating;
	}
	
	public static int getAnimatingSpeed() {
		return animatingSpeed;
	}
	
	
}