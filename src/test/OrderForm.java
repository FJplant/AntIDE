
//Title:        Ant Developer
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Kwon, Young Mo
//Company:      Antsoft Co.
//Description:  Ant Developer Project

package test;

import java.awt.*;
import com.borland.dbswing.*;
import com.borland.jbcl.layout.*;
import javax.swing.*;

public class OrderForm extends JPanel {
  JdbNavToolBar jdbNavToolBar1 = new JdbNavToolBar();
  JLabel jLabel1 = new JLabel();
  JTextField jTextField1 = new JTextField();
  JTextField jTextField2 = new JTextField();
  JTextField jTextField3 = new JTextField();
  JTextField jTextField4 = new JTextField();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JdbComboBox jdbComboBox1 = new JdbComboBox();
  JdbComboBox jdbComboBox2 = new JdbComboBox();
  TableScrollPane tableScrollPane1 = new TableScrollPane();
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public OrderForm() {
    try  {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setLayout(gridBagLayout1);
    jLabel1.setText("Bill To");
    jTextField1.setText("jTextField1");
    jTextField2.setText("jTextField2");
    jTextField3.setText("jTextField3");
    jTextField4.setText("jTextField4");
    jLabel2.setText("Sold By");
    jLabel3.setText("Terms");
    this.add(jdbNavToolBar1, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 13, 46), 39, -4));
    this.add(jTextField1, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 6, 0, 0), 72, 0));
    this.add(jTextField2, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 6, 0, 0), 73, 0));
    this.add(jTextField3, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 6, 0, 0), 73, 0));
    this.add(jTextField4, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 6, 0, 76), 92, 0));
    this.add(jLabel2, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 6, 0, 94), 0, 0));
    this.add(jLabel3, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 268), 44, 0));
    this.add(jdbComboBox1, new GridBagConstraints(0, 7, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 7), 6, 0));
    this.add(jdbComboBox2, new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 176), 0, 0));
    this.add(tableScrollPane1, new GridBagConstraints(0, 8, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 11, 17), 425, 94));
    this.add(jLabel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 8, 0, 99), 0, 0));
  }
} 
