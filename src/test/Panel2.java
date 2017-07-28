
//Title:        Ant Developer
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Kwon, Young Mo
//Company:      Antsoft Co.
//Description:  Ant Developer Project

package test;

import java.awt.*;
import javax.swing.*;
import com.borland.jbcl.layout.*;
import javax.swing.border.*;

public class Panel2 extends JPanel {
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TitledBorder titledBorder1;
  JButton jButton1 = new JButton();

  public Panel2() {
    try  {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("Java Virtual Machine's");
    jLabel1.setText("Total Memory: 9911K");
    this.setLayout(gridBagLayout1);
    jLabel2.setText("Free Memory: 2022K");
    this.setBorder(titledBorder1);
    jButton1.setText("Run GC");
    this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 2, 2));
    this.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.5, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 2, 2));
    this.add(jButton1, new GridBagConstraints(1, 0, 1, 2, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  }
}        
