/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co.
 * All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/browser/packagebrowser/PackageBrowser.java,v 1.9 1999/08/20 08:37:05 remember Exp $
 * $Revision: 1.9 $
 * $History: PackageBrowser.java $
 * 
 * *****************  Version 15  *****************
 * User: Remember     Date: 99-06-10   Time: 6:06p
 * Updated in $/AntIDE/source/ant/browser/packagebrowser
 * 
 * *****************  Version 14  *****************
 * User: Remember     Date: 99-05-28   Time: 12:04p
 * Updated in $/AntIDE/source/ant/browser/packagebrowser
 * 
 * *****************  Version 13  *****************
 * User: Remember     Date: 99-05-27   Time: 12:54p
 * Updated in $/AntIDE/source/ant/browser/packagebrowser
 * 
 * *****************  Version 12  *****************
 * User: Remember     Date: 99-05-26   Time: 6:11p
 * Updated in $/AntIDE/source/ant/browser/packagebrowser
 * 
 * *****************  Version 11  *****************
 * User: Remember     Date: 99-05-20   Time: 8:20p
 * Updated in $/AntIDE/source/ant/browser/packagebrowser
 *
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-10-16   Time: 12:45a
 * Created in $/JavaProjects/src/ant/browser/packagebrowser
 *
 */
package com.antsoft.ant.browser.packagebrowser;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.File;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.tree.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.DefaultListModel;
import javax.swing.ComboBoxModel;

import com.antsoft.ant.pool.librarypool.*;
import com.antsoft.ant.pool.packagepool.*;
import com.antsoft.ant.designer.classdesigner.*;
import com.antsoft.ant.codecontext.*;
import com.antsoft.ant.manager.projectmanager.*;
import com.antsoft.ant.main.MainFrame;
import com.antsoft.ant.util.QuickSorter;
import com.antsoft.ant.util.WindowDisposer;

/**
  * package browser main class
  *
  * @author Kim sang kyun
  */
final  public class PackageBrowser extends JDialog {
   private TreeViewPanel treeViewTab;
   private ListViewPanel listViewTab;
   private JTabbedPane tabbedPane;
   private JTree tree;
   private JList packageList, classList, methodList;
   private JButton selectBtn, cancelBtn, exitBtn;
   private JComboBox libraryCombo;

   private boolean isPackage = false;

   private final String EMPTY_NODE = "Empty";

   private final int LIBRARY_DEPTH = 1;
   private final int PACKAGE_DEPTH = 2;
   private final int CLASS_DEPTH = 3;
   private final int METHOD_DEPTH = 4;

   private boolean isOK = false;

   private String selectedClassItem;
   private String selectedPackageItem;

   private PackageBrowserTreeCellRenderer treeRenderer;
   private LibComboCellRenderer libListRenderer;
   private PackageListCellRenderer packageListRenderer;
   private ClassListCellRenderer classListRenderer;

   private MultiSelectionListener msl;
   private int mulSelectionType;
   private JPanel btnP;

   public PackageBrowser(JFrame frame, String title, boolean modal) {
     super(MainFrame.mainFrame,title, modal);

		 addWindowListener(WindowDisposer.getDisposer());
     addKeyListener(WindowDisposer.getDisposer()); 		

     ActionEventHandler actionEventH = new ActionEventHandler();
     LibComboActionEvnetHandler libComboHandler = new LibComboActionEvnetHandler();
     PackageListSelectionEventHandler packageListSelectionEventH = new PackageListSelectionEventHandler();
     ClassListSelectionEventHandler classListSelectionEventH = new ClassListSelectionEventHandler();
     TreeExpansionEventHandler treeExpansionEventH = new TreeExpansionEventHandler();

     tabbedPane = new JTabbedPane();

     //List view tab
     libListRenderer = new LibComboCellRenderer();
     packageListRenderer = new PackageListCellRenderer();
     classListRenderer = new ClassListCellRenderer();

     libraryCombo = new JComboBox();
     libraryCombo.setRenderer(libListRenderer);
     libraryCombo.addActionListener(libComboHandler);

     packageList = new JList();
     packageList.setCellRenderer(packageListRenderer);
     packageList.addListSelectionListener(packageListSelectionEventH);

     classList = new JList();
     classList.setCellRenderer(classListRenderer);
     classList.addListSelectionListener(classListSelectionEventH);

     listViewTab = new ListViewPanel(libraryCombo, packageList, classList);

     tabbedPane.addTab("ListView", listViewTab);
     tabbedPane.setTabPlacement(SwingConstants.TOP);
     tabbedPane.setSelectedIndex(0);

     tree = new JTree();
     tree.putClientProperty("JTree.lineStyle", "Angled");
     tree.addTreeExpansionListener(treeExpansionEventH);

     treeRenderer = new PackageBrowserTreeCellRenderer();
     tree.setCellRenderer(treeRenderer);

     //Tree view tab
     treeViewTab = new TreeViewPanel(tree);
     treeViewTab.setPackageBrowser(this);
     tabbedPane.addTab("TreeView", treeViewTab);

     //buttons panel
     selectBtn = new JButton("Select");
     selectBtn.setActionCommand("Select");
     selectBtn.addActionListener(actionEventH);
     selectBtn.setEnabled(true);

     cancelBtn = new JButton("Close");
     cancelBtn.setActionCommand("Close");
     cancelBtn.addActionListener(actionEventH);

     btnP = new JPanel();
     FlowLayout btnL = new FlowLayout();
     btnP.setLayout(btnL);

     btnP.add(selectBtn);
     btnP.add(cancelBtn);

     setFirstData();

     getContentPane().add(tabbedPane, BorderLayout.CENTER);
     getContentPane().add(btnP, BorderLayout.SOUTH);
     getContentPane().add(new JPanel(),BorderLayout.WEST);
     getContentPane().add(new JPanel(),BorderLayout.EAST);

     setSize(250,480);
     setLocation(150, 150);
     this.setResizable(false);
   }

   final public void setOK(boolean flag){
     this.isOK = flag;
   }

   final public boolean isOK(){
     return this.isOK;
   }

   /**
    * tree view panel �� table view panel�� ó���� ���̴� data�� setting
    */
   final private void setFirstData(){

     //tree view data setting
     //���� project�� ���� library info���� ��´�

     Enumeration libs = LibraryPool.getProjectLibrarys(ProjectManager.getCurrentProject().getProjectName());
     TreePath rootPath = new TreePath(treeViewTab.getRootNode().getPath());

     if(libs != null)
     for(; libs.hasMoreElements(); ){
       LibraryInfo libInfo = (LibraryInfo)libs.nextElement();
       String libName = libInfo.getName();

       treeRenderer.addLibrarySink(libName);

       //library name�� tree�� add�Ѵ�
       DefaultMutableTreeNode libNode;

       libNode = treeViewTab.addObjectToRoot(libName);
       treeViewTab.addObject(libNode, EMPTY_NODE);
       TreePath libPath = new TreePath(libNode.getPath());

       //library name�� list�� add�Ѵ�
       listViewTab.addLibraryData(libName);
     }

     if(ProjectManager.getCurrentProject().getPathModel().getOutputRoot() == null) return;

     String label = "User Packages";
     treeRenderer.addLibrarySink(label);

     DefaultMutableTreeNode userNode;

     userNode = treeViewTab.addObjectToRoot(label);
     treeViewTab.addObject(userNode, EMPTY_NODE);

     //library name�� list�� add�Ѵ�
     listViewTab.addLibraryData(label);
     /////////////////////////////////////////////////////////////////////////
   }

   /**
    * ���õ� class name�� �����Ѵ�
    *
    * @param item selected class name
    */
   final public void setSelectedClassItem(String item){
     this.selectedClassItem = item;
   }

   /**
    * ���õ� package name�� �����Ѵ�
    *
    * @param item selected package name
    */
   final public void setSelectedPackageItem(String item){
     this.selectedPackageItem = item;
   }

   /**
    * ���õ� package name�� ��ȯ�Ѵ�
    *
    * @return selected package item
    */
   final public String getSelectedPackageItem(){
     return this.selectedPackageItem;
   }

   /**
    * ���õ� class name�� ��ȯ�Ѵ�
    *
    * @return selected class name
    */
   final public String getSelectedClassItem(){
     return this.selectedClassItem;
   }

   final public void setMultiSelectionListener(MultiSelectionListener msl){
     this.msl = msl;
   }

   final public void fireMultiSelectionPerformed(String selectedItem){
     msl.selectionPerformed(selectedItem);
   }

   final public void dispose(){
     super.dispose();
     System.gc();
   }

   // action event handler inner class
   final class ActionEventHandler implements ActionListener {
     public void actionPerformed(ActionEvent e){
       String comm = e.getActionCommand();

       if(comm.equals("Select")){
         if(msl == null){
           setOK(true);
           dispose();
         }
         else{
           msl.selectionPerformed(getSelectedClassItem());
           setSelectedClassItem(null);
           setSelectedPackageItem(null);
         }
       }
       else if(comm.equals("Close")){
         setOK(false);
         dispose();
       }

       else if(comm.equals("CLOSE")){
         dispose();
       }
     }
   }

   //tree expansion event handler inner class

   final class TreeExpansionEventHandler implements TreeExpansionListener{
     public void treeCollapsed(TreeExpansionEvent event){
     }

     public void treeExpanded(TreeExpansionEvent event){

       TreePath path = event.getPath();
       DefaultMutableTreeNode expandedNode = (DefaultMutableTreeNode)path.getLastPathComponent();

       //constructor parent node ��쿡�� null�� �ȴ�. �ֳĸ� treeNodes�� �־����
       //�ʰ� ClassDepth���� �ٷ� �־������ �����̴�
       if(expandedNode != null){
         //expansion�� ���� ������
         if(expandedNode.getChildCount() == 1){

           MutableTreeNode childNode = (MutableTreeNode)expandedNode.getChildAt(0);

           //ù��° node��  EMPTY_NODE�̸�
           if(EMPTY_NODE.equals(childNode.toString())){
             treeViewTab.removeNodeFromParent(childNode);
             int depth = expandedNode.getLevel();

             if(depth == LIBRARY_DEPTH){
               if(expandedNode.toString().equals("User Packages")) userLibraryExpanded(expandedNode);
               else libraryExpanded(expandedNode);
             }
             else if(depth == PACKAGE_DEPTH){

               //Library check
               DefaultMutableTreeNode parent = (DefaultMutableTreeNode)expandedNode.getParent();
               String libName = parent.getUserObject().toString();
               LibraryInfo libInfo = LibraryPool.getProjectLibraryInfo(ProjectManager.getCurrentProject().getProjectName(), libName);
               if(libInfo != null){
                 Enumeration libPaths = libInfo.getFiles();
                 String packageName = expandedNode.getUserObject().toString();
                 PackageInfo pInfo = PackagePool.getPackageInfo(libInfo.getName(), libPaths, packageName);
                 if(pInfo != null) packageExpanded(pInfo, expandedNode);
               }
               else{
                 //User check
                 Vector packages = PackagePool.getUserPackage(expandedNode.toString());
                 if(packages != null) userPackageExpanded(expandedNode, packages);
               }  
             }
           }
         }
       }
     }

     /**
      * library node�� ó������ expanded �Ǿ����� ȣ���
      *
      * @param expandedNode expanded node
      */

     final public void libraryExpanded(DefaultMutableTreeNode expandedNode){

       String libName = expandedNode.getUserObject().toString();
       LibraryInfo libInfo = LibraryPool.getProjectLibraryInfo(ProjectManager.getCurrentProject().getProjectName(), libName);

       PackageContainer packageInfos = PackagePool.getPackageInfos(libInfo);
       Enumeration packageNames = packageInfos.getPackageNames();

       for(; packageNames.hasMoreElements(); ){
         String packageName = (String)packageNames.nextElement();
         DefaultMutableTreeNode packageNode;

         //package name�� tree�� add�Ѵ�
         treeRenderer.addPackageSink(packageName);
         packageNode = treeViewTab.addObject(expandedNode, packageName);

         if(packageInfos.getPackageInfo(packageName).getSize() > 0){
           treeViewTab.addObject(packageNode, EMPTY_NODE);
         }
       }
     }

     /**
      * user library node�� ó������ expanded �Ǿ����� ȣ���
      *
      * @param expandedNode expanded node
      */

     final public void userLibraryExpanded(DefaultMutableTreeNode expandedNode){

       Vector sorted = QuickSorter.sort(PackagePool.getUserPackages(), QuickSorter.LESS_STRING);
       for(int i=0; i<sorted.size(); i++){
         String userPackageName = ((StringBuffer)sorted.elementAt(i)).toString();
         DefaultMutableTreeNode userPackageNode;

         //package name�� tree�� add�Ѵ�
         treeRenderer.addPackageSink(userPackageName);
         userPackageNode = treeViewTab.addObject(expandedNode, userPackageName);
         treeViewTab.addObject(userPackageNode, EMPTY_NODE);
       }
     }

     /**
      * user package node�� ó������ expanded �Ǿ����� ȣ���
      *
      * @param expandedNode expanded node
      */

     private void userPackageExpanded(DefaultMutableTreeNode expandedNode, Vector packages){

       Vector sorted = QuickSorter.sort(packages, QuickSorter.LESS_STRING);
       for(int i=0; i<sorted.size(); i++){
         treeViewTab.addObject(expandedNode, (String)sorted.elementAt(i));
       }
     }

     /**
      * package node�� ó������ expanded �Ǿ����� ȣ���
      *
      * @param pInfo packageInfo
      */

     private void packageExpanded(PackageInfo pInfo, DefaultMutableTreeNode expandedNode){

       //Ȯ���� ���� class name���� ��´�
       Enumeration classNames = pInfo.getClassNamesWithoutExtension();;

       //class�� tree�� add�Ѵ�
       for(; classNames.hasMoreElements(); ){
         String className = (String)classNames.nextElement();
         DefaultMutableTreeNode classNode;
         treeViewTab.addObject(expandedNode, className);
       }
     }
   }


   final public boolean showUserPackage(){
     ComboBoxModel m = libraryCombo.getModel();
     for(int i=0; i<m.getSize(); i++){
       if(((String)m.getElementAt(i)).equals("User Packages")){
         libraryCombo.setSelectedIndex(i);
         return true;
       }
     }
     return false;
   }

   // source browser���� package�� ���������� ���ȴ�
   public boolean showPackage(String packageName){

     tabbedPane.setSelectedIndex(0);
     Enumeration e = LibraryPool.getProjectLibrarys(ProjectManager.getCurrentProject().getProjectName());
     if(e != null)
     for(; e.hasMoreElements(); ){
       LibraryInfo info = (LibraryInfo)e.nextElement();
       PackagePool.getPackageInfos(info);

       if(PackagePool.getPackageInfo(info.getName(), info.getPath(), packageName) != null){

         libraryCombo.setSelectedItem(info.getName());
         packageList.setSelectedValue(packageName, true);
         btnP.remove(selectBtn);
         cancelBtn.setText("Close");
         cancelBtn.setActionCommand("CLOSE");
         return true;
       }
     }

     Vector v = PackagePool.getUserPackages();

     if(v != null)
     for(int i=0; i<v.size(); i++){
       if(packageName.equals(((StringBuffer)v.elementAt(i)).toString())){
         libraryCombo.setSelectedItem("User Packages");

         for(int j=0; j<packageList.getModel().getSize(); j++){
           if(packageName.equals((String)packageList.getModel().getElementAt(j))){
             listViewTab.setSelectedPackageIndex(j);
             btnP.remove(selectBtn);
             cancelBtn.setText("Close");
             cancelBtn.setActionCommand("CLOSE");
             return true;
           }
         }
         return false;
       }
     }


     dispose();
     return false;

   }

   ///////////////////////////////////////////////////////////////////////////////////

   //library combo box action event handler
   String prevSelItem = "";
   class LibComboActionEvnetHandler implements ActionListener{
     public void actionPerformed(ActionEvent e){

       String libName = (String)libraryCombo.getModel().getSelectedItem();
       if(libName == null || prevSelItem.equals(libName)) return;
       prevSelItem = libName;

       //package data�� �����ش�
       listViewTab.clearPackageData();
       //class data�� �����ش�
       listViewTab.clearClassData();
       //listViewPanel�� �������ش�
       listViewTab.repaint();


       if(!libName.equals("User Packages")){

         LibraryInfo libInfo = LibraryPool.getProjectLibraryInfo(ProjectManager.getCurrentProject().getProjectName(), libName);
         PackageContainer packageInfos = PackagePool.getPackageInfos(libInfo);
         Enumeration packageNames = packageInfos.getPackageNames();

         for(; packageNames.hasMoreElements(); ){
           String packageName = (String)packageNames.nextElement();
           listViewTab.addPackageData(packageName);
         }
       }
       else{
         Vector sorted = QuickSorter.sort(PackagePool.getUserPackages(), QuickSorter.LESS_STRING);
         for(int i=0; i<sorted.size(); i++){
           String userPackageName = ((StringBuffer)sorted.elementAt(i)).toString();
           listViewTab.addPackageData(userPackageName);
         }
       }
     }
   }

   /** package list selection evnet handler inner class */
   class PackageListSelectionEventHandler implements ListSelectionListener{
     public void valueChanged(ListSelectionEvent e){

       String libName = (String)libraryCombo.getSelectedItem();
       String packageName = (String)packageList.getSelectedValue();

       /** selectedPackageItem�� �����Ѵ� */
       selectedPackageItem = packageName;

       listViewTab.clearClassData();

       //Library check
       LibraryInfo libInfo = LibraryPool.getProjectLibraryInfo(ProjectManager.getCurrentProject().getProjectName(), libName);
       if(libInfo != null){
         Enumeration libPaths = libInfo.getFiles();
         PackageInfo pInfo = PackagePool.getPackageInfo(libInfo.getName(), libPaths, packageName);

         if(pInfo != null){
           //Ȯ���� ���� class name���� ��´�
           Enumeration classNames = pInfo.getClassNamesWithoutExtension();

           //class�� class list�� add�Ѵ�
           if(classNames != null)
           for(; classNames.hasMoreElements(); ){
             String className = (String)classNames.nextElement();
             listViewTab.addClassData(className);
           }
           listViewTab.repaint();
         }
       }
       else{
         //User check
         Vector packages = PackagePool.getUserPackage(packageName);

         if(packages != null){
           Vector sorted = QuickSorter.sort(packages, QuickSorter.LESS_STRING);
           for(int i=0; i<sorted.size(); i++){
             String className = (String)sorted.elementAt(i);
             listViewTab.addClassData(className);
           }
           listViewTab.repaint();
         }
       }
     }
   }


   /** class list selection evnet handler inner class */
   class ClassListSelectionEventHandler implements ListSelectionListener{
     public void valueChanged(ListSelectionEvent e){

       String packageName = (String)packageList.getSelectedValue();
       String className = (String)classList.getSelectedValue();
       /** selectedClassItem�� �����Ѵ� */
       setSelectedClassItem(packageName + "." + className);
     }
   }
 }



