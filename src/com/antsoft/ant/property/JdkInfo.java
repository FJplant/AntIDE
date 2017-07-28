/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/JdkInfo.java,v 1.3 1999/07/22 03:42:03 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 */
package com.antsoft.ant.property;

import java.awt.event.*;
import java.util.*;
import java.io.File;

/**
 * jdk info class
 *
 * @author kim sang kyun
 */
public class JdkInfo implements java.io.Serializable, Cloneable{

  private String javaEXEPath;
  private String javacEXEPath;
  private String appletviewerEXEPath;
  private String javadocEXEPath;
  private Vector classPaths;
  private String sourcePath;
  private String docPath;
  private String version;

  /** default constructor */
  public JdkInfo() {
    classPaths = new Vector(5, 2);
  }

  public synchronized Object clone(){
    try{
      JdkInfo info = (JdkInfo)super.clone();

      info.javaEXEPath = (javaEXEPath != null) ? javaEXEPath : null;
      info.javacEXEPath = (javacEXEPath != null) ? javacEXEPath : null;
      info.appletviewerEXEPath = (appletviewerEXEPath != null) ? appletviewerEXEPath : null;
      info.javadocEXEPath = (javadocEXEPath != null) ? javadocEXEPath : null;
      info.sourcePath = (sourcePath != null) ? sourcePath : null;
      info.docPath = (docPath != null) ? docPath : null;
      info.classPaths = (classPaths != null) ? (Vector)classPaths.clone() : null;
      info.version = (version != null) ? version : null;
      return info;
    }catch(CloneNotSupportedException e){
      throw new InternalError();
    }
  }

  /**
   * constructor
   *
   * @param javaEXEPath java.exe path
   * @param classPaths class path vector
   * @param sourcePath source path
   * @param docPath doc path

   */
  public JdkInfo(String javaEXEPath, Vector classPaths, String sourcePath, String docPath, String version){
    this();
    this.javaEXEPath = javaEXEPath;

    this.classPaths = classPaths;
    this.sourcePath = sourcePath;
    this.docPath = docPath;
    setJavacEXEPath(javaEXEPath);
    setAppletviewerEXEPath(javaEXEPath);
    setJavadocEXEPath(javaEXEPath);
    this.version = version;
  }

  /**
   * set java.exe path
   *
   * @param path java.exe path
   */
  public void setJavaEXEPath(String path){
    javaEXEPath = path;
    setJavacEXEPath(path);
    setAppletviewerEXEPath(path);
    setJavadocEXEPath(path);
  }


  public String getJavawEXEPath(){
    String javacexe = getJavacEXEPath();
    String javawexe = null;
    if(javacexe != null){
      if(System.getProperty("os.name").indexOf("Win") != -1){
        javawexe = javacexe.substring(0, javacexe.lastIndexOf("c")) + "w.exe";
      }
      else{
        javawexe = javacexe.substring(0, javacexe.lastIndexOf("c")) + "w";
      }
    }

    return javawexe;
  }

  /**
   * set jdk version
   */
  public void setVersion(String version){
    this.version = version;
  }

  public String getVersion(){
    return version;
  }

  public void setJavacEXEPath(String javaexepath){
    File java = new File(javaexepath);

    if(System.getProperty("os.name").indexOf("Win") != -1){
      javacEXEPath = java.getParent() + File.separator + "javac.exe";
    }
    else{
      javacEXEPath = java.getParent() + File.separator + "javac";
    }
  }

  public String getJavacEXEPath(){
    return javacEXEPath;
  }

  public void setAppletviewerEXEPath(String javaexepath){
    File java = new File(javaexepath);
    if(System.getProperty("os.name").indexOf("Win") != -1){
      appletviewerEXEPath = java.getParent() + File.separator + "appletviewer.exe";
    }
    else{
      appletviewerEXEPath = java.getParent() + File.separator + "appletviewer";
    }
  }

  public String getAppletviewerEXEPath(){
    return appletviewerEXEPath;
  }

  public void setJavadocEXEPath(String javaexepath) {
    File java = new File(javaexepath);
    if(System.getProperty("os.name").indexOf("Win") != -1){
      javadocEXEPath = java.getParent() + File.separator + "javadoc.exe";
    }
    else{
      javadocEXEPath = java.getParent() + File.separator + "javadoc";
    }
  }

  public String getJavadocEXEPath() {
    return javadocEXEPath;
  }

  /**
   * get java.exe path
   *
   * @return java.exe path
   */
  public String getJavaEXEPath(){
    return javaEXEPath;
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
   * get class path list
   *
   * @return classpath enumeration
   */
  public Enumeration getClassPaths(){
    return classPaths.elements();
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
    return version;
  }

  public boolean equals(Object item){
    if(item == null) return false;
    else{
      if(this.toString().equals(item.toString()))  return true;
      else return false;
    }
  }
}


