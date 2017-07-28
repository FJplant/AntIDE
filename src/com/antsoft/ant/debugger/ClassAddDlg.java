/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:    Lee, Chul Mok
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/ClassAddDlg.java,v 1.5 1999/08/19 03:39:33 multipia Exp $
 * $Revision: 1.5 $
 * $History: ClassAddDlg.java $
 */

package com.antsoft.ant.debugger;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import com.antsoft.ant.util.ImageList;
import com.antsoft.ant.util.FontList;
import com.antsoft.ant.util.ColorList;
import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.util.WindowDisposer;

public class ClassAddDlg extends JDialog {
  private JFrame parent = null;
  private Vector sourcePath = null; // 이름만 sourcepath이고 사실은 classpath임..
  private Vector classPath = null;  // dialog로 부터 받아오는 class file들을 위한 vector
  private boolean isWin = true;

  // Tree관련
  DefaultMutableTreeNode rootNode;
	DefaultTreeModel treeModel;
  private ClassChooserCellRenderer renderer = new ClassChooserCellRenderer();
	private JTree tree;

  // Button 관련
  private JLabel pathLabel = new JLabel();
  private JTextField pathFld = new JTextField();
  private JButton okBtn,cancelBtn,helpBtn;
  private boolean isOK = false;
  private boolean isHelp = false;


  public ClassAddDlg(JFrame frame, String title, Vector sourcePath, boolean modal) {
    super(frame, "Select Class File or Package", modal);
    this.parent = frame;
    this.sourcePath = sourcePath;

    // add window disposer
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    ActionListener al = new ActionHandler();

    ClassChooserEntry entry = new ClassChooserEntry("package", ClassChooserEntry.COMPUTER, null, true);
    rootNode = new DefaultMutableTreeNode(entry);
    treeModel = new DefaultTreeModel(rootNode);
    treeModel.addTreeModelListener(new MyTreeModelListener());
    tree = new JTree(treeModel);
    tree.setEditable(false);
//    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
    tree.setDoubleBuffered(true);
    tree.putClientProperty("JTree.lineStyle", "Angled");
    tree.setShowsRootHandles(true);
    tree.setCellRenderer(renderer);

    tree.addTreeSelectionListener(new TreeSelectionListener() {
    	public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                            tree.getLastSelectedPathComponent();
        if (node == null) return;
        ClassChooserEntry nodeInfo = (ClassChooserEntry)node.getUserObject();
        if (nodeInfo.getType() == ClassChooserEntry.DIR)
          pathFld.setText(nodeInfo.getFullPath()+ File.separator + "*;");
        else
          pathFld.setText(nodeInfo.getFullPath());

      }
   	});

    tree.addTreeWillExpandListener(new MyTreeWillExpandListener());
    treeModel.addTreeModelListener(new MyTreeModelListener());

    JScrollPane scrollPane = new JScrollPane(tree);

    JPanel topPane = new JPanel(new BorderLayout());
    JLabel expLabel = new JLabel();
    expLabel.setText("    Select Package or Class file");
    expLabel.setFont(FontList.myTextFieldFont);
    topPane.add(expLabel,BorderLayout.NORTH);

    JPanel pathP = new JPanel(new BorderLayout());
    pathLabel.setBorder(BorderList.raisedBorder);
    pathLabel.setFont(FontList.dialogInput12);
    pathLabel.setBackground(ColorList.veryLightGray);
    pathFld.setPreferredSize(new Dimension(500,20));
    pathFld.setText(" ");
    pathLabel.setText(" Name : ");
    pathP.add(pathLabel,BorderLayout.WEST);
    pathP.add(pathFld, BorderLayout.CENTER);
    JPanel emptyP = new JPanel();
    emptyP.setPreferredSize(new Dimension(10,1));
    JPanel emptyP2 = new JPanel();
    emptyP2.setPreferredSize(new Dimension(10,1));
    topPane.add(pathP, BorderLayout.CENTER);
    topPane.add(emptyP, BorderLayout.WEST);
    topPane.add(emptyP2, BorderLayout.EAST);

        // Down 화면을 위한 패널
    JPanel downPane = new JPanel();
    okBtn = new JButton("OK");
    okBtn.setActionCommand("OK");
    okBtn.addActionListener(al);

    cancelBtn = new JButton("Cancel");
    cancelBtn.setActionCommand("CANCEL");
    cancelBtn.addActionListener(al);

    helpBtn = new JButton("Help");
    helpBtn.setActionCommand("Help");
    downPane.add(okBtn);
    downPane.add(cancelBtn);
    downPane.add(helpBtn);

    this.getContentPane().add(scrollPane, BorderLayout.CENTER);
    this.getContentPane().add(topPane, BorderLayout.NORTH);
    this.getContentPane().add(downPane, BorderLayout.SOUTH);
    this.getContentPane().add(new JPanel(), BorderLayout.EAST);
    this.getContentPane().add(new JPanel(), BorderLayout.WEST);

    setSize(500,400);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width)   dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2, (screenSize.height - dlgSize.height) / 2);
    setResizable(true);

    if (System.getProperty("os.name").toLowerCase().indexOf("win") != -1)
      initPackage();
    else
      initPackageLinux();
  }

  public void initPackage() {
    if (sourcePath != null) {

      DefaultMutableTreeNode rootnode= (DefaultMutableTreeNode)treeModel.getRoot();
      for(int i=0; i<sourcePath.size(); i++) {
        String path = (String)sourcePath.elementAt(i);
        File f = new File(path);

        if (f.isDirectory()) {
          String []dirs = searchChildDir(path);
          if (dirs != null) {
            for(int j=0; j<dirs.length; j++) {
              String packagename = "[ " + path + File.separator + " ] " + dirs[j];

              ClassChooserEntry entry = new ClassChooserEntry(packagename, ClassChooserEntry.DIR, path+File.separator+dirs[j], false);
              DefaultMutableTreeNode entryNode = addObject(rootnode,entry,true);

              String[] tmp = searchChildDir(entry.getFullPath());
              if (tmp == null || tmp.length==0)
                tmp = searchChildFiles(entry.getFullPath());
              if (tmp != null && tmp.length > 0) {
                ClassChooserEntry empty = new ClassChooserEntry(null,ClassChooserEntry.DIR,null,false);
                addObject(entryNode,empty,false);
              }
            }
          }
          String []files = searchChildFiles(path);
          //System.out.println(path + " : " + files.length);
          if (files != null) {
            for(int j=0; j<files.length; j++) {
              String classname = "[ " + path + File.separator + " ] " + files[j];

              ClassChooserEntry entry = new ClassChooserEntry(classname, ClassChooserEntry.CLASSFILE, path+File.separator+files[j], false);
              addObject(rootnode,entry,true);
            }
          }
        } else {
          //System.out.println("It's File : " + path );
        }
      }
    }
  }
  public void initPackageLinux() {
    isWin = false;

  }

  public void setClassPath() {
    classPath = new Vector();

    TreePath [] treePath = tree.getSelectionPaths();
    if (treePath != null && treePath.length >0) {
      for(int i=0; i<treePath.length; i++) {
        String packageName = "";
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)treePath[i].getLastPathComponent();
        TreeNode [] parentNode = node.getPath();
        DefaultMutableTreeNode packageNode = (DefaultMutableTreeNode) parentNode[1];

        ClassChooserEntry rootentry = (ClassChooserEntry)packageNode.getUserObject();
        ClassChooserEntry entry = (ClassChooserEntry)node.getUserObject();

        if (entry.getType() == ClassChooserEntry.DIR ||entry.getType() == ClassChooserEntry.COMPUTER){
          continue;
          //이곳은 나중에 package전체를 등록할 수 있도록 할때 따로 설계할 것임.
        }

        packageName = rootentry.getFullPath().substring(rootentry.getFullPath().lastIndexOf(File.separator)+1);
        packageName += entry.getFullPath().substring(rootentry.getFullPath().length());
        packageName = packageName.replace(File.separatorChar , '.');
        packageName = packageName.substring(0,packageName.indexOf(".class"));

        classPath.addElement(packageName);
        //System.out.println(rootentry.getFullPath());
        //System.out.println(entry.getFullPath());
      }
    }
    else {
      //System.out.println("no selection");
    }
  }

  // 실제 OK Btn을 누른후 클릭된 classPath를 등록한다.
  public Vector getClassPath() {
    return classPath;
  }

  // path를 받아서 하위 디렉토리를 return 한다.
  public String[] searchChildDir(String dirName) {
		String [] dirs = null ;

		File temp = new File(dirName);
		dirs = temp.list( new FilenameFilter() {
			public boolean accept(File dir, String fileName) {
				if ((new File(dir, fileName)).isDirectory() )
					return true;
				else return false;
			}
		});

		return dirs;
	}

  // 디렉토리를 받아서 그 디렉토리안의 file을 return 한다.
  public String[] searchChildFiles(String dirName) {
    String [] files = null;

    File temp = new File(dirName);
    files = temp.list( new FilenameFilter() {
      public boolean accept(File dir, String fileName) {
        if ((new File(dir,fileName)).isFile() && fileName.toLowerCase().endsWith(".class"))
          return true;
        else return false;
      }
    });

    return files;
  }

  public boolean isOK() {
    return this.isOK;
  }

  public void okPressed() {
    isOK = true;
    setClassPath();

    dispose();
  }
  public void dispose() {
    int count = getComponentCount();
    for(int i=0; i<count; i++) {
      Component c = getComponent(i);
      this.remove(c);
      c = null;
    }
    super.dispose();
    System.gc();
  }

  public void removeCurrentNode() {

    TreePath currentSelection = tree.getSelectionPath();
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
    TreePath parentPath = tree.getSelectionPath();
    if (parentPath == null) {
      parentNode = rootNode;
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
      parent = rootNode;
    }
    treeModel.insertNodeInto(childNode, parent,
                             parent.getChildCount());
     // Make sure the user can see the lovely new node.
    if (shouldBeVisible) {
      tree.scrollPathToVisible(new TreePath(childNode.getPath()));
    }
    return childNode;
  }
  public void showDirectory(DefaultMutableTreeNode node, ClassChooserEntry entry) {
		addObject(node,entry,true);
	}

  class ActionHandler implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();

      if(cmd.equals("OK")){
        okPressed();
      }
      else if(cmd.equals("CANCEL")) {
        dispose();
      }
      else if(cmd.equals("HELP")) {
      }
    }
  }
  
  class MyTreeWillExpandListener implements TreeWillExpandListener{
    public void treeWillExpand(TreeExpansionEvent e) {

      DefaultMutableTreeNode node = (DefaultMutableTreeNode)(e.getPath().getLastPathComponent());
      if (node == null) return;
      ClassChooserEntry nodeinfo = (ClassChooserEntry)node.getUserObject();

      if (nodeinfo.getCheck()) {
        return;
      }
      nodeinfo.setCheck(true);

      // 일부러 집어 넣은 빈 노드를 하나 삭제한다.
      DefaultMutableTreeNode emptyTreeNode = node.getFirstLeaf();
      treeModel.removeNodeFromParent(emptyTreeNode);

      String []dirs = searchChildDir(nodeinfo.getFullPath());
      if (dirs == null)
        return;

      for (int i=0; i<dirs.length; i++) {
        ClassChooserEntry entry = new ClassChooserEntry(dirs[i],ClassChooserEntry.DIR, nodeinfo.getFullPath() + File.separator + dirs[i], false);
        DefaultMutableTreeNode entryNode = addObject(node, entry, true);

        String[] tmp = searchChildDir(entry.getFullPath());
        if (tmp == null || tmp.length==0)
          tmp = searchChildFiles(entry.getFullPath());
        if (tmp != null && tmp.length > 0) {
          ClassChooserEntry empty = new ClassChooserEntry(null,ClassChooserEntry.DIR,null,false);
          addObject(entryNode,empty,false);
        }
      }

      String []classes = searchChildFiles(nodeinfo.getFullPath());
      if (classes == null)
        return;
      for (int i=0; i<classes.length; i++) {
        ClassChooserEntry entry = new ClassChooserEntry(classes[i],ClassChooserEntry.CLASSFILE, nodeinfo.getFullPath()+File.separator + classes[i], false);
        addObject(node, entry, true);
      }
    }
    public void treeWillCollapse(TreeExpansionEvent e) {

    }
  }
}
class ClassChooserCellRenderer extends DefaultTreeCellRenderer {
  /** Icon 들 */
	static private ImageIcon computerIcon = null;
	static private ImageIcon dirIcon = null;
  static private ImageIcon classIcon = null;

  static {
		// Loading Image
		try {
			computerIcon = ImageList.computerIcon;
      dirIcon = ImageList.dirIcon;
      classIcon = ImageList.classIcon;

		} catch (Exception e) {
			System.err.println("Can't load Image : com/antsoft/ant/debugger/images/....");
		}
  }

	/**
	 *  Constructor
	 */
	public ClassChooserCellRenderer() {
		super();
	}

  /**
   * This is messaged from JTree whenever it needs to get the size
   * of the component or it wants to draw it.
   * This attempts to set the font based on value, which will be
   * a TreeNode.
   */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
						boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

		String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, hasFocus);

		// Tooltips used by the tree.
		setToolTipText(stringValue);
		DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)value;
		ClassChooserEntry entry = (ClassChooserEntry)dmtn.getUserObject();

		switch(entry.getType()) {
			case ClassChooserEntry.DIR:
				setIcon(dirIcon);
				break;
			case ClassChooserEntry.COMPUTER:
				setIcon(computerIcon);
				break;
      case ClassChooserEntry.CLASSFILE:
        setIcon(classIcon);
        break;

		  default:
		}

		setText(stringValue);
		return this;
	}
}


class ClassChooserEntry {
 	public static final int COMPUTER   = 1;
	public static final int DIR      = 2;
  public static final int CLASSFILE = 3;

 	private int type;
  private boolean check;
	private String content = null;
  private String fullpath = null;
	private String[] childs = null;
	/**
	 *  Constructor
	 */
	public ClassChooserEntry(String content, int type, String fullpath, boolean check) {
		this.type = type;
		this.content = content;
		this.check = check;
		this.fullpath = fullpath;
	}

	/**
	 *  노드의 타입을 얻는다.
	 */
	public int getType() {
		return type;
	}

  public void setType(int type) {
    this.type = type;
  }

	/**
	 *  노드의 내용을 얻는다.
	 */
	public String getContent() {
		return content;
	}

	/**
	 *  노드에 내용을 넣는다.
	 */
	public void setContent(String content) {
		this.content = content;
	}
		/**
	 * Gets the check
	 * @return check
	 */
	public boolean getCheck ()
	{
		return check;
	}

	/**
	 * Sets the check
	 * @param check
	 */
	public void setCheck ( boolean check )
	{
		this.check = check;
	}

	/**
	 * Gets the childs
	 * @return childs
	 */
	public String getFullPath()
	{
		return fullpath;
	}

	/**
	 * Sets the childs
	 * @param childs
	 */
	public void setFullPath(String path )
	{
		this.fullpath = path;
	}


	/**
	 *  노드를 나타내는 값을 얻는다.
	 */
	public String toString() {
		return content;
	}


  /**
   *  두 객체가 같은지 본다.
   */
  public boolean equals(Object obj) {
  	if (obj instanceof ClassChooserEntry) {
    	ClassChooserEntry sbte = (ClassChooserEntry)obj;
      if ((sbte.getType() == this.type) && this.content.equals(sbte.getContent()))
      	return true;
    }
    return false;
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
