
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

//This class controls logic for working with products
public class ProductCtr  {
	private ProductDBIF productDB; // This connects to the product database using an interface
	private Product product; // A single product object (not used in this code, maybe for future use)

	// Constructor: runs when a ProductCtr object is created
	public ProductCtr() throws DataAccessException { 
		this.productDB = new ProductDB(); // Sets up the connection to the product database

	}
	
    //gets all products from the database
	public List<Product> findAll() throws DataAccessException {
		return productDB.findAll(false); // 'false' might mean we don’t include deleted or hidden items
	}
	

	// Calling the findByProductName method from the ProductDB to retrieve a product: 
	public Product findByProductName(String ProductName) throws DataAccessException {
		return productDB.findByProductName (ProductName); // Searches the database for the product name
	}
	

	// This method finds a product by its ID number
	public Product findByProductId(int productId) throws DataAccessException {
		  return productDB.findByProductId(productId, false); // Finds product by ID, 'false' may skip deleted ones
	}
	
	// This method finds drinks by their category (like soda, beer etc)
	public List<Drink> findByCategory(String category) throws DataAccessException {
	return productDB.findByCategory(category); // Returns a list of drinks from that category
	}
	
	// This method gets all miscellaneous products (things that are not drinks)
	public List<Miscellaneous> findAllMiscellaneous() throws DataAccessException {
		return productDB.findAllMiscelaneous(); // Gets all other types of products from the database
	}
		
	}
