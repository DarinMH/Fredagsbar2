package model;

import java.time.LocalDate;

public class PurchaseOrder {
	
	private double totalPrice;
	private boolean status;
	private LocalDate date;
	private double discountPercentage;
	private Buyer buyer;
	
	
	public PurchaseOrder(double totalPrice, boolean status, LocalDate date, double discountPercentage, Buyer buyer) {
		super();
		this.totalPrice = totalPrice;
		this.status = status;
		this.date = date;
		this.discountPercentage = discountPercentage;
		this.buyer = buyer;
	}



	public Buyer getBuyer() {
		return buyer;
	}



	public void setBuyer(Buyer buyer) {
		this.buyer = buyer;
	}



	public double getTotalPrice() {
		return totalPrice;
	}


	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}


	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}


	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate date) {
		this.date = date;
	}


	public double getDiscountPercentage() {
		return discountPercentage;
	}


	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	

	
	
}
