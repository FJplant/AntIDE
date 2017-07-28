/*
 * $Header: /AntIDE/source/ant/wizard/JDBCDialog.java 10    99-05-31 9:41a Remember $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 10 $
 */

package com.antsoft.ant.wizard;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.*;

import com.antsoft.ant.main.Main;
import com.antsoft.ant.property.*;


/**
 *  JDBCDialog
 * @author YunKyung Kim
 */
public class JDBCDialog extends JDialog implements ActionListener {
	//EventHandler
	JDBCWizard wizard;
	//Frame
	JFrame frame;

	// First dialog
	JButton backx = new JButton( "<Back" );
	JButton next = new JButton( "Next>" );
	JButton cancel1 = new JButton( "Cancel" );

	JTextField packName = new JTextField( "jdbc", 14 );
	JTextField className = new JTextField( "Jdbc", 14 );
	JLabel classLbl = new JLabel( "Class Name" );
	JLabel packLbl = new JLabel( "Package Name" );

	JCheckBox insert = new JCheckBox( "add insert method" );
	JCheckBox delete = new JCheckBox( "add delete method" );
	JCheckBox update = new JCheckBox( "add update method" );


	// Second dialog
	//ComboBox
	JComboBox protocol = new JComboBox();
	RandomAccessFile accessfile;
	File file;

	//Button
	JButton browse = new JButton( ".." );
	JButton add = new JButton( "+" );
	JButton remove = new JButton( "-" );
	JButton back = new JButton( "<Back" );
	JButton finish = new JButton( "Finish" );
	JButton cancel2 = new JButton( "Cancel" );

	//TextField
	JTextField hostIP = new JTextField( 15 );
	JTextField hostPort = new JTextField( 15 );
	JTextField dbName = new JTextField( 15 );
	JTextField jdbcDriver = new JTextField( 15 );
	JTextField driverName = new JTextField( 15 );
	JTextField id = new JTextField( 7 );
	JTextField passwd = new JTextField( 7 );

	//Label
	JLabel proLbl = new JLabel( "Protocol" );
	JLabel hostIPLbl = new JLabel( "Host IP" );
	JLabel hostPortLbl = new JLabel( "Host Port" );
	JLabel dbNameLbl = new JLabel( "DB Name" );
	JLabel jdbcDriverLbl = new JLabel( "JDBC Driver" );
	JLabel driverLbl = new JLabel( "Driver Name" );
	JLabel idLbl = new JLabel( "ID" );
	JLabel passwdLbl = new JLabel( "Password" );

	// dialog
	ProtocolDialog protocolAdd;

	// CardLayout
	CardLayout topcard = new CardLayout();
	CardLayout bottomcard = new CardLayout();

	JPanel top = new JPanel();
	JPanel bottom =  new JPanel();
	JPanel top1 = new JPanel();
	JPanel top2 = new JPanel();
	JPanel bottom1 = new JPanel();
	JPanel bottom2 = new JPanel();

	/**
	 *  JDBCDialog
	 *
	 *  @param  frame  parent frame
	 *  @param  title  frame title
	 *  @param  modal   modal
	 *  @param  wizard event-handler
	 */
	public JDBCDialog(JFrame frame, String title, boolean modal, JDBCWizard wizard) {
		super(frame, title, modal);
		this.wizard = wizard;
		this.frame = frame;

 		try  {
			top.setLayout( topcard );
			bottom.setLayout( bottomcard );
			jbInit1();
			jbInit2();
			top.add( top1, "First" );
			top.add( top2, "Second" );

			bottom.add( bottom1, "First" );
			bottom.add( bottom2, "Second" );

			getContentPane().add( top, "Center" );
			getContentPane().add( bottom, "South" );
			pack();

			setSize( 330, 365 );
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = this.getSize();
			if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
			if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
			this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

			setResizable(false);
			start();

 	 	}catch (Exception ex) {
			System.out.println( "JDBC constructor"  );
			ex.printStackTrace();
    }
  }


	/**
	 *  jbInit1  -  initialize first card
	 */
	void jbInit1() throws Exception {
	   	//package name input form
   		JPanel panel1 = new JPanel();
   		packLbl.setPreferredSize(new Dimension(100, 20));
   		packName.setPreferredSize(new Dimension(200, 20));
	 		panel1.add( packLbl );
   		panel1.add( packName );
	
   		//class name input form
   		JPanel panel2 = new JPanel();
   		classLbl.setPreferredSize(new Dimension(100, 20));
   		className.setPreferredSize(new Dimension(200, 20));
	 		//panel2.setLayout( new FlowLayout(FlowLayout.LEFT) );
   		panel2.add( classLbl );
   		panel2.add( className );
	
   		//Option
   		JPanel panel4 = new JPanel();
   		panel4.setLayout( new FlowLayout( FlowLayout.LEFT ) );
   		JPanel panel5 = new JPanel();
   		panel5.setLayout( new GridLayout( 3,1 ) );
   		insert.setPreferredSize( new Dimension( 180, 20 ) );
   		delete.setPreferredSize( new Dimension( 180,20 ) );
   		update.setPreferredSize( new Dimension( 180,20 ) );
   		panel5.add( insert );
   		panel5.add( delete );
   		panel5.add( update );
   		panel4.add( panel5 );
    	TitledBorder titled = new TitledBorder( "Option" );
    	panel4.setBorder( titled );

    	JPanel panel6 = new JPanel();
		panel6.setLayout( new BorderLayout() );
   		panel6.add( panel1, "North" );
   		panel6.add( panel2, "South" );
    	//panel6.add( panel4 );

		JLabel label = new JLabel( "" );
		label.setPreferredSize( new Dimension( 20, 20 ) );

		JPanel panel3 = new JPanel( new BorderLayout() );
		panel3.add( new JPanel(), "Center" );
		//panel3.add( label, "North" );
		panel3.add( panel6, "South" );

		JLabel label2 = new JLabel( "");
		label2.setPreferredSize( new Dimension( 20, 10 ) );

		JPanel panel8 = new JPanel( new BorderLayout() );
		panel8.add( new JPanel(), "North" );
		panel8.add( panel3, "Center" );
		panel8.add( label2, "South" );

		JLabel label3 = new JLabel( "");
		label3.setPreferredSize( new Dimension( 35,10 ) );

		JPanel panel9 = new JPanel( new BorderLayout() );
		panel9.add( panel4, "Center" );
    panel9.add( label, "South" );
    
    JPanel panel10 = new JPanel( new BorderLayout() );
    panel10.setBorder( new EtchedBorder() );
    panel10.add( panel9, "Center" );
    panel10.add( new JPanel(), "West" );
    panel10.add( new JPanel(), "East" );
    panel10.add( panel8, "North" );

    top1.setLayout( new BorderLayout() );
    top1.add( new JPanel(), "North" );
    top1.add( panel10, BorderLayout.CENTER );
    top1.add( new JPanel(), BorderLayout.WEST );
    top1.add( new JPanel(), BorderLayout.EAST );
    top1.add( label3, BorderLayout.SOUTH );

		// set event handler
		backx.setEnabled( false );
		next.setActionCommand( "Next" );
		cancel1.setActionCommand( "Cancel" );
		next.addActionListener( wizard );
		cancel1.addActionListener( wizard );

		bottom1.add( backx );
		bottom1.add( next );
		bottom1.add( cancel1 );

//		pack();
//		setResizable( false );
 }


	/**
	 *  jbInit2  -  initialize second dialog
	 */
	void jbInit2() throws Exception {
    	JPanel panel1 = new JPanel();
    	panel1.setLayout( new FlowLayout( FlowLayout.LEFT ) );
  	Vector items = Main.property.getProtocols();
    for (int i = 0; i < items.size(); i++)
    	protocol.addItem((String)items.elementAt(i));
/*
		// open protocol file
		try {
			accessfile = new RandomAccessFile( wizard.path + File.separator + "protocol.txt", "rw" );
			//accessfile = new RandomAccessFile( wizard.path + "protocol.txt", "rw" );
			String line;
			// fill protocol items
			for( line = accessfile.readLine() ; line.length() > 0 ; line = accessfile.readLine() )
				protocol.addItem( line.substring( 0, line.length() - 1 ) );
		} catch( IOException e ) {
			System.out.println( e.toString() );
		} catch( Exception e ) {
		}
*/
   	protocol.setSelectedIndex(0);
   	proLbl.setPreferredSize( new Dimension( 70, 20 ) );
   	protocol.setPreferredSize( new Dimension( 95, 20 ) );

		add.setPreferredSize( new Dimension( 45, 20 ) );
		remove.setPreferredSize( new Dimension( 40, 20 ) );

		// Event handler
		browse.setActionCommand( "Browse" );
		browse.addActionListener( wizard );
		add.setActionCommand( "Add" );
		add.addActionListener( this );
		remove.setActionCommand( "Delete" );
		remove.addActionListener( this );

   	JLabel temp = new JLabel();
   	panel1.add( proLbl );
   	panel1.add( protocol );
   	panel1.add( temp );
   	panel1.add( add );
		panel1.add( remove );


		hostIP.setText( "" );
		hostPort.setText( "" );
		dbName.setText( "" );
		jdbcDriver.setText( "" );
		id.setText( "" );
		passwd.setText( "" );
   	JPanel panel2 = new JPanel();
   	panel2.setLayout( new FlowLayout( FlowLayout.LEFT ) );
   	hostIPLbl.setPreferredSize( new Dimension( 70, 20 ) );
   	hostIP.setPreferredSize( new Dimension( 200, 20 ) );
   	panel2.add( hostIPLbl );
   	panel2.add( hostIP );

   	JPanel panel3 = new JPanel();
   	panel3.setLayout( new FlowLayout( FlowLayout.LEFT ) );
   	hostPortLbl.setPreferredSize( new Dimension( 70, 20 ) );
   	hostPort.setPreferredSize( new Dimension( 200, 20 ) );
   	panel3.add( hostPortLbl );
   	panel3.add( hostPort );

    	JPanel panel4 = new JPanel();
    	panel4.setLayout( new FlowLayout( FlowLayout.LEFT ) );
    	dbNameLbl.setPreferredSize( new Dimension( 70, 20 ) );
    	dbName.setPreferredSize( new Dimension( 200, 20 ) );
    	panel4.add( dbNameLbl );
    	panel4.add( dbName );

    	JPanel panel5 = new JPanel();
    	panel5.setLayout( new FlowLayout( FlowLayout.LEFT ) );
    	jdbcDriverLbl.setPreferredSize( new Dimension( 70, 20 ) );
    	jdbcDriver.setPreferredSize( new Dimension( 200, 20 ) );
		browse.setPreferredSize( new Dimension( 25, 20 ) );
    	panel5.add( jdbcDriverLbl );
    	panel5.add( jdbcDriver );
    	panel5.add( browse );

		JPanel panel0 = new JPanel();
    	panel0.setLayout( new FlowLayout( FlowLayout.LEFT ) );
    	driverLbl.setPreferredSize( new Dimension( 70, 20 ) );
    	driverName.setPreferredSize( new Dimension( 200, 20 ) );
    	panel0.add( driverLbl );
    	panel0.add( driverName );


    	JPanel panel6 = new JPanel();
    	panel6.setLayout( new GridLayout( 6,1 ) );
    	panel6.add( panel1 );
    	panel6.add( panel2 );
    	panel6.add( panel3 );
    	panel6.add( panel4 );
    	panel6.add( panel0 );
    	panel6.add( panel5 );
    	TitledBorder titled1 = new TitledBorder( "Set Protocol" );
    	panel6.setBorder( titled1 );

    	JPanel panel7 = new JPanel();
    	panel7.setLayout( new FlowLayout( FlowLayout.LEFT ) );
    	idLbl.setPreferredSize( new Dimension( 25, 20 ) );
    	id.setPreferredSize( new Dimension( 110, 20 ) );
    	passwdLbl.setPreferredSize( new Dimension( 70, 20 ) );
    	passwd.setPreferredSize( new Dimension( 100, 20 ) );
    	panel7.add( idLbl );
    	panel7.add( id );
    	panel7.add( passwdLbl );
    	panel7.add( passwd );
    	TitledBorder titled2 = new TitledBorder( "Authentification" );
    	panel7.setBorder( titled2 );

    	JPanel panel8 = new JPanel();
    	panel8.setLayout( new GridLayout( 1,1 ) );
    	panel8.add( panel6 );

    	JPanel panel9 = new JPanel();
    	panel9.setLayout( new GridLayout( 1,1 ) );
    	panel9.add( panel7 );

    	JPanel panel10 = new JPanel();
      panel10.setBorder( new EtchedBorder() );
    	panel10.add( panel8 );
    	panel10.add( panel9 );

    	top2.setLayout( new BorderLayout() );
	   	top2.add( panel10, BorderLayout.CENTER );
    	top2.add( new JPanel(), BorderLayout.EAST );
    	top2.add( new JPanel(), BorderLayout.WEST );
    	top2.add( new JPanel(), BorderLayout.NORTH );

		// set event handler
		back.setActionCommand( "Back" );
		finish.setActionCommand( "Finish" );
		cancel2.setActionCommand( "Cancel" );
		back.addActionListener( wizard );
		finish.addActionListener( wizard );
		cancel2.addActionListener( wizard );

		bottom2.add( back );
		bottom2.add( finish );
		bottom2.add( cancel2 );

//		pack();
//		setResizable( false );
	}


	public void actionPerformed( ActionEvent ae ) {
		// analyze event source
		String cmd = ae.getActionCommand();

		if( cmd.equals( "Add" ) ) {
			protocolAdd = new ProtocolDialog( frame, "Protocol Addition", true, this );
			protocolAdd.setVisible( true );
    } else if ( cmd.equals( "Delete" ) ) {
   	 	int index = protocol.getSelectedIndex();
			if( index != 0 ) {
        Main.property.removeJDBCProtocol((String)protocol.getSelectedItem());
      	protocol.removeItem(protocol.getSelectedItem());
				IdePropertyManager.saveProperty(Main.property);
      /*
				try {
        			protocol.removeItem( protocol.getSelectedItem() );
        			accessfile.seek( 0 );

   	     			String spare = "";

        			for( int i = 0; i < index ; i++ ) {
          				spare = spare + accessfile.readLine() + "\n";
        			}

   	     			long offset = accessfile.getFilePointer();
        			System.out.println( accessfile.readLine() );

        			while( accessfile.getFilePointer() != accessfile.length() ) {
          				spare = spare + accessfile.readLine() + "\n" ;
        			}
        			File temp = new File( wizard.path, "protocol.txt" );
        			FileOutputStream os = new FileOutputStream( temp );

        			os.write( spare.getBytes() );

        			os.close();
      			} catch( IOException e ) {

      			} catch( Exception e ) {

      			}
        */
			}
		} else if( cmd.equals( "OK" ) ) {
			protocolAdd.setVisible( false );
			String input = protocolAdd.getProtocol();
			if( !input.equals( "") ) {
        Main.property.removeJDBCProtocol(input);
      	protocol.addItem(input);
				IdePropertyManager.saveProperty(Main.property);
/*
				try {
					accessfile.seek( accessfile.length() );
					accessfile.writeBytes( input + "\n" );
					protocol.addItem( input );
				} catch( IOException e ) {
					System.out.println( e.toString() );
				}
*/
			}
		}
	}


	/**
	 *  start
	 */
	public void start() {
		topcard.show( top, "First" );
		bottomcard.show( bottom, "First" );

	}

	/**
	 *  previous  -  show previous dialog
	 */
	public void previous() {
		topcard.previous(top );
		bottomcard.previous(bottom);
	}

	/**
	 *  next  -  show next dialog
	 */
	public void next() {
		topcard.next(top );
		bottomcard.next( bottom );
	}


	/**
	 *  clear
	 */
	public void clear() {
		// first dialog
		className.setText( "Jdbc" );
		packName.setText( "jdbc" );
		insert.setSelected( false );
		delete.setSelected( false );
		update.setSelected( false );

		// second dialog
		protocol.setSelectedIndex( 0 );
		hostIP.setText( "" );
		hostPort.setText( "" );
		dbName.setText( "" );
		jdbcDriver.setText( "");
		driverName.setText( "" );
		id.setText( "" );
		passwd.setText( "" );


		try {
			accessfile.close();
		} catch( IOException e ) {
			System.out.println( e.toString() );
		}
	}		
}

