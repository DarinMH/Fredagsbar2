package controller;

import java.util.List;

import DB.BorrowableProductDB;
import DB.BorrowableProductDBIF;
import DB.DataAccessException;
import model.BorrowableProduct;

//This class controls the logic for borrowable products
public class BorrowableProductCtr {
   
    private BorrowableProductDBIF borrowableProductDB;  // This connects to the database using the interface
    private ReservationCtr reservationCtr; //used for reservation control

    // Constructor: runs when we create an object of this class
    public BorrowableProductCtr() throws DataAccessException {
        this.borrowableProductDB = new BorrowableProductDB(); // Sets up the connection to the database
    }

 // This method gets a list of all borrowable products
    public List<BorrowableProduct> findAll() throws DataAccessException {
        return borrowableProductDB.findAll(); // Gets all products from the database
    }

 // This method gets one product by its ID number
    public BorrowableProduct findByProductId(int productId) throws DataAccessException {
        return borrowableProductDB.findByProductId(productId);
    }
    
   
 
    
}
