package controller;

import java.util.List;

import DB.BorrowableProductDB;
import DB.BorrowableProductDBIF;
import DB.DataAccessException;
import model.BorrowableProduct;

public class BorrowableProductCtr {
    
    private BorrowableProductDBIF borrowableProductDB;
    private ReservationCtr reservationCtr; 

    public BorrowableProductCtr() throws DataAccessException {
        this.borrowableProductDB = new BorrowableProductDB();
    }

    // Henter alle lånbare produkter
    public List<BorrowableProduct> findAll() throws DataAccessException {
        return borrowableProductDB.findAll();
    }

    // Henter ét lånbart produkt baseret på produktId
    public BorrowableProduct findByProductId(int productId) throws DataAccessException {
        return borrowableProductDB.findByProductId(productId);
    }
    
   
 
    
}
