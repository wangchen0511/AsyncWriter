package chen.async.write;

/**
 * Create a connection:
 * 	
 * 	Firstly call the static block, which will find the proper JDBC driver. 
 * 
 * 	DriverManager.getConnection will create a new connection. So we need a DBPools to improve
 * 	the performance.
 * 
 * 
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateConnection {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/test";

	/**
	 * Applications no longer need to explictly load JDBC drivers using
	 * Class.forName(). Existing programs which currently load JDBC drivers
	 * using Class.forName() will continue to work without modification.
	 */

	/**
	 * static { try { // The newInstance() call is a work around for some //
	 * broken Java implementations Class.forName(JDBC_DRIVER).newInstance(); }
	 * catch (Exception ex) { throw new RuntimeException(
	 * "Can not instantialize the jdbc driver!", ex); } }
	 */
	public Connection createConnection() {
		Connection conn = null;
		try {
			/*
			 * url - a database url of the form jdbc:subprotocol:subname here
			 * test is database name localhost is the url.
			 * 
			 * If we want to add password, we can use,
			 * user=mysql&password=greatsqldb
			 * 
			 * Or also we can use like getConnection(url,user,password)
			 * 
			 * If we do not have password, we do not need add &password, or we
			 * can set the third field as null or ""
			 * 
			 * We need to call
			 */
			// conn = DriverManager.getConnection("jdbc:mysql://localhost/test?"
			// + "user=mysql");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/test?",
					"mysql", null);
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return conn;
	}

}

