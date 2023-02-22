/*
 * CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2021
 */

package assignment4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/* 
 * See the PDF for descriptions of the methods and fields in this
 * class. 
 * You may add fields, methods or inner classes to Critter ONLY
 * if you make your additions private; no new public, protected or
 * default-package code or data can be added to Critter.
 */

public abstract class Critter_Backup {

    private int energy = 0;
    private boolean critterHasMoved = false;
    private boolean isFighting = false;
    private boolean forcedToFight = false;

    private int x_coord;
    private int y_coord;

    private static List<Critter> population = new ArrayList<Critter>();
    private static List<Critter> babies = new ArrayList<Critter>();
    

    /* Gets the package name.  This assumes that Critter and its
     * subclasses are all in the same package. */
    private static String myPackage;

    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    private static Random rand = new Random();

    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    public static void setSeed(long new_seed) {
        rand = new Random(new_seed);
    }

    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the unqualified name of a concrete
     * subclass of Critter, if not, an InvalidCritterException must be
     * thrown.
     *
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void createCritter(String critter_class_name)
            throws InvalidCritterException {
        try {
        	// Used StackOverflow to fix newInstance() depreciated error
        	// https://stackoverflow.com/questions/46393863/what-to-use-instead-of-class-newinstance
			Critter newCritter = (Critter) Class.forName(myPackage + "." + critter_class_name).getConstructor().newInstance();
        	 newCritter.energy = Params.START_ENERGY;
        	 newCritter.x_coord = getRandomInt(Params.WORLD_WIDTH);
        	 newCritter.y_coord = getRandomInt(Params.WORLD_HEIGHT);
        	 population.add(newCritter);
        	 
        } catch (Exception e) {
        	throw new InvalidCritterException(critter_class_name);
        }
    }
    

    /**
     * Gets a list of critters of a specific type.
     *
     * @param critter_class_name What kind of Critter is to be listed.
     *        Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name)
            throws InvalidCritterException {
        ArrayList<Critter> critterInstances = new ArrayList<Critter>();
        try {
        	for(Critter currCritter:population) {
        		if(Class.forName(myPackage + "." + critter_class_name).isInstance(currCritter)){
        			critterInstances.add(currCritter);
        		}	
        	}
        } catch(Exception e) {
        	throw new InvalidCritterException(critter_class_name);
        }
        return critterInstances;
    }

    /**
     * Clear the world of all critters, dead and alive
     */
    public static void clearWorld() {
        population.clear();
    }

    public static void worldTimeStep() throws InvalidCritterException {
    	boolean doingTimeStep = true;
    	
    	cullDead(doingTimeStep);	//Do time steps and remove those that lost all energy
        doingTimeStep = false;
        
    	doEncounters();	//Process fights
        genClovers();	// Create clovers
        babiesToPopulation();	//Move babies to population
        
        cullDead(doingTimeStep);	// Remove newly dead critters
    }
    
    private static void babiesToPopulation() {
    	population.addAll(babies);
        babies.clear();
    }
    
    /*
     * Remove any dead critters. Pass in whether or not time step
     * is happening. If false, don't do time step or change energy;
     * only remove dead.
     * 
     *  @param doingTimeStep
     */
    private static void cullDead(boolean doingTimeStep) {
    	List<Critter> populationToRemove = new ArrayList<Critter>();
    	for(Critter currCritter:population) {
    		// Process timestep before checking energy
    		// Don't process time step if just culling 
    		if (doingTimeStep) {
    			currCritter.critterHasMoved = false;
    			currCritter.doTimeStep();
            	currCritter.energy -= Params.REST_ENERGY_COST;
    		}
    		if (currCritter.energy <= 0) {
    			populationToRemove.add(currCritter);
    		}
    	}
    	population.removeAll(populationToRemove);
    }
    
    private static void genClovers() throws InvalidCritterException {
    	for (int i = 0; i < Params.REFRESH_CLOVER_COUNT; i++) {
    		createCritter("Clover");
    	}
    }
    
    // TODO Finish function
    private static void doEncounters() {
    	for (Critter critA:population) {
    		for(Critter critB:population) {
    			if (!critA.equals(critB)) {	// Not fighting self
    				if (critA.energy > 0 && critB.energy > 0) {	// Make sure critter hasn't died yet (will be removed in cullDead later)
	    				if(critA.x_coord == critB.x_coord && critA.y_coord == critB.y_coord) {	// If they're on the same spot, fight
	    					critA.isFighting = true;
	        				critB.isFighting = true;
	        				critA.forcedToFight = false;
	        				critB.forcedToFight = false;
	        				boolean critAResponse = critA.fight(critB.toString());
	        				boolean critBResponse = critB.fight(critA.toString());
	        				int rollA;
	        				int rollB;
	        				
	        				// A elected to fight
	        				if (critAResponse || critB.forcedToFight) {
	        					rollA = Critter.getRandomInt(critA.energy);
	        				}
	        				// A elected not to fight
	        				else {
	        					rollA = 0;
	        				}
	        				// B elected to fight
	        				if(critBResponse || critB.forcedToFight) {
	        					rollB = Critter.getRandomInt(critB.energy);
	        				}
	        				// B elected not to fight
	        				else {
	        					rollB = 0;
	        				}
	        				
	        				// B loses
	        				if(rollA >= rollB) {
	        					critA.energy += critB.energy / 2;
	        					critB.energy = 0;
	        				}
	        				// A loses
	        				else {
	        					critB.energy += critA.energy / 2;
	        					critA.energy = 0;
	        				}
	    				}
    				}
    			}
    		}
    	}
    }

    public static void displayWorld() {
        // TODO: Complete this method
    }

    /**
     * Prints out how many Critters of each type there are on the
     * board.
     *
     * @param critters List of Critters.
     */
    public static void runStats(List<Critter> critters) {
        System.out.print("" + critters.size() + " critters as follows -- ");
        Map<String, Integer> critter_count = new HashMap<String, Integer>();
        for (Critter crit : critters) {
            String crit_string = crit.toString();
            critter_count.put(crit_string,
                    critter_count.getOrDefault(crit_string, 0) + 1);
        }
        String prefix = "";
        for (String s : critter_count.keySet()) {
            System.out.print(prefix + s + ":" + critter_count.get(s));
            prefix = ", ";
        }
        System.out.println();
    }

    public abstract void doTimeStep();

    public abstract boolean fight(String oponent);

    /* a one-character long string that visually depicts your critter
     * in the ASCII interface */
    public String toString() {
        return "";
    }

    protected int getEnergy() {
        return energy;
    }

    
    // TODO need a way to check if critter in a fight
    // If it is and the place they want to move to is occupied,
    // they can't move there.
    protected final void walk(int direction) {
        this.energy -= Params.WALK_ENERGY_COST;
        if(!critterHasMoved) {
        	moveCritter(direction, 1);
        }
    }

    protected final void run(int direction) {
    	this.energy -= Params.WALK_ENERGY_COST;
    	if(!critterHasMoved) {
        	moveCritter(direction, 2);
        }
    }
    
    private final void moveCritter(int direction, int offset) {
    	int originalX = x_coord;
    	int originalY = y_coord;
    	if(direction == 0) {
    		x_coord += offset;	// East
    	}
    	else if(direction == 1) {
    		x_coord += offset;	//East
    		y_coord += offset;	//North
    	}
    	else if (direction == 2) {
    		y_coord += offset;	//North
    	}
    	else if (direction == 3) {
    		x_coord += -offset;	//West
    		y_coord += offset;	//North
    	}
    	else if (direction == 4) {
    		x_coord += -offset;	//West
    	}
    	else if (direction == 5) {
    		x_coord += -offset;	//West
    		y_coord += -offset;	//South
    	}
    	else if (direction == 6) {
    		y_coord += -offset;	//South
    	}
    	else if (direction == 7) {
    		x_coord += offset;	//East
    		y_coord += -offset;	//South
    	}
    	// If critter tries to run from a fight
    	if(isFighting) {
    		// Check if spot available to move
    		if(!spotAvailable(x_coord, y_coord)) {
    			x_coord = originalX;
    			y_coord = originalY;
    			forcedToFight = true;
    		}
    	}
    	
    	critterHasMoved = true;
    }
    
    private final boolean spotAvailable(int x, int y) {
    	for (Critter currCritter:population) {
    		if (currCritter != this) {
    			if (currCritter.x_coord == x && currCritter.y_coord == y) {
    				return false;
    			}
    		}
    	}
    	return true;
    }

    protected final void reproduce(Critter offspring, int direction) {
        // TODO: Complete this method
    }

    /**
     * The TestCritter class allows some critters to "cheat". If you
     * want to create tests of your Critter model, you can create
     * subclasses of this class and then use the setter functions
     * contained here.
     * <p>
     * NOTE: you must make sure that the setter functions work with
     * your implementation of Critter. That means, if you're recording
     * the positions of your critters using some sort of external grid
     * or some other data structure in addition to the x_coord and
     * y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends Critter {

        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }

        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
        }

        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }

        /**
         * This method getPopulation has to be modified by you if you
         * are not using the population ArrayList that has been
         * provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         */
        protected static List<Critter> getPopulation() {
            return population;
        }

        /**
         * This method getBabies has to be modified by you if you are
         * not using the babies ArrayList that has been provided in
         * the starter code.  In any case, it has to be implemented
         * for grading tests to work.  Babies should be added to the
         * general population at either the beginning OR the end of
         * every timestep.
         */
        protected static List<Critter> getBabies() {
            return babies;
        }
    }
}
