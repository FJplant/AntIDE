/*
 * $Id: ServletOptionPanel.java,v 1.6 1999/08/25 03:28:14 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Lee Chul Mok
 */
package com.antsoft.ant.runner;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.util.FontList;
import com.antsoft.ant.property.*;

/**
 * Servlet Path Panel
 *
 * @autoor Lee Chul Mok
 */
public class ServletOptionPanel extends JPanel {
    private JTextField portTfl,logTfl,maxTfl,timeoutTfl,directoryTfl,propertyTfl;
    private JButton portBtn,logBtn,maxBtn,timeoutBtn,directoryBtn,propertyBtn;
    private JFrame parent;
    public static IdeProperty property;

    public ServletOptionPanel(JFrame parent) {
      this.parent = parent;
      property = IdePropertyManager.loadProperty();
      try {
        jbInit();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }

    void jbInit() throws Exception {
      setLayout(new BorderLayout());
      setBorder(BorderList.etchedBorder5);

      ActionListener al = new ActionHandler();

      // port panel
      JLabel portLbl = new JLabel(" -p  port", JLabel.LEFT);
      portLbl.setPreferredSize(new Dimension(340,20));

      JPanel portLblP  = new JPanel(new FlowLayout(FlowLayout.LEFT));
      portLblP.add(portLbl);

      portTfl = new JTextField(20);
      portTfl.setPreferredSize(new Dimension(340,20));
      portBtn = new JButton();
      portBtn.setPreferredSize(new Dimension(20,20));
      portBtn.setActionCommand("PORT");
      portBtn.addActionListener(al);

      portTfl.setText(property.getServletPort()); // default value;
      portBtn.setEnabled(false);

      JPanel portP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      portP.add(portTfl);
      portP.add(portBtn);

      // backup log panel
      JLabel logLbl = new JLabel(" -b  backup log", JLabel.LEFT);
      logLbl.setPreferredSize(new Dimension(340,20));

      JPanel logLblP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      logLblP.add(logLbl);

      logTfl = new JTextField(20);
      logTfl.setPreferredSize(new Dimension(340,20));
      logBtn = new JButton();
      logBtn.setPreferredSize(new Dimension(20,20));
      logBtn.setActionCommand("LOG");
      logBtn.addActionListener(al);

      logTfl.setText(property.getServletLog()); // default value;
      logBtn.setEnabled(false);

      JPanel logP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      logP.add(logTfl);
      logP.add(logBtn);

      // max handlers
      JLabel maxLbl = new JLabel(" -m  max handlers", JLabel.LEFT);
      maxLbl.setPreferredSize(new Dimension(340,20));

      JPanel maxLblP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      maxLblP.add(maxLbl);

      maxTfl = new JTextField(20);
      maxTfl.setPreferredSize(new Dimension(340,20));
      maxBtn = new JButton();
      maxBtn.setPreferredSize(new Dimension(20,20));
      maxBtn.setActionCommand("MAX");
      maxBtn.addActionListener(al);

      maxTfl.setText(property.getServletMax()); // default value;
      maxBtn.setEnabled(false);

      JPanel maxP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      maxP.add(maxTfl);
      maxP.add(maxBtn);

      // timeout
      JLabel timeoutLbl = new JLabel(" -t  timeout", JLabel.LEFT);
      timeoutLbl.setPreferredSize(new Dimension(340,20));

      JPanel timeoutLblP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      timeoutLblP.add(timeoutLbl);

      timeoutTfl = new JTextField(20);
      timeoutTfl.setPreferredSize(new Dimension(340,20));
      timeoutBtn = new JButton();
      timeoutBtn.setPreferredSize(new Dimension(20,20));
      timeoutBtn.setActionCommand("TIMEOUT");
      timeoutBtn.addActionListener(al);

      timeoutTfl.setText(property.getServletTimeout()); // default value;
      timeoutBtn.setEnabled(false);

      JPanel timeoutP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      timeoutP.add(timeoutTfl);
      timeoutP.add(timeoutBtn);

      // directory
      JLabel directoryLbl = new JLabel(" -d  document directory", JLabel.LEFT);
      directoryLbl.setPreferredSize(new Dimension(300,20));

      JPanel directoryLblP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      directoryLblP.add(directoryLbl);

      directoryTfl = new JTextField(20);
      directoryTfl.setPreferredSize(new Dimension(340,20));
      directoryBtn = new JButton("..");
      directoryBtn.setPreferredSize(new Dimension(20,20));
      directoryBtn.setActionCommand("DIRECTORY");
      directoryBtn.addActionListener(al);

      directoryTfl.setText(property.getServletDirectory()); // default value;
      //directoryBtn.setEnabled(false);

      JPanel directoryP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      directoryP.add(directoryTfl);
      directoryP.add(directoryBtn);

      // servlet property
      JLabel propertyLbl = new JLabel(" -s  servlet property", JLabel.LEFT);
      propertyLbl.setPreferredSize(new Dimension(300,20));

      JPanel propertyLblP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      propertyLblP.add(propertyLbl);

      propertyTfl = new JTextField(20);
      propertyTfl.setPreferredSize(new Dimension(300,20));
      propertyBtn = new JButton("..");
      propertyBtn.setPreferredSize(new Dimension(20,20));
      propertyBtn.setActionCommand("PROPERTY");
      propertyBtn.addActionListener(al);

      propertyTfl.setText(property.getServletProperty()); // default value;

      JPanel propertyP = new JPanel(new FlowLayout(FlowLayout.LEFT));
      propertyP.add(propertyTfl);
      propertyP.add(propertyBtn);

      Box box = Box.createVerticalBox();
      box.add(portLblP);
      box.add(portP);
      box.add(logLblP);
      box.add(logP);
      box.add(maxLblP);
      box.add(maxP);
      box.add(timeoutLblP);
      box.add(timeoutP);
      box.add(directoryLblP);
      box.add(directoryP);
      box.add(propertyLblP);
      box.add(propertyP);

      add(box, BorderLayout.CENTER);
      add(new JPanel(), BorderLayout.SOUTH);
    }

  // Option의 값을 읽어 낼수 있는 함수.
  public String getPort() {
    return portTfl.getText();
  }

  public String getLog() {
    return logTfl.getText();
  }

  public String getMax() {
    return maxTfl.getText();
  }

  public String getTimeout() {
    return timeoutTfl.getText();
  }

  public String getDirectory() {
    return directoryTfl.getText();
  }

  public String getProperty() {
    return propertyTfl.getText();
  }

    // 버튼이 눌려 졌을때 발생하는 이벤트 핸들러

  public void portBtnSelected() {
  }

  public void logBtnSelected() {
  }

  public void maxBtnSelected() {
  }

  public void timeoutBtnSelected() {
  }

  public void directoryBtnSelected() {
  }

  public void propertyBtnSelected() {
  }

  class ActionHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String cmd = e.getActionCommand();

      if (cmd.equals("PORT")) {
        portBtnSelected();
      }
      else if (cmd.equals("LOG")) {
        logBtnSelected();
      }
      else if (cmd.equals("MAX")) {
        maxBtnSelected();
      }
      else if (cmd.equals("TIMEOUT")) {
        timeoutBtnSelected();
      }
      else if (cmd.equals("DIRECTORY")) {
        directoryBtnSelected();
      }
      else if (cmd.equals("PROPERTY")) {
        propertyBtnSelected();
      }
    }
  }
}
/*
 * $Log: ServletOptionPanel.java,v $
 * Revision 1.6  1999/08/25 03:28:14  multipia
 * 소스 정리
 *
 */
