/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/projectmanager/ProjectExplorer.java,v 1.74 1999/08/31 05:51:50 kahn Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.74 $
 */

package com.antsoft.ant.manager.projectmanager;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.Element;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.antsoft.ant.main.*;
import com.antsoft.ant.designer.codeeditor.*;
import com.antsoft.ant.pool.sourcepool.*;
import com.antsoft.ant.browser.sourcebrowser.*;
import com.antsoft.ant.codecontext.codeeditor.*;
import com.antsoft.ant.designer.classdesigner.*;
import com.antsoft.ant.property.*;
import com.antsoft.ant.property.projectproperty.*;
import com.antsoft.ant.compiler.Compiler;
import com.antsoft.ant.interpreter.Interpreter;
import com.antsoft.ant.util.Constants;
import com.antsoft.ant.util.EventHandlerListDlg;
import com.antsoft.ant.util.GetSetAddDialog;
import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.util.ImageViewer;
import com.antsoft.ant.runner.*;
import com.antsoft.ant.tools.javadoc.JavaDocBuilder;
//import com.antsoft.ant.designer.codeeditor.OpenSourcePanel;


/**
 *  class ProjectExplorer
 *
 *  @author Jinwoo Baek
 *  @author kim sang kyun
 */
public class ProjectExplorer extends JPanel
		implements SourceBrowserEventListener, EditFunctionEventListener {

  private ProjectPanel projectPanel;
	private SourceBrowser sourceBrowser;
  private CompileOutputPanel outputPanel;
  private SourcePool sourcePool;
  private Project project;
  private OpenSourcePanel openSourcePanel;

  // Opened File list
  private Vector openedList = new Vector(15, 5);
  private Vector openedListForSaveAll = new Vector(15, 5);

  // 가장 최근에 오픈되었던 source
  private SourceEntry lastOpenedSource;
  private int lastPosition = 0;
  private boolean once = true;

  // 선택된 directory의 하위 file을 담아두는 vector;
  private Vector lowerFiles; // itree;

  private TextViewPanel upperTvp, bottomTvp;
  private Point upperTvpPosition, bottomTvpPosition;

  // for GUI
  private JSplitPane sp1;
  private JSplitPane sp2;
	private JSplitPane sp3;
  private JSplitPane sp4;

  // for image view... by itree
  private ImageViewer imageViewerPanel = null;
  private CardLayout cardLayout = null;
  private JPanel textAndImageP = null;

  private Process p;
  private Interpreter intptr = null;
  private String[] cmd;
  private MainFrame mainFrm;
  private EditorStatusBar statusBar = null;

  public static final int PROJECT_PANEL = 1;
  public static final int SOURCE_BROWSER = 2;
  public static final int TEXTVIEW_PANEL = 3;

  public JPanel sp2Wrapper;
  private OverridePanel overrideP;

  private boolean isOverridePanelVisible = false;
  private boolean isSourceBrowserVisible = true;

	/**
	 *  Constructor
	 */
  public ProjectExplorer(Project prj, MainFrame frm) {
    setLayout(new BorderLayout());

    project = prj;
    mainFrm = frm;
    outputPanel = new CompileOutputPanel(this);
    openSourcePanel = new OpenSourcePanel(this,mainFrm);
    projectPanel = new ProjectPanel(this, project, openSourcePanel);
    sourceBrowser = new SourceBrowser(this);
    sourcePool = new SourcePool();
    statusBar = mainFrm.m_Status;
    upperTvp = new TextViewPanel(this, TextViewPanel.UPPER, statusBar, openSourcePanel);
    bottomTvp = new TextViewPanel(this, TextViewPanel.BOTTOM, statusBar, openSourcePanel);

		sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, projectPanel, sourceBrowser);
    sp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, null, null);
    sp4 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp2, null);
    sp2Wrapper = new JPanel(new BorderLayout());
    

    JPanel statusPlusOpened = new JPanel(new GridLayout(1,1, 0, 0));
    openSourcePanel.setPreferredSize(new Dimension(statusBar.getWidth(), 22));
    statusPlusOpened.add(openSourcePanel);

    sp2Wrapper.add(sp4, BorderLayout.CENTER);

    //override panel
    if(!isFilesTab())  overrideP = new OverridePanel(mainFrm.getCodeContext(), this);

    //itree... for StatusBar
    statusBar.setVisible(true);
    statusBar.hideRightStatus(true);

    sp2Wrapper.setBorder(BorderList.unselLineBorder);

    //itree... for ImageViewer
    cardLayout = new CardLayout();
    textAndImageP = new JPanel();
    imageViewerPanel = new ImageViewer();

    textAndImageP.setLayout(cardLayout);
    textAndImageP.add(sp2Wrapper,"TextViewPanel");
    textAndImageP.add(imageViewerPanel,"ImageViewer");
    cardLayout.show(textAndImageP,"TextViewPanel");
    // End itree
    sp1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp3, textAndImageP);
    sp3.setDividerLocation( (int)((mainFrm.getSize().height * 0.8) / 2) );

		sp1.setDividerSize(4);
		sp2.setDividerSize(4);
		sp3.setDividerSize(4);
    sp4.setDividerSize(2);
		sp2.setOneTouchExpandable(true);

    add(sp1, BorderLayout.CENTER);
    add(statusPlusOpened, BorderLayout.SOUTH);
  }

  public void showImagePanel() {
    cardLayout.show(textAndImageP,"ImageViewer");

  }
  public void showTextPanel() {
    cardLayout.show(textAndImageP,"TextViewPanel");

  }

  private void setSourceBrowserVisible(boolean isVisible){
    if(isVisible){
      sp3.add(sourceBrowser, JSplitPane.BOTTOM);
      sp3.setDividerLocation(sourceBrowser.getLastDividerLocation());
      sp3.doLayout();
    }

    else{
       sourceBrowser.setLastDividerLocation(sp3.getDividerLocation());
       sp3.remove(sourceBrowser);
    }

    isSourceBrowserVisible = isVisible;
  }

  private void setOverridePanelVisible(boolean isVisible){
    if(overrideP == null) return;

    if(isVisible){
      sp2Wrapper.add(overrideP, BorderLayout.NORTH);
    }

    else {
       sp2Wrapper.remove(overrideP);
    }
    isOverridePanelVisible = isVisible;
  }

  public void openSourceHappened(Object source, ProjectPanelTreeNode openedNode){
    if(source instanceof ProjectPanel){
      if (!this.isFilesTab())
        openSourcePanel.notifyFileOpen(openedNode);
    }
    else{
      projectPanel.notifyFileOpen(openedNode);
    }
  }

  public void openImageSource(String path,String filename) {
    imageViewerPanel.loadImage(path,filename);
    showImagePanel();
  }

  public void closeImageSource() {
    showTextPanel();
  }

  public void setPosition(){
    sp3.setDividerLocation( (int)((mainFrm.getSize().height * 0.8) / 2) );
  }

  int prevComp = -1;
  public void setFocusedComponent(int type){
    if(prevComp == type) return;

    if(prevComp == PROJECT_PANEL) projectPanel.clearBorder();
    else if(prevComp == SOURCE_BROWSER) sourceBrowser.clearBorder();
    else if(prevComp == TEXTVIEW_PANEL) sp2Wrapper.setBorder(BorderList.unselLineBorder);

    if(type == PROJECT_PANEL) projectPanel.setSelLineBorder();
    else if(type == SOURCE_BROWSER) sourceBrowser.setSelLineBorder();
    else if(type == TEXTVIEW_PANEL) {
      sp2Wrapper.setBorder(BorderList.selLineBorder);

      //TextViewPanel = this.getCurrentTvp();
    }

    prevComp = type;
  }

	/**
	 *  프로젝트를 얻어온다.
	 */
	public Project getProject() {
		return project;
	}

  /**
   * default project인가 아닌가를 반환
   */
  public boolean isFilesTab(){
    return project.getProjectName().equals("Files");
  }

  public void commentLines(){
    TextViewPanel tvp = getCurrentTvp();
    if(tvp != null) tvp.commentLines();
  }

  public void unCommentLines(){
    TextViewPanel tvp = getCurrentTvp();
    if(tvp != null) tvp.unCommentLines();
  }

  public void moveLines(boolean isForward){
    TextViewPanel tvp = getCurrentTvp();
    if(tvp != null) tvp.moveLines(isForward);
  }

  /////////////////////////// ADD To PROJECT ///////////////////////////////


  /**
   * files tab에서 add file button을 선택했을대 호출
   */
  public void addToFilesTab(String path, boolean modifiable){

    FileDialog filedlg = new FileDialog(MainFrame.mainFrame, "Open", FileDialog.LOAD);

    if(path!=null) filedlg.setDirectory(path);
    filedlg.setFile("*.java");
    filedlg.show();
    String filename = filedlg.getFile();

    if (filename != null) {
      File self = new File(filedlg.getDirectory(), filename);
      Vector addedFile = new Vector(1, 1);

      ProjectFileEntry pfe = null;
      pfe = project.addFile(self.getParent(), self.getName());
      if (pfe != null) {
        projectPanel.addFileToProject(pfe);
        addedFile.addElement(pfe.getFullPathName());
        openDocument(pfe.getFullPathName(), modifiable);
      }

      mainFrm.getCodeContext().addFiles(addedFile);
      addedFile = null;
    }
  }
  /**
   * file browser에서 java file에 double click을 하는 경우 opened list에 옮겨준다.
   */
  public void addToFilesTab(String path, String filename, boolean modifiable) {
    if (filename != null) {
      File self = new File(path, filename);
      Vector addedFile = new Vector(1, 1);

      ProjectFileEntry pfe = null;
      pfe = project.addFile(self.getParent(), self.getName());
      if (pfe != null) {
        projectPanel.addFileToProject(pfe);
        addedFile.addElement(pfe.getFullPathName());
        openDocument(pfe.getFullPathName(), modifiable);
      }

      mainFrm.getCodeContext().addFiles(addedFile);
      addedFile = null;
    }
  }

	/**
	 *  ProjectPanel에서의 요청을 처리하기 위한 함수
	 *  특정위치로 디렉토리이동을 한다.
	 *
	 *  @param path 특정 디렉토리에서 다이얼로그를 띄우게한다.
	 */
  public void addToProject(String path, boolean modifiable) {
    File pathF = (path==null) ? null : new File(path);

    if (path != null && pathF.exists()) {
      AddToProjectDialog dlg = new AddToProjectDialog(mainFrm, "Add to Project", true, new File(path), projectPanel.getFilesAndDirsInSelectedTreePath());
      dlg.setVisible(true);

      if (dlg.isOK()) {
        File files[] = dlg.getSelectedFiles();
        ProjectFileEntry pfe = null;
        Vector addedFile = new Vector(10, 5);

        DefaultMutableTreeNode addedNode = null;
        for (int i = 0; i < files.length; i++) {
          if (files[i] != null) {
            // 디렉토리를 선택했으면
            if (files[i].isDirectory()) {
            	addFolderToProject(projectPanel.getCurrentNode(), files[i], addedFile, dlg.isRecursively());
            }
            // 파일을 선택했으면
            else {
              pfe = project.addFile(files[i].getParent(), files[i].getName());
              if (pfe != null) {
                addedNode = projectPanel.addFileToProject(pfe);
                addedFile.addElement(pfe.getPath() + File.separator + pfe.getName());
              }
            }
          }
        }
        if(addedNode != null) {
          projectPanel.fileNodeSelected((ProjectPanelTreeNode)addedNode);
          projectPanel.setCurrentNode(addedNode);
        }

        mainFrm.getCodeContext().addFiles(addedFile);
        dlg = null;
      }
    }
    else {
    	// source path가 지정되어 있지 않으면 File Dialog를 띄운다.
			JFileChooser dlg = new JFileChooser();
      dlg.setPreferredSize(new Dimension(400, 300));
			dlg.setFileFilter(new FileFilter() {
				public boolean accept(java.io.File file) {
					if (file.isDirectory() || file.getName().toLowerCase().endsWith(".java") ||
              file.getName().toLowerCase().endsWith("html") || file.getName().toLowerCase().endsWith("htm")) return true;
					else return false;
				}

				public String getDescription() {
					return "*.java";
				}
			});
			dlg.setMultiSelectionEnabled(true);
      dlg.setCurrentDirectory(new File(project.getPath()));

      dlg.setLocation(250, 300);
      dlg.setDialogTitle("Add File(s)...");

      if (dlg.showOpenDialog(mainFrm) == JFileChooser.APPROVE_OPTION) {
        File files[] = dlg.getSelectedFiles();
        Vector addedFile = new Vector(10, 2);

        ProjectFileEntry pfe = null;
        for (int i = 0; i < files.length; i++ ) {
          pfe = project.addFile(files[i].getParent(), files[i].getName());
          if (pfe != null) {
            projectPanel.addFileToProject(pfe);
            addedFile.addElement(pfe.getFullPathName());
          }
        }
        if ((files.length == 1) && (pfe != null)) openDocument(pfe.getFullPathName(), modifiable);
        if (files.length == 0) {
          File file = dlg.getSelectedFile();
          pfe = project.addFile(file.getParent(), file.getName());
          if (pfe != null) {
            projectPanel.addFileToProject(pfe);
            
            addedFile.addElement(pfe.getFullPathName());
            openDocument(pfe.getFullPathName(), modifiable);
          }
        }
        mainFrm.getCodeContext().addFiles(addedFile);

        if(files != null) {
          for(int i=0; i<files.length; i++) files[i] = null;
          files = null;
        }
        addedFile = null;
      }

      dlg = null;
    }
  }

	private void addFolderToProject( DefaultMutableTreeNode parent, File folder, Vector addedFiles, boolean recursively ) {
    // 일단 ProjectPanel에 폴더를 추가한다.
    DefaultMutableTreeNode addedFolderNode = projectPanel.addFolder(parent, folder.getName());

    // 디렉토리 및의 소스 파일들을 찾고
    String[] list = folder.list(new FilenameFilter() {
      public boolean accept(File dir, String filename) {
      	File file = new File( dir, filename );
        if (filename.toLowerCase().endsWith(".java") && !file.isDirectory())
        	return true;
				if (file.isDirectory() && !filename.equals("CVS"))
					return true;

        return false;
      }
    });

    // 위에서 만든 폴더 아래에 추가한다.
    for (int k = 0; k < list.length; k++) {
      File file = new File(folder, list[k]);
			if ( file.isDirectory() && recursively ) {
				addFolderToProject( addedFolderNode, file, addedFiles, recursively );
			} else if ( !file.isDirectory() ) {
				// 파일이면,
	      //project에 등록한다
  	    ProjectFileEntry pfe = project.addFile(file.getParent(), file.getName());
  	    //이전에 등록되었던게 아니면
  	    if (pfe != null) {
  	      projectPanel.addObject(addedFolderNode, pfe, false);
  	      addedFiles.addElement(pfe.getFullPathName());
  	    }
  	  }
    }
	}
	
  public ProjectFileEntry addFileToProject(String pathName, String fileName, boolean modifiable) {
    ProjectFileEntry pfe = null;
    Vector addedFile = new Vector(1, 1);
    pfe = project.addFile(pathName, fileName);

    //이전에 없던 거였다면
    if (pfe != null) {
      projectPanel.addFileToProject(pfe);

      addedFile.addElement(pfe.getFullPathName());
      openDocument(pfe.getFullPathName(), modifiable);
      mainFrm.getCodeContext().addFiles(addedFile);
    }
    return pfe;
  }


  /*
  public Vector getLowerTreeModel(DefaultMutableTreeNode node) {
    Vector tmp = new Vector();

		if (node instanceof ProjectPanelTreeNode) {
    	ProjectPanelTreeNode pptn = (ProjectPanelTreeNode)node;
      if (pptn.isFolder()) {
        for (int i = 0; i < pptn.getChildCount(); i++) {
         	tmp = getLowerTreeModel((DefaultMutableTreeNode)pptn.getChildAt(i));
        }
      }
      else if (pptn.isFile()) {
      	ProjectFileEntry pfe = (ProjectFileEntry)pptn.getObject();
      	tmp.addElement(pfe);
      }
    }
    else {
    	for (int i = 0; i < node.getChildCount(); i++) {
      	tmp = getLowerTreeModel((DefaultMutableTreeNode)node.getChildAt(i));
      }
    }
    return tmp;
  }
  */
  /////////////////////////// ADD To PROJECT ///////////////////////////////
  public ProjectFileEntry addNewFileToProject(String pathName, String fileName, boolean modifiable) {
    ProjectFileEntry pfe = null;
    if (fileName != null && pathName != null) {
      pfe = project.addFile(pathName, fileName);
      pfe.setNewFile(true);

      if (pfe != null) {
        projectPanel.addFileToProject(pfe);
        openDocument(pfe.getFullPathName(), modifiable);
      }
    }
    return pfe;
  }

  /**
   * files tab 일경우만 해당된다. jdk source를 등록한다
   */
  public ProjectFileEntry addLibfileToProject(String fileName, JavaDocument doc){

    ProjectFileEntry pfe = project.addLibFile(fileName);
    if(pfe == null) return null;

    SourceEntry libEntry = new SourceEntry(fileName, doc);
    sourcePool.addSource(libEntry);

    projectPanel.setFilesTabbedPane(ProjectPanel.OPENEDLIST);
    projectPanel.addFileToDefaultProject(pfe);
    openDocument(libEntry, false);
    return pfe;
  }

  /**
   * Compile Error발생시 호출된다
   */
  public void openDocumentWhenCompileError(String path, String name){
    File f = new File(path, name);
    TextViewPanel tvp = getCurrentTvp();
    if(tvp != null && tvp.getSourceEntry().getFullPathName().equals(f.getAbsolutePath())) return;

    projectPanel.setSelectedFileWithExpand(path, name);
  }

  public ProjectPanel getProjectPanel(){
    return projectPanel;
  }  


	/**
	 *  프로젝트에서 파일을 삭제한다.
	 */
  public void removeFromProject(ProjectFileEntry pfe) {

    //project file에서 삭제한다
    project.removeFile(pfe);

    //source pool에서 삭제 한다
    sourcePool.deleteSource(pfe.getFullPathName());

    //opened list에서 삭제 한다
    openedList.removeElement(pfe);

    //save all을 위한 opened list에서도 삭제한다
    openedListForSaveAll.removeElement(pfe);

    //last open source 와 같으면 null로 세팅한다
    if(lastOpenedSource != null &&  lastOpenedSource.getFullPathName().equals(pfe.getFullPathName())) lastOpenedSource = null;
  }

  /**
   *  Compile Output 화면을 보여줄 것인가?
   */
  public void showMessageBoard(boolean b) {

  	if (b && (sp4.getBottomComponent() == null)) {

    	sp4.setBottomComponent(outputPanel);
      if (once) {
        Dimension d = this.getAccessibleContext().getAccessibleComponent().getSize();
		    lastPosition = d.height * 3 / 4;
        once = false;
        d = null;
      }

    	sp4.setDividerLocation(lastPosition);
    }
    else if (!b && (sp4.getBottomComponent() != null)) {

    	lastPosition = sp4.getDividerLocation();
      sp4.remove(outputPanel);
    }

    mainFrm.setMessageState(b);
  }

  public boolean isShowingMessageBoard() {
  	if (sp4.getBottomComponent() != null) return true;
  	return false;
  }

///////////////////// 컴파일 관련 시작 //////////////////////////////////////////////
	/**
	 *  현재 프로젝트를 컴파일한다.(Rebuild)
	 */
	public void buildProject() {
		mainFrm.saveAllCurrent();
    buildFiles(null);
	}

	/**
	 *  현재 프로젝트를 컴파일한다.(Make)
	 */
	public void makeProject() {
		mainFrm.saveAllCurrent();
    makeFiles(null);
	}

	/**
	 *  지정된 파일들을 컴파일한다.(Rebuild)
	 *
	 *  @param fileList 컴파일할 파일들
	 */
	public void buildFiles(Vector fileList) {
    Thread compileT = new BuildThread(fileList);
    compileT.setPriority(Thread.MIN_PRIORITY);
    compileT.start();
	}

	/**
	 *  지정된 파일들을 컴파일한다.(Make)
	 *
	 *  @param fileList 컴파일할 파일들
	 */
	public void makeFiles(Vector fileList) {
    Thread compileT = new CompileThread(fileList);
    compileT.setPriority(Thread.MIN_PRIORITY);
    compileT.start();
	}

  /**
   * Sun tools를 사용한 compiler Thread(Rebuild)
   *
   * @author kim sang kyun
   */
  class BuildThread extends Thread{
    Vector fileList;

    public BuildThread( Vector files ){
      fileList = files;
    }

    public void run(){

    	Compiler com = new Compiler(project);
      if (com != null) {
		  	if (outputPanel != null) {

          SwingUtilities.invokeLater(new Runnable(){
            public void run() {
			        outputPanel.clear();
            }
          });
  			}
        if (fileList == null)	com.buildProject(outputPanel);
        else if (fileList.size() == 1) com.buildFile((String)fileList.firstElement(), outputPanel);
       	else com.buildFiles(fileList, outputPanel);
        if (outputPanel.isNoError()) showMessageBoard(false);
        else {
        	showMessageBoard(true);
          outputPanel.selectFirstError();
        }
      }
    }
  }

  class CompileThread extends Thread {
    Vector fileList;

    public CompileThread( Vector files ) {
      fileList = files;
    }

    public void run(){

    	Compiler com = new Compiler(project);
      if (com != null) {
		  	if (outputPanel != null) {

          SwingUtilities.invokeLater(new Runnable(){
            public void run() {
			        outputPanel.clear();
            }
          });
  			}
        if (fileList == null) com.makeProject(outputPanel);
        else if (fileList.size() == 1) com.makeFile((String)fileList.firstElement(), outputPanel);
       	else com.makeFiles(fileList, outputPanel);
        if (outputPanel.isNoError()) showMessageBoard(false);
        else {
        	showMessageBoard(true);
          outputPanel.selectFirstError();
        }
      }
    }
  }

	/**
	 *  소스 파일을 컴파일한다.(하나의 file만을 선택해서 Make or Rebuild를 할 결우....)
	 */
  public void compileFile(String pathName, String fileName) {
    File f = new File(pathName, fileName);

  	if (fileName.toLowerCase().endsWith(".java") && f.exists()) {
			save();
  	  Vector file = new Vector(1, 1);
    	//ProjectFileEntry pfe = project.getFileEntry(f.getAbsolutePath());
	    //if (pfe != null) {
  	  	file.addElement(f.getAbsolutePath());
    	  CompileThread t = new CompileThread(file);
        t.start();
	    //}
    }
  }

  public void buildFile(String pathName, String fileName) {
    File f = new File(pathName, fileName);

  	if (fileName.toLowerCase().endsWith(".java") && f.exists()) {
			save();
  	  Vector file = new Vector(1, 1);
    	//ProjectFileEntry pfe = project.getFileEntry(f.getAbsolutePath());
	    //if (pfe != null) {
  	  	file.addElement(f.getAbsolutePath());
    	  BuildThread t = new BuildThread(file);
        t.start();
	    //}
    }
  }

  /**
	 *  주어진 html file을 이용해서 appletviewer를 실행한다.
	 *
	 *  @param fileName html file 이름
	 */
  public void runApplet(String pathName, String fileName) {
   AppletRunner runner = new AppletRunner(project);
   if(runner != null) {
      if(outputPanel != null) {
         mainFrm.showOutputDialog();
      }
 		  OutputFrame frm = mainFrm.showOutputDialog();
      runner.appletRun(pathName, fileName, frm.getStdOut());
    }
  }

  /**
   * 주어진 java source에 대한 javadoc을 만들어 준다.
   *
   * @param fileName java file 이름
   * @Author Itree
   */
  public void makeJavadocPrj() {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)projectPanel.getRootNode();
    //Vector fileList = project.getFiles();
    lowerFiles = new Vector();
    getLowerFileModel(node);

    //Thread makeJavadocT = new JavadocMakeThread(fileList);
    Thread makeJavadocT = new JavadocMakeThread(lowerFiles);
    makeJavadocT.setPriority(Thread.MAX_PRIORITY);
    makeJavadocT.start();
    //JavaDocBuilder builder = new JavaDocBuilder(project);
    //if (builder != null) {
    //  OutputFrame frm = mainFrm.showOutputDialog();
    //  builder.buildDocProject(lowerFiles,frm);
    //}
  }

  public void makeJavadocDir() {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)projectPanel.getCurrentNode();
    lowerFiles = new Vector();
    getLowerFileModel(node);


    /*
    for (int i=0; i<fileString.length(); i++) {
      if( fileString.charAt(i) == '\n' ) {
        fileList.addElement(filebuffer.toString());
        filebuffer = new StringBuffer();
      }
      else {
        filebuffer.append(fileString.charAt(i));
      }
    }
    */
    Thread makeJavadocT = new JavadocMakeThread(lowerFiles);
    makeJavadocT.setPriority(Thread.MAX_PRIORITY);
    makeJavadocT.start();
    //JavaDocBuilder builder = new JavaDocBuilder(project);
    //if (builder != null) {
    //  OutputFrame frm = mainFrm.showOutputDialog();
    //  builder.buildDocProject(lowerFiles,frm);
    //}
  }

  public void getLowerTreeModel(DefaultMutableTreeNode node) {

		if (node instanceof ProjectPanelTreeNode) {
    	ProjectPanelTreeNode pptn = (ProjectPanelTreeNode)node;
      if (pptn.isFolder()) {
//        ProjectFileEntry pfe = (ProjectFileEntry)pptn.getObject();
      	lowerFiles.addElement(pptn.toString());
        for (int i = 0; i < pptn.getChildCount(); i++) {
          DefaultMutableTreeNode childnode =(DefaultMutableTreeNode)pptn.getChildAt(i);
          ProjectPanelTreeNode childpptn = (ProjectPanelTreeNode)childnode;
          if (childpptn.isFile()) {
            ProjectFileEntry pfe = (ProjectFileEntry)childpptn.getObject();
            //lowerFiles.addElement(pfe);
            break;
          }
        }
        for (int i = 0; i < pptn.getChildCount(); i++) {
          DefaultMutableTreeNode childnode =(DefaultMutableTreeNode)pptn.getChildAt(i);
          ProjectPanelTreeNode childpptn = (ProjectPanelTreeNode)childnode;
          if (childpptn.isFolder()) {
            getLowerTreeModel(childnode);
          }
        }
      }
      else if (pptn.isFile()) {
//      	ProjectFileEntry pfe = (ProjectFileEntry)pptn.getObject();
//      	lowerFiles.addElement(pfe);
      }
    }
    else {
    	for (int i = 0; i < node.getChildCount(); i++) {
      	getLowerTreeModel((DefaultMutableTreeNode)node.getChildAt(i));
      }
    }
  }

  public void getLowerFileModel(DefaultMutableTreeNode node) {

    if (node instanceof ProjectPanelTreeNode) {
     	ProjectPanelTreeNode pptn = (ProjectPanelTreeNode)node;
      if (pptn.isFolder()) {
        for (int i = 0; i < pptn.getChildCount(); i++) {
         	getLowerFileModel((DefaultMutableTreeNode)pptn.getChildAt(i));
        }
      }
      else if (pptn.isFile()) {
      	ProjectFileEntry pfe = (ProjectFileEntry)pptn.getObject();
      	lowerFiles.addElement(pfe);
      }
    }
    else {
    	for (int i = 0; i < node.getChildCount(); i++) {
      	getLowerFileModel((DefaultMutableTreeNode)node.getChildAt(i));
      }
    }  }

  class JavadocMakeThread extends Thread {
    Vector fileList;
    public JavadocMakeThread(Vector files) {
      fileList = files;
    }
    public void run() {
      JavaDocBuilder javadocBuilder = new JavaDocBuilder(project);
      if(javadocBuilder != null) {
        OutputFrame frm = mainFrm.showOutputDialog();
        javadocBuilder.buildDocProject(fileList, frm);
      }
    }
  }
  /**
   * 주어진 java source에 대한 javadoc을 만들어 준다.
   *
   * @param fileName java file 이름
   * @Author Itree
   */
  public void makeJavadocFile(String pathName, String fileName) {
    JavaDocBuilder javadocBuilder = new JavaDocBuilder(project);
    if(javadocBuilder != null) {

      OutputFrame frm = mainFrm.showOutputDialog();
      javadocBuilder.buildDocFile(pathName, fileName, frm);
    }
  }

	/**
	 *  주어진 클래스를 실행한다.
	 *
	 *  @param className 실행할 클래스 이름
	 */
	public void runClass(String className) {
    if (className == null) return;
  	final String name = className;
  	Thread t = new Thread(new Runnable() {
    	public void run() {
        String path = lastOpenedSource.getPath();
        String fileName = lastOpenedSource.getName();
        Vector files = new Vector(1,1);

        files.addElement(project.getFileEntry(lastOpenedSource.getFullPathName()));
  		  Thread compileT = new BuildThread(files);
		    compileT.setPriority(Thread.MIN_PRIORITY);
    		compileT.start();
		    try {
	  		  compileT.join();
		    } catch (InterruptedException e) {
		    }
		  	if (intptr != null) {
		    	intptr = null;
    		  System.gc();
		      System.gc();
    		}
		    intptr = new Interpreter(project);
    		if (intptr != null) {
    		  OutputFrame frm = mainFrm.showOutputDialog();
    		  intptr.interpret(name, frm.getStdOut(), frm.getStdErr() );
		    }
      }
    });
    t.setPriority(Thread.MIN_PRIORITY);
    t.start();
	}

	/**
	 *  실행한 프로세스를 죽인다.
	 */
	public void killProcess() {
		if (intptr != null) intptr.kill();
	}

///////////////////// 컴파일 관련 끝//////////////////////////////////////////////
	/**
	 *  Source를 보여준다
	 *
	 *  @param se 보여줄 소스
	 *  @param modifiable 열 파일을 수정할 수 있도록 할건지 선택
	 */
  public void openDocument(SourceEntry se, boolean modifiable) {

  	TextViewPanel curTvp = getCurrentTvp();
    TextViewPanel focusedTvp = null;
    statusBar.hideRightStatus(false);

    if(sp2.getTopComponent() == null || sp2.getBottomComponent() == null) {
      int location = sp2.getDividerLocation();
      upperTvp.setSourceEntry(se);
      sp2.setTopComponent(upperTvp);

      bottomTvp.setSourceEntry(se);

      sp2.setBottomComponent(bottomTvp);
      sp2.setDividerLocation(location);

      focusedTvp = bottomTvp;
    }
    else{
      focusedTvp = getCurrentTvp();
      focusedTvp.setSourceEntry(se);
    }

    if (!modifiable) focusedTvp.getEditor().setEditable(false);
    else focusedTvp.getEditor().setEditable(true);

    boolean found = false;

    ProjectFileEntry pf = project.getFileEntry(se.getFullPathName());

    for (int i = 0; i < openedList.size(); i++) {
      ProjectFileEntry pfe = (ProjectFileEntry)openedList.elementAt(i);
      if (pfe != null && pfe.equals(pf)) {
        found = true;
        break;
      }
    }

    if (!found) {
      if(pf != null){
        pf.syncLastModifiedTimeWithRealFile();
        openedList.addElement(pf);
        openedListForSaveAll.addElement(pf);
      }
    }
    fileSelected(se);
    mainFrm.setCaptionText(project.getProjectName() + " - "	+ se.getFullPathName());

    //가장 최근에 오픈된 source를 갱신한다
    lastOpenedSource = null;
    lastOpenedSource = se;
  }

	/**
	 *  소스 파일을 에디터에 연다
	 */
  public void openDocument(String fullPath, boolean modifiable) {
    SourceEntry se = sourcePool.getSource(fullPath);

    if (se != null) openDocument(se, modifiable);
 }

	/**
	 *  소스 파일을 에디터에 연다
	 */
  public boolean openDocument(ProjectFileEntry pfe, boolean modifiable) {
    if(!pfe.isNewFile() && !pfe.isLibraryFile() && !pfe.isExists()){
      mainFrm.setCaptionText(project.getProjectName() + " - "	+ pfe.getFullPathName());
      closeDocument();
      return false;
    }

    SourceEntry se = sourcePool.getSource(pfe);
    if (se != null) openDocument(se, modifiable);

    return true;
 }


  /**
   * 위, 아래 TextViewPanel을 화면에서 제거하고 sourcepool에서는 제거하지 않는다 (Side Effect때문)
   * source browser내용도 제거한다
   */
  public void closeDocument() {
    if(sp2.getTopComponent() != null) {
      upperTvp.clear();
      sp2.remove(upperTvp);

    }
    if(sp2.getBottomComponent() != null) {
      bottomTvp.clear();
      sp2.remove(bottomTvp);
    }

    if(sourceBrowser != null) {
      sourceBrowser.clear();
    }
    //itree 추가.
    statusBar.hideRightStatus(true);

    if(!isFilesTab()){
      overrideP.removeAll();
      setOverridePanelVisible(false);
    }

    lastOpenedSource = null;
  }

  public void closeFile(){
  	closeOpenFile();
    closeDocument();
  }

  public void closeOpenFile() {
    if(lastOpenedSource == null) return;

    ProjectFileEntry pfe = project.getFileEntry(lastOpenedSource.getFullPathName());
    if(lastOpenedSource.isModified()){
      int op = JOptionPane.showConfirmDialog(mainFrm, lastOpenedSource.getFullPathName() +
                                  " is modified. Save it?",
                                  "File Close", JOptionPane.YES_NO_OPTION);

      if (op == JOptionPane.YES_OPTION) {
        lastOpenedSource.saveFile();
        pfe.syncLastModifiedTimeWithRealFile();
      }
    }

    projectPanel.setCurrentFileClosed();
    sourcePool.deleteSource(lastOpenedSource.getFullPathName());

    openedList.removeElement(pfe);
    openedListForSaveAll.removeElement(pfe);
    lastOpenedSource = null;
  }

  public void closeAllOpenFile() {
    int closedCount=0;
    int savedCount= 0;
    int unsavedCount = 0;
    boolean flag = true;

    Vector partiallyClosed = new Vector();
		for (int i = 0; i < openedListForSaveAll.size(); i++) {
			ProjectFileEntry pfe = (ProjectFileEntry)openedListForSaveAll.elementAt(i);
			SourceEntry se = sourcePool.getSource(pfe);

      if(se.isModified()){
        int op = JOptionPane.showConfirmDialog(mainFrm, se.getFullPathName() +
                                  " is modified. Save it?",
                                  "File Close", JOptionPane.YES_NO_CANCEL_OPTION);

        if (op == JOptionPane.YES_OPTION) {
          se.saveFile();
          pfe.syncLastModifiedTimeWithRealFile();
          savedCount++;
          partiallyClosed.addElement(new Integer(i));
          openedList.removeElement(pfe);
        } else if (op == JOptionPane.NO_OPTION) {
          unsavedCount++;
          partiallyClosed.addElement(new Integer(i));
          openedList.removeElement(pfe);
        } else {
          flag = false;
          break;
        }
      }

      if(upperTvp != null && upperTvp.getSourceEntry() != null && upperTvp.getSourceEntry().equals(se))
        upperTvp.clear();

      if(bottomTvp != null && bottomTvp.getSourceEntry() != null && bottomTvp.getSourceEntry().equals(se))
        bottomTvp.clear();

      closedCount++;
    }

    if(flag) {
      projectPanel.setOpenFileAllClosed();
      sourcePool.deleteAll();
      lastOpenedSource = null;
      closeDocument();
      openedListForSaveAll.removeAllElements();
      openedList.removeAllElements();
    } else {
      for(int i=0;i<partiallyClosed.size(); i++){
        openedListForSaveAll.removeElementAt( ((Integer)partiallyClosed.elementAt(i)).intValue());
      }
    }
  }
////////////////////////// 저장 관련 시작 /////////////////////////////////////

	/**
	 *  세이브를 수행한다.
   *
   *  @return saved file
	 */
	public String save() {

    String savedFile = null;
    TextViewPanel focusedTvp = getCurrentTvp();
  	if (focusedTvp != null) {
    	SourceEntry se = focusedTvp.getSourceEntry();
      if (se != null && se.isModified())	{
        if(save(se) == true){
          savedFile = se.getFullPathName();
        }
        else {
          savedFile = null;
        }
      }
    }

    return savedFile;
	}

  /**
   * Mainframe의 save all menu가 선택되었을때 동작
   */
  public int saveAll(){
    int savedCount=0;
		for (int i = 0; i < openedListForSaveAll.size(); i++) {
			ProjectFileEntry pfe = (ProjectFileEntry)openedListForSaveAll.elementAt(i);
			SourceEntry se = sourcePool.getSource(pfe);
      if(se.isModified()){
  		  se.saveFile();
        pfe.syncLastModifiedTimeWithRealFile();
        if(pfe.isNewFile()) pfe.setNewFile(false);
        mainFrm.getCodeContext().modifyTableWhenSaveFile(true);
        savedCount++;
      }
    }
    return savedCount;
  }

	/**
	 *  지정한 파일을 세이브한다.
	 */
  public boolean save(SourceEntry se) {
    if(se.saveFile()){
      ProjectFileEntry pfe = project.getFileEntry(se.getFullPathName());
      pfe.syncLastModifiedTimeWithRealFile();
      if (mainFrm != null) mainFrm.getCodeContext().modifyTableWhenSaveFile(true);
      return true;
    }

    return false;
  }

	/**
	 *  Save As를 수행한다.
	 */
  public String saveAs() {

    String savedFile = null;

    TextViewPanel focusedTvp = getCurrentTvp();
    if(focusedTvp == null) return null;

    SourceEntry se = focusedTvp.getSourceEntry();
    ProjectFileEntry pfe = project.getFileEntry(se.getFullPathName());
    if (se != null && pfe != null) {
      FileDialog dlg = new FileDialog(mainFrm);
      dlg.setMode(FileDialog.SAVE);
      dlg.setDirectory(se.getPath());
      dlg.setFile(se.getName());
      dlg.show();
      String file = dlg.getFile();
      if (file != null) {
        String directory = dlg.getDirectory();
        se.setFile(directory, file, true);
        project.removeFile(pfe);
        projectPanel.removeNode(pfe);
        File f = new File(directory, file);
        ProjectFileEntry newFE = new ProjectFileEntry(f.getParent(), f.getName());
        project.addFile(newFE);
        projectPanel.addFileToProject(newFE);
        project.isModified = true;
        se.saveFile();
        openDocument(pfe.getFullPathName(), true);
        projectPanel.repaint();

        savedFile= se.getFile().getAbsolutePath();
      }

      dlg = null;
    }

    return savedFile;
  }
  ////////////////////////// 저장 관련 끝 /////////////////////////////////////

  /**
   *  실행 파라미터를 수정할 때
   */
  public void showParameters() {
  	ProjectRunParamDlg dlg = new ProjectRunParamDlg(mainFrm, "Parameters...", true);
    dlg.setParams(project.getParameters());
    dlg.setUsable(project.isUsableParam());
    dlg.setVisible(true);
    if (dlg.isOk()) {
    	project.setParameters(dlg.getParams());
      project.setUsableParam(dlg.isUsable());
    }
    dlg = null;
  }

	/**
	 *  가장 최근에 오픈된  소스를 반환한다
	 *
	 *  @return SourceEntry 현재 열린 소스
	 */
	public SourceEntry getLastOpenedSource() {
    return lastOpenedSource;
	}

  /**
   * 포커스를 얻은 TextViewPanel의 source와 최근에 선택된 파일이
   * 다를때, 그 source가 트리에서 선택된 효과를 준다
   */
  public void focusOfTextViewPanelChanged(SourceEntry focusedSE){

    if(lastOpenedSource != focusedSE){
      fileSelected(focusedSE);
      lastOpenedSource = focusedSE;

      bottomTvp.hidePopups();
      upperTvp.hidePopups();
    }

    else if(upperTvp.getSourceEntry() == bottomTvp.getSourceEntry()){
      bottomTvp.hidePopups();
      upperTvp.hidePopups();
    }
  }

  /**
   * 현재 포커스를 얻은 TextViewPanel을 반환
   */
  public TextViewPanel getCurrentTvp(){
    if(sp2.getTopComponent() == null && sp2.getBottomComponent()==null) return null;
    else return (TextViewPanel.focusedPosition.equals(TextViewPanel.UPPER) ? upperTvp : bottomTvp);
  }

  /**
   * files tab일 경우 만 해당된다. 현재 오픈되었던 library file들을 닫는다
   */
  public void clearLibraryFiles(){

    Vector pfes = project.getFiles();
    Vector libPfes = new Vector();
    for(int i=0; i<pfes.size(); i++){
      ProjectFileEntry pfe = (ProjectFileEntry)pfes.elementAt(i);
      if(pfe.isLibraryFile()) {

        DefaultMutableTreeNode node = projectPanel.getCurrentNode();
        if(node != null && node.getUserObject().equals(pfe)){
          closeDocument();
          lastOpenedSource = null;
        }

        projectPanel.removeNode(pfe);
        removeFromProject(pfe);
        libPfes.addElement(pfe);
      }
    }


    for(int i=0; i<libPfes.size(); i++){
      project.removeFile((ProjectFileEntry)libPfes.elementAt(i));
    }

    projectPanel.getFileBrowserPanel().clear();
    projectPanel.setOpenFileAllClosed();
    project.clear();
  }

	/**
	 *  ClassDesigner에서 생성된source
	 */
	public void newCodeGenerated(String source, String packageName, String className) {

    String sourceRoot = project.getPathModel().getSourceRoot();
    if(sourceRoot.endsWith(File.separator)) sourceRoot = sourceRoot.substring(0, sourceRoot.length()-2);
    
    String fileName = className + ".java";
    String relativePath = "";

    if (packageName != null) {
      StringTokenizer st = new StringTokenizer(packageName);
      StringBuffer sb = new StringBuffer();
      while(st.hasMoreTokens()) {
        String token = st.nextToken(".");
        if ((token != null) && (token != "")) sb.append(token + File.separator);
        else break;
      }
      relativePath = sb.toString();
    }

    String path = "";


    if(sourceRoot == null) path = IdePropertyManager.GENERATED_SOURCE_ROOT_DIR+File.separator+relativePath;
    else path = sourceRoot + File.separator + relativePath;

    //sourcepool에 등록
    SourceEntry entry = sourcePool.newSource(path, fileName, source);

    //project에 등록 하고 오픈한다
    ProjectFileEntry pfe = addNewFileToProject(path, fileName, true);
    //pfe.syncLastModifiedTimeWithRealFile();
  }

	/**
	 *  CodeContext로부터 넘어오는 Edit관련 Event를 처리한다.
	 *
	 *  @param event 추가할 Item들이 들어있다.
	 */
  Hashtable prevTreeModel = new Hashtable(15, 0.9F);
  Hashtable currTreeModel = new Hashtable(15, 0.9F);
	public void insertEvent(SourceBrowserEvent event) {

    if ((event != null) && (sourceBrowser != null)) {

      //모두 새로 넣어야 할 경우
      if(prevTreeModel.size() == 0){

        EventContent content = event.firstEvent();
        Vector v = new Vector(15, 5);
        if (content != null)
        do {
            prevTreeModel.put(content.getHashtableKey(), content);
            v.addElement(content);
        } while (event.hasMoreEvents() && ((content = event.nextEvent()) != null));

        if(!isFilesTab()) overrideP.removeAll();

        changeSourceBrowser(v, true);
        sourceBrowser.setTreeModel();
        return;
      }

      //넣거나 빼야 할 경우
      else {
        currTreeModel.clear();
        EventContent content = event.firstEvent();
        if (content != null)
        do {
          currTreeModel.put(content.getHashtableKey(), content);
        } while (event.hasMoreEvents() && ((content = event.nextEvent()) != null));
      }

      Vector toRemove = new Vector(5, 2);
      if(prevTreeModel.size() > 0)
      for(Enumeration e = prevTreeModel.keys(); e.hasMoreElements(); ){
        String key = (String)e.nextElement();
        if(currTreeModel.get(key) == null){
          toRemove.addElement(prevTreeModel.get(key));
          prevTreeModel.remove(key);
        }
      }

      if(toRemove.size() > 0) changeSourceBrowser(toRemove, false);

      Vector toInsert = new Vector(5, 2);
      for(Enumeration e = currTreeModel.keys(); e.hasMoreElements(); ){
        String key = (String)e.nextElement();
        if(prevTreeModel.get(key) == null){
          EventContent ec = (EventContent)currTreeModel.get(key);
          if(ec.getContentType() == EventContent.CLASS){
            sourceBrowser.addClassNode(ec);
            prevTreeModel.put(key, ec);
          }
          else if(ec.getContentType() == EventContent.INNER){
            Vector parentsWithMe = new Vector();
            parentsWithMe.addElement(ec);
            EventContent pec = (EventContent)currTreeModel.get( ec.getParent().getHashtableKey() );

            while(pec != null){
              parentsWithMe.addElement(pec);
              currTreeModel.remove(pec.getHashtableKey());
              if(pec.getParent() != null){
                pec = (EventContent)currTreeModel.get( pec.getParent().getHashtableKey() );
              }
              else{
                pec = null;
              }
            }

            for(int i=parentsWithMe.size()-1; i>=0; i--){
              EventContent iec = (EventContent)parentsWithMe.elementAt(i);
              if(iec.getContentType() == EventContent.CLASS) sourceBrowser.addClassNode(iec);
              else if(iec.getContentType() == EventContent.INNER) sourceBrowser.addInnerClassNode(iec);

              prevTreeModel.put(iec.getHashtableKey(), iec);
            }
          }
          else{
            toInsert.addElement(ec);
            prevTreeModel.put(key, ec);
          }
        }
      }

      if(toInsert.size() > 0) changeSourceBrowser(toInsert, true);

      //classnode들을 expand시킨다
      sourceBrowser.expandClassNodes();
    }
  }

  class OverrideItem {
    public String data;
    public int type;

    public OverrideItem(String data, int type){
      this.data = data;
      this.type = type;
    }
  }

  /**
   * source browser의 tree node에서 빼거나 더한다
   */
  private void changeSourceBrowser(Vector data, boolean insert){
    final Vector overrideData = new Vector(5,2);

    int size = data.size();
    for(int i=0; i<size; i++){
      EventContent c = (EventContent)data.elementAt(i);
      int type = c.getContentType();

      switch (type) {
        case EventContent.CLASS:
          if(insert)	{
            sourceBrowser.addClassNode(c);
            if(!isFilesTab()) overrideData.addElement(new OverrideItem(c.getContent() , OverridePanel.CLASS));
          }
          else {
            sourceBrowser.removeNode(type, c);
            if(!isFilesTab()) overrideData.addElement(new OverrideItem(c.getContent(), OverridePanel.CLASS));
          }

          break;

        case EventContent.INTERFACE:
          if(insert) {
             sourceBrowser.addInterfaceNode(c);
          }
          else sourceBrowser.removeNode(type, c);
          break;

        case EventContent.ATTR:
          if(insert) sourceBrowser.addAttributeNode(c);
          else sourceBrowser.removeNode(type, c);
          break;

        case EventContent.OPER:
          if(insert) sourceBrowser.addOperationNode(c);
          else sourceBrowser.removeNode(type, c);
          break;

        case EventContent.INNER:
          if(insert) {
            sourceBrowser.addInnerClassNode(c);
            if(!isFilesTab()) overrideData.addElement(new OverrideItem(c.getContent(), OverridePanel.INNER));
          }
          else {
            sourceBrowser.removeNode(type, c);
            if(!isFilesTab()) overrideData.addElement(new OverrideItem(c.getContent(), OverridePanel.INNER));
          }

          break;

        case EventContent.IMPORT:
          if(insert) sourceBrowser.addImportNode(c);
          else sourceBrowser.removeNode(type, c);
          break;

        case EventContent.PACKAGE:
          if(insert) sourceBrowser.addPackageNode(c);
          else sourceBrowser.removeNode(type, c);
          break;
        default:
      }
    }

    //override panel update

    if(!isFilesTab()){
      final boolean isInsert = insert;
      SwingUtilities.invokeLater(new Runnable(){
        public void run(){
          for(int i=0; i<overrideData.size(); i++){
            OverrideItem item = (OverrideItem)overrideData.elementAt(i);
            if(isInsert) {
              overrideP.addClass(item.data, item.type);
            }
            else overrideP.removeClass(item.data, item.type);
          }
        }
      });
    }
  }

	public void showEventList(EditFunctionEvent event) {
		// Event 자체를 넘겨주는 것은 좋지 못한 것 같다.
		// 다른 곳에 쓰일 수 있도록 범용성을 주는 것이 나을 것 같다.
		// Event에서 값들을 뽑아서 보내는 것이 낫겠군.
		Vector eventValues = new Vector();
    TextViewPanel focusedTvp = getCurrentTvp();

  	eventValues.addElement(event.firstEvent());
		while(event.hasMoreEvents()) eventValues.addElement(event.nextEvent());

    if(eventValues == null || eventValues.isEmpty() || focusedTvp == null) return;
		switch(event.getEventType()) {
		// Intellisense Event의 경우
		case EditFunctionEvent.INTELLISENSE:
      focusedTvp.showIntellisense(eventValues);
			break;

    case EditFunctionEvent.IMPORTING :
			focusedTvp.showImporting(eventValues);
			break;

    case EditFunctionEvent.NEWING :
      focusedTvp.showNewing(eventValues);
			break;

		// Parameterizing Event의 경우
		case EditFunctionEvent.PARAMETERING:
			focusedTvp.showParameterizing(eventValues);
			break;

    case EditFunctionEvent.IMPLEMENTS :
      focusedTvp.showImplements(eventValues);
      break;
		}
	}

	/** MainFrame의 레퍼런스를 얻어온다. */
	public MainFrame getMainFrame() {
		return mainFrm;
	}

	/**
	 *  새로운 파일이 선택되었을 때 처리(주로 SourceBrowser를 위함)
	 *
	 *  @param se 선택된 파일 소스
	 */
	public void fileSelected(SourceEntry se) {

 	if (se != null) {
     	boolean status = false;
			mainFrm.displayMessageAtStatusBar(" ");

			projectPanel.setSelectionFile(se.getPath(), se.getName());

      // 이전 sourcebrowser 데이타를 지운다
      prevTreeModel.clear();
      sourceBrowser.clear();

			if (mainFrm.getCodeContext() != null && se.getDocumentType() == SourceEntry.JAVA){
        if(!isOverridePanelVisible) {
          setOverridePanelVisible(true);
        }

        if(!isSourceBrowserVisible) {
          setSourceBrowserVisible(true);
        }

        sourceBrowser.reload();
        mainFrm.getCodeContext().setSourceEntry(se);
      }
      else{

        if(isOverridePanelVisible){
          setOverridePanelVisible(false);
        }

        if(isSourceBrowserVisible) {
          setSourceBrowserVisible(false);
        }
      }

      TextViewPanel focusedTvp = getCurrentTvp();
			if (focusedTvp != null) {
        focusedTvp.requestFocus();
      }
		}
	}

	/**
	 *  특정위치로 Caret을 옮길때
	 *
	 *  @param int 옮길 offset
	 */
	public void moveCaret(int offset){
	//public void moveCaret(int line){

    TextViewPanel focusedTvp = getCurrentTvp();
		if (focusedTvp != null) {
      focusedTvp.moveCaret(offset, false);
      //focusedTvp.moveLine(line,true);
    }
	}

	/**
	 *  케럿을 특정 라인으로 이동한다.
	 */
	public void moveLine(int line) {
    TextViewPanel focusedTvp = getCurrentTvp();
		if (focusedTvp != null) focusedTvp.moveLine(line, true);
	}

	/**
	 *
	 */
	public void sourceBrowserSelection(String parent,String selected) {
		mainFrm.getCodeContext().selectSourceBrowser(parent,selected);
    if(sp2.getTopComponent() != null) upperTvp.hidePopups();
    if(sp2.getBottomComponent() != null) bottomTvp.hidePopups();
	}

	/**
	 *  Code Context에서 처리한 indent를 보여주기 위해 해당
	 *  소스를 다시 읽어들인다.
	 */
	public void notifyChangeDocument() {
		if ((project != null) && ProjectManager.getCurrentProject().equals(this.project)) {
      TextViewPanel focusedTvp = getCurrentTvp();
			if (focusedTvp != null) focusedTvp.getEditor().setDocument(focusedTvp.getSourceEntry().getDocument());
		}
	}

  public void addOverriedMethod(String source, int pos){
    TextViewPanel focusedTvp = getCurrentTvp();
    if(focusedTvp!=null && focusedTvp.getEditorStatusBar().isReadOnlyMode()){
      JOptionPane.showMessageDialog(MainFrame.mainFrame, "Current file is Read Only", "alert", JOptionPane.ERROR_MESSAGE);
      return;
    }

    SourceEntry se = getLastOpenedSource();
    JavaDocument doc = (JavaDocument)se.getDocument();
    try {
      focusedTvp.moveCaret(pos, false);
      doc.insertString(pos, source);
    } catch (javax.swing.text.BadLocationException e) {
      System.err.println(e);
    }
  }

  public void addHandler(String className, boolean isInner) {
    TextViewPanel focusedTvp = getCurrentTvp();

    if(focusedTvp!=null && focusedTvp.getEditorStatusBar().isReadOnlyMode()){
      JOptionPane.showMessageDialog(MainFrame.mainFrame, "Current file is Read Only", "alert", JOptionPane.ERROR_MESSAGE);
      return;
    }

    EventHandlerListDlg dlg = new EventHandlerListDlg();
    if(isInner) dlg.setFirstIndent("\t\t");
    dlg.showWindow();
    SourceEntry se = getLastOpenedSource();
    if (dlg.isOK() && se != null) {
    	JavaDocument doc = (JavaDocument)se.getDocument();
      try {
        int offsetInserted = mainFrm.getCodeContext().getInsertPosition(className);
        focusedTvp.moveCaret(offsetInserted, false);
        doc.insertString(offsetInserted, dlg.getGeneratedSource());

        //if(dlg.isBeautifyOn()) mainFrm.getCodeContext().indentDocument();
      } catch (javax.swing.text.BadLocationException e) {
				System.err.println(e);
      }
    }

    dlg = null;
  }

  public void addMethod(String className, boolean isInner) {
    TextViewPanel focusedTvp = getCurrentTvp();

    if(focusedTvp!=null && focusedTvp.getEditorStatusBar().isReadOnlyMode()){
      JOptionPane.showMessageDialog(MainFrame.mainFrame, "Current file is Read Only", "alert", JOptionPane.ERROR_MESSAGE);
      return;
    }

    MethodDialog dlg = new MethodDialog(MainFrame.mainFrame, "Method Add Dialog", true, MethodDialog.ADD);
    if(isInner) dlg.setFirstIndent("\t\t");
    dlg.setVisible(true);

    SourceEntry se = getLastOpenedSource();
    if (dlg.isOK() && se != null) {

    	JavaDocument doc = (JavaDocument)se.getDocument();
      try {
        int offsetInserted = mainFrm.getCodeContext().getInsertPosition(className);
        focusedTvp.moveCaret(offsetInserted, false);
        doc.insertString(offsetInserted, dlg.getGeneratedSource());

      } catch (javax.swing.text.BadLocationException e) {
	  		System.err.println(e);
      }
    }

    dlg = null;
  }

  public void addGetSetMethod(JTree tree, DefaultMutableTreeNode currNode, boolean isInner) {
    TextViewPanel focusedTvp = getCurrentTvp();

    if(focusedTvp!=null && focusedTvp.getEditorStatusBar().isReadOnlyMode()){
      JOptionPane.showMessageDialog(MainFrame.mainFrame, "Current file is Read Only", "alert", JOptionPane.ERROR_MESSAGE);
      return;
    }

    GetSetAddDialog dlg = new GetSetAddDialog();
    dlg.setTreeAndNode(tree, currNode);
    if(isInner) dlg.setFirstIndent("\t\t");
    dlg.showDialog();

    SourceEntry se = getLastOpenedSource();
    if (dlg.isOK() && se != null) {
    	JavaDocument doc = (JavaDocument)se.getDocument();
      try {
        EventContent ec = (EventContent)currNode.getUserObject();
        String key = ec.getHashtableKey();
        int offsetInserted = mainFrm.getCodeContext().getInsertPosition(key);
        focusedTvp.moveCaret(offsetInserted, false);
        doc.insertString(offsetInserted, dlg.getGeneratedSource());

      } catch (javax.swing.text.BadLocationException e) {
	  		System.err.println(e);
      }
    }

    dlg = null;
  }


  public void addMain(String className, boolean isInner) {
    TextViewPanel focusedTvp = getCurrentTvp();

    if(focusedTvp!=null && focusedTvp.getEditorStatusBar().isReadOnlyMode()){
      JOptionPane.showMessageDialog(MainFrame.mainFrame, "Current file is Read Only", "alert", JOptionPane.ERROR_MESSAGE);
      return;
    }

    String gap = "\t";
    String firstIndent = (isInner) ? "\t\t" : "\t";
    StringBuffer source = new StringBuffer();

    source.append("\n\n");
    source.append(firstIndent + "/**" + "\n");
    source.append(firstIndent + " * " + "Main method" + "\n");
    source.append(firstIndent + " * " + "\n");
    source.append(firstIndent + " * " + "@param args" + " command line arguments" + "\n");
    source.append(firstIndent + " */" + "\n");

    source.append(firstIndent + "public static void main( String [] args )" + "\n");
    source.append(firstIndent + "{" + "\n");
    source.append(firstIndent + gap + "//TO DO (implementation here)" + "\n");
    source.append(firstIndent + "}" );
    source.append("\n");

    SourceEntry se = getLastOpenedSource();
    if (se != null) {
    	JavaDocument doc = (JavaDocument)se.getDocument();
      try {
        int offsetInserted = mainFrm.getCodeContext().getInsertPosition(className);
        focusedTvp.moveCaret(offsetInserted, false);
        doc.insertString(offsetInserted, source.toString());

      } catch (javax.swing.text.BadLocationException e) {
	  		System.err.println(e);
      }
    }
  }

  public void addField(String className, boolean isInner) {
    TextViewPanel focusedTvp = getCurrentTvp();

    if(focusedTvp!=null && focusedTvp.getEditorStatusBar().isReadOnlyMode()){
      JOptionPane.showMessageDialog(MainFrame.mainFrame, "Current file is Read Only", "alert", JOptionPane.ERROR_MESSAGE);
      return;
    }

    FieldDialog dlg = new FieldDialog(MainFrame.mainFrame, "Field Add Dialog", true, FieldDialog.ADD);
    if(isInner) dlg.setFirstIndent("\t\t");
    dlg.setVisible(true);

    SourceEntry se = getLastOpenedSource();
    if (dlg.isOK() && se != null) {

    	JavaDocument doc = (JavaDocument)se.getDocument();

      try {
        // getter insert
        int offsetOfGetter = mainFrm.getCodeContext().getInsertPosition(className);
        String getter = dlg.getGeneratedGetterMethodSource();
        if(getter != null) {
          doc.insertString(offsetOfGetter, getter);
        }

        // setter insert
        int offsetOfSetter = mainFrm.getCodeContext().getInsertPosition(className);
        String setter = dlg.getGeneratedSetterMethodSource();
        if(setter != null) doc.insertString(offsetOfSetter, setter);

        //field insert
        //TO DO
        int offsetOfField = mainFrm.getCodeContext().getInsertPosition(className);
        String field = dlg.getGeneratedFieldSource();
        focusedTvp.moveCaret(offsetOfField, false);
        doc.insertString(offsetOfField, field);

      } catch (javax.swing.text.BadLocationException e) {
	  		System.err.println(e);
      }
    }

    dlg = null;
  }


  public void showProperty() {
		ProjectPropertyDlg dlg = new ProjectPropertyDlg(mainFrm, project.getProjectName(), true);

		dlg.setCompilerModel(project.getCompilerModel());
		dlg.setInterpreterModel(project.getInterpreterModel());

		dlg.setPathModel(project.getPathModel());
		dlg.setVisible(true);
		if (dlg.isOK()) {
			if (project != null) project.setPathModel(dlg.getPathModel());
		}

    dlg = null;
  }

  public SourcePool getSourcePool(){
    return sourcePool;
  }

  public void gotoLine() {
    TextViewPanel focusedTvp = getCurrentTvp();
  	if (focusedTvp != null) focusedTvp.gotoLine(mainFrm);
  }

	public void updateEnvironment() {
    if(upperTvp != null) upperTvp.reloadEnvironment();
    if(bottomTvp != null) bottomTvp.reloadEnvironment();
	}

  public void toggleWorkSpace(){
    if(sp1.getDividerLocation() >= sp1.getMinimumDividerLocation()){
      sp1.setDividerLocation(0);
    }
    else{
      sp1.setDividerLocation(sp1.getLastDividerLocation());
    }
  }

  public boolean isWorkSpaceVisible(){                       
    return ( sp1.getDividerLocation() >= sp1.getMinimumDividerLocation() );
  }  

	/**
	 *  프로젝트 종료시 처리할 것들
	 */
	public boolean canClose(Frame owner) {

    //다이얼로그를 만든다
    ProjectClosingDlg dlg = new ProjectClosingDlg(mainFrm, "Save " + project.getProjectName());

    //마지막 save한후 open된 파일들에 대해서 검사한다
    for (int i = 0; i < openedListForSaveAll.size(); i++) {
      ProjectFileEntry pfe = (ProjectFileEntry)openedListForSaveAll.elementAt(i);

      SourceEntry se = sourcePool.getSource(pfe);
      if(se.isModified()){
        dlg.addItem(pfe);
      }
    }

    //project file을 검사한다
    if (project.isModified()) dlg.addItem(project);

    //다이얼로그를 띄운다
    dlg.setLocation(300, 300);
    if (dlg.doesContainItem()) dlg.setVisible(true);
    else return true;

    if (dlg.isOK()) {
      Vector item = dlg.getItems();
      for (int i = 0; i < item.size(); i++) {
        Object obj = item.elementAt(i);
        if (obj instanceof ProjectFileEntry) {
          ProjectFileEntry pfe = (ProjectFileEntry)obj;
          SourceEntry se = sourcePool.getSource(pfe);
          if (se != null) {
            save(se);
            pfe.syncLastModifiedTimeWithRealFile();
          }
        }
        else if (obj instanceof Project){ ProjectManager.saveProject(project); }
      }

      item = null;
      dlg.dispose();
      dlg = null;
      return true;
    }

    return false;
	}

  public void clearSourcePool(){
     sourcePool.deleteAll();
     sourcePool = null;
  }

  public Vector getOpenedProjectFileEntryListForSaveAll(){
    return openedListForSaveAll;
  }

  public void resetOpenedProjectFileEntryListForSaveAll(){
    openedListForSaveAll.removeAllElements();

    TextViewPanel curr = getCurrentTvp();
    if(curr!=null){
      SourceEntry se = curr.getSourceEntry();
      if(se!=null){
        openedListForSaveAll.addElement(project.getFileEntry(se.getFullPathName()));
      }
    }
  }

  public Vector getTotalOpenedList(){
    return openedList;
  }

  /**
   * 외부에서 파일이 변경되었는지를 체크한다
   */
  public void checkExternalFileUpdate(){
    if(this.isFilesTab()) return;

    int size = openedList.size();
		if(size == 0) return;

    Vector updatedPfes = new Vector(5, 2);
    for(int i=0; i<size; i++){
      ProjectFileEntry pfe = (ProjectFileEntry)openedList.elementAt(i);
      File f = new File(pfe.getPath(), pfe.getName());

      if(pfe.isExternallyChanged()){
        updatedPfes.addElement(pfe);
      }
    }

    if(updatedPfes.size() == 0) return;

    //사용자에게 외부 갱신 파일의 처리에 대해 물어본다
		ExternalUpdateFileDlg dlg = new ExternalUpdateFileDlg(MainFrame.mainFrame, "External File Updated... Select Files to Reload ");
    for(int i=0; i<updatedPfes.size(); i++){
      dlg.addItem((ProjectFileEntry)updatedPfes.elementAt(i));
    }

    dlg.setVisible(true);

    if(dlg.isOK()){
      Vector sel = dlg.getSelectedItems();
      for(int i=0; i<sel.size(); i++){
        ProjectFileEntry pfe = (ProjectFileEntry)sel.elementAt(i);
        SourceEntry newEntry = sourcePool.getSourceWhenExternallyUpdated(pfe);
        newEntry.setExternalUpdate(true);
        if(newEntry != null){
          mainFrm.getCodeContext().setReloadSourceEntry(newEntry);

          if(newEntry.equals(upperTvp.getSourceEntry())) {
            upperTvp.setSourceEntry(newEntry);
          }

          if(newEntry.equals(bottomTvp.getSourceEntry())) {
            bottomTvp.setSourceEntry(newEntry);
          }

          getCurrentTvp().getEditor().repaint();

          if(getCurrentTvp().getSourceEntry().equals(newEntry)) {
            mainFrm.getCodeContext().setSourceEntry(newEntry);
          }
          newEntry.getDocument().setModified(false);
        }
        newEntry.setExternalUpdate(false);
        pfe.syncLastModifiedTimeWithRealFile();
      }

      Vector unsel = dlg.getUnselectedItems();
      for(int i=0; i<unsel.size(); i++){
        ProjectFileEntry pfe = (ProjectFileEntry)unsel.elementAt(i);
        SourceEntry.syncWithBuffer(pfe.getPath(), pfe.getName());
        pfe.syncLastModifiedTimeWithRealFile();
        //sourcePool.getSource(pfe).getDocument().setModified(true);
      }
    }
    else {
      Vector unsel = dlg.getAllItems();
      for(int i=0; i<unsel.size(); i++){
        ProjectFileEntry pfe = (ProjectFileEntry)unsel.elementAt(i);
        SourceEntry.syncWithBuffer(pfe.getPath(), pfe.getName());
        pfe.syncLastModifiedTimeWithRealFile();
        //sourcePool.getSource(pfe).getDocument().setModified(true);
      }
    }
  }

  public String getCurrentSelectedPackage(){
    return projectPanel.getCurrentSelectedPackage();
  }
}

