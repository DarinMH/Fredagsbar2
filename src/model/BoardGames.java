package model;

public class BoardGames extends BorrowableProduct{
	
	private int boardGameId;
	private String name;
	private double compensationalPrice;
	private Reservation reservation;
	
	public BoardGames(int boardGameId, String name, double compensationalPrice, int amount, String productName, int productId, Reservation reservation, String productType, boolean status) {
	    super(amount, productName, productId, reservation, productType, status);
	    this.boardGameId = boardGameId;
	    this.name = name;
	    this.compensationalPrice = compensationalPrice;
	    this.reservation = reservation;
	}


	public int getBoardGameId() {
		return boardGameId;
	}

	public void setBoardGameId(int boardGameId) {
		this.boardGameId = boardGameId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCompensationalPrice() {
		return compensationalPrice;
	}

	public void setCompensationalPrice(double compensationalPrice) {
		this.compensationalPrice = compensationalPrice;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

}
