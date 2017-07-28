/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co.
 * All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Id: FileBrowser.java,v 1.22 1999/08/31 04:09:35 multipia Exp $
 * $Revision: 1.22 $
 */
package com.antsoft.ant.browser.filebrowser;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.io.File;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import com.antsoft.ant.util.QuickSorter;
import com.antsoft.ant.util.ImageList;
import com.antsoft.ant.manager.projectmanager.*;

/**
 * @author 이철목
 * @version 1.0
 * @since 1999.06.15
 */
public class FileBrowser extends JPanel implements ActionListener{
	protected DefaultMutableTreeNode rootNode;
	protected DefaultTreeModel treeModel;
	protected JTree tree;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private FileBrowserCellRenderer renderer = new FileBrowserCellRenderer();
	private FileBrowsePanel parent = null;

	public FileBrowser(FileBrowsePanel parent) {
		this.parent = parent;

    UIManager.put("Tree.expandedIcon", ImageList.minusIcon);
    UIManager.put("Tree.collapsedIcon", ImageList.plusIcon);

    MouseListener treeMouseHandler = new TreeMouseHandler();

    FileBrowserEntry entry = new FileBrowserEntry("computer", FileBrowserEntry.COMPUTER, null, true);
		rootNode = new DefaultMutableTreeNode(entry);
		treeModel = new DefaultTreeModel(rootNode);
    treeModel.addTreeModelListener(new MyTreeModelListener());


    tree = new JTree(treeModel);
    tree.setEditable(false);
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.setDoubleBuffered(true);
    tree.putClientProperty("JTree.lineStyle", "Angled");
    tree.setShowsRootHandles(true);
    tree.setCellRenderer(renderer);
    tree.addTreeWillExpandListener(new MyTreeWillExpandListener());
    tree.addMouseListener(treeMouseHandler);

/*
    try {
      UIManager.put("Tree.expandedIcon", new ImageIcon("image/temp.gif"));
      System.out.println(UIManager.get("Button.border"));
    	UIManager.setLookAndFeel("javax.swing.plaf.multi.multiLookAndFeel");
    	UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      SwingUtilities.updateComponentTreeUI(tree);
    } catch (Exception e) {
    }
*/
    tree.addTreeSelectionListener(new TreeSelectionListener() {
    	public void valueChanged(TreeSelectionEvent e) {
      	DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                            tree.getLastSelectedPathComponent();
        if (node == null) return;
        FileBrowserEntry nodeInfo = (FileBrowserEntry)node.getUserObject();


        if (nodeInfo.getType() == FileBrowserEntry.DIR) {
          nodeInfo.setType(FileBrowserEntry.OPENDIR);
          closeDocument();
        }
        if (nodeInfo.getType() == FileBrowserEntry.COMPUTER)
          closeDocument();
        else if (nodeInfo.getType() == FileBrowserEntry.DRIVER)
          closeDocument();

        TreePath oldPath = e.getOldLeadSelectionPath();

        if (oldPath != null) {
          DefaultMutableTreeNode oldNode = (DefaultMutableTreeNode)oldPath.getLastPathComponent();
          FileBrowserEntry oldEntry = (FileBrowserEntry)oldNode.getUserObject();

          if (oldEntry.getType() == FileBrowserEntry.OPENDIR)
            oldEntry.setType(FileBrowserEntry.DIR);
        }
        if (nodeInfo.getType() == FileBrowserEntry.JAVA) {
          String path = getSelectedFilePath();
          String filename = nodeInfo.getContent();
          openJavaDocument(path,filename);
        }
        else if(nodeInfo.getType() == FileBrowserEntry.TEXT) {
          String path = getSelectedFilePath();
          String filename = nodeInfo.getContent();
          openTextDocument(path,filename);
        }
        else if(nodeInfo.getType() == FileBrowserEntry.HTML) {
          String path = getSelectedFilePath();
          String filename = nodeInfo.getContent();
          openHtmlDocument(path,filename);
        }
        else if(nodeInfo.getType() == FileBrowserEntry.BMP) {
          String path = getSelectedFilePath();
          String filename = nodeInfo.getContent();
          openBmpImage(path,filename);
        }
        else if(nodeInfo.getType() == FileBrowserEntry.JPG) {
          String path = getSelectedFilePath();
          String filename = nodeInfo.getContent();
          openJpgImage(path,filename);
        }
        else if(nodeInfo.getType() == FileBrowserEntry.GIF) {
          String path = getSelectedFilePath();
          String filename = nodeInfo.getContent();
          openGifImage(path,filename);
        }
      }
   	});

    JScrollPane scrollPane = new JScrollPane(tree);
    setLayout(new GridLayout(1,0));
    add(scrollPane);
	}
  public void closeDocument() {
    parent.pe.closeImageSource();
    parent.pe.closeFile();
  }

  String lastOpenFile;
  public void openJavaDocument(String path, String file) {
    if(lastOpenFile != null) parent.pe.getSourcePool().deleteSource(lastOpenFile);

    File f = new File(path, file);
    ProjectManager.getCurrentProject().addFile(f.getAbsolutePath());
    parent.pe.showTextPanel();
    parent.pe.openDocument(f.getAbsolutePath(), true);

    lastOpenFile = f.getAbsolutePath();
  }

  public void openTextDocument(String path, String file) {
    String fullPath = path + File.separator + file;
    parent.pe.showTextPanel();
    parent.pe.openDocument(fullPath,true);
  }

  public void openHtmlDocument(String path, String file) {
    String fullPath = path + File.separator + file;
    parent.pe.showTextPanel();
    parent.pe.openDocument(fullPath,true);
  }
  public void openBmpImage(String path, String filename) {
    parent.pe.openImageSource(path, filename);
  }
  public void openJpgImage(String path, String filename) {
    parent.pe.openImageSource(path, filename);
  }
  public void openGifImage(String path, String filename) {
    parent.pe.openImageSource(path, filename);
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
    toolkit.beep();
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

	public void showDirectory(DefaultMutableTreeNode node, FileBrowserEntry entry) {

		addObject(node,entry,true);
	}

	public String getSelectedDirectoryPath() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if (node == null) return null;

		Object [] parentObject = 	node.getUserObjectPath();
		String path = "";
		for(int j=1; j<parentObject.length; j++) {
			FileBrowserEntry parentPath = (FileBrowserEntry) parentObject[j];
			path = path + parentPath.getContent() + File.separator;
		}
    // 명시적으로 Method를 빠져나가기 전에 필요없는 instance는 null로
    // 해 주는 것이 명확하게 garbage collect된다.
    parentObject = null;

		return path;
	}
  public String getSelectedFilePath() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if (node == null) return null;

		Object [] parentObject = 	node.getUserObjectPath();
		String path = "";
		for(int j=1; j<parentObject.length-1; j++) {
			FileBrowserEntry parentPath = (FileBrowserEntry) parentObject[j];
			path = path + parentPath.getContent() + File.separator;
		}

    parentObject = null;
		return path;
	}

  public void actionPerformed(ActionEvent evt) {
  }

	class MyTreeWillExpandListener implements TreeWillExpandListener{
		public void treeWillExpand(TreeExpansionEvent e) {
      Vector sortedList = new Vector();
			DefaultMutableTreeNode node;
			node = (DefaultMutableTreeNode)(e.getPath().getLastPathComponent());

			if (node == null) return;

			FileBrowserEntry nodeinfo = (FileBrowserEntry)node.getUserObject();

			if (nodeinfo.getCheck()) {
				return;
			}
			nodeinfo.setCheck(true);

			//// path를 구한다.
			Object [] parentObject = node.getUserObjectPath();

			String path = "";
			for(int j=1; j<parentObject.length; j++) {
				FileBrowserEntry parentPath = (FileBrowserEntry) parentObject[j];
				path = path + parentPath.getContent() + File.separator;
			}
      // 다 썼으면 명시적으로 null
      parentObject = null;
			///////////////////////////////////////////

			//if(nodeinfo.getChilds() != null) {
		  DefaultMutableTreeNode emptyTreeNode = node.getFirstLeaf();
		  FileBrowserEntry preEmpty = (FileBrowserEntry)emptyTreeNode.getUserObject();

			if(preEmpty.getContent() == null) {
				treeModel.removeNodeFromParent(emptyTreeNode);

				String[] childNode = parent.searchChildDir(path);
        if (childNode == null)
          return;
        String[] childFiles = parent.searchChildFiles(path);

				for(int a=0; a<childNode.length; a++){
					FileBrowserEntry entry = new FileBrowserEntry(childNode[a],FileBrowserEntry.DIR,null,false);
					//showDirectory(node,entry);
          sortedList.addElement(entry);
				}
        sortedList = QuickSorter.sort(sortedList,QuickSorter.LESS_STRING,true);
        for( int b=0; b<sortedList.size(); b++) {
          FileBrowserEntry em = (FileBrowserEntry)sortedList.elementAt(b);
          //showDirectory(node,em);
          DefaultMutableTreeNode entrynode = addObject(node,em,false);
          addObject(entrynode,preEmpty,false);

        }

        sortedList.removeAllElements();

        for (int a=0; a<childFiles.length; a++) {
          if (childFiles[a].toLowerCase().endsWith(".java")) {
            FileBrowserEntry entry = new FileBrowserEntry(childFiles[a],FileBrowserEntry.JAVA,null,false);
            //showDirectory(node,entry);
            sortedList.addElement(entry);
          } else if (childFiles[a].toLowerCase().endsWith(".js")) {
            FileBrowserEntry entry = new FileBrowserEntry(childFiles[a],FileBrowserEntry.JAVASCRIPT,null,false);
            //showDirectory(node,entry);
            sortedList.addElement(entry);
          } else if (childFiles[a].toLowerCase().endsWith(".pl")) {
            FileBrowserEntry entry = new FileBrowserEntry(childFiles[a],FileBrowserEntry.PERL,null,false);
            //showDirectory(node,entry);
            sortedList.addElement(entry);
          } else if (childFiles[a].toLowerCase().endsWith(".txt")) {
            FileBrowserEntry entry = new FileBrowserEntry(childFiles[a],FileBrowserEntry.TEXT,null,false);
            //showDirectory(node,entry);
            sortedList.addElement(entry);
          } else if (childFiles[a].toLowerCase().endsWith(".html") || childFiles[a].toLowerCase().endsWith(".htm")) {
            FileBrowserEntry entry = new FileBrowserEntry(childFiles[a],FileBrowserEntry.HTML,null,false);
            //showDirectory(node,entry);
            sortedList.addElement(entry);
          } else if (childFiles[a].toLowerCase().endsWith(".gif")) {
            FileBrowserEntry entry = new FileBrowserEntry(childFiles[a],FileBrowserEntry.GIF,null,false);
            //showDirectory(node,entry);
            sortedList.addElement(entry);
          } else if (childFiles[a].toLowerCase().endsWith(".jpg")) {
            FileBrowserEntry entry = new FileBrowserEntry(childFiles[a],FileBrowserEntry.JPG,null,false);
            //showDirectory(node,entry);
            sortedList.addElement(entry);
          } //else if (childFiles[a].toLowerCase().endsWith(".bmp")) {
            //FileBrowserEntry entry = new FileBrowserEntry(childFiles[a],FileBrowserEntry.BMP,null,false);
            //showDirectory(node,entry);
          //}
        }
        sortedList = QuickSorter.sort(sortedList,QuickSorter.LESS_STRING,true);
        for( int b=0; b<sortedList.size(); b++) {
          FileBrowserEntry em = (FileBrowserEntry)sortedList.elementAt(b);
          showDirectory(node,em);
        }

        /*
				for (int i=0; i<childNode.length; i++) {
					DefaultMutableTreeNode childTreeNode = (DefaultMutableTreeNode)node.getChildAt(i);
					String[] childOfChild = parent.searchChildDir(path+childNode[i]);
          String[] filesOfChild = null;
          if (childOfChild.length == 0) {
            filesOfChild = parent.searchChildFiles(path+childNode[i]);
          }

          if(childOfChild.length>0 || filesOfChild.length>0) {
						FileBrowserEntry empty = new FileBrowserEntry(null,FileBrowserEntry.DIR,null,false);
						addObject(childTreeNode, empty);
					}
					//FileBrowserEntry childDir = new FileBrowserEntry(path+childNode[i],FileBrowserEntry.DIR,childOfChild,false);
					//System.out.println(childNode[i]);
					//parent.viewDownNode(childTreeNode, childDir);
				}
        */
        childNode = null;
        childFiles = null;
			}
		}
		public void treeWillCollapse(TreeExpansionEvent e) {
			DefaultMutableTreeNode node;
			node = (DefaultMutableTreeNode)(e.getPath().getLastPathComponent());

			if (node == null) return;

			FileBrowserEntry nodeinfo = (FileBrowserEntry)node.getUserObject();
		}
	}

  class TreeMouseHandler extends MouseAdapter {
    public void mousePressed(MouseEvent evt){
      if (evt.getClickCount() == 2) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                            tree.getLastSelectedPathComponent();
        if (node == null) return;
        FileBrowserEntry nodeInfo = (FileBrowserEntry)node.getUserObject();

        if (nodeInfo.getType() == FileBrowserEntry.JAVA) {
          String path = getSelectedDirectoryPath();
          String filename = nodeInfo.getContent();
          //parent.pe.addToFilesTab(path,filename,true);
        }
      }

    }
    public void mouseReleased(MouseEvent evt) {

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

/*
 * $Log: FileBrowser.java,v $
 * Revision 1.22  1999/08/31 04:09:35  multipia
 * Warning나는 것 수정
 *
 * Revision 1.21  1999/08/24 07:44:51  remember
 * no message
 *
 * Revision 1.20  1999/08/16 08:48:55  remember
 * no message
 *
 * Revision 1.19  1999/08/07 15:10:39  multipia
 * 명시적인 null 대입 추가
 *
 * Revision 1.18  1999/08/04 02:56:59  itree
 * 수정
 *
 * Revision 1.17  1999/07/27 04:53:53  multipia
 * Java Script, Perl 추가
 *
 * Revision 1.16  1999/07/27 04:45:52  multipia
 * Header 부분 추가
 *
 */
