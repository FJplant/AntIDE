/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co.
 * All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/browser/packagebrowser/PackageBrowserTreeCellRenderer.java,v 1.5 1999/08/17 05:42:24 remember Exp $
 * $Revision: 1.5 $
 * $History: PackageBrowserTreeCellRenderer.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:25p
 * Updated in $/AntIDE/source/ant/browser/packagebrowser
 * 
 * *****************  Version 1  *****************
 * User: Remember     Date: 99-05-06   Time: 10:55p
 * Created in $/AntIDE/source/ant/browser/packagebrowser
 * 새로 했음
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 98-10-23   Time: 2:05a
 * Updated in $/JavaProjects/src/ant/browser/packagebrowser
 *
 */
package com.antsoft.ant.browser.packagebrowser;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.util.*;
import javax.swing.tree.*;
import com.antsoft.ant.designer.classdesigner.ClassDesigner;
import com.antsoft.ant.util.*;

public class PackageBrowserTreeCellRenderer extends DefaultTreeCellRenderer {
   private TreeViewPanel treeViewPanel = new TreeViewPanel();
   private Hashtable librarySink, packageSink;

   /** default constructor */
   public PackageBrowserTreeCellRenderer(){
     super();
     librarySink = new Hashtable();
     packageSink = new Hashtable();
     setFont(FontList.treeFont);
   }

   public Component getTreeCellRendererComponent(JTree tree, Object value,
                                           	 	 boolean sel, boolean expanded,
                          						 boolean leaf, int row, boolean hasFocus)
   {
     super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus);
     String name = value.toString();

     if(isRoot(name)){
       setIcon(ImageList.packageBrowserRootNodeIcon);
     }
     else if(isLibrary(name)){
       setIcon(ImageList.packageBrowserLibNodeIcon);
     }
     else if(isPackage(name)){
       setIcon(ImageList.packageIcon);
     }
     else{
       setIcon(ImageList.classIcon);
     }

     setText(value.toString());

     return this;
   }

   /**
    * package sink 에 package name을 add한다
    *
    * @param packageName package name
    */
   public void addPackageSink(String packageName){
     packageSink.put(packageName, packageName);
   }

   /**
    * library sink 에 library name을 add한다
    *
    * @param libraryName library name
    */
   public void addLibrarySink(String libraryName){
     librarySink.put(libraryName, libraryName);
   }

   /**
    * root 인지를 판별한다
    *
    * @param name node name
    * @return root이면 true 아니면 false
    */
   public boolean isRoot(String name){
     if(name.equals("Packages")){
       return true;
     }
     else{
       return false;
     }
   }

   /**
    * package 인지를 판별한다
    *
    * @param name node name
    * @return package이면 true 아니면 false
    */
   private boolean isPackage(String name){
     if(packageSink.get(name) != null){
       return true;
     }
     else{
       return false;
     }
   }

   /**
    * library 인지를 판별한다
    *
    * @param name node name
    * @return library이면 true 아니면 false
    */
   private boolean isLibrary(String name){
     if(librarySink.get(name) != null){
       return true;
     }
     else{
       return false;
     }
   }
}
