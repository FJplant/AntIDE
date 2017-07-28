/*
 * $Id: Main.java,v 1.23 1999/08/31 12:43:09 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.23 $
 * Author:       Baek, Jin-woo
 *               Kwon, Young Mo
 * SubAuthor:    Kim, Sung-hoon
 */
package com.antsoft.ant.main;

import javax.swing.UIManager;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.*;
import java.io.*;
import java.util.Hashtable;

import com.antsoft.ant.codecontext.CodeContext;
import com.antsoft.ant.manager.projectmanager.*;
import com.antsoft.ant.manager.keymanager.*;
import com.antsoft.ant.property.*;
import com.antsoft.ant.util.ImageList;
import com.antsoft.ant.util.WindowDisposer;
import com.antsoft.ant.util.DocumentWriter;
import com.antsoft.ant.util.EchoThread;

/**
 * Ant가 시작되는 메인 Class입니다.
 *
 * @author Baek, Jin-woo
 * @author Kim sang-kyun
 * @author Kwon, Young Mo
 */
public class Main extends JApplet implements Runnable {
  private boolean packFrame = false;
  private static boolean debugMode = false;
	private MainFrame mainFrame;
	private ThreadGroup antThreadGroup;
	static private OutputFrame antOutputFrame;
  public static IdeProperty property;
  public static Hashtable paramHash;
  public static boolean isParamLoadEnd = false;

  //Construct the application
  public Main() {

  	antInit();
  	if ( debugMode )
  		displayAntOutput();
  	
    ThreadGroup antThreadGroup = new ThreadGroup("Ant-ThreadGroup");
    Thread t = new Thread(antThreadGroup, this, "Ant-MainThread");
    t.setDaemon( true );
    t.start();
  }

  private void antInit() {
  	Frame splashOwner = new Frame("Splash Owner");
		SplashScreen splash = new SplashScreen(splashOwner, ImageList.splashIcon);

	  try{
      /* load property */
      splash.showStatus("Loading IDE property...");
      Thread.sleep(20);
      property = IdePropertyManager.loadProperty();

      /* load paramHash */
      splash.showStatus("Loading method parameter data...");
      Thread.sleep(20);

      Thread paramDataLoader = new ParamDataLoader();
      paramDataLoader.setPriority(Thread.MIN_PRIORITY);
      paramDataLoader.start();

      /* codecontext를 생성한다 */
      splash.showStatus("Generating Code Context...");
      Thread.sleep(20);
      CodeContext codeContext = new CodeContext();

      /* main frame을 생성하고 codecontext 를 넘긴다 */
      splash.showStatus("Building up Main Frame...");
      Thread.sleep(20);

      mainFrame = new MainFrame(codeContext, splash);

      /* Keymanager를 시작한다 */
      //KeyManager.start();

      //Validate frames that have preset sizes
      //Pack frames that have useful preferred size info, e.g. from their layout
      if (packFrame) {
        mainFrame.pack();
      }
      else
        mainFrame.validate();

      Rectangle lastBounds = Main.property.getLastFrameBounds();
      if(lastBounds != null) {
         mainFrame.setBounds(lastBounds);
      }
      else{
        // Set Main Frame's Size
        mainFrame.setSize(new Dimension(800, 600));
        // Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = mainFrame.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        mainFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        // End ------- center the window ------------
      }

			loadServletRunnerOption();
			loadJavaDocOption();			
      splash.showStatus("Loading modules finished...");
      Thread.sleep(20);
      splash.showStatus("and starting Ant...");
      Thread.sleep(50);
      splash.close();      
      WindowDisposer.disposeWindow(splashOwner);
      
      // show the main frame
      mainFrame.setVisible(true);
    } catch(Exception e) {
    	e.printStackTrace();
    }
  }
	// antInit() method

	private void loadServletRunnerOption() {
    String servletRunner = Main.property.getServletRunnerPath();
    if(servletRunner == null) {
      servletRunner = new String("none");
      String servletWebBrowserPath = new String("none");
      boolean makingBatch = true;
      String servletPort = new String("8080");
      String servletLog = new String("50");
      String servletMax = new String("100");
      String servletTimeout = new String("5000");
      String servletDirectory = new String(".\\examples");
      String servletProperty = new String(".\\examples\\servlet.properties");

      Main.property.setServletRunnerPath(servletRunner);
      Main.property.setServletWebBrowserPath(servletWebBrowserPath);
      Main.property.setMakingBatch(makingBatch);
      Main.property.setServletPort(servletPort);
      Main.property.setServletLog(servletLog);
      Main.property.setServletMax(servletMax);
      Main.property.setServletTimeout(servletTimeout);
      Main.property.setServletDirectory(servletDirectory);
      Main.property.setServletProperty(servletProperty);

      IdePropertyManager.saveProperty(Main.property);
    }
	}
	
	private void loadJavaDocOption() {
    String encoding = Main.property.getEncodingDoc();
    if (encoding == null) {
      boolean jdk1_1Doc = true;
      boolean verbose = false;
      int scope = 4;
      boolean author = false;
      boolean version = false;
      boolean noindex = false;
      boolean notree = false;
      boolean nodeprecate = false;

      boolean newclasspath = false;
      boolean newsourcepath = false;
      String classpath = new String(" ");
      String sourcepath = new String(" ");
      encoding = new String(" ");
      String docencoding = new String(" ");
      String jflag = new String(" ");

      boolean use = false;
      boolean nohelp = false;
      boolean nonavbar = false;
      boolean nodeprecatedlist = false;
      boolean splitindex = false;
      String doclet = new String(" ");
      String docletpath = new String(" ");
      String bootclasspath = new String(" ");
      String extdirs = new String(" ");
      String locale = new String(" ");
      String link = new String(" ");
      String linkoffline = new String(" ");
      String group = new String(" ");
      String helpfile = new String(" ");
      String stylesheetfile = new String(" ");

      String windowtitle = new String(" ");
      String doctitle = new String(" ");
      String header = new String(" ");
      String footer = new String(" ");
      String bottom = new String(" ");

      Main.property.setJavadocVersion(jdk1_1Doc);
      Main.property.setVerboseDoc(verbose);
      Main.property.setScopeDoc(scope);
      Main.property.setAuthorDoc(author);
      Main.property.setVersionDoc(version);
      Main.property.setNoindexDoc(noindex);
      Main.property.setNotreeDoc(notree);
      Main.property.setNodeprecateDoc(nodeprecate);
      Main.property.setNewclasspathDoc(newclasspath);
      Main.property.setNewsourcepathDoc(newsourcepath);
      Main.property.setClasspathDoc(classpath);
      Main.property.setSourcepathDoc(sourcepath);
      Main.property.setEncodingDoc(encoding);
      Main.property.setDocencodingDoc(docencoding);
      Main.property.setJDoc(jflag);
      Main.property.setUseDoc(use);
      Main.property.setNohelpDoc(nohelp);
      Main.property.setNonavbarDoc(nonavbar);
      Main.property.setNodeprecatedlistDoc(nodeprecatedlist);
      Main.property.setSplitindexDoc(splitindex);
      Main.property.setDocletDoc(doclet);
      Main.property.setDocletpathDoc(docletpath);
      Main.property.setBootclasspathDoc(bootclasspath);
      Main.property.setExtdirsDoc(extdirs);
      Main.property.setLocaleDoc(locale);
      Main.property.setLinkDoc(link);
      Main.property.setLinkofflineDoc(linkoffline);
      Main.property.setGroupDoc(group);
      Main.property.setHelpfileDoc(helpfile);
      Main.property.setStylesheetfileDoc(stylesheetfile);
      Main.property.setWindowtitleDoc(windowtitle);
      Main.property.setDoctitleDoc(doctitle);
      Main.property.setHeaderDoc(header);
      Main.property.setFooterDoc(footer);
      Main.property.setBottomDoc(bottom);
      IdePropertyManager.saveProperty(Main.property);
    }
	}
	
	/**
	 *  Ant에서 출력하는 standard output과 standard error 메시지를 화면상의 
	 * Window에 출력하도록 하는 Method
	 *
	 * @author Kwon, Young Mo
	 */
	private void displayAntOutput() {
 		// Debug Mode이면 Ant의 Output을 OutputFrame에 출력하도록 한다.
 		antOutputFrame = new OutputFrame( "Ant output", mainFrame );

 		// First, redirect the standard output stream
 		Output stdout = antOutputFrame.getStdOut();
 		PipedOutputStream pos = new PipedOutputStream();
 		PrintStream ps = new PrintStream( pos );
 		
 		// sink with piped output stream 
 		PipedInputStream pis = null;
 		try {
	 		pis = new PipedInputStream( pos );
 		} catch ( IOException e ) {
 			System.err.println( "Pipe connection failed: " );
 		}
 		BufferedReader reader = new BufferedReader( new InputStreamReader( pis ) );
 		// Buffered Writer를 사용하면 즉각적인 출력이 되지 않는다.
 		DocumentWriter writer = new DocumentWriter( stdout.getOutput().getDocument() );
		EchoThread echoOut = new EchoThread( reader, writer );
		echoOut.setDaemon( true );
		echoOut.start();
		
 		System.setOut(ps);
 		
 		// Second, redirect the standard error stream
 		Output stderr = antOutputFrame.getStdErr();
 		pos = new PipedOutputStream();
 		ps = new PrintStream( pos );
 		
 		// sink with piped output stream
 		try {
	 		pis = new PipedInputStream( pos );
 		} catch ( IOException e ) {
 			System.err.println( "Pipe connection failed: " );
 		}
 		reader = new BufferedReader( new InputStreamReader( pis ) );
 		writer = new DocumentWriter( stderr.getOutput().getDocument() );
 		
		EchoThread echoErr = new EchoThread( reader, writer );
		echoErr.setDaemon( true );
		echoErr.start();
 		System.setErr(ps);

 		antOutputFrame.setSize( 500, 250 ); 		
 		antOutputFrame.setVisible(true);
 		
 		// Test output
 		System.out.println("Standard output redirected...");
 		System.err.println("Standard error redirected...");
	}

	public void init() {
		getContentPane().add(new JButton("About Ant..."), BorderLayout.CENTER);
	}

  public void destroy() {
  }

  public void start() {
  }

  public void stop() {
  }

  public static void main(String[] args) {
    PrintStream err = null;

    try  {
      // Command Argument들을 받아 들이는 곳입니다.
      int argc = 0 ;
      while ( argc < args.length ) {
        if ( args[argc].equals("-errlog") ) {
          // Standard Error 설정
          argc ++;
          FileOutputStream out = new FileOutputStream( args[argc] );
          // 에러를 파일에다가 출력하는 기능입니다.
          err = new PrintStream( out );
          System.setErr( err );
          System.err.println( "Error Log Starts:" );
        } else if ( args[argc].equals("-debug") ) {
        	debugMode = true;
        }
        argc++;
      }
	  // Standard Error 설정
    } catch ( IOException ioe ) {
	    System.out.println( "IOException occurred" );
	  } catch (Exception e) {
      e.printStackTrace();
    }

    Main app = new Main();
  }

  public static boolean isDebugMode() {
    return debugMode;
  }

	/**
	 * Ant main thread's run method
	 */
  public void run() {
    while ( true ) {
      // 현재는 1초마다 한번씩 일어나며 아무 일도 안함.
      try {
        Thread.currentThread().sleep( 1000 );
	      // TODO: Source Pool내에서 수정이된 소스를 지정한 곳에다가 미리 저장하도록 한다.
	      
      } catch ( InterruptedException e ) {
      }
    }
  }

  /**
   * 메소드의 파라메터 정보를 읽어오는 thread
   */
  class ParamDataLoader extends Thread {
    public void run(){
      paramHash = IdePropertyManager.loadParamHash();
      isParamLoadEnd = true;
    }
  }

	/**
	 * Gets the antOutputFrame
	 * 
	 * @return antOutputFrame
	 */
	static public OutputFrame getAntOutputFrame( ) {
		return antOutputFrame;
	}
}
/* 
 * $Log: Main.java,v $
 * Revision 1.23  1999/08/31 12:43:09  multipia
 * -debug 옵션 처리 추가
 *
 * Revision 1.22  1999/08/28 08:22:25  remember
 * no message
 *
 * Revision 1.21  1999/08/27 08:42:30  multipia
 * no message
 *
 * Revision 1.20  1999/08/27 08:31:44  multipia
 * Thread priority 설정 없앰
 *
 * Revision 1.19  1999/08/27 08:27:08  multipia
 * no message
 *
 * Revision 1.18  1999/08/27 07:11:20  multipia
 * Standard Output과 Standard Error를 Window 화면으로 redirection
 *
 * Revision 1.17  1999/08/25 10:23:31  multipia
 * Applet인 상태에서 동작을 하도록 약간 수정.
 * 소스 정리
 *
 */
