package controller;

import java.util.List;

import DB.CustomerDB;
import DB.CustomerDBIF;
import DB.DataAccessException;
import model.Customer;



public class CustomerCtr {
	private CustomerDBIF customerDB;

	public CustomerCtr() throws DataAccessException {
		this.customerDB = new CustomerDB();
	}
	/* (non-Javadoc)
	 * @see controller.PersonCtrIF#findAll()
	 */

	public List<Customer> findAll() throws DataAccessException {
		return customerDB.findAll(false);
	}
	
	/* (non-Javadoc)
	 * @see controller.PersonCtrIF#findById(int)
	 */

	public Customer findByStudentId(int studentId) throws DataAccessException {
		return customerDB.findByStudentId(studentId, false);
	}
	
	/* (non-Javadoc)
	 * @see controller.PersonCtrIF#updatePerson(int, java.lang.String, java.lang.String, java.lang.String, java.time.LocalDate, int)
	 */

}
