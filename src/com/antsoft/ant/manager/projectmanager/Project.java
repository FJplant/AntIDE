/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/projectmanager/Project.java,v 1.19 1999/08/30 08:07:24 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.19 $
 */
package com.antsoft.ant.manager.projectmanager;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.*;
import javax.swing.tree.*;
import javax.swing.JTree;

import com.antsoft.ant.property.*;
import com.antsoft.ant.property.projectproperty.*;
import com.antsoft.ant.main.Main;
import com.antsoft.ant.pool.librarypool.*;                    
import com.antsoft.ant.codecontext.CodeContext;
import com.antsoft.ant.pool.sourcepool.SourceEntry;
import com.antsoft.ant.util.DayInfo;
import com.antsoft.ant.util.Constants;
import com.antsoft.ant.util.FileClassLoader;
import com.antsoft.ant.main.Main;

/**
 *  하나의 프로젝트를 관리하는 오브젝트
 *
 *  @author Baek, Jin-woo
 *  @author Kim sang kyun
 */
public class Project implements Serializable{
  /**
   *  Project Name
   */
  private String name = null;
  private FileClassLoader loader = null;

  /**
   *  Project's Main Path
   */
  private String path = null;
  private File pathFile = null;

  /**
   *  Files in Project
   */
  //private Vector files = new Vector();
  private Hashtable files = new Hashtable(20, 0.9F);

	/**
	 *  Tree and Tree Model for ProjectPanel
	 */
	private DefaultTreeModel treeModel = null;
  private JTree tree = null;

  private DefaultPathModel pathModel = new DefaultPathModel();

  private CompilerOptionModel compilerModel = new CompilerOptionModel();
  private InterpreterOptionModel interpreterModel = new InterpreterOptionModel();

  File fl = null;
  boolean isModified = false;

  private long lastID = 0;

  private String comment = "";
  private String lastSavedTime = null;
  private String createdTime = null;
  private String params = "";

  private boolean isUseParam = false;


  public Project() {
    name = "Files";
		path = "";
    pathFile = null;

    // default path model 로 setting
    this.pathModel = Main.property.getPathModel();
		// for ProjectPanel's tree
		treeModel = new DefaultTreeModel(new DefaultMutableTreeNode(name));
    tree = new JTree();
  }

  /**
   *  Constructor. 해당 프로젝트 파일을 열어서
   *  내부 데이터를 채운다.
   *
   *  @param name Project name
	 *  @param path Project file path
   */
  public Project(String name, String path) {

    this.name = name;
    this.path = path;
    this.pathFile = new File(path);

    // default path model 로 setting
    this.pathModel = Main.property.getPathModel();
		// for ProjectPanel's tree
		treeModel = new DefaultTreeModel(new DefaultMutableTreeNode(name));
    tree = new JTree();
  }


  public void setPath(String newPath){
    this.path = newPath;
  }

  /**
   * librarypool의 data를 현재의 project 내용으로 초기화한다
   */
  public void initLibraryPool(){
    Vector datas = pathModel.getLibraryPoolDatas();
    updateClassLoader();

    LibraryPool.removeAllProjectLibs(name);

    for(int i=0; i<datas.size(); i++){
      LibraryPool.addProjectLibraryInfo(name, (LibraryInfo)datas.elementAt(i));
    }
  }

  /**
   * 새로운 path model을 설정하면서 source root가 바뀌었으면 모든 파일 entry에 적용되도록 한다
   */
  public void setPathModel(DefaultPathModel model){
    if(pathModel != null && pathModel.getSourceRoot() != null){
      String oldSrcRoot = pathModel.getSourceRoot();
      String newSrcRoot = model.getSourceRoot();
      if(!oldSrcRoot.equals(newSrcRoot)){
        for(Enumeration e=files.elements(); e.hasMoreElements(); ){
          ProjectFileEntry pfe = (ProjectFileEntry)e.nextElement();
          pfe.sourceRootChanged(newSrcRoot);
        }
      }
    }

    pathModel = model;
    isModified = true;
  }

  /**
   * 프로젝트 오픈할때 호출된다
   */
  public void syncEntrysWithRealFile(){
     for(Enumeration e = files.elements(); e.hasMoreElements(); ){
       ProjectFileEntry entry = (ProjectFileEntry)e.nextElement();

       if(entry.isExternallyChanged()){
         SourceEntry.syncWithRealFile(entry.getPath(), entry.getName());
       }
       entry.syncLastModifiedTimeWithRealFile();
     }
  }

	/**
	 *  ProjectPanel의 Tree정보를 얻는다.
	 *
	 *  @return JTree tree
	 */
  public JTree getTree(){
    return this.tree;
  }

  public DefaultTreeModel getTreeModel(){
    return this.treeModel;
  }


	/**
	 *  TreeModel을 설정한다.
	 *
	 *  @param treeModel 설정할 tree model
	 */
	public void setTreeModel(DefaultTreeModel treeModel) {
		this.treeModel = treeModel;
    isModified = true;
	}

  /**
   *  현 프로젝트의 컴파일 옵션을 얻는다.
   */
  public CompilerOptionModel getCompilerModel() {
  	return compilerModel;
  }

  public void setCompilerModel(CompilerOptionModel compilerModel) {
  	this.compilerModel = compilerModel;
    isModified = true;
  }

  /**
   *  현 프로젝트의 실행 옵션을 얻는다.
   */
  public InterpreterOptionModel getInterpreterModel() {
  	return interpreterModel;
  }

  public void setInterpreterModel(InterpreterOptionModel interpreterModel) {
  	this.interpreterModel = interpreterModel;
    isModified = true;
  }

  public DefaultPathModel getPathModel(){
    return this.pathModel;
  }

  public String getProjectName() {
    return name;
  }

  public void setProjectName(String name) {
  	this.name = name;
  }

  public ProjectFileEntry addLibFile(String fileName){
    ProjectFileEntry file = null;
    if(fileName != null){
      if(files.get(fileName) == null){
        file = new ProjectFileEntry(fileName);
        files.put(fileName, file);
        isModified = true;
      }
    }
    return file;
  }

  public ProjectFileEntry addFile(String pathName, String fileName) {
    ProjectFileEntry file = null;
    if (fileName != null && pathName != null) {
      File f = new File(pathName, fileName);
      String fullPath = f.getAbsolutePath();

      if(getProjectName().equals("Files")){
        if( files.get(fullPath) == null){
    	    file = new ProjectFileEntry(f, pathModel.getSourceRoot() );
          files.put(fullPath, file);
        	isModified = true;
        }
      }
      //source root로 하위 패스에 존재 한다
      else if(pathName.startsWith(pathModel.getSourceRoot())) {
        if( files.get(fullPath) == null){
    	    file = new ProjectFileEntry(f, pathModel.getSourceRoot() );
          files.put(fullPath, file);
        	isModified = true;
        }
      }

      //source root의 하위에 존재 하지 않으면 삽입하지 않는다 
      /*
      else {
        if(files.get(fullPath) == null){
          if(pathFile != null) file = new ProjectFileEntry(fullPath, pathModel.getSourceRoot());
          else file = new ProjectFileEntry(fullPath, null);

          files.put(fullPath, file);
        	isModified = true;
        }
      }
      */
    }
    return file;
  }

  public ProjectFileEntry addFile(String path){
    File f = new File(path);
    return addFile(f.getParent(), f.getName());
  }

  public ProjectFileEntry addFile(ProjectFileEntry file) {
    if (file != null) {
      String fullpath = file.getFullPathName();

      if (files.get(file.getFullPathName()) == null){
        files.put(file.getFullPathName(), file);
  	    isModified = true;
      }
    }
    return file;
  }

  public File getPathFile(){
    return pathFile;
  }

  public void setFiles(Hashtable files){
    this.files = files;
  }

  public ProjectFileEntry getFileEntry(String fullPath) {

    return (ProjectFileEntry)files.get(fullPath);
  }

  public void removeFile(String pathName, String fileName) {
    File f = new File(pathName, fileName);
    if(files.remove(f.getAbsolutePath()) != null) isModified = true;
  }

  public void removeFile(ProjectFileEntry file) {
    if(files.remove(file.getFullPathName()) != null) isModified = true;
  }

  public void setLastSavedTime(){
    lastSavedTime = DayInfo.getFormattedDayStr();
  }

  public String getLastSavedTime(){
    return lastSavedTime;
  }

  public boolean isUsableParam() {
  	return isUseParam;
  }

  public void setUsableParam(boolean b) {
  	this.isUseParam = b;
    this.isModified = true;
  }

  public boolean isModified() {
    return isModified;
  }

  public String getPath() {
    return path;
  }

  public Vector getFiles() {
    Vector v = new Vector(files.size(), 1);
    for(Enumeration e = files.elements(); e.hasMoreElements(); ) v.addElement(e.nextElement());
    return v;
  }

  //public Vector getDirectoryFiles() {
  //  DefaultMutableTreeNode node = (DefaultMetableTreeNode)

  /**
   *  Tree 구조를 프로젝트 파일 텍스트로 나타낸다.
   */
  public String treeModelToString(DefaultMutableTreeNode node) {
  	StringBuffer out = new StringBuffer();
		if (node instanceof ProjectPanelTreeNode) {
    	ProjectPanelTreeNode pptn = (ProjectPanelTreeNode)node;
      if (pptn.isFile()) {
      	ProjectFileEntry pfe = (ProjectFileEntry)pptn.getObject();
        out.append("File=" + pfe.getPathToWrite() + Constants.lineSeparator);
      }
      else if (pptn.isFolder()) {
       	out.append("Folder=" + pptn.getID() + "#" + (String)pptn.getUserObject() +
          							"#" + pptn.getParentID() + Constants.lineSeparator);



        for (int i = pptn.getChildCount() - 1; i >= 0; i--) {
        	out.append(treeModelToString((DefaultMutableTreeNode)pptn.getChildAt(i)));
        }
      }
    }
    else {
    	for (int i = node.getChildCount() - 1; i >= 0; i--) {
      	out.append(treeModelToString((DefaultMutableTreeNode)node.getChildAt(i)));
      }
    }
    return out.toString();
  }

  /**
   *  프로젝트 파일에 저장되는 텍스트 형태로 나타낸다.
   */
  public String toString() {
  	StringBuffer out = new StringBuffer();

    // 프로젝트 이름
    out.append("ProjectName=" + name + Constants.lineSeparator);
    // 프로젝트 설명
    if (comment != null)
	    out.append("Comment=" + comment + Constants.lineSeparator);
    else
    	out.append("Comment=" + "" + Constants.lineSeparator);
    // 마지막 파일 ID
    out.append("LastID=" + lastID + Constants.lineSeparator);

    if (createdTime != null)
    	out.append("CreatedTime=" + createdTime + Constants.lineSeparator);
    else
    	out.append("CreatedTime=" + "" + Constants.lineSeparator);

    out.append("UseParam=" + isUseParam + Constants.lineSeparator);
    if (params != null)
    	out.append("Parameters=" + params + Constants.lineSeparator);
    else
    	out.append("Parameters=" + "" + Constants.lineSeparator);

    // 나머지 옵션들
    pathModel.setProjectPathModel(true);
    out.append(pathModel);
    out.append(compilerModel);
    out.append(interpreterModel);

    // 프로젝트에 속한 파일들
    out.append(treeModelToString((DefaultMutableTreeNode)treeModel.getRoot()));

    return out.toString();
  }

  public boolean equals(Project prj) {
		if (name.equals(prj.getProjectName()) && path.equals(prj.getPath())) return true;
		return false;
  }

  public void setComment(String comment){
    this.comment = comment;
    this.isModified = true;
  }

  public String getComment(){
    return comment;
  }

  public String getCreatedTime(){
    return this.createdTime;
  }

  public void setCreatedTime(String time){
    this.createdTime = time;
  }

  public String getParameters() {
  	return params;
  }

  public void setParameters(String params) {
  	this.params = params;
    this.isModified = true;
  }

  public long getLastID() {
  	return lastID;
  }

  public void setLastID(long lastID) {
  	this.lastID = lastID;
  }

  private static String strip( String Stripped, String ToStrip ) {
  	String stringStripped = new String();
  	String stringToStrip = new String();
  	int index;

  	stringStripped = Stripped; stringToStrip = ToStrip;

  	// 짜를 부분이 있다면...
  	if((index = stringStripped.indexOf(stringToStrip)) != -1) {
  		String temp1 = stringStripped.substring(0, index);
  		String temp2 = stringStripped.substring(index + stringToStrip.length());
  		return (temp1 + temp2);
  	}	else return stringStripped;
  }

  private String getFileName(String fullname) {
    if (fullname != null) {
      int idx = fullname.lastIndexOf(';');
      if (idx != -1) return fullname.substring(idx + 1, fullname.length());
    }
    return null;
  }

  private String getPathName(String fullname) {
    if (fullname != null) {
      int idx = fullname.lastIndexOf(';');
      if (idx != -1) return fullname.substring(0, idx);
    }
    return null;
  }

  public void updateClassLoader(){
    if(loader ==  null) {
      loader = new FileClassLoader();
    }
    else {
      loader.removeAllPath();
    }

    Vector libs = null;
    if(!getProjectName().equals("Files")) libs = pathModel.getLibraryPoolDatas();
    else libs = pathModel.getLibraryPoolDatasForFilesTab();
    
    for(int i=0; i<libs.size(); i++){
      LibraryInfo libInfo = (LibraryInfo)libs.elementAt(i);
      for(Enumeration e=libInfo.getFiles(); e.hasMoreElements(); ){
        File file = (File)e.nextElement();
        String path = file.getAbsolutePath();
        loader.addPath(path);
      }
    }
  }

  public void clear(){
    if(files != null) files.clear();
    if(treeModel != null) {
      DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
      root.removeAllChildren();
    }
  }

  public FileClassLoader getClassLoader(){

    return loader;
  }

  public void setClassLoader(FileClassLoader loader){
    this.loader = loader;
  }
}
