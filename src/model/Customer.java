
package model;

import java.util.List;

public class Customer {
	

	private int studentId;
	private String firstName;
	private String lastName;
	private String email;
	private int amount; 
	public Customer() {
		
	}
	
	public Customer(int studentId) {
	this.studentId = studentId;;
	}
	
	public Customer(int studentId, String firstName, String lastName, String email, int amount) {
		this.studentId = studentId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.amount=amount; 

	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setAmount (int amount) {
		this.amount=amount; 
	} 
	
	public int getAmount() {
		return amount;
	}
	public void setCustomerCategory(int amount) {
		this.amount = amount;
	}	
public int getCustomerCategory (int amount) {
	return amount; 
}

public void setReservations(List<Reservation> reservations) {
	
}
	}
	
	
