/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/LibraryPropertyDlg.java,v 1.7 1999/08/19 04:18:09 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.7 $
 */
package com.antsoft.ant.property;

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

/**
 * library property를 설정하는 dialog
 *
 * @author kim sang kyun
 */

public class LibraryPropertyDlg extends JDialog{
  private JTextField nameTf, classPathTf, sourcePathTf, docPathTf;
  private JButton classPathBtn, sourcePathBtn, docPathBtn, okBtn,
                  cancelBtn, addBtn, removeBtn, editBtn;
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
  
  private String currOpenPath = null;


  public LibraryPropertyDlg(JFrame f, String title, boolean modal, LibInfoContainer libInfos) {
    super(f, title, modal);

		// register window event handler
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    this.libInfos = libInfos;
    this.parent = f;
    ActionListener al = new ActionHandler();
    removedLibs = new Vector(3, 2);

    //library name label
    nameLbl = new JLabel(" Name", JLabel.LEFT);
    nameLbl.setPreferredSize(new Dimension(180,20));
    //nameLbl.setEnabled(false);

    //name text field
    nameTf = new JTextField(19);
    nameTf.setPreferredSize(new Dimension(150,20));
    nameTf.setEnabled(false);
    JButton tmp = new JButton(" ");
    tmp.setPreferredSize(new Dimension(20,20));
    tmp.setVisible(false);

    //name panel
    JPanel nameP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    nameP.add(nameTf);
    nameP.add(tmp);

    //class path label
    classPathLbl = new JLabel(" Class Path", JLabel.LEFT);
    classPathLbl.setPreferredSize(new Dimension(180,20));
    //classPathLbl.setEnabled(false);

    //class path text field
    classPathTf = new JTextField(19);
    classPathTf.setPreferredSize(new Dimension(150,20));
    classPathTf.setEnabled(false);
    classPathBtn = new JButton("...");
    classPathBtn.setPreferredSize(new Dimension(20,20));
    classPathBtn.setEnabled(false);
    classPathBtn.setActionCommand("CLASSPATH");
    classPathBtn.addActionListener(al);

    //class path panel
    JPanel classP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    classP.add(classPathTf);
    classP.add(classPathBtn);

    //source path label
    sourcePathLbl = new JLabel(" Source Path", JLabel.LEFT);
    sourcePathLbl.setPreferredSize(new Dimension(180,20));
    //sourcePathLbl.setEnabled(false);

    //source path text field
    sourcePathTf = new JTextField(19);
    sourcePathTf.setPreferredSize(new Dimension(150,20));
    sourcePathTf.setEnabled(false);
    sourcePathBtn = new JButton("...");
    sourcePathBtn.setPreferredSize(new Dimension(20,20));
    sourcePathBtn.setEnabled(false);
    sourcePathBtn.setActionCommand("SOURCE");
    sourcePathBtn.addActionListener(al);

    //source path panel
    JPanel sourceP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    sourceP.add(sourcePathTf);
    sourceP.add(sourcePathBtn);

    //doc path label
    docPathLbl = new JLabel(" Doc Path");
    docPathLbl.setPreferredSize(new Dimension(180,20));
    //docPathLbl.setEnabled(false);

    //source path text field
    docPathTf = new JTextField(19);
    docPathTf.setPreferredSize(new Dimension(150,20));
    docPathTf.setEnabled(false);
    docPathBtn = new JButton("...");
    docPathBtn.setPreferredSize(new Dimension(20,20));
    docPathBtn.setEnabled(false);
    docPathBtn.setActionCommand("DOC");
    docPathBtn.addActionListener(al);

    //doc panel
    JPanel docP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    docP.add(docPathTf);
    docP.add(docPathBtn);

    //button
    okBtn = new JButton("OK");
    okBtn.setActionCommand("OK");
    okBtn.addActionListener(al);

    cancelBtn = new JButton("Cancel");
    cancelBtn.setActionCommand("CANCEL");
    cancelBtn.addActionListener(al);

    addBtn = new JButton("Add");
    addBtn.setActionCommand("ADD");
    addBtn.addActionListener(al);

    removeBtn = new JButton("Remove");
    removeBtn.setActionCommand("REMOVE");
    removeBtn.addActionListener(al);

    editBtn = new JButton("Edit");
    editBtn.setActionCommand("EDIT");
    editBtn.addActionListener(al);

    JPanel buttonP = new JPanel();
    buttonP.add(addBtn);
    buttonP.add(removeBtn);
    buttonP.add(editBtn);
    buttonP.add(okBtn);
    buttonP.add(cancelBtn);

    //jdk list
    libList = new JList();
    libListModel = new LibraryListModel(libInfos);
    libList.setModel(libListModel);

    ListSelectionListener lsl = new ListSelectionHandler();
    libListSelectModel = new DefaultListSelectionModel();
    libListSelectModel.addListSelectionListener(lsl);
    libList.setSelectionModel(libListSelectModel);

    libList.addMouseListener(new MouseAdapter(){
      public void mousePressed(MouseEvent e){
        if(e.getClickCount() > 1 && !libList.isSelectionEmpty()){
          okBtnSelected();
        }
      }});

    JScrollPane listPane = new JScrollPane(libList);
    listPane.setPreferredSize(new Dimension(230,190));

    //lib list panel
    JPanel listP = new JPanel();
    TitledBorder border1 = new TitledBorder(new EtchedBorder(),"Library List");
    border1.setTitleColor(Color.black);
    border1.setTitleFont(FontList.regularFont);
    listP.setBorder(border1);
    listP.add(listPane);

    //right panel
    JPanel rightP = new JPanel(new GridLayout(8,1));
    rightP.setBorder(BorderList.pathBorder);
    rightP.add(nameLbl);
    rightP.add(nameP);
    rightP.add(classPathLbl);
    rightP.add(classP);
    rightP.add(sourcePathLbl);
    rightP.add(sourceP);
    rightP.add(docPathLbl);
    rightP.add(docP);

    //total panel
    JPanel totalP = new JPanel(new GridLayout(1,2));
    totalP.add(listP);
    totalP.add(rightP);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(totalP,BorderLayout.CENTER);
    getContentPane().add(buttonP,BorderLayout.SOUTH);
    getContentPane().add(new JPanel(),BorderLayout.WEST);
    getContentPane().add(new JPanel(),BorderLayout.EAST);
    getContentPane().add(new JPanel(),BorderLayout.NORTH);

    //focus setting
    nameTf.setNextFocusableComponent(classPathTf);
    classPathTf.setNextFocusableComponent(sourcePathTf);
    sourcePathTf.setNextFocusableComponent(docPathTf);

    //this.setSize(440,500);
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

  public void classPathBtnSelected(){

    ExtensionFileFilter filter = new ExtensionFileFilter();
    filter.addExtension("jar");
    filter.addExtension("zip");

    JFileChooser filechooser = new JFileChooser();

    filechooser.setFileFilter(filter);
    filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    filechooser.setMultiSelectionEnabled(false);
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("ClassPath");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("ClassPath");

    if(filechooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();
			currOpenPath = f.getParent();     

      if(!classPathTf.getText().equals("")){
        classPathTf.setText(classPathTf.getText() + ", " + f.getAbsolutePath());
      }
      else{
        classPathTf.setText(f.getAbsolutePath());
      }
    }
  }

  public void sourceBtnSelected(){

    ExtensionFileFilter filter = new ExtensionFileFilter();
    filter.addExtension("jar");
    filter.addExtension("zip");

    JFileChooser filechooser = new JFileChooser();
    filechooser.setFileFilter(filter);
    
    if(currOpenPath != null)
    filechooser.setCurrentDirectory(new File(currOpenPath));

    filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("Select JDK Source Directory");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("Output Root Directory");

    if(filechooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();
      sourcePathTf.setText(f.getAbsolutePath());
    }
  }

  public void docBtnSelected(){

    JFileChooser filechooser = new JFileChooser();
    
    filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("Select JDK Doc Directory");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("Output Root Directory");
    
    if(currOpenPath != null)
    filechooser.setCurrentDirectory(new File(currOpenPath));

    if(filechooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();
      docPathTf.setText(f.getAbsolutePath());
    }
  }

 public void editBtnSelected(){

    LibInfo libInfo = (LibInfo)libListModel.getElementAt(libList.getSelectedIndex());
    this.currentEditingLibInfo = libInfo;

    nameTf.setText(libInfo.getName());
    classPathTf.setText(libInfo.getClassPathString());
    sourcePathTf.setText(libInfo.getSourcePath());
    docPathTf.setText(libInfo.getDocPath());
  }

  public void removeBtnSelected(){
    if(libListModel.getSize() > 0 && !libListSelectModel.isSelectionEmpty()){
      removedLibs.addElement(libListModel.getElementAt(libList.getSelectedIndex()));
      libListModel.removeElementAt(libList.getSelectedIndex());
    }

    setEnableFlag(false);
    setEmptyFieldEnabled();
  }

  public void doneBtnSelected(){
    File classPathF = new File(classPathTf.getText());
    File source = new File(sourcePathTf.getText());
    File doc = new File(docPathTf.getText());

    if(classPathTf.getText().length() > 0 && !classPathF.exists()){
      JOptionPane.showMessageDialog(null, "Class Path not avaliable", "Error", JOptionPane.ERROR_MESSAGE, ImageList.errorIcon );
      return;
    }

    else if(sourcePathTf.getText().length() > 0 && !source.exists()){
      JOptionPane.showMessageDialog(null, "Source Path not avaliable", "Error", JOptionPane.ERROR_MESSAGE, ImageList.errorIcon );
      return;
    }

    else if(docPathTf.getText().length() > 0 && !doc.exists()){
      JOptionPane.showMessageDialog(null, "Doc Path not avaliable", "Error", JOptionPane.ERROR_MESSAGE, ImageList.errorIcon );
      return;
    }

    Vector classPath = new Vector();
    StringTokenizer st = new StringTokenizer(classPathTf.getText(), "," , false);

    if(st.countTokens() == 0){
      classPath.addElement(classPathTf.getText());
    }
    else{
      while(st.hasMoreTokens()) {
        String token = st.nextToken();
        classPath.addElement(token);
      }
    }

    if(this.currentEditingLibInfo == null){
      LibInfo libInfo = new LibInfo(nameTf.getText(), classPath, sourcePathTf.getText(), docPathTf.getText());
      libListModel.addElement(libInfo);
    }
    else{
      removedLibs.addElement(currentEditingLibInfo.clone());

      currentEditingLibInfo.setName(nameTf.getText());
      currentEditingLibInfo.setClassPath(classPath);
      currentEditingLibInfo.setSourcePath(sourcePathTf.getText());
      currentEditingLibInfo.setDocPath(docPathTf.getText());

      libListModel.fireUpdate(currentEditingLibInfo, libList.getSelectedIndex());
      currentEditingLibInfo = null;
    }

    setEnableFlag(false);
    setEmptyFieldEnabled();
  }

  public void okBtnSelected(){
    if(nameTf.isEnabled()) doneBtnSelected();
    else{
      if(!libList.isSelectionEmpty()){
        this.selectedLibInfo = (LibInfo)libListModel.getElementAt(libList.getSelectedIndex());
      }
      this.isOK = true;
      dispose();
    }
  }

  public Vector getRemovedLibs(){
    return this.removedLibs;
  }

  private void libSelected(int index, boolean enable){
    LibInfo selectedLib = (LibInfo)libListModel.getElementAt(index);
    setEnableFlag(enable);

    nameTf.setText(selectedLib.getName());
    classPathTf.setText(selectedLib.getClassPathString());
    sourcePathTf.setText(selectedLib.getSourcePath());
    docPathTf.setText(selectedLib.getDocPath());
  }

  /** libList selection event handler */
  class ListSelectionHandler implements ListSelectionListener{

    public void valueChanged(ListSelectionEvent e){
      libSelected(libList.getSelectedIndex(), false);
    }
  }

  private void setEnableFlag(boolean flag){
    nameTf.setEnabled(flag);
    classPathTf.setEnabled(flag);
    sourcePathTf.setEnabled(flag);
    docPathTf.setEnabled(flag);

    classPathBtn.setEnabled(flag);
    sourcePathBtn.setEnabled(flag);
    docPathBtn.setEnabled(flag);

    /*nameLbl.setEnabled(flag);
    classPathLbl.setEnabled(flag);
    sourcePathLbl.setEnabled(flag);
    docPathLbl.setEnabled(flag);*/
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

      if(cmd.equals("CLASSPATH")){
        classPathBtnSelected();
      }

      else if(cmd.equals("SOURCE")){
        sourceBtnSelected();
      }

      else if(cmd.equals("DOC")){
        docBtnSelected();
      }

      else if(cmd.equals("ADD")){
        setEnableFlag(true);
        setEmptyFieldEnabled();
        nameTf.requestFocus();
      }

      else if(cmd.equals("EDIT")){
        if(!libListSelectModel.isSelectionEmpty()){
          setEnableFlag(true);
          nameTf.requestFocus();
          editBtnSelected();
        }
      }

      else if(cmd.equals("OK")){
        okBtnSelected();
      }

      else if(cmd.equals("REMOVE")){
        if(!libListSelectModel.isSelectionEmpty()) removeBtnSelected();
      }

      else if(cmd.equals("DONE")){
        if(nameTf.getText().equals("")){
          JOptionPane.showMessageDialog(null, "Library Name is not specified!!", "Error", JOptionPane.ERROR_MESSAGE, ImageList.errorIcon );
        }

        else if(classPathTf.getText().equals("")){
          JOptionPane.showMessageDialog(null, "CLASSPATH is not specified!!", "Error", JOptionPane.ERROR_MESSAGE, ImageList.errorIcon );
        }
      }

      else if(cmd.equals("CANCEL")){
        isOK = false;
        dispose();
      }
    }
  }
}
