
package DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DB.DBConnection;
import DB.DataAccessException;
import model.Drink;
import model.Miscellaneous;
import model.Product;
import model.Supplier;

public class ProductDB implements ProductDBIF {
		
		private static final String FIND_ALL_Q = "SELECT p.ProductId, p.productName, p.supplierId, p.SalePrice, p.PurchasePrice, p.productType, " +
		        "COALESCE(d.AlcoholPercentage, '') AS AlcoholPercentage, COALESCE(d.Brand, '') AS Brand, Coalesce(d.Category, '') AS Category, " + 
		        "COALESCE(m.Amount, '') AS Amount " +
		        "FROM product p " + 
				"LEFT JOIN Drink d on p.ProductId = d.ProductId " +
				"LEFT JOIN Miscellaneous m on p.ProductId = m.ProductId";  
		private static final String FIND_BY_PRODUCTID_Q = FIND_ALL_Q + " where p.ProductId = ?";
		private static final String FIND_BY_PRODUCTNAME_Q = FIND_ALL_Q + " where p.productName = ?"; 
//		private static final String FIND_BY_CATEGORY_Q = " SELECT p.ProductName, p.SalePrice FROM Product p JOIN Drink d ON p.ProductId = d.ProductId WHERE d.Category = ? ";
		private static final String FIND_BY_CATEGORY_Q = "Select p.*, d.alcoholPercentage, d.brand, d.category " + 
		"From Product p JOIN Drink d ON p.ProductId = d.ProductId WHERE d.category = ?"; 
		private static final String FIND_ALL_MISCELLANEOUS_Q =   "SELECT p.*, m.amount FROM Product p JOIN Miscellaneous m ON p.ProductId = m.ProductId " +
			    "WHERE p.productType = 'Miscellaneous'";
		private PreparedStatement findAll; 
		private PreparedStatement findByProductId; 
		private PreparedStatement findByProductName; 
		private PreparedStatement findByCategory; 
		private PreparedStatement findAllMiscellaneous; 
		private InventoryDBIF inventoryDB; 

public ProductDB(InventoryDBIF inventoryDB) throws DataAccessException {
	this.inventoryDB=inventoryDB; 
	init(); 
}

public ProductDB() throws DataAccessException {
	inventoryDB = new InventoryDB(this); 
	init(); 
}

	
	//Initializes the ProductDB class and prepares SQL statements.
	
	private void init() throws DataAccessException {
		try {
		findAll = DBConnection.getInstance().getConnection()
				.prepareStatement(FIND_ALL_Q); 
		findByProductId = DBConnection.getInstance().getConnection()
				.prepareStatement(FIND_BY_PRODUCTID_Q);
		findByProductName = DBConnection.getInstance().getConnection()
				.prepareStatement(FIND_BY_PRODUCTNAME_Q);
		findByCategory = DBConnection.getInstance().getConnection().prepareStatement(FIND_BY_CATEGORY_Q); 
		findAllMiscellaneous = DBConnection.getInstance().getConnection().prepareStatement(FIND_ALL_MISCELLANEOUS_Q); 
		}catch(SQLException e) {
			throw new DataAccessException(e, "Could not prepare statement"); 
		}

	}


	@Override
	// Retrieves all products from the database.
	public List<Product> findAll(boolean fullAssociation) throws DataAccessException {
		// TODO Auto-generated method stub
		try { 
			ResultSet rs = findAll.executeQuery(); 
		List<Product> res = buildObjects(rs, fullAssociation); 
		return res; 
		} catch (SQLException e) {
		DataAccessException d = new DataAccessException(e, "Could not find them");
		throw d; 
		}
	}
	
	@Override
	public List<Miscellaneous> findAllMiscelaneous() throws DataAccessException {
		// TODO Auto-generated method stub
		List<Miscellaneous> miscellaneous = new ArrayList<>(); 
		try {
			ResultSet rs = findAllMiscellaneous.executeQuery(); 
		
			
			while(rs.next()) {
				Product product = buildObject(rs, false); 
				if(product instanceof Miscellaneous) {
					miscellaneous.add((Miscellaneous) product); 
				}
			}
		} catch (SQLException e) {
			DataAccessException d = new DataAccessException(e, "Could not find Miscellaneous products"); 
			throw d; 
		}
		
		return miscellaneous;
	}

	// Retrieves a product by its.
	public Product findByProductId(int ProductId, boolean fullAssociation) throws DataAccessException {
	
		try {
			findByProductId.setInt(1, ProductId); 
			ResultSet rs = findByProductId.executeQuery(); 
			Product p = null; 
			if(rs.next()) {
				p = buildObject(rs, fullAssociation); 
			}
			return p; 
		}catch(SQLException e) {
			throw new DataAccessException(e, "Could not find by Product ID = " + ProductId); 
		}
		
	}
		@Override
		// Retrieves a product by its.
		public Product findByProductName(String productName) throws DataAccessException {
			// TODO Auto-generated method stub
			try {
				findByProductName.setString(1, productName); 
				ResultSet rs = findByProductName.executeQuery(); 
				Product p = null; 
				if(rs.next()) {
					p = buildObject(rs, false); 
				}
				return p; 
			}catch(SQLException e) {
				throw new DataAccessException(e, "Could not find by Product Name = " + productName); 
			}
	}
		
		@Override
		public List<Drink> findByCategory(String category) throws DataAccessException {
			// TODO Auto-generated method stub
			List<Drink> drinks = new ArrayList<>(); 
			try {
			findByCategory.setString(1, category); 
			ResultSet rs = findByCategory.executeQuery(); 

			while(rs.next()) {
				Product product = buildObject(rs, false); 
				if(product instanceof Drink) {
					drinks.add((Drink) product); 
				}
			}
			return drinks; 
			}catch(SQLException e) {
				throw new DataAccessException(e, "Could not find by Category = " + category); 
			}
		}

	// Builds a product object from a database result set.
	private Product buildObject(ResultSet rs, boolean fullAssociation) throws SQLException {
		
		String productType = rs.getString("productType"); 
		Product product = null; 
		
		
		switch(productType.toLowerCase())  {
		case "drink": product = new Drink(
				rs.getString("productName"), 
				rs.getDouble("purchasePrice"), 
				rs.getDouble("salePrice"),
				rs.getInt("ProductId"),
				rs.getString("productType"),
				new Supplier(rs.getInt("supplierId"), null, null, null),
				rs.getDouble("alcoholPercentage"), 
				rs.getString("brand"),
				rs.getString("category")
				); 
		break; 
		case "miscellaneous": product = new Miscellaneous(
				rs.getInt("amount"),
				rs.getString("productName"), 
				rs.getDouble("purchasePrice"), 
				rs.getDouble("salePrice"),
				rs.getInt("ProductId"),
				rs.getString("productType"),
				new Supplier(rs.getInt("supplierId"), null, null, null)
				);
		break; 
		default:
            System.out.println("Unknown product type: " + productType);
            product = null; // Set product to null for unknown types
            break;
		}
            return product; 
				
	}
	// Inserts a new product into the database.
	private List<Product> buildObjects(ResultSet rs, boolean fullAssociation) throws SQLException {
		List<Product> res = new ArrayList<>(); 
		while(rs.next()) {
			res.add(buildObject(rs, fullAssociation)); 
		}
		return res; 
	
	}

	



}
