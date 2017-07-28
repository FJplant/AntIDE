import javax.swing.*;

public class DataTypeList  {
	private String item;

	public DataTypeList( ) {
		
	}

	public void setItem( String item ) {
		this.item = item;
	}

	public void setItem( Object o ) {
		if( o instanceof String ) {
			setItem( (String)o );
		} else if( o instanceof DataTypeList ) {
			setItem( ( (DataTypeList)o).getItem() );
		}
	}

	public String getItem() {
		return item;
	}

}
