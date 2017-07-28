/*
 * $Header: /AntIDE/source/ant/wizard/AppletDialog.java 8     99-05-17 12:03a Multipia $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 8 $
 */
package com.antsoft.ant.wizard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * AppletDialog
 * @author YunKyung Kim
 */
public class AppletDialog extends JDialog implements ActionListener {
	// CardLayout
	CardLayout topcard;
	CardLayout bottomcard;

	// Outer Panel
	JPanel card1 = new JPanel();
	JPanel card2 = new JPanel();
	JPanel card3 = new JPanel();
	JPanel bottom1 = new JPanel();
	JPanel bottom2 = new JPanel();
	JPanel bottom3 = new JPanel();
	JPanel top = new JPanel();
	JPanel bottom = new JPanel();

	// First Dialog Resources
	JLabel packLbl = new JLabel( "Package Name" );
	JTextField packName = new JTextField( "applet", 18 );

	JLabel clsLbl = new JLabel( "Class Name" );
	JTextField className = new JTextField( "Applet1", 18 );


	JCheckBox usingSwing = new JCheckBox( "Use Swing Classes" );
	JCheckBox canStandalone = new JCheckBox( "Can run standalone" );

	JButton back1 = new JButton( "<-Back" );
	JButton next1 = new JButton( "Next->" );
	JButton cancel1 = new JButton( "Cancel" );
	JButton back2 = new JButton( "<-Back" );
	JButton next2 = new JButton( "Next->" );
	JButton cancel2 = new JButton( "Cancel" );
	JButton back3 = new JButton( "<-Back" );
	JButton finish = new JButton( "Finish" );
	JButton cancel3 = new JButton( "Cancel" );

	// Second Dialog Resources
	//Table
	JTable table = null;
	String[] colNames = { "Name", "Value", "Variable" };
	DefaultTableModel model = new DefaultTableModel(colNames, 1);

	//Button
	JButton add = new JButton( "Add Parameter" );
	JButton delete = new JButton( "Delete Parameter" );

	// Third Dialog Resources
	//Label
	JLabel titleLbl = new JLabel( "Title" );
	JLabel nameLbl = new JLabel( "Name" );
	JLabel codeBaseLbl = new JLabel( "CodeBase" );
	JLabel widthLbl = new JLabel( "Width" );
	JLabel heightLbl = new JLabel( "Height" );
	JLabel hspaceLbl = new JLabel( "Hspace" );
	JLabel vspaceLbl = new JLabel( "Vspace" );
	JLabel alignLbl = new JLabel( "Align" );

	//TextField
	JTextField title = new JTextField( "Untitled", 19 );
	JTextField name = new JTextField( 19 );
	JTextField codeBase = new JTextField( 19 );
	JTextField width = new JTextField( 4 );
	JTextField height = new JTextField( 4 );
	JTextField vspace = new JTextField( 4 );
	JTextField hspace = new JTextField( 4 );
	JComboBox align = new JComboBox();

	// 	Event handler
	AppletWizard handler;
	
	/**
 	*  AppletDialog  -  constructor
 	*/
	public AppletDialog(Frame frame, String title, boolean modal, AppletWizard wizard) {
		super(frame, title, modal);
		this.handler = wizard;

		try  {
			jbInit1();
			jbInit2();
			jbInit3();
			// ActionEvent Handler
			back1.setEnabled( false );
			back2.setActionCommand( "Back" );
			back3.setActionCommand( "Back" );
			next1.setActionCommand( "Next" );
			next2.setActionCommand( "Next" );
			cancel1.setActionCommand( "Cancel" );
			cancel2.setActionCommand( "Cancel" );
			cancel3.setActionCommand( "Cancel" );
			finish.setActionCommand( "Finish" );
			back2.addActionListener( handler );
			back3.addActionListener( handler );
			next1.addActionListener( handler );
			next2.addActionListener( handler );
			cancel1.addActionListener( handler );
			cancel2.addActionListener( handler );
			cancel3.addActionListener( handler );
			finish.addActionListener( handler );

			// top panel	
			topcard = new CardLayout();	
			top.setLayout( topcard );
			top.add( card1, "First" );
			top.add( card2, "Second" );
			top.add( card3, "Third" );
			// bottom panel
			bottomcard = new CardLayout();
			bottom.setLayout( bottomcard  );
			bottom1.setLayout( new FlowLayout( FlowLayout.CENTER ) );
			bottom2.setLayout( new FlowLayout( FlowLayout.CENTER ) );
			bottom3.setLayout( new FlowLayout( FlowLayout.CENTER ) );

			bottom1.add( back1 );
			bottom1.add( next1 );
			bottom1.add( cancel1 );

			bottom2.add( back2 );
			bottom2.add( next2 );
			bottom2.add( cancel2 );

			bottom3.add( back3 );
			bottom3.add( finish );
			bottom3.add( cancel3 );

			bottom.add( bottom1, "First" );
			bottom.add( bottom2, "Second" );
			bottom.add( bottom3, "Third" );
			getContentPane().add( top, "Center" );
			getContentPane().add( bottom, "South" );
			//pack();

      setSize(360, 320);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = this.getSize();
      if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
      if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
      this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

      setResizable(false);
      start();
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
	}


	/**
	 *  jbInit1  -  initialize first card
	 */
	void jbInit1() throws Exception {
		//package name input form
		JPanel panel1 = new JPanel();
		packLbl.setPreferredSize(new Dimension(100, 20));
		packName.setPreferredSize(new Dimension(280, 20));
		panel1.add( packLbl );
		panel1.add( packName );

		//class name input form
		JPanel panel2 = new JPanel();
		clsLbl.setPreferredSize(new Dimension(100, 20));
		className.setPreferredSize(new Dimension(280, 20));
		panel2.add( clsLbl );
		panel2.add( className );
		
		//Option
		JPanel panel4 = new JPanel();
		panel4.setLayout( new FlowLayout( FlowLayout.LEFT ) );
		JPanel panel5 = new JPanel();
		panel5.setLayout( new GridLayout( 2,1 ) );
		usingSwing.setPreferredSize( new Dimension( 150, 20 ) );
		canStandalone.setPreferredSize( new Dimension( 150,20 ) );
		panel5.add( usingSwing );
		panel5.add( canStandalone );
    //panel5.add( new JPanel() );
		panel4.add( panel5 );
		TitledBorder titled = new TitledBorder( "Option" );
		panel4.setBorder( titled );
		
		JPanel panel6 = new JPanel();
		panel6.setLayout( new BorderLayout() );
		panel6.add( panel1, "North" );
		panel6.add( panel2, "South" );
		//panel6.add( panel4 );
		
		JPanel panel3 = new JPanel( new BorderLayout() );
		panel3.add( new JPanel(), "Center" );
		panel3.add( new JPanel(), "North" );
		panel3.add( panel6, "South" );

    JLabel label1 = new JLabel("" );
    label1.setPreferredSize( new Dimension( 20, 10 ) );

		JPanel panel8 = new JPanel( new BorderLayout() );
		panel8.add( new JPanel(), "North" );
		panel8.add( new JPanel(), "South" );
		panel8.add( panel3, "Center" );

		JPanel panel7 = new JPanel();
		panel7.setLayout( new BorderLayout() );
		panel7.add( panel4, BorderLayout.CENTER );
		panel7.add( new JPanel(), BorderLayout.WEST );
		panel7.add( new JPanel(), BorderLayout.EAST );
		panel7.add( panel8, BorderLayout.NORTH );
    panel7.add( new JPanel(), "South" );
		panel7.setBorder( new EtchedBorder() );

    card1.setLayout( new BorderLayout() );
    card1.add( label1, "North" );
    card1.add( new JPanel(), "South" );
    card1.add( new JPanel(), "East" );
    card1.add( new JPanel(), "West" );
		card1.add( panel7, "Center" );
	}


	/**
	 *  jbInit2  -  initialize second card
	 */
	void jbInit2() throws Exception {
		table = new JTable();
    table.setModel(model);
   	table.setRowHeight(20);
		model.setValueAt( "", 0, 0 );
		model.setValueAt( "", 0, 1 );
		model.setValueAt( "", 0, 2 );

    JScrollPane sp = new JScrollPane(table);
    sp.setPreferredSize( new Dimension( 300, 170 ) );
    JPanel panel1 = new JPanel();
    panel1.setLayout( new BorderLayout(5,5) );
    panel1.add( sp, BorderLayout.CENTER );
    JPanel panel2 = new JPanel();

		// event handler
		add.setActionCommand( "AddRow" );
		delete.setActionCommand( "DeleteRow" );
		add.addActionListener( this );
		delete.addActionListener( this );

   	panel2.add( add );
   	panel2.add( delete );
   	panel1.add( panel2, BorderLayout.SOUTH );
   	TitledBorder titled = new TitledBorder( "HTML Parameter" );
   	panel1.setBorder( titled );

    JPanel panel4 = new JPanel();
    panel4.setBorder( new EtchedBorder() );
    panel4.add( panel1 );
  	card2.setLayout(new BorderLayout());

   	card2.add( panel4, BorderLayout.CENTER );
    card2.add( new JPanel(), "South" );
    card2.add( new JPanel(), "North" );
    card2.add( new JPanel(), "East" );
    card2.add( new JPanel(), "West" );
	}


	/**
	 *  jbInit3  -  initialize third card
	 */
	void jbInit3() throws Exception {
		JPanel panel0 = new JPanel();
 		titleLbl.setPreferredSize(new Dimension(60, 20));
   		title.setPreferredSize(new Dimension(240, 20));
		title.setText( "" );
   		panel0.setLayout( new FlowLayout( FlowLayout.LEFT ) );
   		panel0.add( titleLbl );
   		panel0.add( title );

		JPanel panel1 = new JPanel();
   		nameLbl.setPreferredSize(new Dimension(60, 20));
   		name.setPreferredSize(new Dimension(240, 20));
		name.setText( "" );
		panel1.setLayout( new FlowLayout( FlowLayout.LEFT ) );
   		panel1.add( nameLbl );
   		panel1.add( name );

  		JPanel panel2 = new JPanel();
  		codeBaseLbl.setPreferredSize(new Dimension(60, 20));
  		codeBase.setPreferredSize(new Dimension(240, 20));
		codeBase.setText( "" );
  		panel2.setLayout( new FlowLayout( FlowLayout.LEFT ) );
  		panel2.add( codeBaseLbl );
  		panel2.add( codeBase );

  		JPanel panel3 = new JPanel();
  		widthLbl.setPreferredSize(new Dimension(50, 20));
  		width.setPreferredSize(new Dimension(60, 20));
		width.setText( "" );
  		JLabel temp1 = new JLabel();
  		temp1.setPreferredSize( new Dimension(30,20) );
  		heightLbl.setPreferredSize(new Dimension(60, 20));
  		height.setPreferredSize(new Dimension(50, 20));
		height.setText( "" );
  		panel3.setLayout( new FlowLayout( FlowLayout.LEFT ) );
  		panel3.add( heightLbl );
  		panel3.add( height );
  		panel3.add( temp1 );
  		panel3.add( widthLbl );
 		panel3.add( width );

  		JPanel panel4 = new JPanel();
  		vspaceLbl.setPreferredSize(new Dimension(50, 20));
  		vspace.setPreferredSize(new Dimension(60, 20));
		vspace.setText( "" );
  		JLabel temp2 = new JLabel();
  		temp2.setPreferredSize( new Dimension(30,20) );
  		hspaceLbl.setPreferredSize(new Dimension(60, 20));
  		hspace.setPreferredSize(new Dimension(60, 20));
		hspace.setText( "" );
  		panel4.setLayout( new FlowLayout( FlowLayout.LEFT ) );
  		panel4.add( hspaceLbl );
  		panel4.add( hspace );
  		panel4.add( temp2 );
  		panel4.add( vspaceLbl );
  		panel4.add( vspace );

  		JPanel panel5 = new JPanel();
  		alignLbl.setPreferredSize(new Dimension(60, 20));
  		align.setPreferredSize(new Dimension(100, 20));
  		panel5.setLayout( new FlowLayout( FlowLayout.LEFT ) );
  		panel5.add( alignLbl );
  		ComboBoxModel model = align.getModel();
  		align.addItem( "Left" );
  		align.addItem( "Center" );
  		align.addItem( "Right" );
  		panel5.add( align );

  		JPanel panel6 = new JPanel();
  		panel6.setLayout( new FlowLayout( FlowLayout.LEFT ) );
  		panel6.add( panel0 );
  		panel6.add( panel1 );
  		panel6.add( panel2 );
  		panel6.add( panel3 );
  		panel6.add( panel4 );
  		panel6.add( panel5 );

  		TitledBorder titled = new TitledBorder( "HTML Page" );
  		panel6.setBorder( titled );

  		JPanel panel8 = new JPanel();
      panel8.setBorder( new EtchedBorder() );
  		panel8.setLayout( new BorderLayout() );
  		panel8.add( panel6, BorderLayout.CENTER );
  		panel8.add( new JPanel(), BorderLayout.SOUTH );

  		card3.setLayout(new BorderLayout());

  		card3.add( panel8, BorderLayout.CENTER );
  		card3.add( new JPanel(), BorderLayout.EAST );
  		card3.add( new JPanel(), BorderLayout.WEST );
  		card3.add( new JPanel(), BorderLayout.NORTH );
		//pack();
		//setResizable(false);
  	}

	/**
	 *  start - applet wizard starging
	 */
	public void start() {
		topcard.show( top, "First" );
		bottomcard.show( bottom, "First" );
	}

	/**
	 *  next  -  show next dialog
	 */
	public void next( ) {
		topcard.next( top );
		bottomcard.next( bottom );
	}


	/**
	 *  previous  -  show privious dialog
	 */
	public void previous( ) {
		topcard.previous( top );
		bottomcard.previous( bottom);
	}


	/**
	 *  clear  - clear resources
	 */
	public void clear() {
		// JTextField
		packName.setText( "applet" );
		className.setText( "Applet1" );
		title.setText( "" );
		name.setText( "" );
		codeBase.setText( "" );
		width.setText( "" );
		height.setText( "" );
		vspace.setText( "" );
		hspace.setText( "" );
		
		// JCheckBox
		usingSwing.setSelected( false );
		canStandalone.setSelected( false );
		
		// JTable
		for( int row = model.getRowCount() - 1 ; row > 0 ; row-- ) 
			model.removeRow( row  );
		
		model.setValueAt( "", 0, 0 );
		model.setValueAt( "", 0, 1 );
		model.setValueAt( "", 0, 2 );	

		align.setSelectedIndex( 0 );
	}

	/**
	 *  actionPercformed  -  ActionEvent handler
	 *
	 *  @param  ae  ActionEvent
	 */
	public void actionPerformed( ActionEvent ae ) {
		// analyze event source
		String cmd = ae.getActionCommand();

		if( cmd.equals( "AddRow" ) ) {
			model.addRow( colNames );
			model.setValueAt( "", model.getRowCount() - 1, 0 );
			model.setValueAt( "", model.getRowCount() - 1, 1 );
			model.setValueAt( "", model.getRowCount() - 1, 2 );
		} else if ( cmd.equals( "DeleteRow" ) ) {
			if( table.getRowCount() > 1 ) { 
				int selected =  table.getSelectedRowCount();

				if( selected == 0 ) {
					model.setValueAt( "", table.getRowCount() - 1, 0 );
					model.setValueAt( "", table.getRowCount() - 1, 1 );
					model.setValueAt( "", table.getRowCount() - 1, 2 );
					model.removeRow( table.getRowCount() - 1 );
				} else if ( selected == 1 ) {
					int rows[] = table.getSelectedRows();
					model.setValueAt( "", rows[0], 0 );
					model.setValueAt( "", rows[0], 1 );
					model.setValueAt( "", rows[0], 2 );
					model.removeRow( rows[0] );	
					if( table.getRowCount() == rows[0]  ) { 
						//table.setRowSelectionInterval( rows[0] - 1, rows[0] - 1 );
						table.setEditingRow( rows[0] - 1 );
						table.setEditingColumn( 0 );
					}	
					
				} else { 
					int rows[] = table.getSelectedRows();
					boolean delete = false;
					if(  table.getRowCount() > rows.length ) delete = true;

					for( int i = 0 ; i < rows.length ; i++ ) { 
						if( !delete  && ( i == ( rows.length - 1 ) ) ) {
							model.setValueAt( "", 0, 0 );
							model.setValueAt( "", 0, 1 );
							model.setValueAt( "", 0, 2 );
								
						} else {
							model.setValueAt( "", rows[i] - i, 0 );
							model.setValueAt( "", rows[i] - i, 1 );
							model.setValueAt( "", rows[i] - i, 2 );		
							model.removeRow( rows[i] - i );
						}
					}
				}
			} else if(table.getRowCount() == 1 ) {
				model.setValueAt( "", 0, 0 );
				model.setValueAt( "", 0, 1 );
				model.setValueAt( "", 0, 2 );	
			}
				
		}
	}

}
	
