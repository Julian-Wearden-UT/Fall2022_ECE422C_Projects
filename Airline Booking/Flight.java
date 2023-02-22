/* MULTITHREADING <Flight.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * <Julian Wearden>
 * <jfw864>
 * <17615>
 * Slip days used: <0>
 * Fall 2021
 */
package assignment6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Flight {
	private String flightNo;
	private int[] curRows;// Holds current row of each class [current first row, current business row, current economy row]
	private SeatLetter[] curLetters;// Holds next free seat for each class [next first seat, next business seat, next economy seat]
	private int[] lastRow; //Holds last row for each class [last row first, last row business, last row economy]
	private Object lock; //printTicket sync
    private SeatLetter[][] classSeats; // Holds seats for each class (ABEF for first, ABCDEF for business & economy)
    private SeatClass[] classes; //Holds types of classes (first, business, economy)
    private int printDelay; // Delay time used to print tickets
    private SalesLogs log;

    public Flight(String flightNo, int firstNumRows, int businessNumRows, int economyNumRows) {
    	this.printDelay = 50; // 50 ms. Use it to fix the delay time between prints.
    	this.log = new SalesLogs();
    	this.flightNo = flightNo;
    	this.lock = new Object();
    	this.classes = new SeatClass[] {SeatClass.FIRST, SeatClass.BUSINESS, SeatClass.ECONOMY};
    	
    	//Initialize seat letters available for each class
    	this.classSeats = new SeatLetter[3][];
    	this.classSeats[SeatClass.FIRST.getIntValue()] = new SeatLetter[] {SeatLetter.A, SeatLetter.B, SeatLetter.E, SeatLetter.F};
    	this.classSeats[SeatClass.BUSINESS.getIntValue()] = new SeatLetter[] {SeatLetter.A, SeatLetter.B, SeatLetter.C, SeatLetter.D, SeatLetter.E, SeatLetter.F};
    	this.classSeats[SeatClass.ECONOMY.getIntValue()] = new SeatLetter[] {SeatLetter.A, SeatLetter.B, SeatLetter.C, SeatLetter.D, SeatLetter.E, SeatLetter.F};
    	
    	//Initialize last row of each class
    	this.lastRow = new int[3];
    	this.lastRow[SeatClass.FIRST.getIntValue()] = firstNumRows;
    	this.lastRow[SeatClass.BUSINESS.getIntValue()] = firstNumRows + businessNumRows;
    	this.lastRow[SeatClass.ECONOMY.getIntValue()] = firstNumRows + businessNumRows + economyNumRows;
    	
    	//Initialize with first row of each class
    	this.curRows = new int[3];
    	this.curRows[SeatClass.FIRST.getIntValue()] = firstNumRows == 0 ? 0 : 1; //Row where First class starts. If no first rows, set to 0
    	this.curRows[SeatClass.BUSINESS.getIntValue()] = businessNumRows == 0 ? 0 : firstNumRows + 1; //Row where Business class starts. If no business rows, set to 0
    	this.curRows[SeatClass.ECONOMY.getIntValue()]= economyNumRows == 0 ? 0: firstNumRows + businessNumRows + 1;	//Row where Economy class starts. If no economy rows, set to 0
    	
    	this.curLetters = new SeatLetter[] {SeatLetter.A,SeatLetter.A,SeatLetter.A}; // Next seat for first, business, and economy class respectively
    }
    
    public void setPrintDelay(int printDelay) {
        this.printDelay = printDelay;
    }

    public int getPrintDelay() {
        return printDelay;
    }

    /**
     * Returns the next available seat not yet reserved for a given class
     *
     * @param seatClass a seat class(FIRST, BUSINESS, ECONOMY)
     * @return the next available seat or null if flight is full
     */
	public Seat getNextAvailableSeat(SeatClass seatClass) {
		//Synchronize grabbing of next available seat & updating
		synchronized(this) {
			int classType = seatClass.getIntValue();
			int nextAvailableRow = this.curRows[classType];
			SeatLetter nextAvailableLetter = this.curLetters[classType];
			Seat newSeat = new Seat(seatClass, nextAvailableRow, nextAvailableLetter);
			
			////Downgrade user////
			//No more seats in this class (all rows full)
			if(nextAvailableRow == 0) {
				//Can't downgrade from economy
				if(seatClass == SeatClass.ECONOMY) {
					return null; //Flight is full
				}
				//call same function with downgraded seat class
				else {
					return getNextAvailableSeat(classes[classType + 1]);
				}
			}
			
			////Update for next call to function////
			//Last Seat in Row
			if (nextAvailableLetter.getIntValue() >= SeatLetter.F.getIntValue()) {
				//Last Row in Class
				if(nextAvailableRow == lastRow[classType]) {
					curRows[classType] = 0;	//0 signifies no rows left
				}
				//Class not full, but last seat in row
				else {
					this.curRows[classType] += 1;	//Next row
					this.curLetters[classType] = SeatLetter.A;	//Set to A
				}
			}
			//Row not full, just update letter
			else {
				int currIndex = Arrays.asList(classSeats[classType]).indexOf(nextAvailableLetter); //Get index of current letter (Mainly needed for First class)
				this.curLetters[classType] = this.classSeats[classType][currIndex + 1]; //Get next letter in classSeats for classType
			}
			
			this.log.addSeat(newSeat);	//To make sure seats were assigned in the correct order
			return newSeat;
		}
	}

	/**
     * Prints a ticket to the console for the customer after they reserve a seat.
     *
     * @param seat a particular seat in the airplane
     * @return a flight ticket or null if a ticket office failed to reserve the seat
     */
	public Ticket printTicket(String officeId, Seat seat, int customer) {
        synchronized(lock) {
        	// Delay between prints
        	try {
				Thread.sleep(printDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	Ticket newTicket = new Ticket(this.flightNo, officeId, seat, customer);
        	System.out.println(newTicket);
		    this.log.addTicket(newTicket);	//To make sure they were printed in the correct order
	        return newTicket;
        }
    }

	/**
     * Lists all seats sold for this flight in the order of allocation
     *
     * @return list of seats sold
     */
    public List<Seat> getSeatLog() {
        return this.log.seatLog;
    }

    /**
     * Lists all tickets sold for this flight in order of printing.
     *
     * @return list of tickets sold
     */
    public List<Ticket> getTransactionLog() {
        return this.log.ticketLog;
    }
    
    static enum SeatClass {
		FIRST(0), BUSINESS(1), ECONOMY(2);

		private Integer intValue;

		private SeatClass(final Integer intValue) {
			this.intValue = intValue;
		}

		public Integer getIntValue() {
			return intValue;
		}
	}

	static enum SeatLetter {
		A(0), B(1), C(2), D(3), E(4), F(5);

		private Integer intValue;

		private SeatLetter(final Integer intValue) {
			this.intValue = intValue;
		}

		public Integer getIntValue() {
			return intValue;
		}
	}

	/**
     * Represents a seat in the airplane
     * FIRST Class: 1A, 1B, 1E, 1F ... 
     * BUSINESS Class: 2A, 2B, 2C, 2D, 2E, 2F  ...
     * ECONOMY Class: 3A, 3B, 3C, 3D, 3E, 3F  ...
     * (Row numbers for each class are subject to change)
     */
	static class Seat {
		private SeatClass seatClass;
		private int row;
		private SeatLetter letter;

		public Seat(SeatClass seatClass, int row, SeatLetter letter) {
			this.seatClass = seatClass;
			this.row = row;
			this.letter = letter;
		}

		public SeatClass getSeatClass() {
			return seatClass;
		}

		public void setSeatClass(SeatClass seatClass) {
			this.seatClass = seatClass;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public SeatLetter getLetter() {
			return letter;
		}

		public void setLetter(SeatLetter letter) {
			this.letter = letter;
		}

		@Override
		public String toString() {
			return Integer.toString(row) + letter + " (" + seatClass.toString() + ")";
		}
	}

	/**
 	 * Represents a flight ticket purchased by a customer
 	 */
	static class Ticket {
		private String flightNo;
		private String officeId;
		private Seat seat;
		private int customer;
		public static final int TICKET_STRING_ROW_LENGTH = 31;

		public Ticket(String flightNo, String officeId, Seat seat, int customer) {
			this.flightNo = flightNo;
			this.officeId = officeId;
			this.seat = seat;
			this.customer = customer;
		}

		public int getCustomer() {
			return customer;
		}

		public void setCustomer(int customer) {
			this.customer = customer;
		}

		public String getOfficeId() {
			return officeId;
		}

		public void setOfficeId(String officeId) {
			this.officeId = officeId;
		}
		
		@Override
		public String toString() {
			String result, dashLine, flightLine, officeLine, seatLine, customerLine, eol;

			eol = System.getProperty("line.separator");

			dashLine = new String(new char[TICKET_STRING_ROW_LENGTH]).replace('\0', '-');

			flightLine = "| Flight Number: " + flightNo;
			for (int i = flightLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				flightLine += " ";
			}
			flightLine += "|";

			officeLine = "| Ticket Office ID: " + officeId;
			for (int i = officeLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				officeLine += " ";
			}
			officeLine += "|";

			seatLine = "| Seat: " + seat.toString();
			for (int i = seatLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				seatLine += " ";
			}
			seatLine += "|";

			customerLine = "| Customer: " + customer;
			for (int i = customerLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				customerLine += " ";
			}
			customerLine += "|";

			result = dashLine + eol + flightLine + eol + officeLine + eol + seatLine + eol + customerLine + eol
					+ dashLine;

			return result;
		}
	}

	/**
	 * SalesLogs are security wrappers around an ArrayList of Seats and one of Tickets
	 * that cannot be altered, except for adding to them.
	 * getSeatLog returns a copy of the internal ArrayList of Seats.
	 * getTicketLog returns a copy of the internal ArrayList of Tickets.
	 */
	static class SalesLogs {
		private ArrayList<Seat> seatLog;
		private ArrayList<Ticket> ticketLog;

		private SalesLogs() {
			seatLog = new ArrayList<Seat>();
			ticketLog = new ArrayList<Ticket>();
		}

		@SuppressWarnings("unchecked")
		public ArrayList<Seat> getSeatLog() {
			return (ArrayList<Seat>) seatLog.clone();
		}

		@SuppressWarnings("unchecked")
		public ArrayList<Ticket> getTicketLog() {
			return (ArrayList<Ticket>) ticketLog.clone();
		}

		public void addSeat(Seat s) {
			seatLog.add(s);
		}

		public void addTicket(Ticket t) {
			ticketLog.add(t);
		}
	}
}
