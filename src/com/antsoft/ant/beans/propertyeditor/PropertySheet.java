package com.antsoft.ant.beans.propertyeditor;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class PropertySheet extends JFrame{

  public JPanel p0 = new JPanel();

  public PropertySheet(){
    super("Property Sheet");
    jInit();
    setSize(300,400);
    setVisible(true);
  }

  private void jInit(){
    EtchedBorder border = new EtchedBorder();
    p0.setLayout(new FlowLayout(FlowLayout.LEFT));
    p0.setPreferredSize(new Dimension(250,350));
    p0.setBorder(border);
    getContentPane().add(p0);
  }
}
