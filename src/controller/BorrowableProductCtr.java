package controller;

import java.util.List;

import DB.BorrowableProductDB;
import DB.BorrowableProductDBIF;
import DB.DataAccessException;
import model.BorrowableProduct;

//This class controls the logic for borrowable products
public class BorrowableProductCtr {
<<<<<<< Updated upstream
    
	// This connects to the borrowable product database interface
    private BorrowableProductDBIF borrowableProductDB;
=======
<<<<<<< HEAD

   
    private BorrowableProductDBIF borrowableProductDB;  // This connects to the database using the interface
    private ReservationCtr reservationCtr; //used for reservation control

    
=======
    
	// This connects to the borrowable product database interface
    private BorrowableProductDBIF borrowableProductDB;
>>>>>>> 8dc7f082531cdc1fdb2aa40f4ae21623c48f810c
>>>>>>> Stashed changes

 // Constructor: runs when a new BorrowableProductCtr object is created
    public BorrowableProductCtr() throws DataAccessException {
    	// Sets up the connection to the borrowable product database
        this.borrowableProductDB = new BorrowableProductDB();
    }

 // This method gets a list of all borrowable products
<<<<<<< Updated upstream
=======
<<<<<<< HEAD

    // Finds all the products 

=======
>>>>>>> 8dc7f082531cdc1fdb2aa40f4ae21623c48f810c
>>>>>>> Stashed changes
    public List<BorrowableProduct> findAll() throws DataAccessException {
        return borrowableProductDB.findAll(); // Calls the database method to fetch all products
    }

<<<<<<< Updated upstream
    //find one borrowable product using productId 
=======
<<<<<<< HEAD
 // This method gets one product by its ID number

    // Finds one product based on the productId

=======
    //find one borrowable product using productId 
>>>>>>> 8dc7f082531cdc1fdb2aa40f4ae21623c48f810c
>>>>>>> Stashed changes
    public BorrowableProduct findByProductId(int productId) throws DataAccessException {
        return borrowableProductDB.findByProductId(productId); // Calls the database to get the product with the given productId 
    }
}
