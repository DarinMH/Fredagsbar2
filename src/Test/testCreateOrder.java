package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGenerator.Simple;
import org.junit.jupiter.api.Test;

import DB.DBConnection;
import DB.DataAccessException;
import controller.SaleOrderCtr;
import model.Customer;
import model.Drink;
import model.Product;
import model.SaleOrder;
import model.SaleOrderLine;
import model.Staff;
import model.Supplier;




//Test class for saleOrderCtr functionalities related to order
class testCreateOrder {
	
	private SaleOrderCtr saleOrderCtr; // Initializes SaleOrderCtr

	//Method that runs before each test.
	@BeforeEach
	void setUp() throws  DataAccessException {
	
		// Initializes SaleOrderCtr
	    saleOrderCtr = new SaleOrderCtr();
	} 
	
//Test case
@Test 
void testOrderCompletionWithOneProduct() throws DataAccessException {

	
	int studentId = 1234;  //StudentId that it tests in the case
	int productId = 88;  // ProductId that it tests in the case
	

// Generates an OrderNumber with the current time in miliseconds so that the same orderNumber doesnt come up twice.
//Finds the customer and product with the controllers
	int orderNumber = (int) (System.currentTimeMillis() % 100000);
	Customer foundCustomer = saleOrderCtr.getCustomerCtr().findByStudentId(studentId); 
	Product foundProduct = saleOrderCtr.getProductCtr().findByProductId(productId); 
	
	//Creates a new SaleOrder object with its respective parameters
	SaleOrder order = saleOrderCtr.createSaleOrder(
            orderNumber, LocalDate.now(), false, foundCustomer, null, 0, 0);
	
	//Creates a SaleOrderLine with one product
	SaleOrderLine saleOrderLine = new SaleOrderLine(order, foundProduct, 1); 

	//adds an orderline to the SaleOrder
	order.addOrderline(saleOrderLine);
	
	
	//calls the controller methods to add a customer and a product to the order
	saleOrderCtr.addCustomerToOrder(foundCustomer); 
	saleOrderCtr.addProductToOrder(foundProduct, 1); 
	
	//assertions to verify that the correct customer and product were found.
	assertEquals(studentId, foundCustomer.getStudentId()); 
	assertEquals(productId, foundProduct.getProductId());
	
	
	
	
}

@Test 
void testOrderCompletionWith2Products() throws DataAccessException {
	
	int studentId = 1234; 
	int productId = 88; 
	int productId2 = 420; 
	
	int orderNumber = (int) (System.currentTimeMillis() % 100000);
	
	Customer foundCustomer = saleOrderCtr.getCustomerCtr().findByStudentId(studentId); 
	Product foundProduct = saleOrderCtr.getProductCtr().findByProductId(productId); 
	Product foundProduct2 = saleOrderCtr.getProductCtr().findByProductId(productId2); 
	
	SaleOrder order = saleOrderCtr.createSaleOrder(
            orderNumber, LocalDate.now(), false, foundCustomer, null, 0, 0);
	
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
void testNoCustomerAdded() throws DataAccessException {

	int productId = 2; 

	int orderNumber = (int) (System.currentTimeMillis() % 100000);
	

	Product foundProduct = saleOrderCtr.getProductCtr().findByProductId(productId); 
	
	SaleOrder order = saleOrderCtr.createSaleOrder(
            orderNumber, LocalDate.now(), false, null, null, 0, 0);
	
	SaleOrderLine saleOrderLine = new SaleOrderLine(order, foundProduct, 1); 

	
	order.addOrderline(saleOrderLine);

	
	saleOrderCtr.addProductToOrder(foundProduct, 1); 

	assertEquals(productId, foundProduct.getProductId());

	
}


@Test 
void testAddMultipleInstancesOfOneProduct() throws DataAccessException {

	
	int studentId = 1234; 
	int productId = 2; 
	

	int orderNumber = (int) (System.currentTimeMillis() % 100000);
	
	Customer foundCustomer = saleOrderCtr.getCustomerCtr().findByStudentId(studentId); 
	Product foundProduct = saleOrderCtr.getProductCtr().findByProductId(productId); 
	
	SaleOrder order = saleOrderCtr.createSaleOrder(
            orderNumber, LocalDate.now(), false, foundCustomer, null, 0, 0);
	
	SaleOrderLine saleOrderLine = new SaleOrderLine(order, foundProduct, 1); 

	
	order.addOrderline(saleOrderLine);
	
	saleOrderCtr.addCustomerToOrder(foundCustomer); 
	
	
	
	saleOrderCtr.addProductToOrder(foundProduct, 2); 
	

	assertEquals(studentId, foundCustomer.getStudentId()); 
	assertEquals(productId, foundProduct.getProductId());
	
	
	
	
}


@Test 
void testInvalidProduct() throws DataAccessException {

	
	int studentId = 1234; 
	int productId = 6; 

	Customer foundCustomer = saleOrderCtr.getCustomerCtr().findByStudentId(studentId); 
	Product foundProduct = saleOrderCtr.getProductCtr().findByProductId(productId); 
	
	
	assertNull(foundProduct, "Product should be null");

	
	
}



@Test 
void testInvalidCustomer() throws DataAccessException {

	int studentId = 521; 
	int productId = 6; 

	Customer foundCustomer = saleOrderCtr.getCustomerCtr().findByStudentId(studentId); 
	Product foundProduct = saleOrderCtr.getProductCtr().findByProductId(productId); 
	
	
	assertNull(foundCustomer, "Customer should be null");

	
}



}
