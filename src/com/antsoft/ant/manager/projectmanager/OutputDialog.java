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
	 *  output내용을 넣는다.
	 */
  public void setText(String text) {
    if (output != null) output.setText(text);
  }

	/**
	 *  Output내용을 삭제한다.
	 */
  public void clearText() {
    if (output != null) output.clear();
  }

	/**
	 *  어느 프로젝트의 출력인지 설정한다
	 */
  public void setPE(ProjectExplorer pe) {
  	if (output != null) output.setPE(pe);
  }

	/**
	 *  현 다이얼로그를 닫는다.
	 */
  public void closeDlg() {
  	setVisible(false);
  }

	/**
	 *  출력 패널을 얻는다.
	 */
  public OutputPanel getOutput() {
  	return output;
  }
}

