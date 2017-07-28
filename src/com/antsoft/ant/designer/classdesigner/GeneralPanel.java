/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/classdesigner/GeneralPanel.java,v 1.8 1999/07/29 08:14:14 remember Exp $
 * Ant ( JDK wrapper Java IDE )

 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.8 $
 * $History: GeneralPanel.java $
 *                    
 * *****************  Version 4  *****************
 * User: Remember     Date: 99-05-20   Time: 4:57p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 *
 * *****************  Version 3  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:44p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 *
 * *****************  Version 2  *****************
 * User: Remember     Date: 99-05-07   Time: 2:34p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 *
 * *****************  Version 15  *****************
 * User: Remember     Date: 98-10-23   Time: 2:05a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 *
 * *****************  Version 14  *****************
 * User: Remember     Date: 98-10-17   Time: 4:08a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 *
 * *****************  Version 13  *****************
 * User: Remember     Date: 98-10-10   Time: 3:17a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * com.sun.java -> javax
 * 
 * *****************  Version 12  *****************
 * User: Remember     Date: 98-09-24   Time: 2:21a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * package browser와의 연계완료
 *
 * *****************  Version 11  *****************
 * User: Remember     Date: 98-09-23   Time: 6:35a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * Class 이름 바꾸었음.
 *
 * *****************  Version 10  *****************
 * User: Remember     Date: 98-09-21   Time: 9:45p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 *
 * *****************  Version 9  *****************
 * User: Remember     Date: 98-09-18   Time: 10:28p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * 
 * *****************  Version 8  *****************
 * User: Remember     Date: 98-09-18   Time: 12:15a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * 
 * *****************  Version 7  *****************
 * User: Remember     Date: 98-09-16   Time: 5:58p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 *
 * *****************  Version 6  *****************
 * User: Remember     Date: 98-09-16   Time: 12:03p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * 
 * *****************  Version 5  *****************
 * User: Remember     Date: 98-09-12   Time: 2:25a
 * Updated in $/Ant/src/ant/designer/classdesigner
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 98-09-11   Time: 1:17a
 * Updated in $/Ant/src/ant/designer/classdesigner
 * import list, extends text field package browser와 연동 완결
 *
 * *****************  Version 3  *****************
 * User: Remember     Date: 98-09-10   Time: 3:20a
 * Updated in $/Ant/src/ant/designer/classdesigner
 * event hander 추가
 *
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-09-07   Time: 10:07p
 * Updated in $/Ant/src/ant/designer/classdesigner
 * package name 변경
 *
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-09-03   Time: 12:59a
 * Created in $/Ant/src
 * Attrubute add GUI
 *
 */
package com.antsoft.ant.designer.classdesigner;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import com.antsoft.ant.browser.packagebrowser.*;
import com.antsoft.ant.codecontext.CodeContext;
import com.antsoft.ant.manager.projectmanager.ProjectManager;
import com.antsoft.ant.util.*;
import com.antsoft.ant.pool.librarypool.LibraryPool;
import com.antsoft.ant.pool.classpool.ClassPool;

/**
 * @author  Kim sang kyun
 */
public class GeneralPanel extends JPanel{
  private JTextField packageTf, classTf, extendsTf, docTf;
  private String extendsPackageName;
  private JList importList, implementsList;
  private JButton importAddBtn, importRemoveBtn, implementsAddBtn,
                  implementsRemoveBtn, extendsFindBtn, packageFindBtn;
  private JComboBox javaDocCombo;
  private DefaultListModel importM;
  private ImplementsListModel implementsM;
  private JScrollPane importListScrollPane, implementsListScrollPane;
  private Vector docM;
  private String see, author, version, since, desc;
  private JCheckBox publicBtn, abstractBtn, finalBtn;

  private CDGeneralInfo info;
  private JavaDocInfo javaDoc = new JavaDocInfo();

  public GeneralPanel(CDGeneralInfo cdGeneralInfo) {
    this.info = cdGeneralInfo;
    this.info.setJavaDoc(javaDoc);

    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    setBorder(BorderList.etchedBorder5);
    ActionEventHandler actionEventHandler = new ActionEventHandler();
    ListSelectEventHandler listHandler = new ListSelectEventHandler();
    DocumentListener docListener = new DocumentEventHandler();

    //package label
    JLabel pkgLbl = new JLabel("     Package");
    pkgLbl.setPreferredSize(new Dimension(84,20));
    pkgLbl.setFont(FontList.regularFont);
    pkgLbl.setForeground(Color.black);
    packageFindBtn = new JButton("..");
    packageFindBtn.setPreferredSize(new Dimension(20,20));
    packageFindBtn.setFont(FontList.regularFont);

    packageFindBtn.setActionCommand("PackageFind");
    packageFindBtn.addActionListener(actionEventHandler);

    //package textfiled, button
    packageTf = new JTextField(13);
    //packageTf.setPreferredSize(new Dimension(150,20));
    packageTf.getDocument().putProperty("TYPE", "PACKAGE");
    packageTf.getDocument().addDocumentListener(docListener);

    //by lila
    JPanel p1 = new JPanel();
    p1.setLayout(new FlowLayout(FlowLayout.LEFT));
    p1.add(pkgLbl);
    p1.add(packageTf);
    p1.add(packageFindBtn);

    //import label
    JLabel importLbl = new JLabel("    Import");
    importLbl.setFont(FontList.regularFont);
    importLbl.setForeground(Color.black);

    //import add, remove button
    importAddBtn = new JButton("Add");
    importAddBtn.setFont(FontList.regularFont);
    importAddBtn.setActionCommand("ImportAdd");
    importAddBtn.addActionListener(actionEventHandler);

    importRemoveBtn = new JButton("Remove");
    importRemoveBtn.setFont(FontList.regularFont);
    importRemoveBtn.setActionCommand("ImportRemove");
    importRemoveBtn.addActionListener(actionEventHandler);

    //by lila
    JPanel p2 = new JPanel();
    p2.setLayout(new GridLayout(3,1));
    p2.add(importLbl);
    p2.add(importAddBtn);
    p2.add(importRemoveBtn);

    importM = new DefaultListModel();
    importList = new JList(importM);
    importList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    importList.setSelectedIndex(0);
    importList.addListSelectionListener(listHandler);
    importListScrollPane = new JScrollPane(importList);
    //importList.setPreferredSize(new Dimension(200,150));
    importListScrollPane.setPreferredSize(new Dimension(170,150));

    JPanel p3 = new JPanel();
    p3.setLayout(new BorderLayout());
    p3.add(p2,BorderLayout.NORTH);
    p3.add(new JPanel(),BorderLayout.CENTER);

    JPanel p4 = new JPanel();
    p4.setLayout(new BorderLayout(5,0));
    p4.add(p3,BorderLayout.WEST);
    p4.add(importListScrollPane,BorderLayout.CENTER);

    JPanel p5 = new JPanel();
    p5.setLayout(new BorderLayout());
    p5.setBorder(new EtchedBorder());
    p5.add(p1,BorderLayout.NORTH);
    p5.add(p4,BorderLayout.CENTER);
    p5.add(new JPanel(),BorderLayout.EAST);
    p5.add(new JPanel(),BorderLayout.WEST);
    p5.add(new JPanel(),BorderLayout.SOUTH);

    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    leftPanel.add(p5);

    //extends label
    JLabel extendsLbl = new JLabel("    Extends");
    extendsLbl.setFont(FontList.regularFont);
    extendsLbl.setForeground(Color.black);
    extendsLbl.setPreferredSize(new Dimension(84,20));

    extendsFindBtn = new JButton("..");
    extendsFindBtn.setFont(FontList.regularFont);
    extendsFindBtn.setPreferredSize(new Dimension(20,20));
    extendsFindBtn.setActionCommand("ExtendsFind");
    extendsFindBtn.addActionListener(actionEventHandler);

    //extends textfield, find button
    extendsTf = new JTextField(13);
    extendsTf.getDocument().putProperty("TYPE", "EXTENDS");
    extendsTf.getDocument().addDocumentListener(docListener);

    //by lila
    JPanel p6 = new JPanel();
    p6.setLayout(new FlowLayout(FlowLayout.LEFT));
    p6.add(extendsLbl);
    p6.add(extendsTf);
    p6.add(extendsFindBtn);

    //implements label
    JLabel implementsLbl = new JLabel("  Implements ");
    implementsLbl.setFont(FontList.regularFont);
    implementsLbl.setForeground(Color.black);
    implementsLbl.setPreferredSize(new Dimension(74,20));

    //implements add, remove button
    implementsAddBtn = new JButton("Add");
    implementsAddBtn.setFont(FontList.regularFont);
    implementsAddBtn.setActionCommand("ImplementsAdd");
    implementsAddBtn.addActionListener(actionEventHandler);

    implementsRemoveBtn = new JButton("Remove");
    implementsRemoveBtn.setFont(FontList.regularFont);
    implementsRemoveBtn.setActionCommand("ImplementsRemove");
    implementsRemoveBtn.addActionListener(actionEventHandler);

    //by lila
    JPanel p7 = new JPanel();
    p7.setLayout(new GridLayout(3,1));
    p7.add(implementsLbl);
    p7.add(implementsAddBtn);
    p7.add(implementsRemoveBtn);

    JPanel p8 = new JPanel();
    p8.setLayout(new BorderLayout());
    p8.add(p7,BorderLayout.NORTH);
    p8.add(createDummyPanel(), BorderLayout.CENTER);

    //implements List
    implementsM = new ImplementsListModel();
    implementsList = new JList(implementsM);
    implementsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    implementsList.setSelectedIndex(0);
    implementsList.addListSelectionListener(listHandler);
    implementsListScrollPane = new JScrollPane(implementsList);
    implementsListScrollPane.setPreferredSize(new Dimension(170,150));

    //by lila
    JPanel p9 = new JPanel();
    p9.setLayout(new BorderLayout(5,0));
    p9.add(p8,BorderLayout.WEST);
    p9.add(implementsListScrollPane,BorderLayout.CENTER);

    JPanel p10 = new JPanel();
    p10.setLayout(new BorderLayout());
    p10.setBorder(new EtchedBorder());
    p10.add(p6,BorderLayout.NORTH);
    p10.add(p9,BorderLayout.CENTER);
    p10.add(createDummyPanel(),BorderLayout.EAST);
    p10.add(createDummyPanel(),BorderLayout.WEST);
    p10.add(createDummyPanel(),BorderLayout.SOUTH);

    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    rightPanel.add(p10);

    //class modifier
    JLabel classModifierLbl = new JLabel("Class Modifier");
    classModifierLbl.setPreferredSize(new Dimension(90,20));
    classModifierLbl.setFont(FontList.regularFont);
    classModifierLbl.setForeground(Color.black);

    //public, abstract, final Checkbox, RadioButton
    publicBtn = new JCheckBox("public", true);
    publicBtn.setPreferredSize(new Dimension(55,20));
    publicBtn.setFont(FontList.regularFont);
    publicBtn.setActionCommand("PUBLIC_BUTTON");
    publicBtn.addActionListener(actionEventHandler);

    abstractBtn = new JCheckBox("abstract", false);
    abstractBtn.setPreferredSize(new Dimension(70,20));
    abstractBtn.setFont(FontList.regularFont);
    abstractBtn.setActionCommand("ABSTRACT_BUTTON");
    abstractBtn.addActionListener(actionEventHandler);

    finalBtn = new JCheckBox("final", false);
    finalBtn.setPreferredSize(new Dimension(70,20));
    finalBtn.setFont(FontList.regularFont);
    finalBtn.setActionCommand("FINAL_BUTTON");
    finalBtn.addActionListener(actionEventHandler);

    //class name label, textfield
    JLabel classLbl = new JLabel("Class Name");
    //classLbl.setPreferredSize(new Dimension(60,20));
    classLbl.setFont(FontList.regularFont);
    classLbl.setForeground(Color.black);

    classTf = new JTextField(15);
    classTf.getDocument().putProperty("TYPE", "CLASS");
    classTf.getDocument().addDocumentListener(docListener);

    JLabel label = new JLabel("  ");
    label.setPreferredSize(new Dimension(17,20));

    //by lila
    JPanel p11 = new JPanel();
    p11.setLayout(new FlowLayout(FlowLayout.LEFT));
    p11.add(classModifierLbl);
    p11.add(publicBtn);
    p11.add(abstractBtn);
    p11.add(finalBtn);
    //p11.add(label);
    p11.add(classLbl);
    p11.add(classTf);

    //javadoc
    docM = new Vector();
    docM.addElement("JavaDoc");
    docM.addElement("See");
    docM.addElement("Author");
    docM.addElement("Version");
    docM.addElement("Since");

    javaDocCombo = new JComboBox(docM);
    javaDocCombo.setFont(FontList.regularFont);
    javaDocCombo.setSelectedItem(docM.elementAt(0));
    javaDocCombo.setActionCommand("JavaDoc");
    javaDocCombo.addActionListener(actionEventHandler);
    docTf = new JTextField(40);
    docTf.getDocument().putProperty("TYPE", "JAVADOC");
    docTf.getDocument().addDocumentListener(docListener);

    docTf.setText("JavaDoc Comment Setting");
    docTf.setEditable(false);

    //by lila
    JPanel p12 = new JPanel();
    p12.setLayout(new FlowLayout(FlowLayout.LEFT));
    p12.add(javaDocCombo);
    p12.add(new JLabel("   "));
    p12.add(docTf);

    JPanel p13 = new JPanel();
    p13.setLayout(new GridLayout(2,1));
    p13.add(p11);
    p13.add(p12);

    JPanel p14 = new JPanel();
    p14.setLayout(new BorderLayout());
    p14.setBorder(new EtchedBorder());
    p14.add(p13,BorderLayout.CENTER);
    p14.add(createDummyPanel(),BorderLayout.WEST);
    p14.add(createDummyPanel(),BorderLayout.EAST);

    JPanel bottom = new JPanel();
    bottom.setLayout(new FlowLayout(FlowLayout.CENTER));
    bottom.add(p14);

    this.setLayout(new FlowLayout(FlowLayout.LEFT));
    add(leftPanel);
    add(rightPanel);
    add(bottom);

    /*
    p1.setBackground(new Color(166, 201, 213));
    p2.setBackground(new Color(166, 201, 213));
    p3.setBackground(new Color(166, 201, 213));
    p4.setBackground(new Color(166, 201, 213));
    p5.setBackground(new Color(166, 201, 213));
    p6.setBackground(new Color(166, 201, 213));
    p7.setBackground(new Color(166, 201, 213));
    p8.setBackground(new Color(166, 201, 213));
    p9.setBackground(new Color(166, 201, 213));
    p10.setBackground(new Color(166, 201, 213));
    p11.setBackground(new Color(166, 201, 213));
    p12.setBackground(new Color(166, 201, 213));
    p13.setBackground(new Color(166, 201, 213));
    p14.setBackground(new Color(166, 201, 213));

    this.setBackground(new Color(166, 201, 213));
    */
  }

  private JPanel createDummyPanel(){
    JPanel p = new JPanel();
    return p;
  }

  /**
   * packageName TextField에 packageName을 삽입한다.
   *
   * @param packagename  삽입될 package name
   */
  public void setPackageName(String packagename){
    packageTf.setText(packagename);
    packageTf.setToolTipText(packagename);
  }

  public void removeImportList(int index){
    String packageName = null;
    String removedElement = (String)importM.elementAt(index);
    info.removeImportAt(index);

    if(removedElement.endsWith("*")){
      packageName = removedElement.substring(0, removedElement.lastIndexOf("*")-1);
      if(extendsPackageName!=null && extendsPackageName.equals(packageName)){
        extendsTf.setText(packageName + "." + extendsTf.getText());
      }
      String interfacePackage = null;
      for(int i=0; i<implementsM.getSize(); i++){
        interfacePackage = (String)implementsM.getPackageName(i);
        if(interfacePackage.equals(packageName)){
          String newName = packageName + "." + implementsM.getElementAt(i);
          implementsM.setElementAt(newName, i);
          info.setImplementsAt(newName, i);
        }
      }
    }
    else{
      packageName = removedElement.substring(0, removedElement.lastIndexOf("."));

      if(extendsPackageName != null && extendsPackageName.equals(removedElement)){
        extendsTf.setText(packageName + "." + extendsTf.getText());
        extendsPackageName = "";
      }
      String interfacePackage = null;
      for(int i=0; i<implementsM.getSize(); i++){
        interfacePackage = (String)implementsM.getPackageName(i);
        if(interfacePackage.equals(removedElement)){
          implementsM.setElementAt(packageName + "." + implementsM.getElementAt(i), i);
        }
      }
    }
    importM.removeElementAt(index);
    implementsList.repaint();
    importListScrollPane.validate();
  }

  /**
   * ImportList DataModel에 data를 삽입한다
   *
   * @param importName  삽입될 package 또는 class name
   */
  public void addImportList(String importName, boolean all){
    importM.addElement(importName);

    /* GeneralInfo객체에 저장한다 */
    info.addImport(importName);

    /** package name을 구해서 같은 package에 속한 class들을 extend 하거나
        implements하면 short name으로 바꾼다 */
    String packageName = null;

    String oldExtends = extendsTf.getText();
    if(all){
      packageName = importName.substring(0, importName.lastIndexOf("*")-1);

      if(oldExtends.startsWith(packageName)){
        extendsPackageName = packageName;
        extendsTf.setText(oldExtends.substring(oldExtends.lastIndexOf(".")+1));
      }
      String interfaceName = null;
      for(int i=0; i<implementsM.getSize(); i++){
        interfaceName = (String)implementsM.getElementAt(i);
        if(interfaceName.startsWith(packageName)){
          String newName = interfaceName.substring(interfaceName.lastIndexOf(".")+1);
          implementsM.setElementAt(newName, i);
          info.setImplementsAt(newName, i);
          implementsM.setPackageAt(packageName, i);
        }
      }
    }
    else{
      packageName = importName.substring(0, importName.lastIndexOf("."));
      if(oldExtends.equals(importName)){
        extendsPackageName = importName;
        extendsTf.setText(oldExtends.substring(oldExtends.lastIndexOf(".")+1));
      }
      String interfaceName = null;
      for(int i=0; i<implementsM.getSize(); i++){
        interfaceName = (String)implementsM.getElementAt(i);
        if(interfaceName.equals(importName)){
          String newName = interfaceName.substring(interfaceName.lastIndexOf(".")+1);
          implementsM.setElementAt(newName, i);
          info.setImplementsAt(newName, i);
          implementsM.setPackageAt(importName, i);
        }
      }
    }
    implementsList.repaint();
    importListScrollPane.validate();
  }

  /**
   * ExtendsName TextField에 class name을 삽입한다.
   *
   * @param extendsName  삽입될 class name
   */
  public void setExtendsName(String extendsName){
    extendsTf.setText(extendsName);
    extendsTf.setToolTipText(extendsName);
  }

  /**
   * ImplementsList DataModel에 data를 삽입한다
   *
   * @param implementsName  interface name
   */
  public void addImplementsList(String interfaceName){
    implementsM.addElement(interfaceName);
    info.addImplements(interfaceName);
    implementsListScrollPane.validate();
  }

  public void setSee(String seeData){
    see = seeData;
  }

  public String getSee(){
    return see;
  }

  public void setAuthor(String authorData){
    author = authorData;
  }

  public String getAuthor(){
    return author;
  }

  public void setVersion(String versionData){
    version = versionData;
  }

  public String getVersion(){
    return version;
  }

  public void setSince(String sinceData){
    since = sinceData;
  }

  public String getSince(){
    return since;
  }

  public void setDescription(String descData){
    desc = descData;
  }

  public String getDescription(){
    return desc;
  }

  public String getClassName(){
    return classTf.getText();
  }

  class ImplementsListModel extends AbstractListModel{
    private Vector interfaceData, packageData;
   /** default constructor */
    public ImplementsListModel(){
      interfaceData = new Vector();
      packageData = new Vector();
    }

    public Object getElementAt(int index){
      return interfaceData.elementAt(index);
    }

    public boolean contains(Object obj){
      return interfaceData.contains(obj);
    }

    public void setPackageAt(String packageName, int index){
      packageData.setElementAt(packageName, index);
    }

    public void setElementAt(String interfaceName, int index){
      interfaceData.setElementAt(interfaceName, index);
    }

    public void removeElementAt(int index){
      interfaceData.removeElementAt(index);
      packageData.removeElementAt(index);
    }

    public Object getPackageName(int index){
      return packageData.elementAt(index);
    }

    public int getSize(){
      return interfaceData.size();
    }

    public void removeAllElements(){
      interfaceData.removeAllElements();
      packageData.removeAllElements();
    }

    public void addElement(String interfaceName){
      int index = interfaceData.size();
      interfaceData.addElement(interfaceName);
      packageData.addElement("");
      fireIntervalAdded(this, index, index);
    }
  }

  /**
   * Radio button의 상태가 변했을때 처리해준다
   */
  class ChangeEventHandler implements ChangeListener{
    public void stateChanged(ChangeEvent e){
    }
  }

  /**
   * Document Listener. text field나 textarea상에서 사용자가 변화를
   * 가했을때 즉각 정보를 변경시켜준다
   */
  class DocumentEventHandler implements DocumentListener{
    public void process(DocumentEvent e){
      String type = (String)e.getDocument().getProperty("TYPE");
      if(type.equals("PACKAGE")){
        if(packageTf.getText().equals("")) info.setPackageName(null);
        else info.setPackageName(packageTf.getText());
      }
      else if(type.equals("EXTENDS")){
        if(extendsTf.getText().equals("")) info.setSuperName(null);
        else info.setSuperName(extendsTf.getText());
      }
      else if(type.equals("CLASS")){
        if(classTf.getText().equals("")) info.setClassName(null);
        else info.setClassName(classTf.getText());
      }
      else if(type.equals("JAVADOC")){
        String item = (String)javaDocCombo.getSelectedItem();
        if(item.equals("See")){
          if(docTf.getText().equals("")){
            setSee(null);
            javaDoc.setSee(null);
          }
          else{
            setSee(docTf.getText());
            javaDoc.setSee(docTf.getText());
          }
        }
        else if(item.equals("Author")){
          if(docTf.getText().equals("")){
            setAuthor(null);
            javaDoc.setAuthor(null);
          }
          else{
            setAuthor(docTf.getText());
            javaDoc.setAuthor(docTf.getText());
          }
        }
        else if(item.equals("Version")){
          if(docTf.getText().equals("")) {
            setVersion(null);
            javaDoc.setVersion(null);
          }
          else{
            setVersion(docTf.getText());
            javaDoc.setVersion(docTf.getText());
          }
        }
        else if(item.equals("Since")){
          if(docTf.getText().equals("")) {
            setSince(null);
            javaDoc.setSince(null);
          }
          else{
            setSince(docTf.getText());
            javaDoc.setSince(docTf.getText());
          }
        }
        else if(item.equals("Desc")){
          if(docTf.getText().equals("")) {
            setDescription(null);
            javaDoc.setDescription(null);
          }
          else{
            setDescription(docTf.getText());
            javaDoc.setDescription(docTf.getText());
          }
        }
      }
    }

    public void insertUpdate(DocumentEvent e){ process(e);  }
    public void changedUpdate(DocumentEvent e){ process(e); }
    public void removeUpdate(DocumentEvent e){ process(e); }
  }

  //List Selection Event Handler
  class ListSelectEventHandler implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent e) {
    }
  }

  //Action event handler
  class ActionEventHandler implements ActionListener {
    private String actionCommand;
    private final String MESSAGE = "Set Project's JDK Property";

    public void actionPerformed(ActionEvent e){
      actionCommand = e.getActionCommand();
      if(actionCommand.equals("PackageFind")){
        PackageBrowser pvf = new PackageBrowser(ClassDesigner.parent, "User Package Browser", true);
        if(pvf.showUserPackage()) pvf.setVisible(true);
        else return;

        if(pvf.isOK()){
          String packageName = pvf.getSelectedPackageItem();

          //화면에 packagename setting
          setPackageName(packageName);
        }
      }
      else if(actionCommand.equals("ImportAdd")){
        //Jdk가 설정되어 있는지 체크한다
        if(LibraryPool.getProjectLibrarys(ProjectManager.getCurrentProject().getProjectName()) == null){
           JOptionPane.showMessageDialog(null, MESSAGE, "alert", JOptionPane.ERROR_MESSAGE);
           return;
        }

        final PackageBrowser pvf = new PackageBrowser(ClassDesigner.parent, "User Package Browser", true);
        pvf.setMultiSelectionListener(new MultiSelectionListener(){
               public void selectionPerformed(String selectedItem){
                 if(selectedItem != null && importM.contains(selectedItem) == false){
                    addImportList(selectedItem, false);
                 }
                 else{
                   String packageName = pvf.getSelectedPackageItem();
                   if(packageName != null){
                     String importName = packageName + ".*";
                     if(importM.contains(importName) == false) addImportList(importName, true);
                   }}
               }});
        pvf.setVisible(true);
      }
      else if(actionCommand.equals("ImportRemove")){
        if(((DefaultListModel)importList.getModel()).getSize() > 0){
          int index = importList.getSelectedIndex();

          if(index > -1){
            removeImportList(index);
          }
        }
      }
      else if(actionCommand.equals("ExtendsFind")){
        //Jdk가 설정되어 있는지 체크한다
        if(LibraryPool.getProjectLibrarys(ProjectManager.getCurrentProject().getProjectName()) == null){
           JOptionPane.showMessageDialog(null, MESSAGE, "alert", JOptionPane.ERROR_MESSAGE);
           return;
        }

        final PackageBrowser pvf = new PackageBrowser(ClassDesigner.parent, "User Package Browser", true);
        pvf.setVisible(true);

        if(pvf.isOK() == true){
          String fullClassName = pvf.getSelectedClassItem();
          if(fullClassName == null) return;
          boolean isInterface = false;
          try{
            isInterface = ClassPool.isInterface(fullClassName);
          }
          catch(ClassNotFoundException e1){
            e1.printStackTrace();
          }

          if( (fullClassName != null) && !isInterface){
            String packageAll = fullClassName.substring(0, fullClassName.lastIndexOf(".")+1) + "*";
            if(importM.indexOf(fullClassName) != -1 || importM.indexOf(packageAll) != -1){
              setExtendsName(fullClassName.substring(fullClassName.lastIndexOf(".")+1));
            }else{
              setExtendsName(fullClassName);
            }
          }
          else if(fullClassName != null){
            JOptionPane.showMessageDialog(null, "You Selected Interface", "Error", JOptionPane.ERROR_MESSAGE, ClassDesigner.errorIcon );
          }
        }
      }
      else if(actionCommand.equals("ImplementsAdd")){
        //Jdk가 설정되어 있는지 체크한다
        if(LibraryPool.getProjectLibrarys(ProjectManager.getCurrentProject().getProjectName()) == null){
           JOptionPane.showMessageDialog(null, MESSAGE, "alert", JOptionPane.ERROR_MESSAGE);
           return;
        }

        final PackageBrowser pvf = new PackageBrowser(ClassDesigner.parent, "User Package Browser", true);
        pvf.setMultiSelectionListener(new MultiSelectionListener(){
               public void selectionPerformed(String selectedItem){
                 if(selectedItem != null){
                   boolean isInterface = false;
                   try{
                     isInterface = ClassPool.isInterface(selectedItem);
                   }
                   catch(ClassNotFoundException e1){
                     e1.printStackTrace();
                   }
                   if(!isInterface){
                     JOptionPane.showMessageDialog(null, "It is not Interface", "Error", JOptionPane.ERROR_MESSAGE, ClassDesigner.errorIcon );
                     return;
                   }
                   String packageAll = selectedItem.substring(0, selectedItem.lastIndexOf(".")+1) + "*";
                   if(importM.indexOf(selectedItem) != -1 || importM.indexOf(packageAll) != -1){
                     if(!implementsM.contains(selectedItem.substring(selectedItem.lastIndexOf(".")+1))){
                       addImplementsList(selectedItem.substring(selectedItem.lastIndexOf(".")+1));
                     }
                   }else{
                     if(implementsM.contains(selectedItem) == false) addImplementsList(selectedItem);
                   }}}});
        pvf.setVisible(true);
      }
      else if(actionCommand.equals("ImplementsRemove")){
        if(((ImplementsListModel)implementsList.getModel()).getSize() > 0){
          int index = implementsList.getSelectedIndex();
          if(index > -1){
            implementsM.removeElementAt(index);
            info.removeImplementsAt(index);
          }
          implementsListScrollPane.validate();
          implementsListScrollPane.repaint();
        }
      }
      else if(actionCommand.equals("JavaDoc")){
        String item = (String)javaDocCombo.getSelectedItem();
        if(item.equals("See")){
          docTf.setEditable(true);
          docTf.setText(getSee());
        }
        else if(item.equals("Author")){
          docTf.setEditable(true);
          docTf.setText(getAuthor());
        }
        else if(item.equals("Version")){
          docTf.setEditable(true);
          docTf.setText(getVersion());
        }
        else if(item.equals("Since")){
          docTf.setEditable(true);
          docTf.setText(getSince());
        }
        else if(item.equals("JavaDoc")){
          docTf.setEditable(false);
          docTf.setText("JavaDoc Comment Setting");
        }
      }

      else if(actionCommand.equals("PUBLIC_BUTTON")){
        info.setPublic(publicBtn.isSelected());
      }

      else if(actionCommand.equals("ABSTRACT_BUTTON")){
        info.setAbstract(abstractBtn.isSelected());
      }

      else if(actionCommand.equals("FINAL_BUTTON")){
        info.setFinal(finalBtn.isSelected());
      }
    }
  }
}

