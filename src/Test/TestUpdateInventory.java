package Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Connection;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
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

class TestUpdateInventory {
	
	
	private InventoryCtr inventoryCtr; 
	private InventoryDB inventoryDB; 
	private ProductDB productDB; 
	private DBConnection dbConnection; 
	private Connection con; 
	

	
	@BeforeEach 
	void setUp() throws DataAccessException {
		
		inventoryDB = new InventoryDB(); 
		productDB = new ProductDB(); 
		
		inventoryCtr = new InventoryCtr(); 
		
		
		

		

	}
	
	
	@AfterEach
	void tearDown() throws DataAccessException {
		
		

	}

	
	
	
@Test 
void addStock() throws DataAccessException {

	
	
	int inventoryId = 420; 
	int productId = 88; 
	
//	int expStudentId = 1234; 
	
	
	
	


	
	
	Inventory foundInventory = inventoryCtr.findByInventoryId(inventoryId); 
	Product foundProduct = inventoryCtr.getProductCtr().findByProductId(productId); 
	
	int quantity = 1; 
	
//	InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(inventoryId, productId); 
	
	
	
	
	InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(88, 420); 
	
	
	int formerValue = inventoryProduct.getQuantityInStock(); 
	
	 inventoryProduct = inventoryCtr.addStock(foundInventory, foundProduct, quantity); 
	

	assertEquals(formerValue + quantity, inventoryProduct.getQuantityInStock()); 

	
	
	
	
}

@Test 
void removeStock() throws DataAccessException {
	
	int inventoryId = 420; 
	int productId = 88; 
	
//	int expStudentId = 1234; 
	
	
	int quantity = 1; 
	


	
	
	Inventory foundInventory = inventoryCtr.findByInventoryId(inventoryId); 
	Product foundProduct = inventoryCtr.getProductCtr().findByProductId(productId); 
	
//	InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(inventoryId, productId); 
	
	
	
	
	InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(88, 420); 
	
	
	int formerValue = inventoryProduct.getQuantityInStock(); 
	
	 inventoryProduct = inventoryCtr.removeStock(foundInventory, foundProduct, quantity); 
	

	assertEquals(formerValue - quantity, inventoryProduct.getQuantityInStock()); 

	
	
 
}


@Test 
void addMultipleProducts() throws DataAccessException {

	
	
	int inventoryId = 420; 
	int productId = 88; 
	
//	int expStudentId = 1234; 
	
	
	
	


	
	
	Inventory foundInventory = inventoryCtr.findByInventoryId(inventoryId); 
	Product foundProduct = inventoryCtr.getProductCtr().findByProductId(productId); 
	
	int quantity = 2; 
	
//	InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(inventoryId, productId); 
	
	
	
	
	InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(88, 420); 
	
	
	int formerValue = inventoryProduct.getQuantityInStock(); 
	
	 inventoryProduct = inventoryCtr.addStock(foundInventory, foundProduct, quantity); 
	

	assertEquals(formerValue + quantity, inventoryProduct.getQuantityInStock()); 
	
	
}


@Test 
void removeMultipleProducts() throws DataAccessException {

	
	
	int inventoryId = 420; 
	int productId = 88; 
	
//	int expStudentId = 1234; 
	
	
	
	


	
	
	Inventory foundInventory = inventoryCtr.findByInventoryId(inventoryId); 
	Product foundProduct = inventoryCtr.getProductCtr().findByProductId(productId); 
	
	int quantity = 2; 
	
//	InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(inventoryId, productId); 
	
	
	
	
	InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(88, 420); 
	
	
	int formerValue = inventoryProduct.getQuantityInStock(); 
	
	 inventoryProduct = inventoryCtr.removeStock(foundInventory, foundProduct, quantity); 
	

	assertEquals(formerValue - quantity, inventoryProduct.getQuantityInStock()); 
	
	
}


@Test 
void invalidInventoryId() throws DataAccessException {

	
	
	int inventoryId = 421; 
	int productId = 88; 
	
//	int expStudentId = 1234; 
	
	
	int quantity = 2; 
	


	
	
	Inventory foundInventory = inventoryCtr.findByInventoryId(inventoryId); 
	Product foundProduct = inventoryCtr.getProductCtr().findByProductId(productId); 
	
InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(88, 420); 
	
	
	int formerValue = inventoryProduct.getQuantityInStock(); 
	
	 inventoryProduct = inventoryCtr.removeStock(foundInventory, foundProduct, quantity); 
	
	
	
	assert
	assertNull(foundInventory, "Inventory should be null"); 
	
	
	
}



@Test 
void invalidCustomer() throws DataAccessException {

	
	int studentId = 521; 
	int productId = 6; 
	
//	int expStudentId = 1234; 

	
	Customer foundCustomer = saleOrderCtr.getCustomerCtr().findByStudentId(studentId); 
	Product foundProduct = saleOrderCtr.getProductCtr().findByProductId(productId); 
	
	
	assertNull(foundCustomer, "Product should be null");
	
	



	
	
	
	
}



}
	
	
	






