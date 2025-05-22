package controller;

import java.time.LocalDate;
import java.util.List;

import DB.DataAccessException;
import DB.ReservationDBIF;
import model.BorrowableProduct;
import model.Customer;
import model.Reservation;
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
    public boolean endReservation() throws DataAccessException { 
        this.currentReservation.setStatus(true); // Marks the reservation as finished (true)
        return currentReservation.isStatus(); // Returns the current status (show now be true)
    }
 //This method gets a list of all reservation 
    public List<Reservation> findAll() throws DataAccessException { 

        return reservationDB.findAll(false);  // Gets all reservations from the database 


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

    // This method adds a customer to the current reservation
    public Customer addCustomerToOrder(Customer customer) throws DataAccessException { 
    	//// Finds the full customer info using their student ID
        customer = customerCtr.findByStudentId(customer.getStudentId()); 
        this.currentReservation.setCustomer(customer); // Adds the customer to the reservation
        currentReservation.setDate(LocalDate.now()); // Sets today's date on the reservation 
        return customer; // Returns the full customer object
    }
    // Adds a product to the current reservation 
    public BorrowableProduct addProductToReservation(BorrowableProduct product) throws DataAccessException { 
    	// Finds the full product using the product ID
        product = borrowableProductCtr.findByProductId(product.getProductId()); 
        this.currentReservation.setBorrowableProduct(product); // Adds the product to the reservation
        currentReservation.setDate(LocalDate.now()); // Updates the date to today
        return product; // Returns the full product object
    }
    
}