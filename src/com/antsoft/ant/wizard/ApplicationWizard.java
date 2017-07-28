/*
 * $Header: /AntIDE/source/ant/wizard/ApplicationWizard.java 7     99-05-17 12:03a Multipia $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 7 $
 */
package com.antsoft.ant.wizard;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 *  ApplicationWizard
 */
public class ApplicationWizard{
	/**  application dialog */
	static AppDialog appDialog;
	/**  current dialog number */
	static int current;

	/**  stream to write source code */
  static File[] file = new File[3];

  FileOutputStream fs1;
  FileOutputStream fs2;
  FileOutputStream fs3;

  //header부분에 들어갈 내용
  //String author = "lila";
  String date;
  static String aboutDialogName = "AboutDialog";
  String projectName = "Wizard";
  String copy = "Copyright (c) ";
  String objectName = "frame";

	/** current directory path */
	String currentpath = "d:\\";
	/** packagename */
	String packName;
	/** class file name */
	String className;
	/** frame class name */
	String fName;
	/** title */
	String fTitle;
  /** using swing */
  boolean usingSwing;
  /** can standalone */
  boolean canStandalone;
	/** status bar */
	boolean statusBar;
	/** menu bar */
	boolean menuBar;
  /** tool bar */
  boolean toolBar;
  /** about box */
  boolean aboutBox;

  /** constructor */
  public ApplicationWizard(){
    appDialog = new AppDialog();
//    appDialog.setSize(420, 250);
    appDialog.setVisible( true );
    if (appDialog.isOK()) {
      initialData();
      generateMainClassCode();
      generateFrameClassCode();
    }
  }

  public ApplicationWizard(Frame parent, String path) {
  	currentpath = path;
    appDialog = new AppDialog(parent, "Application Wizard", true);
//    appDialog.setSize(420, 250);
    appDialog.setVisible( true );
    if (appDialog.isOK()) {
      initialData();
      generateMainClassCode();
      generateFrameClassCode();
    }
	}

  /**
   *  initialData - dialog로부터 데이터들을 받아온다
   */
  void initialData(){
    packName = appDialog.getPackName();
    className = appDialog.getClassName();
    usingSwing = appDialog.getUsingSwing();
    //canStandalone = appDialog.getCanStandalone();
    fName = appDialog.getFrameName();
    fTitle = appDialog.getFrameTitle();
    menuBar = appDialog.getMenuBar();
    toolBar = appDialog.getToolBar();
    statusBar = appDialog.getStatusBar();
    aboutBox = appDialog.getAboutBox();

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
	 *  generateMainClassCode
	 */
	void generateMainClassCode() {
		try {
    	File parent = new File( currentpath + File.separator + packName.replace('.', File.separator.charAt(0)) );
      if ((parent != null) && (parent.exists() || parent.mkdirs())) {
	      file[0] = new File( parent, className + ".java" );
  	    fs1 = new FileOutputStream( file[0] );

    	  //Header
				write1( "/*\n" );
  	    		write1( " *\tpackage : " + packName + "\n" );
				write1( " *\tsource  : " + className + ".java\n" );
				write1( " *\tdate    : " + date + "\n" );
				write1( " */\n\n" );

	      write1( "package " + packName + ";\n\n" );
				write1( "import java.awt.*;\n" );
				write1( "import java.awt.event.*;\n" );

	      if( usingSwing ){
  	      write1( "import javax.swing.*;\n" );
    	  }

	      //constructor
				write1( "\npublic class " + className + " { \n\n"  );
  	    write1( "\t" + fName + " " + objectName + ";\n\n" );
				write1( "\t/**\n" );
				write1( "\t *  " + className + " -  constructor \n" );
				write1( "\t */\n" );
				write1( "\tpublic " + className + "() {\n\n" );
    	  write1( "\t\t" + objectName + " = new " + fName + "();\n\n" );
	      write1( "\t\t" + objectName + ".pack();\n" );
  	    write1( "\t\t" + objectName + ".setVisible( true );\n\n" );
				write1( "\t}\n\n" );
				write1( "\tpublic static void main( String args[] ) {\n\n" );
	      write1( "\t\t" + className + " app = new " + className + "();\n\n" );
				write1( "	}\n" );
				write1( "}" );

	      fs1.close();
      }
      else System.out.println("sasdfasdfasdf");
		} catch( IOException e ) {
		}
	}

	/**
	 *  generateFrameClassCode
	 */
	void generateFrameClassCode() {
		try{
    	File parent = new File( currentpath + File.separator + packName.replace('.', File.separator.charAt(0)) );
			// 새로운 sourcefile을 만든다.
      if ((parent != null) && (parent.exists() || parent.mkdirs())) {
	      file[1] = new File( parent, fName + ".java" );
	      fs2 = new FileOutputStream( file[1] );

	      write2( "/*\n" );
	      		write2( " *\tpackage : " + packName + "\n" );
				write2( " *\tsource  : " + fName + ".java\n" );
				write2( " *\tdate    : " + date + "\n" );
				write2( " */\n\n" );

  	    write2( "package " + packName + ";\n\n");
				write2( "import java.awt.*;\n" );
				write2( "import java.awt.event.*;\n" );

	      //using Swing case
  	    if( usingSwing ){
    	    write2( "import javax.swing.*;\n" );
		  		write2( "\npublic class " + fName );
        	write2( " extends JFrame implements ActionListener{ \n\n"  );
	        if( menuBar ){
  	        write2( "\t//Menu Bar\n" );
    	      write2( "\tJMenuBar menuBar;\n\n" );
      	    write2( "\t//Menus\n" );
        	  write2( "\tJMenu fileMenu;\n" );
          	write2( "\tJMenu helpMenu;\n\n" );
	          write2( "\t//Menu Items\n" );
  	        write2( "\tJMenuItem exitItem;\n" );
    	      write2( "\tJMenuItem aboutItem;\n\n" );
        	}
      	  if( toolBar ){
	          write2( "\t//tool bar\n" );
  	        write2( "\tJToolBar toolBar;\n\n" );
    	      write2( "\t//labels\n" );
      	    write2( "\tJButton btn1;\n" );
        	  write2( "\tJButton btn2;\n" );
          	write2( "\tJButton btn3;\n\n" );
	        }
  	      if( statusBar ){
    	      write2( "\tJLabel statusBar;\n\n" );
      	  }

	        write2( "\t/**\n" );
  	      write2( "\t *\t" + fName + " -  constructor \n" );
    	    write2( "\t */\n" );
      	  write2( "\tpublic " + fName + "() {\n\n" );
        	write2( "\t\tsuper( \"" + fTitle + "\" );\n" );
	        write2( "\t\taddWindowListener(new WindowAdapter() {\n" );
  	      write2( "\t\t\tpublic void windowClosing(WindowEvent evt) {\n" );
    	    write2( "\t\t\t\tSystem.exit(0);\n" );
      	  write2( "\t\t\t}\n" );
        	write2( "\t\t}\n" );
	        write2( "\t);\n" );
  	      write2( "\t\taInit();\n\n" );
    	    write2( "\t}\n\n" );
      	  write2( "\t/**\n" );
	        write2( "\t *\tframe initialization\n" );
  	      write2( "\t */\n" );
    	    write2( "\tpublic void aInit() {\n\n" );

      	  if( menuBar ){
	          write2( "\t\t//menubar create\n" );
  	        write2( "\t\tmenuBar = new JMenuBar();\n\n" );
    	      write2( "\t\tfileMenu = new JMenu( \"File\" );\n" );
      	    write2( "\t\thelpMenu = new JMenu( \"Help\" );\n\n" );
        	  write2( "\t\texitItem = new JMenuItem( \"Exit\" );\n" );
          	write2( "\t\taboutItem = new JMenuItem( \"About\" );\n\n" );
	          write2( "\t\tfileMenu.add( exitItem );\n" );
  	        write2( "\t\thelpMenu.add( aboutItem );\n\n" );
    	      write2( "\t\tmenuBar.add( fileMenu );\n" );
      	    write2( "\t\tmenuBar.add( helpMenu );\n\n" );
        	  write2( "\t\tthis.setJMenuBar( menuBar );\n\n" );

	          write2( "\t\texitItem.addActionListener( this );\n" );
  	        write2( "\t\taboutItem.addActionListener( this );\n\n" );
    	    }
	        if( toolBar ){
  	        write2( "\t\t//toolbar create\n" );
    	      write2( "\t\ttoolBar = new JToolBar();\n\n" );
      	    write2( "\t\tbtn1 = new JButton( new ImageIcon( \"open.gif\" ) );\n" );
        	  write2( "\t\tbtn2 = new JButton( new ImageIcon( \"close.gif\" ) );\n" );
          	write2( "\t\tbtn3 = new JButton( new ImageIcon( \"help.gif\" ) );\n" );
	          write2( "\t\ttoolBar.add( btn1 );\n" );
  	        write2( "\t\ttoolBar.add( btn2 );\n" );
    	      write2( "\t\ttoolBar.add( btn3 );\n\n" );
      	    write2( "\t\ttoolBar.setFloatable( false );\n" );
        	  write2( "\t\tthis.getContentPane().add( toolBar, BorderLayout.NORTH );\n\n" );
	        }
  	      if( statusBar ){
    	      write2( "\t\t//status bar\n" );
      	    write2( "\t\tstatusBar = new JLabel();\n" );
        	  write2( "\t\tstatusBar.setText( \" \" );\n" );
          	write2( "\t\tthis.getContentPane().add( statusBar, BorderLayout.SOUTH );\n\n" );
	        }
  	      if( aboutBox ){
    	      generateAboutDialog();
      	  }

	      }else{
			  	write2( "\npublic class " + fName );
    	    write2( " extends Frame implements ActionListener{ \n\n"  );
      	  if( menuBar ){
	          write2( "\t//Menu Bar\n" );
  	        write2( "\tMenuBar menuBar;\n\n" );
    	      write2( "\t//Menus\n" );
      	    write2( "\tMenu fileMenu;\n" );
        	  write2( "\tMenu helpMenu;\n\n" );
          	write2( "\t//Menu Items\n" );
	          write2( "\tMenuItem exitItem;\n" );
  	        write2( "\tMenuItem aboutItem;\n\n" );
    	    }
    	    if( statusBar ){
      	    write2( "\tLabel statusBar;\n\n" );
        	}
	        write2( "\t/**\n" );
  	      write2( "\t *\t" + fName + " -  constructor \n" );
    	    write2( "\t */\n" );
      	  write2( "\tpublic " + fName + "() {\n\n" );
        	write2( "\t\tsuper( \"" + fTitle + "\" );\n" );
	        write2( "\t\taddWindowListener(new WindowAdapter() {\n" );
  	      write2( "\t\t\tpublic void windowClosing(WindowEvent evt) {\n" );
    	    write2( "\t\t\t\tSystem.exit(0);\n" );
      	  write2( "\t\t\t}\n" );
        	write2( "\t}\n" );
	        write2( "\t);\n" );
  	      write2( "\t\tainit();\n\n" );
    	    write2( "\t}\n\n" );
      	  write2( "\t/**\n" );
        	write2( "\t *\tframe initialization\n" );
	        write2( "\t */\n" );
  	      write2( "\tpublic void ainit() {\n\n" );

    	    if( menuBar ){
	          write2( "\t\t//menubar create\n" );
  	        write2( "\t\tmenuBar = new MenuBar();\n\n" );
    	      write2( "\t\tfileMenu = new Menu( \"File\" );\n" );
      	    write2( "\t\thelpMenu = new Menu( \"Help\" );\n\n" );
        	  write2( "\t\texitItem = new MenuItem( \"Exit\" );\n" );
          	write2( "\t\taboutItem = new MenuItem( \"About\" );\n\n" );
	          write2( "\t\tfileMenu.add( exitItem );\n" );
  	        write2( "\t\thelpMenu.add( aboutItem );\n\n" );
    	      write2( "\t\tmenuBar.add( fileMenu );\n" );
      	    write2( "\t\tmenuBar.setHelpMenu( helpMenu );\n\n" );
        	  write2( "\t\tthis.setMenuBar( menuBar );\n\n" );

	          write2( "\t\texitItem.addActionListener( this );\n" );
  	        write2( "\t\taboutItem.addActionListener( this );\n\n" );
    	    }
      	  if( statusBar ){
        	  write2( "\t\tstatusBar = new Label();\n" );
          	write2( "\t\tstatusBar.setText( \" \" );\n" );
	          write2( "\t\tadd( statusBar, BorderLayout.SOUTH );\n\n" );
  	      }
    	    if( aboutBox ){
      	    generateAboutDialog();
        	}
	      }
  	    write2( "\t}\n\n" );

	      //actionEvent code generate---------------------------------------
  	    write2( "\t/**\n" );
    	  write2( "\t *\tactionPerformed\n" );
      	write2( "\t */\n" );
	      write2( "\tpublic void actionPerformed( ActionEvent e ) {\n\n" );
  	    write2( "\t\tif( e.getSource() == exitItem ){\n" );
    	  write2( "\t\t\tdispose();\n" );
      	write2( "\t\t}\n" );
	      if( aboutBox ){
  	      write2( "\t\telse if( e.getSource() == aboutItem ){\n" );
    	    write2( "\t\t\t" + aboutDialogName + " dlg = new " + aboutDialogName + "( this, \"About Dialog\" );\n" );
      	  write2( "\t\t\t dlg.setSize( 200, 130 );\n" );
        	write2( "\t\t\t dlg.setVisible( true );\n" );
	        write2( "\t\t}\n" );
  	    }
    	  write2( "\t}\n" );

	      write2( "}");

  	    fs2.close();
      }
		} catch( IOException e ) {
		}
	}

  /**
   *  generateAboutDialo - about dialog를 생성하는 코드를 만들어 주는 함수
   */
  public void generateAboutDialog(){

    try {
    	File parent = new File( currentpath + File.separator + packName.replace('.', File.separator.charAt(0)) );
      if ((parent != null) && (parent.exists() || parent.mkdirs())) {
	      file[2] = new File( parent, aboutDialogName + ".java" );
  	    fs3 = new FileOutputStream( file[2] );

    	  //Header
				write3( "/*\n" );
  	    		write3( " *\tpackage : " + packName + "\n" );
				write3( " *\tsource  : " + aboutDialogName + ".java\n" );
				write3( " *\tdate    : " + date + "\n" );
				write3( " */\n\n" );

    	  write3( "package " + packName + ";\n\n" );
				write3( "import java.awt.*;\n" );
				write3( "import java.awt.event.*;\n" );

  	    if( usingSwing ){
    	    write3( "import javax.swing.*;\n\n" );
      	  write3( "public class " + aboutDialogName + " extends JDialog implements ActionListener{ \n\n"  );
        	write3( "\t//panels\n" );
	        write3( "\tJPanel panel1;\n" );
  	      write3( "\tJPanel panel2;\n\n" );
    	    write3( "\t//button\n" );
      	  write3( "\tJButton btn;\n\n" );
	        write3( "\t//labels;\n" );
  	      write3( "\tJLabel proName;\n" );
    	    write3( "\tJLabel copyright;\n\n" );

      	  //constructor
	  			write3( "\t/**\n" );
			  	write3( "\t *  " + aboutDialogName + " -  constructor \n" );
				  write3( "\t */\n" );
   				write3( "\tpublic " + aboutDialogName + "( JFrame f, String msg ) {\n\n" );
      	  write3( "\t\tsuper( f, msg, true );\n\n" );
        	write3( "\t\tpanel1 = new JPanel();\n" );
	        write3( "\t\tpanel1.setLayout( new FlowLayout( FlowLayout.LEFT ) );\n" );
  	      write3( "\t\tproName = new JLabel( \"" + projectName + "\" );\n" );
    	    write3( "\t\tcopyright = new JLabel( \"" + copy + "\" );\n" );
      	  write3( "\t\tpanel1.add( proName );\n" );
        	write3( "\t\tpanel1.add( copyright );\n\n" );
	        write3( "\t\tpanel2 = new JPanel();\n" );
  	      write3( "\t\tpanel2.setLayout( new GridLayout( 1,2 ) );\n" );
    	    write3( "\t\tpanel2.add( new JLabel( \"ICON\" ) );\n" );
      	  write3( "\t\tpanel2.add( panel1 );\n\n" );
        	write3( "\t\tbtn = new JButton( \"OK\" );\n" );
	        write3( "\t\tbtn.addActionListener(this);\n" );
  	      write3( "\t\tJPanel panel3 = new JPanel();\n" );
    	    write3( "\t\tpanel3.setLayout( new FlowLayout( FlowLayout.CENTER ) );\n" );
      	  write3( "\t\tpanel3.add( btn );\n\n" );
        	write3( "\t\tgetContentPane().setLayout( new BorderLayout() );\n" );
	        write3( "\t\tgetContentPane().add( panel2, BorderLayout.CENTER );\n" );
  	      write3( "\t\tgetContentPane().add( new JPanel(), BorderLayout.NORTH );\n" );
    	    write3( "\t\tgetContentPane().add( new JPanel(), BorderLayout.EAST );\n" );
      	  write3( "\t\tgetContentPane().add( new JPanel(), BorderLayout.WEST );\n" );
        	write3( "\t\tgetContentPane().add( panel3, BorderLayout.SOUTH );\n" );
	      }else{
  	      write3( "public class " + aboutDialogName + " extends Dialog implements ActionListener{ \n\n"  );
    	    write3( "\t//panels\n" );
      	  write3( "\tPanel panel1;\n" );
        	write3( "\tPanel panel2;\n\n" );
	        write3( "\t//button\n" );
  	      write3( "\tButton btn;\n\n" );
    	    write3( "\t//labels;\n" );
      	  write3( "\tLabel proName;\n" );
        	write3( "\tLabel copyright;\n\n" );

	        //constructor
		  		write3( "\t/**\n" );
			  	write3( "\t *  " + aboutDialogName + " -  constructor \n" );
				  write3( "\t */\n" );
   				write3( "\tpublic " + aboutDialogName + "( Frame f, String msg ) {\n\n" );
	        write3( "\t\tsuper( f, msg, true );\n\n" );
  	      write3( "\t\tpanel1 = new Panel();\n" );
    	    write3( "\t\tpanel1.setLayout( new FlowLayout( FlowLayout.LEFT ) );\n" );
      	  write3( "\t\tproName = new Label( \"" + projectName + "\" );\n" );
        	write3( "\t\tcopyright = new Label( \"" + copy + "\" );\n" );
	        write3( "\t\tpanel1.add( proName );\n" );
  	      write3( "\t\tpanel1.add( copyright );\n\n" );
    	    write3( "\t\tpanel2 = new Panel();\n" );
      	  write3( "\t\tpanel2.setLayout( new GridLayout( 1,2 ) );\n" );
        	write3( "\t\tpanel2.add( new Label( \"ICON\" ) );\n" );
	        write3( "\t\tpanel2.add( panel1 );\n\n" );
  	      write3( "\t\tbtn = new Button( \"OK\" );\n\n" );
    	    write3( "\t\tbtn.addActionListener(this);\n" );
      	  write3( "\t\tPanel panel3 = new Panel();\n" );
        	write3( "\t\tpanel3.setLayout( new FlowLayout( FlowLayout.CENTER ) );\n" );
	        write3( "\t\tpanel3.add( btn );\n\n" );
  	      write3( "\t\tsetLayout( new BorderLayout() );\n" );
    	    write3( "\t\tadd( panel2, BorderLayout.CENTER );\n" );
      	  write3( "\t\tadd( new Panel(), BorderLayout.NORTH );\n" );
        	write3( "\t\tadd( new Panel(), BorderLayout.EAST );\n" );
	        write3( "\t\tadd( new Panel(), BorderLayout.WEST );\n" );
  	      write3( "\t\tadd( panel3, BorderLayout.SOUTH );\n" );
    	  }
      	write3( "\t}\n\n" );
	      write3( "\t/**\n" );
  	    write3( "\t *\tactionListener - 이벤트 처리\n" );
    	  write3( "\t */\n" );
      	write3( "\tpublic void actionPerformed( ActionEvent e ) {\n\n" );
	      write3( "\t\tif( e.getSource() == btn ) {\n" );
  	    write3( "\t\t\tdispose();\n" );
    	  write3( "\t\t}\n" );
      	write3( "\t}\n" );
	      write3( "}\n" );

  	    fs3.close();
      }
		} catch( IOException e ) {
		}
  }

  public File[] getFiles() {
  	return file;
  }

  //writers - 출력하는 함수---------------------------------------------------

  void write1( String s ) throws IOException{
    fs1.write( s.getBytes() );
  }
  void write2( String s ) throws IOException{
    fs2.write( s.getBytes() );
  }
  void write3( String s ) throws IOException{
    fs3.write( s.getBytes() );
  }

}
