/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/defaultproperty/WebPanel.java,v 1.5 1999/07/23 07:21:24 lila Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author: Kim, Sang Kyun
 * $Revision: 1.5 $
 */
package com.antsoft.ant.property.defaultproperty;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import com.antsoft.ant.util.*;

public class WebPanel extends JPanel {
  private JComboBox browserCombo;
  private Vector allPaths;
  private DefaultComboBoxModel model;
  private JButton addBtn, removeBtn;
  private JFrame parent;
  private JCheckBox internalCbx;
  private boolean isInternal = true;

  public WebPanel(JFrame parent) {
    this.parent = parent;
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    setLayout(new BorderLayout());
    setBorder(BorderList.etchedBorder5);

    ActionListener al = new ActionEventHandler();

    internalCbx = new JCheckBox("Use internal JavaDoc Viewer");
    internalCbx.setSelected(true);
    internalCbx.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        if(internalCbx.isSelected()) {
          isInternal = true;
          browserCombo.setEnabled(false);
        }
        else {
          isInternal = false;
          browserCombo.setEnabled(true);
        }
      }});

    JPanel internalP = new JPanel(new BorderLayout());
    internalP.add(internalCbx, BorderLayout.WEST);

    //label
    //JLabel webLbl = new JLabel("WebBrowser Path");
    //JPanel webLblP = new JPanel(new BorderLayout());
    //webLblP.add(webLbl, BorderLayout.WEST);

    //combo box
    allPaths = new Vector(2, 1);
    model = new DefaultComboBoxModel(allPaths);
    browserCombo = new JComboBox(model);
    browserCombo.setPreferredSize(new Dimension(330,20));
    browserCombo.setEnabled(false);

    //button
    addBtn = new JButton("Add");
    addBtn.setActionCommand("ADD");
    addBtn.addActionListener(al);

    removeBtn = new JButton("Remove");
    removeBtn.setActionCommand("REMOVE");
    removeBtn.addActionListener(al);

    JPanel btnP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    btnP.add(addBtn);
    btnP.add(removeBtn);

    //combobox panel
    JPanel comboP = new JPanel(new BorderLayout());
    comboP.add(browserCombo, BorderLayout.CENTER);

    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p.setPreferredSize(new Dimension(350,100));
    BlackTitledBorder border = new BlackTitledBorder("WebBrowser Path");
    p.setBorder(border);
    p.add(comboP);
    p.add(btnP);

    Box box = Box.createVerticalBox();
    box.add(internalP);
    box.add(p);

    JPanel centerP = new JPanel(new BorderLayout());
    centerP.add(box, BorderLayout.NORTH);
    centerP.add(new JPanel(),BorderLayout.CENTER);

    add(centerP, BorderLayout.CENTER);
    add(new JPanel(), BorderLayout.NORTH);
    add(new JPanel(), BorderLayout.WEST);
    add(new JPanel(), BorderLayout.EAST);
    add(new JPanel(), BorderLayout.SOUTH);
  }

  public boolean isInternalUse(){
    return this.isInternal;
  }

  public void setInternal(boolean flag){
    this.isInternal = flag;
    this.internalCbx.setSelected(flag);
  }  

  public void addBtnSelected(){
    ExtensionFileFilter filter = null;

    if(OSVerifier.getOS().equals(OSVerifier.MS_WINDOW)){
      filter = new ExtensionFileFilter("exe");
    }

    JFileChooser filechooser = new JFileChooser();
    if(filter != null) filechooser.setFileFilter(filter);
    
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("Select WebBrowser Path");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("WebBrowser path");

    if(filechooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();
      if(model.getIndexOf(f.getAbsolutePath()) == -1){
        model.addElement(f.getAbsolutePath());
        model.setSelectedItem(f.getAbsolutePath());
      }
    }
  }

  public void removeBtnSelected(){
    if(model.getSize() > 0)
    model.removeElement(browserCombo.getSelectedItem());
  }

  /**
   * return current web browser path
   *
   * @return current web browser path
   */
  public String getSelectedBrowserPath(){
    String item = (String)model.getSelectedItem();
    if(item == null) return null;
    
    StringBuffer ret = new StringBuffer();
    StringTokenizer st = new StringTokenizer(item, "\\", true);
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      if(token.indexOf(" ") != -1){
        token = makeDosStyle(token);
        ret.append(token);
      }
      else{
        ret.append(token);
      }
    }
    return ret.toString();
  }

  public String getSelectedBrowserPathToSave(){
    return (String)model.getSelectedItem();
  }

  private String makeDosStyle(String path){
    String ret = null;
    ret = path.substring(0, 6);
    ret = ret + "~1";
    return ret;
  }

  public void setSelectedBrowserPath(String path){
    if(model.getIndexOf(path) != -1){
      model.setSelectedItem(path);
    }
  }

  public Vector getAllBrowserPaths(){
    Vector ret = new Vector(2, 1);
    for(int i=0; i<model.getSize(); i++){
      ret.addElement(model.getElementAt(i));
    }
    return ret;
  }

  public void setAllBrowserPaths(Vector allPaths){
    model = new DefaultComboBoxModel(allPaths);
    browserCombo.setModel(model);
  }

  class ActionEventHandler implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();
      if(cmd.equals("ADD")){
        addBtnSelected();
      }
      else if(cmd.equals("REMOVE")){
        removeBtnSelected();
      }
    }
  }
}




