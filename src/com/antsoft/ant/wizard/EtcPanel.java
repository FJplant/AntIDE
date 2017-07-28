
package com.antsoft.ant.wizard;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class EtcPanel extends JPanel {

  //JLabel lbl = new JLabel("Select method to overrinde.");
  JCheckBox toString = new JCheckBox("toString()");
  JCheckBox equals = new JCheckBox("equals()");
  JCheckBox paint = new JCheckBox("paint()");
  JCheckBox beanInfo = new JCheckBox("Create BeanInfo Class");

  JLabel lbl = new JLabel("");

  TitledBorder border1 = new TitledBorder("Select overriding method");
  TitledBorder border2 = new TitledBorder("BeanInfo Class");

  public EtcPanel() {
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    //size ÁöÁ¤
    toString.setPreferredSize( new Dimension(330,20) );
    equals.setPreferredSize( new Dimension(330,20) );
    paint.setPreferredSize( new Dimension(330,20) );
    beanInfo.setPreferredSize( new Dimension(330,20) );
    lbl.setPreferredSize( new Dimension(330,25) );

    JPanel p4 = new JPanel();
    p4.setLayout( new FlowLayout(FlowLayout.LEFT) );
    p4.add( new JLabel("") );
    p4.add( toString );

    JPanel p5 = new JPanel();
    p5.setLayout( new FlowLayout(FlowLayout.LEFT) );
    p5.add( new JLabel("") );
    p5.add( equals );

    JPanel p6 = new JPanel();
    p6.setLayout( new FlowLayout(FlowLayout.LEFT) );
    p6.add( new JLabel("") );
    p6.add( paint );

    /*JPanel p8 = new JPanel();
    p8.setLayout( new FlowLayout(FlowLayout.LEFT) );
    p8.add( new JLabel() );
    p8.add( bean );
    p8.add( event );*/

    JPanel p7 = new JPanel();
    p7.setLayout( new FlowLayout(FlowLayout.LEFT) );
    p7.add( new JLabel("") );
    p7.add( beanInfo );

    JPanel p1 = new JPanel();
    p1.setLayout( new GridLayout(3,1) );
    p1.setBorder(border1);
    p1.add(p4);
    p1.add(p5);
    p1.add(p6);

    JPanel p2 = new JPanel();
    p2.setLayout( new GridLayout(1,1) );
    p2.setBorder(border2);
    p2.add(p7);

    JPanel p3 = new JPanel();
    p3.setLayout( new GridLayout(1,1) );
    p3.add(lbl);

    setLayout( new FlowLayout(FlowLayout.CENTER) );
    add(p1);
    add(p3);
    add(p2);
  }
}

