/*
 *	package : test
 *	source  : AboutDialog.java
 *	date    : 1999.9.19
 */

package test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AboutDialog extends JDialog implements ActionListener{ 

	//panels
	JPanel panel1;
	JPanel panel2;

	//button
	JButton btn;

	//labels;
	JLabel proName;
	JLabel copyright;

	/**
	 *  AboutDialog -  constructor 
	 */
	public AboutDialog( JFrame f, String msg ) {
		super( f, msg, true );

		panel1 = new JPanel();
		panel1.setLayout( new FlowLayout( FlowLayout.LEFT ) );
		proName = new JLabel( "Wizard" );
		copyright = new JLabel( "Copyright (c) " );
		panel1.add( proName );
		panel1.add( copyright );

		panel2 = new JPanel();
		panel2.setLayout( new GridLayout( 1,2 ) );
		panel2.add( new JLabel( "ICON" ) );
		panel2.add( panel1 );

		btn = new JButton( "OK" );
		btn.addActionListener(this);
		JPanel panel3 = new JPanel();
		panel3.setLayout( new FlowLayout( FlowLayout.CENTER ) );
		panel3.add( btn );

		getContentPane().setLayout( new BorderLayout() );
		getContentPane().add( panel2, BorderLayout.CENTER );
		getContentPane().add( new JPanel(), BorderLayout.NORTH );
		getContentPane().add( new JPanel(), BorderLayout.EAST );
		getContentPane().add( new JPanel(), BorderLayout.WEST );
		getContentPane().add( panel3, BorderLayout.SOUTH );
	}

	/**
	 *	actionListener - ??¨¬?¨¡? ?©ø¢¬?
	 */
	public void actionPerformed( ActionEvent e ) {
		if( e.getSource() == btn ) {
			dispose();
		}
	}
}
