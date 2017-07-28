/*
 * $Id: ClassDialog.java,v 1.2 1999/08/19 05:25:07 multipia Exp $
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

import com.antsoft.ant.util.WindowDisposer;

/*
 *  ClassDialog - 클래스 위저드 다이얼로그
 *  designed by Kim Yun Kyung
 *  date 1999.7.13
 */
public class ClassDialog extends JDialog implements ActionListener, KeyListener{
  public JTextField packName;
  public JTextField className;
  public JTextField baseClass;
  public JCheckBox createConst;
  private JButton ok;
  private JButton cancel;
  private boolean isOK = false;

  public ClassDialog(Frame parent, String title, boolean modal) {
    super(parent, title, modal);
    try  {
      jInit();
      pack();
      setSize(350, 230);
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

    JLabel pNameLbl = new JLabel("Package Name");
    pNameLbl.setPreferredSize(new Dimension(100,20));
    packName = new JTextField(17);
//    packName.addKeyListener(this);
    JPanel p1 = new JPanel();
    p1.setLayout(new FlowLayout(FlowLayout.LEFT));
    p1.add(pNameLbl);
    p1.add(packName);

    JLabel cNameLbl = new JLabel("Class Name");
    cNameLbl.setPreferredSize(new Dimension(100,20));
    className = new JTextField("MyClass",17);
    className.addKeyListener(this);
    JPanel p2 = new JPanel();
    p2.setLayout(new FlowLayout(FlowLayout.LEFT));
    p2.add(cNameLbl);
    p2.add(className);

    JLabel baseLbl = new JLabel("Base Class");
    baseLbl.setPreferredSize(new Dimension(100,20));
    baseClass = new JTextField("java.lang.Object",17);
    JPanel p3 = new JPanel();
    p3.setLayout(new FlowLayout(FlowLayout.LEFT));
    p3.add(baseLbl);
    p3.add(baseClass);

    createConst = new JCheckBox("Create default constructor");
    createConst.setPreferredSize(new Dimension(280,20));
    JPanel p4 = new JPanel();
    p4.setLayout(new FlowLayout(FlowLayout.LEFT));
    p4.add(createConst);

    JPanel p5 = new JPanel();
    p5.setLayout(new FlowLayout(FlowLayout.CENTER));
    p5.setBorder(new EtchedBorder());
    p5.add(p1);
    p5.add(p2);
    p5.add(p3);
    p5.add(p4);

    ok = new JButton("OK");
    cancel = new JButton("Cancel");
//    ok.setEnabled(false);
    ok.addActionListener(this);
    cancel.addActionListener(this);
    JPanel p6 = new JPanel();
    p6.setLayout(new FlowLayout(FlowLayout.CENTER));
    p6.add(ok);
    p6.add(cancel);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(p5,BorderLayout.CENTER);
    getContentPane().add(p6,BorderLayout.SOUTH);
    getContentPane().add(new JPanel(),BorderLayout.NORTH);
    getContentPane().add(new JPanel(),BorderLayout.EAST);
    getContentPane().add(new JPanel(),BorderLayout.WEST);
  }

  public boolean isOK(){
    return isOK;
  }

  //////////// event 처리  ///////////////////////////////////////////////
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
    }else*/ if(e.getSource() == className){
      if(!(className.getText().equals("")) && !(className.getText()==null))
        ok.setEnabled(true);
      else ok.setEnabled(false);
    }
  }
  public void keyPressed(KeyEvent e){
  }
  public void keyTyped(KeyEvent e){
  }
}
