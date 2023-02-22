package assignment2;

//Class to manage pegs and count result of guess
public class Pegs {

	private int blackPegs;
	private int whitePegs;
	public Code secretCode;
	public Code userCode;
	
	public Pegs(String guess, String secret) {
		this.blackPegs = 0;
		this.whitePegs = 0;
		this.secretCode = new Code(secret);
		this.userCode = new Code(guess);
	}
	
	public void calculatePegs() {
		//Calculate number of black pegs
		for (int i = 0; i < userCode.length; i++) {
			if(userCode.codeArray[i].equals(secretCode.codeArray[i]) && userCode.isAvailable(i) && secretCode.isAvailable(i)) {
				blackPegs ++;
				//Make chars unavailable after they've been counted
				userCode.makeUnavailable(i);
				secretCode.makeUnavailable(i);
			}
		}
		//Calculate number of white pegs
		for (int i = 0; i < userCode.length; i++) {
			for(int j = 0; j < secretCode.length; j++) {
				if(userCode.codeArray[i].equals(secretCode.codeArray[j]) && userCode.isAvailable(i) && secretCode.isAvailable(j)) {
					whitePegs ++;
					//Make chars unavailable after they've been counted
					userCode.makeUnavailable(i);
					secretCode.makeUnavailable(j);
				}
			}
		}
	}
	
	//Format pegs for output
	public String toString() {
		return (blackPegs + "b_" + whitePegs + "w");
	}
	
	//Has the user guessed correctly
	public Boolean pegsFilled() {
		return blackPegs == userCode.length;
	}
}
