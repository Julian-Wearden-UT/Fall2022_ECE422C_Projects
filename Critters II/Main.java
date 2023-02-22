/*
 * CRITTERS Main.java
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

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Main extends Application{

	public static Stage mainStage;
	public static GridPane worldGrid;
	public static Label statsLabel;
	public static TextField statsTextField;
	public static double STAGE_WIDTH = 1600;
	public static double STAGE_HEIGHT = 900;
	double SETTINGS_WIDTH = 300;
	double STATS_WIDTH = 300;
	double STATS_HEIGHT = 100;
	
    public static void main(String[] args) {
        launch(args);
    }
    
    /* Gets the package name.  The usage assumes that Critter and its
    subclasses are all in the same package. */
    public static String myPackage; // package of Critter file.

    /* Critter cannot be in default pkg. */
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }
    
/**
 * Starts and sets the grid, buttons, actions, and animations for critterGrid
 * 
 *  @param arg0 the Stage
 */
	@Override
	public void start(Stage arg0) throws Exception {
		
		mainStage = arg0;
		mainStage.setTitle("Critter World");
		worldGrid = new GridPane();
		
		StackPane root = new StackPane();
		GridPane settingsPane = new GridPane();
		GridPane hiddenSettingsPane = new GridPane();
		GridPane hiddenStatsPane = new GridPane();
		BorderPane functions = new BorderPane();
		ScrollPane statsScrollPane = new ScrollPane();
		GridPane statsPane = new GridPane();
		Animation animation = new Animation();
		statsLabel = new Label();
		
		
		//================================================================================
	    // Create Buttons For Settings Pane
	    //================================================================================
		
		//Animate Button		
		Button fastForwardButton = createSubButton(getLink("images" + File.separator + "fastforward.png"), 50, 50);
		Button pauseButton = createSubButton(getLink("images" + File.separator + "pause.png"), 50, 50);
		Button playButton = createSubButton(getLink("images" + File.separator + "play.png"), 50, 50);
		GridPane animateOptions = new GridPane();
		animateOptions.add(playButton, 1, 0);
		animateOptions.add(pauseButton, 2, 0);
		animateOptions.add(fastForwardButton, 3, 0);
		Button animateButton = createSettingsButton("Animate", animateOptions, true);
		animateButton.getStyleClass().add("animate");
		
		// Step Button
		TextField stepTextField = createTextFieldPrompt("Count (Optional)");
		Button stepButton = createSettingsButton("Step", stepTextField, true);
		stepButton.getStyleClass().add("normal");
		
		// Seed Button
		TextField seedTextField = createTextFieldPrompt("Seed Number (Optional)");
		Button seedButton = createSettingsButton("Seed", seedTextField, true);
		seedButton.getStyleClass().add("normal");

		// Create Button
		TextField createTextFieldClassName = createTextFieldPrompt("Critter Name (REQUIRED)");
		TextField createTextFieldCount = createTextFieldPrompt("Count (Optional)");	// Count
		GridPane createTextFields = new GridPane();	//Put textboxes in gridpane so they can be embedded in button
		createTextFields.add(createTextFieldClassName, 0, 0);
		createTextFields.add(createTextFieldCount, 0, 1);
		Button createButton = createSettingsButton("Create", createTextFields, true);
		createButton.getStyleClass().add("normal");

		
		// Stats Button
		statsTextField = createTextFieldPrompt("Critter Name (Optional)");
		Button statsButton = createSettingsButton("Stats", statsTextField, true);
		statsButton.getStyleClass().add("normal");
		
		// Clear Button
		Button clearButton = createSettingsButton("Clear", false);
		clearButton.getStyleClass().add("clear");
		
		//Quit button
		Button quitButton = createSettingsButton("Quit", false);
		quitButton.getStyleClass().add("quit");

		
		
		//================================================================================
	    // Actions for Buttons
	    //================================================================================
		
		// Animate Actions
		pauseButton.setDisable(true);
		fastForwardButton.setDisable(true);
		pauseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				pauseButton.setDisable(true);
				fastForwardButton.setDisable(true);
				playButton.setDisable(false);
				stepButton.setDisable(false);
				seedButton.setDisable(false);
				statsButton.setDisable(false);
				createButton.setDisable(false);
				clearButton.setDisable(false);
				animation.stop();
			}
		});		
		fastForwardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				animation.increaseSpeed();
			}
		});
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				pauseButton.setDisable(false);
				fastForwardButton.setDisable(false);
				stepButton.setDisable(true);
				playButton.setDisable(true);
				seedButton.setDisable(true);
				statsButton.setDisable(true);
				createButton.setDisable(true);
				clearButton.setDisable(true);
				animation.start();
			}
		});
		
		// Step Action
		String wordStepCount = stepTextField.getText(); //The user count input 
		stepButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String wordStepCount = stepTextField.getText();
				Integer counter;
				//if there is no count
				if(wordStepCount.equals(null) || wordStepCount.equals("")) {
					counter = 1;
				} 
				//there is a count
				else {
					try{
						counter = Integer.parseInt(wordStepCount);
					}
					catch(Exception e) {
						counter = 1;
					}
				}	
				for(int i = 0; i < counter; i++) {
					try {
						stepTextField.setStyle(null);
						Critter.worldTimeStep();
					} catch (InvalidCritterException | Error e) {
						stepTextField.setStyle("-fx-text-box-border: red; -fx-focus-color: #ff000022;");
						stepTextField.setText("Error");
					}
				}
				Critter.displayWorld(worldGrid);
			}
		});
		

		// Seed Action
		String wordListSeed = seedTextField.getText(); // The user seed input
		seedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String wordListSeed = seedTextField.getText();
				if (!wordListSeed.equals(null)) {
					long seedNumber = 0;	// Default
					try {
						seedTextField.setStyle(null);
						seedNumber = Long.parseLong(wordListSeed);
					} catch(Exception e) {
						seedTextField.setStyle("-fx-text-box-border: red; -fx-focus-color: #ff000022;");
						seedTextField.setText("Invalid Number");
					}
					Critter.setSeed(seedNumber);
				}
			}
		});
		
		// Create Action
		String wordClassCreate = createTextFieldClassName.getText(); // The user input class name
		String wordCountCreate = createTextFieldCount.getText();	// The user input count
		createButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Integer counter;
				String wordClassCreate = createTextFieldClassName.getText();
				String wordCountCreate = createTextFieldCount.getText();
				if(wordCountCreate.equals(null) || wordCountCreate.equals("")) { //if there is no count
					counter = 1;
				}
				else {
					try {
						counter = Integer.parseInt(wordCountCreate);
					}
					catch(Exception e) {
						counter = 1;
					}
				}	
				
				for(int i = 0; i < counter; i++) {
					try {
						createTextFieldClassName.setStyle(null);
						Critter.createCritter(wordClassCreate);
					} catch (InvalidCritterException | Error e) {
						createTextFieldClassName.setStyle("-fx-text-box-border: red; -fx-focus-color: #ff000022;");
						createTextFieldClassName.setText("Invalid Critter Type");
					}
				}
				Critter.displayWorld(worldGrid);
				
			}
			
		});
		
		// Stats Action TODO
		String wordListStats = statsTextField.getText(); //The user input
		statsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				statsTextField.setStyle(null);
				int result = updateStats();	//0 No error, 1 Invalid Critter, 2 Unknown Error
				if(result == 1) {
					statsTextField.setStyle("-fx-text-box-border: red; -fx-focus-color: #ff000022;");
					statsTextField.setText("Invalid Critter Type");
				}
				else if(result == 2) {
					statsTextField.setStyle("-fx-text-box-border: red; -fx-focus-color: #ff000022;");
					statsTextField.setText("Error");
				}
			}
				
			
			
		});
		
		// Clear Action
		clearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) { //On the click of the button, it should exit;
				Critter.clearWorld();
				Critter.displayWorld(worldGrid);
			}
		});
		
		// Quit Action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) { //On the click of the button, it should exit;
				System.exit(0);
			}
		});
		
		//================================================================================
	    // Create SideBar Drawer
	    //================================================================================
		
		Button hideTab = createTabButton("Hide Settings", true);
		Button showTab = createTabButton("Show Settings", true);
		hideTab.getStyleClass().add("drawer");
		showTab.getStyleClass().add("drawer");
		//Show the settings
		showTab.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) { //On the click of the button, it should exit;
				functions.getChildren().remove(hiddenSettingsPane);
				functions.setLeft(settingsPane);
			}
		});
		//Hide the settings
		hideTab.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) { //On the click of the button, it should exit;
				functions.getChildren().remove(settingsPane);
				functions.setLeft(hiddenSettingsPane);
			}
		});
		
		Button hideTabStats = createTabButton("Hide Stats");
		Button showTabStats = createTabButton("Show Stats");
		hideTabStats.getStyleClass().add("drawer");
		showTabStats.getStyleClass().add("drawer");
		//Show the settings
		showTabStats.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) { //On the click of the button, it should exit;
				functions.getChildren().remove(hiddenStatsPane);
				functions.setRight(statsPane);
			}
		});
		//Hide the settings
		hideTabStats.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) { //On the click of the button, it should exit;
				functions.getChildren().remove(statsPane);
				functions.setRight(hiddenStatsPane);
			}
		});
		
		
		//================================================================================
	    // Set Up for View
	    //================================================================================
		
		//Add all settings button to a Grid Pane
		settingsPane.add(animateButton, 0, 0);
		settingsPane.add(stepButton, 0, 1);
		settingsPane.add(seedButton, 0, 2);
		settingsPane.add(createButton, 0, 3);
		settingsPane.add(statsButton, 0, 4);
		settingsPane.add(clearButton, 0, 5);
		settingsPane.add(quitButton, 0, 6);
		settingsPane.add(hideTab, 1, 0);
		GridPane.setHalignment(hideTab, HPos.LEFT);
		GridPane.setValignment(hideTab, VPos.TOP);
		settingsPane.setPrefWidth(SETTINGS_WIDTH + STAGE_HEIGHT/6);
		settingsPane.setAlignment(Pos.TOP_LEFT);
		
		//Add show tab button to seperate grid pane
		hiddenSettingsPane.add(showTab, 0, 0);
		GridPane.setHalignment(showTab, HPos.LEFT);
		GridPane.setValignment(showTab, VPos.TOP);
		hiddenSettingsPane.setPrefWidth(SETTINGS_WIDTH);
		hiddenSettingsPane.setAlignment(Pos.TOP_LEFT);
		
		//Scroll pane
		statsScrollPane.setContent(statsLabel);
		statsScrollPane.setPrefSize(STATS_WIDTH, STATS_HEIGHT);
		statsPane.add(statsScrollPane, 0, 1); 
		statsPane.add(hideTabStats, 0, 0);
		GridPane.setHalignment(hideTabStats, HPos.RIGHT);
		GridPane.setValignment(hideTabStats, VPos.TOP);
		statsPane.setAlignment(Pos.BOTTOM_RIGHT);
		statsPane.setPrefSize(STATS_WIDTH + STAGE_HEIGHT/6, STATS_HEIGHT);
		
		hiddenStatsPane.add(showTabStats, 0, 0);
		GridPane.setHalignment(showTabStats, HPos.RIGHT);
		GridPane.setValignment(showTabStats, VPos.CENTER);
		hiddenStatsPane.setPrefSize(STATS_WIDTH, STATS_HEIGHT);
		hiddenStatsPane.setAlignment(Pos.BOTTOM_RIGHT);

		// Create overlaying functions pane (Settings & Stats Panels)
		functions.setLeft(hiddenSettingsPane);
		functions.setRight(hiddenStatsPane);
		functions.setPrefSize(STAGE_WIDTH, STAGE_HEIGHT);
		
		//Settings for displaying world grid
		worldGrid.setPrefSize(STAGE_WIDTH, STAGE_HEIGHT);
		//worldGrid.setBackground(Background.fill(Color.GREEN));	//For debug of grid
		worldGrid.setBackground(Background.fill(Color.web("#344B61")));
		root.getChildren().add(worldGrid);	//World grid behind functionality
		root.getChildren().add(functions);
		Scene scene = new Scene(root, STAGE_WIDTH, STAGE_HEIGHT);
		mainStage.setScene(scene);
	    scene.getStylesheets().add(getLink("styles.css"));		
		
    	mainStage.show();
	}
/**
 * Creates a setting button with 3 parameters text, content, and large
 * 
 * @param text text of the button
 * @param content content inside the button
 * @param large ratio of the button
 * @return returns a new Settings button 
 */
	public Button createSettingsButton(String text, Node content, Boolean large) {
		Button newButton = new Button (text, content);
		int ratio = 12;	//Small button
		if (large) {
			ratio = 6;	//Large button
		}
		//Font, size, etc
		newButton.setPrefSize(SETTINGS_WIDTH, STAGE_HEIGHT/ratio);
		newButton.setTextAlignment(TextAlignment.CENTER);
		newButton.setContentDisplay(ContentDisplay.BOTTOM);
		return newButton;
	}
	
	/**
	 * Creates a setting button with 2 parameters text and large
	 * 
	 * @param text text of the button
	 * @param large ratio of the button
	 * @return returns a new Settings button
	 */
	public Button createSettingsButton(String text, Boolean large) {
		Button newButton = new Button (text);
		int ratio = 12;	//Small button
		if (large) {
			ratio = 6;	//Large button
		}
		newButton.setPrefSize(SETTINGS_WIDTH, STAGE_HEIGHT/ratio);
		newButton.setTextAlignment(TextAlignment.CENTER);
		newButton.setContentDisplay(ContentDisplay.BOTTOM);
		return newButton;
	}
	
	/**
	 * Creates a sub Button
	 * 
	 * @param imageLocation Location of the Image
	 * @param width the width of the image
	 * @param height the height of the button
	 * @return
	 */
	public Button createSubButton(String imageLocation, int width, int height) {
		ImageView newImage = new ImageView(new Image(imageLocation, width, height, true, true));
		Button newButton = new Button();
		newButton.setGraphic(newImage);
		return newButton;
	}
	/** 
	 * Creates a text field Prompt
	 * 
	 * @param  text prompt for the text field
	 * @return returns the new Text Field
	 */
	public TextField createTextFieldPrompt(String prompt) {
		TextField newTextField = new TextField("");
		newTextField.setPromptText(prompt);
		return newTextField;
	}
	/**
	 * Creates a tab button with text and left parameters
	 * 
	 * @param text text of the button
	 * @param left boolean value to determine offset of the button 
	 * @return returns a new Tab button
	 */
	public Button createTabButton(String text, Boolean left) {
		int angle;
		int offsetX;
		int offsetY;
		if(left) {
			angle = 90;
			offsetY = 50;
			offsetX = 0;
		}
		else {
			angle = -90;
			offsetY = 50;
			offsetX = (int) (STAGE_HEIGHT/6);
		}
		Button newButton = new Button(text);
		newButton.setPrefHeight(50);
		newButton.setPrefWidth(STAGE_HEIGHT/6);
		Rotate rotate = new Rotate();
		rotate.setPivotX(newButton.getLayoutX() + offsetX); //Pivot X Top-Left corner
		rotate.setPivotY(newButton.getLayoutY() + offsetY); //Pivot Y
		rotate.setAngle(angle); //Angle degrees
		newButton.getTransforms().add(rotate);
		return newButton;
	}
	
	/**
	 * Creates a tab button with one text parameter
	 * 
	 * @param text text of the button
	 * @return returns a new Tab button
	 */
	public Button createTabButton(String text) {
		Button newButton = new Button(text);
		newButton.setPrefHeight(50);
		newButton.setPrefWidth(STAGE_HEIGHT/6);
		return newButton;
	}
	
	/**
	 * Updates the stats of everything happening on critterGrid 
	 * 
	 * @return returns integer signifying error 
	 */
	public static int updateStats() {
		String wordListStats = statsTextField.getText();
		if (wordListStats.equals(null) || wordListStats.equals("")) {
			wordListStats = "Critter";
		}
		List<Critter> critterList = new ArrayList<Critter>();
		try {
			 critterList = Critter.getInstances(wordListStats); // Get all instances of that critter type
			 Method runStatsForClass = Class.forName(Main.myPackage + "." + wordListStats).getMethod("runStats", List.class);	// Get runStats method for that critter type
			 String output = (String) runStatsForClass.invoke(null, critterList);	// Run/Invoke runStats method with critterList. Note: null argument because runStats is static
			 statsLabel.setText(output);
		} catch (InvalidCritterException e) {
			return 1;
		} catch(Exception e) {
			//e.printStackTrace();
			return 2;
		}
		return 0;
	}
	/**
	 * 
	 * @param link the link
	 * @return returns the link 
	 */
	public String getLink(String link) {
		java.net.URL url = this.getClass().getResource(link);
	    if (url == null) {
	        System.out.println("Resource not found. Aborting.");
	        System.exit(-1);
	    }
	    return url.toExternalForm(); 
	}
}
