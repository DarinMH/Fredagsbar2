package model;

import java.time.*;

public class Reservation {

	private int reservationId;
	private LocalDate date;
	private Customer customer;
	private int amount; 
	private boolean status; 
	private BorrowableProduct borrowableProduct; 
	
	public Reservation(int reservationId, LocalDate date, int amount, Customer customer,
			boolean status, BorrowableProduct borrowableProduct) {
		super();
		this.reservationId = reservationId;
		this.date = date;
		this.customer = customer;
		this.amount=amount; 
		this.status=status; 
		this.borrowableProduct = borrowableProduct; 
	}

	public int getReservationId() {
		return reservationId;
	}

	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}


	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public void setBorrowableProduct(BorrowableProduct borrowableProduct) { 
		this.borrowableProduct = borrowableProduct;
	}

	public BorrowableProduct getBorrowableProduct() {
		return borrowableProduct;
	}
	
	
	
	
	
	
}
