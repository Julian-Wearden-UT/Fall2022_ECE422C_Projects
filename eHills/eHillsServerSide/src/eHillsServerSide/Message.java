/*
* EE422C Final Project submission by
* Replace <...> with your actual data.
* <Julian Wearden>
* <jfw864>
* <17615>
* Fall 2022
*/

package eHillsServerSide;

import java.io.Serializable;
import java.util.ArrayList;


class Message implements Serializable{

  private static final long serialVersionUID = 1L;
  String type;
  String input;
  int number;
  String username;
  String password;
  Item item;
  Double bid;
  ArrayList<Item> itemList;

  protected Message() {
    this.type = "";
    this.input = "";
    this.number = 0;
    //System.out.println("server-side message created");
  }
  
  protected Message(String type) {
	  this.type = type;
  }

  protected Message(String type, String input) {
    this.type = type;
    this.input = input;
  }
  
  //Pass through login
  protected Message(String type, String username, String password) {
	    this.type = type;
	    this.username = username;
	    this.password = password;
  }
  
  protected Message(String type, Item item, Double bid, String username) {
	  this.type = type;
	  this.item = item;
	  this.bid = bid;
	  this.username = username;
  }
  

  protected Message(String type, ArrayList<Item> itemList) {
    this.type = type;
    this.itemList = itemList;
  }
  
//  @Override
//	public String toString(){
//		return "{\"type\":" + '"' + this.type + '"' + "," +
//				"\"input\":" + '"' + this.input + '"' + "," +
//				"\"number\":" + this.number + "," +
//				"\"username\":" + '"' + this.username + '"' + "," +
//				"\"password\":" + '"' + this.password + '"' + "," +
//				"\"item\":"  + this.item + "," +
//				"\"bid\":" + this.bid + "," +
//				"\"itemList\":" + this.itemList +  "}";	
//				
//	}
  
}