/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co.
 * All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/browser/sourcebrowser/SourceBrowser.java,v 1.26 1999/08/31 12:25:33 multipia Exp $
 * $Revision: 1.26 $
 * $History: SourceBrowser.java $
 *
 *
 */
package com.antsoft.ant.browser.sourcebrowser;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.util.Vector;
import java.util.Hashtable;

import com.antsoft.ant.manager.projectmanager.*;
import com.antsoft.ant.util.*;
import com.antsoft.ant.codecontext.codeeditor.EventContent;


/**
 *  class SourceBrowser
 *
 *  @author Jinwoo Baek
 *  @author kim sang kyun
 */
public class SourceBrowser extends JPanel implements ActionListener {
	/** Tree형태로 분석된 소스를 보여준다. */
	private JTree tree = null;

	/** Project Explorer에 Embed 된다. */
	private ProjectExplorer pe = null;

	/** tree's root node */
	private DefaultMutableTreeNode rootNode;
  private DefaultMutableTreeNode importRootNode;


	/** manager tree model */

	private DefaultTreeModel treeModel;

	/** tree selection model */
	private TreeSelectionModel treeSelectionModel;

  private JComboBox combo;
  private DefaultComboBoxModel comboM;

	/** TreeCell Renderer */
	private SourceBrowserTreeCellRenderer renderer = new SourceBrowserTreeCellRenderer();

  /** 현재 보여줄 클래스 노드 */
  private DefaultMutableTreeNode currentNode = null;

	/** scroller */
	private JScrollPane scrollPane = null;
	private JViewport vp = null;

  /** Popup mene */
  private JPopupMenu classPopup;
  private JMenuItem addField;
  private JMenuItem addMain;
  private JMenuItem addMethod;
  private JMenuItem addGetSet;
  private JMenuItem addHandler;

  private JLabel sourceNameLbl = new JLabel("     ");

  private JButton addFieldBtn, addMainBtn, addMethodBtn, addGetSetBtn, addHandlerBtn;
  private boolean isTreeUpdate = false;
  private boolean isComboUpdate = false;

  private int lastDividerLocation = 0;

  private Hashtable nodeHash =new Hashtable();

	/**
	 * Constructor
	 */

	public SourceBrowser(ProjectExplorer pe) {

		this.pe = pe;
		this.setPreferredSize(new Dimension(250, 0));
		this.setMaximumSize(new Dimension(2000, 0));
		this.setMinimumSize(new Dimension(0, 0));
		this.setLayout(new BorderLayout());
    setBorder(BorderList.unselLineBorder);

    if(!pe.isFilesTab()){
      classPopup = new JPopupMenu();

      addField = new JMenuItem("Add Field", ImageList.addfieldIcon);
      addMain = new JMenuItem("Add Main Method", ImageList.addmainIcon);
      addMethod = new JMenuItem("Add Method", ImageList.addmethodIcon);
      addGetSet= new JMenuItem("Add GetSet Method", ImageList.addgetsetIcon);
      addHandler = new JMenuItem("Add EventHandler", ImageList.addeventIcon);

      comboM = new DefaultComboBoxModel();
      combo = new JComboBox(comboM);
      combo.setMinimumSize(new Dimension(1,1));
      combo.setToolTipText("Selected Class");
      combo.addItemListener(new ComboHandler());
      combo.setRenderer(new ComboRenderer());

      addFieldBtn = new JButton(ImageList.addfieldIcon);
      addFieldBtn.setMargin(new Insets(0,0,0,0));
      addFieldBtn.setActionCommand("ADD_FIELD_B");
      addFieldBtn.addActionListener(this);
      addFieldBtn.setToolTipText("Add Field");

      addMainBtn = new JButton(ImageList.addmainIcon);
      addMainBtn.setMargin(new Insets(0,0,0,0));
      addMainBtn.setActionCommand("ADD_MAIN_B");
      addMainBtn.addActionListener(this);
      addMainBtn.setToolTipText("Add Main Method");

      addMethodBtn = new JButton(ImageList.addmethodIcon);
      addMethodBtn.setMargin(new Insets(0,0,0,0));
      addMethodBtn.setActionCommand("ADD_METHOD_B");
      addMethodBtn.addActionListener(this);
      addMethodBtn.setToolTipText("Add Method");

      addGetSetBtn = new JButton(ImageList.addgetsetIcon);
      addGetSetBtn.setMargin(new Insets(0,0,0,0));
      addGetSetBtn.setActionCommand("ADD_GETSET_B");
      addGetSetBtn.addActionListener(this);
      addGetSetBtn.setToolTipText("Add Getter, Setter Method");

      addHandlerBtn = new JButton(ImageList.addeventIcon);
      addHandlerBtn.setMargin(new Insets(0,0,0,0));
      addHandlerBtn.setActionCommand("ADD_HANDLER_B");
      addHandlerBtn.addActionListener(this);
      addHandlerBtn.setToolTipText("Add Event Handler Inner Class");


      Box btnBox = Box.createHorizontalBox();
      btnBox.add(addFieldBtn);
      btnBox.add(addMainBtn);
      btnBox.add(addMethodBtn);
      btnBox.add(addGetSetBtn);
      btnBox.add(addHandlerBtn);

      enableBtns(false);

      JPanel comboP = new JPanel();
      comboP.add(combo);

      JPanel topP = new JPanel(new BorderLayout(0,2));
      topP.add(comboP,BorderLayout.WEST);
      //topP.add(btnBoxP,BorderLayout.CENTER);
      topP.add(btnBox,BorderLayout.CENTER);
      add(topP, BorderLayout.NORTH);

      addField.setActionCommand("ADD_FIELD");
      addField.addActionListener(this);
      addMain.setActionCommand("ADD_MAIN");
      addMain.addActionListener(this);
      addMethod.setActionCommand("ADD_METHOD");
      addMethod.addActionListener(this);
      addGetSet.setActionCommand("ADD_GETSET");
      addGetSet.addActionListener(this);
      addHandler.setActionCommand("ADD_HANDLER");
      addHandler.addActionListener(this);
      classPopup.add(addField);
      classPopup.add(addMain);
      classPopup.add(addMethod);
      classPopup.add(addGetSet);
      classPopup.add(addHandler);
    }

		rootNode = new DefaultMutableTreeNode( new EventContent("", EventContent.FILEROOTNODE));
		importRootNode = new DefaultMutableTreeNode( new EventContent("imports", EventContent.IMPORTROOTNODE));
    rootNode.add(importRootNode);
		treeModel = new DefaultTreeModel(rootNode);

    tree = new JTree(treeModel);
    tree.setDoubleBuffered(true);
    tree.putClientProperty("JTree.lineStyle", "Angled");
		tree.setEditable(false);
		treeSelectionModel = tree.getSelectionModel();
		treeSelectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setCellRenderer(renderer);
		tree.setFont(FontList.treeFont);

		// tree에서 item을 선택하면 소스에 적당한 위치로 이동하도록 하는 것이 좋겠쥐?
    tree.addMouseListener(new TreeMouseEventHandler());
    tree.addMouseMotionListener( new MouseMotionHandler() );
    tree.addKeyListener(new KeyHandler());
    // Java File Name인 루트를 안보이게 하려면 아래를 Uncomment
    tree.setRootVisible(false);
    tree.setShowsRootHandles( true );

		scrollPane = new JScrollPane(tree);
		vp = scrollPane.getViewport();
		add(scrollPane, BorderLayout.CENTER);
	}

  public void setSelLineBorder(){
    setBorder(BorderList.selLineBorder);
  }

  public void clearBorder(){
    setBorder(BorderList.unselLineBorder);
  }

  public void setLastDividerLocation(int newLoc){
    lastDividerLocation = newLoc;
  }

  public int getLastDividerLocation(){
    return lastDividerLocation;
  }


  private void enableBtns(boolean flag){
    if(pe.isFilesTab()) return;

    addFieldBtn.setEnabled(flag);
    addMainBtn.setEnabled(flag);
    addMethodBtn.setEnabled(flag);
    addGetSetBtn.setEnabled(flag);
    addHandlerBtn.setEnabled(flag);
  }


  private void remindPE(){
    pe.setFocusedComponent(ProjectExplorer.SOURCE_BROWSER);
  }

  //tree의 왼쪽 위가 보이도록 한다
  public void setPositionToLeftTop(){
    vp.setViewPosition(new Point(0, 0));
  }

  public void actionPerformed(ActionEvent evt) {
    String cmd = evt.getActionCommand();

    if(evt.getSource() instanceof JButton){
       if( pe == null || comboM.getSelectedItem() == null) return;
    }
    else {
       if( pe == null || currentNode == null) return;
    }

    boolean isInner = false;
    String key = "";
    if(currentNode != null){
      EventContent sbte = (EventContent)currentNode.getUserObject();
      isInner = ( sbte.getContentType() == EventContent.INNER );
      key = sbte.getHashtableKey();
    }

    if (cmd.equals("ADD_FIELD"))	pe.addField(key, isInner );
    else if (cmd.equals("ADD_MAIN")) pe.addField(key, isInner );
    else if (cmd.equals("ADD_METHOD")) pe.addMethod(key, isInner );
    else if (cmd.equals("ADD_GETSET"))	pe.addGetSetMethod(tree, currentNode, isInner );
  	else if (cmd.equals("ADD_HANDLER"))	pe.addHandler(key, isInner );
    else if (cmd.equals("ADD_FIELD_B")) pe.addField((String)comboM.getSelectedItem(), isInner);
    else if (cmd.equals("ADD_MAIN_B")) pe.addMain((String)comboM.getSelectedItem(), isInner );
    else if (cmd.equals("ADD_METHOD_B")) pe.addMethod((String)comboM.getSelectedItem(), isInner );
  	else if (cmd.equals("ADD_HANDLER_B"))  pe.addHandler((String)comboM.getSelectedItem(), isInner);
    else if (cmd.equals("ADD_GETSET_B"))  {
      for(int i=0; i<rootNode.getChildCount(); i++){
        DefaultMutableTreeNode child = (DefaultMutableTreeNode)rootNode.getChildAt(i);
        EventContent ec = (EventContent)child.getUserObject();
        if(ec.getHashtableKey().equals((String)comboM.getSelectedItem())){
          pe.addGetSetMethod(tree, child, isInner );
          break;
        }
      }
    }
  }


  Vector classNodes = new Vector();
  private void addTreeNode(DefaultMutableTreeNode parent, EventContent childObj){

    int contentType = childObj.getContentType();
    int childCount = parent.getChildCount();

    int i=0;
    OUT:
    for(; i<childCount; i++){
      DefaultMutableTreeNode childAt = (DefaultMutableTreeNode)parent.getChildAt(i);
      EventContent childAtObj = (EventContent)childAt.getUserObject();
      int childContentType = childAtObj.getContentType();
      switch(contentType){
        case EventContent.OPER :
             if(childContentType == EventContent.ATTR) break OUT;
             else if(childContentType == EventContent.INNER) continue;
             break;
        case EventContent.ATTR :
             if(childContentType == EventContent.OPER) continue;
             else if(childContentType == EventContent.INNER) continue;
             break;
        case EventContent.INNER :
             if(childContentType == EventContent.CLASS) continue;
             else if(childContentType == EventContent.OPER ||
                     childContentType == EventContent.ATTR) break OUT;
             break;
        case EventContent.PACKAGE :
             if(childContentType != EventContent.IMPORTROOTNODE) continue;
             else break OUT;
      }

      if(childAtObj.getContent().compareTo(childObj.getContent()) < 0) continue;
      else if(childAtObj.getContent().compareTo(childObj.getContent()) == 0) return;
      else break;
    }

    DefaultMutableTreeNode toInsert = new  DefaultMutableTreeNode(childObj);
    treeModel.insertNodeInto(toInsert, parent, i);

    if(contentType==EventContent.CLASS){
      nodeHash.put(childObj.getHashtableKey() , toInsert);
      classNodes.addElement(toInsert);
    }
    else if(contentType==EventContent.INNER){
      nodeHash.put(childObj.getHashtableKey(), toInsert);
    }
  }


	/**
	 *  package 를 나타내는 node를 추가한다.
	 *
	 *  @param packageNode 추가할 노드
	 */
	public void addPackageNode(EventContent packageNode) {
		if( packageNode == null ) return;
    addTreeNode(rootNode, packageNode);
	}

	/**
	 *  class 를 나타내는 node를 추가한다.
	 *
	 *  @param classNode 추가할 노드
	 */
	public void addClassNode(EventContent classNode) {

		if (classNode == null) return;
    if( !pe.isFilesTab() && comboM.getIndexOf(classNode.getHashtableKey())==-1) comboM.addElement(classNode.getHashtableKey());
    addTreeNode(rootNode, classNode);
	}

	/**
	 *  interface 를 나타내는 node를 추가한다.
	 *
	 *  @param interfaceNode 추가할 노드
	 */
	public void addInterfaceNode(EventContent interfaceNode) {
		if (interfaceNode == null) return;
    addTreeNode(rootNode, interfaceNode);
	}

	/**
	 *  attribute 를 나타내는 node를 추가한다.
	 *
	 *  @param attNode 추가할 노드
	 */
	public void addAttributeNode(EventContent attrNode) {
		if (attrNode == null) return;
    DefaultMutableTreeNode parent = (DefaultMutableTreeNode)nodeHash.get(attrNode.getParent().getHashtableKey());
    if(parent == null) {
      EventContent pp = attrNode.getParent();
      if(pp.getContentType() == EventContent.CLASS) addClassNode(pp);
      else if(pp.getContentType() == EventContent.INNER) addInnerClassNode(pp);
      else addInterfaceNode(pp);
      parent = (DefaultMutableTreeNode)nodeHash.get(attrNode.getParent().getHashtableKey());
    }
    if(parent != null) addTreeNode(parent, attrNode);
	}

	/**
	 *  operation 을 나타내는 node를 추가한다.
	 *
	 *  @param operNode 추가할 노드
	 */
	public void addOperationNode(EventContent operNode) {

		if (operNode == null) return;
    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) nodeHash.get(operNode.getParent().getHashtableKey());
    if(parent == null) {
      EventContent pp = operNode.getParent();
      if(pp.getContentType() == EventContent.CLASS) addClassNode(pp);
      else if(pp.getContentType() == EventContent.INNER) addInnerClassNode(pp);
      else addInterfaceNode(pp);

      parent = (DefaultMutableTreeNode)nodeHash.get(operNode.getParent().getHashtableKey());
    }

    if(parent != null) addTreeNode(parent, operNode);
	}

	/**
	 *  inner class 를 나타내는 node를 추가한다.
	 *
	 *  @param innerNode 추가할 노드
	 */
	public void addInnerClassNode(EventContent innerNode) {
		if (innerNode == null) return;
    DefaultMutableTreeNode parent = (DefaultMutableTreeNode)nodeHash.get(innerNode.getParent().getHashtableKey());
    if(parent == null) {
      EventContent pp = innerNode.getParent();
      if(pp.getContentType() == EventContent.CLASS) addClassNode(pp);
      else if(pp.getContentType() == EventContent.INNER) addInnerClassNode(pp);
      else addInterfaceNode(pp);

      parent = (DefaultMutableTreeNode)nodeHash.get(innerNode.getParent().getHashtableKey());
    }

    if( !pe.isFilesTab() && comboM.getIndexOf(innerNode.getHashtableKey())==-1) comboM.addElement(innerNode.getHashtableKey());
    if(parent != null) addTreeNode(parent, innerNode);
	}

	/**
	 *  import 를 나타내는 node를 추가한다.
	 *
	 *  @param importNode 추가할 노드
	 */
	public void addImportNode(EventContent importNode) {
		if (importNode == null) return;
    addTreeNode(importRootNode, importNode);
	}


  public void clearClassNodes(){
    if(!pe.isFilesTab()) comboM.removeAllElements();
  }

	public void initViewport() {
		vp.setViewPosition(new Point(0, 0));
	}

	/**
	 *  Tree에서 노드를 삭제한다.
	 *
	 *  @param item 삭제할 item
	 */
	public void removeNode(int type, EventContent content) {
		for (int i = 0; i < tree.getRowCount(); i++) {
			TreePath tp = tree.getPathForRow(i);
			DefaultMutableTreeNode tn = (DefaultMutableTreeNode)tp.getLastPathComponent();
			Object obj = tn.getUserObject();
			if (obj instanceof EventContent) {
				EventContent c = (EventContent)obj;
				if (content.equals(c)) {
          treeModel.removeNodeFromParent(tn);
          int entryType = content.getContentType();
          if(entryType == EventContent.CLASS || entryType == EventContent.INNER){
            if(!pe.isFilesTab()) comboM.removeElement(content);
            nodeHash.remove(content.getHashtableKey());
          }
					break;
				}
			}
		}
  }

  /**
   * reload
   */
  public void reload(){
    int width = getSize().width;

    if(pe.isFilesTab()) return;

    int btnWidth = addFieldBtn.getWidth() * 5;
    combo.setPreferredSize(new Dimension(width - btnWidth - 12, combo.getHeight()));
    enableBtns(true); 

  }

  /**
   * Tree상의 classNode들을 expand 시킨다. sourcebrowser의 내용이 바뀔때 바다
   *  호출 되어야 한다
   */ 
  public void expandClassNodes(){
    for(int i=0; i<classNodes.size(); i++){
      DefaultMutableTreeNode classNode = (DefaultMutableTreeNode)classNodes.elementAt(i);
      tree.expandPath(new TreePath(classNode.getPath()));
    }

    tree.expandPath(new TreePath( importRootNode.getPath() ));
    classNodes.removeAllElements();
  }

  /**
   * sourceBrowser의 내용이 전부 바뀔때 (다른 파일 선택) 호출된다.
   * 만들어진 treeModel을 tree에 설정하고, class node들을 expand 시킨다
   */
  public void setTreeModel(){
    tree.setModel(treeModel);
    expandClassNodes();
  }

  public void clear(){

    removeAll();
    tree.setModel(null);

    if(pe.isFilesTab()) return;
    
    if(comboM.getSize() > 0)  comboM.removeAllElements();
    enableBtns(false);
  }

	/**
	 *  Tree에서 모든 노드를 삭제한다.
	 */
	public void removeAll() {
    if(rootNode.getChildCount() > 0){
      importRootNode.removeAllChildren();
  		rootNode.removeAllChildren();
      rootNode.add(importRootNode);


      nodeHash.clear();
      treeModel.reload();
    }
	}

  class TreeMouseEventHandler extends MouseAdapter {
    public void mouseClicked(MouseEvent evt) {
      remindPE();
    }

    public void mouseReleased(MouseEvent evt) {
      if (evt.isPopupTrigger()) {
        tree.setSelectionPath(tree.getPathForLocation(evt.getX(), evt.getY()));
        TreePath path = tree.getSelectionPath();

        if (path != null) {
          DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)path.getLastPathComponent();
          currentNode = dmtn;
          Object obj = dmtn.getUserObject();
          if (obj instanceof EventContent) {
            EventContent sbte = (EventContent)obj;
            int type = sbte.getContentType();
            if (type == EventContent.CLASS || type == EventContent.INNER) {
              classPopup.show(tree, evt.getX(), evt.getY());
            }
          }
        }
      }
    }

    public void mousePressed(MouseEvent evt){
      if(isComboUpdate) return;

      if(tree.hasFocus()){
        TreePath tp = tree.getPathForLocation(evt.getX(), evt.getY());
        if(tp == null) return;

        currentNode = (DefaultMutableTreeNode)tp.getLastPathComponent();
        EventContent sbte = (EventContent)currentNode.getUserObject();

        if (!currentNode.isRoot() && currentNode != importRootNode) {
          // 상위의 class name과 함께 보낸다.
          String parentContent = null;
          String childContent = sbte.getContent();

          String key = sbte.getHashtableKey();
          if(sbte.getContentType() == EventContent.IMPORT) parentContent = null;
          else if(sbte.getContentType() == EventContent.PACKAGE) parentContent = null;
          else if(key.indexOf(".") != -1){
            parentContent = key.substring(0, key.lastIndexOf("."));
          }

          pe.sourceBrowserSelection(parentContent, childContent);

          if(!pe.isFilesTab() && comboM.getIndexOf(sbte.getHashtableKey()) != -1) {
             isTreeUpdate = true;
             comboM.setSelectedItem(sbte.getHashtableKey());
             isTreeUpdate = false;
          }
        }
      }
    }
  }


	/**
	 * Source Browser내에서 화면 폭 때문에 보이지 않는 멤버들을 보여주기 위해서 툴팁을
	 * 이용한다.
	 * 좀더 세련되게 보이기 위해서는 원래의 위치에 덮어 쓰는 것이 좋을 것이다.
	 */
	class MouseMotionHandler extends MouseMotionAdapter {
		public void mouseMoved( MouseEvent e ) {
			TreePath path = tree.getClosestPathForLocation( e.getX(), e.getY() );
			if ( path != null ) {
				DefaultMutableTreeNode tn = (DefaultMutableTreeNode)path.getLastPathComponent();
				Object obj = tn.getUserObject();
				if (obj instanceof EventContent) {
					EventContent sbte = (EventContent)obj;
					tree.setToolTipText( sbte.getContent() );
				}
			} else
				tree.setToolTipText( "" );
		}
	}

  class ComboHandler implements ItemListener {
    private String prevSel = "";

    public void itemStateChanged(ItemEvent e){
      if(isTreeUpdate) return;
      String selItem = (String)comboM.getSelectedItem();
      if(selItem == null) return;

      if(prevSel.equals(selItem)) return;
      else prevSel = selItem;

      DefaultMutableTreeNode sameNode = (DefaultMutableTreeNode)nodeHash.get(selItem);
      if(sameNode != null){
        isComboUpdate = true;
        tree.setSelectionPath(new TreePath(sameNode.getPath()));
        isComboUpdate = false;
        pe.sourceBrowserSelection(null, sameNode.toString());
      }
    }
  }

  class KeyHandler extends KeyAdapter{
    public void keyPressed(KeyEvent e){
      if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
        if(classPopup != null && classPopup.isVisible()) classPopup.setVisible(false);
      }
    }
  }

  class ComboRenderer extends DefaultListCellRenderer {
    private JList list;
    public ComboRenderer(){
      setOpaque(true);
      setFont(FontList.treeFont);
    }

    public Component getListCellRendererComponent(
        JList list,
      	Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus)
    {
      if(list == null) this.list = list;

      if (isSelected) {
          setBackground(ColorList.darkBlue);
          setForeground(Color.white);
      }
      else {
          setBackground(Color.white);
          setForeground(Color.black);
      }

      if (value instanceof Icon) {
          setIcon((Icon)value);
      }
      else {
          setText((value == null) ? "" : value.toString());
      }

      setEnabled(list.isEnabled());
      setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);

      if(list.getSelectedValue() != null)
      list.setToolTipText(list.getSelectedValue().toString());

      return this;
    }
  }
}


