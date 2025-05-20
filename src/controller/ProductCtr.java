
package controller;

import java.util.ArrayList;
import java.util.List;

import DB.DataAccessException;
import DB.InventoryDB;
import DB.InventoryDBIF;
import DB.ProductDB;
import DB.ProductDBIF;
import model.Drink;
import model.InventoryProduct;
import model.Miscellaneous;
import model.Product;

public class ProductCtr  {
	private ProductDBIF productDB;
	private Product product; 


	public ProductCtr() throws DataAccessException { 
		this.productDB = new ProductDB(); 

	}
	

	public List<Product> findAll() throws DataAccessException {
		return productDB.findAll(false);
	}
	

	// Calling the findByProductName method from the ProductDB to retrieve a product: 
	public Product findByProductName(String ProductName) throws DataAccessException {
		return productDB.findByProductName (ProductName);
	}
	




	public Product findByProductId(int productId) throws DataAccessException {
		  return productDB.findByProductId(productId, false);
	}
	
	public List<Drink> findByCategory(String category) throws DataAccessException {
	return productDB.findByCategory(category);
	}
	
	public List<Miscellaneous> findAllMiscellaneous() throws DataAccessException {
		return productDB.findAllMiscelaneous(); 
	}
		
	}
