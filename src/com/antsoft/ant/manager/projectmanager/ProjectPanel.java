/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/projectmanager/ProjectPanel.java,v 1.54 1999/08/31 12:27:11 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.54 $
 * $History: ProjectPanel.java $
 *
 * *****************  Version 55  *****************
 * User: Strife       Date: 99-06-24   Time: 8:13a
 * Updated in $/AntIDE/source/ant/manager/projectmanager
 * 프로젝트 파일 구성 조정 관련 수정
 *
 * *****************  Version 54  *****************
 * User: Remember     Date: 99-06-11   Time: 5:28p
 * Updated in $/AntIDE/source/ant/manager/projectmanager
 *
 *
 */
package com.antsoft.ant.manager.projectmanager;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import com.antsoft.ant.pool.sourcepool.*;
import com.antsoft.ant.property.*;
import com.antsoft.ant.property.defaultproperty.*;
import com.antsoft.ant.property.projectproperty.*;
import com.antsoft.ant.designer.classdesigner.*;
import com.antsoft.ant.util.EventHandlerListDlg;
import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.util.ImageList;
import com.antsoft.ant.main.MainFrame;
import com.antsoft.ant.designer.codeeditor.TextViewPanel;
import com.antsoft.ant.designer.codeeditor.OpenSourcePanel;
import com.antsoft.ant.codecontext.CodeContext;

import com.antsoft.ant.browser.filebrowser.*;
import com.antsoft.ant.util.FontList;
import com.antsoft.ant.util.LineCountDialog;
import com.antsoft.ant.compiler.Compiler;

/**
 *  class ProjectPanel
 *
 *  @author Jinwoo Baek
 *  @author kim sang kyun
 */
public class ProjectPanel extends JPanel implements ActionListener {
  private ProjectExplorer pe = null;
  private Project project = null;
  private BorderLayout borderLayout1 = new BorderLayout();
//  private JTree tree = new JTree();
  private ProjectFileEntry current = null;
  private DefaultMutableTreeNode currentNode = null;
  private DefaultMutableTreeNode lastSelectedFolder = null;
  private OpenSourcePanel osp = null;
  private FileBrowsePanel fileBrowserP = null;
  private boolean browser = false;
  //private CardLayout cardLayout = null;
  private JTabbedPane filesTabbedPane = null;
  private Font f;
  // for popup menu
  private JPopupMenu prjPopup = new JPopupMenu();
  private JPopupMenu folderPopup = new JPopupMenu();

  private JPopupMenu filePopup = new JPopupMenu();

  private JMenuItem prjAddFile = new JMenuItem("Add File...");
	private JMenuItem prjAddFolder = new JMenuItem("Add Folder...");
  private JMenuItem prjMake = new JMenuItem("Make");
  private JMenuItem prjBuild = new JMenuItem("Rebuild");
  private JMenuItem prjProperty = new JMenuItem("Properties...");

	private JMenuItem folderAddFile = new JMenuItem("Add File...");
	private JMenuItem folderAddFolder = new JMenuItem("Add Folder...");
	private JMenuItem folderRemove = new JMenuItem("Remove", ImageList.removeBtnIcon);
  private JMenuItem folderMake = new JMenuItem("Make");
	private JMenuItem folderBuild = new JMenuItem("Rebuild");
	private JMenuItem folderLineCounterRecursively = new JMenuItem("Recursively");
	private JMenuItem folderLineCounter = new JMenuItem("Only direct children");

  private JMenuItem fileMake = new JMenuItem("Make");
  private JMenuItem fileRebuild = new JMenuItem("Rebuild");
  private JMenuItem fileRemove = new JMenuItem("Remove", ImageList.removeBtnIcon);
	private JMenuItem fileLineCounter = new JMenuItem("Source analysis");
  private JMenuItem fileRun = new JMenuItem("Run", ImageList.run);


	// Icon for add or remove a file for project
	private JButton savePrjBtn = new JButton(ImageList.savePrjBtnIcon);
	private JButton closePrjBtn = new JButton(ImageList.closePrjBtnIcon);
	private JButton addFolderBtn = new JButton(ImageList.addFolderBtnIcon);
	private JButton addFileBtn = new JButton(ImageList.addFileBtnIcon);
	private JButton removeBtn = new JButton(ImageList.removeBtnIcon);
	private JButton propertyBtn = new JButton(ImageList.propertyBtnIcon);
  private JButton filebrowserBtn = new JButton(ImageList.filebrowserBtnIcon);
  private JButton filetabBtn = new JButton(ImageList.filetabBtnIcon);

  /////////////////////////////////////////////////
  // file tab, opended list 구분.
  public static final int OPENEDLIST = 100;
  public static final int DIRECTORY  = 101;

	/**
	 *  Root Node
	 */
	protected DefaultMutableTreeNode rootNode;

  /**
   *  TreeCell Renderer
   */
  ProjectTreeCellRenderer renderer = new ProjectTreeCellRenderer();

	/**
	 *  manage tree model
	 */
	private DefaultTreeModel treeModel;
  private JTree tree;

	/**
	 *  Constructor
	 */
  public ProjectPanel(ProjectExplorer pe, Project prj, OpenSourcePanel osp) {
    this.pe = pe;
    project = prj;
    this.osp = osp;
    MouseListener treeMouseEventHandler = new TreeMouseEventHandler();
    setBorder(BorderList.unselLineBorder);

    setPreferredSize(new Dimension(250, 0));
    setMaximumSize(new Dimension(2000, 0));
    setMinimumSize(new Dimension(0, 0));
    setLayout(borderLayout1);

    tree = project.getTree();
    treeModel = project.getTreeModel();


    // for tree
		tree.setModel(treeModel);
    tree.putClientProperty("JTree.lineStyle", "Angled");
		tree.setEditable(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
    tree.addMouseListener(treeMouseEventHandler);
    tree.addTreeSelectionListener(new TreeSelHandler());
    tree.setFont(FontList.treeFont);


    // 폴더 추가시 Root노드를 참조하기 때문에 현재는 없앨 수 없다.
    tree.setCellRenderer(renderer);
		tree.addTreeWillExpandListener(new ProjectPanelTreeWillExpandHandler());
		rootNode = (DefaultMutableTreeNode)treeModel.getRoot();

    tree.addKeyListener(new KeyAdapter(){
      public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
          if(prjPopup != null && prjPopup.isVisible()) prjPopup.setVisible(false);
          else if(folderPopup != null && folderPopup.isVisible()) folderPopup.setVisible(false);
          else if(filePopup != null && filePopup.isVisible()) filePopup.setVisible(false);
        }
      }
    });

    // for popup menu
    prjAddFile.addActionListener(this);
		// 폴더추가
		prjAddFolder.addActionListener(this);
    prjMake.addActionListener(this);
    prjBuild.addActionListener(this);
    prjProperty.addActionListener(this);

    // for popup menu
    folderAddFile.addActionListener(this);
		// 폴더추가
		folderAddFolder.addActionListener(this);
		folderRemove.addActionListener(this);
		folderMake.addActionListener(this);
		folderBuild.addActionListener(this);
		folderLineCounter.addActionListener(this);
		folderLineCounterRecursively.addActionListener(this);

    fileMake.addActionListener(this);
    fileRebuild.addActionListener(this);
    fileRemove.addActionListener(this);
    fileLineCounter.addActionListener(this);
    fileRun.addActionListener(this);

    //icon설정
    prjAddFile.setIcon(ImageList.addFileBtnIcon);
    prjAddFolder.setIcon(ImageList.addFolderBtnIcon);
    prjMake.setIcon(ImageList.make);
    prjBuild.setIcon(ImageList.rebuild);
    prjProperty.setIcon(ImageList.propertyBtnIcon);
    folderAddFile.setIcon(ImageList.addFileBtnIcon);
    folderAddFolder.setIcon(ImageList.addFolderBtnIcon);
//    folderRemove.setIcon(ImageList.removeBtnIcon);
    folderMake.setIcon(ImageList.make);
    folderBuild.setIcon(ImageList.rebuild);
    fileMake.setIcon(ImageList.make);
    fileRebuild.setIcon(ImageList.rebuild);
//    fileRemove.setIcon(ImageList.removeBtnIcon);
    fileLineCounter.setIcon(ImageList.linecount);



    prjPopup.add(prjAddFile);
		prjPopup.add(prjAddFolder);
    prjPopup.add(prjMake);
    prjPopup.add(prjBuild);
    prjPopup.addSeparator();
    prjPopup.add(prjProperty);

		folderPopup.add(folderAddFile);
		folderPopup.add(folderAddFolder);
		folderPopup.add(folderRemove);
    folderPopup.addSeparator();
		folderPopup.add(folderMake);
		folderPopup.add(folderBuild);

		// folder line counter
		JMenu folderLineCounterMenu = new JMenu("Source analysis");
    folderLineCounterMenu.setIcon(ImageList.linecount);
		folderLineCounterMenu.add(folderLineCounter);
		folderLineCounterMenu.add(folderLineCounterRecursively);
		folderPopup.add(folderLineCounterMenu);

    filePopup.add(fileMake);
    filePopup.add(fileRebuild);
    filePopup.add(fileRemove);
    filePopup.addSeparator();
    filePopup.add(fileLineCounter);
    filePopup.add(fileRun);

		savePrjBtn.setToolTipText("Save Project");
		savePrjBtn.setPreferredSize(new Dimension(20, 20));
		savePrjBtn.addActionListener(this);

		closePrjBtn.setToolTipText("Close Project");
		closePrjBtn.setPreferredSize(new Dimension(20, 20));
		closePrjBtn.addActionListener(this);

		addFolderBtn.setToolTipText("Add Folder");
		addFolderBtn.setPreferredSize(new Dimension(20, 20));
		addFolderBtn.addActionListener(this);

		addFileBtn.setToolTipText("Add File");
		addFileBtn.setPreferredSize(new Dimension(20, 20));
		addFileBtn.addActionListener(this);

		removeBtn.setToolTipText("Remove");
		removeBtn.setPreferredSize(new Dimension(20, 20));
		removeBtn.addActionListener(this);

		propertyBtn.setToolTipText("Project Properties...");
		propertyBtn.setPreferredSize(new Dimension(20, 20));
    propertyBtn.addActionListener(this);

    // itree
    filebrowserBtn.setToolTipText("File Browser");
    filebrowserBtn.setPreferredSize(new Dimension(20,20));
    filebrowserBtn.addActionListener(this);

    filetabBtn.setToolTipText("Opend Files");
    filetabBtn.setPreferredSize(new Dimension(20,20));
    filetabBtn.addActionListener(this);

		JPanel btnPl1 = new JPanel();
		btnPl1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		btnPl1.add(addFolderBtn);
		btnPl1.add(addFileBtn);
		btnPl1.add(removeBtn);
		btnPl1.add(propertyBtn);

		JPanel btnPl2 = new JPanel();
		btnPl2.setLayout(new FlowLayout(FlowLayout.LEFT));
		btnPl2.add(savePrjBtn);
		btnPl2.add(closePrjBtn);


		addFolderBtn.setEnabled(false);
		addFileBtn.setEnabled(false);
		removeBtn.setEnabled(false);
		propertyBtn.setEnabled(true);

		JPanel btnPl = new JPanel();
    JPanel btnPl3 = null;

    if(!pe.isFilesTab()){
  		btnPl.setLayout(new GridLayout(1, 2));
	  	btnPl.add(btnPl2);
		  btnPl.add(btnPl1);
   		JScrollPane scrollPane = new JScrollPane(tree);
	  	setLayout(new BorderLayout());
		  add(btnPl, BorderLayout.NORTH);
  		add(scrollPane, BorderLayout.CENTER);
    }
    else{
      filesTabbedPane = new JTabbedPane();

      //cardLayout = new CardLayout();
      //setLayout(cardLayout);

      JPanel filesBorderLayout = new JPanel(new BorderLayout());
      JPanel dirBorderLayout = new JPanel(new BorderLayout());

      JPanel btnPforDefaultPE = new JPanel();
      btnPforDefaultPE.setLayout(new FlowLayout(FlowLayout.LEFT));
      btnPforDefaultPE.add(addFileBtn);
      btnPforDefaultPE.add(removeBtn);

      //JPanel btnPforDefaultPE2 = new JPanel();
      //btnPforDefaultPE2.setLayout(new FlowLayout(FlowLayout.LEFT));
      //btnPforDefaultPE2.add(addFileBtn);
      //btnPforDefaultPE2.add(removeBtn);

      JPanel computerPanel = new JPanel();
      computerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
      computerPanel.add(filebrowserBtn);

      btnPl.setLayout(new BorderLayout());
      btnPl.add(btnPforDefaultPE, BorderLayout.WEST);
      btnPl.add(computerPanel, BorderLayout.EAST);

      JPanel filetabPanel = new JPanel();
      filetabPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      filetabPanel.add(filetabBtn);

      btnPl3 = new JPanel();
      btnPl3.setLayout(new BorderLayout());
      //btnPl3.add(btnPforDefaultPE2, BorderLayout.WEST);
      btnPl3.add(filetabPanel, BorderLayout.EAST);

      JPanel directoryPanel = new JPanel(new BorderLayout());
      fileBrowserP = new FileBrowsePanel(pe,project);
      directoryPanel.add(fileBrowserP);

      addFileBtn.setEnabled(true);
      addFileBtn.setIcon(ImageList.open);

      removeBtn.setEnabled(true);
      filebrowserBtn.setEnabled(true);

      JScrollPane scrollPane = new JScrollPane(tree);

		  filesBorderLayout.add(btnPl, BorderLayout.NORTH);
  		filesBorderLayout.add(scrollPane, BorderLayout.CENTER);

      //dirBorderLayout.add(btnPl3, BorderLayout.NORTH);
      dirBorderLayout.add(directoryPanel, BorderLayout.CENTER);


      MyTabbedPaneListener mtp = new MyTabbedPaneListener();

      filesTabbedPane.addTab("Directory" , ImageList.browserIcon , dirBorderLayout);
      filesTabbedPane.addTab("Opend List" , ImageList.openedIcon , filesBorderLayout);

      filesTabbedPane.addChangeListener(mtp);

      filesTabbedPane.setTabPlacement(SwingConstants.BOTTOM);
      add(filesTabbedPane,BorderLayout.CENTER);
    }
	}

  class TreeSelHandler implements TreeSelectionListener {
    public void valueChanged(TreeSelectionEvent e){
      prevPath = e.getOldLeadSelectionPath();
    }
  }

  // itree 추가
  class MyTabbedPaneListener implements ChangeListener {
    public void stateChanged(ChangeEvent e) {
      pe.closeImageSource();
      pe.closeDocument();
    }
  }
  //////////////////
  // Opendlist tabbedpane 과 directory tabbedpane의 selection
  public void setFilesTabbedPane(int view) {

    if (view == this.DIRECTORY)
      filesTabbedPane.setSelectedIndex(0);
    else
      filesTabbedPane.setSelectedIndex(1);
  }


  public void setCurrentFileClosed(){

    if(currentNode instanceof ProjectPanelTreeNode){
      ProjectPanelTreeNode node = (ProjectPanelTreeNode)currentNode;
      tree.repaint();
      osp.fileClosed((ProjectPanelTreeNode)currentNode);

      // itree
      node = osp.getNextSource();
      if(node != null) {
        notifyFileOpen(node);
        pe.openSourceHappened(this, node);
      }
      else{
        tree.setSelectionRow(-1);
        tree.repaint();
      }
    }
  }
  public void setOpenFileAllClosed() {
    tree.setSelectionRow(-1);
    tree.repaint();
    if(osp !=null) osp.closeAllOpenFile();

    prevPath = null;
    prevTvp = null;
  }

  public FileBrowsePanel getFileBrowserPanel(){
    return fileBrowserP;
  }

  /**
   * add file button이나 메뉴가 선택되었을때
   */
  private void addFileToProjectSelected(){

    TreePath tp = tree.getSelectionPath();
    if(tp == null && !pe.isFilesTab()) return;

    String srcRoot = project.getPathModel().getSourceRoot();
    if(srcRoot == null || pe.isFilesTab()) {
      pe.addToFilesTab(null, true);
      return;
    }

    StringBuffer dir = new StringBuffer();
    dir.append(srcRoot);
    if (tp != null) {
      Object obj[] = tree.getSelectionPath().getPath();
      for (int i = 1; i < obj.length; i++) {
        if (obj[i] instanceof ProjectPanelTreeNode) {
          ProjectPanelTreeNode pptn = (ProjectPanelTreeNode)obj[i];
          if (pptn.isFolder()) dir.append(File.separator + pptn.toString());
          else break;
        }
      }

      obj = null;
      pe.addToProject(dir.toString(), true);
    }
  }

  /**
   * add folder button이나 메뉴가 선택되었을때
   */
  private void fileBrowserOpen() {
  }

  private void fileTabOpen() {
    setFilesTabbedPane(this.OPENEDLIST);
  }

  private void addFolderSelected(){
    ProjectAddFolderDlg dlg = new ProjectAddFolderDlg(pe.getMainFrame(), "Add Folder...", true);
    dlg.setVisible(true);
    if (dlg.isOK()) {
      String value = dlg.getValue();
      if(value != null) addObject(value);
    }
  }

  private void removeBtnSelected(){
    TreePath tp = tree.getSelectionPath();
    if (tp == null) return;

    DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)tp.getLastPathComponent();
    if (currentNode != null) {

      if(currentNode instanceof ProjectPanelTreeNode){
        ProjectPanelTreeNode node = (ProjectPanelTreeNode)currentNode;
        if(node.isFile()) {//osp.notifyNodeRemoved(node);
          osp.fileClosed(node);
        }

        else if (node.isFolder()) {
          osp.directoryClose(node);
        }
      }
      removeCurrentNode(currentNode);
    }
  }

  private void folderAddFileSelected(){
    StringBuffer dir = new StringBuffer();
    dir.append(project.getPathModel().getSourceRoot());
    Object obj[] = tree.getSelectionPath().getPath();
    for (int i = 1; i < obj.length; i++) {
      if (obj[i] instanceof ProjectPanelTreeNode) {
        ProjectPanelTreeNode pptn = (ProjectPanelTreeNode)obj[i];
        if (pptn.isFolder()) dir.append(File.separator + pptn.toString());
        else break;
      }
    }
    pe.addToProject(dir.toString(), true);
    obj = null;
  }

  private void folderBuildSelected(){
    TreePath tp = tree.getSelectionPath();
    if(tp == null) return;

    Vector fileList = new Vector();
    DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)tp.getLastPathComponent();
    Enumeration nodes = dmtn.depthFirstEnumeration();
    while (nodes.hasMoreElements()) {
      ProjectPanelTreeNode pptn = (ProjectPanelTreeNode)nodes.nextElement();
      if (pptn.isFile()) {
      	ProjectFileEntry pfe = (ProjectFileEntry)pptn.getUseObject();
      	fileList.addElement(pfe.getFullPathName());
      }
    }
    pe.buildFiles(fileList);
  }

  private void folderMakeSelected(){
    TreePath tp = tree.getSelectionPath();
    if(tp == null) return;

    Vector fileList = new Vector();
    DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)tp.getLastPathComponent();
    Enumeration nodes = dmtn.depthFirstEnumeration();
    SourcePool sourcePool = pe.getSourcePool();

    String outputPath = pe.getProject().getPathModel().getOutputRoot();
    while (nodes.hasMoreElements()) {
      ProjectPanelTreeNode pptn = (ProjectPanelTreeNode)nodes.nextElement();
      if (pptn.isFile()) {
        ProjectFileEntry pfe = (ProjectFileEntry)pptn.getUserObject();
        SourceEntry se = sourcePool.getSource(pfe);
        if (se.isModified()) {
          pe.save(se);
          fileList.addElement(pfe.getFullPathName());
          continue;
        }

        String cFileName = MainFrame.getCodeContext().getRelativeClassFile(pfe.getFullPathName());
        if (outputPath != null)
          cFileName = outputPath + File.separator + cFileName.replace('.', File.separatorChar) + ".class";
        else cFileName = cFileName.replace('.', File.separatorChar) + ".class";
        File cFile = new File(cFileName);
 				if (cFile.exists()) {
        	if (pfe.getLastModifiedTime() <= cFile.lastModified()) continue;
        }

        fileList.addElement(pfe.getFullPathName());
      }
    }
    pe.makeFiles(fileList);
  }

  /**
   * 폴더 아래에 있는 파일들의 소스의 내용을 분석한다.
   *
   * @param recursively 디렉토리를 재귀적으로 탐색할지를 결정한다.
   */
  private void folderLineCounterSelected( boolean recursively ) {
    TreePath tp = tree.getSelectionPath();
    if (tp == null) return;

    Vector fileList = new Vector();
    DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)tp.getLastPathComponent();
    Enumeration nodes = null;
    if ( recursively == true ) {
	    nodes = dmtn.depthFirstEnumeration();
		} else {
			nodes = dmtn.children();
		}
    while (nodes.hasMoreElements()) {
      ProjectPanelTreeNode pptn = (ProjectPanelTreeNode)nodes.nextElement();
      if (pptn.isFile()) {
        ProjectFileEntry pfe = (ProjectFileEntry)pptn.getUserObject();
        fileList.addElement(pfe);
      }
    }
    LineCountDialog dlg = new LineCountDialog( fileList );
    dlg.setVisible(true);
  }
  
  private void fileRemoveSelected(){
    TreePath tp = tree.getSelectionPath();
    if (current == null || tp == null) return;

    DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)tp.getLastPathComponent();
    if (currentNode != null) removeCurrentNode(currentNode);

    if(currentNode instanceof ProjectPanelTreeNode){
      ProjectPanelTreeNode node = (ProjectPanelTreeNode)currentNode;
      if(node.isFile())  osp.fileClosed(node);
    }
  }

  private void fileLineCounter() {
    TreePath tp = tree.getSelectionPath();
    if (current == null || tp == null) return;

    DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)tp.getLastPathComponent();
    ProjectFileEntry entry = null;
    
    if (currentNode != null) 
    	entry = (ProjectFileEntry)currentNode.getUserObject();

    Vector v = new Vector();
    if ( entry != null ) {
	    v.addElement(entry);
	    LineCountDialog dlg = new LineCountDialog(v);
	    dlg.setVisible(true);
	  }
  }

  private void fileRunSelected(){
    SourceEntry se = pe.getLastOpenedSource();
    String fileName, pathName;

    // 1999.05.20 itree 추가
    // run source가 .html file인지 .java file인지 check!
    if (se != null) {
      fileName = se.getName();
      pathName = se.getPath();

      if (fileName.endsWith(".html") || fileName.endsWith(".htm"))
         pe.runApplet(pathName, fileName);
      else {
         //MainFrame.getCodeContext().notifyRun();
         pe.runClass(se.getRunnableClassName());
      }
    }
  }

  public void actionPerformed(ActionEvent evt) {
  	if (evt.getSource() == addFolderBtn) {addFolderSelected(); }
    else if (evt.getSource() == addFileBtn){ addFileToProjectSelected(); }
    else if (evt.getSource() == removeBtn){ removeBtnSelected(); }
    else if (evt.getSource() == propertyBtn) { pe.showProperty();  }
    else if (evt.getSource() == savePrjBtn) { pe.getMainFrame().saveProject(); }
    else if (evt.getSource() == closePrjBtn) { pe.getMainFrame().closeProject(); }
    else if (evt.getSource() == filebrowserBtn) { fileBrowserOpen(); }
    else if (evt.getSource() == filetabBtn) { fileTabOpen(); }
    else if (evt.getSource() == prjAddFile) { addFileToProjectSelected(); }
    else if (evt.getSource() == prjAddFolder) { addFolderSelected(); }
    else if (evt.getSource() == prjMake) { pe.makeProject(); }
    else if (evt.getSource() == prjBuild) { pe.buildProject(); }
    else if (evt.getSource() == prjProperty) { pe.showProperty(); }
    else if (evt.getSource() == folderAddFolder) { addFolderSelected(); }
    else if (evt.getSource() == folderRemove){ removeBtnSelected(); }
    else if (evt.getSource() == folderAddFile){ folderAddFileSelected(); }
    else if (evt.getSource() == folderMake){ folderMakeSelected(); }
    else if (evt.getSource() == folderBuild){ folderBuildSelected(); }
    else if (evt.getSource() == folderLineCounterRecursively) { folderLineCounterSelected(true); }
    else if (evt.getSource() == folderLineCounter) { folderLineCounterSelected(false); }
    // file에 대해서는 Make나 Rebuild나 같은 정책이다.
    else if (evt.getSource() == fileMake ){ if (current != null) pe.compileFile(current.getPath(), current.getName()); }
    else if (evt.getSource() == fileRebuild ){ if (current != null) pe.buildFile(current.getPath(), current.getName()); }
    else if (evt.getSource() == fileRemove ) { fileRemoveSelected(); }
    else if (evt.getSource() == fileLineCounter ) { fileLineCounter(); }
    else if (evt.getSource() == fileRun) { fileRunSelected(); }
  }

	/**
	 *  Remove all nodes except the root node.
	 */
	public void clear() {
		rootNode.removeAllChildren();
		treeModel.reload();
	}


  /**
   * folder node를 지운다
   */
  private void removeFolderNode(DefaultMutableTreeNode folderNode){
    int childNum = treeModel.getChildCount(folderNode);

    //먼저 자식들을 모두 삭제한다
    for(int i=childNum-1; i>=0; i--){
      DefaultMutableTreeNode child = (DefaultMutableTreeNode)treeModel.getChild(folderNode, i);

      if(child.getUserObject() instanceof ProjectFileEntry) {
         removeFileNode(child);
       }
      else  removeFolderNode(child);
    }

    //마지막으로 자신을 삭제한다
    MutableTreeNode parent = (MutableTreeNode)folderNode.getParent();
    if(parent != null) treeModel.removeNodeFromParent(folderNode);
  }

  /**
   * file node를 지운다
   */
  private void removeFileNode(DefaultMutableTreeNode fileNode){

    //먼저 project로 부터 삭제한다
    pe.removeFromProject((ProjectFileEntry)fileNode.getUserObject());

    //tree에서 삭제한다
    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)fileNode.getParent();
    if(parentNode != null) {
      DefaultMutableTreeNode childAfter = (DefaultMutableTreeNode)parentNode.getChildAfter(fileNode);
      treeModel.removeNodeFromParent(fileNode);

      if(childAfter!=null && childAfter.getUserObject() instanceof ProjectFileEntry){
         ProjectFileEntry obj = (ProjectFileEntry)childAfter.getUserObject();
         pe.openDocument(obj.getFullPathName(), true);
      }
      else{ pe.closeDocument(); }
      addFolderBtn.setEnabled(false);
      addFileBtn.setEnabled(true);
      removeBtn.setEnabled(true);
    }
  }

	/**
	 *  Remove the currently selected node. 완전히 지운다 
	 */
	public void removeCurrentNode(DefaultMutableTreeNode currentNode) {
		if (currentNode == null) return;

    //file node
    if(currentNode.getUserObject() instanceof ProjectFileEntry) {
      removeFileNode(currentNode);

    }
    else removeFolderNode(currentNode);
  }

	/**
	 *  Remove the specified file node. 단순히 tree에서 지워준다 
	 *
	 *  @param pfe entry to remove
	 */
	public void removeNode(ProjectFileEntry pfe) {

		for (int i = 0; i < tree.getRowCount(); i++) {
   		TreePath tp = tree.getPathForRow(i);
			DefaultMutableTreeNode tn = (DefaultMutableTreeNode)tp.getLastPathComponent();
			Object obj = tn.getUserObject();
			if (obj instanceof ProjectFileEntry) {
				ProjectFileEntry pf = (ProjectFileEntry)obj;
				if (pf.equals(pfe)) {
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode)tn.getParent();
					if (parent != null) {
						DefaultMutableTreeNode next = (DefaultMutableTreeNode)parent.getChildAfter(tn);
						if (next != null) tree.setSelectionRow(i);
						else if (i > 0) tree.setSelectionRow(i - 1);

						treeModel.removeNodeFromParent(tn);
					}
					break;
				}
			}
		}
	}

	/**
	 *  Add child to the currently selected node.
	 *
	 *  @param child object to add to tree
	 *  @return DefalutMutableTreeNode added tree node
	 */
	public DefaultMutableTreeNode addObject(Object child) {
		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = tree.getSelectionPath();

		if (parentPath == null) {
			parentNode = rootNode;
		} else {
			parentNode = (DefaultMutableTreeNode)(parentPath.getLastPathComponent());
			if (parentNode instanceof ProjectPanelTreeNode) {
				ProjectPanelTreeNode node = (ProjectPanelTreeNode)parentNode;
				if (node.isFile()) parentNode = (DefaultMutableTreeNode)node.getParent();
			}
		}

		return addObject(parentNode, child, true);
	}

	/**
	 *  Add child to the parent node
	 *
	 *  @param parent parent node
	 *  @param child object to add to tree
	 *  @return DefalutMutableTreeNode added tree node
	 */
	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child) {
		return addObject(parent, child, true);
	}

	/**
	 *  Add child to the parent node with visibility
	 *
	 *  @param parent parent node
	 *  @param child object to add to tree
	 *  @param shouldBeVisible whether let node visible or not
	 *  @return DefaultMutableTreeNode added tree node
	 */
	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
				Object child, boolean shouldBeVisible) {
		ProjectPanelTreeNode childNode = new ProjectPanelTreeNode(child);
    project.setLastID(project.getLastID() + 1);
    childNode.setID(String.valueOf(project.getLastID()));

		if (parent == null) {
			parent = rootNode;
		}

		// 노드의 제 위치(Sorting이 되게)를 찾아서 넣어야한다. -_-;;
		int index = 0;
		for (int i = 0; i < parent.getChildCount(); i++) {
			ProjectPanelTreeNode iter = (ProjectPanelTreeNode)parent.getChildAt(i);
			// 폴더 먼저 배열하고 파일을 배열하자
			if (childNode.isFolder()) {
				if (iter.isFolder()) {
        	int comp = childNode.toString().toUpperCase().compareTo(iter.toString().toUpperCase());
					if (comp > 0) index++;
          else if ((comp == 0) && (childNode.toString().compareTo(iter.toString()) > 0)) index++;
					else break;
				} else break;
			}
			// 파일을 배열하자
			else if (childNode.isFile()) {
				if (iter.isFile()) {
        	int comp = childNode.toString().toUpperCase().compareTo(iter.toString().toUpperCase());
					if (comp > 0) index++;
          else if ((comp == 0) && (childNode.toString().compareTo(iter.toString()) > 0)) index++;
					else break;
				} else index++;
			}
		}

    if (childNode.isFolder()) {
	    if (parent instanceof ProjectPanelTreeNode)
	    	childNode.setParentID(((ProjectPanelTreeNode)parent).getID());
    }
		treeModel.insertNodeInto(childNode, parent, index);

		// Make sure the user can see the lovely new node.
		if (shouldBeVisible) {
			tree.expandPath(new TreePath(parent.getPath()));
			tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		}
		return childNode;
	}

  /**
   * default project일겨우에 사용된다
   */
  public void addFileToDefaultProject(ProjectFileEntry pfe) {
    if (pfe != null) {
      currentNode = addObject((DefaultMutableTreeNode)treeModel.getRoot(), pfe);
		}
  }

	/**
	 *  프로젝트에 파일을 추가하면서 프로젝트 트리에도 추가한다.
	 */
  public DefaultMutableTreeNode addFileToProject(ProjectFileEntry pfe) {
		// 원래 있던 파일들과 비교를 해야한다.
    if (pfe == null) return null;

    TreePath treepath = null;
    TreePath tpFound = null;
    boolean found = false;
    Object[] objs = pathToTreePath(pfe.getPath());
    if (objs != null) {
      treepath = new TreePath(objs);
    }
    objs = null;

    for (int i = 0; i < tree.getRowCount(); i++) {
      TreePath tp = tree.getPathForRow(i);
      if ((treepath != null) && tp.equals(treepath)) {
        tpFound = tp;
      }

      DefaultMutableTreeNode tn = (DefaultMutableTreeNode)tp.getLastPathComponent();
      Object obj = tn.getUserObject();
      if (obj instanceof ProjectFileEntry) {
        ProjectFileEntry pf = (ProjectFileEntry)obj;
        if (pf.equals(pfe)) {
          found = true;
          break;
        }
      }
    }

    if (!found) {
      if (tpFound != null) {
        return addObject((DefaultMutableTreeNode)tpFound.getLastPathComponent(), pfe);
      }
      else {
        DefaultMutableTreeNode parent = addPath(treepath);
        if (parent != null)	{
          return addObject(parent, pfe);
        }
        else {
          return addObject(pfe);
        }
      }
    }
    else return null;
  }

  public Hashtable getFilesAndDirsInSelectedTreePath(){
    Hashtable ret = new Hashtable(10, 0.9F);
    TreePath tp = tree.getSelectionPath();

    DefaultMutableTreeNode node = (DefaultMutableTreeNode)tp.getLastPathComponent();
    if(!node.isRoot() && node instanceof ProjectPanelTreeNode ){
      ProjectPanelTreeNode pnode = (ProjectPanelTreeNode)node;
      if(pnode.isFile()) node = (DefaultMutableTreeNode)node.getParent();
    }

    for(int i=0; i<node.getChildCount(); i++){
      ret.put(((DefaultMutableTreeNode)node.getChildAt(i)).toString(), "");
    }

    return ret;
  }

  /**
   *  트리에 path를 추가한다.
   */
  public DefaultMutableTreeNode addPath(TreePath tp) {
  	if (tp == null) return null;

    DefaultMutableTreeNode parent = rootNode;
    for (int i = 1; i < tp.getPathCount(); i++) {
      parent = addObject(parent, ((ProjectPanelTreeNode)tp.getPathComponent(i)).getUserObject());
    }
    return parent;
  }

	/**
	 * 프로젝트 트리에 폴더를 추가
	 */
	public DefaultMutableTreeNode addFolder( DefaultMutableTreeNode parent, String folderName ) {
  	// 폴더가 있는지 보고
    if (folderName == null) return null;
    boolean found = false;

    if (parent != null) {
    	// 선택된 것이 file인지를 체크 한다. 파일이면, 상위 디렉토리를 가져 온다.
    	if (parent != rootNode) {
        if (((ProjectPanelTreeNode)parent).isFile()) 
        	parent = (DefaultMutableTreeNode)parent.getParent();
			}

      if (parent != rootNode) {
        ProjectPanelTreeNode pptn = (ProjectPanelTreeNode)parent;

        int i = 0;
        ProjectPanelTreeNode nd = null;
        for (i = 0; i < pptn.getChildCount(); i++) {
          nd = (ProjectPanelTreeNode)pptn.getChildAt(i);
          if (nd.isFolder() && nd.toString().equals(folderName)) {
            found = true;
            break;
          }
        }
        if (!found) {
          lastSelectedFolder = addObject(pptn, folderName);
        }
        else lastSelectedFolder = (DefaultMutableTreeNode)nd;
      }
      else {
        int i = 0;
        ProjectPanelTreeNode nd = null;
        for (i = 0; i < currentNode.getChildCount(); i++) {
          nd = (ProjectPanelTreeNode)currentNode.getChildAt(i);
          if (nd.isFolder() && nd.toString().equals(folderName)) {
            found = true;
            break;
          }
        }
        if (!found) lastSelectedFolder = addObject(folderName);
        else lastSelectedFolder = (DefaultMutableTreeNode)nd;
      }
    }

    return lastSelectedFolder;
	}	 
	
  /**
   *  프로젝트 트리에 폴더를 추가
   */
  public DefaultMutableTreeNode addFolder(String folderName) {
  	return addFolder((ProjectPanelTreeNode)currentNode, folderName);
  }

  /**
   *  가장 최근에 추가한 폴더에 추가하기
   */
  public void addFileToProjectWithLastAddedFolder(ProjectFileEntry pfe) {
  	// 원래 있던 파일들과 비교를 해야한다.
    if (pfe == null) return;
    boolean found = false;
    for (int i = 0; i < tree.getRowCount(); i++) {
      TreePath tp = tree.getPathForRow(i);
      DefaultMutableTreeNode tn = (DefaultMutableTreeNode)tp.getLastPathComponent();
      Object obj = tn.getUserObject();
      if (obj instanceof ProjectFileEntry) {
        ProjectFileEntry pf = (ProjectFileEntry)obj;
        if (pf.equals(pfe)) {
          found = true;
          break;
        }
      }
    }

    if (!found && (lastSelectedFolder != null)) addObject(lastSelectedFolder, pfe, true);
  }

  /**
   * compile error message 발생시 사용된다
   */
  public void setSelectedFileWithExpand(String pathName, String fileName){
  	DefaultMutableTreeNode node = null;
    if (pathName != null) {
	    File f = new File(pathName, fileName);
  	  node = getTreeNode(rootNode, f.getAbsolutePath());
    }
    else node = getTreeNode(rootNode, fileName);

    if(node != null){
      TreeNode [] nodes = node.getPath();
      TreePath tp = new TreePath(nodes);
      tree.setSelectionPath(tp);  
      fileNodeSelected((ProjectPanelTreeNode)node);
      currentNode = node;
    }
  }

  /**
   * node의 자식들 중에 UserObject가 pfe인것을 찾아 return
   */
  private DefaultMutableTreeNode getTreeNode(DefaultMutableTreeNode node, String fullPath){
    int count = node.getChildCount();

    for(int i=0; i<count; i++){
      DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getChildAt(i);
      if(child instanceof ProjectPanelTreeNode){
        ProjectPanelTreeNode pptn = (ProjectPanelTreeNode)child;
        Object userObj = child.getUserObject();
        if(userObj instanceof ProjectFileEntry){

          ProjectFileEntry entry = (ProjectFileEntry)userObj;
          if(entry.getFullPathName().equals(fullPath)) {
            return child;
          }
          else{
            if(!child.isLeaf()){
              DefaultMutableTreeNode ret = getTreeNode(child, fullPath);
              if(ret != null) return ret;
            }
          }
        }
        else{
          if(!child.isLeaf()){
            DefaultMutableTreeNode ret = getTreeNode(child, fullPath);
            if(ret != null) return ret;
          }
        }
      }
      else{
        if(!child.isLeaf()){
          DefaultMutableTreeNode ret = getTreeNode(child, fullPath);
          if(ret != null) return ret;
        }
      }
    }

    return null;
  }

	/**
	 *  프로젝트 트리에서 해당 노드를 선택되도록 한다.
	 */
  public void setSelectionFile(String pathName, String fileName) {

//  	ProjectFileEntry pfe = new ProjectFileEntry(pathName, fileName);
    String key = "";
    if(pathName != null){
      File f = new File(pathName, fileName);
      key = f.getAbsolutePath();
    }
    else key = fileName;

    for (int i = 0; i < tree.getRowCount(); i++) {
      TreePath tp = tree.getPathForRow(i);
      DefaultMutableTreeNode tn = (DefaultMutableTreeNode)tp.getLastPathComponent();
      Object obj = tn.getUserObject();
      if (obj instanceof ProjectFileEntry) {
        ProjectFileEntry pf = (ProjectFileEntry)obj;
        if (pf.getFullPathName().equals(key)) {
          tree.setSelectionPath(tp);
          if (tn instanceof ProjectPanelTreeNode)
            pe.openSourceHappened(this, (ProjectPanelTreeNode)tn);
          break;
        }
      }
    }
	}

  /**
   *  파일 경로로 되어있는 문자열을 분석하여 트리 경로로 구성한다.
   */
  public Object[] pathToTreePath(String path) {

  	String sourcePath = project.getPathModel().getSourceRoot();
    Vector items = new Vector(5, 2);
    if ( sourcePath != null && path.startsWith(sourcePath)) {
    	int idx = sourcePath.length();
      if (!sourcePath.endsWith(File.separator)) {
        idx++;
        if(sourcePath.equals(path)){
          Object [] objs = new Object[1];
          objs[0] = rootNode;
          return objs;
        }
      }    

    	String str = path.substring(idx);
      StringTokenizer token = new StringTokenizer(str);
      while(token.hasMoreTokens()) {
      	String item = token.nextToken(File.separator);
        if ((item != null) && !item.trim().equals("")) {
        	ProjectPanelTreeNode pptn = new ProjectPanelTreeNode(item);
          if (pptn != null) items.addElement(pptn);
          else break;
        }
        else break;
      }
      if (items.size() > 0) {
      	Object[] objs = new Object[items.size() + 1];
        objs[0] = rootNode;
        for (int i = 1; i < items.size() + 1; i++)
        	objs[i] = (ProjectPanelTreeNode)items.elementAt(i - 1);
        return objs;
      }
      else return null;
    }
    else return null;
  }

  public void setSelLineBorder(){
    setBorder(BorderList.selLineBorder);
  }

  public void clearBorder(){
    setBorder(BorderList.unselLineBorder);
  }

  /**
   * 아래 있는 TreeMouseEventHandler에서 호출된다
   * files tab일 경우는 opensource panel를 동작시키지 않는다 
   */
  public void fileNodeSelected(ProjectPanelTreeNode pptn){
    if(notifyFileOpen(pptn) && !pe.isFilesTab()) pe.openSourceHappened(this, pptn);
  }

  // itree 추가
  public DefaultMutableTreeNode getCurrentNode() {
    return currentNode;
  }

  public void setCurrentNode(DefaultMutableTreeNode node){
    currentNode = node;
  }
      
  public DefaultMutableTreeNode getRootNode() {
    return rootNode;
  }

  public boolean notifyFileOpen(ProjectPanelTreeNode pptn){
    boolean ret = false;

    currentNode = pptn;
    current = (ProjectFileEntry)currentNode.getUserObject();
    if (current != null) {

      //tree에서 선택된 효과
      TreeNode [] nodes = currentNode.getPath();
      TreePath tp = new TreePath(nodes);
      tree.setSelectionPath(tp);

      //실제 오픈
      ret = pe.openDocument(current, true);
    }
    else ret = false;

    removeBtn.setEnabled(true);
    if(!pe.isFilesTab()){
      addFolderBtn.setEnabled(false);
      addFileBtn.setEnabled(true);
    }

    return ret;
  }

  public String getCurrentSelectedPackage(){
    TreePath path = tree.getSelectionPath();

    if(path == null) return null;

    int size = path.getPathCount();

    DefaultMutableTreeNode last = (DefaultMutableTreeNode)path.getLastPathComponent();
    if (last instanceof ProjectPanelTreeNode) {
       ProjectPanelTreeNode ppt = (ProjectPanelTreeNode)last;
       if(ppt.isFile()){
         --size;
       }
    }

    String packageName = "";

    for(int i=1; i<size; i++){
      String pathStr = path.getPathComponent(i).toString();
      packageName += pathStr + ".";
    }

    if(!packageName.equals(""))
    packageName = packageName.substring(0, packageName.length()-1);

    return packageName;
  }

  TreePath prevPath = null;
  String prevTvp = null;

	/**
	 *  Inner class TreeSelectionListener from mouse
	 *
	 *  트리에서 노드를 선택했을 때 처리한다.
   *  TreeSelection event로 처리하면 심각한 Side Effect가 발생하기 때문에
	 *  마우스 이벤트로 처리한다
   *
	 *  @author Jinwoo Baek
   *  @author kim sang kyun
	 */
  class TreeMouseEventHandler extends MouseAdapter {
    public void mousePressed(MouseEvent evt){
      // 우선은 여기에다가 무조건 TextViewPanel을 볼 수 있도록 cardlayout을 변경한다.
      // 추후에 Project panel에도 image가 등록이 될수 있는 경우
      // 적당히 변경해주도록 한다. by itree

      if (evt.getModifiers() == InputEvent.BUTTON3_MASK) {
        tree.setSelectionPath(tree.getPathForLocation(evt.getX(), evt.getY()));
      }

      pe.showTextPanel();
      pe.setFocusedComponent(ProjectExplorer.PROJECT_PANEL);

      TreePath path = tree.getSelectionPath();

      String currTvp = TextViewPanel.focusedPosition;
      if (path == null) return;

      if((prevPath != null) && (prevTvp != null) &&
          prevPath.equals(path) && prevTvp.equals(currTvp) ) return;


      prevPath = path;
      prevTvp = currTvp;

      DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)path.getLastPathComponent();
      if (dmtn instanceof ProjectPanelTreeNode) {

        ProjectPanelTreeNode pptn = (ProjectPanelTreeNode)dmtn;
        if (pptn.isFile()) { // 파일일 경우
          fileNodeSelected(pptn);
          currentNode = dmtn;
        }
        else { // 폴더일 경우
          removeBtn.setEnabled(true);
          if(!pe.isFilesTab()){
            addFolderBtn.setEnabled(true);
            addFileBtn.setEnabled(true);
          }
          currentNode = dmtn;
          pe.closeDocument();
          osp.unPressedButton();
          current = null;
        }
      } // root node
      else {
        removeBtn.setEnabled(false);
        if(!pe.isFilesTab()){
          addFolderBtn.setEnabled(true);
          addFileBtn.setEnabled(true);
        }
        currentNode = dmtn;
        pe.closeDocument();        
      }
    }

    public void mouseReleased(MouseEvent evt) {

      if (evt.getModifiers() == InputEvent.BUTTON3_MASK) {
        tree.setSelectionPath(tree.getPathForLocation(evt.getX(), evt.getY()));
        TreePath path = tree.getSelectionPath();

        if (path != null) {
          DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)path.getLastPathComponent();
          if (dmtn instanceof ProjectPanelTreeNode) {
            ProjectPanelTreeNode pptn = (ProjectPanelTreeNode)dmtn;
            // 폴더일때
            if (pptn.isFolder()) {
              Point p = tree.getLocationOnScreen();
              if(!pe.isFilesTab()) {

                if(Compiler.isCompiling) {
                  folderMake.setEnabled(false);
                  folderBuild.setEnabled(false);
                }
                else{
                  folderMake.setEnabled(true);
                  folderBuild.setEnabled(true);

                  folderPopup.show(tree, evt.getX(), evt.getY());
                }
              }
            }
            // 파일일때
            else if (pptn.isFile()) {

              Point p = tree.getLocationOnScreen();
              if(!pe.isFilesTab()) {

                if(Compiler.isCompiling) {
                  fileMake.setEnabled(false);
                  fileRebuild.setEnabled(false);
                }
                else{
                  fileMake.setEnabled(true);
                  fileRebuild.setEnabled(true);
                }

                SourceEntry se = pe.getLastOpenedSource();
                if ( !Compiler.isCompiling && se != null && se.getRunnableClassName()!=null ||
                     dmtn.getUserObject().toString().toLowerCase().endsWith(".html") ||
                     dmtn.getUserObject().toString().toLowerCase().endsWith(".htm") ) {
                  fileRun.setEnabled(true);
                  filePopup.show(tree, evt.getX(), evt.getY());
                } else {
                	fileRun.setEnabled(false);
                  filePopup.show(tree, evt.getX(), evt.getY());
                }
              }
            }
          }
          // Root Node일때
          else {
            if(Compiler.isCompiling) {
              prjMake.setEnabled(false);
              prjBuild.setEnabled(false);
            }
            else{
              prjMake.setEnabled(true);
              prjBuild.setEnabled(true);
            }

            Point p = tree.getLocationOnScreen();
            if(!pe.isFilesTab()) prjPopup.show(tree, evt.getX(), evt.getY());
          }
        }
      }
    }
  }

	/**
	 *  Inner class ProjectPanelTreeWillExpandHandler
	 *
	 *  프로젝트 트리에서 expand를 처리하기 위함.
	 *
	 *  @author Jinwoo Baek
	 */
	class ProjectPanelTreeWillExpandHandler implements TreeWillExpandListener {
		public void treeWillExpand(TreeExpansionEvent event) {
		}

		public void treeWillCollapse(TreeExpansionEvent event) {
			TreePath tp = event.getPath();
			if (tp.isDescendant(tree.getSelectionPath())) tree.setSelectionPath(tp);
		}
	}
}

