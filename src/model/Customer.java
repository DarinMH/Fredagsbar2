
package model;

import java.util.List;

public class Customer {
	

	private int studentId;
	private String firstName;
	private String lastName;
	private String email;
	private int numberOfCustomers; 
	public Customer() {
		
	}
	
	public Customer(int studentId) {
	this.studentId = studentId;;
	}
	
	public Customer(int studentId, String firstName, String lastName, String email, int numberOfCustomers) {
		this.studentId = studentId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.numberOfCustomers=numberOfCustomers; 

	}
	
	
	// Getters and setters
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
	
	public void setNumberOfCustomers (int numberOfCustomers) {
		this.numberOfCustomers=numberOfCustomers; 
	} 
	
	public int getNumberOfCustomers() {
		return numberOfCustomers;
	}
	public void setCustomerCategory(int numberOfCustomers) {
		this.numberOfCustomers = numberOfCustomers;
	}	
public int getCustomerCategory (int amount) {
	return amount; 
}

public void setReservations(List<Reservation> reservations) {
	
}
	}
	
	
