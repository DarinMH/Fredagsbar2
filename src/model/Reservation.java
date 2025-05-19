package model;

import java.time.*;

public class Reservation {

	private int reservationId;
	private LocalDate date;
	private Customer customer;
	
	public Reservation(int reservationId, LocalDate date, int amount, Customer customer) {
		super();
		this.reservationId = reservationId;
		this.date = date;
		this.customer = customer;
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
	
}
