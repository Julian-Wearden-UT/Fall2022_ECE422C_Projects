/*
* EE422C Final Project submission by
* Replace <...> with your actual data.
* <Julian Wearden>
* <jfw864>
* <17615>
* Fall 2022
*/

package eHillsClientSide;

public class User{
	
	private String username;
	private String password;
	
	public User(){}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public User(boolean guest) {
		this.username = "Guest";
		this.password = null;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	@Override
	public String toString(){
		return "{\"username\":" + '"' + this.username + '"' +  "," +
				"\"password\":" + '"' + this.password + '"' +  "}";	
				
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj.getClass() != this.getClass()) {
			return false;
		}
		
		final User other = (User) obj;
		if(this.username.equals(other.username) && this.password.equals(other.password)) {
			return true;
		}
		
		return false;
	}
	
	
}
