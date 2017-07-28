
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Jinwoo Baek
//Company:      Antsoft
//Description:  Your description

package com.antsoft.ant.debugger;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.*;

/**
 *  class ExceptionSelDlg
 *
 *  @author Jinwoo Baek
 */
public class ExceptionSelDlg extends JDialog implements ActionListener, WindowListener {
  // Components
  JPanel pl1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel pl2 = new JPanel();
  JScrollPane scroller = new JScrollPane();
  JButton okBtn = new JButton();
  JButton cancelBtn = new JButton();
  TitledBorder titledBorder1;
  JList exList = new JList();
  JTextField usrTf;

  // Data Members
  private boolean isOk;

  public ExceptionSelDlg(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public ExceptionSelDlg() {
    this(null, "", false);
  }

  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("Available Exceptions");
    pl1.setLayout(borderLayout1);
    okBtn.setMargin(new Insets(0, 2, 0, 2));
    okBtn.setText("Ok");
    cancelBtn.setMargin(new Insets(0, 2, 0, 2));
    cancelBtn.setText("Cancel");
    scroller.setBorder(titledBorder1);
    getContentPane().add(pl1);
    pl1.add(pl2, BorderLayout.SOUTH);

    pl2.add(okBtn, null);
    pl2.add(cancelBtn, null);

    scroller.getViewport().add(exList, null);
    JPanel pl4 = new JPanel(new BorderLayout());
    JLabel usrLbl = new JLabel("Exception Name : ");
    usrTf = new JTextField();
    pl4.add(usrLbl, BorderLayout.WEST);
    pl4.add(usrTf, BorderLayout.CENTER);

    JPanel pl3 = new JPanel(new BorderLayout());
    pl3.add(scroller, BorderLayout.CENTER);
    pl3.add(pl4, BorderLayout.SOUTH);
    pl3.setBorder(new EtchedBorder());

    pl1.add(pl3, BorderLayout.CENTER);

    exList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    okBtn.addActionListener(this);
    cancelBtn.addActionListener(this);
  }

  // ∫∏ø©¡Ÿ Exception class List
  public void setAvailableExceptions(Vector objs) {
    exList.setListData(objs);
  }

  public boolean isOk() {
    return isOk;
  }

  public Object[] getSelected() {
    return exList.getSelectedValues();
  }

  public String getUsrException() {
    return usrTf.getText();
  }

  public void actionPerformed(ActionEvent e) {
    //TODO: implement this java.awt.event.ActionListener method;
    if (e.getSource() == okBtn) {
      dispose();
      isOk = true;
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

