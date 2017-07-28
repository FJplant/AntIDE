/*
 *	package : test
 *	source  : SimpleQuery.java
 *	date    : 1999.8.9
 */

package test;

import java.sql.*;

public class SimpleQuery extends { 
	/**
	 * <p>Main entry point for the application
	 */
	public static void main(String args[]) {
		try {
			// Perform the simple query and display the results
			performQuery();
		} catch (Exception ex) {
			ex.printStackTrace();
		}				
	}
	public static void performQuery() throws Exception {
		// The name of the JDBC driver to use
		String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
		
		// The JDBC connection URL
		String connectionURL = "jdbc:odbc:MyAccessDataSource";
		
		// The JDBC Connection object
		Connection con = null;
		
		// The JDBC Statement object
		Statement stmt = null;
		
		// The SQL statement to execute
		String sqlStatement = "SELECT Empno, Name, Position FROM Employee";
		
		// The JDBC ResultSet object
		ResultSet rs = null;
		
		try {
			System.out.println("Registering " + driverName);
			
			// Create an instance of the JDBC driver so that it has
			// a chance to register itself
			Class.forName(driverName).newInstance();
			
			System.out.println("Connecting to " + connectionURL);
			
			con = DriverManager.getConnection(connectionURL);
		
			stmt = con.createStatement();
			
			rs = stmt.executeQuery(sqlStatement);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int columnCount = rsmd.getColumnCount();
			
			System.out.println("");
			String line = "";
			for ( int i = 0; i < columnCount; i++) {
				if ( i > 0 ) {
					line += ", ";
				}
				line += rsmd.getColumnLabel(i + 1);
			}
			System.out.println(line);
			
			int rowCount = 0;
			
			while (rs.next()) {
				rowCount++;
				
				line = "";
			}
		} finally {
		}
	}
}
