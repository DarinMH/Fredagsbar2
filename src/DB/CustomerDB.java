
package DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DB.DataAccessException;
import model.Customer;
import model.Reservation;
import DB.DBConnection;

public class CustomerDB implements CustomerDBIF {

	private static final String FIND_ALL_Q =
			"select * from customer";
	private static final String FIND_BY_STUDENT_ID_Q =
			FIND_ALL_Q + " where StudentId = ?";
	private PreparedStatement findAll; 
	private PreparedStatement findByStudentId;
	
	public CustomerDB() throws DataAccessException {
		try {
			findAll = DBConnection.getInstance().getConnection().prepareStatement(FIND_ALL_Q);
			findByStudentId = DB.DBConnection.getInstance().getConnection().prepareStatement(FIND_BY_STUDENT_ID_Q);
		}catch(SQLException e) {
			throw new DataAccessException(e, "Could not prepare statement");
			
		}
	}
	@Override
	public List<Customer>findAll(boolean fullAssociation) throws DataAccessException{
		try {
			ResultSet rs = findAll.executeQuery();
			List<Customer> res = buildObjects(rs, false);
			return res;
		}catch (SQLException e) {
			DataAccessException he = new DataAccessException(e, "Could not find all");
			throw he;
		}
	}
	@Override
	public Customer findByStudentId(int studentId, boolean fullAssociation) throws DataAccessException {
		try {
			findByStudentId.setInt(1, studentId);
			ResultSet rs = findByStudentId.executeQuery();
			Customer customer = null;
			if(rs.next()) {
				customer = buildObject(rs, fullAssociation);
			}
			return customer;
		}catch (SQLException e) {
			throw new DataAccessException(e, "could not find customer by CustomerId="+ studentId);
		}
	}
	private Customer buildObject(ResultSet rs, boolean fullAssociation) throws DataAccessException {
		
		Customer customer = new Customer();
	
		try {
	        customer.setStudentId(rs.getInt("StudentId")); 
	        customer.setFirstName(rs.getString("FirstName"));
	        customer.setLastName(rs.getString("LastName"));
	        customer.setEmail(rs.getString("StudentEmail"));
	        customer.setNumberOfCustomers(rs.getInt("numberOfCustomers"));
	        
		} catch (SQLException e) {
    		
    		throw new DataAccessException (e, "could not build.."); 
		}
	        return customer;
	    }
	    
	        private List<Customer> buildObjects(ResultSet rs, boolean fullAssociation) throws DataAccessException {
	    		List<Customer> res = new ArrayList<>(); 
	    		try {
	    		while(rs.next()) {
	    			res.add(buildObject(rs, false)); 
	    		}
	        } catch (SQLException e) {
	    		// TODO Auto-generated catch block
	    		throw new DataAccessException(e, "could not build"); }
	    		return res; 
	        }
	  
	}
	
