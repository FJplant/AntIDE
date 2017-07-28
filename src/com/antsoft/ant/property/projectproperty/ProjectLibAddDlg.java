/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/projectproperty/ProjectLibAddDlg.java,v 1.6 1999/08/19 04:24:20 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.6 $
 */
package com.antsoft.ant.property.projectproperty;

import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Vector;
import java.util.StringTokenizer;
import com.antsoft.ant.util.*;
import com.antsoft.ant.property.*;

/**
 * library property를 설정하는 dialog
 *
 * @author kim sang kyun
 */

public class ProjectLibAddDlg extends JDialog{
  private JTextField nameTf, classPathTf, sourcePathTf, docPathTf;
  private JButton okBtn, cancelBtn;
  private JList libList;
  private LibraryListModel libListModel;
  private DefaultListSelectionModel libListSelectModel;

  private JFrame parent;
  private LibInfoContainer libInfoContainer;
  private JLabel nameLbl, classPathLbl, sourcePathLbl, docPathLbl;
  private JScrollPane libPane;

  private LibInfo selectedLibInfo;
  private Vector removedLibs;

  private LibInfo currentEditingLibInfo = null;
  private LibInfoContainer libInfos;

  private boolean isOK = false;

  public ProjectLibAddDlg(JFrame f, String title, boolean modal, LibInfoContainer libInfos) {

    super(f, title, modal);

		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    this.libInfos = libInfos;
    this.parent = f;
    getContentPane().setLayout(new BorderLayout());
    ActionListener al = new ActionHandler();

    //library name
    nameLbl = new JLabel(" Name");
    nameLbl.setPreferredSize(new Dimension(150,20));

    /*JPanel p1 = new JPanel();
    p1.setLayout(new FlowLayout(FlowLayout.LEFT));
    p1.add(nameLbl);*/

    nameTf = new JTextField(19);
    nameTf.setPreferredSize(new Dimension(150,20));
    nameTf.setBackground(Color.white);
    nameTf.setEditable(false);

    JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p2.add(nameTf);

    //class path
    classPathLbl = new JLabel(" Class Path");
    classPathLbl.setPreferredSize(new Dimension(150,20));

    /*JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p3.add(classPathLbl);*/

    classPathTf = new JTextField(19);
    classPathTf.setPreferredSize(new Dimension(150,20));
    classPathTf.setBackground(Color.white);
    classPathTf.setEditable(false);

    JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p4.add(classPathTf);

    //source path
    sourcePathLbl = new JLabel(" Source Path");
    sourcePathLbl.setPreferredSize(new Dimension(150,20));

    /*JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p5.add(sourcePathLbl);*/

    sourcePathTf = new JTextField(19);
    sourcePathTf.setPreferredSize(new Dimension(150,20));
    sourcePathTf.setEditable(false);
    sourcePathTf.setBackground(Color.white);
    JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p6.add(sourcePathTf);

    //doc path
    docPathLbl = new JLabel(" Doc Path");
    docPathLbl.setPreferredSize(new Dimension(150,20));

    /*JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p7.add(docPathLbl);*/

    docPathTf = new JTextField(19);
    docPathTf.setPreferredSize(new Dimension(150,20));
    docPathTf.setBackground(Color.white);
    docPathTf.setEditable(false);

    JPanel p8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p8.add(docPathTf);

    //right panel
    JPanel p9 = new JPanel(new GridLayout(8,1));
    TitledBorder border2 = new TitledBorder(new EtchedBorder(),"Path");
    border2.setTitleColor(Color.black);
    border2.setTitleFont(FontList.regularFont);
    p9.setBorder(border2);
    p9.add(nameLbl);
    p9.add(p2);
    p9.add(classPathLbl);
    p9.add(p4);
    p9.add(sourcePathLbl);
    p9.add(p6);
    p9.add(docPathLbl);
    p9.add(p8);

    //jdk list
    libList = new JList();
    libListModel = new LibraryListModel(libInfos);
    libList.setModel(libListModel);

    ListSelectionListener lsl = new ListSelectionHandler();
    libListSelectModel = new DefaultListSelectionModel();
    libListSelectModel.addListSelectionListener(lsl);
    libList.setSelectionModel(libListSelectModel);

    libList.addMouseListener(new MouseAdapter(){
      public void mouseClicked(MouseEvent e){
        if(e.getClickCount() > 1) okBtnSelected();
      }});


    //jdk list scroll pane
    libPane = new JScrollPane(libList);
    libPane.setPreferredSize(new Dimension(210,190));
    //lib list panel
    JPanel p11 = new JPanel();
    TitledBorder border = new TitledBorder(new EtchedBorder(),"Libraries");
    border.setTitleColor(Color.black);
    border.setTitleFont(FontList.regularFont);
    p11.setBorder(border);
    p11.add(libPane);

    okBtn = new JButton("OK");
    okBtn.setActionCommand("OK");
    okBtn.addActionListener(al);

    cancelBtn = new JButton("Cancel");
    cancelBtn.setActionCommand("CANCEL");
    cancelBtn.addActionListener(al);

    JPanel buttonP = new JPanel();
    buttonP.add(okBtn);
    buttonP.add(cancelBtn);

    JPanel p12 = new JPanel(new GridLayout(1,2));
    p12.add(p11);
    p12.add(p9);

    //focus setting
    nameTf.setNextFocusableComponent(classPathTf);
    classPathTf.setNextFocusableComponent(sourcePathTf);
    sourcePathTf.setNextFocusableComponent(docPathTf);


    getContentPane().add(p12, BorderLayout.CENTER);
    getContentPane().add(new JPanel(), BorderLayout.NORTH);
    getContentPane().add(new JPanel(), BorderLayout.EAST);
    getContentPane().add(new JPanel(), BorderLayout.WEST);
    getContentPane().add(buttonP, BorderLayout.SOUTH);

    //setSize(440,330);
    this.setResizable(false);
  }

  public void setSelectedLib(String libName){

    int size = libList.getModel().getSize();
    for(int i=0;i<size; i++){
      String name = libList.getModel().getElementAt(i).toString();
      if(name.equals(libName)) {
        libList.setSelectedIndex(i);
        return;
      }
    }
  }

  public boolean isOk(){
    return this.isOK;
  }

  public LibInfoContainer getLibInfos(){
    return this.libInfos;
  }

  public LibInfo getSelectedLibInfo(){
    return selectedLibInfo;
  }

  public void okBtnSelected(){
    if(!libList.isSelectionEmpty())
      this.selectedLibInfo = (LibInfo)libListModel.getElementAt(libList.getSelectedIndex());

    this.isOK = true;
    dispose();
  }

  /** libList selection event handler */
  class ListSelectionHandler implements ListSelectionListener{

    public void valueChanged(ListSelectionEvent e){
      LibInfo selectedLib = (LibInfo)libListModel.getElementAt(libList.getSelectedIndex());
      //setEnableFlag(false);

      nameTf.setText(selectedLib.getName());
      classPathTf.setText(selectedLib.getClassPathString());
      sourcePathTf.setText(selectedLib.getSourcePath());
      docPathTf.setText(selectedLib.getDocPath());
    }
  }

  private void setEnableFlag(boolean flag){
    nameTf.setEnabled(flag);
    classPathTf.setEnabled(flag);
    sourcePathTf.setEnabled(flag);
    docPathTf.setEnabled(flag);

    nameLbl.setEnabled(flag);
    classPathLbl.setEnabled(flag);
    sourcePathLbl.setEnabled(flag);
    docPathLbl.setEnabled(flag);
  }

  private void setEmptyFieldEnabled(){
    nameTf.setText("");
    classPathTf.setText("");
    sourcePathTf.setText("");
    docPathTf.setText("");
  }

  /** action event handler */
  class ActionHandler implements ActionListener{

    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();

      if(cmd.equals("OK")){
        okBtnSelected();
      }

      else if(cmd.equals("CANCEL")){
        isOK = false;
        dispose();
      }
    }
  }
}
