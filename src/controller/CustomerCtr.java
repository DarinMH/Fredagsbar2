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
	
	
	// The methods calls the methods in the CustomerDB class. 

	public List<Customer> findAll() throws DataAccessException {
		return customerDB.findAll(false);
	}
	


	public Customer findByStudentId(int studentId) throws DataAccessException {
		return customerDB.findByStudentId(studentId, false);
	}
	


}
