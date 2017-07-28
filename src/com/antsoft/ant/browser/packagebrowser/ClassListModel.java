/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co.
 * All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/browser/packagebrowser/ClassListModel.java,v 1.3 1999/07/22 03:13:25 multipia Exp $
 * $Revision: 1.3 $
 * $History: ClassListModel.java $
 * 
 * *****************  Version 5  *****************
 * User: Remember     Date: 99-06-10   Time: 6:06p
 * Updated in $/AntIDE/source/ant/browser/packagebrowser
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 99-05-20   Time: 8:19p
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
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-10-21   Time: 1:22a
 * Updated in $/JavaProjects/src/ant/browser/packagebrowser
 *
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-10-20   Time: 2:55a
 * Created in $/JavaProjects/src/ant/browser/packagebrowser
 *
 */

package com.antsoft.ant.browser.packagebrowser;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import java.util.Vector;

import com.antsoft.ant.pool.classpool.*;

/**
 * method list model class
 *
 * @author Kim sang kyun
 */
final public class ClassListModel extends DefaultListModel{
  final private Vector sigData = new Vector();

  /** default constructor */
  public ClassListModel(){}

  final public Object getElementAt(int index){
    return sigData.elementAt(index);
  }

  final public int getSize(){
    return sigData.size();
  }

  final public void removeAllElements(){
    sigData.removeAllElements();
  }

  final public void addElement(String sig){
    int index = sigData.size();
    sigData.addElement(sig);
  	fireIntervalAdded(this, index, index);
  }
}
