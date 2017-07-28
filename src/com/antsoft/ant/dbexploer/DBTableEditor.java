import javax.swing.*;
import java.awt.*;
import java.awt.Component;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;

/**
 *  DBTableCellRenderer
 */
public class DBTableEditor extends JTextField implements TableCellEditor {
	/** color List */
	Vector colorList;

	/** listeners */
	protected transient Vector listeners;
	
	/** originalValue */
	protected transient String data;

	public DBTableEditor( int dbRow ) {
		super( );
		setPreferredSize( new Dimension( 80, 20 ) );
		
		colorList = new Vector( 1, 1 );
		for( int i = 0 ; i < dbRow ; i++ )
			colorList.addElement( new Boolean( false ) );

		setText( "" );
		listeners = new Vector();

	}

	/**
	 *  getTableCellEditorComponent
	 */
	public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row, int col ) { 
		if( value instanceof JTextField ) {
			data =  ((JTextField)value).getText();
			setText( data );
		} else if( value instanceof String ) {
			data =  (String)value;
			setText( data );
		}	


/*		if( ( (Boolean)colorList.elementAt( row ) ).booleanValue() ) {
			setNewColor();
			if( isSelected ) setSelectedColor();
			if( table.getSelectedColumn() == col ) setNewColor();
			setText( data );
		}
		else{
			resetColor();
			if( isSelected ) setSelectedColor();
			if( table.getSelectedColumn() == col ) resetColor();
			setText( data );
		}
*/		if( ( (Boolean)colorList.elementAt( row ) ).booleanValue() ) {
			setNewColor();
			
		}
		else{
			resetColor();
		}

		
		return this;
	}


	/**
	 *  setNewColor
	 */
	public void setNewColor() {
		//setBackground( new Color( 109, 109, 225 ) );
		setBackground( new Color( 120, 120, 180 ) );
		setForeground( Color.white );
	}


	/**
	 *  setSelectedColor
	 */
	public void setSelectedColor() {
		setBackground( new Color( 200, 200, 240 ) );
	}


	/**
	 *  resetColor
	 */
	public void resetColor() {
		setBackground( Color.white );
		setForeground( Color.black );

	}


	/**
	 *  addedNewRow
	 */
	public void addedNewRow( ) {
		colorList.addElement( new Boolean( true ) );
	}

	/**
	 *  removedRow
	 */
	public void removedRow( int row ) {
		colorList.removeElementAt( row );
	}

	/**
	 *  insertedRow
	 */
	public void insertedRow( int row ) {
		colorList.removeElementAt( row );
		colorList.insertElementAt( new Boolean( false ), row );
		//repaint();
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
		return getText();
	}

	/**
	 *  setCellEditorValue
	 */
	public void setCellEditorValue( String data ) {
		this.data = data;
		setText( data );
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
		setText( data );
		ChangeEvent ce = new ChangeEvent( this );
		for( int i = listeners.size()  ; i >= 0 ; i-- ) {
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
