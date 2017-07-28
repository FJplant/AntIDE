/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/LocalVariablesPanel.java,v 1.22 1999/08/31 05:22:57 itree Exp $
 * $Revision: 1.22 $
 * $History: LocalVariablesPanel.java $
 */


package com.antsoft.ant.debugger;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.tree.*;

import javax.swing.event.*;

import sun.tools.debug.*;

public class LocalVariablesPanel extends JPanel {
  private DebuggerProxy debuggerProxy;
  private RemoteThread currentThread;

  private BorderLayout borderLayout1 = new BorderLayout();

  // Variable 정보를 트리로 나타내야 오브젝트 타입의 데이터도 쉽게 나타낼 수 있다.
  private JTree tree;
  private LocalVariablesTreeCellRenderer renderer;
  private DefaultTreeModel treeModel;
  private DefaultMutableTreeNode rootNode;
  private DefaultMutableTreeNode localVars;
  private DefaultMutableTreeNode methodArgs;
  private LocalVariablesEntry rootentry;
  private LocalVariablesEntry localentry;
  private LocalVariablesEntry methodentry;

  private Vector localVariableVector;
  private JButton addBtn = null;
  private ImageIcon btnIcon = null;
  private AntDebuggerPanel parentPanel = null;

  /**
   *  Constructor
   *  @param debuggerProxy 디버거의 정보를 얻어올 수 있는 proxy
   */
  public LocalVariablesPanel(DebuggerProxy debuggerProxy, AntDebuggerPanel parent) {
    this.debuggerProxy = debuggerProxy;
    this.parentPanel = parent;

    // 트리 초기화

    UIManager.put("Tree.expandedIcon",
              new ImageIcon(LocalVariablesPanel.class.getResource("image/minus.gif")));
    UIManager.put("Tree.collapsedIcon",
              new ImageIcon(LocalVariablesPanel.class.getResource("image/plus.gif")));

    
    renderer = new LocalVariablesTreeCellRenderer();

    rootentry = new LocalVariablesEntry("root", null, null, null, null,LocalVariablesEntry.ROOT);
    localentry = new LocalVariablesEntry("Local Variables", null, null, null,null, LocalVariablesEntry.LOCAL);
    methodentry = new LocalVariablesEntry("Method Arguments", null, null, null,null, LocalVariablesEntry.METHOD);
    rootNode = new DefaultMutableTreeNode(rootentry);
    treeModel = new DefaultTreeModel(rootNode);
    tree = new JTree(treeModel);
    tree.setCellRenderer(renderer);
    tree.putClientProperty("JTree.lineStyle", "Angled");
    tree.setShowsRootHandles(true);
    tree.setRootVisible(false);
    tree.addTreeWillExpandListener(new MyTreeWillExpandListener());
    tree.setFont(new Font("DialogInput", Font.PLAIN, 12));
    tree.addTreeSelectionListener( new TreeSelectionListener() {
  		public void valueChanged( TreeSelectionEvent e ) {
	  		//TO DO
        addBtn.setEnabled(true);
      }
  	});
    //rootNode = (DefaultMutableTreeNode)treeModel.getRoot();

    localVars = addObject(rootNode,localentry,true);
    methodArgs = addObject(rootNode,methodentry,true);
    try  {
      uiInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  public LocalVariablesPanel(DebuggerProxy debuggerProxy, boolean dlg) {
    if (dlg == false)
      return;
    this.debuggerProxy = debuggerProxy;
    // 트리 초기화

    UIManager.put("Tree.expandedIcon",
              new ImageIcon(LocalVariablesPanel.class.getResource("image/minus.gif")));
    UIManager.put("Tree.collapsedIcon",
              new ImageIcon(LocalVariablesPanel.class.getResource("image/plus.gif")));


    renderer = new LocalVariablesTreeCellRenderer();

    rootentry = new LocalVariablesEntry("root", null, null, null, null,LocalVariablesEntry.ROOT);
    localentry = new LocalVariablesEntry("Local Variables", null, null, null,null, LocalVariablesEntry.LOCAL);
    methodentry = new LocalVariablesEntry("Method Arguments", null, null, null,null, LocalVariablesEntry.METHOD);
    rootNode = new DefaultMutableTreeNode(rootentry);
    treeModel = new DefaultTreeModel(rootNode);
    tree = new JTree(treeModel);
    tree.setCellRenderer(renderer);
    tree.putClientProperty("JTree.lineStyle", "Angled");
    tree.setShowsRootHandles(true);
    tree.setRootVisible(false);
    tree.addTreeWillExpandListener(new MyTreeWillExpandListener());
    tree.setFont(new Font("DialogInput", Font.PLAIN, 12));
    //rootNode = (DefaultMutableTreeNode)treeModel.getRoot();

    localVars = addObject(rootNode,localentry,true);
    methodArgs = addObject(rootNode,methodentry,true);
    try  {
      uiInit2();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  public final void reset(DebuggerProxy debuggerProxy, AntDebuggerPanel antDebuggerPanel) {
    this.debuggerProxy = debuggerProxy;
    this.parentPanel = antDebuggerPanel;

    localVars.removeAllChildren();
    methodArgs.removeAllChildren();
    rootNode.removeAllChildren();
    treeModel.reload(rootNode);

    localVars = addObject(rootNode, localentry,true);
    methodArgs = addObject(rootNode, methodentry, true);

    //viewVars();
    //System.out.println("[L_V_Panel] locals : " + locals);
    //localListModel.removeAllElements();
  }

  void uiInit() throws Exception {
    this.setLayout(borderLayout1);
    ActionListener al = new ActionHandler();

    btnIcon = new ImageIcon(LocalVariablesPanel.class.getResource("image/arrright.gif"));
    addBtn = new JButton(btnIcon);
    addBtn.setActionCommand("ADD");
    addBtn.addActionListener(al);
    addBtn.setEnabled(false);

    JPanel upPane = new JPanel(new BorderLayout());
    upPane.add(new JLabel("Local Variables: "), BorderLayout.CENTER);
    upPane.add(addBtn, BorderLayout.EAST);

    add(upPane, BorderLayout.NORTH);
    add(new JScrollPane(tree), BorderLayout.CENTER);

    setPreferredSize( new Dimension( 300, 200 ) );
  }
  void uiInit2() throws Exception {
    this.setLayout(borderLayout1);

    //add(new JLabel("Local Variables:"), BorderLayout.NORTH);
    add(new JScrollPane(tree), BorderLayout.CENTER);

    //setPreferredSize( new Dimension( 300, 200 ) );
  }


  ///////////////////////////////////////////////////////////////////
  // Tree 관련 함수.
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
  // Tree 관련 함수 끝.
  //////////////////////////////////////////////////////////////

  public void update() throws Exception{
    viewVars();
  }

  // 이 함수는 WatchLblDlg.java 에서 호출하는 함수임.
  // select된 entry의 정보를 넘겨준다.
  public LocalVariablesEntry getSelectionEntry() {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
    LocalVariablesEntry entry = (LocalVariablesEntry)node.getUserObject();
    if(entry == null)
      return null;

    TreeNode[] path = node.getPath();

    if (path.length < 3)
      return entry;

    StringBuffer buf = new StringBuffer();
    for (int a=2; a<path.length-1; a++) {
      LocalVariablesEntry tmpEntry = (LocalVariablesEntry)((DefaultMutableTreeNode)path[a]).getUserObject();
      buf.append(tmpEntry.getName() + ".");
    }
    buf.append(entry.getName());
    entry.setValue(buf.toString());
    return entry;
  }

  // 직접 WatchListPanel에 ADD할 수 있는 함수.
  private void addToList() {
    LocalVariablesEntry entry = getSelectionEntry();
    if (entry.getEntryType() == LocalVariablesEntry.LOCAL ||
        entry.getEntryType() == LocalVariablesEntry.ROOT ||
        entry.getEntryType() == LocalVariablesEntry.METHOD )
    return;

    WatchPanel wp = parentPanel.getWatchPanel();
    wp.addToListVar(entry);

  }

  private DefaultMutableTreeNode parent = rootNode;

  public void viewVars() throws Exception {
    // tree 초기화
    //System.out.println("Method viewVars(): start");
    tree.setModel(null);
    localVars.removeAllChildren();
    methodArgs.removeAllChildren();
    rootNode.removeAllChildren();
    treeModel.reload(rootNode);

    localVars = addObject(rootNode, localentry, true);
    methodArgs = addObject(rootNode, methodentry, true);

    currentThread = debuggerProxy.getCurrentThread();
    if ((currentThread == null) || (!currentThread.isSuspended())) {
      if (currentThread != null) {
        System.out.println(" =======>  " + currentThread.getName() + " : " + currentThread.getStatus());
      }
      return;
    }

    //treeModel.insertNodeInto(localVars, rootNode, 0);
    //addObject(localVars,rootNode,true);
    //addObject(methodArgs, rootNode, true);
    //treeModel.insertNodeInto(methodArgs, rootNode, rootNode.getChildCount());

    RemoteStackVariable[] rsv = currentThread.getStackVariables();
    RemoteStackFrame rsf = currentThread.getCurrentFrame();

    // Debug 메시지
    // Debug 정보가 없는 상태에서는 rsv.length == 0 이다.
    //System.out.println("Method viewVars(): Current Thread = " + currentThread.toString());
    //System.out.println("Method viewVars(): Length of rsv = " + rsv.length);
    //System.out.println("Method viewVars(): before each local variables");
    // 각 변수들에 대해
    for (int i = 0; i < rsv.length; i++) {
      // In Scope
      //System.out.println("Method viewVars(): rsv[" + i + "]= " + rsv.toString());
      if (rsv[i].inScope()) {
//        depth = 0;
        RemoteValue rv = rsv[i].getValue();
        // Is Object
        if ((rv != null) && rv.isObject()) {
          // Is Array
          if (rv instanceof RemoteArray) {
            RemoteArray ra = (RemoteArray)rv;

            //DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(rsv[i].getName() + " (" + rsv[i].getType() + ")");
            DefaultMutableTreeNode newNode = null;
            LocalVariablesEntry entry = new LocalVariablesEntry(rsv[i].getName(), null, rsv[i].getType().toString(), null, null,LocalVariablesEntry.ARRAY);
            //newNode = new DefaultMutableTreeNode(entry);

            if (!rsv[i].methodArgument()) {
              //treeModel.insertNodeInto(newNode, localVars, localVars.getChildCount());
              newNode = addObject(localVars, entry, true);
            }
            else {
              //treeModel.insertNodeInto(newNode, methodArgs, methodArgs.getChildCount());
              newNode = addObject(methodArgs,entry,true);
            }
            RemoteValue[] rarv = ra.getElements();
            for (int j = 0; j < rarv.length; j++) {
              // Is Object
              if ((rarv[j] != null) && rarv[j].isObject()) {
                DefaultMutableTreeNode inNode = null;
                LocalVariablesEntry inEntry = null;
                // Is String
                if (rarv[j] instanceof RemoteString) {
                  //inNode = new DefaultMutableTreeNode(rsv[i].getName() + "[" + j + "] : " + rarv[j].typeName() + " = " + rarv[j].description());
                  inEntry = new LocalVariablesEntry(
                      rsv[i].getName() + "[" + j + "]", null, rarv[j].typeName(), rarv[j].description(),(RemoteObject)rarv[j], LocalVariablesEntry.STRING);
                  //inNode = new DefaultMutableTreeNode(inEntry);
                }
                // Not String
                else {
                  //inNode = new DefaultMutableTreeNode(rsv[i].getName() + "[" + j + "] : " + rarv[j].typeName());
                  inEntry = new LocalVariablesEntry(
                      rsv[i].getName() + "[" + j + "]", null, rarv[j].typeName(), null,(RemoteObject)rarv[j], LocalVariablesEntry.OBJECT);
                  //inNode = new DefaultMutableTreeNode(inEntry);
                }
                //treeModel.insertNodeInto(inNode, newNode, newNode.getChildCount());
                inNode = addObject(newNode, inEntry, true);
                parent = inNode;
                // 하위 내용은 현재 보여주지 않고 나중에 tree를 expand할때 보여주기 위해서 우선은
                // empty 객체를 넣어준다.
                String empty = new String("empty");
                addObject(inNode,empty,false);
                //viewVars((RemoteObject)rarv[j]);
              }
              // Not Object
              else {
                //String content = rsv[i].getName() + "[" + j + "] : " + rsv[i].getType() + " = ";
                StringBuffer des = new StringBuffer();
                if (rarv[j] != null) {
                  // Is Char
                  if (rarv[i] instanceof RemoteChar) {
                    //content += "'" + rarv[j].description() + "'";
                    des.append("'" + rarv[j].description() + "'");
                  }
                  // Not Char
                  else
                    //content += rarv[j].description();
                    des.append(rarv[j].description());
                }
                else// content += rarv[j];
                    des.append(rarv[j]);

                LocalVariablesEntry nEntry = new LocalVariablesEntry(
                    rsv[i].getName() + "[" + j + "]", null, rsv[i].getType().toString(), des.toString(),null, LocalVariablesEntry.GENERAL);
                DefaultMutableTreeNode nNode = null; //new DefaultMutableTreeNode(nEntry);
                nNode = addObject(newNode,nEntry,true);

                //treeModel.insertNodeInto(new DefaultMutableTreeNode(content), newNode, newNode.getChildCount());
              }
            }
          }
          // Not Array
          else {
            DefaultMutableTreeNode newNode = null;
            // Is String
            LocalVariablesEntry sEntry= null;
            if (rv instanceof RemoteString) {
              //newNode = new DefaultMutableTreeNode(rsv[i].getName() + " (" + rsv[i].getType() + ") = \"" + rv.description() + "\"");
              sEntry = new LocalVariablesEntry(
                  rsv[i].getName(), null, rsv[i].getType().toString(), "\"" + rv.description()+ "\"", (RemoteObject)rv, LocalVariablesEntry.STRING);
              //newNode = new DefaultMutableTreeNode(sEntry);
            }
            // Not String
            else  {
              //newNode = new DefaultMutableTreeNode(rsv[i].getName() + " (" + rsv[i].getType() + ")");
              sEntry = new LocalVariablesEntry(
                  rsv[i].getName(), null, rsv[i].getType().toString(), null, (RemoteObject)rv,LocalVariablesEntry.OBJECT);
              //newNode = new DefaultMutableTreeNode(nsEntry);
            }
            //treeModel.insertNodeInto(newNode, localVars, localVars.getChildCount());
            newNode = addObject(localVars,sEntry,true);
            parent = newNode;
            depth = 0;
            String empty = new String("empty");
            addObject(newNode, empty, false);
            //viewVars((RemoteObject)rv);
          }
        }
        // Null or Not Object
        else {
          StringBuffer content = new StringBuffer();
          if (rv != null) content.append(rv.description());
          else content.append(rv);
          LocalVariablesEntry mEntry = new LocalVariablesEntry(
            rsv[i].getName(),null,rsv[i].getType().toString(),content.toString(), null,LocalVariablesEntry.GENERAL);
          DefaultMutableTreeNode mNode = null; //new DefaultMutableTreeNode(mEntry);
          mNode = addObject(localVars,mEntry ,true);
          //treeModel.insertNodeInto(new DefaultMutableTreeNode(content.toString()), localVars, localVars.getChildCount());
        }
      }
      // Not in Scope
      else {

        //DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(rsv[i].getName() + " : " + rsv[i].getType() + " = ???", false);
        LocalVariablesEntry newEntry = new LocalVariablesEntry(
            rsv[i].getName(), null, rsv[i].getType().toString(), "???", null,LocalVariablesEntry.GENERAL);
        DefaultMutableTreeNode newNode = null; //new DefaultMutableTreeNode(newEntry);
        if (!rsv[i].methodArgument()) {
          //treeModel.insertNodeInto(newNode, localVars, localVars.getChildCount());
          newNode = addObject(localVars, newEntry, true);
        }
        else {
          //treeModel.insertNodeInto(newNode, methodArgs, methodArgs.getChildCount());
          newNode = addObject(methodArgs,newEntry, true);
        }
      }
    }

    treeModel.reload();
    tree.setModel(treeModel);
    tree.expandPath(new TreePath(localVars.getPath()));
    tree.expandPath(new TreePath(methodArgs.getPath()));
/*
    if (localVars.getChildCount() > 0) {
      DefaultMutableTreeNode tn = (DefaultMutableTreeNode)localVars.getChildAt(0);
      tree.expandPath(new TreePath(tn.getPath()));
    }
    if (methodArgs.getChildCount() > 0) {
      DefaultMutableTreeNode tn = (DefaultMutableTreeNode)methodArgs.getChildAt(0);
      tree.expandPath(new TreePath(tn.getPath()));
    }
*/
    //System.out.println("Method viewVars(): end");
  }

  private int depth;
  private final int MAX_DEPTH = 1;

  /**
   *  객체 데이터에 대한 정보 추출
   *
   *  @param obj RemoteObject
   */
  public void viewVars(RemoteObject obj, DefaultMutableTreeNode parent) throws Exception {
    DefaultMutableTreeNode dmtn = parent;
    RemoteField rf[] = obj.getFields();
//    depth++;
    for (int i = 0; i < rf.length; i++) {
      RemoteValue rv = obj.getFieldValue(rf[i].getName());
      if ((rv != null) && rv.isObject()) {
        if (depth < MAX_DEPTH) {
          // Array 일 경우
          if (rv instanceof RemoteArray) {
            RemoteArray ra = (RemoteArray)rv;

            //DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(rf[i].getName() + " (" + rf[i].getType() + ")");
            LocalVariablesEntry nEntry = new LocalVariablesEntry(rf[i].getName() , null, rf[i].getType().toString(),null,null,LocalVariablesEntry.ARRAY);
            DefaultMutableTreeNode newNode = null; //new DefaultMutableTreeNode(nEntry);
            newNode = addObject(dmtn,nEntry,false);
            //treeModel.insertNodeInto(newNode, dmtn, dmtn.getChildCount());
            RemoteValue[] rarv = ra.getElements();
            for (int j = 0; j < rarv.length; j++) {
              if ((rarv[j] != null) && rarv[j].isObject()) {
                DefaultMutableTreeNode inNode = null;
                LocalVariablesEntry inEntry = null;
                if (rarv[j] instanceof RemoteString) {
                  inEntry = new LocalVariablesEntry(
                      rf[i].getName() + "[" + j + "]",null, rarv[j].typeName() , rarv[j].description(), (RemoteObject)rarv[j] ,LocalVariablesEntry.STRING);
                  //inNode = new DefaultMutableTreeNode(inEntry);
                  //inNode = new DefaultMutableTreeNode(rf[i].getName() + "[" + j + "] : " + rarv[j].typeName() + " = " + rarv[j].description());
                }
                else {
                  inEntry = new LocalVariablesEntry(
                      rf[i].getName() + "[" + j + "]", null, rarv[j].typeName(), null, (RemoteObject)rarv[j],LocalVariablesEntry.OBJECT);
                  //inNode = new DefaultMutableTreeNode(inEntry);
                  //inNode = new DefaultMutableTreeNode(rf[i].getName() + "[" + j + "] : " + rarv[j].typeName());
                }
                inNode = addObject(newNode,inEntry, false);
                //treeModel.insertNodeInto(inNode, newNode, newNode.getChildCount());
                parent = inNode;
                //depth++;
                String empty = new String("empty");
                addObject(inNode, empty, false);
                //viewVars((RemoteObject)rarv[j]);
                //depth--;
              }
              else {
                StringBuffer des = new StringBuffer();
                //StringBuffer content = new StringBuffer();
                //content.append(rf[i].getName());
                //content.append("[");
                //content.append(j);
                //content.append("] = ");

                if (rarv[j] != null) {
                  if (rarv[j] instanceof RemoteChar) {
                    des.append("'" + rarv[j].description() + "'");
                    //content.append("'");
                    //content.append(rarv[j].description());
                    //content.append("'");
                  }
                  else { // content.append(rarv[j].description());
                    des.append(rarv[j].description());
                  }
                }
                else //content.append(rarv[j]);
                  des.append(rarv[j]);

                LocalVariablesEntry aEntry = new LocalVariablesEntry(
                    rf[i].getName() + "[" + j + "]", null, null, des.toString(),null, LocalVariablesEntry.GENERAL);
                DefaultMutableTreeNode aNode = null; new DefaultMutableTreeNode(aEntry);
                aNode = addObject(newNode,aEntry,false);
                //treeModel.insertNodeInto(new DefaultMutableTreeNode(content.toString()), newNode, newNode.getChildCount());
              }
            }
          }
          else {
            DefaultMutableTreeNode newNode = null;
            LocalVariablesEntry nEntry = null;
            if (rv instanceof RemoteString) {
              nEntry = new LocalVariablesEntry(
                  rf[i].getName() , null, rf[i].getType().toString(),"\"" + rv.description() + "\"" ,(RemoteObject)rv,LocalVariablesEntry.STRING);
              //newNode = new DefaultMutableTreeNode(rf[i].getName() + " (" + rf[i].getType() + ") = \"" + rv.description() + "\"");
            }
            else {
              nEntry = new LocalVariablesEntry(
                rf[i].getName() , null, rf[i].getType().toString(), null ,(RemoteObject)rv ,LocalVariablesEntry.OBJECT);
            //newNode = new DefaultMutableTreeNode(rf[i].getName() + " (" + rf[i].getType() + ")");
            }
            //newNode = new DefaultMutableTreeNode(nEntry);
            newNode = addObject(dmtn,nEntry,false);

            //treeModel.insertNodeInto(newNode, dmtn, dmtn.getChildCount());
            parent = newNode;
            //depth++;
            String empty = new String("empty");
            addObject(newNode,empty,false);
            //viewVars((RemoteObject)rv);
            //depth--;
          }
        }
        else {
          LocalVariablesEntry entry = new LocalVariablesEntry(
            rf[i].getName(), null, rf[i].getType().toString(), "{...}",null,LocalVariablesEntry.GENERAL);

          //StringBuffer content = new StringBuffer();
          //content.append(rf[i].getName());
          //content.append(" : ");
          //content.append(rf[i].getType());
          //content.append(" = {...}");
          //DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(content.toString());
          DefaultMutableTreeNode newNode = null; //new DefaultMutableTreeNode(entry);
          //treeModel.insertNodeInto(newNode, dmtn, dmtn.getChildCount());
          newNode = addObject(dmtn,entry,false);
        }
      }
      else {
        StringBuffer des = new StringBuffer();
        //StringBuffer content = new StringBuffer();
        //content.append(rf[i].getName());
        //content.append(" : ");
        //content.append(rf[i].getType());
        //content.append(" = ");
        if (rv != null) //content.append(rv.description());//obj.getField(i);
          des.append(rv.description());
        else
          des.append(rv); //content.append(rv);

        LocalVariablesEntry entry = new LocalVariablesEntry(
            rf[i].getName(), null, rf[i].getType().toString(),des.toString() ,null,LocalVariablesEntry.GENERAL);
        DefaultMutableTreeNode node = null; //new DefaultMutableTreeNode(entry);
        node = addObject(dmtn,entry,false);
//        treeModel.insertNodeInto(new DefaultMutableTreeNode(content.toString()), dmtn, dmtn.getChildCount());
      }
    }
//    depth--;
  }

  class ActionHandler implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String cmd = e.getActionCommand();

      if(cmd.equals("ADD")) {
        addToList();
      }
    }
  }

  class MyTreeWillExpandListener implements TreeWillExpandListener{
		public void treeWillExpand(TreeExpansionEvent e) {
      DefaultMutableTreeNode node;
			node = (DefaultMutableTreeNode)(e.getPath().getLastPathComponent());

      if (node == null) return;
      LocalVariablesEntry entry = (LocalVariablesEntry)node.getUserObject();

      if(entry.getEntryType() == LocalVariablesEntry.OBJECT ||
                      entry.getEntryType() == LocalVariablesEntry.STRING) {
        node.removeAllChildren();
        treeModel.reload(node);
        try {
          viewVars(entry.getRemoteObject(), node);
        } catch (Exception ex) {
          System.out.println("LocalVariablesPanel.java : At TreeWillExpandListener");
        }
      }
		}
		public void treeWillCollapse(TreeExpansionEvent e) {
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

