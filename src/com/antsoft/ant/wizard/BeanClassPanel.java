
package com.antsoft.ant.wizard;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class BeanClassPanel extends JPanel {

  JLabel classNameLbl = new JLabel("Class Name");
  JTextField className = new JTextField(20);
  JLabel packNameLbl = new JLabel("Package Name");
  JTextField packName = new JTextField(20);
  JCheckBox createEvent = new JCheckBox("Create Event Object and Listener");
  JLabel eventNameLbl = new JLabel("Event Name");
  JTextField eventName = new JTextField("CustomEvent",20);
  JLabel listenerNameLbl = new JLabel("Listener Name");
  int index = eventName.getText().indexOf("Event");
  String name = eventName.getText().substring(0,index);
  JTextField listenerName = new JTextField(name+"Listener",20);

  TitledBorder border1 = new TitledBorder("Bean Class");
  TitledBorder border2 = new TitledBorder("Event");

  JPanel p7 = new JPanel();

  public BeanClassPanel() {
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    //사이즈 지정
    classNameLbl.setPreferredSize( new Dimension(100,20) );
    className.setPreferredSize( new Dimension(250,20) );
    packNameLbl.setPreferredSize( new Dimension(100,20) );
    packName.setPreferredSize( new Dimension(250,20) );
    createEvent.setPreferredSize( new Dimension(340,20) );
    eventNameLbl.setPreferredSize( new Dimension(100,20) );
    eventName.setPreferredSize( new Dimension(250,20) );
    listenerNameLbl.setPreferredSize( new Dimension(100,20) );
    listenerName.setPreferredSize( new Dimension(250,20) );

    //Bean Class
    JPanel p1 = new JPanel();
    p1.setLayout(new FlowLayout( FlowLayout.LEFT ));
    p1.add( classNameLbl );
    p1.add( className );

    JPanel p2 = new JPanel();
    p2.setLayout( new FlowLayout(FlowLayout.LEFT) );
    p2.add( packNameLbl );
    p2.add( packName );

    JPanel p3 = new JPanel();
    p3.setBorder(border1);
    p3.setLayout( new GridLayout(2,1) );
    p3.add(p1);
    p3.add(p2);

    //event option
    JPanel p4 = new JPanel();
    p4.setLayout( new FlowLayout(FlowLayout.LEFT) );
    p4.add( createEvent );

    JPanel p5 = new JPanel();
    p5.setLayout( new FlowLayout(FlowLayout.LEFT) );
    p5.add( eventNameLbl );
    p5.add( eventName );
    eventNameLbl.setEnabled(false);
    eventName.setDisabledTextColor( Color.lightGray );
    eventName.setEnabled(false);

    JPanel p6 = new JPanel();
    p6.setLayout( new FlowLayout(FlowLayout.LEFT) );
    p6.add( listenerNameLbl );
    p6.add( listenerName );
    listenerName.setEditable(false);
    listenerNameLbl.setEnabled(false);
    listenerName.setDisabledTextColor( Color.lightGray );
    listenerName.setEnabled(false);

    p7.setBorder(border2);
    p7.setLayout( new GridLayout(2,1) );
    p7.add(p5);
    p7.add(p6);

    setLayout( new FlowLayout(FlowLayout.CENTER) );
    add(p3);
    add(p4);
    add(p7);
  }
}

