/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co.
 * All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/browser/packagebrowser/PackageListModel.java,v 1.3 1999/07/22 03:13:25 multipia Exp $
 * $Revision: 1.3 $
 * $History: PackageListModel.java $
 * 
 * *****************  Version 5  *****************
 * User: Remember     Date: 99-05-28   Time: 12:04p
 * Updated in $/AntIDE/source/ant/browser/packagebrowser
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 99-05-20   Time: 8:20p
 * Updated in $/AntIDE/source/ant/browser/packagebrowser
 *
 * *****************  Version 3  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:25p
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
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-10-19   Time: 8:57a
 * Created in $/JavaProjects/src/ant/browser/packagebrowser
 *
 */
package com.antsoft.ant.browser.packagebrowser;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import java.util.Hashtable;
import java.util.Vector;
import java.awt.event.*;

/**
 * package list model class
 *
 * @author Kim sang kyun
 */
public class PackageListModel extends DefaultListModel{
  private Vector data;

  /** default constructor */
  public PackageListModel(){
    data = new Vector();
  }

  /**
   * index에 해당하는 element를 반환
   *
   * @param index index
   * @return element
   */
  public Object getElementAt(int index){
    if(data.size() > index) return (String)data.elementAt(index);
    else return null;
  }

  public int getSize(){
    return data.size();
  }

  public void removeAllElements(){
    data.removeAllElements();
  }

  public void addElement(String packageName){
    int index = data.size();
    data.addElement(packageName);
    fireIntervalAdded(this, index, index);
  }
}
