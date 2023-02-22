/*
* EE422C Final Project submission by
* Replace <...> with your actual data.
* <Julian Wearden>
* <jfw864>
* <17615>
* Fall 2022
*/

package eHillsClientSide;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Client extends Application{
	

  //private static String host = "ec2-3-16-23-232.us-east-2.compute.amazonaws.com";
  private static String host = "127.0.0.1";
  private static BufferedReader fromServer;
  private static PrintWriter toServer;
  private Scanner consoleInput = new Scanner(System.in);
  public boolean loggedIn = false;
  private Gson gson = new Gson();
  static ClientController clientController;
  static MainPageController mainPageController;

  public static void main(String[] args) {
	  try {
		  new Client().setUpNetworking();
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
	  launch(args);
  }
  @Override
  public void start(Stage primaryStage) throws Exception {
	  //fx:controller="eHillsClientSide.ClientController"
      FXMLLoader fxmlLoader = new FXMLLoader();
      Parent root = fxmlLoader.load(getClass().getResource("SplashPage.fxml").openStream());
      Scene scene = new Scene(root,1600, 900);
	  clientController = fxmlLoader.getController();
	  ClientController.client = this;
      primaryStage.setScene(scene);
      primaryStage.show();
  }
  

  private void setUpNetworking() throws Exception {
    @SuppressWarnings("resource")
    Socket socket = new Socket(host, 4242);
    System.out.println("Connecting to... " + socket);
    Client.fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    Client.toServer = new PrintWriter(socket.getOutputStream());

    Thread readerThread = new Thread(new Runnable() {
      @Override
      public void run() {
        String input;
        try {
          while ((input = fromServer.readLine()) != null) {
            //System.out.println("From server: " + input);
			Message message = gson.fromJson(input, Message.class);
            processRequest(message);
          }
        } catch (Exception e) {
          System.out.println("Server Error: Restart the Server and Try Again.");
        }
      }
    });

    Thread writerThread = new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          String input = consoleInput.nextLine();
          String[] variables = input.split(",");
          Message request = new Message();
          GsonBuilder builder = new GsonBuilder();
          Gson gson = builder.create();
          sendToServer(request);
        }
      }
    });

    readerThread.start();
    writerThread.start();
  }

  protected void processRequest(Message message) {
		try {
			switch (message.type) {
			case "valid_login":
				ClientController.loginStatus = 1;
				break;
				
			case "invalid_login":
				ClientController.loginStatus = -1;
				break;
				
			case "invalid_bid":
				Platform.runLater(new Runnable() {
				    @Override
				    public void run() {
				        mainPageController.invalidBidAlert();
				    }
				});
				break;
				
			case "bid_placed":
				Platform.runLater(new Runnable() {
				    @Override
				    public void run() {
				    	mainPageController.bidPlacedAlert();
				    }
				});
				break;
				
			case "items_update":
				MainPageController.itemList = message.itemList;
		    	mainPageController.updateBidHistoryList();
				break;
				
			case "sold":
				MainPageController.itemList = message.itemList;
		    	mainPageController.updateBidHistoryList();
				break;
				
			case "time_update":
				Platform.runLater(new Runnable() {
				    @Override
				    public void run() {
				    	MainPageController.itemList = message.itemList;
				    	if(mainPageController != null) {
				    		mainPageController.updateTimer();
				    	}
				    }
				});
				break;
				
			default:
				System.out.println("A client side message error occured");
				break;
			}

		} catch (Exception e) {
			System.out.println("A message error occured");
		}
	}
  

  protected void sendToServer(Message message) {
    toServer.println(gson.toJson(message));
    toServer.flush();
  }

}