/*
 *  package : test
 *  source  : JdbcTest.java
 *  date    : 1999.5.16
 */

package test;

import java.sql.*;

public class JdbcTest { 
	// driver name
	public static final String DRIVER = "";
	// url
	public static final String URL = "jdbd:odbc://210.124.152.4:1433/vnet";
	// ID
	public static final String ID = "vnet";
	// Password
	public static final String PASSWD = "vnet";

	/**
	 *   JdbcTest
	 */
	public JdbcTest() { 
	}

	/**
	 *  connectExample - JDBC연동하는 예제입니다. 
	 */
	public void connectExample() {
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;

		//DB connection
		try { 
			Class.forName( DRIVER );
			con = (Connection)DriverManager.getConnection( URL, ID, PASSWD );
			stmt = con.createStatement();
		} catch( ClassNotFoundException e) {
			// exception handling
		} catch( SQLException e ) {
			// exception handling
		}

		// query
		try { 
 			// To Do :
			String query = "select * from Table";
 			result = stmt.executeQuery( query );
			result.next();
			String data = result.getString( 1 );
			// connection close
			stmt.close();
			con.close();
		} catch( SQLException e ) {
			// exception handling
		}
	}

	/**
	 *  insert - insert query를 수행하는 method
	 */
	public void insert() {
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;

		//DB connection
		try { 
			Class.forName( DRIVER );

			con = (Connection)DriverManager.getConnection( URL, ID, PASSWD );
			stmt = con.createStatement();
		} catch( ClassNotFoundException e) {
			// exception handling
		} catch( SQLException e ) {
			// exception handling
		}

		// query
		try { 
 			// To Do :
			String query = "insert into Table ( column1, column2, column3) values ( value1, value2, value3) ";
 			stmt.executeUpdate( query );

			// connection close
			stmt.close();
			con.close();
		} catch( SQLException e ) {
			// exception handling
		}
	}

	/**
	 *  delete - delete query를 수행하는 method 
	 */
	public void delete() {
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;

		//DB connection
		try { 
			Class.forName( DRIVER );
			con = (Connection)DriverManager.getConnection( URL, ID, PASSWD );
			stmt = con.createStatement();
		} catch( ClassNotFoundException e) {
			// exception handling
		} catch( SQLException e ) {
			// exception handling
		}

		// query
		try { 
 			// To Do :
			String query = "delete Table where column='value'";
 			stmt.executeUpdate( query );

			// connection close
			stmt.close();
			con.close();
		} catch( SQLException e ) {
			// exception handling
		}
	}

	/**
	 *  update - update query를 수생하는 method
	 */
	public void update() {
		Connection con = null;
		Statement stmt = null;
		ResultSet result = null;

		//DB connection
		try { 
			Class.forName( DRIVER );
			con = (Connection)DriverManager.getConnection( URL, ID, PASSWD );
			stmt = con.createStatement();
		} catch( ClassNotFoundException e) {
			// exception handling
		} catch( SQLException e ) {
			// exception handling
		}

		// query
		try { 
 			// To Do :
			String query = "update Table set column1 ='value1' where column2 = 'value2'";
 			stmt.executeUpdate( query ); 			

			// connection close
			stmt.close();
			con.close();
		} catch( SQLException e ) {
			// exception handling
		}
	}
}

