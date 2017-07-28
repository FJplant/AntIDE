/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/codeeditor/GotoLineDlg.java,v 1.3 1999/07/22 04:57:31 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 *  라인 이동하기 대화상자
 */

package com.antsoft.ant.designer.codeeditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 *  class GotoLineDlg
 *
 *  @author Jinwoo Baek
 */
public class GotoLineDlg extends JDialog implements ActionListener {
	private JLabel lbl = new JLabel("Line to go");
  private JTextField tf = new JTextField();
  private JButton okBtn = new JButton("Ok");
  private JButton cancelBtn = new JButton("Cancel");

  private boolean isOk = false;
  /**
   *  Constructor
   */
	public GotoLineDlg(Frame owner) {
  	super(owner, "Go to Line...", true);
    JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    //p1.setBorder(new EtchedBorder());
    tf.setPreferredSize(new Dimension(150, 20));
    p1.add(lbl);
    p1.add(tf);

    okBtn.addActionListener(this);
    cancelBtn.addActionListener(this);

    tf.addKeyListener(new KeyAdapter() {
    	public void keyPressed(KeyEvent evt) {
      	if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
        	isOk = false;
          setVisible(false);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        	isOk = true;
        	setVisible(false);
        }
      }
    });
    addKeyListener(new KeyAdapter() {
    	public void keyPressed(KeyEvent evt) {
      	if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
        	isOk = false;
          setVisible(false);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        	isOk = true;
        	setVisible(false);
        }
      }
    });
    JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
    p2.add(okBtn);
    p2.add(cancelBtn);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(p1,BorderLayout.CENTER);
    getContentPane().add(p2,BorderLayout.SOUTH);
    getContentPane().add(new JPanel(),BorderLayout.WEST);
    getContentPane().add(new JPanel(),BorderLayout.EAST);
    getContentPane().add(new JPanel(),BorderLayout.NORTH);

    this.setSize(250,100);
    this.setResizable(false);
    tf.requestFocus();
  }

  public boolean isOk() {
	 	return isOk;
  }

  public int getLineNumber() {
  	if ((tf.getText() != null) && !tf.getText().equals(""))
	  	return Integer.parseInt(tf.getText());

    return -1;
  }

  public void actionPerformed(ActionEvent evt) {
  	if (evt.getSource() == okBtn) {
    	isOk = true;
    	setVisible(false);
    }
    else if (evt.getSource() == cancelBtn) {
    	isOk = false;
    	setVisible(false);
    }
  }
}

