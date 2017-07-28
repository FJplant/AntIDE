/*
 * $Id: FrameDialog.java,v 1.2 1999/08/19 05:25:07 multipia Exp $
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

/*
 *  FrameDialog - Frame wizard dialog
 *  designed by Kim Yun Kyung
 *  date 1999.7.12
 */
public class FrameDialog extends JDialog implements ActionListener, KeyListener{
  public JTextField frameName;
  public JTextField packName;
  public JCheckBox createConst;
  public JCheckBox useSwing;
  public JCheckBox createMain;
  private JButton ok;
  private JButton cancel;
  private boolean isOK = false;

  public FrameDialog(Frame frame,String title,boolean modal) {
    super(frame,title,true);
    try  {
      jInit();
      pack();
      setSize(350, 270);
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
    JLabel fNameLbl = new JLabel("Frame Name");
    fNameLbl.setPreferredSize(new Dimension(100,20));
    frameName = new JTextField("Frame1",17);
    frameName.addKeyListener(this);
    p2.add(fNameLbl);
    p2.add(frameName);

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

    JPanel createMainP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    createMain = new JCheckBox("Create main method");
    createMain.setPreferredSize(new Dimension(280,20));
    createMainP.add(createMain);

    JPanel p6 = new JPanel();
    p6.setLayout(new GridLayout(3,1));
    p6.setBorder(new BlackTitledBorder("Option"));
    p6.add(p4);
    p6.add(p5);
    p6.add(createMainP);

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

  /////////////////////// event Ã³¸®  //////////////////////////////////
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
    if(e.getSource() == frameName){
      if(!(frameName.getText().equals("")) && !(frameName.getText()==null))
        ok.setEnabled(true);
      else ok.setEnabled(false);
    }
  }
  public void keyPressed(KeyEvent e){
  }
  public void keyTyped(KeyEvent e){
  }
}
