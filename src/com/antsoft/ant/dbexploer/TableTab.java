import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;
import java.sql.*;


/**
 *  TableTab
 *
 *  @author : Young-Joo Kim
 *
 */
public class TableTab extends JPanel implements ActionListener, TableModelListener {
	/** parent frame */
	ExplorerFrame frame;
	/** manager */
	ExplorerManager manager;

	/** resources */
	JPanel centerP;
	JLabel tableLbl;
	DBTable table = null;
	DBTableModel tableModel;
	DBTableRenderer renderer;
	DBTableEditor editor;
	JScrollPane tableSp;
	JButton addRow;
	JButton insert;
	JButton delete;
	Vector colNames;
	Vector data;
	boolean noNull[];
	boolean numeric[];
	String currentTable = "";
	int dbRowCount;
	int currentRowCount; 


	/**
	 *  TableTabManager
	 */
	public TableTab( ExplorerFrame frame, ExplorerManager manager ) {
		super();
		this.frame = frame;
		this.manager = manager;

		setLayout( new BorderLayout() );

		tableLbl = new JLabel( "     table contents " );
		tableLbl.setPreferredSize( new Dimension( 400, 20 ) );

		tableSp = new JScrollPane();
		centerP = new JPanel();
		centerP.setLayout( new BorderLayout() );
		centerP.add( tableSp, "Center" );
		centerP.add( new JPanel(), "East" );
		centerP.add( new JPanel(), "West" );
		
		JPanel bottomP = new JPanel();
		bottomP.setLayout( new FlowLayout() );
		addRow = new JButton( "AddRow" );
		addRow.setActionCommand( "AddRow" );
		addRow.addActionListener( this );
		insert = new JButton( "Insert" );
		insert.setActionCommand( "Insert" );
		insert.addActionListener( this );
		insert.setEnabled( false );
		delete = new JButton( "Delete" );
		delete.setActionCommand( "Delete" );
		delete.addActionListener( this );
		bottomP.add( addRow );
		bottomP.add( insert );
		bottomP.add( delete );
		
		add( tableLbl, "North" );
		add( centerP, "Center" );
		add( bottomP, "South" );
		
	}

	/**
	 *  getCurrentTable
	 */
	public String getCurrentTable(   ) {
		return currentTable;
	}

	/**
	 *  setCurrentTable
	 */
	public void setCurrentTable( String tableName ) {
		this.currentTable = tableName;
	}

	/**
	 *  showTableData
	 */
	public void showTableData( ResultSet rs ) {
		try {
			// scrollPane�� �����Ѵ�.
			centerP.remove( tableSp );	
			// resultset�� metadata �� ��´�.			
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
			colNames = new Vector( 1, 1 );
			noNull = new boolean[ cols + 1 ];
			numeric = new boolean[ cols + 1 ];

			// column name�� ��´�.
			for( int i = 1 ; i <= cols ; i++ ) {
				colNames.addElement( rsmd.getColumnName( i ) );
				noNull[i-1] = ( ResultSetMetaData.columnNoNulls == rsmd.isNullable( i ) );
				numeric[i-1] = isNumeric( rsmd.getColumnType( i ) );
			}

				
			// row ���� ��� �����͸� ���Ϳ� �ִ´�.
			int rows = 0;
			data = new Vector( 1, 1 );
			while( rs.next() ) { 
				for( int i = 1; i <= cols ; i++ ) { 
					data.addElement( rs.getString( i ) );
				}
				rows++;
			}

			// db�� ����� row���� ��´�.
			dbRowCount = rows;
			currentRowCount = rows;
			//System.out.println( "dbRow : " + dbRowCount + " currentRow : " + currentRowCount );

			// table�� �����.
			tableModel = new DBTableModel( colNames , rows );
			table = new DBTable( tableModel, colNames.size() );
			renderer = new DBTableRenderer( rows );
			editor = new DBTableEditor( rows );
			table.setRowHeight( 20 );
			tableSp = new JScrollPane( table );
			tableSp.setColumnHeaderView( table.getTableHeader() );
			centerP.add( tableSp, "Center" );

			for( int i = 0 ; i < colNames.size() ; i ++ ) {
				table.getColumn( i ).setPreferredWidth( 80 ); 
				table.getColumn( i ).setCellRenderer( renderer );
				table.getColumn( i ).setCellEditor( editor );

			}

			// table�� �����͸� �ִ´�.
			for( int i = 0; i < rows ; i++ )
				for( int j = 0; j < cols ; j++ ) { 
					tableModel.setValueAt( (String)data.elementAt( cols * i + j ) , i, j );
				}

			// printcomponents
			printComponents( getGraphics() );

			// table�� ����ٴ� �޼��� ���
			if( rows == 0 ) {
				JOptionPane.showMessageDialog( this, "Table is empty!", "DB Table", JOptionPane.PLAIN_MESSAGE );
			}

		} catch( SQLException e ) {
			//System.out.println( e.toString() );
		} catch( Exception e ) {
			//System.out.println( e.toString() );
		}	
		tableModel.addTableModelListener( this );
	}


	/**
	 *  isNumeric
	 */
	boolean isNumeric( int type ) {
		if( ( type == Types.BIGINT )  || ( type == Types.DECIMAL ) ||
			( type == Types.DOUBLE )  || ( type == Types.FLOAT ) ||
			( type == Types.INTEGER ) || ( type == Types.NUMERIC ) ||
			( type == Types.REAL )    || ( type == Types.SMALLINT ) ||
			( type == Types.TINYINT ) ) 
			return true;
		else 
			return false;
	}

	/**
	 *  getInsertData
	 */
	public String getInsertData( int row) {
		String query = "insert into " + getCurrentTable() + " ( ";
		int cols = colNames.size();

		boolean insert = false;	
		for( int i = 0 ; i < cols ; i++ )
			if( !tableModel.getValueAt( row, i ).equals( "" ) ) insert = true;

		if( !insert ) return "empty";


		for( int i = 0 ; i < cols ; i++ ) {
			// NOT NULL�ε� �����Ͱ� ���� ���
			if( noNull[i]  && tableModel.getValueAt( row, i ).equals( "" ) )
				return new Integer(i).toString();

			if( !tableModel.getValueAt( row, i ).equals( "" ) ) {
				query += colNames.elementAt( i );			
				if( i != cols - 1 ) query += ", ";
			}
		}

		query += " ) values ( ";

		for( int i = 0 ; i < cols ; i++ ) {
			if( !tableModel.getValueAt( row, i ).equals( "") ) {
				// nemeric type
				if( numeric[ i ] )
					query += tableModel.getValueAt( row, i );
				// other
				else 
					try {
						query += "'" + new String( ((String)tableModel.getValueAt( row, i )).getBytes(), "8859_1" ) + "' ";
					} catch( UnsupportedEncodingException e ) {
						query += "'" + tableModel.getValueAt( row, i ) + "' ";
						//System.out.println( e.toString() );
					}

				if( i != cols -1 ) query += ", ";
			}
		}
		query += " );";
			
		return query;
	}

	/**
	 *  getDeleteData
	 */
	public String getDeleteData( int row ) {
		if( row >= dbRowCount ) return null;

		String query = "delete from " + getCurrentTable() + " where ";
		int cols = colNames.size();

		for( int i = 0 ; i < cols ; i++ ) {
			query += colNames.elementAt( i ) + "=" ;			

			// type check whether numeric is or not  
			if( numeric[ i ] ) {
				//System.out.print( i + " is numeric " );
				query += tableModel.getValueAt( row, i );
			}	// other type
			else  {
				query += "'" + tableModel.getValueAt( row, i ) + "' ";
			}
			if( i != cols - 1 ) query += "and ";
		}
		query += ";";

		return query;
	}


	/**
	 *  addRow - ���ο� ���� �ϳ� �߰��Ѵ�.
	 */
	public void addRow() {
		Vector col = new Vector( 1, 1 );
		for( int i = 0 ; i < colNames.size() ; i++ ) 
			col.addElement( "" );
		
		renderer.addedNewRow();
		editor.addedNewRow();
		tableModel.addRow( col );		

		insert.setEnabled( true );
		currentRowCount++;
	}


	


	/**
	 *  insert  -  insert button�� �������� �� ó��
	 */
	public void insert() {
		try {
			table.setEditingCell();
	/*		if( dbRowCount == 0 ) {
				table.setRowSelectionInterval( dbRowCount, dbRowCount);
				table.setEditingRow( dbRowCount );
				//table.setEditingCell();
			} else {
				table.setRowSelectionInterval( dbRowCount - 1 , dbRowCount- 1);
				table.setEditingRow( dbRowCount - 1 );
				//table.setEditingCell();
			}
	*/	} catch( Exception e ) {
			//System.out.println( e.toString() + " in insert " );
		}

		// row count to insert 
		int rows = currentRowCount - dbRowCount;
		int nextRow = dbRowCount;

		for( int i = dbRowCount; i < currentRowCount  ; i++ ) {
			//System.out.println( "currentRow : " + currentRowCount + " i : " + i );
			nextRow = i;
			// query statement �����
			String query = getInsertData( i );
			// data�� ���� ���
			if( query.startsWith( "empty" ) ) {
				JOptionPane.showMessageDialog( frame, (i+1) + "st row - Columns are empty! " , "SQL Error", JOptionPane.PLAIN_MESSAGE );
				renderer.removedRow( i );
				editor.removedRow( i );
				tableModel.removeRow( i );		
				currentRowCount--;
				rows--;

				if( ( i == currentRowCount ) && ( i != 0 ) ) nextRow--; 
				if( ( i == 0 ) && ( currentRowCount == 0 ) ) {
					addRow();
					i++;
				}
				//System.out.println( "nextRow : " + nextRow );
				table.setRowSelectionInterval( nextRow, nextRow );
				table.setEditingRow( nextRow );
				table.resetCell( nextRow, table.getEditingColumn() );	
				i--;
					
				continue;
			} // insert
			else if( query.startsWith( "insert" ) ) {	
				int result = 0;
				try {	
					result = manager.insertRow( query );
				} catch( ClassNotFoundException e ) {
					JOptionPane.showMessageDialog( frame, "Invalid JDBC Driver - fail to connect DB ", "DB Connection Error", JOptionPane.ERROR_MESSAGE );
					tableModel.removeRow( i );
					currentRowCount--;
					renderer.removedRow( i );
					editor.removedRow( i );
					rows--;
					if( ( i == currentRowCount ) && ( i != 0 ) ) nextRow--; 
					i--;
					if( currentRowCount == 0 ) {
						addRow();
						i++;
					}
					table.setRowSelectionInterval( nextRow, nextRow );
					table.setEditingRow( nextRow );
					table.resetCell( nextRow, table.getEditingColumn() );	
					
					continue;		
				} catch( SQLException e ) {
					JOptionPane.showMessageDialog( this, (i+1) + "st Row - fail to insert Row  ", "SQL Error", JOptionPane.ERROR_MESSAGE );
					tableModel.removeRow( i );
					currentRowCount--;
					renderer.removedRow( i );
					editor.removedRow( i );
					rows--;
					if( ( i == currentRowCount ) && ( i != 0 ) ) nextRow--; 
					i--;
					if( currentRowCount == 0 ) {
						addRow();
						i++;
					}		
					table.setRowSelectionInterval( nextRow, nextRow );
					table.setEditingRow( nextRow );
					table.resetCell( nextRow, table.getEditingColumn() );			
					continue;
				}	
				// when fail to insert
				if( result != 1 ) {
					renderer.removedRow( i );
					editor.removedRow( i );
					tableModel.removeRow( i );
					if( table.getRowCount()  == i ) { 
						//table.resetCell( i - 1, table.getEditingColumn() );
					} else {
						//table.setEditingCell();
					}
					currentRowCount--;
					rows--;
					if( ( i == currentRowCount ) && ( i != 0 ) ) nextRow--; 
					i--;
					if( currentRowCount == 0 ) {
						addRow();
						i++;
					}
					table.setRowSelectionInterval( nextRow, nextRow );
					table.setEditingRow( nextRow );
					table.resetCell( nextRow, table.getEditingColumn() );				
				} else {
					renderer.insertedRow( i );
					editor.insertedRow( i );		
					table.setRowSelectionInterval( i , i );
					table.setEditingRow( i );		
					dbRowCount++;

					// data vector �� �ֱ�
					for( int j = 0 ; j < colNames.size() ; j++ ) {
						//System.out.println( tableModel.getValueAt( i, j ) + " inserted " );
						data.addElement( tableModel.getValueAt( i, j ) );
					}
				}
			} // notnull �����Ͱ� ����ִ� ���
			else {
				JOptionPane.showMessageDialog( frame, (String)colNames.elementAt( Integer.parseInt( query ) ) + " is NOT NULL! " , "SQL Error", JOptionPane.PLAIN_MESSAGE );
				renderer.removedRow( i );
				editor.removedRow( i );
				tableModel.removeRow( i );
				currentRowCount--;
				if( ( i == currentRowCount ) && ( i != 0 ) ) nextRow--; 
				i--;
				table.setRowSelectionInterval( nextRow, nextRow );
				table.setEditingRow( nextRow );
				table.resetCell( nextRow, table.getEditingColumn() );					
				continue;
			}
		}
		table.repaint();
		table.getSelectedCellEditor( ).repaint();
		table.getSelectedCellRenderer().repaint();
		// button disable
		if( dbRowCount == currentRowCount )
			insert.setEnabled( false );	
	}


	/**
	 *  delete 
	 */
	public void delete() {
		try {
			table.setEditingCell();
		} catch( Exception e ) {
		}

		// select �� ���� ���ϱ�
		int selected =  table.getSelectedRowCount();
		if( selected == 0 ) return;

		// delete�� row number���
		int row = table.getSelectedRow();
		// ������ selection�� �� ��
		int nextRow = row;
	
		// table�� ������� ���� row�� ���� ���
		if( row >= dbRowCount ) {
			currentRowCount--;
			renderer.removedRow( row );
			editor.removedRow( row );
			tableModel.removeRow( row );	
			if( ( row == currentRowCount ) && ( row  != 0 ) ) nextRow = row - 1;
			if( row == 0 ) addRow();	
			table.setRowSelectionInterval( nextRow, nextRow );
			table.setEditingRow( nextRow );
			table.resetCell( nextRow, table.getEditingColumn() );
			//table.setEditingCell();
			return;
		}

		// column���� ��´�.
		int cols = table.getColumnCount();

		String delete = getDeleteData( row );
		// table�� ����ִ� �����͸� ���� ���
		int result = 0;
		try {
			result = manager.deleteRow( delete );
		} catch( ClassNotFoundException e ) { 
			JOptionPane.showMessageDialog( this, "Invalid JDBC Driver - fail to connect DB ", "DB Connection Error", JOptionPane.ERROR_MESSAGE );
			return;
		} catch( SQLException e ) {
			JOptionPane.showMessageDialog( this, "Fail to delete Selected Row  ", "SQL Error", JOptionPane.ERROR_MESSAGE );
			return;
		}
		if( result != 1 ) {
			JOptionPane.showMessageDialog( this, "Fail to delete Selected Row  ", "SQL Error", JOptionPane.ERROR_MESSAGE );
			return;
		}
		dbRowCount--; 
		currentRowCount--;
		// data vector���� ����
		for( int i = 0 ; i < colNames.size() ; i++ ) {
			data.removeElementAt( row * colNames.size() );
		}

		if( ( row == currentRowCount ) && ( row != 0 ) ) nextRow = row - 1;
		
		tableModel.removeRow( row );
		if( ( row == 0 ) && ( currentRowCount == 0 ) ) addRow();
		table.setRowSelectionInterval( nextRow, nextRow );
		table.setEditingRow( nextRow );
		//table.setEditingRow( row - 1 );
		table.resetCell( nextRow, table.getEditingColumn() );
		renderer.removedRow( row );
		editor.removedRow( row );

		//if( ( row == 0 ) && ( currentRowCount == 0 ) ) addRow();

	}


	/**
	 *  update
	 */
	void update( int row, int col ) {
		// ���� �����Ϳ� ���ؼ� ���� ���� ��� update�Ѵ�.
		if( !((String)data.elementAt( row * colNames.size() + col ) ).equals( tableModel.getValueAt( row, col ) ) ) {
			// query stmt�����
			String query = "update " + getCurrentTable() + " set ";
			query += (String)colNames.elementAt( col ) + "=" ;
			if( numeric[col] ) query += tableModel.getValueAt( row, col );
			else  {
				query += "'" + tableModel.getValueAt( row, col ) + "' ";
/*				try {
					query += "'" + new String( ((String)tableModel.getValueAt( row, col )).getBytes(), "8859_1" )  + "' ";
					query += "'" + tableModel.getValueAt( row, col ) + "' ";
				} catch( UnsupportedEncodingException e ) {
					System.out.println( e.toString() );
					query += "'" + tableModel.getValueAt( row, col ) + "' ";
				}
*/			}

			query += " where ";
			for( int i = 0 ; i < colNames.size() ; i++ ) {
				if( i != col ) {
					query += colNames.elementAt( i ) + "=" ;
					if( numeric[i] ) query += tableModel.getValueAt( row, i );
					else {
						query += "'" + tableModel.getValueAt( row, i )+ "' ";
				/*		try {
							query += "'" + new String( ((String)tableModel.getValueAt( row, i )).getBytes(), "8859_1" ) + "' ";
						} catch( UnsupportedEncodingException e ) {
							System.out.println( e.toString());
							query += "'" + tableModel.getValueAt( row, i ) + "' ";
						}
				*/	}

					if( i != colNames.size() - 1 ) query += " and ";
				}
			}

			// and �� ������ ���� ó��
			if( query.endsWith( "and " ) ) 
				query = query.substring( 0, query.length() - 4 );

			// query����
			try {
				int result = manager.updateRow( query );
				if( result != 1 ) {
					tableModel.setValueAt( data.elementAt( row * colNames.size() + col ), row, col );
				} else {
					data.removeElementAt( row * colNames.size() + col );
					data.insertElementAt( tableModel.getValueAt( row, col ), row * colNames.size() + col );
				}
			} catch( ClassNotFoundException e ) {
				JOptionPane.showMessageDialog( this, "Invalid JDBC Driver - fail to connect DB ", "DB Connection Error", JOptionPane.ERROR_MESSAGE );
				tableModel.setValueAt( data.elementAt( row * colNames.size() + col ), row, col );
				return;
			}catch( SQLException e ) {
				JOptionPane.showMessageDialog( this, "Fail to update Selected Row  ", "SQL Error", JOptionPane.ERROR_MESSAGE );
				tableModel.setValueAt( data.elementAt( row * colNames.size() + col ), row, col );
				return;
			}

		}
	}


	/** 
	 *  actionPerformed
	 */
	public void actionPerformed( ActionEvent ae ) {
		String  cmd = ae.getActionCommand();
		Object source = ae.getSource();
		
		if( getCurrentTable().equals( "" ) ) return;
		// add new empty row
		if( cmd.equals( "AddRow" ) ) {
			addRow();
		} // insert in table
		else if( cmd.equals( "Insert" )) {
			insert();
		} // table delete
		else if( cmd.equals( "Delete" ) ){
			delete();
		}
		//System.out.println( "dbRow : " + dbRowCount + " currentRow : " + currentRowCount );
	}


	/**
	 *  tableChanged
	 */
	public void tableChanged( TableModelEvent e ) {
		if( e.getType() == TableModelEvent.UPDATE ) {
			if( e.getFirstRow() < dbRowCount ) {
				//System.out.println( "original data : " + data.elementAt( e.getFirstRow() * colNames.size() + e.getColumn() ) );
				update( e.getFirstRow(), e.getColumn() );
			}
		}
		
		
	}
	
}
