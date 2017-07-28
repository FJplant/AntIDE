/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/PathPropertyInfo.java,v 1.3 1999/07/22 03:42:03 multipia Exp $
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

/**
 * Path property Info class
 *
 * @author kim, sang kyun
 */
public class PathPropertyInfo {
  private JdkInfo currentJdkInfo;
  private String sourceRoot;
  private String outputRoot;
  private String documentRoot;
  private LibInfoContainer libraryInfoContainer;

  public PathPropertyInfo(){}

  public JdkInfo getCurrentJdkInfo() {
    return currentJdkInfo;
  }

  public void setCurrentJdkInfo(JdkInfo newCurrentJdkInfo) {
    currentJdkInfo = newCurrentJdkInfo;
  }

  public void setDocumentRoot(String newDocumentRoot) {
    documentRoot = newDocumentRoot;
  }

  public String getDocumentRoot() {
    return documentRoot;
  }

  public void setSourceRoot(String newSourceRoot) {
    sourceRoot = newSourceRoot;
  }

  public String getSourceRoot() {
    return sourceRoot;
  }

  public void setOutputRoot(String newOutputRoot) {
    outputRoot = newOutputRoot;
  }

  public String getOutputRoot() {
    return outputRoot;
  }

  public void setLibInfoContainer(LibInfoContainer newLibInfoContainer) {
    libraryInfoContainer = newLibInfoContainer;
  }

  public LibInfoContainer getLibInfoContainer() {
    return libraryInfoContainer;
  }
}
