/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/pool/sourcepool/MsgBox.java,v 1.3 1999/07/22 03:06:59 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * $History: MsgBox.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-17   Time: 12:22a
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 
 * *****************  Version 1  *****************
 * User: Insane       Date: 98-09-12   Time: 5:18p
 * Created in $/Ant/src/ant/sourcepool
 * 소스 관리자
 *
 */
package com.antsoft.ant.pool.sourcepool;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 *  class MsgBox
 *
 *  @author Jinwoo Baek
 */
public class MsgBox extends JDialog {
  static final int YES = 0;
  static final int NO = 1;
  static final int CANCEL = 2;

  JPanel panel1 = new JPanel();
  JPanel btnPanel = new JPanel();
  JLabel lblMsg = new JLabel();
  JButton yesButton = new JButton("Yes");
  JButton noButton = new JButton("No");
  JButton cancelButton = new JButton("Cancel");
  int btnState;

  BorderLayout borderLayout1 = new BorderLayout();

	/**
	 *  Constructor
	 */
  public MsgBox(Frame frame, String title) {
    super(frame, title, true);
    try  {
      jbInit();
      pack();
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension dlgSize = this.getSize();
      if (dlgSize.height > screenSize.height)
        dlgSize.height = screenSize.height;
      if (dlgSize.width > screenSize.width)
        dlgSize.width = screenSize.width;
      this.setLocation((screenSize.width - dlgSize.width) / 2, (screenSize.height - dlgSize.height) / 2);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    btnPanel.setLayout(new GridLayout(1, 3));
    yesButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        yesButton_actionPerformed(e);
      }
    });
    noButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        noButton_actionPerformed(e);
      }
    });
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancelButton_actionPerformed(e);
      }
    });
    btnPanel.add(yesButton);
    btnPanel.add(noButton);
    btnPanel.add(cancelButton);
    panel1.add(lblMsg , BorderLayout.CENTER);
    panel1.add(btnPanel, BorderLayout.SOUTH);
    this.getContentPane().add(panel1);
    panel1.setPreferredSize(new Dimension(300, 100));
  }

	/**
	 *  메시지를 설정한다.
	 */
  void setMsg(String msg) {
    lblMsg.setText(msg);
  }

	/** Event Handler */
  void yesButton_actionPerformed(ActionEvent e) {
    this.setVisible(false);
    btnState = YES;
  }

  void noButton_actionPerformed(ActionEvent e) {
    this.setVisible(false);
    btnState = NO;
  }

  void cancelButton_actionPerformed(ActionEvent e) {
    this.setVisible(false);
    btnState = CANCEL;
  }

	/** 어느 버튼이 눌렸나? */
  int whichButton() {
    return btnState;
  }
}

