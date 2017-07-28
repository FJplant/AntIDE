/*
 * $Id: ProjectRunParamDlg.java,v 1.6 1999/08/31 04:57:39 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.6 $
 */
package com.antsoft.ant.manager.projectmanager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.antsoft.ant.util.WindowDisposer;

/**
 *  class ProjectRunParamDlg
 *
 *  @author Jinwoo Baek
 */
public class ProjectRunParamDlg extends JDialog implements ActionListener {
	JLabel lbl = new JLabel("Runtime parameter");
  JTextField tf = new JTextField(18);
  JCheckBox cb = new JCheckBox("Use parameter", false);
  JButton okBtn = new JButton("Ok");
  JButton cancelBtn = new JButton("Cancel");

  boolean isOk = false;

  /**
   *  Constructor
   */
	public ProjectRunParamDlg(JFrame owner, String title, boolean modal) {
  	super(owner, title, modal);
                                       
		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer());
		
		uiInit();
  }

	protected void uiInit() {
    okBtn.addActionListener(this);
    cancelBtn.addActionListener(this);

    lbl.setPreferredSize(new Dimension(200,20));
    tf.setPreferredSize(new Dimension(200,20));
    cb.setPreferredSize(new Dimension(200,20));
    
    JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p1.add(lbl);

    JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p2.add(tf);

    JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p3.add(cb);

    JPanel p4 = new JPanel();
    p4.add(okBtn);
    p4.add(cancelBtn);

    JPanel p5 = new JPanel(new GridLayout(3,1));
    p5.add(p1);
    p5.add(p2);
    p5.add(p3);

    JPanel p6 = new JPanel();
    p6.setBorder(new EtchedBorder());
    p6.add(p5);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(p6,BorderLayout.CENTER);
    getContentPane().add(p4,BorderLayout.SOUTH);
    getContentPane().add(new JPanel(),BorderLayout.EAST);
    getContentPane().add(new JPanel(),BorderLayout.WEST);
    getContentPane().add(new JPanel(),BorderLayout.NORTH);

    setSize(260,180);
    this.setResizable(false);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height)
      dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width)
      dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2,
                (screenSize.height - dlgSize.height) / 2);
	}
	
  public boolean isOk() {
  	return isOk;
  }

  public boolean isUsable() {
  	return cb.isSelected();
  }

  public void setUsable(boolean b) {
  	cb.setSelected(b);
  }

  public String getParams() {
  	return tf.getText();
  }

  public void setParams(String param) {
  	if (tf != null) tf.setText(param);
  }

  public void actionPerformed(ActionEvent evt) {
  	if (evt.getSource() == okBtn) {
    	dispose();
//    	setVisible(false);
    	isOk = true;
    }
    else if (evt.getSource() == cancelBtn) {
    	dispose();
//    	setVisible(false);
    	isOk = false;
    }
  }
}
/*
 * $Log: ProjectRunParamDlg.java,v $
 * Revision 1.6  1999/08/31 04:57:39  multipia
 * no message
 *
 */
