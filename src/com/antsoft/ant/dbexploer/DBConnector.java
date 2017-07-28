import java.sql.*;
import java.util.*;
//import oracle.jdbc.driver.*;

public class DBConnector {
	/** ConnectionInfo */
	ConnectionInfo info;
	/** Connection */
	Connection con;
	/** Statement */
	Statement stmt;
	/** tablelist */
	Vector tableList;
	
	/**
	 *  DBConnector
	 *
	 *  @param  info  infomation for DB Connection
	 */
	public DBConnector( ConnectionInfo info ) throws ClassNotFoundException, SQLException  {
		this.info = info;

		//DB connection
		Class.forName( info.getDriver() );
		Connection con = (Connection)DriverManager.getConnection( info.getUrl(), info.getID() , info.getPassword() );
		con.close();

	}


	/**
	 *  getDBMetaData
	 */
	public DatabaseMetaData getDBMetaData() {
		DatabaseMetaData data = null;

		try {
			connect();

			data = con.getMetaData();
			con.close();
		} catch( ClassNotFoundException e ) {
			
		} catch( SQLException e ) {
			shutdown();
		}
	
		return data;	

	}


	/**
	 *  getTableList
	 */
	public Vector getTableList() {
		DatabaseMetaData data = null;
		ResultSet list = null;
		String[] types = { "TABLE" };

		try {
			connect();

			data = con.getMetaData();
			list = data.getTables( null, null, null, types ); 
			tableList = new Vector( 1, 1 );
			while( list.next() )
				tableList.addElement( list.getString( 3 ) );
				
			con.close();
		} catch( ClassNotFoundException e ) {
			System.out.println( e.toString() );
		} catch( SQLException e ) {
			System.out.println( e.toString() );
		}
	
		return tableList;	
	} 


	/**
	 *  getTableData
	 */
/*	public ResultSet getTableData( String table ) {
		return executeQuery( "select * from " + table + ";" );	
	}
*/

	/**
	 *  getDataTypeList
	 */
	ResultSet getDataTypeList() {
		DatabaseMetaData data = null;
		ResultSet list = null;
		try {
			connect();
			data = con.getMetaData();
			list = data.getTypeInfo();
			con.close();
		} catch( ClassNotFoundException e ) {
			System.out.println( e.toString() );
		} catch( SQLException e ) {
			System.out.println( e.toString() );
		} catch( Exception e ) {
			System.out.println( e.toString() );
		}
		return list;
	}


	/**
	 *  getPrimaryKey
	 */
	public ResultSet getPrimaryKeyList( String tableName ) throws ClassNotFoundException, SQLException {
		DatabaseMetaData data = null;
		ResultSet primaryKey = null;

		connect();
		data = con.getMetaData();
		primaryKey = data.getPrimaryKeys( "", "", tableName );
		con.close();

		return primaryKey;
	}
	

 	/**
	 *  addColumn
	 *
	 *  @param  table  table name
	 *  @param  column column name
	 */
	public int addColumn( String table, String column ) {
		int result = 0;

		//DB connection
		try {
			connect();
		} catch( ClassNotFoundException e) {
			// exception handling
		} catch( SQLException e ) {
			// exception handling
			shutdown();
		}

		try {
			String query = "alter table " + table + " add column " + column + ";";  
			result = stmt.executeUpdate( query );
			// connection close
			stmt.close();
			con.close();
			return result;
		} catch( SQLException e ) {
			// exception handling
			shutdown();
		}
		return result;
	}


	/**
	 *  deleteColumn 
	 */
	public int deleteColumn( String table, String column ) {
		int result = 0;

		//DB connection
		try {
			connect();

			String query = "alter table " + table + " add column " + column + ";";  
			result = stmt.executeUpdate( query );
			// connection close
			stmt.close();
			con.close();
		} catch( ClassNotFoundException e) {
		} catch( SQLException e ) {
			// exception handling
			shutdown();
		}
		return result;
	}


	/**
	 *  select
	 *
	 *  @param  query  query statment
	 */
	public ResultSet select( String query ) {
		ResultSet result = null;
		
		try {
			connect();
	
			result = stmt.executeQuery( query );
			stmt.close();
			con.close();
		} catch( ClassNotFoundException e ) {
		} catch( SQLException e ) {
			shutdown();
		}
		return result;
	
	}


	
	/**
	 *  executeUpdate
	 *
	 *  @param  query  query statement
	 */
	public int executeUpdate( String query ) throws ClassNotFoundException, SQLException {
		int result = 0;

		connect();
		
		result = stmt.executeUpdate( query );
		stmt.close();
		con.close();

		return result;

	}

	/**
	 *  executeQuery
	 *
	 *  @param  query  query statement
	 */
	public ResultSet executeQuery( String query )  throws ClassNotFoundException , SQLException {
		ResultSet result = null;

		connect();
		result = stmt.executeQuery( query );
		stmt.close();
		con.close();
		
		return result;

	}


	/**
	 *  dropTable
	 *
	 *  @param  table  table name
	 */
	public void dropTabel( String table ) {
		
	
	}

	/**
	 *  connect
	 */
	private void connect() throws ClassNotFoundException, SQLException  {
		Class.forName( info.getDriver() );
		con = (Connection)DriverManager.getConnection( info.getUrl(), info.getID(), info.getPassword() );
		stmt = con.createStatement();
	}

	/**
	 *  showdown
	 */
	private void shutdown() {
		try {
			if( stmt != null )  stmt.close();
			if( con != null ) con.close();
		} catch( SQLException e ) {
		}
	}
}
