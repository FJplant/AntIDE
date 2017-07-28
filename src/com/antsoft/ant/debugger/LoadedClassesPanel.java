/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/LoadedClassesPanel.java,v 1.18 1999/09/01 02:01:21 itree Exp $
 * $Revision: 1.18 $
 * $History: LoadedClassesPanel.java $
 * 
 * *****************  Version 6  *****************
 * User: Bezant       Date: 99-06-18   Time: 1:56a
 * Updated in $/AntIDE/source/ant/debugger
 * 실행 프로그램이 종료되었을때 reset시켜주는
 * 루틴을 넣었습니다.
 * 
 * *****************  Version 5  *****************
 * User: Bezant       Date: 99-06-16   Time: 12:18a
 * Updated in $/AntIDE/source/ant/debugger
 * 별루 못했음. -_-;
 * 
 * *****************  Version 4  *****************
 * User: Bezant       Date: 99-06-11   Time: 8:37p
 * Updated in $/AntIDE/source/ant/debugger
 * Breakpoint Manage에 대한 성능개선이 좀 있었습니다.
 * 흠..
 *
 * *****************  Version 3  *****************
 * User: Multipia     Date: 99-06-02   Time: 10:46p
 * Updated in $/AntIDE/source/ant/debugger
 *
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:42p
 * Updated in $/AntIDE/source/ant/debugger
 *
 * *****************  Version 1  *****************
 * User: Multipia     Date: 99-05-11   Time: 6:48p
 * Created in $/AntIDE/source/ant/debugger
 * Initial Version.
 */

package com.antsoft.ant.debugger;

import java.awt.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import java.awt.event.*;

import sun.tools.debug.*;

/**
 * Remote Java Virtual Machine 상에 load된 class들의 리스트를 보여 주는 패널
 */
public class LoadedClassesPanel extends JPanel implements ActionListener, Runnable{
  private DebuggerProxy debuggerProxy;
  private MethodPanel methodPanel;
  private SourcePanel sourcePanel;
  private WatchPanel watchPanel;
  private JList classList = new JList();
  private JTree classTree = null;
  private LoadedClassesTreeCellRenderer classTreeRenderer = null;
  private DefaultListModel classesListModel = new DefaultListModel();
  private LoadedClassesListCellRenderer listRenderer = new LoadedClassesListCellRenderer();
  //private LoadedClassesTreeModel classesTreeModel = null;
  private DefaultTreeModel treeModel = null;
  private DefaultMutableTreeNode rootnode = null;
	private DefaultMutableTreeNode jdknode = null;
	private DefaultMutableTreeNode sunnode = null;
	private DefaultMutableTreeNode usernode = null;
  private JScrollPane classScrollPane = null;
//  private JLabel status = new JLabel("Loaded Classes: 0, Loaded Interfaces: 0");
  private JLabel statusInterface = new JLabel(" Interfaces : 0");
  private JLabel statusClasses = new JLabel(" Classes : 0");
  private int loadedClasses, loadedInterfaces;
  private String currentClassId = null;

  private Vector loadedClassVec = new Vector();
  private Hashtable loadedClassHash = new Hashtable();

  private static int test = 0;

  BorderLayout borderLayout1 = new BorderLayout();

  JButton showListButton, showTreeButton;
  String listActionCommand = "Show List Type";
  String treeActionCommand = "Show Tree Type";

  static Icon listIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/list.gif"));
  static Icon treeIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/tree.gif"));

  //처음 보여주는 형식은 Tree 형태입니다.
  boolean isList = false;

  private AntDebuggerMainFrame mainFrame;
  private RemoteDebugger remoteDebugger;

  public LoadedClassesPanel( DebuggerProxy debugger, SourcePanel sourcePanel, MethodPanel methodPanel, WatchPanel watchPanel ) {
    debuggerProxy = debugger;
    this.sourcePanel = sourcePanel;
    this.methodPanel = methodPanel;
    this.watchPanel = watchPanel;
    //classesTreeModel = new LoadedClassesTreeModel();

    // itree
    UIManager.put("Tree.expandedIcon",
              new ImageIcon(LoadedClassesPanel.class.getResource("image/minus.gif")));
    UIManager.put("Tree.collapsedIcon",
              new ImageIcon(LoadedClassesPanel.class.getResource("image/plus.gif")));
    try  {
//      this.remoteDebugger = remoteDebugger;
//      this.mainFrame = frame;
      uiInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  public final void reset(DebuggerProxy debuggerProxy, SourcePanel sourcePanel, MethodPanel methodPanel, WatchPanel watchPanel){
    this.debuggerProxy = debuggerProxy;
    this.sourcePanel = sourcePanel;
    this.methodPanel = methodPanel;
    this.watchPanel = watchPanel;
    this.currentClassId = null;

    showListButton.setEnabled(true);
    showTreeButton.setEnabled(true);
    classesListModel.removeAllElements();
    System.out.println("LoadedClasses Reset");

    if(isList) {
    }
    else {
      jdknode.removeAllChildren();
      sunnode.removeAllChildren();
      usernode.removeAllChildren();

      treeModel.reload(rootnode);
      loadedClassHash.clear();

    }
//    try {
//      update();
//    } catch(Exception e) {
//      System.out.println("LoadedClassesPanel : UI update Exception");
//    }
  }

  void uiInit() throws Exception {
    this.setLayout(borderLayout1);

    JPanel upPane = new JPanel();
    upPane.setLayout(new BorderLayout(0,0));
    JPanel upleftPane = new JPanel();

    //showListButton = new JButton("List");
    showListButton = new JButton(listIcon);
    showListButton.setToolTipText("List의 형태로 보여줍니다.");
    showListButton.setMargin(new Insets(0, 0, 0, 0));
    //showListButton.setMaximumSize(new Dimension(listIcon.getIconWidth() ,listIcon.getIconHeight()));

    showListButton.setActionCommand(listActionCommand);
    showListButton.addActionListener(this);
    showListButton.setEnabled(false);

	  //showTreeButton = new JButton("Tree");
  	showTreeButton = new JButton(treeIcon);
	  showTreeButton.setToolTipText("Tree의 형태로 보여줍니다.");
  	showTreeButton.setMargin(new Insets(0, 0, 0, 0));
    showTreeButton.setActionCommand(treeActionCommand);
    showTreeButton.addActionListener(this);
    showTreeButton.setEnabled(false);

    upPane.add(new JLabel("  Loaded Classes"), BorderLayout.WEST);
    upleftPane.add(showListButton);
    upleftPane.add(showTreeButton);
   	upPane.add(upleftPane, BorderLayout.EAST);

    //add(new JLabel("Loaded Classes:"), BorderLayout.NORTH);
    classList.setModel( classesListModel );
    classList.setCellRenderer(listRenderer); 
    //classTree.setModel( classesTreeModel );
    add(upPane, BorderLayout.NORTH);
    classScrollPane = new JScrollPane();

    //////////////////////////////////////////////////////////////////
    //// Tree UI 설계
    // TreeModel 생성
    LoadedClassesEntry rootentry = new LoadedClassesEntry("Loaded Classes", null,LoadedClassesEntry.ROOT);
    rootnode = new DefaultMutableTreeNode(rootentry);
    treeModel = new DefaultTreeModel(rootnode);
    treeModel.addTreeModelListener(new MyTreeModelListener());
    // Tree Cell Renderer 생성
    classTreeRenderer = new LoadedClassesTreeCellRenderer();
    // Tree 생성
    classTree = new JTree(treeModel);
    classTree.setEditable(false);
    classTree.setDoubleBuffered(true);
    classTree.setShowsRootHandles(true);
    classTree.setRootVisible(false);
    classTree.putClientProperty("JTree.lineStyle", "Angled");
    classTree.setCellRenderer(classTreeRenderer);

    LoadedClassesEntry jdkEntry = new LoadedClassesEntry("JAVA",null, LoadedClassesEntry.ROOTJAVA);
    LoadedClassesEntry sunEntry = new LoadedClassesEntry("SUN",null, LoadedClassesEntry.ROOTSUN);
    LoadedClassesEntry userEntry = new LoadedClassesEntry("USER",null, LoadedClassesEntry.ROOTUSER);
    jdknode = addObject(rootnode,jdkEntry,true);
    sunnode = addObject(rootnode,sunEntry,true);
    usernode = addObject(rootnode,userEntry,true);

    setPreferredSize( new Dimension( 200, 150 ) );

		if ( isList )
			classScrollPane.setViewportView(classList);
		else
		  classScrollPane.setViewportView(classTree);

    /// status bar 정보..고치기
    JPanel statusPl = new JPanel(new GridLayout(1,2,0,0));
    Font f = new Font(showListButton.getFont().getName(), Font.PLAIN,13);

    statusClasses.setFont(f);
    statusClasses.setForeground(Color.black);
    statusClasses.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.white, Color.gray));

    statusInterface.setFont(f);
    statusInterface.setForeground(Color.black);
    statusInterface.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.white, Color.gray));
    statusPl.add(statusInterface);
    statusPl.add(statusClasses);

    add(classScrollPane, BorderLayout.CENTER);
    add(statusPl, BorderLayout.SOUTH);

    MouseListener mouseListener = new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        //System.out.println("clicked..");
        Component comp = classScrollPane.getViewport().getView();

        String classId = null;
        int index = classList.locationToIndex(e.getPoint());
        if( index != -1 ){
          classList.setSelectedIndex(index);
          LoadedClassesEntry entry = (LoadedClassesEntry)classList.getSelectedValue(); 
          classId = entry.getName();

          if ( classId != null ) {
            methodPanel.setClass(classId);
            sourcePanel.setClass(classId);
            watchPanel.setCurrentClassId(classId);

            currentClassId = classId;
          }
        }
      }
    };
    classList.addMouseListener(mouseListener);

    classTree.addTreeSelectionListener( new TreeSelectionListener() {
  		public void valueChanged( TreeSelectionEvent e ) {
	  		//TO DO
        TreePath path = e.getPath();
        if ( path.getPathCount() < 4 ) {
          sourcePanel.showEmptyDocument();
          return;
        }

				DefaultMutableTreeNode entryNode = (DefaultMutableTreeNode)path.getLastPathComponent();
				LoadedClassesEntry entry = (LoadedClassesEntry)entryNode.getUserObject();
        String classId = entry.getFullName();

        if ( classId != null ) {
          methodPanel.setClass(classId);
          sourcePanel.setClass(classId);
          watchPanel.setCurrentClassId(classId);
          currentClassId = classId;
        }
		  }
  	});

    try {
      update();
    } catch ( Exception e ) {
    }
  }

  ///////////////////////////////////////////////////////////////////
  // Tree 관련 함수.
  public void removeCurrentNode() {

    TreePath currentSelection = classTree.getSelectionPath();
    if (currentSelection != null) {
      DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                       (currentSelection.getLastPathComponent());
      MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
      if (parent != null) {
        treeModel.removeNodeFromParent(currentNode);
        return;
      }
    }
       // Either there was no selection, or the root was selected.
    //toolkit.beep();
  }
   /** Add child to the currently selected node. */
  public DefaultMutableTreeNode addObject(Object child) {
    DefaultMutableTreeNode parentNode = null;
    TreePath parentPath = classTree.getSelectionPath();
    if (parentPath == null) {
      parentNode = rootnode;
    } else {
      parentNode = (DefaultMutableTreeNode)
                     (parentPath.getLastPathComponent());
    }
    return addObject(parentNode, child, true);
  }

  public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                          Object child) {
    return addObject(parent, child, false);
  }
  public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                     Object child, boolean shouldBeVisible) {
    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
    if (parent == null) {
      parent = rootnode;
    }
    treeModel.insertNodeInto(childNode, parent,
                             parent.getChildCount());
     // Make sure the user can see the lovely new node.
    if (shouldBeVisible) {
      classTree.scrollPathToVisible(new TreePath(childNode.getPath()));
    }
    return childNode;
  }
  // Tree 관련 함수 끝.
  //////////////////////////////////////////////////////////////
  public void addClass(String cls) {
  	//TODO: 1. package name과 class name을 추출.
		//      2. package name을 가지는 MutableTreeNode reference를 가져옴.
		//      3. 가져온 MutableTreeNode reference를 parent로 하여, reference의
		//        마지막 인덱스에 Class Name을 추가
		String packageName = null;
		String className = "";
		StringTokenizer t = new StringTokenizer(cls, ".");
		className = t.nextToken();
		
		while ( t.hasMoreTokens() ) {
			if ( packageName == null )
				packageName = className;
			else
			  packageName = packageName + "." + className;

			className = t.nextToken();
		}
		DefaultMutableTreeNode currentNode = null;
		int entryType, entryTypeDir;
		if (packageName.startsWith("java.") || packageName.startsWith("javax.")) {
			currentNode = jdknode;
			entryType = LoadedClassesEntry.JDK;
			entryTypeDir = LoadedClassesEntry.JDKPACKAGE;
		}
		else if(packageName.startsWith("sun.")) {
			currentNode = sunnode;
			entryType = LoadedClassesEntry.SUN;
			entryTypeDir = LoadedClassesEntry.SUNPACKAGE;
		}
		else {
			currentNode = usernode;
			entryType = LoadedClassesEntry.USER;
			entryTypeDir = LoadedClassesEntry.USERPACKAGE;
		}

		int childCount = currentNode.getChildCount();
		boolean packageFound = false;
		for ( int i = 0; i < childCount && packageFound != true; i++ ) {
			DefaultMutableTreeNode packageNode = (DefaultMutableTreeNode)currentNode.getChildAt(i);
			if ( ((LoadedClassesEntry)packageNode.getUserObject()).toString().equals(packageName) ) {
			  // check the class is already loaded.
			  boolean classFound = false;
			  for ( int j = 0; j < packageNode.getChildCount(); j++ ) {
			    DefaultMutableTreeNode classNode = (DefaultMutableTreeNode)packageNode.getChildAt(j);
			  	if ( ((LoadedClassesEntry)classNode.getUserObject()).toString().equals(className) )
			  		classFound = true;
			  }
			  if ( !classFound ) {
			    //insertNodeInto( new DefaultMutableTreeNode( className, false ),
          //              packageNode, packageNode.getChildCount());
          LoadedClassesEntry entry = new LoadedClassesEntry(className,packageName + "." + className,
          									entryType);
          addObject(packageNode,entry,false);
        }
				packageFound = true;
			}
		}

		if ( packageFound == false ) {
			// 새로운 Package Node를 생성한다.
			DefaultMutableTreeNode newPackageNode = null;
			LoadedClassesEntry packageEntry = new LoadedClassesEntry(packageName, null,entryTypeDir);
			newPackageNode = addObject(currentNode,packageEntry,true);
			LoadedClassesEntry classEntry = new LoadedClassesEntry(className,packageName + "." + className, entryType);
			addObject(newPackageNode, classEntry,false);
		}
  }

  public String getCurrentClassId() {
    return currentClassId;
  }

  public void addListClass(String cls) {

  	int type;
  	if (cls.startsWith("java.") || cls.startsWith("javax."))
  		type = LoadedClassesEntry.JDK;
  	else if (cls.startsWith("sun."))
  		type = LoadedClassesEntry.SUN;
  	else
  		type = LoadedClassesEntry.USER;

  	LoadedClassesEntry entry = new LoadedClassesEntry(cls, null, type);
	 	if(!classesListModel.contains(entry))
	  	classesListModel.addElement(entry);
  }

  public void update() throws Exception {
    // Load된 class들을 list에 넣습니다.
    //classesListModel.removeAllElements();
    Thread t = new Thread (this);
    t.setPriority(Thread.MIN_PRIORITY);
    t.start();

  }

  public void run() {
    try {
      showTreeButton.setEnabled(false);
      showListButton.setEnabled(false);
      RemoteClass[] list = debuggerProxy.getRemoteDebugger().listClasses();
      test++;
      System.out.println("///////////////////////////////////////////");
      System.out.println("Update : " + test);
      System.out.println("///////////////////////////////////////////");
      //여기서 classListModel과 classTreeModel에 load된 class들을
      // 잘 넣어야 함다.
      System.out.println("LoadedClassesPanel.java update");
      loadedClasses = loadedInterfaces = 0;
      for (int i = 0 ; i < list.length ; i++) {
        StringBuffer desc = new StringBuffer();
        desc.append(list[i].getName());
        if (list[i].getName().startsWith("[")) {
          continue;
        }
        if ( list[i].isInterface() ) {
          //desc.append("(I)");
          loadedInterfaces++;
        } else {
          //desc.append("(C)");
          loadedClasses++;
        }

        //classes.addElement(list[i].description());
        if ( isList ) {
          // List에 추가
          addListClass(desc.toString());
        } else {
          // tree에 추가.
          //classesTreeModel.addClass(desc.toString());
          //if (!loadedClassVec.contains(desc.toString())){
          //  loadedClassVec.addElement(desc.toString());
          //  addClass(desc.toString());
          //}
          if (!loadedClassHash.contains(desc.toString())) {
            loadedClassHash.put(desc.toString(),desc.toString());
            addClass(desc.toString());
          }
        }
      }

      statusClasses.setText(" Classes : " + loadedClasses);
      statusInterface.setText(" Interfaces : " + loadedInterfaces);
      showTreeButton.setEnabled(true);
      showListButton.setEnabled(true);
    } catch (Exception e) {
      System.out.println("Exception in LoadedClassesPanel : " + e);
    } finally {
      System.out.println( "LoadedClasses Update thread is completed" );
    }
  }

  public void actionPerformed(ActionEvent evt){
  	String LF = evt.getActionCommand();
  	if(LF.equals(treeActionCommand)){
  		if(isList){
  			//System.out.println(listActionCommand);
        //지금 tree의 형태로 보여 주면 됨니다.
        classScrollPane.setViewportView(classTree);
  			isList = false;
  		}

  	} else if(LF.equals(listActionCommand)){
  		if(!isList){
  			//System.out.println(listActionCommand);
        classScrollPane.setViewportView(classList);
  			isList = true;
  		}
  	}
  	try {
	    update();
	  } catch (Exception e){
	    // currently just skip it.
	  }
  }
  class MyTreeModelListener implements TreeModelListener {
    public void treeNodesChanged(TreeModelEvent e) {
 			DefaultMutableTreeNode node;
    	node = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
     /*
     * If the event lists children, then the changed
     * node is the child of the node we've already
     * gotten.  Otherwise, the changed node and the
     * specified node are the same.
     */
    	try {
        int index = e.getChildIndices()[0];
        node = (DefaultMutableTreeNode)(node.getChildAt(index));
	    } catch (NullPointerException exc) {}
			System.out.println("The user has finished editing the node.");
    	System.out.println("New value: " + node.getUserObject());
		}
		public void treeNodesInserted(TreeModelEvent e) {
		}
		public void treeNodesRemoved(TreeModelEvent e) {
		}
		public void treeStructureChanged(TreeModelEvent e) {
		}
	}
}
