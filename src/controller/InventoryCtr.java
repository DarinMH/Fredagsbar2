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
	
	
	
	// methods that calls the methods from the InventoryDB class
	
	public List<Inventory> findAll() throws DataAccessException {
		return inventoryDB.findAll(false); 
	}
	
	public List<InventoryProduct> findAllInventoryProduct() throws DataAccessException {
		return inventoryDB.findAllInventoryProduct(); 
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
	
	
	public InventoryProduct addStock(Inventory inventory, Product product, int quantity) throws DataAccessException  {
		synchronized (this) {
		// Method that removes the stock from an inventory 
		inventory = findByInventoryId(inventory.getInventoryId()); 	
		product = productCtr.findByProductId(product.getProductId()); 
		InventoryProduct inventoryProduct = inventoryDB.findInventoryProduct(product.getProductId(), inventory.getInventoryId()); 

		
		// The quantity in stock are being changed based on the quantity
	inventoryProduct.setQuantityInStock(inventoryProduct.getQuantityInStock() + quantity); 
		
	inventoryDB.updateInventoryProduct(inventoryProduct);
	
	return inventoryProduct; 
	
		}
	
		}
	
	// Method that transfers stock from one inventory to another. 
	
	// inventoryFrom is the inventory where stock is being removed from
	
	// inventoryTo is the inventory where stock is being added to
	
	public void transferStock(Inventory inventoryTo, Inventory inventoryFrom, Product product, int quantity) throws DataAccessException {
		synchronized (this) {
		// Method that removes the stock from an inventory 
		inventoryFrom = inventoryDB.findByInventoryId(inventoryFrom.getInventoryId(), false); 
		inventoryTo = inventoryDB.findByInventoryId(inventoryTo.getInventoryId(), false); 
		product = productCtr.findByProductId(product.getProductId()); 
		
		
		// removeStock is being called, and the inventoryFrom object is the parameter. 
		removeStock(inventoryFrom, product, quantity); 
		// addStock is being called and the inventoryTo object is the parameter. 
		addStock(inventoryTo, product, quantity); 

		}
	}
	
	// Method that removes the stock from an inventory 
	
	public InventoryProduct removeStock(Inventory inventory, Product product, int quantity) throws DataAccessException {
		
		synchronized (this) {
		
//		The objects used in the method are being found
		
		inventory = findByInventoryId(inventory.getInventoryId()); 
		product = productCtr.findByProductId(product.getProductId()); 
		InventoryProduct inventoryProduct = inventoryDB.findInventoryProduct(product.getProductId(), inventory.getInventoryId()); 
		
		
		// The quantity in stock are being changed based on the quantity

		inventoryProduct.setQuantityInStock(inventoryProduct.getQuantityInStock() - quantity);

		inventoryDB.updateInventoryProduct(inventoryProduct);

		return inventoryProduct; 
		
		}
	}
	
	// Returns the total stock available for a given product across all inventories
	public int getTotalStock(int productId) throws DataAccessException {
		List<InventoryProduct> inventories = inventoryDB.findInventoryProductByProductId(productId); 
		
		int total = 0; 
		for(InventoryProduct iP : inventories) {
			total += iP.getQuantityInStock(); 
		}
		
		return total; 
	}
	// Returns the total stock for all products in a specific inventory
	public int getTotalInventoryStock(int inventoryId) throws DataAccessException {
		
		List<InventoryProduct> products = inventoryDB.findInventoryProductByInventoryId(inventoryId); 
		
		int totalStock = 0; 
		
		for(InventoryProduct inventory : products) {
			totalStock += inventory.getQuantityInStock(); 
		}
		
		return totalStock; 
		
	}
	
	// Returns the stock for a specific product in a specific inventory
	public int getInventoryStockForProduct(int inventoryId, int productId) throws DataAccessException {
		InventoryProduct ip = findInventoryProduct(productId, inventoryId);
		return ip.getQuantityInStock(); 

	}


	// getter for the productCtr, so the ProductCtr methods can be used in the UI
	public ProductCtr getProductCtr() {
		return productCtr; 
	}
	

}