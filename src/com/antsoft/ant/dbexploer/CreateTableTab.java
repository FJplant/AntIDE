import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.sql.*;


/**
 *  CreateTableTab
 *
 *  @author : Young-Joo Kim
 *
 */
public class CreateTableTab extends JPanel implements ActionListener {
	/** parent frame */
	ExplorerFrame frame;
	/** manager */
	ExplorerManager manager;

	/** resources */
	JLabel createLbl;
	JTextField createTableName;
	JButton add;
	JButton remove;
	JButton create;
	CreateTableHeader createHeader;
	JTable createTable = null;
	CreateTableModel createModel;
	CreateTableColumnModel createColumn;
	JScrollPane createSp;

	/**
	 *  CreateTableTab
	 */
	public CreateTableTab( ExplorerFrame frame, ExplorerManager manager ) {
		super();
		this.frame = frame;
		this.manager = manager;

		setLayout( new BorderLayout() );
		createLbl = new JLabel( "     create new table :" );
		createLbl.setPreferredSize( new Dimension( 120, 20 ) );
		createTableName = new JTextField( 10 );
		createTableName.setPreferredSize( new Dimension( 200, 20 ) );
		JPanel tablenameP = new JPanel();
		tablenameP.setLayout( new FlowLayout( FlowLayout.LEFT ) );
		tablenameP.add( createLbl );
		tablenameP.add( createTableName );

		// set table
		createModel = new CreateTableModel( );
		createColumn = new CreateTableColumnModel();
		createTable = new JTable( createModel, createColumn );
		createTable.setDefaultRenderer( DataTypeList.class, new DataTypeRenderer( manager.getDataTypeList() ) );
		createTable.setDefaultEditor( DataTypeList.class, new DataTypeEditor( manager.getDataTypeList()) );
		// size Á¤ÇÏ±â
		//createTable.getColumn( "Name" ).setPreferredWidth( 120 );
		//createTable.getColumn( "Type" ).setPreferredWidth( 120 );

		createTable.setRowHeight( 20 );
		createTable.createDefaultColumnsFromModel();
		createTable.setMaximumSize( new Dimension( 800, 500 ) );
		createTable.setBackground( Color.lightGray );

		JViewport jv = new JViewport();
		jv.setView( createTable );
		jv.setPreferredSize( createTable.getMaximumSize() );

		createTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
		createSp = new JScrollPane( createTable  );
		createSp.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS );
		createSp.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

		JPanel buttonP = new JPanel();
		buttonP.setLayout( new FlowLayout() );
		add = new JButton( "Add" );
		add.setActionCommand( "Add" );
		add.addActionListener( this );
		remove = new JButton( "Remove" );
		remove.setActionCommand( "Remove" );
		remove.addActionListener( this );
		create = new JButton( "Create" );
		create.setActionCommand( "Create" );
		create.addActionListener( this );
		buttonP.add( add );
		buttonP.add( remove );
		buttonP.add( create );

		JPanel spP = new JPanel();
		spP.setLayout( new BorderLayout() );
		spP.add( createSp, "Center" );
		spP.add( new JPanel(), "East" );
		spP.add( new JPanel(), "West" );

		add( tablenameP, "North" );
		add( spP, "Center" );
		add( buttonP, "South" );
			
	}


	/**
	 *  createTable
	 */
	void createTable() {
		String tableName = getCreateTableName();

		if( tableName.equals( "" ) ) {
			JOptionPane.showMessageDialog( frame, "Table Name is empty! "  , "Create Table", JOptionPane.ERROR_MESSAGE );
			return;
		}

		boolean empty = false;
		for( int i = 0 ; i < createModel.getRowCount() ; i++ )
			if( createModel.getValueAt( i, 0 ).equals( "" ) ) empty = true;

		if( empty ) {
			JOptionPane.showMessageDialog( frame, "Table Column Info is empty! "  , "Create Table", JOptionPane.ERROR_MESSAGE );
			return;
		}

		int cols = createModel.getColumnCount();
		int rows = createModel.getRowCount();
		Vector primaryKey = new Vector( 1, 1 );
		Vector unique = new Vector( 1, 1 );

		//System.out.println( "CreateTable name : " + tableName );
		//System.out.println( "CreateTable col count : " + cols );
		//System.out.println( "CreateTable row count : " + rows );

/*		for( int i = 0 ; i < rows ; i++ )
			for( int j = 0 ; j < cols ; j++ )
				System.out.println( "( " + i + ", " + j + " ) " + createModel.getValueAt( i, j ) );
*/				
		String	query = "CREATE TABLE " + tableName + " ( "; 
		for( int i = 0 ;  i < rows  ; i++ ) {
			if( !createModel.getValueAt( i, 0 ).equals( "" ) ) {	
				// column name
				query += "\n\t" + createModel.getValueAt( i, 0 );
				// column type
				query += " " + createModel.getValueAt( i, 1 );
				// column type length
				if( !createModel.getValueAt( i, 2 ).equals( "" ) )query += "(" + createModel.getValueAt( i, 2 ) + ") ";
				// default value
				if( !createModel.getValueAt( i, 3 ).equals( "" ) ) {
					query += "  DEFAULT " ;
					try {
						query += Integer.parseInt( (String)createModel.getValueAt( i, 3 ) );
					} catch( NumberFormatException e ) {
						query += "'" + createModel.getValueAt( i, 3 ) + "' ";
					}
				}

				// null check
				if( createModel.getValueAt( i, 4 ).equals( new Boolean( false )) ) query += "  NOT NULL ";
				// primary key
				if( createModel.getValueAt( i, 5 ).equals( new Boolean( true ) ) ) primaryKey.addElement( createModel.getValueAt( i, 0 ) ); 
				// unique
				if( createModel.getValueAt( i, 6 ).equals( new Boolean( true ) ) ) unique.addElement( createModel.getValueAt( i, 0 ) ); 
				
				query += ", ";
			} 
		}	
		// primary key list
		if( primaryKey.size() != 0 ) query += "\n\tPRIMARY KEY( ";
		for( int p = 0; p < primaryKey.size() ; p++ ) {
			query += primaryKey.elementAt( p ); 
			if( p != primaryKey.size() - 1 ) query += ", ";
		}

		if( primaryKey.size() != 0 ) query += " )";

		// unique
		if( unique.size() != 0 ) {
			if( primaryKey.size() != 0 ) query += ", ";
			query += "\n\tUNIQUE ( ";
		}

		for( int u = 0 ; u < unique.size(); u++ ) {
			query += unique.elementAt( u );
			if( u != unique.size() - 1 ) query += ", ";
		}
		if( unique.size() != 0 ) query += " ) ";

		//if( query.endsWith( ", " ) ) query = query.substring( 0, query.length() - 2 ); 
		query += " );";

		frame.setQueryStatement( query );
		JOptionPane.showMessageDialog( frame, "Check query statement again!"  , "Create Table", JOptionPane.PLAIN_MESSAGE );	

	}


	/**
	 *  getCreateTableModel
	 */
	public CreateTableModel getCreateTableModel() {
		return createModel;
	}


	/**
	 *  getCreateTableName
	 */
	public String getCreateTableName() {
		return createTableName.getText();
	}


	/**
	 *  resetCreateTable
	 */
	public void resetCreateTable( int row ) {
		createModel.setValueAt( "", row, 0 );	
		createModel.setValueAt( "", row, 1 );	
		createModel.setValueAt( "", row, 2 );	
		createModel.setValueAt( "", row, 3 );	
		createModel.setValueAt( new Boolean( false ), row, 4 );	
		createModel.setValueAt( new Boolean( false ), row, 5 );	
		createModel.setValueAt( new Boolean( false ), row, 6 );	
		createModel.setValueAt( new Boolean( false ), row, 7 );	
	}	

	/**
	 *  removeRow
	 */
	void removeRow() {
		if( createTable.getRowCount() > 1 ) { 
			int selected =  createTable.getSelectedRowCount();
			int cols = 8;

			if( selected == 0 ) {
				createModel.resetRow( createTable.getRowCount() - 1 );
				createModel.removeRow( createTable.getRowCount() - 1 );
			} else if ( selected == 1 ) {
				int rows[] = createTable.getSelectedRows();
				createModel.resetRow( rows[0] );
				
				createModel.removeRow( rows[0] );	
				if( createTable.getRowCount() == rows[0]  ) { 
					createTable.setEditingRow( rows[0] - 1 );
					createTable.setEditingColumn( 0 );
				}	
				
			} else { 
				int rows[] = createTable.getSelectedRows();
				boolean delete = false;
				if(  createTable.getRowCount() > rows.length ) delete = true;

				for( int i = 0 ; i < rows.length ; i++ ) { 
					if( !delete  && ( i == ( rows.length - 1 ) ) ) {
						createModel.resetRow( 0 );
							
					} else {
						createModel.resetRow( rows[i] - 1  );
						createModel.removeRow( rows[i] - i );
					}
				}
			}
		} else if( createTable.getRowCount() == 1 ) {
			createModel.resetRow( 0 );
		}
	}


	/**
	 *  actionPerformed
	 */
	public void actionPerformed( ActionEvent ae ) {
		String  cmd = ae.getActionCommand();
		
		// create table row addition
		if( cmd.equals( "Add" ) ) {
			createModel.addRow( );

		} // create table row remove
		else if( cmd.equals( "Remove" ) ) {
			removeRow();	

		} // create table command
		else if( cmd.equals( "Create" ) ) {
			createTable();	
		}
	}




}
