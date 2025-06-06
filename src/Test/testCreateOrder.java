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
	Customer foundCustomer = saleOrderCtr.findCustomerByStudentId(studentId); 
	Product foundProduct = saleOrderCtr.findProductByProductId(productId); 
	
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



@Test // Test case 2
void testOrderCompletionWith2Products() throws DataAccessException {
	
	int studentId = 1234; //StudentId that it tests in the case
	int productId = 88;  // ProductId that it tests in the case
	int productId2 = 420;  // ProductId2 that it tests in the case
	
	
	// Generates an OrderNumber with the current time in miliseconds so that the same orderNumber doesnt come up twice.
	//Finds the customer and product with the controllers
	int orderNumber = (int) (System.currentTimeMillis() % 100000);
	Customer foundCustomer = saleOrderCtr.findCustomerByStudentId(studentId); 
	Product foundProduct = saleOrderCtr.findProductByProductId(productId); 
	Product foundProduct2 = saleOrderCtr.findProductByProductId(productId2); 
	
	//Creates a new SaleOrder object with its respective parameters
	SaleOrder order = saleOrderCtr.createSaleOrder(
            orderNumber, LocalDate.now(), false, foundCustomer, null, 0, 0);
	
	//Creates two SaleOrderLines with two different products
	SaleOrderLine saleOrderLine = new SaleOrderLine(order, foundProduct, 1); 
	SaleOrderLine saleOrderLine2 = new SaleOrderLine(order, foundProduct2, 1); 

	//adds orderlines to the SaleOrder
	order.addOrderline(saleOrderLine);
	order.addOrderline(saleOrderLine2);
	
	//calls the controller methods to add a customer and adds the products to the order
	saleOrderCtr.addCustomerToOrder(foundCustomer); 
	saleOrderCtr.addProductToOrder(foundProduct2, 1); 
	saleOrderCtr.addProductToOrder(foundProduct, 1); 
	
	//assertions to verify that the correct customer and products were found.
	assertEquals(foundCustomer.getStudentId(), order.getCustomer().getStudentId()); 
	assertEquals(2, order.getOrderLines().size()); 
	assertEquals(studentId, foundCustomer.getStudentId()); 
	assertEquals(productId, foundProduct.getProductId());
	assertEquals(productId2, foundProduct2.getProductId());
	
 
}


@Test //test case 3
void testNoCustomerAdded() throws DataAccessException {

	int productId = 2; 

	int orderNumber = (int) (System.currentTimeMillis() % 100000);
	

	Product foundProduct = saleOrderCtr.findProductByProductId(productId); 
	
	
	// Customer is being set to null here: 
	SaleOrder order = saleOrderCtr.createSaleOrder(
            orderNumber, LocalDate.now(), false, null, null, 0, 0);
	
	
	SaleOrderLine saleOrderLine = new SaleOrderLine(order, foundProduct, 1); 

	
	order.addOrderline(saleOrderLine);

	// Adding the product to the product through the order line
	saleOrderCtr.addProductToOrder(foundProduct, 1); 

	
	
	assertEquals(productId, foundProduct.getProductId());

	
}


@Test //test case 4
void testAddMultipleOfOneProduct() throws DataAccessException {
	
	int studentId = 1234; 
	int productId = 2;  
	
	   // Generate a unique order number based on current time
	int orderNumber = (int) (System.currentTimeMillis() % 100000);
	
	Customer foundCustomer = saleOrderCtr.findCustomerByStudentId(studentId);
	Product foundProduct = saleOrderCtr.findProductByProductId(productId); 
	
	SaleOrder order = saleOrderCtr.createSaleOrder(
            orderNumber, LocalDate.now(), false, null, null, 0, 0);

    // Add product to order through controller

	saleOrderCtr.addCustomerToOrder(foundCustomer); 

	
	saleOrderCtr.addProductToOrder(foundProduct, 2); 
	
	double discount = order.getDiscountPercentage(); 
	
	double totalPrice = order.getTotalPrice(); 

	// Assert that the product ID matches the expected value
	assertEquals(foundCustomer.getStudentId(), order.getCustomer().getStudentId()); 
	assertEquals(1, order.getOrderLines().size()); 
	assertEquals(totalPrice, foundProduct.getSalePrice()*2*discount);

	
	
	
	
}



@Test //test case 5
void testInvalidProduct() throws DataAccessException {

	
	int studentId = 1234; 
	// invalid product id
	int productId = 6; 

	Customer foundCustomer = saleOrderCtr.findCustomerByStudentId(studentId);
	Product foundProduct = saleOrderCtr.findProductByProductId(productId); 
	
	
	// Assertion for the product to be null 
	assertNull(foundProduct, "Product should be null");

	
	
}



@Test //test case 6
void testInvalidCustomer() throws DataAccessException {

	int studentId = 521; 
	int productId = 2; 

	Customer foundCustomer = saleOrderCtr.findCustomerByStudentId(studentId);
	Product foundProduct = saleOrderCtr.findProductByProductId(productId); 
	
	// Assertio for the customer to be null: 
	assertNull(foundCustomer, "Customer should be null");

	
}



}
