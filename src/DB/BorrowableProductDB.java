package DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.BoardGames;
import model.BorrowableProduct;
import model.Product;
import model.Reservation;
import model.Table;

public class BorrowableProductDB implements BorrowableProductDBIF {	
	
	// SQL statements for performing common database operations on the table.
    private static final String FIND_ALL_Q = 
        "SELECT bp.productId, bp.productName, bp.amount, bp.status, bp.productType, " +
        "       COALESCE(bg.compensationalPrice, 0) AS compensationalPrice, " +
        "       COALESCE(t.tableNr, 0) AS tableNr, " +
        "       COALESCE(t.seatAmount, 0) AS seatAmount " +
        "FROM BorrowableProduct bp " +
        "LEFT JOIN boardGames bg ON bp.productId = bg.productId " +
        "LEFT JOIN tablee t ON bp.productId = t.productId";
    private static final String FIND_BY_ID_Q = FIND_ALL_Q + " WHERE bp.productId = ?";
    private static final String UPDATE_STATUS_Q = "update BorrowableProduct set status = ?, amount = ? where productId = ?"; 
 // PreparedStatements to safely execute the SQL queries with parameters in the Java code
    private PreparedStatement findAll;
    private PreparedStatement findById;
    private PreparedStatement updateStatus; 
    private DBConnection con; 

    // Prepares the connection and prepares the SQL statements to be executed. 
    public BorrowableProductDB() throws DataAccessException {
        try {
        con = DBConnection.getInstance(); 
            findAll = con.getConnection().prepareStatement(FIND_ALL_Q);
            findById = con.getConnection().prepareStatement(FIND_BY_ID_Q);
            updateStatus = con.getConnection().prepareStatement(UPDATE_STATUS_Q); 
        } catch (SQLException e) {
            throw new DataAccessException(e, "Kunne ikke forberede statements i BorrowableProductDB");
        }
    }

    // finds all the objects from the database
    @Override
    public List<BorrowableProduct> findAll() throws DataAccessException {
    	
        try {
            ResultSet rs = findAll.executeQuery();
            return buildObjects(rs, false);
        } catch (SQLException e) {
            throw new DataAccessException(e, "Kunne ikke hente alle lånbare produkter");
        }
    }

    // finds a specific object from the database based on productID 
    @Override
    public BorrowableProduct findByProductId(int productId, boolean fullAssociation) throws DataAccessException {
        try {
            findById.setInt(1, productId);
            ResultSet rs = findById.executeQuery();
            if (rs.next()) {
                return buildObject(rs, false);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e, "Kunne ikke finde produkt med ID = " + productId);
        }
        return null;
    }

	// Builds an object from the current row of the ResultSet.
	// If fullAssociation is true, it fetches and sets the full object.
    private BorrowableProduct buildObject(ResultSet rs, boolean fullAssociation) throws SQLException {
    

     String productType = rs.getString("productType"); 
        BorrowableProduct product = null;

      ;
		switch (productType.toLowerCase()) { 
        case "boardgames": product = new BoardGames(
        
        		
        		rs.getDouble("compensationalPrice"),
        		rs.getInt("amount"), 
        		rs.getString ("productName"), 
        		rs.getInt("productId"), 
        		rs.getString("productType"),
        		rs.getBoolean("status")
        		); 
          
        break; 
        case "tablee": product = new Table ( 
        		rs.getInt("tableNr"),
        		rs.getInt("seatAmount"),
        		 rs.getInt("amount"),
        		 rs.getString("productType"), 
        		 rs.getBoolean("status"),
                 rs.getString("productName"),
                 rs.getInt("productId")
             );
        
      break; 
      default: 
    	  System.out.println ("Unknown product type: " + productType);
    	  product = null; //set borrowable product to null for unknown types
    	  break;
		}
        return product;
    }

	// Converts all rows in the ResultSet into a list of objects.
 	private List<BorrowableProduct> buildObjects(ResultSet rs, boolean fullAssociation) throws SQLException {
 		List<BorrowableProduct> res = new ArrayList<>(); 
 		while(rs.next()) {
 			res.add(buildObject(rs, fullAssociation)); 
 		}
 		return res; 
 	
 	}

	@Override
	public void updateStatus(BorrowableProduct product) throws DataAccessException {
		final boolean status = product.getStatus(); 
		final int amount = product.getAmount(); 
		final int productId = product.getProductId(); 
		
		try {
		updateStatus.setBoolean(1, status); 
		updateStatus.setInt(2, amount);
		updateStatus.setInt(3, productId);
		updateStatus.executeUpdate(); 
		
		}catch(SQLException e) {
			throw new DataAccessException(e, "Could not update "); 
		
	}
	}
}  