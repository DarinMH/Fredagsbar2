package model;

public abstract class BorrowableProduct {
	private int amount;
	private String productName;
	private int productId;
	private Reservation reservation; 
	private String productType; 
	private boolean status;
	
	public BorrowableProduct(int amount, String productName, int productId, Reservation reservation, String productType, boolean status) {
		super();
	this.amount = amount;
	this.productName = productName;
	this.productId = productId;
	this.reservation = reservation; 
	this.productType = productType; 
	this.status=status;  

	}
	
	// Getters and setters

	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public String getProductName() {
		return productName;
	}	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	public Reservation getReservation () {
		return reservation; 
	}
	
	public void setReservation (Reservation reservation) { 
		this.reservation=reservation;
	}
	
	public String getproductType () {
		return productType;
	}
	 public void setProductType (String productType) {
		 this.productType =productType; 
	}
	
	public void setStatus(boolean status) {
		this.status=status; 
	}
	
	public boolean getStatus() {
		return status; 
	}


	

}
