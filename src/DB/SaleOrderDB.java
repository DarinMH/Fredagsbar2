
package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import DB.DataAccessException;
import model.Customer;
import model.SaleOrder;
import model.SaleOrderLine;
import model.Staff;
import model.Buyer;

	public class SaleOrderDB implements SaleOrderDBIF {
		
		private CustomerDBIF customerDB;
		// SQL statements for performing common database operations on the table.
	private static final String FIND_ALL_Q = "select * from SaleOrder"; 
	private static final String FIND_BY_ORDER_NUMBER_Q = FIND_ALL_Q + " where OrderNumber = ?"; 
	private static final String INSERT_SALE_ORDER_Q = "INSERT INTO SaleOrder(OrderNumber, "
	+ "Status, Date, StudentId, Username, totalPrice, discountPercentage) values(?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_SALE_ORDER_LINE_Q = "INSERT INTO SaleOrderLine(OrderNumber, ProductID, Quantity) VALUES(?, ?, ?)";
	private static final String UPDATE_Q = "update SaleOrder set Status = ?, Date = ?, StudentId = ?, "
			+ "Username = ?, totalPrice = ?, discountPercentage = ? where OrderNumber = ?" ; 
	private static final String UPDATE_ORDER_LINE_Q = "update SaleOrderLine set Quantity = ? where OrderNumber = ? and ProductID = ?"; 
	 // PreparedStatements to safely execute the SQL queries with parameters in the Java code.
	private PreparedStatement findAll; 
	private PreparedStatement findByOrderNumber; 
	private PreparedStatement insertSaleOrder; 
	private PreparedStatement insertSaleOrderLine;
	private PreparedStatement update; 
	private PreparedStatement updateOrderLine; 
	private DBConnection dbConnection; 
	
	
	public SaleOrderDB(CustomerDBIF customerDB) throws DataAccessException {
		this.customerDB = customerDB;
		init();
		
	}

	public SaleOrderDB() throws DataAccessException {
		customerDB = new CustomerDB();
		init(); 
	}
	
    // Prepares the connection and prepares the SQL statements to be executed. 
	private void init() throws DataAccessException {
			try {
				
			dbConnection = DBConnection.getInstance(); 
			
			
			findAll = dbConnection.getConnection().
	prepareStatement(FIND_ALL_Q);  
	findByOrderNumber = dbConnection.getConnection().
	prepareStatement(FIND_BY_ORDER_NUMBER_Q);  
	insertSaleOrder = dbConnection.getConnection().
	prepareStatement(INSERT_SALE_ORDER_Q);
	insertSaleOrderLine = dbConnection.getConnection().
			prepareStatement(INSERT_SALE_ORDER_LINE_Q);
	update = dbConnection.getConnection().prepareStatement(UPDATE_Q); 
	updateOrderLine = dbConnection.getConnection().prepareStatement(UPDATE_ORDER_LINE_Q); 
	}catch(SQLException e) {
	throw new DataAccessException(e, "Could not prepare statement"); 
	}
}

	// Builds an object from the current row of the ResultSet.
	// If fullAssociation is true, it fetches and sets the full object.
	private SaleOrder buildObject(ResultSet rs, boolean fullAssociation) throws DataAccessException {
		SaleOrder saleOrder = new SaleOrder();
		try {
		saleOrder.setOrderNumber(rs.getInt("orderNumber"));
		saleOrder.setDate(rs.getDate("date").toLocalDate());
		saleOrder.setStatus(rs.getBoolean("status"));
		saleOrder.setCustomer(new Customer(rs.getInt("StudentId")));
		saleOrder.setStaff(new Staff(rs.getString("username"), null, null, null, null));
		saleOrder.setTotalPrice(rs.getDouble("totalPrice"));
		saleOrder.setDiscountPercentage(rs.getDouble("discountPercentage"));
		if(fullAssociation) {
			Customer customer = this.customerDB.findByStudentId(saleOrder.getCustomer().getStudentId(), false);
	
			saleOrder.setCustomer(customer);
		}
		} catch (SQLException e) {
			
			throw new DataAccessException(e, "could not build"); 
		} 

  
		return saleOrder; 
	
	}
	
	
	// Converts all rows in the ResultSet into a list of objects.
	private List<SaleOrder> buildObjects(ResultSet rs, boolean fullAssociation) throws DataAccessException {
	List<SaleOrder> res = new ArrayList<>();
	try {
	while(rs.next()) {
	res.add(buildObject(rs, false)); 
	}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		throw new DataAccessException(e, "could not build"); 
	} 
	return res; 
	}
	
	@Override
	public List<SaleOrder> findAll(boolean fullAssociation) throws DataAccessException {
		try {
			ResultSet rs = findAll.executeQuery(); 
			List<SaleOrder> res = buildObjects(rs, false); 
			System.out.println("Number of objects found: " + res.size()); 
			return res; 
		} catch (SQLException e) {
			DataAccessException d = new DataAccessException(e, "could not find all orders"); 
			throw d; 
		}

	}
	
	@Override
	public SaleOrder findByOrderNumber(int orderNumber, boolean fullAssociation) throws DataAccessException {
	// TODO Auto-generated method stub
		try {
			findByOrderNumber.setInt(1, orderNumber); 
			ResultSet rs = findByOrderNumber.executeQuery(); 
			SaleOrder saleOrder = null; 
			if(rs.next()) {
				saleOrder = buildObject(rs, false); 
			}
			return saleOrder; 
		}catch(SQLException e) {
			System.out.println("SQL Exception: " + e.getMessage());
			e.printStackTrace(); 
			throw new DataAccessException(e, "Could not find by Order Number"); 
		}
	}

	
	
//	Method to insert the SaleOrder object into the database so it can be persisted.  
	@Override
	public void insertSaleOrder(SaleOrder saleOrder, boolean fullAssociation) throws DataAccessException {
			
			try {
			
		
				dbConnection.startTransaction();
		        
		        insertSaleOrder.setInt(1, saleOrder.getOrderNumber());
		        if(saleOrder.isStatus() != false) {
		        	insertSaleOrder.setBoolean(2, saleOrder.isStatus()); 
		        } else {
		        insertSaleOrder.setBoolean(2, saleOrder.isStatus()); 
		        }
		        insertSaleOrder.setDate(3, java.sql.Date.valueOf(saleOrder.getDate())); 
		 
					if(saleOrder.getCustomer() != null ) {
						   insertSaleOrder.setInt(4, saleOrder.getCustomer().getStudentId()); 
					} else {
						insertSaleOrder.setNull(4, java.sql.Types.INTEGER);
					}
					if(saleOrder.getStaff() != null ) {
					     insertSaleOrder.setString(5, saleOrder.getStaff().getUsername());
					} else {
						insertSaleOrder.setNull(5, java.sql.Types.VARCHAR);
					}
					if(saleOrder.getTotalPrice() != 0) {
		   
		        insertSaleOrder.setDouble(6, saleOrder.getTotalPrice());
		        
					} else {
						insertSaleOrder.setNull(6, java.sql.Types.DOUBLE);
					}
					
					if(saleOrder.getDiscountPercentage() != 0) {
						insertSaleOrder.setDouble(7, saleOrder.getDiscountPercentage());
					} else {
						insertSaleOrder.setNull(7, java.sql.Types.DOUBLE);
					}
				
	
		        	insertSaleOrder.executeUpdate(); 
    
		        dbConnection.commitTransaction();
				
			}catch(Exception e){
					dbConnection.rollbackTransaction();

					
					throw new DataAccessException(e, "save order failed");
					
				}
				
		        
		       

	}

	
//	Method to insert the order line object into the database so it can be persisted. 
	@Override
	public void insertSaleOrderLine(SaleOrderLine saleOrderLine, boolean fullAssociation) throws DataAccessException {
		try {
			
			dbConnection.startTransaction();
			
	
			
			insertSaleOrderLine.setInt(1, saleOrderLine.getSaleOrder().getOrderNumber());
			insertSaleOrderLine.setInt(2, saleOrderLine.getProduct().getProductId());
			insertSaleOrderLine.setInt(3, saleOrderLine.getQuantity());
	        insertSaleOrderLine.executeUpdate();
			
	        dbConnection.commitTransaction();
				System.out.println("dbconnection.commitTransaction();");
				System.out.println("try: connection.commit();");
				System.out.println("finally: connection.setAutoCommit(true);");
				
			}catch(Exception e){
					dbConnection.rollbackTransaction();
					
					System.out.println("dbconnection.rollbackTransaction();");
					System.out.println("try: connection.rollback();");
					System.out.println("finally: connection.setAutoCommit(true);");
					
					throw new DataAccessException(e, "save order failed");
					
				}
	
	}

// method to update the sale order in the database. 
	@Override
	public void update(SaleOrder saleOrder, boolean fullAssociation) throws DataAccessException {
		final int orderNumber = saleOrder.getOrderNumber(); 
		final boolean status = saleOrder.isStatus(); 
		final LocalDate date = saleOrder.getDate();  
		final Integer studentId = (saleOrder.getCustomer() != null) ? saleOrder.getCustomer().getStudentId() : null; 
		String username = null; 
		final double totalPrice = saleOrder.getTotalPrice(); 
		final double discountPercentage = saleOrder.getDiscountPercentage(); 
		
		if(saleOrder.getStaff() != null) {
			username = saleOrder.getStaff().getUsername(); 
		}
		try {
			update.setBoolean(1, status);
			update.setDate(2, java.sql.Date.valueOf(date));
			
			if(studentId != null) {
				update.setInt(3, studentId);
			} else {
				update.setNull(3, Types.INTEGER);
			}
			
			if(saleOrder.getStaff() != null ) {
			     update.setString(4, username);
			} else {
				update.setNull(4, java.sql.Types.VARCHAR);
			}
				
				if(saleOrder.getTotalPrice() != 0) {
					update.setDouble(5, totalPrice); 
				} else {
					update.setNull(5, java.sql.Types.FLOAT); 
				}
				
				update.setDouble(6, discountPercentage); 				
				update.setInt(7, orderNumber);
			
			
			update.executeUpdate(); 
			
		} catch(SQLException e) {
			throw new DataAccessException(e, "Could not update " + orderNumber); 
		 
		}
		
	
		
	}

	// Method to update order line objects in the database
	@Override
	public void updateOrderLine(SaleOrderLine saleOrderLine, boolean fullAssociation) throws DataAccessException {
		// TODO Auto-generated method stub
		
		final int orderNumber = saleOrderLine.getSaleOrder().getOrderNumber(); 
		final int productId = saleOrderLine.getProduct().getProductId(); 
		final int quantity = saleOrderLine.getQuantity(); 
		try {
	
		updateOrderLine.setInt(1, quantity); 
		updateOrderLine.setInt(2, orderNumber); 
		updateOrderLine.setInt(3, productId);
		updateOrderLine.executeUpdate(); 
		
		
		
	} catch(SQLException e) {
		throw new DataAccessException(e, "Could not update orderLines " + orderNumber); 
	}
	}

	}
	
	
