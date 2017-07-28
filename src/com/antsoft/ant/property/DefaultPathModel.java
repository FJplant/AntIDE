/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/DefaultPathModel.java,v 1.10 1999/08/24 09:41:33 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.10 $
 */
package com.antsoft.ant.property;

import com.antsoft.ant.pool.librarypool.*;
import com.antsoft.ant.util.FileClassLoader;
import java.util.Vector;
import java.io.*;
import java.util.Enumeration;

/**
 * DefaultPath Model Implemention class
 *
 * @author kim sang kyun
 */
public class DefaultPathModel implements Serializable, Cloneable{

  private JdkInfoContainer jdkInfos = new JdkInfoContainer();
  private LibInfoContainer allLibInfos = new LibInfoContainer();
  private LibInfoContainer selectedLibInfos = new LibInfoContainer();
  private JdkInfo currentJdkInfo = null;
  private String sourceRoot=null;
  private String outputRoot=null;
  private String documentRoot=null;
  private String classPath = null;
  private boolean isProjectPathModel = false;


  public void setProjectPathModel(boolean flag){
    this.isProjectPathModel = flag;
  }

  public String toString(){
    StringBuffer buf = new StringBuffer();
    int i=0;

    if(sourceRoot != null){
      buf.append("PM_Source_Root=" + sourceRoot + "\r\n");
    }

    if(outputRoot != null){
      buf.append("PM_Output_Root=" + outputRoot + "\r\n");
    }

    if(documentRoot != null) {
      buf.append("PM_Document_Root=" + documentRoot + "\r\n");
    }

    if(classPath != null){
      buf.append("PM_ClassPath=" + classPath + "\r\n");
    }

    if(allLibInfos != null){
      if(isProjectPathModel){
        for(i=0; i<allLibInfos.getSize(); i++){
          LibInfo obj = (LibInfo)allLibInfos.getLibraryInfo(i);
          buf.append("PM_AllLib_Infos="+obj.getName() + "\r\n");
        }
      }

      else{
        for(i=0; i<allLibInfos.getSize(); i++){
          LibInfo obj = (LibInfo)allLibInfos.getLibraryInfo(i);
          buf.append("PM_AllLib_Infos=" + "1"+obj.getName() + "#" +
                                       "2"+obj.getSourcePath() + "#" +
                                       "3"+obj.getDocPath() + "#" +
                                       "4"+obj.getClassPathString() + "\r\n");
        }
      }
    }

    if(selectedLibInfos != null){
      for(i=0; i<selectedLibInfos.getSize(); i++){
        LibInfo obj = (LibInfo)selectedLibInfos.getLibraryInfo(i);
        buf.append("PM_Selected_Lib_Infos="+obj.getName() + "\r\n");
      }
    }

    if(currentJdkInfo != null){
      buf.append("PM_Current_JDKInfo="+currentJdkInfo.getVersion()+"\r\n");
    }

    if(jdkInfos != null){
      if(isProjectPathModel){
        for(i=0; i<jdkInfos.getSize(); i++){
          JdkInfo obj = jdkInfos.getJdkInfo(i);
          buf.append("PM_JDK_Infos="+obj.getVersion() + "\r\n");
        }
      }
      else{
        for(i=0; i<jdkInfos.getSize(); i++){
          JdkInfo obj = jdkInfos.getJdkInfo(i);
          buf.append("PM_JDK_Infos=" + "1"+obj.getJavaEXEPath() + "#" +
                                    "2"+obj.getJavacEXEPath() + "#" +
                                    "3"+obj.getAppletviewerEXEPath() + "#" +
                                    "4"+obj.getSourcePath() + "#" +
                                    "5"+obj.getDocPath() + "#" +
                                    "6"+obj.getVersion() + "#" +
                                    "7"+obj.getClassPathString() + "#" +
                                    "8"+obj.getJavadocEXEPath() + "\r\n");
        }
      }
    }

    return buf.toString();
  }

  public DefaultPathModel(){
  }

  public synchronized Object clone(){
    try{
      DefaultPathModel model = (DefaultPathModel)super.clone();
      model.jdkInfos = (jdkInfos != null) ? (JdkInfoContainer)jdkInfos.clone() : null;
      model.allLibInfos = (allLibInfos != null) ? (LibInfoContainer)allLibInfos.clone() : null;
      model.selectedLibInfos = (selectedLibInfos != null) ? (LibInfoContainer)selectedLibInfos.clone() : null;
      model.currentJdkInfo = (currentJdkInfo != null) ? (JdkInfo)currentJdkInfo.clone() : null;
      model.sourceRoot = (sourceRoot != null) ? (String)sourceRoot : null;
      model.outputRoot = (outputRoot != null) ? (String)outputRoot : null;
      model.documentRoot = (documentRoot != null) ? (String)documentRoot : null;
      model.classPath = (classPath != null) ? (String)classPath : null;
      return model;

    }catch(CloneNotSupportedException e){
      throw new InternalError();
    }
  }


  public String getSourcePath(){
    return "." + File.pathSeparator + getSourceRoot();
  }

  public String getSourceRoot(){
    if(sourceRoot == null || sourceRoot.trim().equals("")) return null;
    else return sourceRoot;
  }

  public String getOutputRoot(){
    if(outputRoot == null || outputRoot.trim().equals("")) return null;
    else return outputRoot;
  }

  public String getDocumentRoot(){
    if(documentRoot == null || documentRoot.trim().equals("")) return null;
    else return documentRoot;
  }

  public JdkInfo getCurrentJdkInfo(){
    if(currentJdkInfo == null) {
      return null;
    }
    else return (JdkInfo)currentJdkInfo.clone();
  }

  public JdkInfoContainer getJdkInfoContainer(){
    return (JdkInfoContainer)jdkInfos.clone();
  }

  public LibInfoContainer getAllLibInfoContainer(){
    return (LibInfoContainer)allLibInfos.clone();
  }

  public LibInfoContainer getSelectedLibInfoContainer(){
    return (LibInfoContainer)selectedLibInfos.clone();
  }

  public void setSourceRoot(String newRoot){
    sourceRoot = newRoot;
  }

  public void setOutputRoot(String newRoot){
    outputRoot = newRoot;
  }

  public void setDocumentRoot(String newRoot){
    documentRoot = newRoot;
  }

  public void setCurrentJdkInfo(JdkInfo info){
    currentJdkInfo = info;
  }

  public void setJdkInfoContainer(JdkInfoContainer jic){
    jdkInfos = jic;
  }

  public void setAllLibInfoContainer(LibInfoContainer lic){
    allLibInfos = lic;
  }

  public void setSelectedLibInfoContainer(LibInfoContainer lic){
    selectedLibInfos = lic;
  }

  public Vector getSourceRootsOfLibs(){
    Vector ret = new Vector(5, 2);
    if(currentJdkInfo != null){
      String path = currentJdkInfo.getSourcePath();
      if(path != null && path.length() != 0) ret.addElement(path);
    }
    //files tab
    else{
      for(Enumeration e=jdkInfos.getJdkInfos(); e.hasMoreElements(); ){
        JdkInfo jdkInfo = (JdkInfo)e.nextElement();
        String path = jdkInfo.getSourcePath();
        if(path != null && path.length() != 0){
          ret.addElement(path);
        }
      }
    }

    for(int i=0; i<selectedLibInfos.getSize(); i++){
      LibInfo libInfo = (LibInfo)selectedLibInfos.getLibraryInfo(i);
      String path = libInfo.getSourcePath();
      if(path != null && path.length() != 0){
        ret.addElement(path);
      }
    }

    for(int i=0; i<ret.size(); i++)
      System.out.println("SourcRoot = " + ret.elementAt(i));

    return ret;
  }

  public Vector getLibraryPoolDatasForFilesTab(){
    Vector ret = new Vector();

    for(int i=0; i<jdkInfos.getSize(); i++){
      JdkInfo jInfo = (JdkInfo)jdkInfos.getJdkInfo(i);
      LibraryInfo info = new LibraryInfo(jInfo.getVersion());
      info.setNew(true);

      for(Enumeration e=jInfo.getClassPaths(); e.hasMoreElements(); ){
        String path = (String)e.nextElement();
        info.addLibraryPath(path);
      }
      ret.addElement(info);
    }

    for(int j=0; j<allLibInfos.getSize(); j++){
      LibInfo libInfo = (LibInfo)allLibInfos.getLibraryInfo(j);
      LibraryInfo info = new LibraryInfo(libInfo.getName());
      info.setNew(true);

      for(Enumeration e1=libInfo.getClassPaths(); e1.hasMoreElements(); ){
        String path = (String)e1.nextElement();
        info.addLibraryPath(path);
      }
      ret.addElement(info);
    }

    return ret;
  }

  public Vector getLibraryPoolDatas(){
    Vector ret = new Vector();

    if(currentJdkInfo != null){
      LibraryInfo info = new LibraryInfo(currentJdkInfo.getVersion());
      info.setNew(true);

      for(Enumeration e=currentJdkInfo.getClassPaths(); e.hasMoreElements(); ){
        String path = (String)e.nextElement();
        info.addLibraryPath(path);
      }
      ret.addElement(info);
    }

    for(int j=0; j<selectedLibInfos.getSize(); j++){
      LibInfo libInfo = (LibInfo)selectedLibInfos.getLibraryInfo(j);
      LibraryInfo info = new LibraryInfo(libInfo.getName());
      info.setNew(true);

      for(Enumeration e1=libInfo.getClassPaths(); e1.hasMoreElements(); ){
        String path = (String)e1.nextElement();
        info.addLibraryPath(path);
      }
      ret.addElement(info);
    }

    return ret;
  }

  public String getClassPath(){
    return classPath;
  }

  public void setClassPath(String path){
    classPath = path;
  }

  public String getDocumentPath() {
    return "." + File.pathSeparator + getDocumentRoot();
  }

  public void updateClassPath(){
    StringBuffer newClassPath = new StringBuffer();
    String pathSeparator = File.pathSeparator;
    String currentDir = "." + pathSeparator;

    //output directory
    newClassPath.append(getOutputRoot() + pathSeparator);
    
    //current directory
    newClassPath.append(currentDir);

    // document directory
    if(getDocumentRoot() != null)  newClassPath.append(getDocumentRoot() + pathSeparator);

    //jdk & library paths
    Vector libs = getLibraryPoolDatas();
    if(libs != null)
    for(int i=0; i<libs.size(); i++){
      LibraryInfo libInfo = (LibraryInfo)libs.elementAt(i);

      for(Enumeration e=libInfo.getPath(); e.hasMoreElements(); ){
        String path = (String)e.nextElement();
        newClassPath.append(path + pathSeparator);
      }
    }

    classPath = newClassPath.toString();
  }
}
