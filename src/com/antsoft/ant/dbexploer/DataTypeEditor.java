import java.awt.Component;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class DataTypeEditor extends JComboBox implements TableCellEditor {
	/** listeners */
	protected transient Vector listeners;

	/** typelist */
	Vector typelist;

	/** originalValue */
	protected transient String type;

	/** 
	 *  DataTypeEditor
	 */
	public DataTypeEditor( Vector typelist ) {
		super( );
		setPreferredSize( new Dimension( 120, 100 ) );
		this.typelist = typelist;

		for( int i = 0 ; i < typelist.size() ; i++ )
			addItem( typelist.elementAt( i ) );	

		setSelectedIndex( 0 );
		listeners = new Vector();
	}

	/**
	 *  getTableCellEditorComponent
	 */
	public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row, int col ) {
		if( value == null ) {
			return this;
		} 
		if( value instanceof DataTypeList ) {
			setSelectedItem( ( (DataTypeList)value).getItem() );
		}
		else {
			setSelectedItem( value );
		}

		table.setRowSelectionInterval( row, row );
		table.setColumnSelectionInterval( col, col );
		type = (String)getSelectedItem();
		return this;
	}

	/**
	 *  cancelCellEditing
	 */
	public void cancelCellEditing() {
		fireEditingCanceled();
	}

	/**
	 * getCellEditorValue
	 */
	public Object getCellEditorValue() {
		return getSelectedItem();
	}

	/**
	 *  isCellEditable
	 */
	public boolean isCellEditable( EventObject eo ) {
		return true;
	}

	/**
	 *  shouldSelectCell
	 */
	public boolean shouldSelectCell( EventObject eo ) {
		return true;
	}

	/**
	 *  stopCellEditing
	 */
	public boolean stopCellEditing() {
		fireEditingStopped();
		return true;
	}

	/**
	 *  addCellEditorListener
	 */
	public void addCellEditorListener( CellEditorListener e ) {
		listeners.addElement( e );
	}

	/**
	 *  removeCellEditorListener
	 */
	public void removeCellEditorListener( CellEditorListener cel ) {
		listeners.removeElement( cel );
	}

	/**
	 *  fireEditingCanceled
	 */
	protected void fireEditingCanceled() {
		setSelectedItem( type );
		ChangeEvent ce = new ChangeEvent( this );
		for( int i = listeners.size() ; i >= 0 ; i-- ) {
			( ( CellEditorListener)listeners.elementAt( i ) ).editingCanceled( ce );
		}
	}

	/**
	 *  fireEditingStopped
	 */
	protected void fireEditingStopped() {
		ChangeEvent ce = new ChangeEvent( this );
		for( int i = listeners.size() - 1; i >= 0 ; i-- ) {
			( ( CellEditorListener)listeners.elementAt( i ) ).editingStopped( ce );
		}
	}
}

	
