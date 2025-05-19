package controller;

import java.time.LocalDate;
import java.util.List;

import DB.DataAccessException;
import DB.ReservationDBIF;
import model.BorrowableProduct;
import model.Customer;
import model.Reservation;

public class ReservationCtr {
private ReservationDBIF reservationDB; 
private CustomerCtr customerCtr;
private BorrowableProduct borrowableProduct; 

public ReservationCtr() throws DataAccessException { 
	customerCtr = new CustomerCtr(); 
	borrowableProductCtr = new BorrowableProductCtr(); 
}
	
public boolean confirmReservation() throws DataAccessException { 
	this.currentReservation.setStatus(true); 
	reservationDB.update(currentReservation, false); 
	
	if (currentReservation.isStatus() == true) { 
		
		return true;
	}
	
		return false; 
}

public List<Reservation> findAll throws DataAccessException { 
	return reservationDB.findAll(false); 
}

public Reservation createReservation (int reservationId, LocalDate date, int amount, Customer custome) throws DataAccessException {
	
	Reservation newReservation = new Reservation (reservationId, amount, Customer); 
	
	reservationDB.insertReservation(newReservation, false); 
	this.currentReservation = newReservation; 
	
	return newReservation; 
}  

} 
