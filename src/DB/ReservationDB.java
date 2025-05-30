package DB;

import model.BoardGames;
import model.BorrowableProduct;
import model.Customer;
import model.Reservation;
import model.Table;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDB implements ReservationDBIF {

	// SQL statements for performing common database operations on the table.
    private static final String FIND_ALL_Q = 
        "SELECT r.reservationId, r.date, r.amount, r.StudentId, r.Status, r.productId, " +
        "c.FirstName, c.LastName, c.StudentEmail, c.numberOfCustomers, " +
        		" b.ProductType AS ProductType " +
        "FROM Reservation r " +
        "LEFT JOIN Customer c ON r.StudentId = c.StudentId " +
        "LEFT JOIN BorrowableProduct b ON r.productId = b.productId";
//	private static final String FIND_ALL_Q = "Select * from Reservation"; 
    private static final String FIND_BY_ID_Q = FIND_ALL_Q + " WHERE r.reservationId = ?";
private static final String INSERT_RESERVATION_Q = "insert into Reservation(ReservationId, Date, Amount, StudentId, Status, productId) values(?, ?, ?, ?, ?, ?)"; 
private static final String UPDATE_Q = "update Reservation set Date = ?, Amount = ?, StudentId = ?, Status = ?, productId = ? where ReservationId = ?"; 
// PreparedStatements to safely execute the SQL queries with parameters in the Java code.
    private PreparedStatement findAll;
    private PreparedStatement findById;
    private PreparedStatement insertReservation; 
    private PreparedStatement update; 
    private DBConnection connection; 
    private CustomerDBIF customerDB; 
    private BorrowableProductDBIF borrowableProductDB; 
    
    public ReservationDB() throws DataAccessException {
    	customerDB = new CustomerDB(); 
    	borrowableProductDB = new BorrowableProductDB(); 
    	init(); 
    }
    
    
    public ReservationDB(CustomerDBIF customerDB) throws DataAccessException {
    	this.customerDB=customerDB; 
    	init(); 
    }
    
    public ReservationDB(BorrowableProductDBIF borrowableProductDB) throws DataAccessException {
    	this.borrowableProductDB=borrowableProductDB; 
    	init(); 
    }

    
    // Prepares the connection and prepares the SQL statements to be executed. .  
    private void init() throws DataAccessException {
        try {
//            Connection con = DBConnection.getInstance().getConnection();
        	connection = DBConnection.getInstance(); 
        	findAll = connection.getConnection().
        	prepareStatement(FIND_ALL_Q);  
          	findById = connection.getConnection().
                	prepareStatement(FIND_BY_ID_Q);  
          	insertReservation = connection.getConnection().
                	prepareStatement(INSERT_RESERVATION_Q);  
          	update = connection.getConnection().prepareStatement(UPDATE_Q); 
        	
//            findAll = con.prepareStatement(FIND_ALL_Q);
//            findById = con.prepareStatement(FIND_BY_ID_Q);
//            insertReservation = con.prepareStatement(INSERT_RESERVATION_Q); 
        } catch (SQLException e) {
            throw new DataAccessException(e, "Kunne ikke forberede statements i ReservationDB");
        }
    }

    
   
    // finds all the reservations from the database
	@Override
	public List<Reservation> findAll(boolean fullAssociation) throws DataAccessException {
        try {
            ResultSet rs = findAll.executeQuery();
            List<Reservation> res = buildObjects (rs, true);
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
                return buildObject(rs, true);
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
            list.add(buildObject(rs, true));
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
    		  String productType = rs.getString("ProductType"); 
    	    	BorrowableProduct product = null; 
    	    	if(productType != null) {
    	    	switch(productType) {
    	    	case "boardGames": product = new BoardGames(rs.getInt("productId")); 
    	    	break; 
    	    	case "tablee": product = new Table(rs.getInt("productId")); 
    	    	break; 
    	    	default:
    	    	}
    	    	}
    	reservation.setReservationId(rs.getInt("reservationId")); 
    	reservation.setDate(rs.getDate("date").toLocalDate());
    	reservation.setAmount(rs.getInt("amount")); 
    	reservation.setCustomer(new Customer (rs.getInt("StudentId"))); 
    	reservation.setStatus(rs.getBoolean("Status"));
//    	reservation.setBorrowableProduct(new BorrowableProduct (rs.getInt("productId")));
    	reservation.setBorrowableProduct(product);
    	if (fullAssociation) { 
    		Customer customer = this.customerDB.findByStudentId(reservation.getCustomer().getStudentId(), true);
    		
    		reservation.setCustomer(customer); 
    		
    		
    	}
    	
      	if (fullAssociation && product != null) { 
    		BorrowableProduct bProduct = borrowableProductDB.findByProductId(reservation.getBorrowableProduct().getProductId(), true);
    		
    		reservation.setBorrowableProduct(bProduct); 
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
		if(reservation.getAmount() != 0) {
			insertReservation.setInt(3, reservation.getAmount());
		} else {
			insertReservation.setNull(3, java.sql.Types.INTEGER);
		}
		
		if(reservation.getCustomer() != null) {
			insertReservation.setInt(4, reservation.getCustomer().getStudentId());
		} else {
			insertReservation.setNull(4, java.sql.Types.INTEGER);
		}
 
		insertReservation.setBoolean(5, reservation.isStatus());
		if(reservation.getBorrowableProduct() != null) {
			insertReservation.setInt(6, reservation.getBorrowableProduct().getProductId());
		} else {
			insertReservation.setNull(6, java.sql.Types.INTEGER);
		}
        insertReservation.executeUpdate();
		
        connection.commitTransaction();

	
		} catch(Exception e){
				connection.rollbackTransaction();
				throw new DataAccessException(e, "save order failed");

	}

}
	
	@Override 
	public void update(Reservation reservation) throws DataAccessException {
		
		final int reservationId = reservation.getReservationId(); 
		final LocalDate date = reservation.getDate(); 
		final int amount = reservation.getAmount(); 
		final int customer = reservation.getCustomer().getStudentId(); 
		final boolean status = reservation.isStatus(); 
		final Integer borrowableProduct = (reservation.getBorrowableProduct() != null) ? reservation.getBorrowableProduct().getProductId() : null; 
		
		try {
		update.setDate(1, java.sql.Date.valueOf(date)); 
		update.setInt(2, amount);
		update.setInt(3, customer);
		update.setBoolean(4, status);
		if(reservation.getBorrowableProduct() != null) {
			update.setInt(5, borrowableProduct);
		} else {
			update.setNull(5, java.sql.Types.INTEGER);
		}

		update.setInt(6, reservationId);
		
		update.executeUpdate(); 
		
		
		} catch(SQLException e) {
			throw new DataAccessException(e, "Could not update " + reservationId); 
		 
		}
	
	}


    
} 

