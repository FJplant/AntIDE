import java.sql.*;

/**
 *  DBProperty
 */
public class DBProperty {
	/** DB product name */
	private String productName;
	/** if alter table add column */
	private boolean supportsAlterAddColumn;
	/** if alter table drop column */
	private boolean supportsAlterDropColumn;

	/**
	 *  DBProperty
	 *
	 *  @param  dbmd  dbtabasemetadata 
	 */
	public DBProperty( DatabaseMetaData dbmd ) throws SQLException {
		productName = dbmd.getDatabaseProductName();
		supportsAlterAddColumn = dbmd.supportsAlterTableWithAddColumn();
		supportsAlterDropColumn = dbmd.supportsAlterTableWithDropColumn();
	}

	/**
	 *  getProductName
	 */
	public String getProductName() {
		return productName;
	}

	/** 
	 *  supportsAlterAddColumn
	 */
	public boolean supportsAlterAddColumn() {
		return supportsAlterAddColumn;
	}

	/** 
	 *  supportsAlterDropColumn
	 */
	public boolean supportsAlterDropColumn() {
		return supportsAlterDropColumn;
	}
	

}
