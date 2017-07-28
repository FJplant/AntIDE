/*
 * $Id: EtcPanel.java,v 1.2 1999/08/19 05:25:07 multipia Exp $
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

import com.antsoft.ant.util.*;

/**
 *  EtcPanel - bean wizard의 마지막 화면을 구성하는 패널
 *  designed by Kim yun Kyung
 */
public class EtcPanel extends JPanel {
  JCheckBox toString = new JCheckBox("toString()");
  JCheckBox equals = new JCheckBox("equals()");
  JCheckBox paint = new JCheckBox("paint()");
  JCheckBox beanInfo = new JCheckBox("Create BeanInfo Class");

  JLabel lbl = new JLabel("");

  BlackTitledBorder border1 = new BlackTitledBorder("Select overriding method");
  BlackTitledBorder border2 = new BlackTitledBorder("BeanInfo Class");

  public EtcPanel() {
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    //size 지정
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
//    setBorder(new EtchedBorder());
    add(p1);
    add(p3);
    add(p2);
  }
}

