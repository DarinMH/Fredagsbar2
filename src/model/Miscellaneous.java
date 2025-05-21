package model;

public class Miscellaneous extends Product {
	private int amount;

	public Miscellaneous(int amount, String productName, double purchasePrice, double salePrice, int productId, 
			String productType, Supplier supplier) {
		super (productName, purchasePrice, salePrice, productId, productType, supplier);
		this.amount = amount;
	}
	
	
	// Getters and setters

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
