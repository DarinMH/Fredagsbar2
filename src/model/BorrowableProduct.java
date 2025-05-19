package model;

public abstract class BorrowableProduct {
	private int amount;
	private String productName;
	private int productId;
	
	public BorrowableProduct(int amount, String productName, int productId) {
		super();
	this.amount = amount;
	this.productName = productName;
	this.productId = productId;
	}

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


	

}
