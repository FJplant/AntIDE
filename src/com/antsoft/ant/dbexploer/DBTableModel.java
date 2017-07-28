import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;


/**
 *  CreateTableModel
 */
public class DBTableModel extends DefaultTableModel {

	/**
	 *  DBTableModel
	 */
	public DBTableModel( Vector colNames, int rows ) {
		super( colNames, rows );
	}

	/**
	 *  addRow
	 */
	public void addRow( Vector rowData ) {
		super.addRow( rowData );

	}


	

}
