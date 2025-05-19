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





class testCreateOrder {
	
	
	
	private SaleOrder accountSmallAmount;
	private SaleOrder accountBigAmount; 
	private SaleOrderCtr saleOrderCtr; 
	private Customer testCustomer; 
	private SaleOrder saleOrder; 
	private Product testProduct; 
	private DBConnection dbConnection; 
	private Connection con; 

	
	



//	@BeforeAll
//	static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterAll
//	static void tearDownAfterClass() throws Exception {
//	}


	
	
//	@Test 
//	public void testRedraw() {
//		System.out.println("testRedraw()"); 
//		boolean result = a.withdraw(3000); 
//		assertTrue(result); 
//		double expBal = 0d; 
//		assertEquals(expBal, a.getBalance(), 0d); 
//	}
	
	
//	@BeforeEach 
//	void setUp() throws Exception {
//
//		
//		
//
//		
//		
//dbConnection = DBConnection.getInstance();
//		
//	Connection con = dbConnection.getConnection(); 
//		
//		con.setAutoCommit(false);
//		
//		saleOrderCtr = new SaleOrderCtr();
//
//
//		 
//		
//		
//		
//		
//	}
	
	
	@BeforeEach
	void setUp() throws  DataAccessException {
	
	    
	    saleOrderCtr = new SaleOrderCtr();
	} 
	
@AfterEach 
void tearDown() throws  Exception {

}


@AfterAll
static void tearDownAfterClass() throws Exception {

}

	
	
	
@Test 
void test1() throws DataAccessException {

	
	int studentId = 1234; 
	int productId = 88; 
	
//	int expStudentId = 1234; 

	int orderNumber = (int) (System.currentTimeMillis() % 100000);
	Customer foundCustomer = saleOrderCtr.getCustomerCtr().findByStudentId(studentId); 
	Product foundProduct = saleOrderCtr.getProductCtr().findByProductId(productId); 
	
	SaleOrder order = saleOrderCtr.createSaleOrder(
            orderNumber, LocalDate.now(), false, foundCustomer, null, 0, 0);
	
	SaleOrderLine saleOrderLine = new SaleOrderLine(order, foundProduct, 1); 

	
	order.addOrderline(saleOrderLine);
	
	saleOrderCtr.addCustomerToOrder(foundCustomer); 
	
	
	
	saleOrderCtr.addProductToOrder(foundProduct, 1); 
	

	assertEquals(studentId, foundCustomer.getStudentId()); 
	assertEquals(productId, foundProduct.getProductId());
	
	
	
	
}

@Test 
void test2() throws DataAccessException {
	
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
void noCustomerAdded() throws DataAccessException {

	
//	int studentId = 696969; 
	int productId = 2; 
	
//	int expStudentId = 1234; 

	int orderNumber = (int) (System.currentTimeMillis() % 100000);
	
//	Customer foundCustomer = saleOrderCtr.getCustomerCtr().findByStudentId(studentId); 
	Product foundProduct = saleOrderCtr.getProductCtr().findByProductId(productId); 
	
	SaleOrder order = saleOrderCtr.createSaleOrder(
            orderNumber, LocalDate.now(), false, null, null, 0, 0);
	
	SaleOrderLine saleOrderLine = new SaleOrderLine(order, foundProduct, 1); 

	
	order.addOrderline(saleOrderLine);
	
//	saleOrderCtr.addCustomerToOrder(foundCustomer); 
	
	
	
	saleOrderCtr.addProductToOrder(foundProduct, 1); 
	

//	assertEquals(studentId, foundCustomer.getStudentId()); 
	assertEquals(productId, foundProduct.getProductId());
	
	
	
	
}


@Test 
void addMultipleInstancesOfOneProduct() throws DataAccessException {

	
	int studentId = 1234; 
	int productId = 2; 
	
//	int expStudentId = 1234; 

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
void invalidProduct() throws DataAccessException {

	
	int studentId = 1234; 
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
	
	
	assertNull(foundCustomer, "Customer should be null");
	
	



	
	
	
	
}



}
	
	
	



