/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/LibraryListModel.java,v 1.3 1999/07/22 03:42:03 multipia Exp $
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
 * Library ListModel class
 */
public class LibraryListModel extends AbstractListModel{

    private LibInfoContainer libInfos;

    public LibraryListModel(){
      this.libInfos = new LibInfoContainer();
    }

    public LibraryListModel(LibInfoContainer infos){
      this.libInfos = infos;
    }

    public int getSize(){
      return libInfos.getSize();
    }

    public LibInfoContainer getLibInfoContainer(){
      return this.libInfos;
    }  

    public Object getElementAt(int index){
      return libInfos.getLibraryInfo(index);
    }

    public void setElementAt(Object libInfo, int index){
      libInfos.setLibraryInfo(libInfo, index);
    }  

    public void addElement(Object libInfo){
      int index = libInfos.getSize();
      libInfos.addLibraryInfo(libInfo);
      fireIntervalAdded(this, index, index);
    }

    public void setLibInfoContainer(LibInfoContainer libInfos){
      this.libInfos = libInfos;
    }

    public void removeElement(Object obj){
      this.libInfos.removeLibraryInfo(obj);
    }

    public void removeElementAt(int index){
      libInfos.removeLibraryInfo(index);
      fireIntervalRemoved(this, index, index);
    }

    public void fireUpdate(Object updatedLibInfo, int index){
      fireContentsChanged(updatedLibInfo, index, index);
    }

    public int indexOf(Object obj){
      return libInfos.indexOf(obj);
    }
}

