
package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleOrder {
	private int orderNumber;
	private LocalDate date;
	private boolean status; 
	private Customer customer; 
	private Staff staff;
	private double totalPrice; 
	private double discountPercentage; 
	private List<SaleOrderLine> orderLines; 
	private SaleOrderLine saleOrderLine; 
	
	public SaleOrder() {
		
	}
	


	public SaleOrder(int orderNumber, LocalDate date, boolean status, Customer customer, Staff staff, double totalPrice,
			double discountPercentage) {
		this.orderNumber = orderNumber;
		this.date = date;
		this.status = status;
		this.customer = customer;
		this.staff = staff;
		this.totalPrice = totalPrice; 
		this.discountPercentage=discountPercentage; 
		orderLines = new ArrayList<>(); 

	}
	
	
	// Getters and setters

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}


	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public double getTotalPrice() {
		
//		Total price is calculated by adding all the prices of the order lines together, by a loop:
		double sum = 0; 
		
		for(SaleOrderLine orderLine : orderLines) {
			sum += orderLine.getPrice()*discountPercentage; 
			
		}
		
		return sum; 
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public List<SaleOrderLine> getOrderLines() {
	return orderLines; 
}
	
	
//	The discount percentage is being calculated in this method, based on different factors: 
	
	public double getDiscountPercentage() {
		discountPercentage = 0; 
		
		
// the discountPercentage value is the one thats being multiplied with the total price of the order. 
//		So if the discount percentage is 0, you multiply the total price with 1, since it just means that the total price does not change. 
		if((customer == null && totalPrice < 100) || (customer != null && customer.getNumberOfCustomers() < 10 && totalPrice < 100)) {
			discountPercentage = 1; 
//		
		} else if (customer != null && customer.getNumberOfCustomers() >= 10 && totalPrice >= 100) {
			discountPercentage = 0.80; 
		} else if((customer == null && totalPrice >= 100) || (customer != null && customer.getNumberOfCustomers() >= 10)) {
			discountPercentage = 0.90; 
		}
		
		return discountPercentage; 
		
	}
	
	
	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage=discountPercentage; 
	}
	
	
	
	public void addOrderline(SaleOrderLine orderLine) {
		orderLines.add(orderLine); 
	}
	



}
