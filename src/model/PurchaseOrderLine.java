package model;

public class PurchaseOrderLine {

	private int quantity;
	private PurchaseOrder purchaseOrder;
	private Product product;
	
	public PurchaseOrderLine(int quantity, PurchaseOrder purchaseOrder, Product product) {
		super();
		this.quantity = quantity;
		this.purchaseOrder = purchaseOrder;
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
	
	
	
	
}
