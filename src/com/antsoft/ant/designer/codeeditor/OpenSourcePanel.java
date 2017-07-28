/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/codeeditor/OpenSourcePanel.java,v 1.18 1999/08/24 08:15:21 itree Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.18 $
 * $History: OpenSourcePanel.java $
 *
 * *****************  Version 2  *****************
 * User: Remember     Date: 99-05-26   Time: 6:11p
 * Updated in $/AntIDE/source/ant/designer/codeeditor
 */
package com.antsoft.ant.designer.codeeditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

import com.antsoft.ant.manager.projectmanager.ProjectPanelTreeNode;
import com.antsoft.ant.manager.projectmanager.ProjectExplorer;
import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.util.AntLineBorder;
import com.antsoft.ant.main.MainFrame;
import com.antsoft.ant.util.ImageList;
import com.antsoft.ant.manager.projectmanager.ProjectFileEntry;
/**
 * 현재 오픈되어 있는 파일들을 갖는다
 *
 * @author kim sang kyun
 */
public class OpenSourcePanel extends JPanel implements ActionListener{

  private Hashtable sourceNodes = new Hashtable(15);
  private ProjectExplorer pe;
  private Font f;
  private JButton currB;//, leftB, rightB;
  private Color selC = new Color(0, 0, 31);
  private JPanel listP;
  private JPanel listPanel;
//  private JScrollPane pane;
  //itree

  private JPopupMenu fileBtnPopup = new JPopupMenu();
  private JMenuItem m_close = new JMenuItem("Close");
  private JMenuItem m_closeAll = new JMenuItem("Close All");
  private JMenuItem m_save = new JMenuItem("Save");
  private JMenuItem m_saveAs = new JMenuItem("Save As...");
  private JMenuItem m_make = new JMenuItem("Make");

  private MouseListener buttonHandlerForEditor;

  private MainFrame mainfrm;
  private int btnWidth = 120;
  private int btnMax = 120;

  public OpenSourcePanel(ProjectExplorer pe, MainFrame frm){
    setLayout(new BorderLayout());
    this.pe = pe;
    mainfrm = frm;
    listP = new JPanel();
    listP.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

    listPanel = new JPanel(new BorderLayout());
    listPanel.add(listP,BorderLayout.CENTER);
    JButton b = new JButton();
    f = new Font(b.getFont().getName(), Font.PLAIN, 12);
    add(listPanel, BorderLayout.CENTER);
    m_close.setFont(f);
    m_closeAll.setFont(f);
    m_make.setFont(f);
    m_save.setFont(f);
    m_saveAs.setFont(f);



    fileBtnPopup.add(m_make);
    fileBtnPopup.addSeparator();
    fileBtnPopup.add(m_save);
    fileBtnPopup.add(m_saveAs);
    fileBtnPopup.add(m_close);
    fileBtnPopup.add(m_closeAll);

    buttonHandlerForEditor = new ButtonMouseEventHandler();

    m_close.addActionListener(this);
    m_closeAll.addActionListener(this);
    m_make.addActionListener(this);
    m_save.addActionListener(this);
    m_saveAs.addActionListener(this);

  }

  private void setSelectedB(JButton b, boolean isSelected){
    if(isSelected){
      b.setForeground(Color.black);
      b.setBorder(BorderList.loweredBorder);
      b.setBackground(new Color(225,225,225));
    }
    else{
      b.setForeground(Color.black);
      b.setBorder(new AntLineBorder(Color.gray, 1, false, true, true, true));
      b.setBackground(Color.lightGray);
    }
  }

  public void notifyModifiedSource(boolean modified) {

    if(currB == null)
      return;

    if(modified) {
        currB.setIcon(ImageList.redIcon);
    }
    else {
        currB.setIcon(ImageList.yellowIcon);
    }
  }

  public void notifyFileOpen(ProjectPanelTreeNode node){

    String name = node.getUseObject().toString();

    if(sourceNodes.get(name) != null){
      int count = listP.getComponentCount();
      for(int i=0; i<count; i++){
        Component c = listP.getComponent(i);
        if(c instanceof JButton){
          JButton b = (JButton)c;

          if(b.getText().equals(name)){
            if(currB != null) setSelectedB(currB, false);

            currB = b;
            setSelectedB(currB, true);
            break;
          }
        }
      }
    }
    else{

      if(currB != null) setSelectedB(currB, false);

      sourceNodes.put(name, node);
      currB = new JButton(name, ImageList.yellowIcon);
      currB.setFont(f);
      currB.setMargin(new Insets(0, 0, 0, 0));

      if ( (listPanel.getWidth()-20) < ((listP.getComponentCount()+1)*btnWidth) ) {
        btnWidth =  (int)((listPanel.getWidth()-20)/(listP.getComponentCount()+1));

        currB.setPreferredSize(new Dimension(btnWidth , 21));
        currB.setToolTipText(" " + name + " ");
        currB.addActionListener(this);
        currB.addMouseListener(buttonHandlerForEditor);

        setSelectedB(currB, true);
        Component [] existComps = listP.getComponents();
        if(existComps == null) listP.add(currB);
        else{
          listP.removeAll();
          listP.add(currB);

          for(int i=0; i<existComps.length; i++){
            JButton tb = (JButton)existComps[i];
            tb.setPreferredSize(new Dimension(btnWidth,21));
            listP.add(tb);
          }
        }
        listP.doLayout();

      } else {
        currB.setPreferredSize(new Dimension(btnWidth , 21));
        currB.setToolTipText(" " + name + " ");
        currB.addActionListener(this);
        currB.addMouseListener(buttonHandlerForEditor);

        setSelectedB(currB, true);
        Component [] existComps = listP.getComponents();
        if(existComps == null) listP.add(currB);
        else{
          listP.removeAll();
          listP.add(currB);

          for(int i=0; i<existComps.length; i++){
            listP.add(existComps[i]);
          }
        }
        listP.doLayout();
      }
    }
  }

  public void actionPerformed(ActionEvent e){
    if(e.getSource() == m_close) {
      if (listP.getComponentCount() == 1)
        mainfrm.fileClose();
      else
        mainfrm.closeOpenFile();
    }
    else if(e.getSource() == m_closeAll) {
      mainfrm.closeAllOpenFile();
    }

    else if(e.getSource() == m_make) {
      mainfrm.make();
    }
    else if(e.getSource() == m_save) {
      mainfrm.saveCurrent();
    }
    else if(e.getSource() == m_saveAs) {
      mainfrm.saveAsCurrent();
    }
    /*
    else{
      if(currB != null) setSelectedB(currB, false);

      JButton b = (JButton)e.getSource();
      currB = b;
      setSelectedB(currB, true);

      ProjectPanelTreeNode node = (ProjectPanelTreeNode)sourceNodes.get(b.getText());
      btnSelect(node);
    } */
  }
  /*
  public void btnSelect(ProjectPanelTreeNode node) {
    if(node != null) {
        System.out.println("open source panel : btnSelect");
        pe.openSourceHappened(this, node);
    }
  } */

  public void unPressedButton() {
    if (currB != null)
      setSelectedB(currB,false);
  }
  public void closeAllOpenFile() {
    sourceNodes.clear();
    btnWidth = btnMax;
    listP.removeAll();
    listP.doLayout();
    listP.repaint();
  }
  public void fileClosed(ProjectPanelTreeNode node){

    String name = node.getUseObject().toString();
    sourceNodes.remove(name);

    if (btnWidth >= btnMax) {
      int count = listP.getComponentCount();
      for(int i=0; i<count; i++){
        Component c = listP.getComponent(i);
        if(c instanceof JButton){
           JButton b = (JButton)c;

          if(b.getText().equals(name)){
            listP.remove(i);
            listP.repaint();
            break;
          }
        }
      }
    } else {
      btnWidth =(int)((listP.getWidth() -20)/(listP.getComponentCount()-1));

      if (btnWidth > btnMax)
        btnWidth=btnMax;

      int count = listP.getComponentCount();
      for(int i=0; i<count; i++){
        Component c = listP.getComponent(i);
        if(c instanceof JButton){
          JButton b = (JButton)c;
          if(b.getText().equals(name)) {
            listP.remove(b);
            listP.repaint();
            i--; count--;
          }
          else
            b.setPreferredSize(new Dimension(btnWidth,21));
          listP.doLayout();
        }
      }
      listP.repaint();
    }
  }
  private void btnRemove(String btnName) {
    if (btnWidth >= btnMax) {
      int count = listP.getComponentCount();
      for(int i=0; i<count; i++){
        Component c = listP.getComponent(i);
        if(c instanceof JButton){
           JButton b = (JButton)c;

          if(b.getText().equals(btnName)){
            listP.remove(i);
            listP.repaint();
            break;
          }
        }
      }
    } else {
      btnWidth =(int)((listP.getWidth() -20)/(listP.getComponentCount()-1));

      if (btnWidth > btnMax)
        btnWidth=btnMax;

      int count = listP.getComponentCount();
      for(int i=0; i<count; i++){
        Component c = listP.getComponent(i);
        if(c instanceof JButton){
          JButton b = (JButton)c;
          if(b.getText().equals(btnName)) {
            listP.remove(b);
            listP.repaint();
            i--; count--;
          }
          else
            b.setPreferredSize(new Dimension(btnWidth,21));
          listP.doLayout();
        }
      }
      listP.repaint();
    }

  }
  public void directoryClose(ProjectPanelTreeNode node) {
    Enumeration enum = sourceNodes.elements();
    int i=0;
    System.out.println(node.getUseObject().toString());
    while(enum.hasMoreElements()) {
      i++;
      System.out.println(" Node : " + i);
      ProjectPanelTreeNode btnNode = (ProjectPanelTreeNode)enum.nextElement();
      System.out.println(btnNode.getUserObject().toString());
      System.out.println(node.isNodeDescendant(btnNode));
      if (node.isNodeDescendant(btnNode)) {
        System.out.println(btnNode.getUserObject().toString());
        btnRemove(btnNode.getUserObject().toString());
      }
    }

  }

  public ProjectPanelTreeNode getNextSource() {
    Enumeration enum = sourceNodes.elements();
    if (!enum.hasMoreElements()) {
      return null;
    }

    return (ProjectPanelTreeNode)enum.nextElement();
  }

  /** FileButton MouseEventHandelr Inner Class */
  class ButtonMouseEventHandler extends MouseAdapter{
    public void mouseReleased(MouseEvent evt) {
      if (evt.isPopupTrigger()) {
        int popupHeight = 95;

        fileBtnPopup.show(currB, evt.getX(), (evt.getY()- popupHeight));
        fileBtnPopup.setSelected(m_close);
      }
    }

    public void mousePressed(MouseEvent evt) {
      JButton b = (JButton)evt.getSource();
      // 현재 선택된 document를 다시 열때 그냥 return;
      if(currB == b)
        return;


      if(currB != null) setSelectedB(currB, false);

      currB = b;
      setSelectedB(currB, true);

      ProjectPanelTreeNode node = (ProjectPanelTreeNode)sourceNodes.get(b.getText());
      if(node != null) {
        pe.openSourceHappened(this, node);
      }
    }
  }
}
