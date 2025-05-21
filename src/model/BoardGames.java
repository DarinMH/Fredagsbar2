package model;

public class BoardGames extends BorrowableProduct{
	

	private double compensationalPrice;
	
	public BoardGames(double compensationalPrice, Reservation reservation, int amount, String productName, int productId,
			String productType, boolean status) {
		super(amount, productName, productId, reservation, productType, status);
	
		this.compensationalPrice = compensationalPrice;
	}
	
	
	// Getters and setters



	public double getCompensationalPrice() {
		return compensationalPrice;
	}

	public void setCompensationalPrice(double compensationalPrice) {
		this.compensationalPrice = compensationalPrice;
	}
	



}
