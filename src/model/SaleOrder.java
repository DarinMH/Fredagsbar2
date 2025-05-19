
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

//	public double getTotalPrice() {
//		
//		double sum = 0;
//		
//		
//		
//		for(SaleOrderLine orderLine: orderLines) {	
//			 
//			if(customer == null || customer.getAmount() < 10) {
//				sum += orderLine.getPrice();
//				
//			} else { 		
//				sum += orderLine.getPrice()*0.8; 
//			
//			}
//		}
//		
//
//		
//	
//		return sum;
//	}
	
	
	public double getTotalPrice() {
		double sum = 0; 
		
		for(SaleOrderLine orderLine : orderLines) {
			sum += orderLine.getPrice()*discountPercentage; 
			
		}
		
		return sum; 
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	
//	public double getDiscountPercentage() {
//		discountPercentage = 0; 
//		
//		
//		
//		if(customer == null && totalPrice >= 100) {
//			discountPercentage = 0.90;
//		} else if (customer != null || customer.getAmount() >= 10) {
//			discountPercentage = 0.90;
//		} else if ((customer != null || customer.getAmount() >= 10) && totalPrice >= 100) {
//			discountPercentage = 0.80; 
//		} 
//		else {
//			discountPercentage = 1;
//		}
//		
//		return discountPercentage; 
//	}
	
	
	public double getDiscountPercentage() {
		discountPercentage = 0; 
		
		
		
		if((customer == null && totalPrice < 100) || (customer != null && customer.getAmount() < 10 && totalPrice < 100)) {
			discountPercentage = 1; 
			
		} else if (customer != null && customer.getAmount() >= 10 && totalPrice >= 100) {
			discountPercentage = 0.80; 
		} else if((customer == null && totalPrice >= 100) || (customer != null && customer.getAmount() >= 10)) {
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
	
	public List<SaleOrderLine> getOrderLines() {
	return orderLines; 
}
	
//	public SaleOrderLine getOrderLine() {
//		return saleOrderLine; 
//	}


}
