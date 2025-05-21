package model;

public class Supplier {
	private int supplierId;
	private String name;
	private String  email;
	private String phoneNr;
	
	public Supplier(int supplierId, String name, String email, String phoneNr) {
		this.supplierId = supplierId;
		this.name = name;
		this.email = email;
		this.phoneNr = phoneNr;
	}
	
	
	// Getters and setters

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNr() {
		return phoneNr;
	}

	public void setPhoneNr(String phoneNr) {
		this.phoneNr = phoneNr;
	}
	

}
