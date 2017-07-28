
package com.antsoft.ant.wizard;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class PropertyPanel extends JPanel {

  String[] colNames = {"Name", "Type", "read", "write", "Bound", "Constrained"};
  DefaultTableModel model = new DefaultTableModel(colNames,0){
    public boolean isCellEditable(int row, int col){ return false;}
    };
  //DefaultTableColumnModel cModel = new DefaultTableColumnModel();
  JTable table = new JTable(model);
  JScrollPane sp = new JScrollPane(table);

  TitledBorder border = new TitledBorder("Property Option");

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
    //size ÁöÁ¤
    sp.setPreferredSize( new Dimension(340,160) );

    //table
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

