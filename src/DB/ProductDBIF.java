
package DB;

import java.util.List;

import model.Drink;
import model.Miscellaneous;
import model.Product;

public interface ProductDBIF {
		List<Product> findAll(boolean fullAssociation) throws DataAccessException; 
		Product findByProductId(int productId, boolean fullAssociation) throws DataAccessException; 		
		Product findByProductName(String productName) throws DataAccessException; 
		List<Drink> findByCategory(String category) throws DataAccessException; 
		List<Miscellaneous> findAllMiscelaneous() throws DataAccessException; 
} 
