/*
* EE422C Final Project submission by
* Replace <...> with your actual data.
* <Julian Wearden>
* <jfw864>
* <17615>
* Fall 2022
*/

package eHillsClientSide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClientController {

	boolean DEBUG = true;	//TODO REMOVE
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	private static String currentUser;
	public static ArrayList<Item> itemList;
	private static boolean  loggedIn;
	public static int loginStatus; //-1 failed login, 0 message not recieved, 1 successful login
	public static Client client;
	
	//SplashPage
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private MenuItem quit;
    
    @FXML
    private ImageView logo;

    @FXML
    void sendToLogInPage(ActionEvent event) throws IOException {
    	root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	scene = new Scene(root);
    	stage.setScene(scene);
    	stage.show();
    }
    
    @FXML
    void initialize() {
    	loggedIn = false;
    	loginStatus = 0;
    	InputStream file;
		//file = new FileInputStream("\\images\\eHillsLogo.png");
		Image image = new Image(getLink("images" + File.separator + "eHillsLogo.png"));
		logo.setImage(image);
    	
    }
    
    public String getLink(String link) {
		java.net.URL url = this.getClass().getResource(link);
	    if (url == null) {
	        System.out.println("Resource not found. Aborting.");
	        System.exit(-1);
	    }
	    return url.toExternalForm(); 
	}
    
    @FXML
    void quitProgram(ActionEvent event) {
    	System.exit(0);
    }
    
    //LoginPage
    @FXML
    private Button continueAsGuestButton;

    @FXML
    private Button logInButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    void continueAsGuest(ActionEvent event) throws IOException {
    	ClientController.currentUser = "Guest";
    	//Switch scenes
    	FXMLLoader fxmlLoader = new FXMLLoader();
		root = fxmlLoader.load(getClass().getResource("MainPage1.fxml").openStream());
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	scene = new Scene(root);
    	MainPageController controller = fxmlLoader.getController();
    	Client.mainPageController = controller;
    	controller.initData(stage, scene, root, currentUser, itemList, loggedIn, loginStatus, client);
    	stage.setScene(scene);
    	stage.show();
    	System.out.println("Continuing as Guest");
    	//initializeMainPage();
    }

    @FXML
    void logIn(ActionEvent event) throws IOException {
    	if(usernameField.getText() == null || passwordField.getText() == null || usernameField.getText() == "" || passwordField.getText() == "") {
    		showInvalidLoginAlert();
    	}
    	Message message = new Message("login", usernameField.getText(), passwordField.getText());
    	client.sendToServer(message);
    	while(loginStatus == 0) {}	//busy wait for message from server
    	if(loginStatus == 1) {
    		ClientController.currentUser = usernameField.getText();
    		//Switch scenes
    		loggedIn = true;
    		FXMLLoader fxmlLoader = new FXMLLoader();
    		root = fxmlLoader.load(getClass().getResource("MainPage1.fxml").openStream());
        	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        	scene = new Scene(root);
        	MainPageController controller = fxmlLoader.getController();
        	Client.mainPageController = controller;
        	controller.initData(stage, scene, root, currentUser, itemList, loggedIn, loginStatus, client);
        	stage.setScene(scene);
        	stage.show();
        	System.out.println("Log in successful");
        	
        	
    	}
    	else {
    		loggedIn = false;
    		loginStatus = 0;
    		showInvalidLoginAlert();
    	}
    }
    
    @FXML
    void showInvalidLoginAlert() {
    	Alert invalidLoginAlert = new Alert(AlertType.ERROR);
		invalidLoginAlert.setTitle("Invalid Login");
		invalidLoginAlert.setHeaderText("The username or password you entered is incorrect");
		invalidLoginAlert.setGraphic(null);
		Button okButton = (Button) invalidLoginAlert.getDialogPane().lookupButton( ButtonType.OK );
	    okButton.setText("Retry");
		invalidLoginAlert.showAndWait();
    }
    
    

}




