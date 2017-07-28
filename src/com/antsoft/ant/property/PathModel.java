/*
 * $Header: /AntIDE/source/ant/property/PathModel.java 2     99-05-17 12:24a Multipia $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 2 $
 */
package com.antsoft.ant.property;

import com.antsoft.ant.util.FileClassLoader;
import java.util.Vector;
/**
 * PathModel interface
 *
 * @author kim sang kyun
 */
public interface PathModel {
  public abstract JdkInfoContainer getJdkInfoContainer();
  public abstract LibInfoContainer getAllLibInfoContainer();
  public abstract LibInfoContainer getSelectedLibInfoContainer();
  public abstract JdkInfo getCurrentJdkInfo();
  public abstract String getSourceRoot();
  public abstract String getOutputRoot();
  public abstract void setJdkInfoContainer(JdkInfoContainer jdkInfos);
  public abstract void setAllLibInfoContainer(LibInfoContainer libInfos);
  public abstract Vector getLibraryPoolDatas();
  public abstract FileClassLoader getClassLoader();
  public abstract void updateClassLoader();
  public abstract String getClassPath();
  public abstract String getSourcePath();
  public abstract void updateClassPath();
  public abstract void setCurrentJdkInfo(JdkInfo info);
  public abstract void setSelectedLibInfoContainer(LibInfoContainer libInfos);
}
