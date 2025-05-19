package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Product {
	private String productName;;
	private double purchasePrice;
	private double salePrice;
	private int productId;
	private String productType; 
	private Supplier supplier;
	private List<InventoryProduct> products; 
	
	public Product() {
		
	}
	
	public Product(int productId) {
	 this.productId=productId; 
	}
	
	public Product(String productName, double purchasePrice, double salePrice, int productId, String productType,
			Supplier supplier) {
		super();
		this.productName = productName;
		this.purchasePrice = purchasePrice;
		this.salePrice = salePrice;
		this.productId = productId;
		this.productType=productType; 
		this.products = new ArrayList<>(); 

		this.productType = productType;
		this.supplier = supplier;

	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public Double getPurchasePrice() {
		return purchasePrice;
	}


	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	

	public Double getPurchesPrice() {
		return purchasePrice;
	}

	public void setPurchesPrice(Double purchesPrice) {
		this.purchasePrice = purchesPrice;
	}


	public Double getSalePrice() {
		return salePrice;
	}


	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}





	public Supplier getSupplier() {
		return supplier;
	}


	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	
	
	public int getTotalQuantityInStock() {
		int totalQuantity = 0; 
		
		for(InventoryProduct iv : products) {
			totalQuantity += iv.getQuantityInStock(); 
		}
		
		return totalQuantity; 
	}
	
	
	public List<InventoryProduct> getInventoryProduct() {
		return products; 
	}
	
	public void setInventoryProducts(List<InventoryProduct> products) {
	    this.products = products;
	}
	
	
	@Override 
	public String toString() {
		return productName; 
	}
	
}