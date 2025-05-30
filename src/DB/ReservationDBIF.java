package DB;

import model.Reservation;
import java.util.List;

public interface ReservationDBIF {

    // Finds all the reservations
	List<Reservation> findAll(boolean fullAssociation) throws DataAccessException; 

    // Finds one reservation based on the reservation ID
    Reservation findByReservationId(int reservationId) throws DataAccessException;
    
    // inserts the reservation into the database. 
    void insertReservation(Reservation reservation) throws DataAccessException;

    void update(Reservation reservation) throws DataAccessException; 

}
