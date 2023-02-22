/*
 * CRITTERS Critter.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/*
 * See the PDF for descriptions of the methods and fields in this
 * class.
 * You may add fields, methods or inner classes to Critter ONLY
 * if you make your additions private; no new public, protected or
 * default-package code or data can be added to Critter.
 */

public abstract class Critter {

    /* START --- NEW FOR PROJECT 5 */
    public enum CritterShape {
        CIRCLE,
        SQUARE,
        TRIANGLE,
        DIAMOND,
        STAR
    }

    /* the default color is white, which I hope makes critters invisible by default
     * If you change the background color of your View component, then update the default
     * color to be the same as you background
     *
     * critters must override at least one of the following three methods, it is not
     * proper for critters to remain invisible in the view
     *
     * If a critter only overrides the outline color, then it will look like a non-filled
     * shape, at least, that's the intent. You can edit these default methods however you
     * need to, but please preserve that intent as you implement them.
     */
    public javafx.scene.paint.Color viewColor() {
        return javafx.scene.paint.Color.web("#344B61");
    }

    public javafx.scene.paint.Color viewOutlineColor() {
        return viewColor();
    }

    public javafx.scene.paint.Color viewFillColor() {
        return viewColor();
    }

    public abstract CritterShape viewShape();

    /**
     * 
     * @param direction
     * @param steps
     * @return string of character in position looking at or null
     */
    protected final String look(int direction, boolean steps) {
    	int offset = 1;
    	if (steps) {
    		offset = 2;
    	}
    	
    	this.setEnergy(this.getEnergy() - Params.LOOK_ENERGY_COST );
    	int newX = this.getX_coord();
    	int newY = this.getY_coord();
    	if(direction == 0) {
    		newX += offset;		// East
    	}
    	else if(direction == 1) {
    		newX += offset;		//East
    		newY += offset;		//North
    	}
    	else if (direction == 2) {
    		newY += offset;		//North
    	}
    	else if (direction == 3) {
    		newX += -offset;	//West
    		newY += offset;		//North
    	}
    	else if (direction == 4) {
    		newX += -offset;	//West
    	}
    	else if (direction == 5) {
    		newX += -offset;	//West
    		newY += -offset;	//South
    	}
    	else if (direction == 6) {
    		newY += -offset;	//South
    	}
    	else if (direction == 7) {
    		newX += offset;		//East
    		newY += -offset;	//South
    	}
    	
    	for (Critter currCritter:population) {
			if (currCritter != this && currCritter.getX_coord() == newX && currCritter.getY_coord() == newY) {
				return currCritter.toString();
			}
    	}
    	
        return null;
    }

    /**
     * Prints out how many Critters of each type there are on the 
     * board.
     * 
     * @param critters List of Critters.
     * @return string of critter info
     */
    public static String runStats(List<Critter> critters) {
    	String output = "";
    	if(Animation.isAnimating()) {
    		output += "Animating Speed: " + Animation.getAnimatingSpeed() + " step(s)\n";
    	}
    	output += "" + critters.size() + " critters as follows -- ";

    	Map<String, Integer> critter_count = new HashMap<String, Integer>();
    	for (Critter crit : critters) {
    	    String crit_string = crit.toString();
    	    critter_count.put(crit_string, critter_count.getOrDefault(crit_string, 0) + 1);
    	}
    	String prefix = "";
    	for (String s : critter_count.keySet()) {
    		output += prefix + s + ":" + critter_count.get(s);
    	    //System.out.print(prefix + s + ":" + critter_count.get(s));
    	prefix = ", ";
    	}
    	output += "\n";
    	//System.out.println();

    	return output;
    }

	/**
	 * Displays grid of critters on
	 * a GridPane
	 * 
	 * @param pane Gridpane to be passed in
	 */
    public static void displayWorld(Object pane) {
    	GridPane critterGrid = (GridPane) pane;
    	Critter [][] critterArray = new Critter [Params.WORLD_HEIGHT][Params.WORLD_WIDTH];
    	
    	for (Critter crit: population) {
			int x = crit.getX_coord();
    		int y = crit.getY_coord();
			critterArray[y][x] = crit;
		}
    	
    	
    	for (int y = 0; y < Params.WORLD_HEIGHT; y++) {
    		for (int x = 0; x < Params.WORLD_WIDTH; x++) {
    			int width = (int) (Main.STAGE_WIDTH/Params.WORLD_WIDTH);
    			int height = (int) (Main.STAGE_HEIGHT/Params.WORLD_HEIGHT);
    			if(critterArray[y][x] == null) {
    				// 	Draw empty item:
    				//	(i.e.) Create JavaFX Node that has blank image
    				Rectangle square = new Rectangle();
    				//white square 
    				square.setStroke(Color.web("#344B61"));
    				square.setWidth(width);
    				square.setHeight(height);
    				square.setFill(Color.web("#344B61"));
    				critterGrid.add(square, x, y);
    				
    			}
    			else {
    				// Draw critter item:
    				//	(i.e.) Create JavaFX Node that holds shape/image of critter
    				Rectangle square = new Rectangle();
    				//white square 
    				square.setStroke(Color.web("#344B61"));
    				square.setWidth(width);
    				square.setHeight(height);
    				square.setFill(Color.web("#344B61"));
    				critterGrid.add(square, x, y);
    				if (critterArray[y][x].viewShape() == CritterShape.CIRCLE) {
    					Circle critterShape = new Circle();
    					critterShape.setStroke(critterArray[y][x].viewOutlineColor());
    					critterShape.setRadius(width/2);
    					critterShape.setFill(critterArray[y][x].viewFillColor());
    					StackPane stack_pane = new StackPane(critterShape);
    					stack_pane.setPrefSize(width, height);  
    					stack_pane.setMinSize(width, height);
    					stack_pane.setMaxSize(width, height);
    					critterGrid.add(stack_pane, x, y);
    				}
    				else if(critterArray[y][x].viewShape() == CritterShape.SQUARE) {
    					Rectangle critterShape = new Rectangle();
    					critterShape.setStroke(critterArray[y][x].viewOutlineColor());
    					critterShape.setWidth(width);
    					critterShape.setHeight(height);
    					critterShape.setFill(critterArray[y][x].viewFillColor()); //some of these do not have viewFillColor
    					StackPane stack_pane = new StackPane(critterShape);
    					stack_pane.setPrefSize(width, height);  
    					stack_pane.setMinSize(width, height);
    					stack_pane.setMaxSize(width, height);
    					critterGrid.add(stack_pane, x, y);
    				}
    				else if(critterArray[y][x].viewShape() == CritterShape.TRIANGLE) {
    					//Polygon critterShape = new Polygon();
    					Path critterShape = new Path();
    					critterShape.setStroke(critterArray[y][x].viewOutlineColor());
    					critterShape.getElements().addAll(new MoveTo(0 , 0),new LineTo(width/2,height),new LineTo(width, 0), new LineTo(0,0), new ClosePath());

    					//((Polygon) critterShape).getPoints().addAll(new Double [] {0.0,0.0, (double) (width*2), (double) (height), (double) (width), (double) (height*2)});
    					critterShape.setFill(critterArray[y][x].viewFillColor());
    					critterShape.setLayoutX(width);
    					critterShape.setLayoutY(height);
    					StackPane stack_pane = new StackPane(critterShape);
    					stack_pane.setPrefSize(width, height);  
    					stack_pane.setMinSize(width, height);
    					stack_pane.setMaxSize(width, height);
    					critterGrid.add(stack_pane, x, y);
    				}
    				else if(critterArray[y][x].viewShape() == CritterShape.STAR) {
    					Path critterShape = new Path();
    					
    					critterShape.setStroke(critterArray[y][x].viewOutlineColor());
    					critterShape.getElements().addAll(new MoveTo(width/2 ,0 ),new LineTo(0 ,height),new LineTo(width, height), new ClosePath(), new MoveTo(0,height/3), new LineTo(width,height/3), new LineTo(width/2 , height *4/3), new ClosePath());
    					critterShape.setFill(critterArray[y][x].viewFillColor());
    					
    					StackPane stack_pane = new StackPane(critterShape);
    					stack_pane.setPrefSize(width, height);  
    					stack_pane.setMinSize(width, height);
    					stack_pane.setMaxSize(width, height);
    					critterGrid.add(stack_pane, x, y);
    				}
    				
    				else { //DIAMOND
    					Path critterShape = new Path();
    					critterShape.setStroke(critterArray[y][x].viewOutlineColor());
    					critterShape.getElements().addAll(new MoveTo(0 , height/2),new LineTo(width/2,height),new LineTo(width, height/2), new LineTo(width/2,0), new LineTo(0, height/2), new ClosePath());
    					critterShape.setFill(critterArray[y][x].viewFillColor());
    					
    					StackPane stack_pane = new StackPane(critterShape);
    					stack_pane.setPrefSize(width, height);  
    					stack_pane.setMinSize(width, height);
    					stack_pane.setMaxSize(width, height);
    					critterGrid.add(stack_pane, x, y);
    				}
    				
    			}
    		}
    	}
    	Main.updateStats();
    }

	/* END --- NEW FOR PROJECT 5
			rest is unchanged from Project 4 */

    private int energy = 0;
    private boolean critterHasMoved = false;
    private boolean critterIsFighting = false;
    private boolean forcedToFight = false;

    private int x_coord;
    private int y_coord;

    private static List<Critter> population = new ArrayList<Critter>();
    private static List<Critter> babies = new ArrayList<Critter>();
    private static int [][] twoGrid;
    

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
        	 newCritter.setEnergy(Params.START_ENERGY);
        	 newCritter.setX_coord(getRandomInt(Params.WORLD_WIDTH));
        	 newCritter.setY_coord(getRandomInt(Params.WORLD_HEIGHT));
        	 population.add(newCritter);
        	 
        } catch (Exception | Error e) {
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

    /**
     * Step through time once. Does time step for each critter,
     * processes encounters between critters, generates new clovers,
     * adds babies to population, and removes all dead critters.
     * 
     * @throws InvalidCritterException
     */
    public static void worldTimeStep() throws InvalidCritterException {
    	boolean doingTimeStep = true;
    	
    	cullDead(doingTimeStep);	//Do time steps and remove those that lost all energy
        doingTimeStep = false;
        
    	doEncounters();	//Process fights
        genClovers();	// Create clovers
        babiesToPopulation();	//Move babies to population
        
        cullDead(doingTimeStep);	// Remove newly dead critters
    }
    
    /**
     * Adds babies list to population list
     */
    private static void babiesToPopulation() {
    	population.addAll(babies);
        babies.clear();
    }
    
    /**
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
    			currCritter.setCritterHasMoved(false); // Hasn't moved this time step
    			currCritter.doTimeStep();
    			currCritter.setEnergy(currCritter.getEnergy() - Params.REST_ENERGY_COST); //Remove time step energy
    		}
    		if (currCritter.getEnergy() <= 0) {
    			populationToRemove.add(currCritter); // Add critter to removal list if lost all energy
    		}
    	}
    	population.removeAll(populationToRemove);	// Remove those in popToRemove from main population
    }
    
    /**
     * Generates clovers based on clover count
     * @throws InvalidCritterException
     */
    private static void genClovers() throws InvalidCritterException {
    	for (int i = 0; i < Params.REFRESH_CLOVER_COUNT; i++) {
    		createCritter("Clover");
    	}
    }
    
    /**
     * Processes encounters between critters on the same x-y coordinate
     * Changes energy of critter based on outcome of fight but does
     * not remove them from population immediately
     */
    private static void doEncounters() {
    	for (Critter critA:population) {
    		for(Critter critB:population) {
    			if (!critA.equals(critB)) {	// Not fighting self
    				if (critA.getEnergy() > 0 && critB.getEnergy() > 0) {	// Make sure critter hasn't died yet (will be removed in cullDead later)
	    				if(critA.getX_coord() == critB.getX_coord() && critA.getY_coord() == critB.getY_coord()) {	// If they're on the same spot, fight
	    					// Set critter fighting flags for movement (in case they try to run)
	    					critA.setCritterIsFighting(true);
	        				critB.setCritterIsFighting(true);
	        				// Not forced to fight until they try to move to an occupied location
	        				critA.setForcedToFight(false);
	        				critB.setForcedToFight(false);
	        				// Check what each critter wants to do
	        				boolean critAResponse = critA.fight(critB.toString());
	        				boolean critBResponse = critB.fight(critA.toString());
	        				int rollA;
	        				int rollB;
	        				
	        				// A elected to fight
	        				if (critAResponse || critA.getForcedToFight() && critA.getEnergy() > 0) {
	        					rollA = Critter.getRandomInt(critA.getEnergy());
	        				}
	        				// A elected not to fight
	        				else {
	        					rollA = 0;
	        				}
	        				// B elected to fight
	        				if(critBResponse || critB.getForcedToFight() && critB.getEnergy() > 0) {
	        					rollB = Critter.getRandomInt(critB.getEnergy());
	        				}
	        				// B elected not to fight
	        				else {
	        					rollB = 0;
	        				}
	        				
	        				//Don't damage energy if one of them chose to run and they're no longer in same position
	        				if(critA.getX_coord() == critB.getX_coord() && critA.getY_coord() == critB.getY_coord()){
	        					// B loses
		        				if(rollA >= rollB) {
		        					critA.setEnergy(critA.getEnergy() + (critB.getEnergy() / 2));
		        					critB.setEnergy(0);
		        				}
		        				// A loses
		        				else {
		        					critB.setEnergy(critB.getEnergy() + (critA.getEnergy() / 2));
		        					critA.setEnergy(0);
		        				}
	        				}
	    				}
    				}
    			}
    		}
    	}
    }
    

    public abstract void doTimeStep();

    public abstract boolean fight(String oponent);

    
    
    /* a one-character long string that visually depicts your critter
     * in the ASCII interface */
    public String toString() {
        return "";
    }

    // Set/Get for private variables
    protected void setEnergy(int new_energy_value) {
        energy = new_energy_value;
    }

    /**
     * Set X coordinate
     * @param new_x_coord
     */
    protected void setX_coord(int new_x_coord) {
        x_coord = new_x_coord;
    }

    /**
     * Set Y coordinate
     * @param new_y_coord
     */
    protected void setY_coord(int new_y_coord) {
        y_coord = new_y_coord;
    }
    
    /**
     * Get X  coordinate
     * @return int x coord
     */
    protected int getX_coord() {
        return x_coord;
    }

    /**
     * Get Y coordinate
     * @return int y coord
     */
    protected int getY_coord() {
    	return y_coord;
    }
    
    /**
     * Get energy int
     * @return int energy
     */
    protected int getEnergy() {
        return energy;
    }

    /**
     * Set if critter has moved
     * @param has_critter_moved
     */
    protected void setCritterHasMoved(boolean has_critter_moved) {
    	critterHasMoved = has_critter_moved;
    }
    
    /**
     * Set if critter is fighting
     * @param is_critter_fighting
     */
    protected void setCritterIsFighting(boolean is_critter_fighting) {
    	critterIsFighting = is_critter_fighting;
    }
    
    /**
     * Set forced to fight
     * @param is_forced_to_fight
     */
    protected void setForcedToFight(boolean is_forced_to_fight) {
    	forcedToFight = is_forced_to_fight;
    }
    
    /**
     * Get critter has moved
     * @return boolean
     */
    protected boolean getCritterHasMoved() {
    	return critterHasMoved;
    }
    
    /**
     * Get forced to fight
     * @return boolean
     */
    protected boolean getForcedToFight() {
    	return forcedToFight;
    }
    
    /**
     * Get critter is fighting
     * @return boolean
     */
    protected boolean getCritterIsFighting() {
    	return critterIsFighting;
    }
    
    /**
     * Get population list
     * @return List population
     */
    protected static List<Critter> getPopulation() {
		return population;
	}
   
    
    /**
     * Makes critter move one step in any direction if they haven't yet moved
     * this time step. Argument direction is a value 0-7: 0 - east,
     * 1 - north east, 2 - north, 3 - north west, 4 - west, 5 - south west, 
     * 6 - south, 7 - south east
     * 
     * @param direction
     */
    protected final void walk(int direction) {
        this.setEnergy(this.getEnergy() - Params.WALK_ENERGY_COST);
        if(!getCritterHasMoved()) {
        	moveCritter(direction, 1);
        }
    }

    /**
     * Makes critter move two steps in any direction if they haven't yet moved
     * this time step. Argument direction is a value 0-7: 0 - east, 
     * 1 - north east, 2 - north, 3 - north west, 4 - west, 5 - south west, 
     * 6 - south, 7 - south east
     * 
     * @param direction
     */
    protected final void run(int direction) {
    	this.setEnergy(this.getEnergy() - Params.RUN_ENERGY_COST);
    	if(!this.getCritterHasMoved()) {
        	moveCritter(direction, 2);
        }
    }
    
    /**
     * Changes critter's x and y position. Direction is number 0-7 of where
     * they want to move and offset is how many steps in that direction. If critter
     * is in a fight and they try to move to an unavailable spot, they won't be
     * allowed to and will be forced to fight.
     * 
     * @param direction
     * @param offset
     */
    private final void moveCritter(int direction, int offset) {
    	int originalX = this.getX_coord();
    	int originalY = this.getY_coord();
    	int newX = originalX;
    	int newY = originalY;
    	if(direction == 0) {
    		newX += offset;		// East
    	}
    	else if(direction == 1) {
    		newX += offset;		//East
    		newY += offset;		//North
    	}
    	else if (direction == 2) {
    		newY += offset;		//North
    	}
    	else if (direction == 3) {
    		newX += -offset;	//West
    		newY += offset;		//North
    	}
    	else if (direction == 4) {
    		newX += -offset;	//West
    	}
    	else if (direction == 5) {
    		newX += -offset;	//West
    		newY += -offset;	//South
    	}
    	else if (direction == 6) {
    		newY += -offset;	//South
    	}
    	else if (direction == 7) {
    		newX += offset;		//East
    		newY += -offset;	//South
    	}
    	// If critter is currently running from a fight and moving to unavailable spot
    	if(this.getCritterIsFighting() && !spotAvailable(newX, newY)) {
    		// Didn't move
    		this.setX_coord(originalX);
    		this.setY_coord(originalY);
    		this.setForcedToFight(true);
    	}
    	else {
    		// Did move
    		this.setX_coord(newX);
        	this.setY_coord(newY);
        	// Set true so they can't move again this time step
        	setCritterHasMoved(true);
    	}
    	
    	// If critter moved out of bounds, wrap them around
    	this.wrapCoordinate();
    }
    
    /**
     * Accounts for torus coordinate system where right edge wraps around to left edge
     * If coordinate of critter goes out of bounds, loop it.
     */
    private final void wrapCoordinate() {
    	twoGrid = new int [Params.WORLD_HEIGHT][Params.WORLD_WIDTH];
    	// Went too far left
    	if(this.getX_coord() < 0 ) {
    		this.setX_coord(twoGrid[0].length - 1);
    	}
    	// Went too far right
    	if (this.getX_coord() > twoGrid[0].length - 1) {
    		this.setX_coord(0);
    	}
    	// Went too far down
    	if (this.getY_coord() < 0) {
    		this.setY_coord(twoGrid.length - 1);
    	}
    	// Went too far up
    	if (this.getY_coord() > twoGrid.length - 1) {
    		this.setY_coord(0);
    	}
    }
    
    
    /**
     * Given x and y coordinates of a critter, checks if another critter 
     * is currently at that position. Returns true if spot available,
     * false otherwise.
     * 
     * @param x
     * @param y
     * @return boolean
     */
    private final boolean spotAvailable(int x, int y) {
    	for (Critter currCritter:population) {
    		if (currCritter != this) {
    			if (currCritter.getX_coord() == x && currCritter.getY_coord() == y) {
    				return false;
    			}
    		}
    	}
    	return true;
    }
    /**
     * Reproduces the offspring
     * and sets the critter to half energy
     * and halves the parent's energy
     * Also, the offspring is added to the babies list
     *
     * @param offspring offspring of critter
     * @param direction the direction of the critter
     */
    protected final void reproduce(Critter offspring, int direction) {
    	 // TODO: Complete this method
    	//Check that parent critter has enough energy
    	if(this.getEnergy() >= Params.MIN_REPRODUCE_ENERGY) {
    		//create a new critter
    		//child must have energy equal to 1/2 of the parent's energy (rounding fractions down)
    		offspring.setEnergy(this.getEnergy() /2 );
    		//Reassign the parent so that it has 1/2 energy (energy rounding up)
    		this.setEnergy(this.getEnergy()/2);
    		//Assign the child a position indicated by the parent's current position and the specified direction. The child will always be created in a position adjacent to the parent
    		//check to see if they're within the bounds
    		offspring.moveCritter(direction, 1);
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

    	/**
    	 * Set energy
    	 */
        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }

        /**
         * Set x coordinate
         */
        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
        }

        /**
         * Set Y coordinate
         */
        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
        }

        /**
         * Get X coordinate
         */
        protected int getX_coord() {
            return super.x_coord;
        }

        /**
         * Get y coordinate
         */
        protected int getY_coord() {
            return super.y_coord;
        }
        
        /**
         * Get energy
         */
        protected int getEnergy() {
            return super.energy;
        }
        
        /**
         * Set critter has moved
         */
        protected void setCritterHasMoved(boolean has_critter_moved) {
        	super.critterHasMoved = has_critter_moved;
        }
        
        /**
         * Set critter is fighting
         */
        protected void setCritterIsFighting(boolean is_critter_fighting) {
        	super.critterIsFighting = is_critter_fighting;
        }
        
        /**
         * set forced to fight
         */
        protected void setForcedToFight(boolean is_forced_to_fight) {
        	super.forcedToFight = is_forced_to_fight;
        }
        
        /**
         * Get critter has moved
         */
        protected boolean getCritterHasMoved() {
        	return super.critterHasMoved;
        }
        
        /**
         * Get forced to fight
         */
        protected boolean getForcedToFight() {
        	return super.forcedToFight;
        }
        
        /**
         * Get critter is fighting
         */
        protected boolean getCritterIsFighting() {
        	return super.critterIsFighting;
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
