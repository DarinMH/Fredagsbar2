package GUI;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import DB.CustomerDB;
import DB.DBConnection;
import DB.DataAccessException;
import DB.InventoryDB;
import controller.InventoryCtr;
import controller.ProductCtr;
import controller.SaleOrderCtr;
import model.Customer;
import model.Inventory;
import model.InventoryProduct;
import model.Product;
import model.SaleOrder;

public class TryMe {
	
	
	
	
	public void generateData() throws DataAccessException {
//	Customer customer = new Customer(6969, "john", "doe", "email", 1); 
	
//	
//
//		CustomerDB customerDB = new CustomerDB();
//		
//		customerDB.findByStudentId(0, false); 
//		
//		System.out.println(customerDB.findByStudentId(696969, false)); 
	
	
 
	


	}
	
	
//	public static void main(String []args) throws DataAccessException {
//		
//	    ProductCtr productCtr = new ProductCtr(); 
//	    Product p = productCtr.findByProductId(22); 
//
//	
//	    System.out.println("Total Qty: " + p.getTotalQuantityInStock());
//		
//		
//		
//		
//		
//	}
	
	
	public static void main(String[] args) throws Exception {
	
//		SaleOrderCtr orderCtr = new SaleOrderCtr(); 
//		SaleOrder order = orderCtr.findByOrderNumber(114699);
//		System.out.println(order.getCustomer().getFirstName()); // ❌ **NullPointerException** (customer not loaded)
		
		
		
		ProductCtr productCtr = new ProductCtr(); 
		Product p = productCtr.findByProductId(22); 
	
		
		InventoryCtr i = new InventoryCtr(); 
		
		
		Inventory inventory = i.findByInventoryId(1); 
		
		
		i.addStock(inventory, p, 2); 
		
		
		System.out.println(i.getTotalStock(22)); 



	
	
}
	
}
