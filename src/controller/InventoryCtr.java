package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DB.DataAccessException;
import DB.InventoryDB;
import DB.InventoryDBIF;
import model.Inventory;
import model.InventoryProduct;
import model.Product;

public class InventoryCtr {
	
	
	private InventoryDBIF inventoryDB; 
	private InventoryProduct inventoryProduct; 
	private ProductCtr productCtr; 
	private List<InventoryProduct> products; 
	
	
	public InventoryCtr() throws DataAccessException {
		this.inventoryDB= new InventoryDB(); 
		this.productCtr = new ProductCtr(); 
		this.products= new ArrayList<>(); 
	}
	
	
	public List<Inventory> findAll() throws DataAccessException {
		return inventoryDB.findAll(false); 
	}
	
	public Inventory findByInventoryId(int inventoryId) throws DataAccessException {
		return inventoryDB.findByInventoryId(inventoryId, false); 
	}
	
	public InventoryProduct findInventoryProduct(int productId, int inventoryId) throws DataAccessException {
		return inventoryDB.findInventoryProduct(productId, inventoryId); 
	}
	
	public List<InventoryProduct> findInventoryProductByProductId(int productId) throws DataAccessException {
		return inventoryDB.findInventoryProductByProductId(productId); 
	}
	
	public List<InventoryProduct> findInventoryProductByInventoryId(int inventoryId) throws DataAccessException {
		return inventoryDB.findInventoryProductByInventoryId(inventoryId); 
	}
//	
//	public InventoryProduct addStock(Inventory inventory, Product product, int quantity) throws DataAccessException {
//		
//		try {
//		inventory = findByInventoryId(inventory.getInventoryId()); 
//		
//		product = productCtr.findByProductId(product.getProductId()); 
//		
//		
////		InventoryProduct inventoryProduct = new InventoryProduct(inventory, quantity, product); 
//		
//		InventoryProduct inventoryProduct = inventoryDB.findInventoryProduct(product.getProductId(), inventory.getInventoryId()); 
//		
//
//		
//	    
//	
//	    // Check if new quantity exceeds capacity
//			int newQuantity = inventoryProduct.getQuantityInStock() + quantity;
//	    if (newQuantity > inventory.getCapacity()) {
//	    	  throw new DataAccessException(
//	                  new RuntimeException("Capacity exceeded"), 
//	                  "Cannot add " + quantity + " units. Max capacity: " + inventory.getCapacity()
//	              );
//	     
//	    }
//		
//	
////		int newQuantity = inventoryProduct.getQuantityInStock() + quantity; 
//		
//	inventoryProduct.setQuantityInStock(inventoryProduct.getQuantityInStock() + quantity); 
//		
//	inventoryDB.updateInventoryProduct(inventoryProduct);
//	
//	return inventoryProduct; 
//	
//	} catch (SQLException e) {
//        // Handle ACTUAL database errors
//        throw new DataAccessException(e, "Database error while updating stock");
//    }
//	
//		
//
//
//		
//		
//	}
	
	
	public InventoryProduct addStock(Inventory inventory, Product product, int quantity) throws DataAccessException  {
		
		

		
		
		inventory = findByInventoryId(inventory.getInventoryId()); 
		
		product = productCtr.findByProductId(product.getProductId()); 
		
		
		
		InventoryProduct inventoryProduct = inventoryDB.findInventoryProduct(product.getProductId(), inventory.getInventoryId()); 
		
		
		
	inventoryProduct.setQuantityInStock(inventoryProduct.getQuantityInStock() + quantity); 
		
	inventoryDB.updateInventoryProduct(inventoryProduct);
	
	return inventoryProduct; 
	
		}
	
	

		
	
	
//	public int getTotalStock(int productId) throws DataAccessException {
//		int total = 0; 
//		Product p = productCtr.findByProductId(getTotalStock(productId)); 
//		
//		for(InventoryProduct iP : products) {
//			total += iP.getQuantityInStock(); 
//		}
//		return total; 
//	}
	
	
	public int getTotalStock(int productId) throws DataAccessException {
		List<InventoryProduct> inventories = inventoryDB.findInventoryProductByProductId(productId); 
		
		int total = 0; 
		for(InventoryProduct iP : inventories) {
			total += iP.getQuantityInStock(); 
		}
		
		return total; 
	}
	
	public int getTotalInventoryStock(int inventoryId) throws DataAccessException {
		
		List<InventoryProduct> products = inventoryDB.findInventoryProductByInventoryId(inventoryId); 
		
		int totalStock = 0; 
		
		for(InventoryProduct inventory : products) {
			totalStock += inventory.getQuantityInStock(); 
		}
		
		return totalStock; 
		
	}
	
	
	public void transferStock(Inventory inventoryTo, Inventory inventoryFrom, Product product, int quantity) throws DataAccessException {
		
		
		inventoryFrom = inventoryDB.findByInventoryId(inventoryFrom.getInventoryId(), false); 
		
		inventoryTo = inventoryDB.findByInventoryId(inventoryTo.getInventoryId(), false); 
		
		product = productCtr.findByProductId(product.getProductId()); 
		
		
		removeStock(inventoryFrom, product, quantity); 
		addStock(inventoryTo, product, quantity); 
		
	
		
		
		
	}
	
	
//	public int getInventoryStockForProduct(int inventoryId, int productId) throws DataAccessException {
//		InventoryProduct ip = findInventoryProduct(productId, inventoryId);
//		return ip != null ? ip.getQuantityInStock() : 0;
//
//	}
	
	public int getInventoryStockForProduct(int inventoryId, int productId) throws DataAccessException {
		InventoryProduct ip = findInventoryProduct(productId, inventoryId);
		return ip.getQuantityInStock(); 

	}

	
	
	public List<InventoryProduct> findAllInventoryProduct() throws DataAccessException {
		return inventoryDB.findAllInventoryProduct(); 
	}
	
	public InventoryProduct removeStock(Inventory inventory, Product product, int quantity) throws DataAccessException {
		inventory = findByInventoryId(inventory.getInventoryId()); 
		product = productCtr.findByProductId(product.getProductId()); 

		InventoryProduct inventoryProduct = inventoryDB.findInventoryProduct(product.getProductId(), inventory.getInventoryId()); 

		int newQuantity = inventoryProduct.getQuantityInStock() - quantity;

		inventoryProduct.setQuantityInStock(newQuantity);

		inventoryDB.updateInventoryProduct(inventoryProduct);

		return inventoryProduct; 
	}
	
	
	

	
	
	
	public ProductCtr getProductCtr() {
		return productCtr; 
	}
	
	

	
	
	
	

}