import javax.swing.*;
import javax.swing.table.*;

public class CreateTableHeader extends JTableHeader {
	/** column model */
	CreateTableColumnModel columnModel;

	/**
	 *  CreateTableHeader
	 */
	public CreateTableHeader( CreateTableColumnModel columnModel, JTable createTable ) {
		super( columnModel );
		
		setTable( createTable );
		setReorderingAllowed( false );
		setColumnSize();
	}


	/**
	 *  setColumnSize
	 */
	public void setColumnSize( ) {
		int columns = columnModel.getColumnCount();

		columnModel.getColumn( 0 ).setPreferredWidth( 100 );
		columnModel.getColumn( 1 ).setPreferredWidth( 120 );

		for( int i = 2 ; i < columns ; i++ )
			columnModel.getColumn( i ).setPreferredWidth( 70 );

	}
}

