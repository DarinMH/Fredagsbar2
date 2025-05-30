package DB;

import java.util.List;
import model.BorrowableProduct;

// Interface til produkter der kan lånes
public interface BorrowableProductDBIF {

    // Henter alle lånbare produkter
    List<BorrowableProduct> findAll() throws DataAccessException;

    // Finder ét lånbart produkt baseret på ID
    BorrowableProduct findByProductId(int productId, boolean fullAssociation) throws DataAccessException;
    
    
    void updateStatus(BorrowableProduct product) throws DataAccessException; 
}
