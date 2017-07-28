/*
 *	package : test
 *	source  : Frame1.java
 *	date    : 1999.9.19
 */

package test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Frame1 extends JFrame implements ActionListener{ 

	//Menu Bar
	JMenuBar menuBar;

	//Menus
	JMenu fileMenu;
	JMenu helpMenu;

	//Menu Items
	JMenuItem exitItem;
	JMenuItem aboutItem;

	/**
	 *	Frame1 -  constructor 
	 */
	public Frame1() {
		super( "My frame" );
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				System.exit(0);
			}
		}
		);
		aInit();
	}

	/**
	 *	frame initialization
	 */
	public void aInit() {
		//menubar create
		menuBar = new JMenuBar();

		fileMenu = new JMenu( "File" );
		helpMenu = new JMenu( "Help" );

		exitItem = new JMenuItem( "Exit" );
		aboutItem = new JMenuItem( "About" );

		fileMenu.add( exitItem );
		helpMenu.add( aboutItem );

		menuBar.add( fileMenu );
		menuBar.add( helpMenu );

		this.setJMenuBar( menuBar );

		exitItem.addActionListener( this );
		aboutItem.addActionListener( this );
	}

	/**
	 *	actionPerformed
	 */
	public void actionPerformed( ActionEvent e ) {
		if( e.getSource() == exitItem ){
			dispose();
		}
		else if( e.getSource() == aboutItem ){
			AboutDialog dlg = new AboutDialog( this, "About Dialog" );
			 dlg.setSize( 200, 130 );
			 dlg.setVisible( true );
		}
	}
}
