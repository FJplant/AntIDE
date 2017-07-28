/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/wizard/generalwizard/ProtocolDialog.java,v 1.2 1999/08/19 05:25:07 multipia Exp $
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
import javax.swing.*;

import com.antsoft.ant.util.WindowDisposer;

/**
 *  ProtocolDialog
 */
class ProtocolDialog extends JDialog {
	JLabel label;
	JTextField input;
	JButton ok;

	/**
	 *  ProtocolDialog  -  constructor
	 */	
	public ProtocolDialog( JFrame frame, String title, boolean modal, JDBCDialog parent ) {
		super( frame, title, modal );
		
		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer());

		label = new JLabel( "Protocol" );
		label.setPreferredSize( new Dimension( 60, 20 ) );
		input = new JTextField( 20 );
		input.setPreferredSize( new Dimension( 100, 20 ) );
		ok = new JButton( "Add" );
		ok.setPreferredSize( new Dimension( 50, 20 ) );
		input.setText( "" );

		getContentPane().setLayout( new FlowLayout() );
		getContentPane().add( label );
		getContentPane().add( input );
		getContentPane().add( ok );
	
		ok.setActionCommand( "OK" );
		ok.addActionListener( parent );

		pack();
//    setSize(420, 300);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = this.getSize();
    if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
    if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
    this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		setResizable( false );
	}

	/**
	 *  getProtocol 
	 */
	public String getProtocol() {
		return input.getText();
	}
} 

