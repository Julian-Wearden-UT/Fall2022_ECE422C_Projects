/*
* EE422C Final Project submission by
* Replace <...> with your actual data.
* <Julian Wearden>
* <jfw864>
* <17615>
* Fall 2022
*/

package eHillsServerSide;

import java.util.ArrayList;

public class Database {
	private static ArrayList<Item> itemList;
	private static ArrayList<User> userList;
	

	Database(String itemList, String userList) {
	    Database.itemList = (ArrayList<Item>) ReadJsonList.readJsonList(Item.class, itemList, true);
	    Database.userList = (ArrayList<User>) ReadJsonList.readJsonList(User.class, userList, false);
	    for(Item i:Database.itemList) {
	    	i.initHistory();
	    	i.startTimer();
	    }
	}
	
	
	public boolean userInList(User user) {
		return userList.contains(user);
	}

	public void updateItem(Item item, Double bid, String username) {
		for(Item i:itemList) {
			if(i.getName().equals(item.getName())) {
				i.setMinAcceptableBid(bid + 0.5);
				i.setCurrentTopBidder(username);
				i.setCurrentTopBid(bid);
				i.addToHistory(username, bid);
			}
		}
	}
	
	public void updateItem(Item item, String username, boolean Sold) {
		for(Item i:itemList) {
			if(i.getName().equals(item.getName())) {
				i.setCurrentTopBidder(username);
				i.setCurrentTopBid(i.getBuyItNowPrice());
				i.sold(true);
				i.addToHistory(username, i.getBuyItNowPrice());
			}
		}
	}
	
	public void updateUser(User user) {
		for(User i:userList) {
			if(i.equals(user)) {
				i.setPassword("hey");
			}
		}
	}
	
	public ArrayList<Item> getItems(){
		return itemList;
	}
	
	public Integer getTime(Item item) {
		for(Item i:itemList) {
			if(i.getName().equals(item.getName())) {
				return i.getBidDuration();
			}
		}
		return 0;
	}
	
}
