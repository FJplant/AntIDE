/*
 * $Id: PropertyPanel.java,v 1.2 1999/08/19 05:25:07 multipia Exp $
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
import javax.swing.table.*;

import com.antsoft.ant.util.*;

public class PropertyPanel extends JPanel {

  MyTableModel model;
  JTable table;
  JScrollPane sp;

  BlackTitledBorder border = new BlackTitledBorder("Property Option");

  JButton add = new JButton("Add");
  JButton edit = new JButton("Edit");
  JButton remove = new JButton("Remove");

  public PropertyPanel() {
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    model = new MyTableModel();
    table = new JTable(model);
    sp = new JScrollPane(table);

    //size ÁöÁ¤
    sp.setPreferredSize( new Dimension(340,160) );

    JComboBox type = new JComboBox();
    type.addItem("String");
    type.addItem("int");
    type.addItem("long");
    type.addItem("float");
    type.addItem("double");
    type.addItem("boolean");
    type.addItem("char");
    type.addItem("byte");

    JCheckBox checkBox = new JCheckBox();

    //table
    DefaultCellEditor typeEditor = new DefaultCellEditor(type);

    TableColumn col = table.getColumn("Type");
    col.setCellEditor(typeEditor);
    col = table.getColumn("Read");
    col = table.getColumn("Write");
    col = table.getColumn("Bound");
    col = table.getColumn("Constrained");


    table.setRowHeight(20);

    table.setShowGrid(true);

    //button
    JPanel p1 = new JPanel();
    p1.setLayout( new FlowLayout(FlowLayout.CENTER) );
    p1.add(add);
    p1.add(edit);
    p1.add(remove);

    JPanel p2 = new JPanel();
    p2.setBorder(border);
    p2.setLayout( new FlowLayout(FlowLayout.CENTER) );
    p2.add(sp);
    p2.add(p1);

    setLayout( new GridLayout(1,1) );
    add(p2);
  }

}

