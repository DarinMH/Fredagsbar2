package controller;

import java.util.List;

import DB.BorrowableProductDB;
import DB.BorrowableProductDBIF;
import DB.DataAccessException;
import model.BorrowableProduct;

//This class controls the logic for borrowable products
public class BorrowableProductCtr {
<<<<<<< HEAD
   
    private BorrowableProductDBIF borrowableProductDB;  // This connects to the database using the interface
    private ReservationCtr reservationCtr; //used for reservation control
=======
    
    private BorrowableProductDBIF borrowableProductDB;
>>>>>>> 4cfca297baed991c1dcca1d0d4314366cf4b0635

    // Constructor: runs when we create an object of this class
    public BorrowableProductCtr() throws DataAccessException {
        this.borrowableProductDB = new BorrowableProductDB(); // Sets up the connection to the database
    }

<<<<<<< HEAD
 // This method gets a list of all borrowable products
=======
    // Finds all the products 
>>>>>>> 4cfca297baed991c1dcca1d0d4314366cf4b0635
    public List<BorrowableProduct> findAll() throws DataAccessException {
        return borrowableProductDB.findAll(); // Gets all products from the database
    }

<<<<<<< HEAD
 // This method gets one product by its ID number
=======
    // Finds one product based on the productId
>>>>>>> 4cfca297baed991c1dcca1d0d4314366cf4b0635
    public BorrowableProduct findByProductId(int productId) throws DataAccessException {
        return borrowableProductDB.findByProductId(productId);
    }
    
   
 
    
}
