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

    // SQL-forespørgsel der henter alle lånbare produkter og forsøger at matche dem med BoardGame og Tabel
    private static final String FIND_ALL_Q = 
        "SELECT bp.productId, bp.productName, bp.amount, " +
        "       COALESCE(bg.boardGameId, 0) AS boardGameId, " +
        "       COALESCE(bg.compensationalPrice, 0) AS compensationalPrice, " +
        "       COALESCE(t.tableNr, 0) AS tableNr, " +
        "       COALESCE(t.seatAmount, 0) AS seatAmount " +
        "FROM BorrowableProduct bp " +
        "LEFT JOIN BoardGame bg ON bp.productId = bg.productId " +
        "LEFT JOIN Tablee t ON bp.productId = t.productId";

    // Samme som ovenfor, men filtrerer på ét bestemt produktId
    private static final String FIND_BY_ID_Q = FIND_ALL_Q + " WHERE bp.productId = ?";



    private PreparedStatement findAll;
    private PreparedStatement findById;

    // Forbereder forbindelsen og SQL-statements til databasen
    public BorrowableProductDB() throws DataAccessException {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            findAll = con.prepareStatement(FIND_ALL_Q);
            findById = con.prepareStatement(FIND_BY_ID_Q);
        } catch (SQLException e) {
            throw new DataAccessException(e, "Kunne ikke forberede statements i BorrowableProductDB");
        }
    }

    // Henter alle lånbare produkter fra databasen
    @Override
    public List<BorrowableProduct> findAll() throws DataAccessException {
    	
        try {
            ResultSet rs = findAll.executeQuery();
            return buildObjects(rs, false);
        } catch (SQLException e) {
            throw new DataAccessException(e, "Kunne ikke hente alle lånbare produkter");
        }
    }

    // Henter ét bestemt lånbart produkt ud fra produktId
    @Override
    public BorrowableProduct findByProductId(int productId) throws DataAccessException {
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

    // Går alle rækker i resultatet igennem og bygger en liste af produktobjekter


    private BorrowableProduct buildObject(ResultSet rs, boolean fullAssociation) throws SQLException {
    
//        int boardGameId = rs.getInt("boardGameId");
//        int tableNr = rs.getInt("tableNr");.
    	
     String productType = rs.getString("productType"); 
        BorrowableProduct product = null;

      ;
		switch (productType.toLowerCase()) { 
        case "BoardGames": product = new BoardGames(
        
        		
        		rs.getDouble("compensationalPrice"),
        		new Reservation(rs.getInt("reservationId"), null, 0, null, false, null),
        		rs.getInt("amount"), 
        		rs.getString ("borrowableProductName"), 
        		rs.getInt("productId"), 
        		rs.getString("productType"),
        		rs.getBoolean("status")
        		); 
          
        break; 
        case "table": product = new Table ( 
        		rs.getInt("tableNr"),
        		rs.getInt("seatAmount"),
        		 rs.getInt("amount"),
        		 rs.getString("productType"), 
        		 rs.getBoolean("status"),
        		 new Reservation(rs.getInt("reservationId"), null, 0, null, false, null),
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

 // Inserts a new product into the database.
 	private List<BorrowableProduct> buildObjects(ResultSet rs, boolean fullAssociation) throws SQLException {
 		List<BorrowableProduct> res = new ArrayList<>(); 
 		while(rs.next()) {
 			res.add(buildObject(rs, fullAssociation)); 
 		}
 		return res; 
 	
 	}
}  