/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/manager/projectmanager/OutputPanel.java,v 1.3 1999/08/14 01:33:56 strife Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * 컴파일이나 실행시 출력을 나타낼 곳.
 */
package com.antsoft.ant.manager.projectmanager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.tree.*;
import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;
import com.antsoft.ant.util.Constants;

/**
 *  class OuputPanel
 *
 *  @author Jinwoo Baek
 */
public class OutputPanel extends JPanel{
  private BorderLayout borderLayout1 = new BorderLayout();
  private JTree tree = null;

	private BufferedInputStream bin = null;
	private FileInputStream fin = null;
	private ProjectExplorer pe = null;

  private MouseAdapter mouseHandler = null;

  private Object selObj = null;
  private JViewport vp = null;
  private JScrollPane scroller = null;
  private Point last = null;

  private int line = -1;
	private int start = -1;
  private int end = -1;

  private DefaultTreeModel treeModel = null;
  private DefaultMutableTreeNode rootNode = null;

  private int numOfErrors = 0;
  private int numOfWarns  = 0;

	/**
	 *  Constructor
	 */
  public OutputPanel(ProjectExplorer pe) {
		this.pe = pe;
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    this.setLayout(borderLayout1);
		rootNode = new DefaultMutableTreeNode();
		treeModel = new DefaultTreeModel(rootNode);
		tree = new JTree(treeModel);
    tree.setEditable(false);
    tree.setRootVisible(false);
    tree.putClientProperty("JTree.lineStyle", "Angled");
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.setCellRenderer(new OutputPanelTreeCellRenderer());
    tree.setDoubleBuffered(true);
    add(tree);

    mouseHandler = new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
  			if (evt.getClickCount() == 2) {
         	doAction();
				}
			}
		};
    tree.addMouseListener(mouseHandler);

    scroller = new JScrollPane(tree);
    vp = scroller.getViewport();
		this.add(scroller);
  }

  public void finalize() {
  	borderLayout1 = null;
    tree.removeMouseListener(mouseHandler);
    mouseHandler = null;
    tree = null;
    bin = null;
    fin = null;
  }

  /**
   *  에러 위치를 찾아간다.
   */
  private void doAction() {
   	TreePath tp = tree.getSelectionPath();
    if (tp != null) {
   		DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)tp.getLastPathComponent();
      if (dmtn instanceof ErrorMsgTreeNode) {
       	ErrorMsgTreeNode emtn = (ErrorMsgTreeNode)dmtn;
        if (emtn.getType() != ErrorMsgTreeNode.CUSTOM) {
					File file = new File(emtn.getFile());
  	      int offset = emtn.getLine();
					if ((file != null) && (pe != null) && (offset != -1)) {
						pe.openDocumentWhenCompileError(file.getParent(), file.getName());
						pe.moveLine(offset - 1);
					}
				}
      }
    }
  }

  /**
   *  컴파일 출력 메시지를 분석한다.
   *  메시지 전체를 분석한다.
   */
  protected void parseMessage(Vector msgV) {


		String text = null;
    numOfErrors = 0;
    numOfWarns  = 0;

    for(int i=0; i<msgV.size(); i++){
      text = (String)msgV.elementAt(i);

      if ((text != null) && (text.indexOf(":") > 0)) {
        StringBuffer head = new StringBuffer();
        StringTokenizer st = new StringTokenizer(text);
        st = new StringTokenizer(st.nextToken("\r"));
        st = new StringTokenizer(st.nextToken("\n"));
        boolean error = false;
        while (st.hasMoreTokens()) {
          String str = st.nextToken(":");
          if (str.toLowerCase().endsWith(".java")) {
            head.append(str);
            error = true;
            break;
          }
          else {
            head.append(str);
            if (st.hasMoreTokens()) head.append(":");
          }
        }

        // 에러 또는 경고 메시지
        if (error) {
          // 에러 라인을 얻고
          try {
            int line = Integer.parseInt(st.nextToken(":"));
            String msg = st.nextToken(":");
            if (st.hasMoreTokens()) {
              String s = st.nextToken(":");
              File file = new File(head.toString());
              // Warning 인 경우
              ErrorMsgTreeNode emtn =
                new ErrorMsgTreeNode(file.getAbsolutePath(),
                    line, s, ErrorMsgTreeNode.WARNING);
              addMessage(emtn);
              numOfWarns++;
            }
            // Error 인 경우
            else {
              File file = new File(head.toString());
              ErrorMsgTreeNode emtn =
                new ErrorMsgTreeNode(file.getAbsolutePath(),
                    line, msg, ErrorMsgTreeNode.ERROR);
              addMessage(emtn);
              numOfErrors++;
            }
          } catch (NumberFormatException e) {
          }
        }
      }
    }
  }

  public void addMessage(ErrorMsgTreeNode node) {

  	if (!node.getFile().equals("")) {
	  	DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(node.getFile());
   	  DefaultMutableTreeNode parent = null;

    	for (int i = 0; i < rootNode.getChildCount(); i++) {
    		parent = (DefaultMutableTreeNode)rootNode.getChildAt(i);
	      if ((parent.getUserObject() != null) &&
        			parent.getUserObject().equals(dmtn.getUserObject())) break;
  	    else parent = null;
    	}


	    if (parent != null) {
        addObject(parent, node, false);
      }
  	  else {
        addObject(addObject(rootNode, node.getFile(), false), node, false);
      }
    }
    else{
    	addObject(rootNode, node, false);
    }
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
		DefaultMutableTreeNode childNode = null;
    ErrorMsgTreeNode childEM = null;
    boolean error = false;
    if (child instanceof ErrorMsgTreeNode) {
    	childEM = (ErrorMsgTreeNode)child;
      error = true;
    }
    else {
    	childNode = new DefaultMutableTreeNode(child);
      error = false;
    }

		if (parent == null) {
			parent = rootNode;
		}

    // 이미 있는 노드인지 확인 후
		int index = 0;
    boolean found = false;
		for (int i = 0; i < parent.getChildCount(); i++) {
    	DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)parent.getChildAt(i);
			if (!error && childNode.getUserObject().equals(dmtn.getUserObject())) {
      	found = true;
      	break;
			}
      else if (error && childEM.equals(dmtn)) {
      	found = true;
        break;
      }
      else {
      	if (error) {
        	if (dmtn instanceof ErrorMsgTreeNode) {
	        	ErrorMsgTreeNode emtn = (ErrorMsgTreeNode)dmtn;
		      	switch (childEM.getType()) {
  		      case ErrorMsgTreeNode.ERROR:
      	    	if (emtn.getType() == ErrorMsgTreeNode.ERROR) index++;
        	    break;
	          case ErrorMsgTreeNode.WARNING:
  	        	if ((emtn.getType() == ErrorMsgTreeNode.ERROR) ||
    	        		(emtn.getType() == ErrorMsgTreeNode.WARNING)) index++;
      	    	break;
        	  default:
	    	  		index = parent.getChildCount();
	          }
  	      }
          else index = parent.getChildCount();
        }
        else index = parent.getChildCount();
      }
		}

    // 없으면 넣는다.
		if (!found) {
    	if (error) treeModel.insertNodeInto(childEM, parent, index);
      else treeModel.insertNodeInto(childNode, parent, index);
    }

		// Make sure the user can see the lovely new node.
		tree.expandPath(new TreePath(parent.getPath()));
		if (shouldBeVisible) {
			tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		}
		return childNode;
	}

  public boolean isNoError() {
  	if (rootNode.getChildCount() == 0) return true;
    else return false;
  }

  public void selectFirstError() {
  	if (rootNode.getChildCount() != 0) {
    	for (int i = 0; i < tree.getRowCount(); i++) {
      	TreePath tp = tree.getPathForRow(i);
        if (tp != null) {
        	TreeNode tn = (TreeNode)tp.getLastPathComponent();
          if (tn instanceof ErrorMsgTreeNode) {
            ErrorMsgTreeNode emtn = (ErrorMsgTreeNode)tn;
            if (emtn.getType() == ErrorMsgTreeNode.ERROR) {
            	tree.setSelectionPath(tp);
              doAction();
            	break;
            }
          }
        }
      }
    }
  }

	/**
	 *  출력을 나타내는 프로젝트 설정
	 */
	public void setPE(ProjectExplorer pe) {
		this.pe = pe;
	}

	/**
	 *  출력을 나타낸다.
	 *
	 *  @param text 나타낼 문자열
	 */
  public void setText(Vector textV){
    if(textV.size() == 0) return;
    
    this.remove(scroller);
  	parseMessage(textV);
    this.add(scroller);
    scroller.repaint();
  }

  public void setText(String text){
    if(text.equals("")) return;

    StringTokenizer st = new StringTokenizer(text, "\n", false);
    Vector v = new Vector(20,5);

    while(st.hasMoreTokens()){
      v.addElement(st.nextToken());
    }

    setText(v);
  }

	/**
	 *  출력화면을 깨끗이한다.
	 */
  public void clear() {
    rootNode.removeAllChildren();
    treeModel.reload();
  }
}

