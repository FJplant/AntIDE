/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/LibInfo.java,v 1.3 1999/07/22 03:42:03 multipia Exp $
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

/**
 * library info class
 *
 * @author kim, sang kyun
 */
public class LibInfo implements java.io.Serializable, Cloneable {

  String name;
  Vector classPaths;
  String sourcePath;
  String docPath;

  /** default constructor */
  public LibInfo() {
    classPaths = new Vector(5, 2);
  }

  public synchronized Object clone(){
    try{
      LibInfo info = (LibInfo)super.clone();
      info.name = (name != null) ? name : null;
      info.sourcePath = (sourcePath != null) ? sourcePath : null;
      info.docPath = (docPath != null) ? docPath : null;
      info.classPaths = (classPaths != null) ? (Vector)classPaths.clone() : null;
      return info;
    }catch(CloneNotSupportedException e){
      throw new InternalError();
    }
  }


  /**
   * constructor
   *
   * @param name library name
   * @param classPaths class path vector
   * @param sourcePath source path
   * @param docPath doc path

   */
  public LibInfo(String name, Vector classPaths, String sourcePath, String docPath){
    this();
    this.name = name;
    this.classPaths = classPaths;
    this.sourcePath = sourcePath;
    this.docPath = docPath;
  }

  /**
   * set library name
   *
   * @param name library name
   */
  public void setName(String name){
    this.name = name;
  }

  /**
   * get library name
   *
   * @return library name
   */
  public String getName(){
    return this.name;
  }

  /**
   * add class path
   *
   * @param classPath classpath to add
   */
  public void addClassPath(String classPath){
    classPaths.addElement(classPath);
  }

  /**
   * remove class path
   *
   * @param classPath class path to remove
   */
  public void removeClassPath(String classPath){
    classPaths.removeElement(classPath);
  }

  /**
   * set class path as vector
   *
   * @param classPath class path vector
   */
  public void setClassPath(Vector classPaths){
    this.classPaths = classPaths;
  }

  /**
   * get classpath to string
   *
   * @return class path string
   */
  public String getClassPathString(){
    StringBuffer path = new StringBuffer();
    for(int i=0; i<classPaths.size(); i++){
      path.append((String)classPaths.elementAt(i));
      if(i != classPaths.size()-1) path.append(", ");
    }

    return path.toString();
  }

  /**
   * get class path list
   *
   * @return classpath enumeration
   */
  public Enumeration getClassPaths(){
    return classPaths.elements();
  }

  /**
   * set source path
   *
   * @param sourcePath source path
   */
  public void setSourcePath(String sourcePath){
    this.sourcePath = sourcePath;
  }

  /**
   * get source path
   *
   * @return source path
   */
  public String getSourcePath(){
    return this.sourcePath;
  }

  /**
   * set doc path
   *
   * @param docPath doc path
   */
  public void setDocPath(String docPath){
    this.docPath = docPath;
  }


  /**
   * get doc path
   *
   * @return docpath
   */
  public String getDocPath(){
    return this.docPath;
  }

  public String toString(){
    return name;
  }

  public boolean equals(Object item){
    if(item == null) return false;
    else {
      if(item.toString().equals(this.toString())) return true;
      else return false;
    }  
  }
}
