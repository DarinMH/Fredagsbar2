package DB;

import java.util.List;
import model.Inventory;
import model.InventoryProduct;

public interface InventoryDBIF {
	List<Inventory> findAll (boolean fullAssociation) throws DataAccessException;
	Inventory findByInventoryId (int inventoryId, boolean fullAssociation) throws DataAccessException;
InventoryProduct findInventoryProduct (int productId, int inventoryId) throws DataAccessException; 
	void updateInventoryProduct(InventoryProduct inventoryProduct) throws DataAccessException; 
	List<InventoryProduct> findInventoryProductByProductId (int productId) throws DataAccessException; 
	List<InventoryProduct> findAllInventoryProduct() throws DataAccessException; 
	List<InventoryProduct> findInventoryProductByInventoryId(int inventoryId) throws DataAccessException; 
	
}
