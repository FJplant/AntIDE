/*
 * $Id: DialogforDialog.java,v 1.2 1999/08/19 05:25:07 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.2 $
 */
package com.antsoft.ant.wizard.generalwizard;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import com.antsoft.ant.util.*;

/**
 *  DialogforDialog - 다이얼로그 생성을 위한 위저드 다이어로그
 *  designed by Kim Yun Kyung
 *  date 1999.7.13
 */
public class DialogforDialog extends JDialog implements ActionListener, KeyListener{
	public JTextField dialogName;
  public JTextField packName;
  public JCheckBox createConst;
  public JCheckBox useSwing;
  private JButton ok;
  private JButton cancel;
  private boolean isOK = false;

  public DialogforDialog(Frame parent,String title,boolean modal) {
    super(parent,title,modal);
    try  {
      jInit();
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

  private void jInit(){
		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer());

    JPanel p1 = new JPanel();
    p1.setLayout(new FlowLayout(FlowLayout.LEFT));
    JLabel pNameLbl = new JLabel("Package Name");
    pNameLbl.setPreferredSize(new Dimension(100,20));
    packName = new JTextField(17);
//    packName.addKeyListener(this);
    p1.add(pNameLbl);
    p1.add(packName);

    JPanel p2 = new JPanel();
    p2.setLayout(new FlowLayout(FlowLayout.LEFT));
    JLabel dNameLbl = new JLabel("Dialog Name");
    dNameLbl.setPreferredSize(new Dimension(100,20));
    dialogName = new JTextField("Dialog1",17);
    dialogName.addKeyListener(this);
    p2.add(dNameLbl);
    p2.add(dialogName);

    JPanel p3 = new JPanel();
    p3.setLayout(new GridLayout(2,1));
    p3.add(p1);
    p3.add(p2);

    JPanel p4 = new JPanel();
    p4.setLayout(new FlowLayout(FlowLayout.LEFT));
    useSwing = new JCheckBox("Use Swing");
    useSwing.setPreferredSize(new Dimension(280,20));
    p4.add(useSwing);

    JPanel p5 = new JPanel();
    p5.setLayout(new FlowLayout(FlowLayout.LEFT));
    createConst = new JCheckBox("Create default constructor");
    createConst.setPreferredSize(new Dimension(280,20));
    p5.add(createConst);


    JPanel p6 = new JPanel();
    p6.setLayout(new GridLayout(2,1));
    p6.setBorder(new BlackTitledBorder("Option"));
    p6.add(p4);
    p6.add(p5);

    JPanel p7 = new JPanel();
    p7.setLayout(new FlowLayout(FlowLayout.CENTER));
    p7.setBorder(new EtchedBorder());
    p7.add(p3);
    p7.add(p6);

    JPanel p8 = new JPanel();
    p8.setLayout(new FlowLayout(FlowLayout.CENTER));
    ok = new JButton("OK");
//    ok.setEnabled(false);
    cancel = new JButton("Cancel");
    ok.addActionListener(this);
    cancel.addActionListener(this);
    p8.add(ok);
    p8.add(cancel);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(p7,BorderLayout.CENTER);
    getContentPane().add(p8,BorderLayout.SOUTH);
    getContentPane().add(new JPanel(),BorderLayout.NORTH);
    getContentPane().add(new JPanel(),BorderLayout.WEST);
    getContentPane().add(new JPanel(),BorderLayout.EAST);
  }

  public boolean isOK(){
    return isOK;
  }

  /////////////////////// event 처리  //////////////////////////////////
  public void actionPerformed(ActionEvent e){
    try{
      if(e.getSource() == ok){
        isOK = true;
      }
      dispose();
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  public void keyReleased(KeyEvent e){
    /*if(e.getSource() == packName){
      if(!(packName.getText().equals("")) && !(packName.getText()==null))
        ok.setEnabled(true);
      else ok.setEnabled(false);
    }else*/ if(e.getSource() == dialogName){
      if(!(dialogName.getText().equals("")) && !(dialogName.getText()==null))
        ok.setEnabled(true);
      else ok.setEnabled(false);
    }
  }
  public void keyPressed(KeyEvent e){
  }
  public void keyTyped(KeyEvent e){
  }
}
