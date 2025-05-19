
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


public class SaleOrderCtr {
private SaleOrderDBIF saleOrderDB; 
private CustomerCtr customerCtr; 
private ProductCtr productCtr; 
private SaleOrder currentSaleOrder; 
private List<SaleOrderLine> orderLines; 



public SaleOrderCtr() throws DataAccessException { 
	
	customerCtr = new CustomerCtr(); 
	productCtr = new ProductCtr(); 
	this.saleOrderDB = new SaleOrderDB(); 
	orderLines = new ArrayList<>(); 
	
}



public boolean confirmOrder() throws DataAccessException {

	this.currentSaleOrder.setStatus(true);
	saleOrderDB.update(currentSaleOrder, false);
	
	if(currentSaleOrder.isStatus() == true) {
		
	return true; 
	}
	
	return false; 
}


// finding all the orders
public List<SaleOrder> findAll() throws DataAccessException {
return saleOrderDB.findAll(false); 

}



public SaleOrder createSaleOrder(int orderNumber, LocalDate date,  boolean status, 
		Customer customer, Staff staff, double totalPrice, double discountPercentage) throws DataAccessException { 
	 
	 SaleOrder newSaleOrder = new SaleOrder(orderNumber, date, status,  customer, staff, totalPrice, discountPercentage); 

	 
	 saleOrderDB.insertSaleOrder(newSaleOrder, false);
	 this.currentSaleOrder = newSaleOrder; 
	 

	 
	return newSaleOrder; 
}


//public SaleOrder createBasicSaleOrder(int orderNumber) throws DataAccessException {
//	
//	SaleOrder saleOrder = createSaleOrder(orderNumber, LocalDate.now(), false, null, null, 0, 0); 
//	
//	saleOrderDB.insertSaleOrderBasic(saleOrder, false);
//	
//	return saleOrder; 
//	
//	
//}

public SaleOrder findByOrderNumber(int orderNumber) throws DataAccessException {
	
	return saleOrderDB.findByOrderNumber(orderNumber, true);
}

public void setCurrentOrder(SaleOrder order) {
	this.currentSaleOrder=order; 
}

public void deleteOrderLine(SaleOrderLine line) throws DataAccessException {
    saleOrderDB.deleteOrderLine(line); // You must create this in SaleOrderDB
}



public void addProductToOrder(Product product, int quantity) throws DataAccessException {
    List<SaleOrderLine> lines = currentSaleOrder.getOrderLines();
    SaleOrderLine existingLine = null;

    for (SaleOrderLine line : lines) {
        if (line.getProduct().getProductId() == product.getProductId()) {
            existingLine = line;
            break;
        }
    }

    if (existingLine != null) {
        existingLine.setQuantity(existingLine.getQuantity() + quantity);
        saleOrderDB.updateOrderLine(existingLine, false);
    } else {
        SaleOrderLine newLine = new SaleOrderLine(currentSaleOrder, product, quantity);
        saleOrderDB.insertSaleOrderLine(newLine, false);
        lines.add(newLine);
    }
}




public Customer addCustomerToOrder(Customer customer) throws DataAccessException {

	
	customer = customerCtr.findByStudentId(customer.getStudentId());
	
	this.currentSaleOrder.setCustomer(customer);
	
	saleOrderDB.update(currentSaleOrder, false);
	
	return customer; 
	
}

public void updateOrder(SaleOrder currentOrder) throws DataAccessException {
	saleOrderDB.update(currentSaleOrder, false);
	
	for(SaleOrderLine orderLine : currentSaleOrder.getOrderLines()) {
		saleOrderDB.updateOrderLine(orderLine, false);
	}
}


public void updateOrderLine(SaleOrderLine orderLine) throws DataAccessException {
	saleOrderDB.updateOrderLine(orderLine, false);
}

public SaleOrder getCurrentSaleOrder() {

	return this.currentSaleOrder; 
	
}


public ProductCtr getProductCtr() {
	return this.productCtr; 
}

public CustomerCtr getCustomerCtr() {
	return this.customerCtr; 
}




}
 
