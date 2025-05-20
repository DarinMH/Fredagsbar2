package controller;

import java.time.LocalDate;
import java.util.List;

import DB.DataAccessException;
import DB.ReservationDBIF;
import model.BorrowableProduct;
import model.Customer;
import model.Reservation;
import DB.ReservationDB;

public class ReservationCtr {
    private ReservationDBIF reservationDB; 
    private CustomerCtr customerCtr;
    private BorrowableProductCtr borrowableProductCtr; 
    private Reservation currentReservation; 

    public ReservationCtr() throws DataAccessException { 
        reservationDB = new ReservationDB();
        customerCtr = new CustomerCtr(); 
        borrowableProductCtr = new BorrowableProductCtr(); 
    }

    public boolean endReservation() throws DataAccessException { 
        this.currentReservation.setStatus(true); 
        this.currentReservation.get
        return currentReservation.isStatus(); 
    }

    public List<Reservation> findAll() throws DataAccessException { 
        return reservationDB.findAll(); 
    }

    public Reservation createReservation(int reservationId, LocalDate date, int amount, Customer customer, boolean status) throws DataAccessException {
        Reservation newReservation = new Reservation(reservationId, date, amount, customer, status);
        reservationDB.insertReservation(newReservation);
        this.currentReservation = newReservation;
        return newReservation;
    }

    public Reservation findByReservationId(int reservationId) throws DataAccessException { 
        return reservationDB.findByReservationId(reservationId);  
    }

    public void setCurrentReservation(Reservation reservation) { 
        this.currentReservation = reservation; 
    }

    public Customer addCustomerToOrder(Customer customer) throws DataAccessException { 
        customer = customerCtr.findByStudentId(customer.getStudentId()); 
        this.currentReservation.setCustomer(customer);
        currentReservation.setDate(LocalDate.now()); 
        return customer; 
    }

    public BorrowableProduct addProductToReservation(BorrowableProduct product) throws DataAccessException { 
        product = borrowableProductCtr.findByProductId(product.getProductId()); 
        this.currentReservation.setBorrowableProduct(product); 
        currentReservation.setDate(LocalDate.now());
        return product;
    }
    
}