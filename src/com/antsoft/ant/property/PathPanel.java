/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/PathPanel.java,v 1.5 1999/08/07 03:33:52 itree Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.5 $
 */
package com.antsoft.ant.property;
import java.awt.*;import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Vector;

import com.antsoft.ant.util.FontList;import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.main.MainFrame;
import com.antsoft.ant.util.DirChooser;

/** * path property를 설정할수 있는 panel
 */
public class PathPanel extends JPanel {
  private JComboBox jdkCombo;
  private JdkComboBoxModel jdkComboModel;

  private JTextField sourceTf, outputTf, documentTf;  private JList libraryList;
  private JScrollPane scrLibPane;
  private LibraryListModel libModel;
  private DefaultListSelectionModel libListSelectModel;
  private JButton jdkAddBtn, sourceBtn, outputBtn, documentBtn, libAddBtn, libRemoveBtn;

  private PathPropertyInfo pathPropertyInfo;  private LibInfoContainer allLibInfos;
  private JFrame parent;

  public PathPanel(JFrame parent) {    this.parent = parent;
    setLayout(new BorderLayout());
    setBorder(BorderList.etchedBorder5);
    Box box = Box.createVerticalBox();

    ActionListener al = new ActionHandler();
    //jdk
    JLabel jdkLbl = new JLabel(" JDK", JLabel.LEFT);
    jdkLbl.setPreferredSize(new Dimension(50,20));

    jdkComboModel = new JdkComboBoxModel();
    jdkCombo = new JComboBox();
    jdkCombo.setModel(jdkComboModel);
    jdkCombo.setPreferredSize(new Dimension(230,20));

    jdkAddBtn = new JButton("Add");
    jdkAddBtn.setActionCommand("JDKADD");
    jdkAddBtn.addActionListener(al);

    //jdk panel
    JPanel p1 = new JPanel();
    p1.setLayout(new FlowLayout(FlowLayout.LEFT));
    p1.add(jdkLbl);
    p1.add(jdkCombo);
    p1.add(jdkAddBtn);

    //source
    JLabel srcLbl = new JLabel("Source", JLabel.LEFT);
    srcLbl.setPreferredSize(new Dimension(43,20));
    sourceTf = new JTextField(23);
    sourceBtn = new JButton("..");
    sourceBtn.setPreferredSize(new Dimension(20,20));
    sourceBtn.setActionCommand("SOURCE");
    sourceBtn.addActionListener(al);

    //source panel
    JPanel p2 = new JPanel();
    p2.setLayout(new FlowLayout(FlowLayout.LEFT));
    p2.add(srcLbl);
    p2.add(sourceTf);
    p2.add(sourceBtn);

    //output
    JLabel outputLbl = new JLabel("Output");
    outputLbl.setPreferredSize(new Dimension(43,20));
    outputTf = new JTextField(23);
    outputBtn = new JButton("..");
    outputBtn.setPreferredSize(new Dimension(20,20));
    outputBtn.setActionCommand("OUTPUT");
    outputBtn.addActionListener(al);

    //output panel
    JPanel p3 = new JPanel();
    p3.setLayout(new FlowLayout(FlowLayout.LEFT));
    p3.add(outputLbl);
    p3.add(outputTf);
    p3.add(outputBtn);
    //document    JLabel documentLbl = new JLabel("Doc");
    documentLbl.setPreferredSize(new Dimension(43,20));
    documentTf = new JTextField(23);
    documentBtn = new JButton("..");
    documentBtn.setPreferredSize(new Dimension(20,20));
    documentBtn.setActionCommand("DOCUMENT");
    documentBtn.addActionListener(al);

    //document panel
    JPanel p4 = new JPanel();
    p4.setLayout(new FlowLayout(FlowLayout.LEFT));
    p4.add(documentLbl);
    p4.add(documentTf);
    p4.add(documentBtn);    //library list    libModel = new LibraryListModel();
    libraryList = new JList();
    libraryList.setModel(libModel);
    libraryList.addMouseListener(new MouseAdapter(){
      public void mousePressed(MouseEvent e){
        if(e.getClickCount() > 1 && !libraryList.isSelectionEmpty()){
          editLibrary(libModel.getElementAt(libraryList.getSelectedIndex()).toString());
        }
      }}
      );
    scrLibPane = new JScrollPane(libraryList);
    //scrLibPane.setBorder(BorderList.lightLoweredBorder);
    scrLibPane.setPreferredSize(new Dimension(370,150));

    libAddBtn = new JButton("Add");
    libAddBtn.setActionCommand("LIBADD");
    libAddBtn.addActionListener(al);

    libRemoveBtn = new JButton("Remove");
    libRemoveBtn.setActionCommand("LIBREMOVE");
    libRemoveBtn.addActionListener(al);
    //button Panel    JPanel p5 = new JPanel();
    p5.setLayout(new FlowLayout(FlowLayout.CENTER));
    p5.add(libAddBtn);
    p5.add(libRemoveBtn);

    //library panel
    JPanel p6 = new JPanel();
    p6.setLayout(new BorderLayout());
    p6.setBorder(BorderList.libBorder);
    p6.add(scrLibPane,BorderLayout.CENTER);
    p6.add(p5,BorderLayout.SOUTH);
    p6.add(new JPanel(),BorderLayout.WEST);
    p6.add(new JPanel(),BorderLayout.EAST);

    //root directories panel
    Box box1 = Box.createVerticalBox();
    box1.add(p2);
    box1.add(p3);
    box1.add(p4);

    JPanel p7 = new JPanel();
    p7.setLayout(new BorderLayout());
    p7.setBorder(BorderList.dirBorder);
    p7.add(box1);

    Box box2 = Box.createVerticalBox();
    box2.add(p1);
    box2.add(p7);
    box2.add(p6);

    setLayout(new BorderLayout());
    add(box2,BorderLayout.CENTER);
    add(new JPanel(),BorderLayout.WEST);
    add(new JPanel(),BorderLayout.EAST);
    add(new JPanel(),BorderLayout.NORTH);
    add(new JPanel(),BorderLayout.SOUTH);
  }

  /**
   * set Path model
   *
   * @param newModel new PathModel
   */
  public void setModel(DefaultPathModel newModel){
    this.allLibInfos = newModel.getAllLibInfoContainer();

    //source, output root directory 정보를 채운다
    sourceTf.setText(newModel.getSourceRoot());
    outputTf.setText(newModel.getOutputRoot());
    documentTf.setText(newModel.getDocumentRoot());

    //jdkInfo combobox 정보를 채운다
    jdkComboModel.setJdkInfoContainer(newModel.getJdkInfoContainer());
    if(newModel.getCurrentJdkInfo() != null){
      jdkComboModel.setSelectedItem(newModel.getCurrentJdkInfo());
    }

    //선택된 libInfo list 정보를 채운다
    LibInfoContainer lic = newModel.getSelectedLibInfoContainer();
    libModel.setLibInfoContainer(lic);
  }

  public DefaultPathModel getModel(){
    DefaultPathModel ret = new DefaultPathModel();
    ret.setJdkInfoContainer(jdkComboModel.getJdkInfoContainer());
    ret.setAllLibInfoContainer(this.allLibInfos);
    ret.setCurrentJdkInfo((JdkInfo)jdkComboModel.getSelectedItem());
    ret.setSelectedLibInfoContainer(libModel.getLibInfoContainer());

    if(sourceTf.getText().length() > 0) ret.setSourceRoot(sourceTf.getText());
    if(outputTf.getText().length() > 0) ret.setOutputRoot(outputTf.getText());
    if(documentTf.getText().length() > 0) ret.setDocumentRoot(documentTf.getText());

    return ret;
  }

  /**
   * source root select button handler
   */
  public void sourceBtnSelected(){

  /*
    JFileChooser filechooser = new JFileChooser();
    filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("Select Source Root Directory");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("Source Root Directory");

    if(filechooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();
      sourceTf.setText(f.getAbsolutePath());
    }
    */
    DirChooser dirchooser = new DirChooser(parent, "Select Source Root Directory",sourceTf.getText() ,false,true);
    dirchooser.setVisible(true);

    if(dirchooser.isOK()){
      File f = new File(dirchooser.getSelectedDirectoryPath());
      sourceTf.setText(f.getAbsolutePath());
    }
  }

  public void outputBtnSelected(){

    /*
    JFileChooser filechooser = new JFileChooser();
    filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("Select Output Root Directory");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("Output Root Directory");

    if(filechooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();
      outputTf.setText(f.getAbsolutePath());
    }
    */
    DirChooser dirchooser = new DirChooser(parent,"Select Output Root Directory",outputTf.getText(),false,true);
    dirchooser.setVisible(true);

    if(dirchooser.isOK()){
      File f = new File(dirchooser.getSelectedDirectoryPath());
      outputTf.setText(f.getAbsolutePath());
    }
  }

  public void documentBtnSelected(){
    /*
    JFileChooser filechooser = new JFileChooser();
    filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    filechooser.setPreferredSize(new Dimension(400, 300));
    filechooser.setApproveButtonText("Select");
    filechooser.setApproveButtonToolTipText("Select Document Root Directory");
    filechooser.setBorder(BorderList.lightLoweredBorder);
    filechooser.setDialogTitle("Document Root Directory");

    if(filechooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File f = filechooser.getSelectedFile();
      documentTf.setText(f.getAbsolutePath());
    }
    */
    DirChooser dirchooser = new DirChooser(parent,"Select Document Root Directory", documentTf.getText(),false,true);
    dirchooser.setVisible(true);

    if(dirchooser.isOK()){
      File f = new File(dirchooser.getSelectedDirectoryPath());
      documentTf.setText(f.getAbsolutePath());
    }
  }

  public void jdkAddBtnSelected(){

    JdkPropertyDlg dlg = new JdkPropertyDlg(MainFrame.mainFrame, "JDK Property Setup", true,
                                           (JdkInfoContainer)jdkComboModel.getJdkInfoContainer().clone());

    dlg.setBounds(200, 200, 540, 300);
    dlg.setVisible(true);

    if(dlg.isOk()){
      jdkComboModel.removeAll();

      JdkInfoContainer jdks = dlg.getJdkInfos();
      int size = jdks.getSize();
      for(int i=0; i<size; i++){
        jdkComboModel.addElement((JdkInfo)jdks.getJdkInfo(i));
      }
    }
  }

  public void editLibrary(String libName){
    libAddBtnSelected(libName);
  }

  public void libAddBtnSelected(String libName){

    LibraryPropertyDlg dlg = new LibraryPropertyDlg(MainFrame.mainFrame, "Library Property Setup", true,
                                               (LibInfoContainer)allLibInfos.clone());
    dlg.setBounds(200, 200, 540, 300);
    if(libName != null) dlg.setSelectedLib(libName);
    dlg.setVisible(true);

    if(dlg.isOk()){
      Vector removed = dlg.getRemovedLibs();
      if(removed.size() > 0){
        for(int i=0; i<removed.size(); i++){
          libModel.removeElement(removed.elementAt(i));
        }
        scrLibPane.repaint();
      }

      LibInfo selectedLib = dlg.getSelectedLibInfo();
      if(selectedLib != null && libModel.indexOf(selectedLib) == -1){
        libModel.addElement(selectedLib);
      }

      if(dlg.getLibInfos() != null)  this.allLibInfos = dlg.getLibInfos();

      for(int i=0; i<libModel.getSize(); i++){
        LibInfo info = (LibInfo)libModel.getElementAt(i);
        libModel.setElementAt((LibInfo)allLibInfos.getLibraryInfo(info.getName()), i);
      }
    }
  }

  private void libRemoveBtnSelected(){
    if(libModel.getSize() > 0 && !libraryList.isSelectionEmpty()){
      libModel.removeElementAt(libraryList.getSelectedIndex());
    }
  }

  //ActionEvent Handler
  class ActionHandler implements ActionListener{

    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();

      if(cmd.equals("SOURCE")){
        sourceBtnSelected();
      }

      else if(cmd.equals("OUTPUT")){
        outputBtnSelected();
      }

      else if(cmd.equals("DOCUMENT")){
        documentBtnSelected();
      }

      else if(cmd.equals("JDKADD")){
        jdkAddBtnSelected();
      }

      else if(cmd.equals("LIBADD")){
        libAddBtnSelected(null);
      }

      else if(cmd.equals("LIBREMOVE")){
        libRemoveBtnSelected();
      }
    }
  }
}
