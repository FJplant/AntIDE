/*
 * $Header: /AntIDE/source/ant/wizard/ServletDialog.java 6     99-05-17 12:03a Multipia $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 6 $
 */
package com.antsoft.ant.wizard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author YunKyung Kim
 */
public class ServletDialog extends JDialog implements ActionListener, ItemListener{

  //flag
  boolean isOK = false;
  boolean flag1 = false;
  boolean flag2 = false;
  boolean flag3 = false;
  boolean flag4 = false;
  boolean flag5 = false;
  
  //Label
  JLabel packNameLbl = new JLabel( "Package Name" );
  JLabel classNameLbl = new JLabel( "Class Title" );

  //TextField
  JTextField packName = new JTextField( "Servlet", 20 );
  JTextField className = new JTextField( "Servlet1", 20 );

  //CheckBox
  JCheckBox usingSwing = new JCheckBox( "Use Swing Classes" );
  JCheckBox standalone = new JCheckBox( "Can Standalone" );

  JCheckBox doGet = new JCheckBox( "doGet() method" );
  JCheckBox doPost = new JCheckBox( "doPost() method" );
  JCheckBox service = new JCheckBox( "service() method" );
  JCheckBox doPut = new JCheckBox( "doPut() method" );
  JCheckBox doDelete = new JCheckBox( "doDelete() method" );

  //border
  EtchedBorder border = new EtchedBorder();

  //Button
  JButton finishBtn = new JButton( "Finish" );
  JButton cancelBtn = new JButton( "Cancel" );

  public ServletDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
      setSize(400, 300);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = this.getSize();
      if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
      if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
      this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

      setResizable( false );
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  public ServletDialog() {
    this(null, "Servlet Wizard", true);
  }

  void jbInit() throws Exception {
  //frame class name input form
    JPanel panel1 = new JPanel();
    packNameLbl.setPreferredSize(new Dimension(100, 20));
    packName.setPreferredSize(new Dimension(300, 20));
    panel1.setLayout( new FlowLayout( FlowLayout.LEFT) );
    panel1.add( packNameLbl );
    panel1.add( packName );

    //frame title name input form
    JPanel panel2 = new JPanel();
    classNameLbl.setPreferredSize(new Dimension(100, 20));
    className.setPreferredSize(new Dimension(300, 20));
    panel2.setLayout( new FlowLayout(FlowLayout.LEFT) );
    panel2.add( classNameLbl );
    panel2.add( className );

    //Button
    JPanel panel3 = new JPanel();
    panel3.setLayout( new FlowLayout( FlowLayout.CENTER ) );
    //panel3.add( backBtn );
    panel3.add( finishBtn );
    panel3.add( cancelBtn );
    finishBtn.addActionListener(this);
    cancelBtn.addActionListener(this);

    //Servlet Method Select
    JPanel panel6 = new JPanel();
    panel6.setLayout( new FlowLayout( FlowLayout.LEFT ) );
    JPanel panel7 = new JPanel();
    panel7.setLayout( new GridLayout( 5,1 ) );
    doGet.setPreferredSize( new Dimension( 310, 20 ) );
    doPost.setPreferredSize( new Dimension( 310,20 ) );
    service.setPreferredSize( new Dimension( 310,20 ) );
    doGet.addItemListener(this);
    doPost.addItemListener(this);
    service.addItemListener(this);
    doPut.addItemListener(this);
    doDelete.addItemListener(this);

    doPut.setPreferredSize( new Dimension( 310,20 ) );
    doDelete.setPreferredSize( new Dimension( 310,20 ) );

    panel7.add( doGet );
    panel7.add( doPost );
    panel7.add( service );
    panel7.add( doPut );
    panel7.add( doDelete );

    panel6.add( panel7 );
    TitledBorder titled2 = new TitledBorder( "Generate Servlet Method" );
    panel6.setBorder( titled2 );

    JPanel panel8 = new JPanel();
    panel8.setBorder( border );
    panel8.add( panel1 );
    panel8.add( panel2 );
    panel8.add( panel6 );

    JPanel panel9 = new JPanel();
    panel9.setLayout( new BorderLayout() );
    panel9.add( panel8, BorderLayout.CENTER );
    panel9.add( panel3, BorderLayout.SOUTH );
    panel9.add( new JPanel(), BorderLayout.WEST );
    panel9.add( new JPanel(), BorderLayout.EAST );
    panel9.add( new JPanel(), BorderLayout.NORTH );
                                                
    getContentPane().add( panel9 );
  }
                                             
  boolean isOK(){
    return isOK;
  }

  String getPackName(){
    return packName.getText();
  }

  String getClassName(){
    return className.getText();
  }

  boolean getDoget(){
    return doGet.isSelected();
  }

  boolean getDopost(){
    return doPost.isSelected();
  }

  boolean getService(){
    return service.isSelected();
  }

  boolean getDoput(){
    return doPut.isSelected();
  }

  boolean getDodelete(){
    return doDelete.isSelected();
  }

  // event처리------------------------------------------------------
  public void actionPerformed( ActionEvent e ){

    if( e.getSource() == finishBtn ){
      //package이름이 없을 경우 에러메시지
      if( (packName.getText() == null) || (packName.getText().trim().equals("")) ){
        JOptionPane.showMessageDialog(this, "Invalid Package Name", "Warning", JOptionPane.ERROR_MESSAGE);
      }
      //클래스 이름이 없을 경우 에러메시지
      else if( (className.getText() == null) || (className.getText().trim().equals("")) ){
        JOptionPane.showMessageDialog(this, "Invalid Class Name", "Warning", JOptionPane.ERROR_MESSAGE);
      }
      else{
        isOK = true;
        dispose();
      }
    }else if( e.getSource() == cancelBtn ){
      dispose();
    }
  }

  public void itemStateChanged( ItemEvent e ){

    if((e.getSource() == service) && service.isSelected() ){
      if (doGet.isSelected()) doGet.setSelected( false );
      if (doPost.isSelected()) doPost.setSelected( false );
      if (doPut.isSelected()) doPut.setSelected( false );
      if (doDelete.isSelected()) doDelete.setSelected( false );

      doGet.setEnabled( false );
      doPost.setEnabled( false );
      doPut.setEnabled( false );
      doDelete.setEnabled( false );

    }else{
      if (e.getSource() == service) {
        if ( flag1 ) doGet.setSelected( true );
        if ( flag2 ) doPost.setSelected( true );
        if ( flag3 ) doPut.setSelected( true );
        if ( flag4 ) doDelete.setSelected( true );

        doGet.setEnabled( true );
        doPost.setEnabled( true );
        doPut.setEnabled( true );
        doDelete.setEnabled( true );
      }

      else if (!service.isSelected()) {
        if (e.getSource() == doGet) {
          if( doGet.isSelected() ) flag1 = true;
          else flag1 = false;
        }

        if (e.getSource() == doPost) {
          if( doPost.isSelected() ) flag2 = true;
          else flag2 = false;
        }

        if (e.getSource() == doPut) {
          if( doPut.isSelected() ) flag3 = true;
          else flag3 = false;
        }

        if (e.getSource() == doDelete) {
          if( doDelete.isSelected() ) flag4 = true;
          else flag4 = false;
        }
      }
    }
  }
}

