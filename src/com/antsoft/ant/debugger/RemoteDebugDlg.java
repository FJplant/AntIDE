
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
import javax.swing.border.TitledBorder;

/**
 *  class RemoteDebugDlg
 *
 *  리모트 디버깅을 지원하기위한 다이얼로그
 *
 *  @author Jinwoo Baek
 */
public class RemoteDebugDlg extends JDialog implements ActionListener, WindowListener {
  // Components
  JPanel pl1 = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JPanel pl2 = new JPanel();
  JPanel pl3 = new JPanel();
  JLabel hostLbl = new JLabel();
  private JTextField hostTf = new JTextField();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  JLabel pwLbl = new JLabel();
  private JPasswordField pwTf = new JPasswordField();
  JPanel jPanel1 = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JLabel jvmArgLbl = new JLabel();
  private JTextField jvmArgTf = new JTextField();
  JPanel pl4 = new JPanel();
  JButton okBtn = new JButton();
  JButton cancelBtn = new JButton();

  // Data Members
  private boolean isOk;

  /**
   *  Constructor
   */
  public RemoteDebugDlg(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public RemoteDebugDlg() {
    this(null, "", false);
  }

  void jbInit() throws Exception {
    pl1.setLayout(gridLayout1);
    gridLayout1.setRows(3);
    hostLbl.setText("Hostname(IP) :");
    hostTf.setPreferredSize(new Dimension(200, 23));
    pl2.setLayout(borderLayout1);
    pl3.setLayout(borderLayout2);
    pwLbl.setPreferredSize(new Dimension(83, 23));
    pwLbl.setText("Password :");
    pwTf.setPreferredSize(new Dimension(200, 23));
    jPanel1.setLayout(borderLayout3);
    jvmArgLbl.setPreferredSize(new Dimension(83, 23));
    jvmArgLbl.setText("JVM Args :");
    jvmArgTf.setPreferredSize(new Dimension(200, 23));
    okBtn.setMargin(new Insets(0, 2, 0, 2));
    okBtn.setText("Ok");
    cancelBtn.setMargin(new Insets(0, 2, 0, 2));
    cancelBtn.setText("Cancel");
    getContentPane().add(pl1);
    getContentPane().setLayout(new BorderLayout());
    pl1.add(pl2, null);
    pl2.add(hostTf, BorderLayout.CENTER);
    pl2.add(hostLbl, BorderLayout.WEST);
    pl1.add(pl3, null);
    pl3.add(pwLbl, BorderLayout.WEST);
    pl3.add(pwTf, BorderLayout.CENTER);
    pl1.add(jPanel1, null);
    jPanel1.add(jvmArgLbl, BorderLayout.WEST);
    jPanel1.add(jvmArgTf, BorderLayout.CENTER);
//    pl1.add(pl4, null);
    pl4.add(okBtn, null);
    pl4.add(cancelBtn, null);
    pl1.setBorder(new TitledBorder("Remote Debug Info"));
    getContentPane().add(pl1, BorderLayout.CENTER);
    getContentPane().add(pl4, BorderLayout.SOUTH);

    okBtn.addActionListener(this);
    cancelBtn.addActionListener(this);

    this.addWindowListener(this);
  }

  public boolean isOk() {
    return isOk;
  }

  // 데이터 멤버에 대한 접근 함수들
  public String getHostName() {
    return hostTf.getText();
  }

  public void setHostName(String host) {
    hostTf.setText(host);
  }

  public String getPassword() {
    return new String(pwTf.getPassword());
  }

  public void setPassword(String pw) {
    pwTf.setText(pw);
  }

  public String getJVMArgs() {
    return jvmArgTf.getText();
  }

  public void setJVMArgs(String args) {
    jvmArgTf.setText(args);
  }

  public void actionPerformed(ActionEvent e) {
    //TODO: implement this java.awt.event.ActionListener method;
    if (e.getSource() == okBtn) {
      if (hostTf.getText().trim().equals(""))
        JOptionPane.showMessageDialog(this, "Host is not valid");
      else {
        dispose();
        isOk = true;
      }
    } else if (e.getSource() == cancelBtn) {
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

