package model;

public class Table extends BorrowableProduct{
	
	private int tableNr;
	private int seatAmount;
	private Reservation reservation;
	
	public Table(int tableNr, int seatAmount, int amount, String productType, boolean status, Reservation reservation, 
			String productName, int productId) {
		super(amount, productName, productId, reservation, productType, status);
		this.tableNr = tableNr;
		this.seatAmount = seatAmount;
		this.reservation = reservation;
		
		
		
		// Getters and setters
	}
	public int getTableNr() {
		return tableNr;
	}
	public void setTableNr(int tableNr) {
		this.tableNr = tableNr;
	}
	public int getSeatAmount() {
		return seatAmount;
	}
	public void setSeatAmount(int seatAmount) {
		this.seatAmount = seatAmount;
	}
	public Reservation getReservation() {
		return reservation;
	}
	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
	
}
