/*
 * $Header: /AntIDE/source/ant/manager/projectmanager/OutputDialog.java 2     99-05-17 12:20a Multipia $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 2 $
 */
package com.antsoft.ant.manager.projectmanager;

import java.awt.*;
import javax.swing.*;

import com.antsoft.ant.main.*;

/**
 *  class OutputDialog
 *
 *  @author Jinwoo Baek
 */
public class OutputDialog extends JDialog {
  OutputPanel output = new OutputPanel(this);
  BorderLayout borderLayout1 = new BorderLayout();
  MainFrame mainFrm = null;

	/**
	 *  Constructor
	 */
  public OutputDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    if (frame instanceof MainFrame) mainFrm = (MainFrame)frame;
    try  {
      jbInit();
      pack();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

	/**
	 *  Constructor
	 */
  public OutputDialog() {
    this(null, "", false);
  }

  void jbInit() throws Exception {
    getContentPane().setLayout(borderLayout1);
    getContentPane().add(output, BorderLayout.CENTER);
  }

	/**
	 *  output������ �ִ´�.
	 */
  public void setText(String text) {
    if (output != null) output.setText(text);
  }

	/**
	 *  Output������ �����Ѵ�.
	 */
  public void clearText() {
    if (output != null) output.clear();
  }

	/**
	 *  ��� ������Ʈ�� ������� �����Ѵ�
	 */
  public void setPE(ProjectExplorer pe) {
  	if (output != null) output.setPE(pe);
  }

	/**
	 *  �� ���̾�α׸� �ݴ´�.
	 */
  public void closeDlg() {
  	setVisible(false);
  }

	/**
	 *  ��� �г��� ��´�.
	 */
  public OutputPanel getOutput() {
  	return output;
  }
}

