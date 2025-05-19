package DB;

import java.util.List;
import model.Customer;

public interface CustomerDBIF {
	List<Customer> findAll(boolean fullAssociation) throws DataAccessException;
	Customer findByStudentId(int studentId, boolean fullAssociation) throws DataAccessException;
	
}
