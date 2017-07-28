/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/JdkListModel.java,v 1.3 1999/07/22 03:42:03 multipia Exp $
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

/**
 * JdkListModel class
 */
public class JdkListModel extends AbstractListModel{

    private JdkInfoContainer jdkInfos;

    public JdkListModel(JdkInfoContainer jdkInfos){
      this.jdkInfos = jdkInfos;
    }

    public int getSize(){
      return jdkInfos.getSize();
    }

    public Object getElementAt(int index){
      return jdkInfos.getJdkInfo(index);
    }

    public boolean exist(Object jdkInfo){
      if(jdkInfos != null && jdkInfos.getJdkInfo(jdkInfo.toString()) != null) return true;
      else return false;
    }

    public void addElement(Object jdkInfo){
      int index = jdkInfos.getSize();
      jdkInfos.addJdkInfo(jdkInfo);
      fireIntervalAdded(this, index, index);
    }

    public void setJdkInfoContainer(JdkInfoContainer jdkInfos){
      this.jdkInfos = jdkInfos;
    }

    public void removeElementAt(int index){
      jdkInfos.removeJdkInfo(index);
      fireIntervalRemoved(this, index, index);
    }

    public void fireUpdate(Object updatedJdkInfo, int index){
      fireContentsChanged(updatedJdkInfo, index, index);
    }
}

