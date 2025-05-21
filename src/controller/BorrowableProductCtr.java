package controller;

import java.util.List;

import DB.BorrowableProductDB;
import DB.BorrowableProductDBIF;
import DB.DataAccessException;
import model.BorrowableProduct;

public class BorrowableProductCtr {
    
    private BorrowableProductDBIF borrowableProductDB;

    public BorrowableProductCtr() throws DataAccessException {
        this.borrowableProductDB = new BorrowableProductDB();
    }

    // Finds all the products 
    public List<BorrowableProduct> findAll() throws DataAccessException {
        return borrowableProductDB.findAll();
    }

    // Finds one product based on the productId
    public BorrowableProduct findByProductId(int productId) throws DataAccessException {
        return borrowableProductDB.findByProductId(productId);
    }
    
   
 
    
}
