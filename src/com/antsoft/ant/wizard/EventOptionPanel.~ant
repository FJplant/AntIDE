package com.antsoft.ant.wizard;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class EventOptionPanel extends JPanel {

  JTable table = new JTable();
  String[] colNames = {"Function Name", "Event ID"};
  DefaultTableModel model = new DefaultTableModel(colNames,1);
  JScrollPane sp = new JScrollPane(table);

  JButton add = new JButton("Add");
  JButton remove = new JButton("Remove");

  JLabel lbl = new JLabel("Add member function of Listener interface and event id.");

  TitledBorder border = new TitledBorder("Event Option");

  public EventOptionPanel() {
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    //size Á¶Àý
    sp.setPreferredSize( new Dimension( 340, 140 ) );
    lbl.setPreferredSize( new Dimension(340,20) );

    JPanel p3 = new JPanel();
    p3.setLayout( new GridLayout(1,1) );
    p3.add(lbl);

    JPanel p4 = new JPanel();
    p4.setLayout( new GridLayout(1,1) );
    p4.add(sp);

    //table
    table.setModel(model);
    table.setRowHeight(20);
		model.setValueAt( "", 0, 0 );
		model.setValueAt( "", 0, 1 );

    //Button
    JPanel p1 = new JPanel();
    p1.setLayout( new FlowLayout(FlowLayout.CENTER) );
    p1.add(add);
    p1.add(remove);

    JPanel p2 = new JPanel();
    p2.setBorder(border);
    p2.add(p4);
    p2.add(p1);

    setLayout(new BorderLayout());
    add(p3, BorderLayout.NORTH);
    add(p2, BorderLayout.CENTER);
  }
}

