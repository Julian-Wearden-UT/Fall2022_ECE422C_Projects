/* MULTITHREADING <BookingClient.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * <Julian Wearden>
 * <jfw864>
 * <17615>
 * Slip days used: <0>
 * Fall 2021
 */

package assignment6;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import assignment6.Flight.Seat;
import assignment6.Flight.SeatClass;
import assignment6.Flight.Ticket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.lang.Thread;

public class BookingClient {
	
	public Object lock;
	private BlockingQueue<Object> printQueue;
	private Map<String, SeatClass[]> offices;
	public Boolean soldOut;
	public Boolean soldOutPrinted;
	Flight flight;
	
	
	/**
     * @param offices maps ticket office id to seat class preferences of customers in line
     * @param flight the flight for which tickets are sold for
     */
    public BookingClient(Map<String, SeatClass[]> offices, Flight flight) {
        // Initialize variables
    	this.lock = new Object();
    	this.printQueue = new LinkedBlockingQueue<Object>();
    	this.offices = offices;
    	this.soldOut = false;
    	this.soldOutPrinted = false;
    	this.flight = flight;
    	
    }

    /**
     * Starts the ticket office simulation by creating (and starting) threads
     * for each ticket office to sell tickets for the given flight
     *
     * @return list of threads used in the simulation,
     * should have as many threads as there are ticket offices
     */
    public List<Thread> simulate() {
    	List<Thread> officeThreads = new ArrayList<Thread>();
    	int firstCustomer = 1;	//firstcustomer will be 1 for the first office, and then length of next office + 1 for next one
    	
    	//Create each thread
    	for(String office : offices.keySet()) {
    		officeThreads.add(new Thread(new IssueTickets(offices.get(office), firstCustomer, office)));
    		firstCustomer += offices.get(office).length;
    	}
    	
    	//Start each thread
    	for (Thread officeThread : officeThreads) {
    		officeThread.start();
    	}
    	
        return officeThreads;
    }

    public static void main(String[] args) {
    	//Initialize to example output
		Map<String, SeatClass[]> offices = new HashMap<String, SeatClass[]>() {{
            put("TO1", new SeatClass[] {SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS});
            put("TO3", new SeatClass[] {SeatClass.BUSINESS, SeatClass.ECONOMY, SeatClass.ECONOMY});
            put("TO2", new SeatClass[] {SeatClass.FIRST, SeatClass.BUSINESS, SeatClass.ECONOMY, SeatClass.ECONOMY});
            put("TO5", new SeatClass[] {SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS});
            put("TO4", new SeatClass[] {SeatClass.ECONOMY, SeatClass.ECONOMY, SeatClass.ECONOMY});

        }};

        Flight flight = new Flight("TR123", 1, 1, 1);
        BookingClient bookingClient = new BookingClient(offices, flight);

        bookingClient.simulate();
    }
    
    private class IssueTickets implements Runnable{
    	private int firstCustomer; //Where to start counting customers for this thread
    	private String officeID;
    	private SeatClass[] seatClassPreferences; //Preferences for this thread
    	private int currentCustomer; //Keeps track of current customer
    	
        IssueTickets(SeatClass[] seatClassPreferences, int firstCustomer, String officeID){
    		this.seatClassPreferences = seatClassPreferences;
    		this.firstCustomer = firstCustomer;
    		this.currentCustomer = firstCustomer;
    		this.officeID = officeID;
    	}
    	
		@Override
		public void run() {
			// Not sold out and not last customer in thread
			while(!soldOut && this.currentCustomer < this.firstCustomer + this.seatClassPreferences.length) {
				Seat seat = flight.getNextAvailableSeat(seatClassPreferences[currentCustomer - this.firstCustomer]);
				
				// No seat available
				if(seat == null) {
					soldOut = true;
				}
				// Seat available, add to queue
				else {
					try {
						printQueue.put(seat);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				synchronized(lock) {
					if(soldOut) {
						//Sold out message
						if(!soldOutPrinted) {
							soldOutPrinted = true;
							// Add to queue so sold out message is printed at the end
							try {
								printQueue.put("Sorry, we are sold out!");
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					// Print ticket
					if(printQueue.peek() != null) {
						//If it's a seat
						if(printQueue.peek() instanceof Seat) {
							flight.printTicket(officeID, (Seat) printQueue.poll(), currentCustomer); //Print ticket
							this.currentCustomer ++; //Get next customer in thread
						}
						//Otherwise print sold out message
						else {
							System.out.println(printQueue.poll());
//							List<Ticket> x = flight.getTransactionLog();	// DEBUG Purposes: Uncomment 2 lines to get transaction log (Should print tickets in order of print)
//							System.out.println(x);
//							List<Seat> y = flight.getSeatLog();			// DEBUG Purposes: Uncomment 2 lines to get seat log (Should print seats in order)
//							System.out.println(y);
						}
					}
				}
			}
		}
    	
    }
}
