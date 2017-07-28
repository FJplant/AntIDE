import java.awt.*;
import java.sql.*;
import javax.swing.*;

/**
 *  ExplorerTest
 *
 *  executeQuery�� �ϸ� Unknown Type Response��� ������ ����
 *  �Ǵ��� �����غ����� ���� Ŭ�����̴�.
 *  �ٵ� �̰Ŵ� �� �ȴ�. �� �̷���?
 *  ¯����.
 */
public class ExplorerTest {
	DBConnector connector;

	ExplorerTest() {
		try {
			//ConnectionInfo info = new ConnectionInfo( "postgresql.Driver",
			//"jdbc:postgresql://ant:5432/namecard", "anda", "anda" );

			// ms sql
			ConnectionInfo info = new ConnectionInfo( "com.inet.tds.TdsDriver", "jdbc:inetdae:antserver:1433?database=ant", "sa", "ant123" );
			connector = new DBConnector( info );
		} catch( ClassNotFoundException e ) {
			System.out.println( e.toString() );
		} catch( SQLException e ) {
			System.out.println( e.toString() );
		} catch( Exception e ) {
			System.out.println( e.toString() );
		}


	}

	public static void main( String[] args ) {
		ExplorerTest test = new ExplorerTest();
			
		try {
			ResultSet tables = test.connector.getTableList( );
			while( tables.next() )
				System.out.println( "table " + tables.getString( 3 ) );

			String query = "select * from " + tables.getString( 3 ) ;

			ResultSet result = test.connector.executeQuery( query );
			while( result.next() )
				System.out.println( "1 : " + result.getString(1) + " 2: " +
						result.getString( 2 ) );

			test.setDBProperty();
		} catch( Exception e ) {
			System.out.println( e.toString() );
		}
	}

	/**
	 *  setDBProperty
	 */
	void setDBProperty() {
		try {
			DatabaseMetaData dbmd = connector.getDBMetaData();
			boolean MultipleResultSet = dbmd.supportsMultipleResultSets();
			boolean MultipleTransactions = dbmd.supportsMultipleTransactions();
			if( MultipleResultSet ) System.out.println( "MultipleResultSet " );
			if( MultipleTransactions ) System.out.println( "MultipleTransactions" );

		} catch( SQLException e ) {
			System.out.println( e.toString() );
		} catch( Exception e ) {
			System.out.println( e.toString() );
		}
	}



}
		

