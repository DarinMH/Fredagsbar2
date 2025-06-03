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



 // Constructor: runs when a new BorrowableProductCtr object is created
    public BorrowableProductCtr() throws DataAccessException {
    	// Sets up the connection to the borrowable product database
        this.borrowableProductDB = new BorrowableProductDB();
    }

 // This method gets a list of all borrowable products

    public List<BorrowableProduct> findAll() throws DataAccessException {
        return borrowableProductDB.findAll(); // Calls the database method to fetch all products
    }
    
    
    public void updateStatus(BorrowableProduct product) throws DataAccessException {
    	borrowableProductDB.updateStatus(product);
    }


 // This method gets one product by its ID number


    public BorrowableProduct findByProductId(int productId) throws DataAccessException {
        return borrowableProductDB.findByProductId(productId, true); // Calls the database to get the product with the given productId 
    }
}
