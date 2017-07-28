

package com.antsoft.ant.wizard;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.*;

public class FireEventPanel extends JPanel {

  JScrollPane eventPane;
  TitledBorder border = new TitledBorder("Select events to fire");
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
    box.add(action);
    box.add(adjustment);
    box.add(component);
    box.add(container);
    box.add(focus);
    box.add(item);
    box.add(key);
    box.add(mouse);
    box.add(mouseMotion);
    box.add(text);
    box.add(window);
    box.add(lbl3);
    box.add(ancestor);
    box.add(caret);
    box.add(cellEditor);
    box.add(change);
    box.add(document);
    box.add(hyperlink);
    box.add(internalF);
    box.add(listData);
    box.add(listS);
    box.add(menu);
    box.add(popupMenu);
    box.add(tableCM);
    box.add(tableM);
    box.add(treeE);
    box.add(treeM);
    box.add(treeS);
    box.add(undo);
    eventPane = new JScrollPane(box);
    eventPane.setPreferredSize( new Dimension(330,185) );
    eventPane.createVerticalScrollBar();
    eventPane.setBackground(Color.white);
    eventPane.setRowHeaderView( new JLabel("   ") );

    JPanel p2 = new JPanel();
    p2.setLayout( new FlowLayout(FlowLayout.CENTER) );
    p2.setBorder(border);
    p2.add(eventPane);

    setLayout( new GridLayout(1,1) );
    add(p2);

  }
}

