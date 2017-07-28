/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/projectmanager/ProjectFileEntry.java,v 1.9 1999/08/30 07:59:07 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.9 $
 * Project ��ü���� ���ϵ��� �� �����ϱ� ���� ��ü
 * ������Ʈ���� �ϳ��� ������ ��Ÿ����.
 */

package com.antsoft.ant.manager.projectmanager;

import java.io.File;
import java.io.Serializable;

/**
 *  class ProjectFileEntry
 *
 *  @author Jinwoo Baek
 *  @author kim sang kyun
 */
public class ProjectFileEntry implements Serializable {
  private String pathName;
  private String fileName;
  private long lastModified;
  private File realFile;
  private String relativePath;
  private boolean isLibraryFile = false;
  private boolean isNewFile = false;

	/**
	 *  Constructor
	 */
  public ProjectFileEntry(File f, String sourceRoot){
    realFile = f;
    pathName = realFile.getParent();
    fileName = realFile.getName();

    if(sourceRoot != null)
    relativePath = getRelativePathToProjectFile(realFile.getAbsolutePath(), sourceRoot);
    if ((realFile != null) && realFile.exists()) lastModified = realFile.lastModified();
  }

  public ProjectFileEntry(String path, String name, String sourceRoot){
    this(new File(path, name), sourceRoot);
  }

  public ProjectFileEntry(String fullPath, String sourceRoot){
    this(new File(fullPath), sourceRoot);
  }

  /**
   * source root�� ����Ǿ����Ƿ� realFile��ü�� �ٲ۴� ( newSourceRoot + relativePath )
   */
  public void sourceRootChanged(String newSourceRoot){
    realFile = null;
    realFile = new File(newSourceRoot, relativePath);
  }

  public String getRelPath(){
    return relativePath;
  }  


  //library file �� ���� (���� �н��� ����)
  public ProjectFileEntry(String fileName){
    this.fileName = fileName;
    this.isLibraryFile = true;
  }

  public void setNewFile(boolean isNew){
    this.isNewFile = isNew;
  }

  /**
   * class designer�� ���� �����Ǿ ���� ���� �ȵǾ��°�?
   */
  public boolean isNewFile(){
    return isNewFile;
  }

  public static String getRelativePathToProjectFile(String fullPath, String sourceRoot ){

    String relpath = fullPath.substring(sourceRoot.length());

    if(relpath.charAt(0) == File.separatorChar) relpath = "." + relpath;
    else relpath = "." + File.separatorChar + relpath;
    return relpath;
  }

  public boolean isExists(){
    if(realFile == null) return false;
    return realFile.exists();
  }


	/**
	 *  ���� �̸��� ��´�.
	 */
  public String getName() {
    return fileName;
  }

	/**
	 *  �ش� ������ �����Ѵ�.
	 */
  public void setFile(String path, String name) {
		realFile = new File(path, name);
    pathName = realFile.getParent();
    fileName = realFile.getName();

    if ((realFile != null) && realFile.exists()) lastModified = realFile.lastModified();
  }


  public String getPathToWrite(){
    if(relativePath != null) return relativePath;
    else return realFile.getAbsolutePath();
  }

  public boolean isLibraryFile(){
    return this.isLibraryFile;
  }  

	/**
	 *  ������ ��θ� ��´�.
	 */
  public String getPath() {
    return pathName;
  }

  /**
   * full path name�� ��ȯ
   */
  public String getFullPathName(){
    if(!isLibraryFile) return realFile.getAbsolutePath();
    else return fileName;
  }

	/**
	 *  �� ��ü�� ��Ÿ���� ���ڿ��� ��´�.
   *  ������Ʈ ���Ͽ� ����Ǵ� ������ ���ڿ��� ������ ����.
	 */
  public String toString() {
    return fileName;
  }

  /**
   * ������ modified time�� ����Ѵ�
   */
  public void setLastModifiedTime(long time) {
    lastModified = time;
  }

  /**
   * ������ modified time�� ��ȯ�Ѵ�
   */
  public long getLastModifiedTime() {
    return lastModified;
  }

  /**
   * ���� ������ ������ ���� �ð��� ��ġ ��Ų��
   */
  public void syncLastModifiedTimeWithRealFile(){
    if(!isLibraryFile && realFile != null) lastModified = realFile.lastModified();
  }

  /*
   * �ܺο��� �ҽ��� �ٲ�������� �˷��ش�
   */
  public boolean isExternallyChanged(){
    if(isLibraryFile) return false;

    if(realFile != null && realFile.exists() && realFile.lastModified() != lastModified) return true;
    else return false;
  }


	/**
	 *  ��ü�� ������ ���Ѵ�.
	 */
  public boolean equals(ProjectFileEntry pfe) {
    if ((pfe != null) && getFullPathName().equals(pfe.getFullPathName())) return true;
    else return false;
  }
}
