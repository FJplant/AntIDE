import javax.swing.*;
import javax.swing.table.*;


/**
 *  CreateTableModel
 */
public class CreateTableModel extends DefaultTableModel {
	/** heaer name */
	static String headers[] = { "Name", "Type", "Length", "Default", "Nulls", "Primary Key", "Unique" };

	/** row data */
	Object rowData[] = { "", new DataTypeList(), "", "", new Boolean( false ), new Boolean( false ), new Boolean( false ) };

	/** class type */
	static Class columnClasses[] = { String.class, DataTypeList.class, String.class, String.class, Boolean.class, Boolean.class, Boolean.class };

	/** column count */
	int column = 1;

	/**
	 *  CreateTableModel
	 */
	public CreateTableModel() {
		super( headers, 0 );
		addRow(  );

	}


	/**
	 *  getColumnCount
	 */
	public int getColumnCount() {
		return headers.length; 
	}

	/**
	 *  getColumnClass
	 */
	public Class getColumnClass( int index ) {
		return columnClasses[index];
	}

	/**
	 *  getColumnName
	 */
	public String getColumnName( int index ) {
		return headers[index];
	}

	/**
	 *  getColumnIndex 
	 */
	public int getColumnIndex( Object identifiers ) {
		String title = (String)identifiers;

		for( int i = 0 ; i < headers.length ; i++ ) 
			if( headers[i].equals( title ) ) return i;

		return -1;
	}


	/**
	 *  isCellEditable
	 */
	public boolean isCellEditable( int row, int col ) {
		return true;
	}

	/**
	 *  addRow
	 */
	public void addRow() {
		super.addRow( rowData );
	}

	/**
	 *  resetRow
	 */
	public void resetRow( int row ) {
		setValueAt( "", row, 0 );	
		setValueAt( "", row, 1 );	
		setValueAt( "", row, 2 );	
		setValueAt( "", row, 3 );	
		setValueAt( new Boolean( false ), row, 4 );	
		setValueAt( new Boolean( false ), row, 5 );	
		setValueAt( new Boolean( false ), row, 6 );	
	}


}

