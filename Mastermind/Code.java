package assignment2;

//Class to manage codes
public class Code {

	public String[] codeArray;	//Contains each letter as one element of array
	public String[] codeAvailable;	//Contains "A" (Available) or "U" (Unavailable) for each element
	public int length;	//Length of array
	
	
	public Code(String code){
		this.codeArray = code.split("");
		this.codeAvailable = new String[code.length()];
		for(int i = 0; i < this.codeAvailable.length; i++) {
			this.codeAvailable[i] = "A";
		}
		this.length = code.length();
	}
	
	//Is element at position available?
	public Boolean isAvailable(int position) {
		return this.codeAvailable[position].equals("A");
	}
	
	//Make element at position unavailable
	public void makeUnavailable(int position) {
		this.codeAvailable[position] = "U";
	}
}
