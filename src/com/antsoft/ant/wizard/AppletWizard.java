/*
 * $Header: /AntIDE/source/ant/wizard/AppletWizard.java 6     99-05-17 12:03a Multipia $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 6 $
 */
package com.antsoft.ant.wizard;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 *  AppletWizard
 */
public class AppletWizard implements ActionListener{
	/**  parent frame */
	Frame parent;
	/**  first dialog */
	static AppletDialog dialog;
	/**  current dialog number */
	static int current = 1;

	/**  stream to write source code */
	FileOutputStream out;

	/**  package name */
	String packagename = "";
	/**  new class file */
	String classname = "";
	/**  current directory path */
	String path = "";

	/**  html file name */
	String filename = "";
	/**  title */
	String title = "";
	/**  codebase */
	String codebase = "";
	/**  width */
	int width;
	/**  height */
	int height;
	/**  vspace */
	int vspace;
	/**  hspace */
	int hspace;
	/**  align */
	String align = "";

	/**  parameter */
	Vector paramname;
	/**  value */
	Vector paramvalue;
	/**  variable */
	Vector paramvariable;

	/**  using swing */
	boolean swing;
	/**  standalone */
	boolean standalone;

	File[] file = new File[2];

	/**
	 *  AppletWizard - constructor
	 */
	public AppletWizard( Frame parent, String path ) {
		this.parent = parent;
		this.path = path;
		paramname = new Vector( 1, 1 );
		paramvalue = new Vector( 1, 1 );
		paramvariable = new Vector( 1, 1 );

		// instantiate dialog
		dialog = new AppletDialog( parent, "AppletWizard", true, this );
//		dialog.setSize( 400, 310);
    dialog.setVisible( true );
//    dialog.start();
	}

	/**
	 *  actionPerformed  -  action event handler
	 *
	 *  @param  ae  ActionEvent
	 */
	public void actionPerformed( ActionEvent ae ) {
		// event source분석
		String cmd = ae.getActionCommand();

		// Command에 따른 처리
		if( cmd.equals( "Applet" ) ){
			current = 1;
			start();
		} else if( cmd.equals( "Next" ) ){
			// data를 얻는다.
			if( current == 1 ) {           // first dialog
				packagename = dialog.packName.getText();
				classname = dialog.className.getText();

				// classname check
				if( !classname.equals( "" ) ) {
					dialog.next();
					current++;
				} else {
				 JOptionPane.showMessageDialog(dialog, "Invalid Class Name", "Warning", JOptionPane.ERROR_MESSAGE);		
				}
				swing = dialog.usingSwing.isSelected();
				standalone = dialog.canStandalone.isSelected();	
			} else if ( current == 2 ) {   // second dialog
				// show next dialog
				dialog.next();
				current++;

				try{
					
					int row = dialog.table.getRowCount();
					// parameter 받기
					for( int i = 0; i < row  ; i++ ) { 
						String name = (String) dialog.table.getValueAt( i, 0 );
						String value = (String)dialog.table.getValueAt( i, 1 );
						String variable = (String)dialog.table.getValueAt( i, 2 );

						if( !name.equals( "" ) && !value.equals( "" ) && !variable.equals( "" )) {
							paramname.addElement( name );
							paramvalue.addElement( value );
							paramvariable.addElement( variable );				
						}
					}	
				} catch( Exception e ) {
					System.out.println(  e.toString() );
				}
			}
		} else if( cmd.equals( "Back" ) ) {
			dialog.previous( );
			if( current == 2 ) {
				paramname.removeAllElements();
				paramvalue.removeAllElements();
				paramvariable.removeAllElements();
			}
			current--;
		} else if( cmd.equals( "Finish" ) ) {
			try{
				// data얻기
				title = dialog.title.getText();
				filename = dialog.name.getText();
				codebase = dialog.codeBase.getText();
				String s_width = dialog.width.getText(); 
				String s_height = dialog.height.getText();
				String s_vspace = dialog.vspace.getText();
				String s_hspace = dialog.hspace.getText();
				try {
					if( !s_width.equals( "" ) ) width  = Integer.parseInt( s_width );
					if( !s_height.equals( "" ) ) height = Integer.parseInt( s_height );
					if( !s_vspace.equals( "" ) ) vspace = Integer.parseInt( s_vspace );
					if( !s_hspace.equals( "" ) ) hspace = Integer.parseInt( s_hspace );
				} catch( NumberFormatException e ) {
					JOptionPane.showMessageDialog(dialog, "Invalid Number Format", "Warning", JOptionPane.ERROR_MESSAGE);		
				}

				align = (String)dialog.align.getSelectedItem();		

				if( !filename.equals( "" ) && ( width != 0 ) && ( height != 0 ) ) { 
					dialog.setVisible( false );
					// generate source file
					generateCode();
					generateHtml();
					// clear resource
					clear();
					current = 1;
				} else if( filename.equals( "" ) ) {
				 JOptionPane.showMessageDialog(dialog, "Invalid Html File Name", "Warning", JOptionPane.ERROR_MESSAGE);	
				} else if( height == 0 ) {
				 JOptionPane.showMessageDialog(dialog, "Invalid Height", "Warning", JOptionPane.ERROR_MESSAGE);		
				} else if ( width == 0 ) {
				 JOptionPane.showMessageDialog(dialog, "Invalid Width", "Warning", JOptionPane.ERROR_MESSAGE);		
				}				
			} catch( Exception e ) {
				System.out.println( e.toString() );
			}
		} else if( cmd.equals( "Cancel" ) ) {
			dialog.setVisible( false );
			clear();
		}	

		
	}

	/**
	 *  generateCode  
	 */
	void generateCode() {
		try {
    	File parent = new File( path + File.separator + packagename.replace('.', File.separator.charAt(0)) );
      if ((parent != null) && (parent.exists() || parent.mkdirs())) {
				file[0] = new File( parent, classname + ".java" );
				if( file[0].exists() ) {
				// 이미 있다는 메세지를 내보낸다.
					file[0] = new File( path + File.separator + packagename, classname + "2.java" );
				} else {
					//file.createNewFile();
				}

				out = new FileOutputStream( file[0] );

				// date
				Calendar calendar = Calendar.getInstance();
				int year = calendar.get( Calendar.YEAR );
				int month = calendar.get( Calendar.MONTH ) + 1;
				int day = calendar.get( Calendar.DAY_OF_MONTH );

				write( "/**\n" );
				if( !packagename.equals( "" ) ) write( " *  @package : " + packagename + "\n" );
				write( " *  @source  : " + classname + ".java\n" );
				//write( " *  @Author : " + author + "\n" );
				write( " *  @date    : " + year + "." + month + "." + day +  "\n" );
				write( " */\n" );

				// package
				if( !packagename.equals( "" ) )
					write( "package " + packagename + ";\n\n" );
				// import
				write( "import java.applet.*;\n" );
				write( "import java.awt.*;\n" );
				write( "import java.awt.event.*;\n" );
				if( swing ) write( "import javax.swing.*;\n" );
				write( "\n" );

				if( swing ) write( "public class " + classname + " extends JApplet {\n" );
				else write( "public class " + classname + " extends Applet { \n"  );

				try {
					// main method
					if( standalone ) {
						write( "	/**\n" );
						write( "	 *  main \n" );
						write( "	 */\n" );
						write( "	public static void main( String args[] ) {\n\n" );
						write( "	}\n\n" );
					}
				} catch( Exception e ) {
					System.out.println( e.toString() );
				}

				// init method
				write( "	/**\n" );
				write( "	 *  init \n" );
				write( "	 */\n" );
				write( "	public void init(  ) {\n" );

				write( "		// parameter parsing\n" );
				for( int i = 0 ; i < paramname.size() ; i++ )
					write( "		String " + (String)paramvariable.elementAt( i ) + " = getParameter( \"" + (String)paramname.elementAt( i ) + "\" );\n" );

		  		write( "	}\n\n" );
				// start method
				write( "	/**\n" );
				write( "	 *  start\n" );
				write( "	 */\n" );
				write( "	public void start() {\n\n" );
				write( "	}\n\n" );
				// paint method
				write( "	/**\n" );
				write( "	 *  paint\n" );
				write( "	 */\n" );
				write( "	public void paint() {\n\n" );
				write( "	}\n\n");

				write( "}\n" );

				out.close();
      }
		} catch( IOException e ) {
			System.out.println( e.toString() );
		}
	}


	/**
	 *  generateHtml  -  applet을 보여줄 html만들기
	 */
	void generateHtml() {
		try {
    	File parent = new File( path + File.separator + packagename.replace('.', File.separator.charAt(0)) );
      if ((parent != null) && (parent.exists() || parent.mkdirs())) {
				file[1] = new File( parent, filename + ".html" );
				if( file[1].exists() ) {
				// 이미 있다는 메세지를 내보낸다.
				} else {
				//file.createNewFile();
				}

				out = new FileOutputStream( file[1] );

				// html file 작섣
				write( "<html>\n" );
				if( !title.equals( "" ) ) {
					write( "<head>\n" );
					write( "  <title>" + title + "</title>\n" );
					write( "</head>\n" );
				}
				write( "<body>\n" );
				write( "  <applet code=" + classname + ".class width=" + width + " height=" + height );
				if( !codebase.equals( "" ) ) write( " codebase=" + codebase );
				write( " align=" + align );
				if( vspace != 0 ) write( " vspace=" + vspace );
				if( hspace != 0 ) write( " hspace=" + hspace );
				write( ">\n" );

				try {
				//System.out.println( "vector size : " + paramname.size() );
				// parameter
					for( int i = 0; i < paramname.size() ; i++ )
						write( "  <param name=" + (String)paramname.elementAt( i ) + " value=" + (String)paramvalue.elementAt( i ) + ">\n" );
				} catch( Exception e ) {
					System.out.println( e.toString() );
				}
				write( "  </applet>\n" );
				write( "</html>" );
				out.close();
      }
		} catch( IOException e ) {

		}
	}

  public File[] getFiles() {
  	return file;
  }

	/**
	 *  write
	 *
	 *  @param  source  source code
	 */
	void write( String source )  {
		try{
			out.write( source.getBytes() );
		} catch( IOException e ) {
			System.out.println( "Exception occurred.." + e.toString() );
		}
	}
	
	/**
	 *  start  -  appletwizard starting
	 */
	public void start() {
		dialog.setVisible( true );
		dialog.start();
	}
	
	/**
	 *  clear  -  clear all contents of resources
	 */
	void clear() {
		// Vector
		paramname.removeAllElements();
		paramvalue.removeAllElements();
		paramvariable.removeAllElements();

		dialog.clear();
	}

}	
