import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

/**
 *  DBTableCellRenderer
 */
public class DBTableRenderer extends DefaultTableCellRenderer {
	/** color List */
	Vector colorList;

	/** data */
	String data;

	public DBTableRenderer( int dbRow ) {
		super();
		setOpaque( true );
		colorList = new Vector( 1, 1 );
		for( int i = 0 ; i < dbRow ; i++ )
			colorList.addElement( new Boolean( false ) );
		
		setRequestFocusEnabled( true );
	}

	/**
	 *  getTableCellRendererComponent
	 */
	public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col ) {
		// 새로 생긴 줄에 색깔을 준다.

		if( value instanceof JTextField ) {
			data =  ((JTextField)value).getText();
			setText( data );
		} else if( value instanceof String ) {
			data =  (String)value;
			setText( data );
		}

		if( ( (Boolean)colorList.elementAt( row ) ).booleanValue() ) {
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
		setBackground( new Color( 200, 200, 250 ) );
	}


	/**
	 *  resetColor
	 */
	public void resetColor() {
		setBackground( Color.white );
		setForeground( Color.black );

	}

	/**
	 *  setCellRendererValue
	 */
	public void setCellRendererValue( String data ) {
		this.data = data;
		setText( data );
	}


	/**
	 *  addedNewRow
	 */
	public void addedNewRow( ) {
		colorList.addElement( new Boolean( true ) );
		//System.out.println( "addedNewRow : " + colorList.size() );
	}

	/**
	 *  removedRow
	 */
	public void removedRow( int row ) {
		//System.out.println(  "removed Row : " + row );
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

}

