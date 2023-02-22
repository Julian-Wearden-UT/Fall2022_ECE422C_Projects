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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Item implements Serializable{
	
	public class HistoryEntry implements Serializable{
		private static final long serialVersionUID = 1L;
		String bidder;
		Double bid;
		HistoryEntry(){}
		HistoryEntry(String bidder, Double bid){
			this.bidder = bidder;
			this.bid = bid;
		}
		public String getBidder() {
			return bidder;
		}
		public void setBidder(String bidder) {
			this.bidder = bidder;
		}
		public Double getBid() {
			return bid;
		}
		public void setBid(Double bid) {
			this.bid = bid;
		}
		@Override
		public String toString() {
			return "[\"" + this.bidder + "\", " + this.bid +  "]";
		}
	}

	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private Double minAcceptableBid;
	private Double minStartingPrice;
	private Double currentTopBid;
	private String currentTopBidder;
	private Double buyItNowPrice;
	private Integer bidDuration;
	private String imagePath;
	private Boolean sold;
	private ArrayList<HistoryEntry> history;
	
	
	
	public Item() {}
	public Item(String name, String description, Double minAcceptableBid, Double minStartingPrice, Double currentTopBid, 
			String currentTopBidder, Double buyItNowPrice, Integer bidDuration, String imagePath) {
		this.name = name;
		this.description = description;
		this.minAcceptableBid = minAcceptableBid;
		this.minStartingPrice = minStartingPrice;
		this.currentTopBid = currentTopBid;
		this.currentTopBidder = currentTopBidder;
		this.buyItNowPrice = buyItNowPrice;
		this.bidDuration = bidDuration;
		this.imagePath = imagePath;
		this.sold = false;
		this.history = new ArrayList<HistoryEntry>();
	}


	public String getName() {
		return name;
	}


	public String getDescription() {
		return description;
	}


	public Double getMinAcceptableBid() {
		return minAcceptableBid;
	}


	public Double getMinStartingPrice() {
		return minStartingPrice;
	}
	
	public void setMinAcceptableBid(Double minAcceptableBid) {
		this.minAcceptableBid = minAcceptableBid;
	}


	public Double getCurrentTopBid() {
		return currentTopBid;
	}


	public void setCurrentTopBid(Double currentTopBid) {
		this.currentTopBid = currentTopBid;
	}


	public String getCurrentTopBidder() {
		return currentTopBidder;
	}


	public void setCurrentTopBidder(String currentTopBidder) {
		this.currentTopBidder = currentTopBidder;
	}


	public Double getBuyItNowPrice() {
		return buyItNowPrice;
	}


	public Integer getBidDuration() {
		return bidDuration;
	}


	public void setBidDuration(Integer bidDuration) {
		this.bidDuration = bidDuration;
	}


	public String getImagePath() {
		return imagePath;
	}

	public Boolean sold() {
		return sold;
	}

	public void sold(Boolean sold) {
		this.sold = sold;
	}
	
	public ArrayList<HistoryEntry> getHistory() {
		return this.history;
	}
	
	public void initHistory() {
		this.history = new ArrayList<HistoryEntry>();
	}
	
	public void addToHistory(String bidder, Double bid) {
		HistoryEntry historyEntry = new HistoryEntry(bidder, bid);
		history.add(historyEntry);
	}
	
	public void startTimer() {
		Runnable timer = new Runnable() {
	    public void run() {
	    		if(getBidDuration() > 0) {
	    			setBidDuration(getBidDuration() - 1);
	    		}
	    		else {
	    			sold(true);
	    		}
		    }
		};
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(timer, 0, 1, TimeUnit.SECONDS);
	}

	@Override
	public String toString(){
		return "{\"name\":" + '"' + this.name + '"' + "," +
				"\"description\":" + '"' + this.description + '"' + "," +
				"\"minAcceptableBid\":" + this.minAcceptableBid + "," +
				"\"minStartingPrice\":" + this.minStartingPrice + "," +
				"\"currentTopBid\":" + this.currentTopBid + "," +
				"\"currentTopBidder\":" + '"' + this.currentTopBidder + '"' + "," +
				"\"buyItNowPrice\":" + this.buyItNowPrice + "," +
				"\"bidDuration\":" + this.bidDuration + "," +
				"\"imagePath\":" + '"' + this.imagePath + '"' + "," +
				"\"history\":" + '"' + this.history + '"' + "," +
				"\"sold\":" + this.sold +  "}";	
	}
	



	
}
