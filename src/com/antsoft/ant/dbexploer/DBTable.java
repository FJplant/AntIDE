import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;

/**
 *  DBTable
 */
public class DBTable extends JTable {
	/**  column size */
	int columns;
	/**  table model */
	DBTableModel model;

	/**
	 *  DBTable
	 */
	public DBTable( DBTableModel model, int columns ) {
		super( model );
		this.columns = columns;
		this.model = model;
		setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

	}

	/**
	 *  getColumn
	 */
	public TableColumn getColumn( int column ) {
		return getColumnModel().getColumn( column );
	}


	/**
	 *  getSelectedCellEditor
	 */
	public DBTableEditor getSelectedCellEditor( ) {
		return getCellEditor( getSelectedColumn() );
	}


	/**
	 *  getCellEditor
	 */
	public DBTableEditor getCellEditor( int column ) {
		return ((DBTableEditor)getColumn( column ).getCellEditor() );
	}

	
	/**
	 *  getCellRenderer
	 */
	public DBTableRenderer getCellRenderer( int column ) {
		return ((DBTableRenderer)getColumn( column ).getCellRenderer() );
	}


	/**
	 *  getCellSelectedRenderer
	 */
	public DBTableRenderer getSelectedCellRenderer(  ) {
		return getCellRenderer( getSelectedColumn() );
	}
	/**
	 *  resetRow
	 */
	void resetRow( int row ) {
		for( int i = 0 ; i < columns ; i++ ) {
			getCellEditor( i ).setCellEditorValue( "" );
			getCellRenderer( i ).setCellRendererValue( "" );		
		}
	}	

	/** 
	 *  resetCell
	 */
	void resetCell( int row, int col ) {
		getCellEditor( col ).setCellEditorValue( (String)model.getValueAt( row, col ) );
		getCellRenderer( col ).setCellRendererValue( (String)model.getValueAt( row, col ) );	
	}


	/**
	 *  setEditingCell
	 */
	public void setEditingCell() {
		model.setValueAt( getCellEditor( getEditingRow() , getEditingColumn()  ).getCellEditorValue(), getEditingRow() , getEditingColumn() );
		model.setValueAt( getCellEditor( getSelectedRow() , getSelectedColumn()).getCellEditorValue(), getSelectedRow() , getSelectedColumn() );

	}


}
