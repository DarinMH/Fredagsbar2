package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	private Connection connection = null; // Holds the database connection object
	private static DBConnection dbConnection; // Static reference to the single instance of DBConnection
	
	private static final String SERVER_ADDRESS = "hildur.ucn.dk"; 
	private static final String DATABASE_NAME = "DMA-CSD-S243_10632105"; 
	private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	
	private static final String USER_NAME = "DMA-CSD-S243_10632105"; 
	private static final String PASSWORD = "Password1!"; 
	private static final int SERVER_PORT = 1433; 
	
	public static void main(String[] args) {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			Connection con = DriverManager.getConnection(
				    "jdbc:sqlserver://hildur.ucn.dk:1433;databaseName=DMA-CSD-S243_10632105;user=DMA-CSD-S243_10632105;password=Password1!;encrypt=false");
			
			
				System.out.println("Connected to database:"+ !con.isClosed()); 
				con.close(); 
				System.out.println("Connected to database:"+ !con.isClosed());
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		
		
		System.out.println("Works"); 
		
	}
	
	private DBConnection() {
		String connectionString = String.format("jdbc:sqlserver://%s:%d;databaseName=%s;user=%s;password=%s;encrypt=false", 
				SERVER_ADDRESS, SERVER_PORT, DATABASE_NAME, USER_NAME, PASSWORD);
		try {
			 // Try to load the JDBC driver for SQL Server
			Class.forName(DRIVER_CLASS);
			connection = DriverManager.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			 // If the JDBC driver is not found, print the error
			System.err.println("Could not load JDBC driver");
			e.printStackTrace();
		} catch (SQLException e) {
			
			 // If there's an error connecting to the database, print details about the error
			System.err.println("Could not connect to database " + DATABASE_NAME + "@" + SERVER_ADDRESS + ":" + SERVER_PORT + " as user " + USER_NAME + " using password ******");
			System.out.println("Connection string was: " + connectionString.substring(0, connectionString.length() - PASSWORD.length()) + "....");
			e.printStackTrace();
		}
	}
	// Static method to get the single instance of DBConnection (Singleton pattern)
	
	public static DBConnection getInstance() {
		if(dbConnection == null) {
			dbConnection = new DBConnection();
		}
		return dbConnection;
	}
	
	
    // Returns the current database connection
	public Connection getConnection() {
		return connection;
	}
	
	  // Closes the connection to the database
	
	public void disconnect() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void startTransaction() throws DataAccessException {
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new DataAccessException(e, "Could not start transaction.");
		}
	}

	public void commitTransaction() throws DataAccessException {
		try {
			try {
				connection.commit();
			} catch (SQLException e) {
				throw e;
				// e.printStackTrace();
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e, "Could not commit transaction");
		}
	}

	public void rollbackTransaction() throws DataAccessException {
		try {
			try {
				connection.rollback();
			} catch (SQLException e) {
				throw e;
				// e.printStackTrace();
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e, "Could not rollback transaction");
		}
	}
	
	public int executeInsertWithIdentity(String sql) throws DataAccessException {
		System.out.println("DBConnection, Inserting: " + sql);
		int res = -1;
		try (Statement s = connection.createStatement()) {
			res = s.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			if (res > 0) {
				ResultSet rs = s.getGeneratedKeys();
				rs.next();
				res = rs.getInt(1);
			}
			// s.close(); -- the try block does this for us now

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new DataAccessException(e, "Could not execute insert (" + sql + ").");
		}
		return res;
	}

	public int executeInsertWithIdentity(PreparedStatement ps) throws DataAccessException {
		// requires perpared statement to be created with the additional argument PreparedStatement.RETURN_GENERATED_KEYS  
		int res = -1;
		try {
			res = ps.executeUpdate();
			if (res > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				rs.next();
				res = rs.getInt(1);
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new DataAccessException(e, "Could not execute insert");
		}
		return res;
	}
	

}