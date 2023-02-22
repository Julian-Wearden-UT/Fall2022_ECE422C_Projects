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
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import eHillsClientSide.Item.HistoryEntry;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainPageController{
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	private static String currentUser;
	public static ArrayList<Item> itemList;
	private static boolean  loggedIn;
	public static int loginStatus; //-1 failed login, 0 message not recieved, 1 successful login
	public static Client client;
	private static final DecimalFormat df = new DecimalFormat("0.00");

	
	//MainPage
    @FXML
    private TableColumn<HistoryEntry, Double> amountColumn;

    @FXML
    private Text bidHistoryText;

    @FXML
    private TableColumn<HistoryEntry, String> bidderColumn;

    @FXML
    private Button buyItNowButton;

    @FXML
    private Text buyItNowPriceText;

    @FXML
    private Text currentBidAmountText;

    @FXML
    private Text currentBidText;

    @FXML
    private Text descriptionText;

    @FXML
    private ImageView itemImage;

    @FXML
    private Text itemNameText;

    @FXML
    private Button logOutButton;

    @FXML
    private ImageView logoImage;

    @FXML
    private Text minAmountText;

    @FXML
    private Button placeBidButton;

    @FXML
    private Text priceText;

    @FXML
    private ComboBox<String> productsDropDown;

    @FXML
    private Text soldText;

    @FXML
    private Text timeLeftText;

    @FXML
    private Text timeText;

    @FXML
    private TextField userBidTextField;

    @FXML
    private Text userGreetingText;
    
    @FXML
    private MenuItem quit;
    
    @FXML
    private TableView<HistoryEntry> tableView;
    
    @FXML
    private ImageView itemImageView;
    
    @FXML
    private Rectangle imageRectangle;
    
    private MediaPlayer mediaPlayerBG;
    
    
    void initData(Stage stage, Scene scene, Parent root, String currentUser, ArrayList<Item> itemList, boolean loggedIn, int loginStatus, Client client) {
    	this.stage = stage;
    	this.scene = scene;
    	this.root = root;
    	MainPageController.currentUser = currentUser;
    	MainPageController.itemList = itemList;
    	MainPageController.loggedIn = loggedIn;
    	MainPageController.loginStatus = loginStatus; //-1 failed login, 0 message not recieved, 1 successful login
    	MainPageController.client = client;
    	descriptionText.setWrappingWidth(700.0);
    	
    	//Start Background Music
    	String fileName = "background.mp3";
    	Media audio = new Media(getLink("audio" + File.separator + fileName));
    	mediaPlayerBG = new MediaPlayer(audio);
    	mediaPlayerBG.setVolume(0.05);
    	mediaPlayerBG.play();
    	
    	initializeMainPage();
    }
    
    @FXML
    void placeBid(ActionEvent event) {
    	try {
	    	Message message = new Message("bid", getCurrentItem(), Double.parseDouble(userBidTextField.getText()), currentUser);
	    	client.sendToServer(message);
    	}catch(NumberFormatException e) {
    		invalidBidAlert();
    	}
    }
    
    @FXML
    void buyItNow(ActionEvent event) {
    	Message message = new Message("buy_now", getCurrentItem(), 0.0, currentUser);
    	client.sendToServer(message);
    	playAudio("success.mp3");
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
    	mediaPlayerBG.stop();
    	loggedIn = false;
    	ClientController.loginStatus = 0;
    	root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	scene = new Scene(root);
    	stage.setScene(scene);
    	stage.show();
    }
    
    @FXML
    void quitProgram(ActionEvent event) {
    	System.exit(0);
    }

    
    @FXML
    void initializeMainPage() {
    
    	if(currentUser.equals("Guest")) {
    		logOutButton.setText("Log In");
    	}
    	userGreetingText.setText("Hello, " + currentUser);
    	client.sendToServer(new Message("get_items"));
    	Image image = new Image(getLink("images" + File.separator + "eHillsLogoSmall.png"));
		logoImage.setImage(image);
		
    	
    	//Initialize DropBox
    	while(itemList == null) {}//Busy wait for items to load
    	
    	for(Item item:itemList) {
    		productsDropDown.getItems().add(item.getName());
    	}
    	productsDropDown.getSelectionModel().selectFirst();
    	showItem(null);
    	
    	//Update timer every second
    	Runnable timer = new Runnable() {
		    public void run() {
		    	Message message = new Message("get_time");
		    	client.sendToServer(message);
		    }
		};
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(timer, 0, 1, TimeUnit.SECONDS);
    	
    }
    
    
    @FXML
    void showItem(ActionEvent event) {
    	Item currItem = getCurrentItem();
    	
    	itemNameText.setText(currItem.getName());
		descriptionText.setText(currItem.getDescription());
		if(currItem.getCurrentTopBid() == 0.0) {
			currentBidAmountText.setText("US $" + df.format(currItem.getMinStartingPrice()));
		}
		else {
			currentBidAmountText.setText("US $" + df.format(currItem.getCurrentTopBid()));
		}
		minAmountText.setText("Enter US $" + df.format(currItem.getMinAcceptableBid()) + " or more");
		buyItNowPriceText.setText("US $" + df.format(currItem.getBuyItNowPrice()));
		String x = getLink("images" + File.separator + currItem.getImagePath());
		Image image2 = new Image(getLink("images" + File.separator + currItem.getImagePath()));
		itemImageView.setImage(image2);
		//timeText.setText(currItem.getBidDuration().toString());
		updateTimer();
		
		
		if(currItem.getHistory() != null) {
			updateBidHistoryList();
		}
		
		
		
		
    	if(!currItem.sold()) {
    		soldText.setOpacity(0);
    		imageRectangle.setFill(Color.TRANSPARENT);
			imageRectangle.setOpacity(0);
			currentBidText.setText("Current bid:");
			userBidTextField.setVisible(true);
			minAmountText.setVisible(true);
			placeBidButton.setVisible(true);
			buyItNowButton.setVisible(true);
			timeText.setVisible(true);
			timeLeftText.setVisible(true);
    	}
    }
    
    @FXML
    public void updateTimer() {
    	Item currItem = getCurrentItem();
    	timeText.setText(currItem.getBidDuration().toString());
    	if(currItem.getBidDuration() <= 0) {
    		updateBidHistoryList();
    	}
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
    void bidPlacedAlert() {
    	playAudio("success.mp3");
    	Alert bidPlacedAlert = new Alert(AlertType.INFORMATION);
    	bidPlacedAlert.setTitle("Congratulations on your Bid!");
    	bidPlacedAlert.setHeaderText("Your bid was processed successfully");
    	bidPlacedAlert.setGraphic(null);
		Button okButton = (Button) bidPlacedAlert.getDialogPane().lookupButton( ButtonType.OK );
		bidPlacedAlert.showAndWait();
    }
    

    
    @FXML
    void playAudio(String fileName) {
    	Media audio = new Media(getLink("audio" + File.separator + fileName));
    	MediaPlayer mediaPlayer = new MediaPlayer(audio);
    	if(fileName.equals("background.mp3")) {
    		mediaPlayer.setVolume(0.15);
    	}
    	else {
    		mediaPlayer.setVolume(1);
    	}
    	mediaPlayer.play();
    }
    
    @FXML
    void invalidBidAlert() {
    	playAudio("fail.mp3");
    	Alert invalidBidAlert = new Alert(AlertType.INFORMATION);
    	invalidBidAlert.setTitle("Invalid bid");
    	invalidBidAlert.setHeaderText("Your bid failed");
    	invalidBidAlert.setGraphic(null);
		Button okButton = (Button) invalidBidAlert.getDialogPane().lookupButton( ButtonType.OK );
		okButton.setText("Retry");
		invalidBidAlert.showAndWait();
    }
    
    @FXML
    Item getCurrentItem() {
    	String itemSelected = productsDropDown.getValue();
    	for (Item item: itemList) {
    		if(item.getName().equals(itemSelected)) {
    			return item;
    		}
    	}
		return null;
    }
    
    @FXML
    void updateBidHistoryList() {
        ObservableList<HistoryEntry> list = FXCollections.observableArrayList();
        Item currItem = getCurrentItem();
        if(currItem != null && currItem.getHistory() != null) {
	        list.addAll(currItem.getHistory());
			bidderColumn.setCellValueFactory(new PropertyValueFactory<HistoryEntry, String>("bidder"));
			amountColumn.setCellValueFactory(new PropertyValueFactory<HistoryEntry, Double>("bid"));
			tableView.setItems(list);
			tableView.refresh();
			if(currItem.getCurrentTopBid() == 0.0) {
				currentBidAmountText.setText("US $" + df.format(currItem.getMinStartingPrice()));
			}
			else {
				currentBidAmountText.setText("US $" + df.format(currItem.getCurrentTopBid()));
			}			minAmountText.setText("Enter US $" + df.format(currItem.getMinAcceptableBid()) + " or more");
			if (currItem.sold()) {
				soldText.setOpacity(1);
				imageRectangle.setFill(Color.BLACK);
				imageRectangle.setOpacity(.5);
				currentBidText.setText("Winning bid:");
				currentBidAmountText.setText("US $" + df.format(currItem.getCurrentTopBid()) + " by " + currItem.getCurrentTopBidder());
				userBidTextField.setVisible(false);
				minAmountText.setVisible(false);
				placeBidButton.setVisible(false);
				buyItNowButton.setVisible(false);
				timeText.setVisible(false);
				timeLeftText.setVisible(false);
				
			}
        }
        
    }

}
