/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/JdkInfoContainer.java,v 1.3 1999/07/22 03:42:03 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 */
package com.antsoft.ant.property;

import java.util.Vector;
import java.util.Enumeration;
import java.io.Serializable;

/**
 * JdkInfo object container
 *
 * @author kim, sang kyun
 */
public class JdkInfoContainer implements Serializable, Cloneable {

  private Vector jdkInfos;

  /** default constructor */
  public JdkInfoContainer() {
    jdkInfos = new Vector(5, 2);
  }

  /**
   * add JdkInfo object
   *
   * @param jdkInfo JdkInfo object
   */
  public void addJdkInfo(Object jdkInfo){
    jdkInfos.addElement(jdkInfo);
  }

  /**
   * remove jdk info
   *
   * @param index index
   */
  public void removeJdkInfo(int index){
    jdkInfos.removeElementAt(index);
  }

  /**
   * remove jdk info
   *
   * @param jdkInfo jdkInfo object to remove
   */
  public void removeJdkInfo(Object jdkInfo){
    jdkInfos.removeElement(jdkInfo);
  }

  /**
   * get jdkInfos
   *
   * @return jdkInfo Enumeration
   */
  public Enumeration getJdkInfos(){
    return jdkInfos.elements();
  }

  public void insertJdkInfoAt(Object jdkInfo, int index){
    jdkInfos.insertElementAt(jdkInfo, index);
  }

  public int indexOf(Object jdkInfo){
    return jdkInfos.indexOf(jdkInfo);
  }

  public void setJdkInfos(Vector jdks){
    this.jdkInfos = jdks;
  }


  /**
   * get jdk info
   *
   * @param index index
   * @return JdkInfo object
   */
  public JdkInfo getJdkInfo(int index){
    return (JdkInfo)jdkInfos.elementAt(index);
  }

  public JdkInfo getJdkInfo(String jdkName){
    JdkInfo ret = null;
    if(jdkInfos != null)
    for(int i=0; i<jdkInfos.size(); i++){
      JdkInfo info = (JdkInfo)jdkInfos.elementAt(i);
      if(info.getVersion().equals(jdkName)) ret = info;
    }

    return ret;
  }

  /**
   * get size
   *
   * @return jdkinfo size
   */
  public int getSize(){
    return jdkInfos.size();
  }

  public synchronized Object clone(){
    JdkInfoContainer jic = new JdkInfoContainer();

    int size = jdkInfos.size();
    for(int i=0;i<size; i++){
      JdkInfo info = (JdkInfo)jdkInfos.elementAt(i);
      jic.jdkInfos.addElement( info.clone() );
    }
    return jic;
  }
}


