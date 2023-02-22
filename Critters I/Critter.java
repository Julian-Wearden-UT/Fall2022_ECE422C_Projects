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

public abstract class Critter {

    private int energy = 0;
    private boolean critterHasMoved = false;

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
        	 Critter newCritter = (Critter) Class.forName(myPackage + "." + critter_class_name).newInstance();
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
    	cullDead(true);	//Do time steps and remove those that lost all energy
        doEncounters();	//Process fights
        genClovers();	// Create clovers
        babiesToPopulation();	//Move babies to population
        cullDead(false);	// Remove newly dead critters
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
    	for (Critter crit1:population) {
    		for(Critter crit2:population) {
    			if (crit1 != crit2) {
    				
    			}
    		}
    	}
    }

    public static void displayWorld() {
        // TODO: Complete this method
    	
    	int [][] twoGrid = new int [Params.WORLD_HEIGHT + 2][Params.WORLD_WIDTH + 2];
    	
    	for(int i = 0; i < twoGrid.length;i++) { //row?
    		
    		for(int j = 0; j < twoGrid[i].length;j++) { //column?
    			//need to print border 
    			//print new line
    			System.out.println();
    			if(i == 0 || i == twoGrid.length) {
    				if (j == 0 || j == twoGrid[i].length) {
    					System.out.print("+");
    				}
    				else {
    					System.out.print("-");
    				}
    			}
    			//Middle part of border
    			if((i > 0 || i < twoGrid.length)  && (j == 0 || j == twoGrid[i].length)) {
    				System.out.print("|");
    			}
    			
    			//now to print critters
    			for (Critter crit: population) {
    				if (crit.x_coord + 2 == i && crit.y_coord + 2 == j) {
    					//need to adjust for the border 
    					
    					crit.toString();
    				}
    			}
    		}
    	}
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
        	moveCritter(direction, 1);
        }
    }
    
    // TODO Need to finish coords
    private final void moveCritter(int direction, int offset) {
    	if(direction == 0) {
    		x_coord += 1;
    	}
    	else if(direction == 1) {
    		y_coord += 1;
    		x_coord += 1;
    	}
    	else if (direction == 2) {
    		
    	}
    }

    protected final void reproduce(Critter offspring, int direction) {
    	 // TODO: Complete this method
    	//Check that parent critter has enough energy
    	if(this.getEnergy() >= Params.MIN_REPRODUCE_ENERGY) {
    		//create a new critter
    		//child must have energy equal to 1/2 of the parent's energy (rounding fractions down)
    		offspring.energy = this.energy/2;
    		//Reassign the parent so that it has 1/2 energy (energy rounding up)
    		this.energy = this.energy/2; 
    		//Assign the child a position indicated by the parent's current position and the specified direction. The child will always be created in a position adjacent to the parent
    		//check to see if they're within the bounds
    		offspring.x_coord+= direction; 
    		offspring.y_coord+= direction;
    		//add offspring to babies
    		babies.add(offspring);
    	}
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