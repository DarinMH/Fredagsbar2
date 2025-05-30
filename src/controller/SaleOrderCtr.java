
package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import DB.DataAccessException;
import DB.SaleOrderDB;
import DB.SaleOrderDBIF;
import model.Buyer;
import model.Customer;
import model.Drink;
import model.Product;
import model.SaleOrder;
import model.SaleOrderLine;
import model.Staff;

//This class controls everything related to sales orders
public class SaleOrderCtr {
private SaleOrderDBIF saleOrderDB; 
private CustomerCtr customerCtr; 
private ProductCtr productCtr; 
private SaleOrder currentSaleOrder; 


//Constructor: runs when a new SaleOrderCtr object is created
public SaleOrderCtr() throws DataAccessException { 
	
	customerCtr = new CustomerCtr(); 
	productCtr = new ProductCtr(); 
	this.saleOrderDB = new SaleOrderDB();  
}

//Confirms the current order and updates it in the database
public boolean confirmOrder() throws DataAccessException {

	this.currentSaleOrder.setStatus(true);
	saleOrderDB.update(currentSaleOrder, false);
	
	if(currentSaleOrder.isStatus() == true) {
		
	return true; 
	}
	
	return false; 
}

//Gets all sale orders from the database
public List<SaleOrder> findAll() throws DataAccessException {
return saleOrderDB.findAll(false); 

}

//Creates a new sale order with all needed details
public SaleOrder createSaleOrder(int orderNumber, LocalDate date,  boolean status, 
		Customer customer, Staff staff, double totalPrice, double discountPercentage) throws DataAccessException { 
	 // Creates a new SaleOrder object
	 SaleOrder newSaleOrder = new SaleOrder(orderNumber, date, status,  customer, staff, totalPrice, discountPercentage); 

	 
	 saleOrderDB.insertSaleOrder(newSaleOrder, false); // Saves the new order to the database
	 this.currentSaleOrder = newSaleOrder; // Sets it as the current working order
	 
	 
	return newSaleOrder; // Returns the new order
}

//Finds a sale order by its number
public SaleOrder findByOrderNumber(int orderNumber) throws DataAccessException {
	
	return saleOrderDB.findByOrderNumber(orderNumber, true);
}

// Sets the current sale order we are working on
public void setCurrentOrder(SaleOrder order) {
	this.currentSaleOrder=order; //Stores the given order
}

public Customer findCustomerByStudentId(int studentId) throws DataAccessException {
return customerCtr.findByStudentId(studentId); 	

}

public Product findProductByProductId(int productId) throws DataAccessException {
	return productCtr.findByProductId(productId); 
	
}


//Adds a product and quantity to the current sale order
public void addProductToOrder(Product product, int quantity) throws DataAccessException {
    List<SaleOrderLine> lines = currentSaleOrder.getOrderLines(); //Get current order line 
    SaleOrderLine existingLine = null; // used to check if product already exists in the order

 // Looks through each line to see if the product is already in the order
    for (SaleOrderLine line : lines) {
        if (line.getProduct().getProductId() == product.getProductId()) {
            existingLine = line;
            break;
        }
    }

 // If the product is already in the order, just update the quantity
    if (existingLine != null) {
        existingLine.setQuantity(existingLine.getQuantity() + quantity);
        saleOrderDB.updateOrderLine(existingLine, false); 
        
     // If the product is not in the order, create a new line
    } else {
        SaleOrderLine newLine = new SaleOrderLine(currentSaleOrder, product, quantity);
        saleOrderDB.insertSaleOrderLine(newLine, false); // Saves the new line in the database
        lines.add(newLine); //Adds the new line to the list 
    }
}

// Adds a customer to the current order
public Customer addCustomerToOrder(Customer customer) throws DataAccessException {
	customer = customerCtr.findByStudentId(customer.getStudentId()); //Finds full customer information 
	
	this.currentSaleOrder.setCustomer(customer); //Sets the customer in the order 
	
	saleOrderDB.update(currentSaleOrder, false); //Updates the order in the database 
	
	return customer; 
	
}

//Updates the whole order and its lines in the database
public void updateOrder(SaleOrder currentOrder) throws DataAccessException {
	saleOrderDB.update(currentSaleOrder, false); // Updates the order itself
	
	for(SaleOrderLine orderLine : currentSaleOrder.getOrderLines()) {
		saleOrderDB.updateOrderLine(orderLine, false); // Updates each product line
	}
}

//Updates a single order line in the order 
public void updateOrderLine(SaleOrderLine orderLine) throws DataAccessException {
	saleOrderDB.updateOrderLine(orderLine, false);
}

//Returns the current sale order object 
public SaleOrder getCurrentSaleOrder() {

	return this.currentSaleOrder; 
	
}

//Returns the product controller 
public ProductCtr getProductCtr() {
	return this.productCtr; 
}

//Returns the customer controller
public CustomerCtr getCustomerCtr() {
	return this.customerCtr; 
}

}
 
