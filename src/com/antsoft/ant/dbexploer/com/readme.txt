MS SQL JDBC Driver Version 1.17
Last Modified:  04-May-1999


Table Of Contents
-----------------
	1    Changes To Previous Versions
	1.1  Changes To Version 1.11
	1.2  Changes To Version 1.12
	1.3  Changes To Version 1.13
	1.4  Changes To Version 1.14
	1.5  Changes To Version 1.15
	1.6  Changes To Version 1.16
	2    Installation
	3    Getting Started
	3.1  SQL Server, Java and JDBC Versions
	3.2  Check Host Name And Port Number Of Your Server
	3.3  Driver Name
	3.4  URL Syntax
	3.5  Implemented Properties
	3.6  Connection Example
	4.   Named Pipes
	5.   Escape Clauses
	5.1  Date and Time
	5.2  Stored Procedures
	5.3  Functions
	6.   Character Converting 
	7.   New Datatypes with SQL Server 7.0
	8.   Copyright and Support



1   Changes To Previous Versions
--------------------------------


1.1 Changes To Version 1.11
----------------------------
	- The url option "debug=true" was removed. You can use
	  DriverManager.setLogStream(System.out);
	- A bug with large values in numeric columns was fixed (invalid values).
	- The new bug with getDouble (only release 1.11) was fixed.
	- Convert problem with setBinaryStream and setAsciiStream was fixed.




1.2 Changes To Version 1.12
----------------------------
	- The method setAsciiStream support very large streams now.
	- A bug with Nummeric columns was fixed.





1.3 Changes To Version 1.13
----------------------------
	- The method getBigDecimal support float sql columns now.
	- Deal Locks with complex transactions was fixed.
	- The date/time methods use now the default timezone.




1.4 Changes To Version 1.14
----------------------------
	- The new datatypes in MS SQL 7.0, i.e. nchar, ntext, nvarchar, 
	  varchar larger 255 character now supported with the propeties
	  sql7=true.
	- A performence problem with sql statements larger 512 bytes was fixed.
	- A bug in isAutoIncrement() was fixed.



1.5 Changes To Version 1.15
----------------------------
	- The daylihgt bug was fixed.
	- Problems with null values was fixed in the new datatype modus (sql7=true).
	- Named pipes are support now.



1.6 Changes To Version 1.16
----------------------------
	- The bug with isClosed() (only 1.16) was fixed. After a call of
	  isClosed() was the connection corrupt. 
	- The method getObject() return a Integer with SQL type tinyint and smalint now.
	- The method getObject() return a GUID String with SQL type unique now.
	- The SQLState of same SQLException was changed.



2 Installation
----------------
	- copy the class files in your classpath
List of the files:
	- com.inet.tds.TdsDriver
	- com.inet.tds.TdsConnection
	- com.inet.tds.TdsResultSet
	- com.inet.tds.TdsStatement
	- com.inet.tds.TdsResultSetMetaData
	- com.inet.tds.TdsDatabaseMetaData
	- com.inet.tds.SqlFunctions		only for use with Escape Functions




3   Getting Started
--------------------

3.1 SQL Server, Java and JDBC Versions
--------------------------------------
	Java Versions: 1.1x
	JDBC Version:  1.22
	SQL Server Version:
	- Microsoft SQL Server 7.0
	- Microsoft SQL Server 6.5



3.2 Check Host Name And Port Number Of Your Server
--------------------------------------------------
This driver works with Microsoft SQL Servers that are configured to use
the TCP/IP networking protocol. Please verify that your server is
currently listening to a TCP/IP port. 

If you know  that your SQL Server is listening on a TCP/IP port and you
know the host name of your SQL Server and the port number you can go to
the next chapter.

To check or enable the TCP/IP Sockets protocol follow these steps:
For Microsoft SQL Server 6.5: 
Click -SQL Setup- in the MS SQL Server program group.
If not selected select -Change Network Support-, select -TCP/IP- and enter 
the port number you want to use (default port: 1433).
If -Change Network Support- is selected, than cancel the setup.
  
For Microsoft SQL Server 7.0: 
Click -SQL Server Network Utility- in the Microsoft SQL Server 7.0 program group. 
On the general property sheet, click -Add- and select -TCP/IP- under Network libraries.
Enter the port number and the proxy address (if nesessary) and click OK.
 
The default port number for the Microsoft SQL Server is usually 1433.
However, servers can be configured to listen on any port number.
  
To make sure that the RDBMS server is listening on the machine name and
port number you specified use: 
  
telnet <hostname or ip address> <port number> 

If the connection is refused, then the hostname or the port number are
incorrect. 



3.3 Driver Name
---------------
The class name of the driver is
	com.inet.tds.TdsDriver



3.4 Url Syntax
--------------
	jdbc:inetdae:hostname:portnumber
	jdbc:inetdae:hostname			-> with default port 1433
	jdbc:inetdae:hostname:portnumber?database=MyDb&language=deutsch
						-> with properties
	jdbc:inetdae://servername/pipe/pipename	-> with named pipes

e.g.	jdbc:inetdae:www.inetsoftware.de:1433
	jdbc:inetdae:localHost:1433
	jdbc:inetdae://MyServer/pipe/sql/query



3.5 Implemented Properties
--------------------------
	- database	-> default is "master"
	- language	-> default is "us_english"
			-> "" -> SQL Server default language
	- user
	- password
	- charset	-> see Character Converting
	- nowarnings	-> "true" getWarnings() returns null
	- sql7		-> default is "false" 
			-> "true" the new datatypes supported


There are two ways to put the properties to the driver:
	1. append the properties to the URL like this
		jdbc:inetdae:hostname:portnumber?database=MyDb&language=deutsch

	2. call from method getConnection(String url, Properties info) from
	   the driver manager.



3.6 Connection Example
----------------------
import java.sql.*;                  // JDBC package

String url = "jdbc:inetdae:localhost:1433";	// use your hostname and port number here
String login = "sa";	   			// use your login here
String password = "";	   			// use your password here

	try{
		DriverManager.setLogStream(System.out); // to create more info 
							  // for technical support
			
		//load the class with the driver
		//Class.forName("com.inet.tds.TdsDriver");		// JDK,Netscape
		//or
		Class.forName("com.inet.tds.TdsDriver").newInstance();	// JDK,Netscape,IE
		//or
		//new com.inet.tds.TdsDriver();			// JDK,Netscape,IE

		
		//set a timeout for login and query
		DriverManager.setLoginTimeout(10);


		//open a connection to the database
		Connection connection = DriverManager.getConnection(url,login,password);

		//select a database
		connection.setCatalog( "MyDatabase");

		//create a statement
		Statement st = connection.createStatement();

		//execute a query
		ResultSet rs = st.executeQuery("SELECT * FROM tblExample");

		// read the data and put it to the console
		while (rs.next()){
			for(int j=1; j<=rs.getMetaData().getColumnCount(); j++){
				System.out.print( rs.getObject(j)+"\t");
			}
			System.out.println();    
		}

		
		//close the objects
		st.close();
		connection.close();

	}catch(Exception e){
		e.printStackTrace();
	}




4. Named Pipes
--------------
Another solution to connect to the sql server are named pipes.
Named pipes are working only in the Java VM 1.1.7 or higher and 
the Java VM 1.2Beta 4 or higher. Named pipes are equal to files 
with UNC path. We have tested named pipes only with the Win32 VM
from Sun. If you want to use named pipes from another platform,  
you require SMB (server message block) on the client or you must 
install NFS (network file system) on the sql server.

The default pipe of the sql server is "/sql/query" but you
can change this pipe name in the server manager.




5.   Escape Clauses
-------------------
The driver implements follow escape clauses:

5.1  Date and Time
------------------
	{d 'yyyy-mm-dd'}
	{t 'hh:mm:ss[.fff]'}
	{ts 'yyyy-mm-dd hh:mm:ss[.fff]'}

5.2  Stored Procedures
----------------------
	{call storedProcedures('Param1'[,'Param2'][,?][...])}
	{? = call storedProcedures('Param1'[,'Param2'][,?][...])}

5.3  Functions
--------------
	{fn now()}
	{fn curdate()}
	{fn curtime()}





6.   Character Converting 
-------------------------
By default character converting is disabled. To use character converting in the driver you need to append "charset=YourCharSet" to the url.

for example: "jdbc:inetdae:localhost:1433?charset=Cp1250"
or           "jdbc:inetdae:localhost:1433?charset=" + sun.io.ByteToCharConverter.getDefault().getCharacterEncoding();
or           "jdbc:inetdae:localhost:1433?charset=" + System.getProperty("file.encoding");

Tip: the property charset is case-sensitive in java





7.   New Datatypes with SQL Server 7.0
--------------------------------------
The SQL server 7.0 supports new datatypes 7.0, i.e. nchar, ntext, nvarchar, 
varchar larger than 255 character.

You can use the new datatypes if you set the property sql7=true. 

If you set the property sql7=true than you can't connect to the SQL Server 6.5.

 


8.   Copyright and Support
--------------------------
	Copyright by i-net software
	More info and updates you can find at
	http:\\www.inetsoftware.de
	news:\\news.inetsoftware.de




© 1998 i-net software


