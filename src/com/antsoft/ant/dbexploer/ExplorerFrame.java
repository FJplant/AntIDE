import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;


/**
 *  SQLWizardFrame 
 */
public class ExplorerFrame extends JFrame implements ActionListener, WindowListener {
	/** manager */
	ExplorerManager manager;

	/** property */
	DBProperty property;

	/** TabbedPane */
	JTabbedPane wizard;

	/** table list panel */
	JPanel listP;
	JLabel listLbl ;
	JComboBox tables;
	JButton show;
	Vector tableList;
	String[] propertys;

	
	/** table tab */
	TableTab tableTab;
	String currentTable;
	
	/** query tab */
	JLabel queryLbl;
	JPanel queryP;
	JTextArea query;
	JButton execute;
	JButton again;

	/** result tab */
	JLabel resultLbl;
	JPanel resultP;
	JPanel resultContent;
	CardLayout resultCard;
	JTable result = null;
	DefaultTableModel resultModel;
	JScrollPane resultSp; 
	JPanel resultSet;
	JPanel resultRow;
	JTextArea rowField;	

	/** createTable tab */
	CreateTableTab createTableTab;	
	
	/**
	 *  SQLWizardFrame  - constructor
	 */
	public ExplorerFrame( ExplorerManager manager, String title, Vector tablelist ) {
		super( title );
		this.manager = manager;
		this.tableList = tablelist;

		init( );
	}

	/**
	 *  init 
	 */
	private void init(  ) {
		// table list panel 
		listLbl = new JLabel( "Tables" );
		listLbl.setPreferredSize( new Dimension( 50, 20 ) );

		tables = new JComboBox( );
		tables.setPreferredSize( new Dimension( 120, 20 ) );

		show = new JButton( "show" );
		show.addActionListener( manager );
		show.setActionCommand( "ShowTable" );
		show.setPreferredSize( new Dimension( 80, 20 ));

		try {
			int i = 0;
			// table list 넣기
			for(  ; i < tableList.size() ; i++ ) {
				tables.addItem( tableList.elementAt( i ) );
			}
			tables.setSelectedIndex( 0 );
			if( i == 0 ) System.out.println( " tablelist is empty " );
		} catch( Exception e ) {
			System.out.println( e.toString() );
		}
				
		wizard = new JTabbedPane( JTabbedPane.TOP );  
		
		listP = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		listP.add( listLbl );
		listP.add( tables );
		listP.add( show );

		// table tab
		tableTab = new TableTab( this, manager );
		
		// query tab
		queryP = new JPanel();
		queryP.setLayout( new BorderLayout() );
		queryLbl = new JLabel( "    write query statement that you want to do " );
		queryLbl.setPreferredSize( new Dimension( 400, 20 ) );

		query = new JTextArea( 60, 50 );
		query.setEditable( true );
		execute = new JButton( "Execute" );
		execute.setActionCommand( "Execute" );
		execute.addActionListener( manager );
		again = new JButton( "Reset" );
		again.setActionCommand( "Again" );
		again.addActionListener( this );

		JPanel executeP = new JPanel();
		executeP.setLayout( new FlowLayout() );
		executeP.add( execute );
		executeP.add( again );

		queryP.add( queryLbl, "North" );
		queryP.add( query, "Center" );
		queryP.add( executeP, "South" );		
		queryP.add( new JPanel(), "East" );
		queryP.add( new JPanel(), "West" );
		
		// result tab
		resultP = new JPanel();
		resultP.setLayout( new BorderLayout() );

		resultLbl = new JLabel( "    Result of Query Statement" );
		resultLbl.setPreferredSize( new Dimension( 400, 20 ) );	

		resultCard = new CardLayout();
		resultContent = new JPanel( );	
		resultContent.setLayout( resultCard );

		// first card - resultSet table
		resultSet = new JPanel();
		resultSet.setLayout( new BorderLayout() ); 
		resultSp = new JScrollPane();
		resultSet.add( resultSp, "Center" );
		resultSet.add( new JPanel(), "East" );
		resultSet.add( new JPanel(), "West" );
		// second card - resultRow textarea
		resultRow = new JPanel();
		resultRow.setLayout( new BorderLayout() );
		rowField = new JTextArea( 40, 30);
		resultRow.add( rowField, "Center" );
		resultRow.add( new JPanel(), "East" );
		resultRow.add( new JPanel(), "West" ); 
		resultContent.add( resultRow, "Row" );
		resultContent.add( resultSet, "Set" );
		resultCard.show( resultContent, "Row" );
		resultP.add( resultContent, "Center" );
		resultP.add( resultLbl, "North" );
		resultP.add( new JPanel(), "South" );

		
		// createTable tab
				createTableTab = new CreateTableTab( this, manager );

		// frame에 붙이기
		Container contents = getContentPane();
		wizard.addTab( "Table", tableTab );
		wizard.addTab( "Query", queryP );
		wizard.addTab( "Result", resultP );
		wizard.addTab( "CreateTable", createTableTab );

		contents.add( listP, "North" );
		contents.add( wizard, "Center" );

		addWindowListener( this );
		setSize( 500, 400 );
		setVisible( true );
		
	}

	/**
	 *  showTableData
	 */
	public void showTableData( ResultSet data ) {
		tableTab.showTableData( data );
		wizard.setSelectedComponent( tableTab );
	}

	/**
	 *  showResultSet
	 */
	void showResultSet( ResultSet rs ) {
		try {
			resultContent.remove( resultSp );
			// resultset의 metadata 을 얻는다.			
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
			Vector colNames = new Vector( 1, 1 );

			// column name을 얻는다.
			for( int i = 1 ; i <= cols ; i++ ) {
				colNames.addElement( rsmd.getColumnName( i ) );
			}
				
			// row 수를 얻고 데이터를 벡터에 넣는다.
			int rows = 0;
			Vector data = new Vector( 1, 1 );
			while( rs.next() ) { 
				for( int i = 1; i <= cols ; i++ ) { 
					data.addElement( rs.getString( i ) );
				}
				rows++;
			}

			// table을 만든다.
			resultModel = new DefaultTableModel( colNames , rows ){
  				public boolean isCellEditable(int row, int col){
                  return false;
				}};
			result = new JTable();
			result.setModel( resultModel );
			result.setRowHeight( 20 );
			resultSp = new JScrollPane( result );
			resultSet.add( resultSp, "Center" );

			// table에 데이터를 넣는다.
			for( int i = 0; i < rows ; i++ )
				for( int j = 0; j < cols ; j++ ) { 
					resultModel.setValueAt( (String)data.elementAt( cols * i + j ) , i, j );
				}
			resultCard.show( resultContent, "Set" );

			resultP.printComponents( resultP.getGraphics() );
			wizard.setSelectedComponent( resultP );

			// table이 비었다는 메세지 출력
			if( rows == 0 ) {
				JOptionPane.showMessageDialog( this, "Table is empty!", "ResultSet Table", JOptionPane.PLAIN_MESSAGE );
			}


		} catch( SQLException e ) {
			System.out.println( e.toString() );
		} catch( Exception e ) {
			System.out.println( e.toString() );
		}	
	}


	/**
	 *  showInsertedResult
	 */
	void showInsertedResult( int rows ) {
		rowField.setText( "query result ( " + rows + " rows  inserted )" );
		resultCard.show( resultContent, "Row" );
		wizard.setSelectedComponent( resultP );	
	}

	/**
	 *  showDeletedResult
	 */
	void showDeletedResult( int rows ) {
		rowField.setText( "query result ( " + rows + " rows  deleted )" );
		resultCard.show( resultContent, "Row" );
		wizard.setSelectedComponent( resultP );	
	}

	/**
	 *  showUpdatedResult
	 */
	void showUpdatedResult( int rows ) {
		rowField.setText( "query result ( " + rows + " rows  updated )" );
		resultCard.show( resultContent, "Row" );
		wizard.setSelectedComponent( resultP );	
	}

	/**
	 *  showCreatedTable
	 */
	void showCreatedTable( String table ) {
		rowField.setText( "created table : " + table  );
		wizard.setSelectedComponent( resultP );	
	}
		
	/**
	 *  showDropedTable
	 */
	void showDropedTable( String table ) {
		rowField.setText( "droped table : " + table  );
		wizard.setSelectedComponent( resultP );	
	}
		
	/**
	 *  getSelectedTable
	 */
	public String getSelectedTable() {
		currentTable = (String)tables.getSelectedItem();
		tableTab.setCurrentTable( currentTable );
		return currentTable;
	}


	/**
	 *  addTableList
	 */
	public void addTableList( String table ) {
		tables.addItem( table );
	}

	/** 
	 *  removeTableList
	 */
	public void removeTableList( String table ) {
		tables.removeItem( table );
	}

	/**
	 *  getTableListObject
	 */
	public void setProperty( DBProperty property) {
		this.property = property;
	}


	/**
	 *  getQueryStatement
	 */
	public String getQueryStatement() {
		return query.getText();
	}

	/**
	 *  setQueryStatment
	 */
	public void setQueryStatement( String stmt ) {
		query.setText( stmt );
		wizard.setSelectedComponent( queryP );
		
	}

	

	/**
	 *  getCurrentTable
	 */
	public String getCurrentTable() {
		return currentTable;
	}


	/** 
	 *  actionPerformed
	 */
	public void actionPerformed( ActionEvent ae ) {
		String  cmd = ae.getActionCommand();
		Object source = ae.getSource();
		
				if( cmd.equals( "Again" ) ) {
			query.setText( "" );
		} // create table command
	}

	public void windowActivated( WindowEvent e ) {
	}

	public void windowClosed( WindowEvent e ) {
		System.exit( 0 );
	}
	
	public void windowClosing( WindowEvent e ) {
	}

	public void windowDeactivated( WindowEvent e ) {
	}

	public void windowDeiconified( WindowEvent e ) {
	}

	public void windowIconified( WindowEvent e ) {
	}

	public void windowOpened( WindowEvent e ) {
	}
 
}
