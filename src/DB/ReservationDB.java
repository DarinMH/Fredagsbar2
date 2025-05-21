package DB;

import model.Customer;
import model.Reservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDB implements ReservationDBIF {

	// SQL statements for performing common database operations on the table.
    private static final String FIND_ALL_Q = 
        "SELECT r.reservationId, r.date, r.amount, r.customerId, " +
        "c.firstName, c.lastName, c.email, c.amount AS customerAmount " +
        "FROM Reservation r " +
        "JOIN Customer c ON r.customerId = c.customerId";
    private static final String FIND_BY_ID_Q = FIND_ALL_Q + " WHERE r.reservationId = ?";
private static final String INSERT_RESERVATION_Q = "insert into Reservation(ReservationId, Date, Amount, StudentId, Status) values(?, ?, ?, ?, ?)"; 
// PreparedStatements to safely execute the SQL queries with parameters in the Java code.
    private PreparedStatement findAll;
    private PreparedStatement findById;
    private PreparedStatement insertReservation; 
    private DBConnection connection; 
    private CustomerDB customerDB; 

    
    // Prepares the connection and prepares the SQL statements to be executed. .  
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

    
   
    // finds all the reservations from the database
	@Override
	public List<Reservation> findAll(boolean fullAssociation) throws DataAccessException {
        try {
            ResultSet rs = findAll.executeQuery();
            List<Reservation> res = buildObjects (rs, false);
            System.out.println("Number of objects found: " + res.size()); 
            return res;
        } catch (SQLException e) {
            DataAccessException d = new DataAccessException(e, "Kunne ikke hente alle reservationer");
            throw d;
        }
	}
  

    @Override
    // finds one reservation based on the reservation ID
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


	// Converts all rows in the ResultSet into a list of objects.
    private List<Reservation> buildObjects(ResultSet rs, boolean fullAssociation) throws DataAccessException {
        List<Reservation> list = new ArrayList<>();
        try {
        while (rs.next()) {
            list.add(buildObject(rs, false));
        }} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		throw new DataAccessException(e, "could not build"); 
    	} 
    	return list; 
    	}
       
	// Builds an object from the current row of the ResultSet.
	// If fullAssociation is true, it fetches and sets the full object.
    private Reservation buildObject(ResultSet rs, boolean fullAssociation) throws DataAccessException {
    	Reservation reservation = new Reservation();
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
    
    
    // Reservation objects are being inserted and persisted in the database. 
	@Override
	public void insertReservation(Reservation reservation) throws DataAccessException {

		try {
		connection.startTransaction();
		
		insertReservation.setInt(1, reservation.getReservationId());
		insertReservation.setDate(2, java.sql.Date.valueOf(reservation.getDate()));
		insertReservation.setInt(3, reservation.getAmount());
		insertReservation.setInt(4, reservation.getCustomer().getStudentId()); 
		insertReservation.setBoolean(5, reservation.isStatus());
        insertReservation.executeUpdate();
		
        connection.commitTransaction();

	
		} catch(Exception e){
				connection.rollbackTransaction();
				throw new DataAccessException(e, "save order failed");

	}

}


    
} 

