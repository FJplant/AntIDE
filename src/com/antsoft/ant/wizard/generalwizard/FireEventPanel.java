/*
 * $Id: FireEventPanel.java,v 1.2 1999/08/19 05:25:07 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.2 $
 */
package com.antsoft.ant.wizard.generalwizard;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.*;
import com.antsoft.ant.util.*;

public class FireEventPanel extends JPanel {
  JScrollPane eventPane;
  BlackTitledBorder border = new BlackTitledBorder("Select events to fire");
  //JLabel lbl1 = new JLabel("Select events to fire.");

  JCheckBox action = new JCheckBox("Action");
  JCheckBox adjustment = new JCheckBox("Adjustment");
  JCheckBox component = new JCheckBox("Component");
  JCheckBox container = new JCheckBox("Container");
  JCheckBox focus = new JCheckBox("Focus");
  JCheckBox item = new JCheckBox("Item");
  JCheckBox key = new JCheckBox("Key");
  JCheckBox mouse = new JCheckBox("Mouse");
  JCheckBox mouseMotion = new JCheckBox("MouseMotion");
  JCheckBox text = new JCheckBox("Text");
  JCheckBox window = new JCheckBox("Window");
  JCheckBox ancestor = new JCheckBox("Ancestor");
  JCheckBox caret = new JCheckBox("Caret");
  JCheckBox cellEditor = new JCheckBox("CellEditor");
  JCheckBox change = new JCheckBox("Change");
  JCheckBox document = new JCheckBox("Document");
  JCheckBox hyperlink = new JCheckBox("Hyperlink");
  JCheckBox internalF = new JCheckBox("InternalFrame");
  JCheckBox listData = new JCheckBox("ListData");
  JCheckBox listS = new JCheckBox("ListSelection");
  JCheckBox menu = new JCheckBox("Menu");
  JCheckBox popupMenu = new JCheckBox("PopupMenu");
  JCheckBox tableCM = new JCheckBox("TableColumnModel");
  JCheckBox tableM = new JCheckBox("TableModel");
  JCheckBox treeE = new JCheckBox("TreeExpansion");
  JCheckBox treeM = new JCheckBox("TreeModel");
  JCheckBox treeS = new JCheckBox("TreeSelection");
  JCheckBox undo = new JCheckBox("UndoableEdit");

  public FireEventPanel() {
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    JLabel lbl2 = new JLabel("AWT Events(java.awt.event.*)");
    JLabel lbl3 = new JLabel("Swing Events(javax.swing.event.*)" );

    Box box = Box.createVerticalBox();
    box.add(lbl2);
    action.setBackground(Color.white);
    box.add(action);
    adjustment.setBackground(Color.white);
    box.add(adjustment);
    component.setBackground(Color.white);
    box.add(component);
    container.setBackground(Color.white);
    box.add(container);
    focus.setBackground(Color.white);
    box.add(focus);
    item.setBackground(Color.white);
    box.add(item);
    key.setBackground(Color.white);
    box.add(key);
    mouse.setBackground(Color.white);
    box.add(mouse);
    mouseMotion.setBackground(Color.white);
    box.add(mouseMotion);
    text.setBackground(Color.white);
    box.add(text);
    window.setBackground(Color.white);
    box.add(window);

    box.add(lbl3);
    ancestor.setBackground(Color.white);
    box.add(ancestor);
    caret.setBackground(Color.white);
    box.add(caret);
    cellEditor.setBackground(Color.white);
    box.add(cellEditor);
    change.setBackground(Color.white);
    box.add(change);
    document.setBackground(Color.white);
    box.add(document);
    hyperlink.setBackground(Color.white);
    box.add(hyperlink);
    internalF.setBackground(Color.white);
    box.add(internalF);
    listData.setBackground(Color.white);
    box.add(listData);
    listS.setBackground(Color.white);
    box.add(listS);
    menu.setBackground(Color.white);
    box.add(menu);
    popupMenu.setBackground(Color.white);
    box.add(popupMenu);
    tableCM.setBackground(Color.white);
    box.add(tableCM);
    tableM.setBackground(Color.white);
    box.add(tableM);
    treeE.setBackground(Color.white);
    box.add(treeE);
    treeM.setBackground(Color.white);
    box.add(treeM);
    treeS.setBackground(Color.white);
    box.add(treeS);
    undo.setBackground(Color.white);
    box.add(undo);

    eventPane = new JScrollPane(box);
    eventPane.setPreferredSize( new Dimension(330,185) );
    eventPane.createVerticalScrollBar();
    eventPane.setRowHeaderView( new JLabel("   ") );

    JPanel p3 = new JPanel();
    p3.setLayout(new BorderLayout());
    p3.setBackground(Color.white);
    p3.add(eventPane,BorderLayout.CENTER);

    JPanel p2 = new JPanel();
    p2.setLayout( new FlowLayout(FlowLayout.CENTER) );
    p2.setBorder(border);
    p2.add(p3);

    setLayout( new GridLayout(1,1) );
    add(p2);
  }
}



