package controller;

import java.util.List;

import DB.BorrowableProductDB;
import DB.BorrowableProductDBIF;
import DB.DataAccessException;
import model.BorrowableProduct;

//This class controls the logic for borrowable products
public class BorrowableProductCtr {
    
	// This connects to the borrowable product database interface
    private BorrowableProductDBIF borrowableProductDB;

 // Constructor: runs when a new BorrowableProductCtr object is created
    public BorrowableProductCtr() throws DataAccessException {
    	// Sets up the connection to the borrowable product database
        this.borrowableProductDB = new BorrowableProductDB();
    }

 // This method gets a list of all borrowable products
    public List<BorrowableProduct> findAll() throws DataAccessException {
        return borrowableProductDB.findAll(); // Calls the database method to fetch all products
    }

    //find one borrowable product using productId 
    public BorrowableProduct findByProductId(int productId) throws DataAccessException {
        return borrowableProductDB.findByProductId(productId); // Calls the database to get the product with the given productId 
    }
}