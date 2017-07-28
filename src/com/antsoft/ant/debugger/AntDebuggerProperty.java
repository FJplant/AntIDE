/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/AntDebuggerProperty.java,v 1.4 1999/08/04 01:22:11 itree Exp $
 * $Revision: 1.4 $
 * $History: AntDebuggerProperty.java $
 */

package com.antsoft.ant.debugger;

import java.util.Vector;
import java.util.Enumeration;
import java.io.*;
import java.awt.Rectangle;

public class AntDebuggerProperty implements Serializable{

  private Rectangle frameBound = null;
  private Vector userSourcePath = null;
  private Vector userClassPath = null;

  private int classMethodPaneSize, classSourcePaneSize, localWatchPaneSize, listWatchPaneSize;

  public AntDebuggerProperty() {
    classMethodPaneSize = 0;
    classSourcePaneSize = 0;
    localWatchPaneSize = 0;
    listWatchPaneSize = 0;
  }

  // AntDebuggerPanel Size property
  public Rectangle getLastFrameBound() {
    return frameBound;
  }
  public void setLastFrameBound(Rectangle bound) {
    frameBound = bound;
  }

  // AntDebugger SourcePath property
  public Vector getUserSourcePath() {
    return userSourcePath;
  }
  public void setUserSourcePath(String path) {
    if(userSourcePath == null)
      userSourcePath = new Vector();
    if(!userSourcePath.contains(path))
      userSourcePath.addElement(path);
  }

  public void setUserSourcePath(Vector path) {
    userSourcePath = path;
  }

  // AntDebugger ClassPath property
  public Vector getUserClassPath() {
    return userClassPath;
  }
  public void setUserClassPath(String path) {
    if(userClassPath == null)
      userClassPath = new Vector();
    if(!userClassPath.contains(path))
      userClassPath.addElement(path);
  }

  public void setUserClassPath(Vector path) {
    userClassPath = path;
  }

  // AntDebugger UI property
  public int getClassMethodPaneSize() {
    return classMethodPaneSize;
  }
  public void setClassMethodPaneSize(int size) {
    classMethodPaneSize = size;
  }
  public int getClassSourcePaneSize() {
    return classSourcePaneSize;
  }
  public void setClassSourcePaneSize(int size) {
    classSourcePaneSize = size;
  }
  public int getLocalWatchPaneSize() {
    return localWatchPaneSize;
  }
  public void setLocalWatchPaneSize(int size) {
    localWatchPaneSize = size;
  }
  public int getListWatchPaneSize() {
    return listWatchPaneSize;
  }
  public void setListWatchPaneSize(int size) {
    listWatchPaneSize = size;
  }

  public String toString(){
    StringBuffer buf = new StringBuffer();

    //version
    buf.append("##Do not modify next line!!!" + "\r\n");
    buf.append("ANTDEBUGGER_SETUP_VERSION=1.0" + "\r\n\r\n");

    if(frameBound != null)
    buf.append("Frame_Bound=" + frameBound.x + "#" + frameBound.y + "#" + frameBound.width + "#" + frameBound.height + "\r\n");

    //SourcePath
    if(userSourcePath != null){
      for(int i=0; i<userSourcePath.size(); i++)
        buf.append("User_Source_Path=" + (String)userSourcePath.elementAt(i) +"\r\n");
    }
    //Class Path
    if(userClassPath != null) {
      for(int i=0; i<userClassPath.size(); i++)
        buf.append("User_Class_Path=" + (String)userClassPath.elementAt(i) + "\r\n");
    }

    buf.append("CMP_Size=" + classMethodPaneSize + "\r\n");
    buf.append("CSP_Size=" + classSourcePaneSize + "\r\n");
    buf.append("LWP_Size=" + localWatchPaneSize + "\r\n");
    buf.append("LiWP_Size=" + listWatchPaneSize + "\r\n");

    return buf.toString();
  }

}