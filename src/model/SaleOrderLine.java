package model;

public class SaleOrderLine {
	private int quantity;
	private Product product;
	private SaleOrder saleOrder;
	
	
	public SaleOrderLine(SaleOrder saleOrder, Product product, int quantity) {
		System.out.println("[SaleOrderLine] Creating new order line:");
	    System.out.println("Quantity: " + quantity);
	    System.out.println("Product: " + product.getProductName());
	    System.out.println("Order: " + (saleOrder != null ? saleOrder.getOrderNumber() : "NULL ORDER!"));
		this.quantity = quantity;
		this.product = product;
		this.saleOrder = saleOrder;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public Product getProduct() {
		return product;
	}


	public void setProduct(Product product) {
		this.product = product;
	}


	public SaleOrder getSaleOrder() {
		return saleOrder;
	}


	public void setSaleOrder(SaleOrder saleOrder) {
		this.saleOrder = saleOrder;
	}
	
	public double getPrice() {
		return product.getSalePrice()*quantity; 
	}
	
	

}