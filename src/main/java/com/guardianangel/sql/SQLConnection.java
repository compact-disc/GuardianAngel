package com.guardianangel.sql;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 11/21/2019
 * 
 * This is a object to store the connection to the SQL server so it only has to be created once.
 * Getter method returns the object needs to make requests.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {
	
	/**
	 * Stored connection to the SQL server
	 */
	private static Connection sqlConnection;
	
	/**
	 * Check if the SQL is connected or not
	 */
	private static Boolean connected = false;
	
	/**
	 * Method to connect to the SQL server
	 */
	private static void connectToSQL() {
		
		//try to connect to the server
		try {
			
			//Connect to the SQL server
			sqlConnection = DriverManager.getConnection("jdbc:mysql://localhost/guardian_angel", "root", null);
			
			connected = true;
			
		} catch (SQLException se) { //catch for any error while trying to connect to the SQL server, also sent to logger
			
			se.printStackTrace();
			
		}
		
	}
	
	/**
	 * Returns the connection to the SQL server
	 * 
	 * @return the connection object that can be used for query
	 */
	public static Connection getSQLConnection() {
		
		if(connected) {
			
			return sqlConnection;
			
		}else {
			
			connectToSQL();
			return sqlConnection;
			
		}
		
	}

}
