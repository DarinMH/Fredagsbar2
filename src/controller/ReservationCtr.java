package controller;

import java.time.LocalDate;
import java.util.List;

import DB.DataAccessException;
import DB.ReservationDBIF;
import model.BorrowableProduct;
import model.Customer;
import model.Reservation;
import model.Table;
import DB.ReservationDB;

// this class handles the logic for reservations 
public class ReservationCtr { 
    private ReservationDBIF reservationDB; // Connects to the reservation database
    private CustomerCtr customerCtr; // Helps with customer-related actions 
    private BorrowableProductCtr borrowableProductCtr; // Helps with borrowable product actions
    private Reservation currentReservation; // Stores the current reservation being worked on

 // Constructor: runs when we create a new ReservationCtr object
    public ReservationCtr() throws DataAccessException { 
        reservationDB = new ReservationDB(); // Connects to the reservation database
        customerCtr = new CustomerCtr(); // Sets up customer controller 
        borrowableProductCtr = new BorrowableProductCtr(); // Sets up product controller 
    }

    // This method ends the current reservation
    public boolean endReservation(int reservationId) throws DataAccessException { 
//        this.currentReservation.setStatus(true); // Marks the reservation as finished (true)
        
    	Reservation reservation = findByReservationId(reservationId); 
    	
    	reservation.setStatus(false);
        
        int amount = reservation.getBorrowableProduct().getAmount() + 1; 
        
        reservation.getBorrowableProduct().setAmount(amount);
        
        reservationDB.update(reservation); 
        
        borrowableProductCtr.updateStatus(reservation.getBorrowableProduct());
//        reservation.getBorrowableProduct().setStatus(true);
//        
        return reservation.isStatus(); // Returns the current status (show now be true)
    }
 //This method gets a list of all reservation 
    public List<Reservation> findAll() throws DataAccessException { 

        return reservationDB.findAll(false);  // Gets all reservations from the database 


    }
    
    
    public boolean confirmReservation() throws DataAccessException {
		this.currentReservation.setStatus(true);
		
		reservationDB.update(currentReservation); 
		
		if(currentReservation.isStatus() == true) {
			
			return true; 
			}
			
			return false;
		
	}
 // This method creates a new reservation and saves it in the database
    public Reservation createReservation(int reservationId, LocalDate date, int amount, Customer customer, boolean status, BorrowableProduct borrowableProduct) throws DataAccessException {
        Reservation newReservation = new Reservation(reservationId, date, amount, customer, status, borrowableProduct);
        reservationDB.insertReservation(newReservation); // Saves the reservation in the database
        this.currentReservation = newReservation; // Sets it as the current reservation
        return newReservation; // Returns the new reservation
    }
//This method finds a reservation using its ID
    public Reservation findByReservationId(int reservationId) throws DataAccessException { 
        return reservationDB.findByReservationId(reservationId); // Searches the database by ID 
    }

    // This method sets the current reservation to work with
    public void setCurrentReservation(Reservation reservation) { 
        this.currentReservation = reservation; // Stores the given reservation
    }
    
    

    public Reservation getCurrentReservation() {
		return currentReservation;
	}
    
    public void updateReservation(Reservation currentReservation) throws DataAccessException {
    	reservationDB.update(currentReservation); 
    }

	// This method adds a customer to the current reservation
    public Customer addCustomerToReservation(int studentId) throws DataAccessException { 
    	//// Finds the full customer info using their student ID
       Customer customer = customerCtr.findByStudentId(studentId); 
       System.out.println("Customer found: " + customer); // Log entire object
       System.out.println("Customer ID: " + customer.getStudentId()); 
        this.currentReservation.setCustomer(customer); // Adds the customer to the reservation
//        currentReservation.setDate(LocalDate.now()); // Sets today's date on the reservation 
        reservationDB.update(currentReservation); 
        return customer; // Returns the full customer object
    }
    // Adds a product to the current reservation 
    public BorrowableProduct addProductToReservation(int productId) throws DataAccessException { 
    	// Finds the full product using the product ID
        BorrowableProduct product = borrowableProductCtr.findByProductId(productId); 
        
        if(product.getProductType().equals("tablee")) {
        	Table table = (Table) product; 
        	Customer customer = currentReservation.getCustomer(); 
        	
        	
        	if(customer != null && table.getSeatAmount() < customer.getNumberOfCustomers()) {
        		throw new IllegalArgumentException("Så mange pladser er der ikke ved bordet");   
        	}
        }
        
        this.currentReservation.setBorrowableProduct(product); // Adds the product to the reservation
//        currentReservation.setDate(LocalDate.now()); // Updates the date to today
        if(product.getAmount() > 0) {
        int amount = product.getAmount() -1; 
        if(amount == 0) {
        product.setStatus(false);
  
        }
        product.setAmount(amount);
        borrowableProductCtr.updateStatus(product);
   
        reservationDB.update(currentReservation); 
        
        } else {
        	
        }
        return product; // Returns the full product object
    }
    
    public List<BorrowableProduct> findAllProducts() throws DataAccessException {
    	return borrowableProductCtr.findAll(); 
    }
    
}