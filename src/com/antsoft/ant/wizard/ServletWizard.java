/*
 * $Header: /AntIDE/source/ant/wizard/ServletWizard.java 7     99-05-17 12:03a Multipia $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 7 $
 */
package com.antsoft.ant.wizard;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;

public class ServletWizard {
	/** dialog */
	static ServletDialog servletDlg;

	/** stream to write source code */
	FileOutputStream fs;
  File[] file = new File[1];

  //header부분에 들어갈 내용
  //String author = "lila";
  String date;

  String objectName = "servlet1";
	String packName;
	String className;
	String currentpath = "d://";

  boolean doGet;
  boolean doPost;
  boolean service;
  boolean doPut;
  boolean doDelete;

	/**
	 *  ServletWizard  - constructor
	 */
	public ServletWizard(){
    servletDlg = new ServletDialog();
//    servletDlg.setSize(440, 370);
    servletDlg.setVisible( true );
    if ( servletDlg.isOK() ) {
      initialData();
      generateCode();
    }
	}

	public ServletWizard(Frame parent, String path){
  	currentpath = path;
    servletDlg = new ServletDialog(parent, "Servlet Wizard", true);
//    servletDlg.setSize(440, 370);
    servletDlg.setVisible( true );
    if ( servletDlg.isOK() ) {
      initialData();
      generateCode();
    }
	}

  void initialData(){
    packName = servletDlg.getPackName();
    className = servletDlg.getClassName();
    doGet = servletDlg.getDoget();
    doPost = servletDlg.getDopost();
    service = servletDlg.getService();
    doPut = servletDlg.getDoput();
    doDelete = servletDlg.getDodelete();

    //date
    Calendar cal = Calendar.getInstance();
    int year = cal.get( Calendar.YEAR );
    int month = cal.get( Calendar.MONTH ) + 1;
    int day = cal.get( Calendar.DATE );
    date = Integer.toString(year) + "." + Integer.toString(month) + "." + Integer.toString(day);
  }

  public void setCurrentPath(String path) {
  	currentpath = path;
  }

  /**
   *  generateCode - 서블릿 코드 생성
   */
  void generateCode(){
    try{
    	File parent = new File( currentpath + File.separator + packName.replace('.', File.separator.charAt(0)) );
      if ((parent != null) && (parent.exists() || parent.mkdirs())) {
				file[0] = new File( parent, className + ".java" );
  	    fs = new FileOutputStream( file[0] );

	      //Header
  	    write( "/*\n" );
    	write( " *\tpackage : " + packName + "\n" );
  		write( " *\tsource  : " + className + ".java\n" );
  		write( " *\tdate    : " + date + "\n" );
	  	write( " */\n\n" );

	      write( "package " + packName + ";\n\n" );
  			write( "import java.io.*;\n" );
	  		write( "import java.util.*;\n" );
 		  	write( "import javax.servlet.*;\n" );
	   		write( "import javax.servlet.http.*;\n\n" );
  	    //constructor
			  write( "\npublic class " + className + " extends HttpServlet { \n\n"  );
  			write( "\t/**\n" );
		  	write( "\t *  " + className + " -  constructor \n" );
			  write( "\t */\n" );
  			write( "\tpublic void " + className + "() {\n\n" );
      	write( "\t}\n\n" );

	      if( doGet ){
  	      write( "\t/**\n" );
    	    write( "\t *\tdoGet\n" );
      	  write( "\t */\n" );
        	write( "\tpublic void doGet( HttpServletRequest req, HttpServletResponse res )\n" );
          write( "\t\t\t\t\t\tthrows ServletException, IOException {\n\n" );
	        write( "\t\tres.setContentType( \"text/html\" );\n" );
  	      write( "\t\tPrintWriter out = new PrintWriter( res.getOutputStream() );\n\n" );
    	    write( "\t\tout.close();\n" );
      	  write( "\t}\n\n" );
	      }
  	    if( doPost ){
    	    write( "\t/**\n" );
      	  write( "\t *\tdoPost\n" );
        	write( "\t */\n" );
	        write( "\tpublic void doPost( HttpServletRequest req, HttpServletResponse res )\n" );
          write( "\t\t\t\t\t\tthrows ServletException, IOException {\n\n" );
  	      write( "\t\tres.setContentType( \"text/html\" );\n" );
    	    write( "\t\tPrintWriter out = new PrintWriter( res.getOutputStream() );\n\n" );
      	  write( "\t\tout.close();\n" );
        	write( "\t}\n\n" );
	      }
  	    if( doPut ){
    	    write( "\t/**\n" );
      	  write( "\t *\tdoPut\n" );
        	write( "\t */\n" );
	        write( "\tpublic void doPut( HttpServletRequest req, HttpServletResponse res )\n" );
          write( "\t\t\t\t\t\tthrows ServletException, IOException {\n\n" );
  	      write( "\t}\n\n" );
    	  }
	      if( doDelete ){
  	      write( "\t/**\n" );
    	    write( "\t *\tdoDelete\n" );
      	  write( "\t */\n" );
        	write( "\tpublic void doDelete( HttpServletRequest req, HttpServletResponse res )\n" );
          write( "\t\t\t\t\t\tthrows ServletException, IOException {\n\n" );
	        write( "\t}\n\n" );
  	    }
    	  if( service ){
      	  write( "\t/**\n" );
        	write( "\t *\tservice\n" );
	        write( "\t */\n" );
  	      write( "\tpublic void service( HttpServletRequest req, HttpServletResponse res )\n" );
          write( "\t\t\t\t\t\tthrows ServletException, IOException {\n\n" );
    	    write( "\t\tPrintWriter out = new PrintWriter( res.getOutputStream() );\n\n" );
      	  write( "\t}\n\n" );
	      }
  	    write( "}\n" );

    	  fs.close();
      }
    }catch( IOException e ){
    }
  }

  public File[] getFiles() {
  	return file;
  }

  void write( String s ) throws IOException{
    fs.write( s.getBytes() );
  }
}
