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
		
		dbConnection = DBConnection.getInstance();
		
		con = dbConnection.getConnection(); 
		
		dbConnection.startTransaction();
		

	}
	
	
	@AfterEach
	void tearDown() throws DataAccessException {
		
		
		
		dbConnection.rollbackTransaction();
	}

	
	
	
@Test 
void test1() throws DataAccessException {

	
	
	int inventoryId = 2; 
	int productId = 43; 
	
//	int expStudentId = 1234; 
	


	
	
	Inventory foundInventory = inventoryCtr.findByInventoryId(inventoryId); 
	Product foundProduct = inventoryCtr.getProductCtr().findByProductId(productId); 
	
//	InventoryProduct inventoryProduct = inventoryCtr.findInventoryProduct(inventoryId, productId); 
	
	InventoryProduct inventoryProduct = inventoryCtr.addStock(foundInventory, foundProduct, 2); 
	
	

	assertEquals(inventoryProduct.getQuantityInStock(), 2); 
	assertEquals(inventoryCtr.getTotalStock(productId), inventoryProduct.getQuantityInStock());
	
	
	
	
}

@Test 
void test2() throws DataAccessException {
	
	int studentId = 696969; 
	int productId = 2; 
	int productId2 = 3; 
	
	Customer foundCustomer = saleOrderCtr.getCustomerCtr().findByStudentId(studentId); 
	Product foundProduct = saleOrderCtr.getProductCtr().findByProductId(productId); 
	Product foundProduct2 = saleOrderCtr.getProductCtr().findByProductId(productId2); 
	
	SaleOrder order = saleOrderCtr.createSaleOrder(
            58199, LocalDate.now(), false, foundCustomer, null, 0, 0);
	
	SaleOrderLine saleOrderLine = new SaleOrderLine(order, foundProduct, 1); 
	
	SaleOrderLine saleOrderLine2 = new SaleOrderLine(order, foundProduct2, 1); 

	
	order.addOrderline(saleOrderLine);
	
	order.addOrderline(saleOrderLine2);
	
	saleOrderCtr.addCustomerToOrder(foundCustomer); 
	
	
	saleOrderCtr.addProductToOrder(foundProduct2, 1); 
	
	saleOrderCtr.addProductToOrder(foundProduct, 1); 
	

	assertEquals(studentId, foundCustomer.getStudentId()); 
	assertEquals(productId, foundProduct.getProductId());
	assertEquals(productId2, foundProduct2.getProductId());
	
 
}


@Test 
void noCustomerAdded() throws DataAccessException {

	
//	int studentId = 696969; 
	int productId = 2; 
	
//	int expStudentId = 1234; 

	
//	Customer foundCustomer = saleOrderCtr.getCustomerCtr().findByStudentId(studentId); 
	Product foundProduct = saleOrderCtr.getProductCtr().findByProductId(productId); 
	
	SaleOrder order = saleOrderCtr.createSaleOrder(
            74919, LocalDate.now(), false, null, null, 0, 0);
	
	SaleOrderLine saleOrderLine = new SaleOrderLine(order, foundProduct, 1); 

	
	order.addOrderline(saleOrderLine);
	
//	saleOrderCtr.addCustomerToOrder(foundCustomer); 
	
	
	
	saleOrderCtr.addProductToOrder(foundProduct, 1); 
	

//	assertEquals(studentId, foundCustomer.getStudentId()); 
	assertEquals(productId, foundProduct.getProductId());
	
	
	
	
}


@Test 
void addMultipleInstancesOfOneProduct() throws DataAccessException {

	
	int studentId = 696969; 
	int productId = 2; 
	
//	int expStudentId = 1234; 

	
	Customer foundCustomer = saleOrderCtr.getCustomerCtr().findByStudentId(studentId); 
	Product foundProduct = saleOrderCtr.getProductCtr().findByProductId(productId); 
	
	SaleOrder order = saleOrderCtr.createSaleOrder(
            59190, LocalDate.now(), false, foundCustomer, null, 0, 0);
	
	SaleOrderLine saleOrderLine = new SaleOrderLine(order, foundProduct, 1); 

	
	order.addOrderline(saleOrderLine);
	
	saleOrderCtr.addCustomerToOrder(foundCustomer); 
	
	
	
	saleOrderCtr.addProductToOrder(foundProduct, 2); 
	

	assertEquals(studentId, foundCustomer.getStudentId()); 
	assertEquals(productId, foundProduct.getProductId());
	
	
	
	
}


@Test 
void invalidProduct() throws DataAccessException {

	
	int studentId = 696969; 
	int productId = 6; 
	
//	int expStudentId = 1234; 

	
	Customer foundCustomer = saleOrderCtr.getCustomerCtr().findByStudentId(studentId); 
	Product foundProduct = saleOrderCtr.getProductCtr().findByProductId(productId); 
	
	
	assertNull(foundProduct, "Product should be null");
	
	



	
	
	
	
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
	
	
	






