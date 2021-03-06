/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/JdkComboBoxModel.java,v 1.3 1999/07/22 03:42:03 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 */
package com.antsoft.ant.property;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

/**
 * Jdk ComboBox Model class
 *
 * @author kim sang kyun
 */
public class JdkComboBoxModel extends AbstractListModel implements MutableComboBoxModel{
    private JdkInfoContainer jdkInfos;
    private Object selectedItem;

    public JdkComboBoxModel(){
      this.jdkInfos = new JdkInfoContainer();
    }

    public void setSelectedItem(Object anItem){
      selectedItem = anItem;
      fireContentsChanged(this, -1, -1);
    }

    public void setJdkInfoContainer(JdkInfoContainer jdkInfos){


      int size = jdkInfos.getSize();
      for(int i=0; i<size; i++)  addElement(jdkInfos.getJdkInfo(i));
    }

    public JdkInfoContainer getJdkInfoContainer(){
      return this.jdkInfos;
    }

    public Object getSelectedItem(){
      return selectedItem;
    }

    public void addElement(Object obj){
      jdkInfos.addJdkInfo(obj);
      fireIntervalAdded(this,jdkInfos.getSize()-1, jdkInfos.getSize()-1);
      setSelectedItem( obj );
    }

    public void removeElement(Object obj){
      int index = jdkInfos.getSize();
      jdkInfos.removeJdkInfo(obj);
      this.fireIntervalRemoved(this, index, index);
   }

    public void removeElementAt(int index){
  	  jdkInfos.removeJdkInfo(index);
      fireIntervalRemoved(this, index, index);
    }

    public void removeAll(){
      int size = jdkInfos.getSize();
      for(int i=size-1; i>=0; i--) removeElementAt(i);
    }

    public void insertElementAt(Object obj, int index){
      jdkInfos.insertJdkInfoAt(obj, index);
    }

    public int getSize(){
      return jdkInfos.getSize();
    }

    public Object getElementAt(int index){
      return jdkInfos.getJdkInfo(index);
    }

    public int indexOf(Object obj){
      return jdkInfos.indexOf(obj);
    }
}



