package model;

public class Buyer extends Staff{
	
	private String email; 

	public Buyer(String username, String password, String firstName, String lastName, String role, String email) {
		super(username, password, firstName, lastName, role);
		this.email=email; 
		// TODO Auto-generated constructor stub
	}
	
	
	// Getters and setters

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
