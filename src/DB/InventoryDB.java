package DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DB.DataAccessException;
import model.Drink;
import model.Inventory;
import model.InventoryProduct;
import model.Miscellaneous;
import model.Product;
import model.SaleOrder;
import DB.DBConnection;

public class InventoryDB implements  InventoryDBIF{

	private static final String FIND_ALL_Q =
			"select * from inventory";
	private static final String FIND_BY_INVENTORY_ID_Q =
			FIND_ALL_Q + " where InventoryId = ?"; 
	private static final String FIND_INVENTORY_PRODUCT_Q = "select ip.*, p.productType from InventoryProduct ip JOIN Product p ON ip.ProductId = p.ProductId where ip.ProductId = ? and ip.inventoryId = ?"; 
	private static final String FIND_INVENTORY_PRODUCT_BY_PRODUCT_ID_Q = "select ip.*, p.productType from InventoryProduct ip JOIN Product p ON ip.ProductId = p.ProductId where ip.ProductId = ?"; 
	private static final String FIND_ALL_INVENTORY_PRODUCT_Q = "select * from InventoryProduct"; 
	private static final String UPDATE_INVENTORY_PRODUCT_Q = "update InventoryProduct set QuantityInStock = ? where inventoryId = ? and ProductId = ?"; 
	private static final String FIND_INVENTORY_PRODUCT_BY_INVENTORY_ID_Q = FIND_ALL_INVENTORY_PRODUCT_Q + " where inventoryId = ?"; 
	private PreparedStatement findAll; 
	private PreparedStatement findByInventoryId;
	private PreparedStatement findInventoryProduct; 
	private PreparedStatement updateInventoryProduct; 
	private PreparedStatement findInventoryProductByProductId; 
	private PreparedStatement findAllInventoryProduct; 
	private PreparedStatement findInventoryProductByInventoryId; 
	private ProductDBIF productDB; 
	
	public InventoryDB(ProductDBIF productDB) throws DataAccessException {
		this.productDB = productDB; 
		init(); 
	}
	
	public InventoryDB() throws DataAccessException {
		productDB = new ProductDB(); 
		init(); 
	}
	
	private void init() throws DataAccessException {
		try {
			findInventoryProductByInventoryId = DBConnection.getInstance().getConnection().prepareStatement(FIND_INVENTORY_PRODUCT_BY_INVENTORY_ID_Q);
			findAll = DBConnection.getInstance().getConnection().prepareStatement(FIND_ALL_Q);
			findByInventoryId = DBConnection.getInstance().getConnection().prepareStatement(FIND_BY_INVENTORY_ID_Q);
			findInventoryProduct =  DBConnection.getInstance().getConnection().prepareStatement(FIND_INVENTORY_PRODUCT_Q);
			updateInventoryProduct = DBConnection.getInstance().getConnection().prepareStatement(UPDATE_INVENTORY_PRODUCT_Q); 
			findInventoryProductByProductId = DBConnection.getInstance().getConnection().prepareStatement(FIND_INVENTORY_PRODUCT_BY_PRODUCT_ID_Q); 
			findAllInventoryProduct = DBConnection.getInstance().getConnection().prepareStatement(FIND_ALL_INVENTORY_PRODUCT_Q); 
	
		}catch(SQLException e) {
			throw new DataAccessException(e, "Could not prepare statement");
			
		}
	}
	@Override
	public List<Inventory>findAll(boolean fullAssociation) throws DataAccessException{
		try {
			ResultSet rs = findAll.executeQuery();
			List<Inventory> res = buildObjects(rs, false);
			return res;
		}catch (SQLException e) {
			DataAccessException he = new DataAccessException(e, "Could not find all");
			throw he;
		}
	}
	@Override
	public Inventory findByInventoryId(int inventoryId, boolean fullAssociation) throws DataAccessException {
		try {
			findByInventoryId.setInt(1, inventoryId);
			ResultSet rs = findByInventoryId.executeQuery();
			Inventory inventory = null;
			if(rs.next()) {
				inventory = buildObject(rs, fullAssociation);
			}
			return inventory;
		}catch (SQLException e) {
			throw new DataAccessException(e, "could not find customer by InventoryId="+ inventoryId);
		}
	}
	
	@Override
	public InventoryProduct findInventoryProduct(int productId, int inventoryId) throws DataAccessException {
		// TODO Auto-generated method stub
		try {
			findInventoryProduct.setInt(1, productId);
			findInventoryProduct.setInt(2, inventoryId);
			ResultSet rs = findInventoryProduct.executeQuery(); 
			InventoryProduct inventoryProduct = null; 
			if(rs.next()) {
				inventoryProduct = buildInventoryProduct(rs, true); 
			}

			return inventoryProduct;  
			
		} catch (SQLException e) {
			
			throw new DataAccessException(e, "could not find inventoryProduct"); 
		}
		
	}
	
	
	
	
	private Inventory buildObject (ResultSet rs, boolean fullAssociation) throws SQLException {
		Inventory inventory = new Inventory(
				rs.getInt("InventoryId"),
				rs.getString("Location"),
				rs.getInt("Capacity")

				
				);
		return inventory;
	}
	
//	private InventoryProduct buildInventoryProduct(ResultSet rs) throws SQLException {
//		String productType = rs.getString("productType"); 
//		Product product = null; 
//		switch(productType) {
//		case "Drink": product = new Drink(null, 0, 0, rs.getInt("ProductId"), null, null, 0, null, null); 
//		break; 
//		case "Miscellaneous": product = new Miscellaneous(0, null, 0, 0, rs.getInt("ProductId"), null, null);
//		
//	
//		}
//	
//		
//		InventoryProduct inventoryProduct = new InventoryProduct(
//	   new Inventory(rs.getInt("inventoryId"), null, 0),
//		rs.getInt("QuantityInStock"),
//		product); 
//		
//		
//		return inventoryProduct; 
//		
//		
//		
//	}
	
	
	private InventoryProduct buildInventoryProduct(ResultSet rs, boolean fullAssociation) throws DataAccessException {
		InventoryProduct inventoryProduct = new InventoryProduct(); 
		try {
			int inventoryId = rs.getInt("InventoryId");
	        int productId = rs.getInt("ProductId");
	        int quantity = rs.getInt("QuantityInStock");
	        
	        Inventory inventory = new Inventory(inventoryId);
	        Product product = productDB.findByProductId(productId, false);
			
			
			inventoryProduct.setInventory(inventory); 
			inventoryProduct.setQuantityInStock(quantity); 
			inventoryProduct.setProduct(product); 
			
			if(fullAssociation) {
				Inventory fullInventory = findByInventoryId(inventoryId, true); 
				Product fullProduct = productDB.findByProductId(productId, true); 
				inventoryProduct.setInventory(fullInventory);
				inventoryProduct.setProduct(fullProduct);
			}
		}catch(SQLException e) {
			throw new DataAccessException(e, "Could not read resultSet"); 
		}
		return inventoryProduct; 
					 
		
	}
	
	private List<Inventory> buildObjects(ResultSet rs, boolean fullAssociation) throws SQLException {
		List<Inventory> res = new ArrayList<>(); 
		while(rs.next()) {
			res.add(buildObject(rs, false)); 
		}
		return res; 
	}
	
	private List<InventoryProduct> buildInventoryProductObjects(ResultSet rs, boolean fullAssociation) throws DataAccessException {
		List<InventoryProduct> res = new ArrayList<>(); 
		try {
			
		while(rs.next()) {
			res.add(buildInventoryProduct(rs, false)); 
		}
	}catch(SQLException e) {
		throw new DataAccessException(e, "Could not build inventoryProducts"); 
	}
		return res; 
	}
	
	@Override
	public void updateInventoryProduct(InventoryProduct inventoryProduct) throws DataAccessException {
		final int inventoryId = inventoryProduct.getInventory().getInventoryId(); 
		final int quantityInStock = inventoryProduct.getQuantityInStock(); 
		final int productId = inventoryProduct.getProduct().getProductId(); 
		
		try {
			
		updateInventoryProduct.setInt(1, quantityInStock);
		updateInventoryProduct.setInt(2, inventoryId);
		updateInventoryProduct.setInt(3, productId);
		updateInventoryProduct.executeUpdate(); 
		
		} catch(SQLException e) {
			throw new DataAccessException(e, "Could not update inventoryProduct " + inventoryId + productId);
		
		
		
	}
	}
	@Override
	public List<InventoryProduct> findInventoryProductByProductId(int productId) throws DataAccessException {
		try {
			findInventoryProductByProductId.setInt(1, productId);
			ResultSet rs = findInventoryProductByProductId.executeQuery(); 
			List<InventoryProduct> res = new ArrayList<>();  
			while(rs.next()) {
				res.add(buildInventoryProduct(rs, false)); 
			}
			return res;  
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DataAccessException(e, "could not find inventoryProduct by productId"); 
		}
	}
	@Override
	public List<InventoryProduct> findAllInventoryProduct() throws DataAccessException {
		// TODO Auto-generated method stub
		try {
			ResultSet rs = findAllInventoryProduct.executeQuery(); 
			List<InventoryProduct> res = buildInventoryProductObjects(rs, false); 
			System.out.println("Number of objects found: " + res.size()); 
			return res; 
		} catch (SQLException e) {
			DataAccessException d = new DataAccessException(e, "could not find all orders"); 
			throw d; 
		}
	}

	@Override
	public List<InventoryProduct> findInventoryProductByInventoryId(int inventoryId) throws DataAccessException {
		try {
			findInventoryProductByInventoryId.setInt(1, inventoryId);
			ResultSet rs = findInventoryProductByInventoryId.executeQuery(); 
			List<InventoryProduct> res = new ArrayList<>();  
			while(rs.next()) {
				res.add(buildInventoryProduct(rs, false)); 
			}
			return res;  
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DataAccessException(e, "could not find inventoryProduct by inventoryId"); 
		}

}
	
}
