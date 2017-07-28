/*
 *	package : test
 *	source  : MyFrame.java
 *	date    : 1999.7.22
 */

package test;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

public class MyFrame extends JFrame implements WindowListener {
	public void windowOpened( WindowEvent e )	{
		//TO DO 
		System.out.println("Window Opened: " + e.toString());
	}

	public void windowClosing( WindowEvent e )	{
		//TO DO 
		System.out.println("Window Closing: " + e.toString());
		dispose();
	}

	public void windowClosed( WindowEvent e )	{
		//TO DO 
		System.out.println("Window Closed: " + e.toString());
		System.exit(0);
	}

	public void windowIconified( WindowEvent e )	{
		//TO DO 
		System.out.println("Window Iconified: " + e.toString());
	}

	public void windowDeiconified( WindowEvent e )	{
		//TO DO 
		System.out.println("Window Deiconified: " + e.toString());
	}

	public void windowActivated( WindowEvent e )	{
		//TO DO 
		System.out.println("Window Activated: " + e.toString());
	}

	public void windowDeactivated( WindowEvent e )	{	
		//TO DO 
		System.out.println("Window Deactivated: " + e.toString());
	}


  /**
	 *	MyFrame -  constructor 
	 */
	public MyFrame() {
		super("MyFrame");
	}

	public static void main( String[] args ) {
	  MyFrame frame = new MyFrame();
	  Vector list = new Vector();
	  
	  int number = Integer.parseInt(args[0]);

	  for ( int i = 0; i <= number; i++ ) {
	  	list.addElement( new Integer( i ) );
	  	if ( ( i % 10000 ) == 0 ) 
	  		System.out.println( "I = " + i );
	  }	  		
	 
	  JList listbox = new JList( list );
		JScrollPane scroll = new JScrollPane( listbox );
		
	  frame.getContentPane().add(scroll, BorderLayout.CENTER);
	  frame.pack();
	  frame.setVisible(true);
	  frame.addWindowListener(frame);
	}
}
