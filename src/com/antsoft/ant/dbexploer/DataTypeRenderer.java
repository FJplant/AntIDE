import java.awt.*;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;


/**
 *  DataTypeRenderer
 */
public class DataTypeRenderer extends JComboBox implements TableCellRenderer {
	/** data type list */
	Vector typelist;

	/** 
	 *  DataTypeRenderer
	 */
	public DataTypeRenderer( Vector typelist ) {
		super( );
		setPreferredSize( new Dimension( 120, 100 ) );
		this.typelist = typelist;

		for( int i = 0 ; i < typelist.size() ; i++ )
			addItem( typelist.elementAt( i ) );	
		
		setSelectedIndex( 0 );
	}

	/**
	 *  getTableCellRendererComponent
	 */
	public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col ) {
		if( value == null ) {
			return this;
		} 
		if( value instanceof DataTypeList ) {
			setSelectedItem( ( (DataTypeList)value).getItem() );
		}
		else {
			setSelectedItem( value );
		}
		return this;
	}
}

	
