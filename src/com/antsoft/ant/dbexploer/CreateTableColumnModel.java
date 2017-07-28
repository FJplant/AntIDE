import javax.swing.*;
import javax.swing.table.*;
import java.util.*;


/**
 *  CreateTableColumnModel
 */
public class CreateTableColumnModel extends DefaultTableColumnModel {
	/** column width */
	int width;

	/** 
	 *  CreateTableColumnModel
	 */
	public CreateTableColumnModel() {
		super();
	}

	/**
	 *  setColumnWidth
	 */
	public void setColumnWidth( int width ) {
		this.width = width;
		totalColumnWidth = width * tableColumns.size();
	}
	

}
