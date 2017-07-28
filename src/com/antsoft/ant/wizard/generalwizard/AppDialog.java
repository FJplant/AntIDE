/*
 * $Id: AppDialog.java,v 1.2 1999/08/19 05:25:07 multipia Exp $
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
import javax.swing.border.*;

import com.antsoft.ant.util.*;

/**
 * @author Youngkyung Kim
 */
public class AppDialog extends JDialog implements ActionListener, KeyListener{
  boolean isOK = false;
  CardLayout card = new CardLayout();
  JPanel panel12 = new JPanel();
  JLabel packLbl = new JLabel( "Package Name" );
  JTextField packName = new JTextField(17);
  JLabel clsLbl = new JLabel( "Class Name" );
  JTextField className = new JTextField("App1", 17 );
  EtchedBorder border = new EtchedBorder();
  JCheckBox usingSwing = new JCheckBox( "Use Swing Classes" );
  JCheckBox canStandalone = new JCheckBox( "Can run standalone" );

  //Label
  JLabel fNameLbl = new JLabel( "Frame Class Name" );
  JLabel fTitleLbl = new JLabel( "Frame Title" );

  //TextField
  JTextField fName = new JTextField( "Frame1", 17 );
  JTextField fTitle = new JTextField( "My frame", 17 );

  //CheckBox
  JCheckBox menuBar = new JCheckBox( "Menu Bar" );
//  JCheckBox toolBar = new JCheckBox( "Tool Bar" );
//  JCheckBox statusBar = new JCheckBox( "Status Bar" );
  JCheckBox aboutBox = new JCheckBox( "About Box" );

  JButton backBtn = new JButton( "<-Back" );
  JButton nextBtn = new JButton( "Next->" );
  JButton cancelBtn = new JButton( "Cancel" );
  JButton finishBtn = new JButton( "Finish" );

  public AppDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
      setSize(350, 240);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = this.getSize();
      if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
      if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
      this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
      setResizable(false);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public AppDialog() {
    this(null, "Application Wizard", true);
  }

  void jbInit() throws Exception {
		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer());

    //package name input form
    JPanel panel1 = new JPanel();
    packLbl.setPreferredSize(new Dimension(110, 20));
    //packName.setPreferredSize(new Dimension(250, 20));
    panel1.setLayout( new FlowLayout( FlowLayout.LEFT) );
//    packName.addKeyListener(this);
    panel1.add( packLbl );
    panel1.add( packName );

    //class name input form
    JPanel panel2 = new JPanel();
    clsLbl.setPreferredSize(new Dimension(110, 20));
//    className.setPreferredSize(new Dimension(250, 20));
    panel2.setLayout( new FlowLayout(FlowLayout.LEFT) );
    className.addKeyListener(this);
    panel2.add( clsLbl );
    panel2.add( className );

    //Option
    JPanel panel4 = new JPanel();
    panel4.setLayout( new FlowLayout( FlowLayout.LEFT ) );

    JPanel panel5 = new JPanel();
    panel5.setLayout( new GridLayout( 1,1 ) );
    usingSwing.setPreferredSize( new Dimension( 280, 20 ) );
    panel5.add( usingSwing );
    panel4.add( panel5 );
    BlackTitledBorder titled1 = new BlackTitledBorder( "Option" );
    panel4.setBorder( titled1 );

    JPanel panel6 = new JPanel();
    panel6.setBorder( border );
    panel6.add( panel1 );
    panel6.add( panel2 );
    panel6.add( panel4 );

    //frame class name input form
    JPanel panel7 = new JPanel();
    fNameLbl.setPreferredSize(new Dimension(110, 20));
//    fName.setPreferredSize(new Dimension(250, 20));
    panel7.setLayout( new FlowLayout( FlowLayout.LEFT) );
    fName.addKeyListener(this);
    panel7.add( fNameLbl );
    panel7.add( fName );

    //frame title name input form
    JPanel panel8 = new JPanel();
    fTitleLbl.setPreferredSize(new Dimension(110, 20));
//    fTitle.setPreferredSize(new Dimension(250, 20));
    panel8.setLayout( new FlowLayout(FlowLayout.LEFT) );
    panel8.add( fTitleLbl );
    panel8.add( fTitle );

    //Option
    JPanel panel9 = new JPanel();
    panel9.setLayout( new FlowLayout( FlowLayout.LEFT ) );
    JPanel panel10 = new JPanel();
    panel10.setLayout( new GridLayout( 2,1 ) );
    menuBar.setPreferredSize( new Dimension( 280, 20 ) );
//    toolBar.setPreferredSize( new Dimension( 280,20 ) );
//    statusBar.setPreferredSize( new Dimension( 280,20 ) );
    aboutBox.setPreferredSize( new Dimension( 280,20 ) );

    panel10.add( menuBar );
//    panel10.add( toolBar );
//    panel10.add( statusBar );
    panel10.add( aboutBox );

    panel9.add( panel10 );
    BlackTitledBorder titled2 = new BlackTitledBorder( "Option" );
    panel9.setBorder( titled2 );

    JPanel panel11 = new JPanel();
    panel11.setBorder( border );
    panel11.add( panel7 );
    panel11.add( panel8 );
    panel11.add( panel9 );

    panel12.setLayout( card );
    panel12.add( panel6, "app" );
    panel12.add( panel11, "frame");

    //Button
    JPanel panel13 = new JPanel();
    panel13.setLayout( new FlowLayout( FlowLayout.CENTER ) );
    panel13.add( backBtn );
    backBtn.setEnabled( false );
    panel13.add( nextBtn );
    //nextBtn.setEnabled(false);
    panel13.add( finishBtn );
    finishBtn.setEnabled( false );
    panel13.add( cancelBtn );
    nextBtn.addActionListener(this);
    cancelBtn.addActionListener(this);
    finishBtn.addActionListener(this);
    backBtn.addActionListener(this);

    JPanel panel14 = new JPanel();
    panel14.setLayout( new BorderLayout() );
    panel14.add( panel12, BorderLayout.CENTER );
    panel14.add( panel13, BorderLayout.SOUTH );
    panel14.add( new JPanel(), BorderLayout.WEST );
    panel14.add( new JPanel(), BorderLayout.EAST );
    panel14.add( new JPanel(), BorderLayout.NORTH );

    getContentPane().add( panel14 );
    card.show(panel12, "app");
  }

  public String getPackName(){
    return packName.getText();
  }
  public String getClassName(){
    return className.getText();
  }
  public String getFrameName(){
    return fName.getText();
  }
  public String getFrameTitle(){
    return fTitle.getText();
  }
  public boolean getUsingSwing(){
    return usingSwing.isSelected();
  }
  public boolean getCanStandalone(){
    return canStandalone.isSelected();
  }
  public boolean getMenuBar(){
    return menuBar.isSelected();
  }
/*  public boolean getToolBar(){
    if( getUsingSwing() )
      return toolBar.isSelected();
    else return false;
  }
  public boolean getStatusBar(){
    return statusBar.isSelected();
  }*/
  public boolean getAboutBox(){
    return aboutBox.isSelected();
  }

  public boolean isOK(){
    return isOK;
  }

  //event Ã³¸® -------------------------------------------------------
  public void actionPerformed( ActionEvent e ){
    if( e.getSource() == nextBtn ){
      /*if( !getUsingSwing() ){
        toolBar.setEnabled( false );
      }else{
        toolBar.setEnabled( true );
      }*/
      card.show(panel12, "frame");
      nextBtn.setEnabled(false);
      backBtn.setEnabled(true);
      finishBtn.setEnabled(true);
    }
    else if (e.getSource() == backBtn) {
      card.show(panel12, "app");
      nextBtn.setEnabled(true);
      backBtn.setEnabled(false);
      finishBtn.setEnabled(false);
    }
    else if (e.getSource() == finishBtn) {
      isOK = true;
      dispose();
    }
    else if ( e.getSource() == cancelBtn ){
      isOK = false;
      dispose();
    }
  }

  public void keyReleased(KeyEvent e){
    /*if(e.getSource() == packName){
      if(!(packName.getText().equals("")) && !(packName.getText()==null))
        nextBtn.setEnabled(true);
      else nextBtn.setEnabled(false);
    }else*/ if(e.getSource() == className){
      if(!(className.getText().equals("")) && !(className.getText()==null))
        nextBtn.setEnabled(true);
      else nextBtn.setEnabled(false);
    }else if(e.getSource() == fName){
      if(!(fName.getText().equals("")) && !(fName.getText()==null))
        finishBtn.setEnabled(true);
      else finishBtn.setEnabled(false);
    }
  }
  public void keyPressed(KeyEvent e){
  }
  public void keyTyped(KeyEvent e){
  }
}
