
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Jinwoo Baek
//Company:      Antsoft
//Description:  Your description

package com.antsoft.ant.debugger;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 *  class RunParamDlg
 *
 *  디버거에서 목적 프로그램 실행시 커멘드라인 파라미터를 받기 위한 다이얼로그
 *
 *  @author Jinwoo Baek
 */
public class RunParamDlg extends JDialog implements ActionListener, WindowListener {
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel pl1 = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JTextField paramTf = new JTextField();
//  JCheckBox useChk = new JCheckBox();
  TitledBorder titledBorder1;
  JPanel jPanel1 = new JPanel();
  JButton okBtn = new JButton();
  JButton cancelBtn = new JButton();
  private boolean isOk;

  /**
   *  Constructor
   */
  public RunParamDlg(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public RunParamDlg() {
    this(null, "", false);
  }

  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("Run Parameter");
    panel1.setLayout(borderLayout1);
    pl1.setLayout(gridLayout1);
    paramTf.setPreferredSize(new Dimension(200, 25));
//    useChk.setText("Use Param");
//    gridLayout1.setRows(2);
    pl1.setBorder(titledBorder1);
    okBtn.setMargin(new Insets(0, 2, 0, 2));
    okBtn.setText("Ok");
    cancelBtn.setMargin(new Insets(0, 2, 0, 2));
    cancelBtn.setText("Cancel");
    getContentPane().add(panel1);
    panel1.add(pl1, BorderLayout.CENTER);
    pl1.add(paramTf, null);
//    pl1.add(useChk, null);
    panel1.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(okBtn, null);
    jPanel1.add(cancelBtn, null);

    okBtn.addActionListener(this);
    cancelBtn.addActionListener(this);
    addWindowListener(this);
    setResizable(false);
  }

  public void setParamText(String str) {
    if (str != null) paramTf.setText(str);
  }

  public String getParamText() {
    return paramTf.getText();
  }

  public boolean isOk() {
    return isOk;
  }

//  public boolean getUseParam() {
//    return useChk.isSelected();
//  }

  public void actionPerformed(ActionEvent evt) {
    if (evt.getSource() == okBtn) {
      // 목적 프로젝트 코드 실행부분에 쓰일 수 있도록 한다.
      dispose();
      isOk = true;
    } else if (evt.getSource() == cancelBtn) {
      // 취소
      dispose();
      isOk = false;
    }
  }

  public void windowOpened(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowClosing(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
    dispose();
    isOk = false;
  }

  public void windowClosed(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowIconified(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowDeiconified(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowActivated(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowDeactivated(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }
}
