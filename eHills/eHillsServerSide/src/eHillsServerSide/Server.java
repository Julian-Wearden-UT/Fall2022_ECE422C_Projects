/*
* EE422C Final Project submission by
* Replace <...> with your actual data.
* <Julian Wearden>
* <jfw864>
* <17615>
* Fall 2022
*/

package eHillsServerSide;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

import com.google.gson.Gson;

class Server extends Observable {
	
	private Database database;
	
	public static void main(String[] args) {
		new Server().runServer();
	}

	private void runServer() {
		database = new Database("itemList", "userList");

		try {
			setUpNetworking();
		} catch (Exception e) {
			System.out.println("Server already running");
			System.exit(0);
			return;
		}
		
  }

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(4242);
		while (true) {
			Socket clientSocket = serverSock.accept();
			System.out.println("Connecting to... " + clientSocket);
			ClientHandler handler = new ClientHandler(this, clientSocket);
			this.addObserver(handler);
			Thread t = new Thread(handler);
			t.start();
		}
	}

	protected void processRequest(String input, Socket clientSocket) throws IOException {
		PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream());
		Gson gson = new Gson();
		Message message = gson.fromJson(input, Message.class);
		Message output = new Message("Error");
		try {
			switch (message.type) {

			case "login":
				User tempUser = new User(message.username, message.password);
				System.out.println("Login read correctly" + message.username + message.password);
				if(database.userInList(tempUser)) {
					output = new Message("valid_login");
					
				}
				else {
					output = new Message("invalid_login");
				}
				toClient.println(gson.toJson(output));
				toClient.flush();
				break;
			case "bid":
				if(message.bid <= message.item.getCurrentTopBid() || message.bid < message.item.getMinAcceptableBid()) {
					output = new Message("invalid_bid");
				}
				else if(message.bid >= message.item.getBuyItNowPrice()) {
					database.updateItem(message.item, message.username, true);
					output = new Message("sold");
					toClient.println(gson.toJson(output));
					toClient.flush();
					this.setChanged();
					this.notifyObservers(gson.toJson(new Message("items_update", database.getItems())));
				}
				else {
					database.updateItem(message.item, message.bid, message.username);
					output = new Message("bid_placed");
				}
				toClient.println(gson.toJson(output));
				toClient.flush();
				this.setChanged();
				this.notifyObservers(gson.toJson(new Message("items_update", database.getItems())));
				break;
				
			case "get_items":
				output = new Message ("items_update", database.getItems());
				toClient.println(gson.toJson(output));
				toClient.flush();
				break;
				
			case "buy_now":
				database.updateItem(message.item, message.username, true);
				output = new Message("sold");
				toClient.println(gson.toJson(output));
				toClient.flush();
				this.setChanged();
				this.notifyObservers(gson.toJson(new Message("items_update", database.getItems())));
				break;
				
			case "get_time":
				output = new Message("time_update", database.getItems());
				toClient.println(gson.toJson(output));
				toClient.flush();
				this.setChanged();
				this.notifyObservers(gson.toJson(new Message("time_update", database.getItems())));
				break;
			default:
				System.out.println("A server side message error occured");
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	}