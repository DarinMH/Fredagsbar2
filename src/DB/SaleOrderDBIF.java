
package DB;

import java.util.List;

import model.SaleOrder;
import model.SaleOrderLine;

public interface SaleOrderDBIF {
	
	List<SaleOrder> findAll(boolean fullAssociation) throws DataAccessException; 
	SaleOrder findByOrderNumber(int orderNumber, boolean fullAssociation) throws DataAccessException; 
	void insertSaleOrder(SaleOrder saleOrder, boolean fullAssociation) throws DataAccessException;
	void insertSaleOrderLine(SaleOrderLine saleOrderLine, boolean fullAssociation) throws DataAccessException;
	void update(SaleOrder saleOrder, boolean fullAssociation) throws DataAccessException; 
	void updateOrderLine(SaleOrderLine saleOrderLine, boolean fullAssociation) throws DataAccessException;
	void deleteOrderLine(SaleOrderLine line) throws DataAccessException;

	
	

}
