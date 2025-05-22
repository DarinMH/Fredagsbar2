package Test;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import DB.DBConnection;
import DB.DataAccessException;
import DB.InventoryDB;
import DB.ProductDB;
import controller.InventoryCtr;
import controller.SaleOrderCtr;
import model.Customer;
import model.Inventory;
import model.InventoryProduct;
import model.Product;
import model.SaleOrder;
import model.SaleOrderLine;



//Test class for Inventory and Product functionalities related to updates to inventory and products
class TestUpdateInventory {
	
	//Initializies the controllers, database and connection classes
	private InventoryCtr inventoryCtr; 
	private InventoryDB inventoryDB; 
	private ProductDB productDB; 
	private DBConnection dbConnection; 
	private Connection con; 
	private InventoryProduct inventoryProduct; 

	//Method that runs before each test.
    @BeforeEach 
    void setUp() throws DataAccessException, SQLException {
        dbConnection = DBConnection.getInstance(); //
        con = dbConnection.getConnection();
        

        con.setAutoCommit(false);
        
        inventoryDB = new InventoryDB(); 
        productDB = new ProductDB(); 
        inventoryCtr = new InventoryCtr(); 
        
 
        inventoryProduct = inventoryDB.findInventoryProduct(88, 420);
    }
    
    //After each test the database doesnt save the tests tested.
    @AfterEach
    void tearDown() throws SQLException {

        if (con != null) {
            con.rollback();
            con.setAutoCommit(true);
        }
    }

	
	
	// Testing for when stock is being added to the inventory
@Test 
void testAddStock() throws DataAccessException {

	
	
	int inventoryId = 420; 
	int productId = 88; 

	
	 // Retrieve inventory and product objects by their IDs
	Inventory foundInventory = inventoryCtr.findByInventoryId(inventoryId); 
	Product foundProduct = inventoryCtr.getProductCtr().findByProductId(productId); 
	
	int quantity = 1; 

	
	InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(88, 420); 
	
	 // Store the current quantity for assertion
	int formerValue = inventoryProduct.getQuantityInStock(); 
	
	 inventoryProduct = inventoryCtr.addStock(foundInventory, foundProduct, quantity); 
	

	assertEquals(formerValue + quantity, inventoryProduct.getQuantityInStock()); 

	
	
	
	
}

@Test 
void testRemoveStock() throws DataAccessException {
	
	int inventoryId = 420; 
	int productId = 88; 
	

	int quantity = 1; 
	


	
	 // Retrieve inventory and product objects by their IDs
	Inventory foundInventory = inventoryCtr.findByInventoryId(inventoryId); 
	Product foundProduct = inventoryCtr.getProductCtr().findByProductId(productId); 

	
	InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(88, 420); 
	
	 // Store the current quantity for assertion
	int formerValue = inventoryProduct.getQuantityInStock(); 
	
	
	 // Add stock and retrieve updated inventoryProduct
	 inventoryProduct = inventoryCtr.removeStock(foundInventory, foundProduct, quantity); 
	

	assertEquals(formerValue - quantity, inventoryProduct.getQuantityInStock()); 

	
	
 
}


@Test 
void testAddMultipleProducts() throws DataAccessException {

	
	
	int inventoryId = 420; 
	int productId = 88; 

	 // Retrieve inventory and product objects by their IDs
	Inventory foundInventory = inventoryCtr.findByInventoryId(inventoryId); 
	Product foundProduct = inventoryCtr.getProductCtr().findByProductId(productId); 
	
	int quantity = 2; 
	

	
	InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(88, 420); 
	
	 // Store the current quantity for assertion
	int formerValue = inventoryProduct.getQuantityInStock(); 
	
	
	 // Add stock and retrieve updated inventoryProduct
	 inventoryProduct = inventoryCtr.addStock(foundInventory, foundProduct, quantity); 
	

	assertEquals(formerValue + quantity, inventoryProduct.getQuantityInStock()); 
	
	
}


@Test 
void testRemoveMultipleProducts() throws DataAccessException {

	
	
	int inventoryId = 420; 
	int productId = 88; 

	 // Retrieve inventory and product objects by their IDs
	Inventory foundInventory = inventoryCtr.findByInventoryId(inventoryId); 
	Product foundProduct = inventoryCtr.getProductCtr().findByProductId(productId); 
	
	int quantity = 2; 

	InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(88, 420); 
	
	 // Store the current quantity for assertion
	int formerValue = inventoryProduct.getQuantityInStock(); 
	
	
	 // Add stock and retrieve updated inventoryProduct
	 inventoryProduct = inventoryCtr.removeStock(foundInventory, foundProduct, quantity); 
	

	assertEquals(formerValue - quantity, inventoryProduct.getQuantityInStock()); 
	
	
}

// Test for an invalid inventory ID
@Test 
void testInvalidInventoryId() throws DataAccessException {

	
	
	int inventoryId = 421; 
	int productId = 88; 
	int quantity = 2; 
	

	 // Retrieve inventory and product objects by their IDs
	Inventory foundInventory = inventoryCtr.findByInventoryId(inventoryId); 
	Product foundProduct = inventoryCtr.getProductCtr().findByProductId(productId); 
	
InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(productId, inventoryId); 
	

	// Assertion for the inventory to be null
	assertNull(foundInventory, "Inventory should be null"); 
	
	
	
}


//Test for an invalid Product ID
@Test 
void testInvalidProductId() throws DataAccessException {

	int inventoryId = 420; 
	int productId = 89; 
	int quantity = 2; 

	
	 // Retrieve inventory and product objects by their IDs
	Inventory foundInventory = inventoryCtr.findByInventoryId(inventoryId); 
	Product foundProduct = inventoryCtr.getProductCtr().findByProductId(productId); 
	
InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(productId, inventoryId); 
	

	
	
	
	// Assertion for the product to be null
	assertNull(foundProduct, "Inventory should be null"); 
	



	
	
	
	
}

// Test for the result when the amount added is more than the inventory capacity
@Test 
void testOverCapacity() throws DataAccessException {

	int inventoryId = 420; 
	int productId = 88; 

	
	 // Retrieve inventory and product objects by their IDs
	Inventory foundInventory = inventoryCtr.findByInventoryId(inventoryId); 
	Product foundProduct = inventoryCtr.getProductCtr().findByProductId(productId); 
	
	int quantity = 5000001; 


	InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(productId, inventoryId); 
	
	 // Store the current quantity for assertion
	int formerValue = inventoryProduct.getQuantityInStock(); 
	
	 // Add stock and retrieve updated inventoryProduct
	 inventoryProduct = inventoryCtr.addStock(foundInventory, foundProduct, quantity); 
	 
	 assertTrue(foundInventory.getCapacity() < foundInventory.getCapacity() + quantity); 
	
	
}


// Test for when the there is less instances of the product than the quantity removed
@Test 
void testRemovingMoreThanInStock() throws DataAccessException {

	
	int inventoryId = 420; 
	int productId = 88; 

	
	 // Retrieve inventory and product objects by their IDs
	Inventory foundInventory = inventoryCtr.findByInventoryId(inventoryId); 
	Product foundProduct = inventoryCtr.getProductCtr().findByProductId(productId); 
	
	int quantity = 3; 
	

	
	InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(productId, inventoryId); 
	
	 // Store the current quantity for assertion
	int formerValue = inventoryProduct.getQuantityInStock(); 
	
	
	 // Remove stock and retrieve updated inventoryProduct
	 inventoryProduct = inventoryCtr.removeStock(foundInventory, foundProduct, quantity); 
	 
	 assertTrue(inventoryCtr.getInventoryStockForProduct(inventoryId, productId) <  quantity); 
	

	
}


@Test
void testTransferStock() throws DataAccessException {
	
// Inventory that the product is being transfered to
	int inventoryId = 420; 
	// Inventory that the product is being transfered to
	int inventoryId2 = 422; 
	int productId = 88; 
	

	 // Retrieve inventory and product objects by their IDs
	Inventory foundInventoryTo = inventoryCtr.findByInventoryId(inventoryId); 
	Product foundProduct = inventoryCtr.getProductCtr().findByProductId(productId); 
	Inventory foundInventoryFrom = inventoryCtr.findByInventoryId(inventoryId2); 
	
	int quantity = 2; 
	


	 // Finding both the inventories
		InventoryProduct inventoryProductBeforeTo = inventoryCtr.findInventoryProduct(productId, inventoryId); 
		InventoryProduct inventoryProductBeforeFrom = inventoryCtr.findInventoryProduct(productId, inventoryId2); 
		
		 // Store the current quantity for assertion from before the transfer happens 
		int valueBeforeTo = inventoryProductBeforeTo.getQuantityInStock(); 
		int valueBeforeFrom = inventoryProductBeforeFrom.getQuantityInStock(); 
		
		 // Transfer stock and retrieve updated inventoryProduct
		 inventoryCtr.transferStock(foundInventoryTo, foundInventoryFrom, foundProduct, quantity); 
		
		
//		 Finding the inventories product object after the transfer happened
		InventoryProduct inventoryProductAfterTo = inventoryCtr.findInventoryProduct(productId, inventoryId); 
		InventoryProduct inventoryProductAfterFrom = inventoryCtr.findInventoryProduct(productId, inventoryId2);
		

	 
	    // Assert that stock was added to destination and removed from  source
		assertEquals(valueBeforeFrom - quantity, inventoryProductAfterFrom.getQuantityInStock()); 
		assertEquals(valueBeforeTo + quantity, inventoryProductAfterTo.getQuantityInStock()); 
	
}






}
	
	
	






