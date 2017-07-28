/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co.
 * All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/browser/packagebrowser/ListViewPanel.java,v 1.4 1999/07/23 01:34:46 remember Exp $
 * $Revision: 1.4 $
 * $History: ListViewPanel.java $
 * 
 * *****************  Version 5  *****************
 * User: Remember     Date: 99-06-10   Time: 6:06p
 * Updated in $/AntIDE/source/ant/browser/packagebrowser
 * 
 * *****************  Version 4  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:25p
 * Updated in $/AntIDE/source/ant/browser/packagebrowser
 * 
 * *****************  Version 3  *****************
 * User: Remember     Date: 99-05-15   Time: 5:34a
 * Updated in $/AntIDE/source/ant/browser/packagebrowser
 * 
 * *****************  Version 2  *****************
 * User: Remember     Date: 99-05-11   Time: 11:23a
 * Updated in $/AntIDE/source/ant/browser/packagebrowser
 * 버그패치
 * 
 * *****************  Version 1  *****************
 * User: Remember     Date: 99-05-06   Time: 10:55p
 * Created in $/AntIDE/source/ant/browser/packagebrowser
 * 새로 했음
 * 
 * *****************  Version 5  *****************
 * User: Remember     Date: 98-10-23   Time: 2:05a
 * Updated in $/JavaProjects/src/ant/browser/packagebrowser
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 98-10-21   Time: 1:22a
 * Updated in $/JavaProjects/src/ant/browser/packagebrowser
 * 
 * *****************  Version 3  *****************
 * User: Remember     Date: 98-10-20   Time: 2:55a
 * Updated in $/JavaProjects/src/ant/browser/packagebrowser
 *
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-10-19   Time: 8:56a
 * Updated in $/JavaProjects/src/ant/browser/packagebrowser
 * 
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-10-17   Time: 1:30p
 * Created in $/JavaProjects/src/ant/browser/packagebrowser
 *
 */
 package com.antsoft.ant.browser.packagebrowser;


 import java.awt.event.*;
 import java.awt.*;
 import java.util.Vector;
 import java.util.Hashtable;
 import javax.swing.*;
 import javax.swing.event.*;

 import com.antsoft.ant.pool.classpool.*;
 import com.antsoft.ant.codecontext.*;


 /**
  * package browser의 list view panel
  *
  * @author Kim sang kyun
  */
final public class ListViewPanel extends JPanel {

   private PackageBrowser packageBrowser;
   private JList packageList, classList;
   private PackageListModel packageListModel;
   private ClassListModel classListModel;

   private DefaultComboBoxModel libraryModel;
   private JScrollPane packagePane, classPane, methodPane;
   private JSplitPane bottomPane, totalPane;

   private JComboBox libraryCombo;
   private SigModel sigModel;

   /** default constructor */
   public ListViewPanel(){}

   /**
    * constructor
    *
    * @param packageList package 를 보여주는 list
    * @param classList class와 interface를 보여주는 list
    */
   public ListViewPanel(JComboBox libraryCombo, JList packageList, JList classList){

     setLayout(new BorderLayout());

     libraryModel = new DefaultComboBoxModel();
     packageListModel = new PackageListModel();
     classListModel = new ClassListModel();

     this.libraryCombo = libraryCombo;
     libraryCombo.setModel(libraryModel);

     this.packageList = packageList;
     packageList.setModel(packageListModel);

     this.classList = classList;
     classList.setModel(classListModel);

     packagePane = new JScrollPane(packageList);
     classPane = new JScrollPane(classList);

     JPanel centerP = new JPanel(new GridLayout(2,1, 0, 10));
     centerP.add(packagePane);
     centerP.add(classPane);

     add(libraryCombo, BorderLayout.NORTH);
     add(centerP, BorderLayout.CENTER);
   }

   final public void setSelectedPackageIndex(int index){
     packageList.setSelectedIndex(index);

     int cell_height = packageList.getCellBounds(0,0).height;
     packagePane.getViewport().setViewPosition(new Point(0, (index-5)*cell_height));

   }

   /**
    * library combo box에 data를 add한다
    *
    * @param libData add될 data
    */
   final public void addLibraryData(String libData){
     libraryModel.addElement(libData);
   }

   /**
    * package list에 data를 add한다
    *
    * @param libName 현재 선택된 library name
    * @param packageName add할 package data
    */

   final public void addPackageData(String packageName){
     packageListModel.addElement(packageName);
   }

   /**
    * class list에 data를 add한다
    *
    * @param packageName 현재 선택된 package name
    * @param className add할 class data
    */
   final public void addClassData(String className){
     classListModel.addElement(className);
   }

   final public void clearAll(){
     clearLibraryData();
     clearPackageData();
     clearClassData();
   }

   final public void clearLibraryData(){
     libraryModel.removeAllElements();
   }

   final public void clearPackageData(){
     packageListModel.removeAllElements();
     packageList.setSelectedIndex(-1);
   }

   final public void clearClassData(){
     classListModel.removeAllElements();
     classList.setSelectedIndex(-1);
   }

   /**
   * 현재 선택된 sigmodel을 반환
   *
   * @return current selected sigmodel
   */
   final private SigModel getSelectedSigModel(){
     return sigModel;
   }

   /**
   * 현재 선택된 sigmodel을 설정
   *
   * @param model current selected sigmodel
   */
   final private void setSelectedSigModel(SigModel model){
     sigModel = model;
   }

   /**
    * package list의 model을 반환
    *
    * @return package list data model
    */
   final public PackageListModel getPackageListModel(){
     return packageListModel;
   }
 }

