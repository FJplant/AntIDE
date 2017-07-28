import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.sql.*;

public class ExplorerManager implements ActionListener, TableModelListener {
	/** parent frame */
	JFrame parent; 
	/** ConnectionInfo */
	ConnectionInfo info;
	/** Frame */
	ExplorerFrame frame;
	/** Conntor */
	DBConnector connector;
	/** TableList */
	Vector tableList;
	/** db property */
	DBProperty property;


	/**
	 *  ExplorerManager
	 */
	public ExplorerManager( JFrame parent, ConnectionInfo info  ) throws ClassNotFoundException, SQLException {
		this.parent = parent;
		this.info = info;

		connector = new DBConnector( info );
		tableList = connector.getTableList();
		frame = new ExplorerFrame( this, "DBExplorer", tableList );
		setDBProperty();
		
	}


	/**
	 *  setDBProperty
	 */
	void setDBProperty() {
		try {
			DatabaseMetaData dbmd = connector.getDBMetaData();
			this.property = new DBProperty( dbmd );
		} catch( SQLException e ) {
			JOptionPane.showMessageDialog( parent, "Fail to get db property data - " + e.toString(), "DB Error", JOptionPane.ERROR_MESSAGE );
		} catch( Exception e ) {
			System.out.println( e.toString() );
		}
	}


	/**
	 *  getDataTypeList
	 */
	Vector getDataTypeList() {
		ResultSet list = connector.getDataTypeList();
		Vector typelist = new Vector( 1, 1 );

		try {
			while( list.next() ) {
				typelist.addElement( list.getString( 1 ) );
			}
		} catch( SQLException e ) {
		} catch( Exception e ) {
			//System.out.println( e.toString() );
		}

		return typelist;
	}

	/**
	 *  getTableData
	 */
	public ResultSet getTableData( String table ) throws ClassNotFoundException , SQLException{
		String query = "select * from " + table + ";" ;

		if( property.getProductName().equals( "Microsoft SQL Server" ) )
			if( query.endsWith( ";" ) ) {
				int index = query.indexOf( ";" );
				query = query.substring( 0, index);
			}

		return 	connector.executeQuery( query );
	}


	/**
	 *  getPrimaryKey
	 */
	public Vector getPrimaryKeyList() {
		Vector primaryKey = new Vector( 1, 1 );

		try {
			ResultSet list = connector.getPrimaryKeyList( frame.getCurrentTable() );

			while( list.next() ) {
				primaryKey.addElement( list.getString( 1 ) );
			}


		} catch( ClassNotFoundException e ) {
			JOptionPane.showMessageDialog( frame, "Invalid JDBC Driver - fail to connect DB ", "DB Connection Error", JOptionPane.ERROR_MESSAGE );
		} catch( SQLException e ) {
			JOptionPane.showMessageDialog( frame, "Fail to get Primary Key ", "SQL Error", JOptionPane.ERROR_MESSAGE );
		} catch( Exception e ) {
			//System.out.println( e.toString() );
		}
		return primaryKey;
	}


	/**
	 *  executeQuery
	 *
	 *  @param  query  query statement
	 */
	void executeQuery( String query )  throws SQLException{
		try {
			ResultSet result = connector.executeQuery( query );
			frame.showResultSet( result );	
		} catch( ClassNotFoundException e ) {
			JOptionPane.showMessageDialog( frame, "Invalid JDBC Driver - fail to connect DB ", "DB Connection Error", JOptionPane.ERROR_MESSAGE );
		} 
	}


	/**
	 *  executeUpdate
	 *
	 *  @param  query  query statement
	 */
	int executeUpdate( String query ) throws SQLException  {
		int result = 0;
		try {
			result = connector.executeUpdate( query );
		} catch( ClassNotFoundException e ) {
			JOptionPane.showMessageDialog( frame, "Invalid JDBC Driver - fail to connect DB ", "DB Connection Error", JOptionPane.ERROR_MESSAGE );
		}
		return result;
	}

	/**
	 *  alterTable
	 */
	void alterTable( String query ) throws SQLException { 
		try {
			connector.executeUpdate( query );

			// table 이름 얻기
			StringTokenizer st = new StringTokenizer( query );
			String table = "";
			if( st.nextToken().toLowerCase().equals( "alter" ) && st.nextToken().toLowerCase().equals( "table" ) )
				table = st.nextToken();
	
			// 바뀐 테이블 보여주기
			frame.showTableData( getTableData( table ) );
		} catch( ClassNotFoundException e ) {
			JOptionPane.showMessageDialog( frame, "Invalid JDBC Driver - fail to connect DB ", "DB Connection Error", JOptionPane.ERROR_MESSAGE );
		}
	}
		
	/**
	 *  createTable
	 */
	public void createTable( String query ) throws SQLException {
		try {
			connector.executeUpdate( query );

			// table 이름 얻기
			StringTokenizer st = new StringTokenizer( query );
			String table = "";
			if( st.nextToken().toLowerCase().equals( "create" ) && st.nextToken().toLowerCase().equals( "table" ) )
	 			table = st.nextToken();

			frame.addTableList( table );	
			frame.showCreatedTable( table );		
		} catch( ClassNotFoundException e ) {
			JOptionPane.showMessageDialog( frame, "Invalid JDBC Driver - fail to connect DB ", "DB Connection Error", JOptionPane.ERROR_MESSAGE );
		}
	}

	/**
	 *  dropTable
	 */
	public void dropTable( String query ) throws SQLException {
		try {
			connector.executeUpdate( query );

			// table 이름 얻기
			StringTokenizer st = new StringTokenizer( query );
			String table = "";
			if( st.nextToken().toLowerCase().equals( "drop" ) && st.nextToken().toLowerCase().equals( "table" ) )
				table = st.nextToken();

			frame.removeTableList( table );	
			frame.showDropedTable( table );				
		} catch( ClassNotFoundException e ) {
			JOptionPane.showMessageDialog( frame, "Invalid JDBC Driver - fail to connect DB ", "DB Connection Error", JOptionPane.ERROR_MESSAGE );
		}
	}


	/**
	 *  delete Row
	 */
	public int deleteRow( String query ) throws ClassNotFoundException , SQLException {
		if( property.getProductName().equals( "Microsoft SQL Server" ) )
			if( query.endsWith( ";" ) ) {
				int index = query.indexOf( ";" );
				query = query.substring( 0, index);
		}
		//System.out.println( "delete statement : " + query );
		return connector.executeUpdate( query );

	}

	/**
	 *  insertRow 
	 */
	public int insertRow( String query ) throws ClassNotFoundException , SQLException {
		if( property.getProductName().equals( "Microsoft SQL Server" ) )
			if( query.endsWith( ";" ) ) {
				int index = query.indexOf( ";" );
				query = query.substring( 0, index);
		}
		//System.out.println( "insert statement : " + query );
		return connector.executeUpdate( query );

	}


	/** 
	 *  updateRow
	 */
	public int updateRow( String query ) throws ClassNotFoundException, SQLException {
		if( property.getProductName().equals( "Microsoft SQL Server" ) )
			if( query.endsWith( ";" ) ) {
				int index = query.indexOf( ";" );
				query = query.substring( 0, index);
		}
		//System.out.println( "update statement : " + query );
		return connector.executeUpdate( query );

	}


			/*
	 *  actionPerformed
	 */
	public void actionPerformed( ActionEvent ae ) {
		String cmd = ae.getActionCommand();

		// execute query command
		if ( cmd.equals( "Execute" ) ) {
			String query = frame.getQueryStatement();
			if( query.equals( "" ) ) {
				JOptionPane.showMessageDialog( frame, "Query Statement is empty!", "Execute Query", JOptionPane.PLAIN_MESSAGE );
				return;
			}
	
			String kind = "";
			try {
				if( property.getProductName().equals( "Microsoft SQL Server" ) )
					if( query.endsWith( ";" ) ) {
						int index = query.indexOf( ";" );
						query = query.substring( 0, index);
					}
	
				// select 
				if( query.toLowerCase().startsWith( "select" ) )  {
					kind = " SELECT ";			
					executeQuery( query );
				} // insert
				else if( query.toLowerCase().startsWith( "insert" ) ) {
					kind = " INSERT ";
					frame.showInsertedResult( executeUpdate( query ) );
				} // delete 
				else if( query.toLowerCase().startsWith( "delete" ) ) {
					kind = " DELETE ";
					frame.showDeletedResult( executeUpdate( query ) );
				} // update 
				else if( query.toLowerCase().startsWith( "update" ) ) {
					kind = " UPDATE ";
					frame.showUpdatedResult( executeUpdate( query ) );
				} // alter
				else if( query.toLowerCase().startsWith( "alter" ) )  {
					// table 이름 얻기
					StringTokenizer st = new StringTokenizer( query );
					String table = "";			
					if( st.nextToken().toLowerCase().equals( "alter" ) && st.nextToken().toLowerCase().equals( "table" ) )
					table = st.nextToken();		
					kind = st.nextToken(); 
					System.out.println( "alter kind : " + kind );

					//if( property.supportsAlterAddColumn() ) System.out.println( "supports addcolumn " );
					//if( property.supportsAlterDropColumn() ) System.out.println( "supports drop column" );

					if( kind.toLowerCase().startsWith( "add" ) && !property.supportsAlterAddColumn() ) {
						JOptionPane.showMessageDialog( frame, property.getProductName() + " not supports alter table with add column", "SQL Error", JOptionPane.ERROR_MESSAGE );
						return;
					}

					if( kind.toLowerCase().startsWith( "drop" ) && !property.supportsAlterDropColumn() ) {
						JOptionPane.showMessageDialog( frame, property.getProductName() + " not supports alter table with drop column", "SQL Error", JOptionPane.ERROR_MESSAGE );
						return;
					}


					kind = " ALTER ";
					alterTable( query );		

				} // drop 
				else if( query.toLowerCase().startsWith( "drop" ) ) {
					kind = " DROP ";
					dropTable( query );
					
				} // create table
				else if( query.toLowerCase().startsWith( "create" ) ) {
					kind = " CREATE TABLE ";
					createTable( query );
	
				}
			} catch( SQLException e ) {
					JOptionPane.showMessageDialog( frame, "Invalid query statement - " + kind  , "SQL Error", JOptionPane.ERROR_MESSAGE );
			}

		} // show table data
		else if( cmd.equals( "ShowTable" ) ) {
			try {
				ResultSet result = getTableData( frame.getSelectedTable() );	
				frame.showTableData( result );
			} catch( ClassNotFoundException e ) {
					JOptionPane.showMessageDialog( frame, "Invalid JDBC Driver - fail to connect DB ", "DB Connection Error", JOptionPane.ERROR_MESSAGE );
			} catch( SQLException e ) {
			JOptionPane.showMessageDialog( frame, "Fail to get table data"   , "SQL Error", JOptionPane.ERROR_MESSAGE );
			}
		} 

	}


	/**
	 *  tableChanged
	 */
	public void tableChanged( TableModelEvent e ) {

	}
}

