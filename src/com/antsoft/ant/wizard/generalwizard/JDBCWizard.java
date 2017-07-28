/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/wizard/generalwizard/JDBCWizard.java,v 1.2 1999/08/19 05:25:07 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.2 $
 */
package com.antsoft.ant.wizard.generalwizard;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 *  JDBCWizard
 *
 *  @Author : Young-Joo Kim
 */
public class JDBCWizard implements ActionListener{
	/** parent frame */
	JFrame parent;
	/** dialog */
	static JDBCDialog dialog;
	/** file chooser */
	static JFileChooser filechooser;
	/** file */
	static File[] file = new File[2];
	/** current dialog number */
	static int current;
	/** stream to write source code */
	FileOutputStream out;

	/** package name */
	String packagename;
	/** class name */
	String classname;	
	/** insert method */
	boolean insert;
	/** delete method */
	boolean delete;
	/** update method */
	boolean update;	
	
	/** driver */
	String driver;
	/** driver name */
	String driverName;
	/** url */
	String url;
	/** id */
	String id;
	/** password */
	String password;
	
	/** path */
	String path;
	/** protocol */
	String protocol;
	/** hostIP */
	String hostIP;
	/** hostPort */
	String hostPort;
	/** dbname */
	String dbName;

	/**
	 *  JDBCWizard  -  constructor
	 *
	 *  @param  path  current directory path
	 */
	public JDBCWizard( JFrame parent, String path ) {
		this.parent = parent;
		this.path = path;

		filechooser = new JFileChooser( path );
		dialog = new JDBCDialog( parent, "JDBCWizard", true, this   );
   	dialog.setVisible( true );
	}

	/**
	 *  actionPerformed  -  actionEvent handler
	 *
	 *  @param  e  ActionEvent
	 */
	public void actionPerformed( ActionEvent ae ) {
		// analyze event command
		String cmd = ae.getActionCommand();

		if( cmd.equals( "JDBC" )){
			dialog.start( );
			dialog.setVisible( true );

		} else if( cmd.equals( "Browse" ) ) {
			int filestatus = filechooser.showDialog( dialog, "Driver Selection" );
			if( filestatus == filechooser.APPROVE_OPTION ) {
				file[0] = filechooser.getSelectedFile();
				dialog.jdbcDriver.setText( file[0].getName() );
			}
		} else if( cmd.equals( "Back" ) ) {
			dialog.previous();
		} else if ( cmd.equals( "Next" ) ) {
			classname = dialog.className.getText();

			if( !classname.equals( "" ) ) {
				dialog.next();
			}

			// data를 얻는다.
			packagename = dialog.packName.getText();
			insert = dialog.insert.isSelected();
			delete = dialog.delete.isSelected();
			update = dialog.update.isSelected();
		} else if( cmd.equals( "Finish" ) ) {
			try{
				// 입력데이터를 읽는다.
				protocol = (String)dialog.protocol.getSelectedItem();
				hostIP = dialog.hostIP.getText();
				hostPort = dialog.hostPort.getText();
				dbName = dialog.dbName.getText();
				driver = dialog.jdbcDriver.getText();
				driverName = dialog.driverName.getText();
				id = dialog.id.getText();
				password = dialog.passwd.getText();

				// url
				url = protocol + ":";
				if( !hostIP.equals( "" ) && !hostPort.equals( "" ) ) url = url + "//" + hostIP + ":" + hostPort+ "/" ;
				if( hostIP.equals( "" ) && !hostPort.equals( "" ) ) url = url + hostPort + ":";
				url = url + dbName;

				if( !protocol.equals( "" ) && !dbName.equals( "" ) && !id.equals( "" ) && !password.equals( "" ) ) {
					dialog.setVisible( false );
					generateCode();
					clear();
				} else if( protocol.equals( "" ) ) {
				 JOptionPane.showMessageDialog(dialog, "Invalid Protocol", "Warning", JOptionPane.ERROR_MESSAGE);
				} else if( dbName.equals( "" ) ) {
				 JOptionPane.showMessageDialog(dialog, "Invalid DB Name", "Warning", JOptionPane.ERROR_MESSAGE);
				} else if( driver.equals( "" ) ) {
          JOptionPane.showMessageDialog(dialog, "Invalid JDBC Driver", "Warning", JOptionPane.ERROR_MESSAGE);
        } else if( id.equals( "" ) ) {
				 JOptionPane.showMessageDialog(dialog, "Invalid ID", "Warning", JOptionPane.ERROR_MESSAGE);
				} else if( password.equals( "") ) {
				 JOptionPane.showMessageDialog(dialog, "Invalid Password", "Warning", JOptionPane.ERROR_MESSAGE);
				}
			} catch( Exception e ) {
				System.out.println( e.toString() );
			}

			//generateConnectionInfo();
		} else if( cmd.equals( "Cancel" ) ) {
			dialog.setVisible( false );
			clear();
		}
	}

	/**
	 *  generateCode  -  write source code into File
	 */
	void generateCode() {
		String source;

		try {
			// source file을 새로 생성한다.
    	File parent = new File( path + File.separator + packagename.replace('.', File.separator.charAt(0)) );
      if ((parent != null) && (parent.exists() || parent.mkdirs())) {
				file[0] = new File( parent, classname+ ".java" );
				if( file[0].exists() ) {
					// 이미 있다는 메세지를 내보낸다.
					file[0] = new File( parent, "2.java" );
				}

				out = new FileOutputStream( file[0] );

				// date
				Calendar calendar = Calendar.getInstance();
				int year = calendar.get( Calendar.YEAR );
				int month = calendar.get( Calendar.MONTH ) + 1 ;
				int day = calendar.get( Calendar.DAY_OF_MONTH );

				write( "/*\n" );
				if( !packagename.equals( "" ) ) write( " *  package : " + packagename + "\n" );
				write( " *  source  : " + classname + ".java\n" );
				//write( " *  author : " + author + "\n" );
				write( " *  date    : " + year + "." + month + "." + day + "\n" );
				write( " */\n\n" );
				// package
				if( !packagename.equals( "" ) )
					write( "package " + packagename + ";\n\n" );

				write( "import java.sql.*;\n\n" );
				write( "public class " + classname + " { \n"  );
				write( "\t// driver name\n" );
				write( "\tpublic static final String DRIVER = \"" + driverName + "\";\n" );
				write( "\t// url\n" );
				write( "\tpublic static final String URL = \"" + url + ":\";\n" );
				write( "\t// ID\n" );
				if( !id.equals( "" ) )write( "\tpublic static final String ID = \"" + id + "\";\n" );
				write( "\t// Password\n" );
				if( !password.equals( "" ) ) write( "\tpublic static final String PASSWD = \"" + password + "\";\n" );

				write( "\n\t/**\n");
				write( "\t *   " + classname + "\n" );
				write( "\t */\n" );
				write( "\tpublic " + classname + "() { \n" );
				write( "\t}\n\n" );

				// example
				write( "\t/**\n" );
				write( "\t *  connectExample - JDBC연동하는 예제입니다. \n" );
				write( "\t */\n" );
				write( "\tpublic void connectExample() {\n" );
				write( "\t\tConnection con = null;\n" );
				write( "\t\tStatement stmt = null;\n" );
				write( "\t\tResultSet result = null;\n\n" );

				write( "\t\t//DB connection\n" );
				write( "\t\ttry { \n" );
	 			write( "\t\t\tClass.forName( DRIVER );\n" );
				write( "\t\t\tcon = (Connection)DriverManager.getConnection( URL, ID, PASSWD );\n" );
				write( "\t\t\tstmt = con.createStatement();\n" );
				write( "\t\t} catch( ClassNotFoundException e) {\n" );
				write( "\t\t\t// exception handling\n" );
				write( "\t\t} catch( SQLException e ) {\n" );
				write( "\t\t\t// exception handling\n" );
				write( "\t\t}\n\n" );

				write( "\t\t// query\n" );
				write( "\t\ttry { \n " );
				write( "\t\t\t// To Do :\n" );
				write( "\t\t\tString query = \"select * from Table\";\n " );
				write( "\t\t\tresult = stmt.executeQuery( query );\n" );
				write( "\t\t\tresult.next();\n" );
				write( "\t\t\tString data = result.getString( 1 );\n" );
				write( "\t\t\t// connection close\n" );
				write( "\t\t\tstmt.close();\n" );
				write( "\t\t\tcon.close();\n" );
				write( "\t\t} catch( SQLException e ) {\n" );
				write( "\t\t\t// exception handling\n" );
				write( "\t\t}\n" );
				write( "\t}\n\n" );

				// insert method
				if( insert ) {
					write( "\t/**\n" );
					write( "\t *  insert - insert query를 수행하는 method\n" );
					write( "\t */\n" );
					write( "\tpublic void insert() {\n" );
					write( "\t\tConnection con = null;\n" );
					write( "\t\tStatement stmt = null;\n" );
					write( "\t\tResultSet result = null;\n\n" );

					write( "\t\t//DB connection\n" );
					write( "\t\ttry { \n" );
			 		write( "\t\t\tClass.forName( DRIVER );\n" );
					write( "\t\t\tcon = (Connection)DriverManager.getConnection( URL, ID, PASSWD );\n" );
					write( "\t\t\tstmt = con.createStatement();\n" );
					write( "\t\t} catch( ClassNotFoundException e) {\n" );
					write( "\t\t\t// exception handling\n" );
					write( "\t\t} catch( SQLException e ) {\n" );
					write( "\t\t\t// exception handling\n" );
					write( "\t\t}\n\n" );

					write( "\t\t// query\n" );
					write( "\t\ttry { \n " );
					write( "\t\t\t// To Do :\n" );
					write( "\t\t\tString query = \"insert into Table ( column1, column2, column3) values ( value1, value2, value3) \";\n " );
					write( "\t\t\tstmt.executeUpdate( query );\n\n" );
					write( "\t\t\t// connection close\n" );
					write( "\t\t\tstmt.close();\n" );
					write( "\t\t\tcon.close();\n" );
					write( "\t\t} catch( SQLException e ) {\n" );
					write( "\t\t\t// exception handling\n" );
					write( "\t\t}\n" );
					write( "\t}\n\n" );
				}
				if( delete ) {
					write( "\t/**\n" );
					write( "\t *  delete - delete query를 수행하는 method \n" );
					write( "\t */\n" );
					write( "\tpublic void delete() {\n" );
					write( "\t\tConnection con = null;\n" );
					write( "\t\tStatement stmt = null;\n" );
					write( "\t\tResultSet result = null;\n\n" );

					write( "\t\t//DB connection\n" );
					write( "\t\ttry { \n" );
			 		write( "\t\t\tClass.forName( DRIVER );\n" );
					write( "\t\t\tcon = (Connection)DriverManager.getConnection( URL, ID, PASSWD );\n" );
					write( "\t\t\tstmt = con.createStatement();\n" );
					write( "\t\t} catch( ClassNotFoundException e) {\n" );
					write( "\t\t\t// exception handling\n" );
					write( "\t\t} catch( SQLException e ) {\n" );
					write( "\t\t\t// exception handling\n" );
					write( "\t\t}\n\n" );

					write( "\t\t// query\n" );
					write( "\t\ttry { \n " );
					write( "\t\t\t// To Do :\n" );
					write( "\t\t\tString query = \"delete Table where column='value'\";\n " );
					write( "\t\t\tstmt.executeUpdate( query );\n\n" );
					write( "\t\t\t// connection close\n" );
					write( "\t\t\tstmt.close();\n" );
					write( "\t\t\tcon.close();\n" );
					write( "\t\t} catch( SQLException e ) {\n" );
					write( "\t\t\t// exception handling\n" );
					write( "\t\t}\n" );
					write( "\t}\n\n" );
				}
				if( update ) {
					write( "\t/**\n" );
					write( "\t *  update - update query를 수생하는 method\n" );
          write( "\t */\n" );
					write( "\tpublic void update() {\n" );
					write( "\t\tConnection con = null;\n" );
					write( "\t\tStatement stmt = null;\n" );
					write( "\t\tResultSet result = null;\n\n" );

					write( "\t\t//DB connection\n" );
					write( "\t\ttry { \n" );
			 		write( "\t\t\tClass.forName( DRIVER );\n" );
					write( "\t\t\tcon = (Connection)DriverManager.getConnection( URL, ID, PASSWD );\n" );
					write( "\t\t\tstmt = con.createStatement();\n" );
					write( "\t\t} catch( ClassNotFoundException e) {\n" );
					write( "\t\t\t// exception handling\n" );
					write( "\t\t} catch( SQLException e ) {\n" );
					write( "\t\t\t// exception handling\n" );
					write( "\t\t}\n\n" );

					write( "\t\t// query\n" );
					write( "\t\ttry { \n " );
					write( "\t\t\t// To Do :\n" );
					write( "\t\t\tString query = \"update Table set column1 ='value1' where column2 = 'value2'\";\n " );
					write( "\t\t\tstmt.executeUpdate( query );\n\n" );
					write( "\t\t\t// connection close\n" );
					write( "\t\t\tstmt.close();\n" );
					write( "\t\t\tcon.close();\n" );
					write( "\t\t} catch( SQLException e ) {\n" );
					write( "\t\t\t// exception handling\n" );
					write( "\t\t}\n" );
					write( "\t}\n" );
				}
				write( "}");

				out.close();
      }
		} catch( IOException e ) {
		}
	}

  public File[] getFiles() {
  	return file;
  }

	/**
	 *  write  - write String into File
	 *
	 *  @param  source   String to be written into File
	 */
	void write( String source ) throws IOException {
		out.write( source.getBytes() );
	}

	/**
	 *  clear
	 */
	void clear() {
		dialog.clear();
	}
}

