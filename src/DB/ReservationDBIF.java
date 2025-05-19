package DB;

import model.Reservation;
import java.util.List;

// Interface til at arbejde med reservationer i databasen
public interface ReservationDBIF {

    // Henter alle reservationer
    List<Reservation> findAll() throws DataAccessException;

    // Finder én reservation baseret på ID
    Reservation findByReservationId(int reservationId) throws DataAccessException;
    
    void insertReservation(Reservation reservation) throws DataAccessException; 
}
