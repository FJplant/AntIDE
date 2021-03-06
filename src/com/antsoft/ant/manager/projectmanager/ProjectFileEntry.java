/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/projectmanager/ProjectFileEntry.java,v 1.9 1999/08/30 07:59:07 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.9 $
 * Project 객체에서 파일들을 잘 관리하기 위한 객체
 * 프로젝트에서 하나의 파일을 나타낸다.
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
   * source root가 변경되었으므로 realFile객체를 바꾼다 ( newSourceRoot + relativePath )
   */
  public void sourceRootChanged(String newSourceRoot){
    realFile = null;
    realFile = new File(newSourceRoot, relativePath);
  }

  public String getRelPath(){
    return relativePath;
  }  


  //library file 을 위함 (실제 패스가 없음)
  public ProjectFileEntry(String fileName){
    this.fileName = fileName;
    this.isLibraryFile = true;
  }

  public void setNewFile(boolean isNew){
    this.isNewFile = isNew;
  }

  /**
   * class designer로 부터 생성되어서 아직 저장 안되었는가?
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
	 *  파일 이름을 얻는다.
	 */
  public String getName() {
    return fileName;
  }

	/**
	 *  해당 파일을 세팅한다.
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
	 *  파일의 경로를 얻는다.
	 */
  public String getPath() {
    return pathName;
  }

  /**
   * full path name을 반환
   */
  public String getFullPathName(){
    if(!isLibraryFile) return realFile.getAbsolutePath();
    else return fileName;
  }

	/**
	 *  이 객체를 나타내는 문자열을 얻는다.
   *  프로젝트 파일에 저장되는 형태의 문자열을 생성해 낸다.
	 */
  public String toString() {
    return fileName;
  }

  /**
   * 마지막 modified time을 기록한다
   */
  public void setLastModifiedTime(long time) {
    lastModified = time;
  }

  /**
   * 마지막 modified time을 반환한다
   */
  public long getLastModifiedTime() {
    return lastModified;
  }

  /**
   * 실제 파일의 마지막 수정 시간과 일치 시킨다
   */
  public void syncLastModifiedTimeWithRealFile(){
    if(!isLibraryFile && realFile != null) lastModified = realFile.lastModified();
  }

  /*
   * 외부에서 소스가 바뀌었는지를 알려준다
   */
  public boolean isExternallyChanged(){
    if(isLibraryFile) return false;

    if(realFile != null && realFile.exists() && realFile.lastModified() != lastModified) return true;
    else return false;
  }


	/**
	 *  객체가 같은지 비교한다.
	 */
  public boolean equals(ProjectFileEntry pfe) {
    if ((pfe != null) && getFullPathName().equals(pfe.getFullPathName())) return true;
    else return false;
  }
}
