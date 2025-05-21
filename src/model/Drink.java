package model;

public class Drink extends Product {
	private double alcoholPercentage;
	private String brand;
	private String category;
	
	

	public Drink(String productName, double purchasePrice, double salePrice, int productId, String productType, Supplier supplier,
			double alcoholPercentage, String brand, String category) {
		super(productName, purchasePrice, salePrice, productId, productType, supplier);
		// TODO Auto-generated constructor stub
		this.alcoholPercentage = alcoholPercentage;
		this.brand = brand;
	}
	
	
	// Getters and setters
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public double getAlcoholPercentage() {
		return alcoholPercentage;
	}
	public void setAlcoholPercentage(double alcoholPercentage) {
		this.alcoholPercentage = alcoholPercentage;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	

}
