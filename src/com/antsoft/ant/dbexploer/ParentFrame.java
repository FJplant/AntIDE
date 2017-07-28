import javax.swing.*;
import java.awt.event.*;

public class ParentFrame extends JFrame  {

	ParentFrame() {
		super( "Parent frame " );
	}

	public static void main( String[] args ) {
		ParentFrame parent = new ParentFrame();
		parent.setSize( 200, 200 );
		//parent.setVisible( true );
		
		ConnectionDialog dialog = new ConnectionDialog( parent );	
		dialog.setVisible( true );

		// ms sql
		//ConnectionInfo info = new ConnectionInfo( "com.inet.tds.TdsDriver", "jdbc:inetdae:antserver:1433?database=ant", "sa", "ant123" );

		//ExplorerManager manager = new ExplorerManager( parent, info );
	}


}
