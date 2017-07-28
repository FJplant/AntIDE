/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co.
 * All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 *  $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/browser/packagebrowser/TreeViewPanel.java,v 1.7 1999/08/19 03:35:38 multipia Exp $
 *  $Revision: 1.7 $
 *  $History: TreeViewPanel.java $
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:25p
 * Updated in $/AntIDE/source/ant/browser/packagebrowser
 *
 * *****************  Version 1  *****************
 * User: Remember     Date: 99-05-06   Time: 10:55p
 * Created in $/AntIDE/source/ant/browser/packagebrowser
 * 새로 했음
 *
 * *****************  Version 6  *****************
 * User: Remember     Date: 98-10-23   Time: 2:05a
 * Updated in $/JavaProjects/src/ant/browser/packagebrowser
 * 
 * *****************  Version 5  *****************
 * User: Remember     Date: 98-10-21   Time: 1:22a
 * Updated in $/JavaProjects/src/ant/browser/packagebrowser
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 98-10-20   Time: 2:55a
 * Updated in $/JavaProjects/src/ant/browser/packagebrowser
 * 
 * *****************  Version 3  *****************
 * User: Remember     Date: 98-10-19   Time: 8:56a
 * Updated in $/JavaProjects/src/ant/browser/packagebrowser
 * 
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-10-17   Time: 4:06a
 * Updated in $/JavaProjects/src/ant/browser/packagebrowser
 * 
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-10-16   Time: 12:45a
 * Created in $/JavaProjects/src/ant/browser/packagebrowser
 */
package com.antsoft.ant.browser.packagebrowser;

import java.awt.Toolkit;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.PopupMenu;
import java.util.Vector;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.tree.*;
import javax.swing.event.*;

import com.antsoft.ant.pool.librarypool.*;
import com.antsoft.ant.pool.packagepool.*;
import com.antsoft.ant.pool.classpool.*;
import com.antsoft.ant.designer.classdesigner.ClassDesigner;
import com.antsoft.ant.codecontext.*;
import com.antsoft.ant.util.FontList;

/**
 *  This class manages real tree view
 *  @author Jin-woo Baek
 *  @author Sang-kyun Kim
 */
public class TreeViewPanel extends JPanel{
  protected DefaultMutableTreeNode rootNode;
  protected DefaultTreeModel treeModel;
  protected JTree tree;
  private Toolkit toolkit = Toolkit.getDefaultToolkit();
  private SigModel sigModel;
  private PackageBrowser packageBrowser;


  /** default constructor */
  public TreeViewPanel(){
  }

  /** Constructor */
  public TreeViewPanel(JTree treeObj) {
    this();
    TreeSelectionEventHandler treeSelectionEvnetHandler = new TreeSelectionEventHandler();

  	rootNode = new DefaultMutableTreeNode("Packages");
  	treeModel = new DefaultTreeModel(rootNode);

  	tree = treeObj;
    tree.setModel(treeModel);
    tree.addTreeSelectionListener(treeSelectionEvnetHandler);
    tree.setFont(FontList.treeFont); 

    tree.setEditable(false);
  	tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
  	tree.setShowsRootHandles(true);
  
  	JScrollPane scrollPane = new JScrollPane(tree);
//    setFont(FontList.regularFont);
  	setLayout(new GridLayout(1,0));
  	add(scrollPane);
  }

  /**
   *  Remove all nodes except the root node.
   */
  public void clear() {
  	rootNode.removeAllChildren();
  	treeModel.reload();
  }

  /**
   *  Remove the currently selected node.
   */
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

  /**
   * parent node로 부터 parameter로 넘어온 node를 삭제한다
   *
   * @param node node to remove
   */
  public void removeNodeFromParent(MutableTreeNode node){
    treeModel.removeNodeFromParent(node);
  }

  /**
   *  Add child to the currently selected node.
   *
   *  @param child object to add to tree
   *  @return DefalutMutableTreeNode the added treenode
   */
  public DefaultMutableTreeNode addObject(Object child) {
  	DefaultMutableTreeNode parentNode = null;
  	TreePath parentPath = tree.getSelectionPath();
  	if (parentPath == null) {
  		parentNode = rootNode;
  	} else {
  		parentNode = (DefaultMutableTreeNode)(parentPath.getLastPathComponent());
  	}

  	return addObject(parentNode, child, true);
  }

  /**
   *  Add child to the Root Node.
   *
   *  @param child object to add to tree
   *  @return DefalutMutableTreeNode the added treenode
   */
  public DefaultMutableTreeNode addObjectToRoot(Object child) {
  	DefaultMutableTreeNode parentNode = rootNode;

		return addObject(parentNode, child, true);
	}

	/**
	 *  Add child to the parent node.
	 *
	 *  @param parent parent object
	 *  @param child object to add to tree
	 *  @return DefaultMutableTreeNode the added treenode
	 */
	public DefaultMutableTreeNode addObject(Object parent, Object child) {
		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = new TreePath(parent);

		if (parentPath == null) {
			parentNode = rootNode;
		} else {
			parentNode = (DefaultMutableTreeNode)(parentPath.getLastPathComponent());
		}

		return addObject(parentNode, child, true);
	}

	/**
	 *  Add child to the parent node
	 *
	 *  @param parent parent node
	 *  @param child object to add to tree
	 *  @return DefalutMutableTreeNode the added treenode
	 */
	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child) {
		return addObject(parent, child, false);
	}

	/**
	 *  Add child to the parent node with visibility
	 *
	 *  @param parent parent node
	 *  @param child object to add to tree
	 *  @param shouldBeVisible whether let node visible or not
	 *  @return DefaultMutableTreeNode the added treenode
	 */
	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
				Object child, boolean shouldBeVisible) {
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child, true);

		if (parent == null) {
			parent = rootNode;
		}

		treeModel.insertNodeInto(childNode, parent, parent.getChildCount());

		// Make sure the user can see the lovely new node.
		if (shouldBeVisible) {
			tree.expandPath(new TreePath(parent.getPath()));
			tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		}

		return childNode;
	}

  /**
   * root node를 반환한다
   *
   * @return root node
   */
  public DefaultMutableTreeNode getRootNode(){
    return rootNode;
  }

  /**
   * 현재 선택된 sigmodel을 반환
   *
   * @return current selected sigmodel
   */
  private SigModel getSelectedSigModel(){
    return sigModel;
  }

  /**
   * 현재 선택된 sigmodel을 설정
   *
   * @param model current selected sigmodel
   */
  private void setSelectedSigModel(SigModel model){
    sigModel = model;
  }

  public PackageBrowser getPackageBrowser(){
     return packageBrowser;
  }

  public void setPackageBrowser(PackageBrowser packageBrowser){
     this.packageBrowser = packageBrowser;
  }

  /** tree selection event handler inner class */
  class TreeSelectionEventHandler implements TreeSelectionListener{
    public void valueChanged(TreeSelectionEvent e){
      TreePath path = e.getPath();
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
      Object userObj = node.getUserObject();

      if(userObj instanceof SigModel){
        SigModel model = (SigModel)userObj;
        setSelectedSigModel(model);
      }
      else{
        setSelectedSigModel(null);
      }

      if(path.getPathCount() == 3){
        packageBrowser.setSelectedPackageItem(node.toString());
      }
      else if(path.getPathCount() == 4){
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
        packageBrowser.setSelectedClassItem(parent.toString() + "." + node.toString());
      }
    }
  }
}
