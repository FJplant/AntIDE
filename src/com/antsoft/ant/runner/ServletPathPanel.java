/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/runner/ServletPathPanel.java,v 1.4 1999/07/22 08:38:43 lila Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Lee Chul Mok
 * $History: ServletPathPanel.java $
 *
 * *****************  Version 7  *****************
 * User: Remember     Date: 99-05-31   Time: 9:59a
 * Updated in $/AntIDE/source/ant/runner
 * 
 * *****************  Version 6  *****************
 * User: Itree        Date: 99-05-26   Time: 4:59p
 * Updated in $/AntIDE/source/ant/runner
*/
package com.antsoft.ant.runner;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;

import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.util.FontList;
import com.antsoft.ant.util.ExtensionFileFilter;
import com.antsoft.ant.property.*;

/**
 * Servlet Path Panel
 *
 * @autoor Lee Chul Mok
 */

public class ServletPathPanel extends JPanel {
  private JCheckBox batchCbx;
  private JTextField runnerPath, webBrowserPath;
  private JButton runnerBtn, webBrowserBtn;

  private JFrame parent;
  public static IdeProperty property;

  public ServletPathPanel(JFrame parent) {
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

    // servlet runner panel
    JLabel runnerLbl = new JLabel("Servlet Runner Path", JLabel.LEFT);
    runnerLbl.setPreferredSize(new Dimension(340,20));

    JPanel runnerLblP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    runnerLblP.add(runnerLbl);

    runnerPath = new JTextField(20);
    runnerPath.setPreferredSize(new Dimension(340,20));
    runnerPath.setText(property.getServletRunnerPath());
    runnerBtn = new JButton("..");
    runnerBtn.setPreferredSize(new Dimension(20,20));
    runnerBtn.setActionCommand("PATH");
    runnerBtn.addActionListener(al);

    JPanel runnerPathP = new JPanel(new FlowLayout(FlowLayout.LEFT));    runnerPathP.add(runnerPath);
    runnerPathP.add(runnerBtn);

    // web Browser Path panel
    JLabel webBrowserLbl = new JLabel("Web Browser Program Path", JLabel.LEFT);
    webBrowserLbl.setPreferredSize(new Dimension(340,20));

    JPanel webBrowserLblP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    webBrowserLblP.add(webBrowserLbl);

    webBrowserPath = new JTextField(20);
    webBrowserPath.setPreferredSize(new Dimension(340,20));
    webBrowserPath.setText(property.getServletWebBrowserPath());
    webBrowserBtn = new JButton("..");
    webBrowserBtn.setPreferredSize(new Dimension(20,20));
    webBrowserBtn.setActionCommand("WEB");
    webBrowserBtn.addActionListener(al);

    JPanel webBrowserP = new JPanel(new FlowLayout(FlowLayout.LEFT));    webBrowserP.add(webBrowserPath);
    webBrowserP.add(webBrowserBtn);

    // for making batch file option with checkbox
    ChangeListener cl = new ChangeEventHandler();

    batchCbx = new JCheckBox("Make batch file for RUN without Ant");    if(!property.getMakingBatch())
      batchCbx.setSelected(false);
    else
      batchCbx.setSelected(true);

    batchCbx.addChangeListener(cl);
    JPanel batchCbxP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    batchCbxP.add(batchCbx);

    Box box = Box.createVerticalBox();    box.add(runnerLblP);
    box.add(runnerPathP);
    box.add(webBrowserLblP);
    box.add(webBrowserP);
    box.add(batchCbxP);

    add(box,BorderLayout.NORTH);    add(new JPanel(), BorderLayout.CENTER);

  }

  /**
   * source root select button handler
   */
  public void pathBtnSelected(){

    ExtensionFileFilter filter = new ExtensionFileFilter();
    filter.addExtension("exe");
    filter.addExtension("bat");

    JFileChooser filechooser = new JFileChooser();
    filechooser.setFileFilter(filter);
    filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    filechooser.setMultiSelectionEnabled(false);
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("Select Servletrunner Program");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("Servletrunner (ex:servletrunner.exe)");

    if(filechooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();
      runnerPath.setText(f.getAbsolutePath());
    }
  }

  private void webBrowserBtnSelected(){

    ExtensionFileFilter filter = new ExtensionFileFilter();
    filter.addExtension("exe");

    JFileChooser filechooser = new JFileChooser();
    filechooser.setFileFilter(filter);
    filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    filechooser.setMultiSelectionEnabled(false);
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("Select Web Browser Program");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("Web Browser (IE or Netscape) ");

    if(filechooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();
      webBrowserPath.setText(f.getAbsolutePath());
    }
  }

  public String getServletRunnerPath() {
    return runnerPath.getText();
  }
  public String getWebBrowserPath() {
    return webBrowserPath.getText();
  }
  public boolean getMakingBatch() {
    return batchCbx.isSelected();
  }

  class ActionHandler implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();

      if(cmd.equals("PATH")){
        pathBtnSelected();
      }
      else if(cmd.equals("WEB")){
        webBrowserBtnSelected();
      }
    }
  }


  class ChangeEventHandler implements ChangeListener {
    public void stateChanged(ChangeEvent e) {

    }
  }
}

