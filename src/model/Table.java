package model;

public class Table extends BorrowableProduct{
	
	private int tableNr;
	private int seatAmount;
	
	
	public Table() {
		
	}
	
	public Table(int productId) {
		super(productId);
	
	}
	
	public Table(int tableNr, int seatAmount, int amount, String productType, boolean status, 
			String productName, int productId) {
		super(amount, productName, productId, productType, status);
		this.tableNr = tableNr;
		this.seatAmount = seatAmount;

		
		
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

}
