package DB;

import model.Customer;
import model.Reservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDB implements ReservationDBIF {

    // SQL-forespørgsler til at hente alle reservationer eller en specifik
    private static final String FIND_ALL_Q = 
        "SELECT r.reservationId, r.date, r.amount, r.customerId, " +
        "c.firstName, c.lastName, c.email, c.amount AS customerAmount " +
        "FROM Reservation r " +
        "JOIN Customer c ON r.customerId = c.customerId";

    private static final String FIND_BY_ID_Q = FIND_ALL_Q + " WHERE r.reservationId = ?";
private static final String INSERT_RESERVATION_Q = "insert into Reservation(ReservationId, Date, Amount, StudentId, Status) values(?, ?, ?, ?, ?)"; 
    private PreparedStatement findAll;
    private PreparedStatement findById;
    private PreparedStatement insertReservation; 
    private DBConnection connection; 
    private CustomerDB customerDB; 
    

    // Initialiserer databasen og forbereder SQL-statements
    public ReservationDB () throws DataAccessException {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            findAll = con.prepareStatement(FIND_ALL_Q);
            findById = con.prepareStatement(FIND_BY_ID_Q);
            insertReservation = con.prepareStatement(INSERT_RESERVATION_Q); 
        } catch (SQLException e) {
            throw new DataAccessException(e, "Kunne ikke forberede statements i ReservationDB");
        }
    }

    
   
    // Henter alle reservationer fra databasen
    public List<Reservation> findAll(boolean fullAssociation) throws DataAccessException {
        try {
            ResultSet rs = findAll.executeQuery();
            List<Reservation> res = buildObjects (rs);
            System.out.println("Number of objects found: " + res.size()); 
            return res;
        } catch (SQLException e) {
            DataAccessException d = new DataAccessException(e, "Kunne ikke hente alle reservationer");
            throw d;
        }
    }

    @Override
    // Finder én reservation ud fra reservationId
    public Reservation findByReservationId(int reservationId) throws DataAccessException {
        try {
            findById.setInt(1, reservationId);
            ResultSet rs = findById.executeQuery();
            if (rs.next()) {
                return buildObject(rs, false);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e, "Kunne ikke finde reservation med ID = " + reservationId);
        }
        return null;
    }

    // Konverterer resultatet fra databasen til en liste af Reservation-objekter
    private List<Reservation> buildObjects(ResultSet rs) throws SQLException {
        List<Reservation> list = new ArrayList<>();
        while (rs.next()) {
            list.add(buildObject(rs, false));
        }
        return list;
    }

    // Bygger et enkelt Reservation-objekt ud fra en række i resultatet
    private Reservation buildObject(ResultSet rs, boolean fullAssociation) throws DataAccessException {
    	Reservation reservation = new Reservation(0, null, 0, null, fullAssociation); 
    	try { 
    	reservation.setReservationId(rs.getInt("reservationId")); 
    	reservation.setDate(rs.getDate("date").toLocalDate());
    	reservation.setAmount(rs.getInt("amount")); 
    	reservation.setCustomer(new Customer (rs.getInt ("CustomerId"))); 
    	reservation.setStatus(rs.getBoolean("Status"));
    	if (fullAssociation) { 
    		Customer customer = this.customerDB.findByStudentId(reservation.getCustomer().getStudentId(), true);
    		
    		reservation.setCustomer(customer); 
    	}
    	} catch (SQLException e) {
    		
    		throw new DataAccessException (e, "could not build.."); 
    	}
    	return reservation; 
 

    }
	@Override
	public void insertReservation(Reservation reservation) throws DataAccessException {
		// TODO Auto-generated 	try {
		
		try {
		connection.startTransaction();
		
		insertReservation.setInt(1, reservation.getReservationId());
		insertReservation.setDate(2, java.sql.Date.valueOf(reservation.getDate()));
		insertReservation.setInt(3, reservation.getAmount());
		insertReservation.setInt(4, reservation.getCustomer().getStudentId()); 
		insertReservation.setBoolean(5, reservation.isStatus());
        insertReservation.executeUpdate();
		
        connection.commitTransaction();
			System.out.println("dbconnection.commitTransaction();");
			System.out.println("try: connection.commit();");
			System.out.println("finally: connection.setAutoCommit(true);");
	
		} catch(Exception e){
				connection.rollbackTransaction();
				
				System.out.println("dbconnection.rollbackTransaction();");
				System.out.println("try: connection.rollback();");
				System.out.println("finally: connection.setAutoCommit(true);");
				
				throw new DataAccessException(e, "save order failed");
				
	
		
	}

}


	@Override
	public List<Reservation> findAll() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}
    
    
} 

