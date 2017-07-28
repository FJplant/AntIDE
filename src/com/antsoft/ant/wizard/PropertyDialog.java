

package com.antsoft.ant.wizard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class PropertyDialog extends JDialog implements ActionListener, ItemListener{

  JLabel nameLbl = new JLabel("Property Name");
  JTextField name = new JTextField(17);
  JLabel typeLbl = new JLabel("Type");
  JComboBox type = new JComboBox();
  JCheckBox read = new JCheckBox("Read Method");
  JCheckBox write = new JCheckBox("Write Method");
  JCheckBox bound = new JCheckBox("Bound");
  JCheckBox constrained = new JCheckBox("Constrained");

  JButton ok = new JButton("OK");
  JButton cancel = new JButton("Cancel");

  TitledBorder border1 = new TitledBorder("Property");
  TitledBorder border2 = new TitledBorder("Method");

  boolean isOk = false;

  public PropertyDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();

      setSize(350, 280);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = this.getSize();
      if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
      if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
      this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

      setResizable(false);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  public PropertyDialog() {
    this(null, "", false);
  }

  void jbInit() throws Exception {
    //size 지정
    nameLbl.setPreferredSize(new Dimension(100,20));
    name.setPreferredSize(new Dimension(200,20));
    typeLbl.setPreferredSize(new Dimension(100,20));
    type.setPreferredSize(new Dimension(90,20));
    read.setPreferredSize(new Dimension(300,20));
    write.setPreferredSize(new Dimension(300,20));
    bound.setPreferredSize(new Dimension(300,20));
    constrained.setPreferredSize(new Dimension(300,20));

    //combobox item 추가
    type.addItem("String");
    type.addItem("int");
    type.addItem("long");
    type.addItem("float");
    type.addItem("double");
    type.addItem("boolean");
    type.addItem("char");
    type.addItem("byte");

    JPanel p1 = new JPanel();
    p1.setLayout( new FlowLayout(FlowLayout.LEFT));
    p1.add(nameLbl);
    p1.add(name);

    JPanel p2 = new JPanel();
    p2.setLayout( new FlowLayout(FlowLayout.LEFT) );
    p2.add(typeLbl);
    p2.add(type);

    JPanel p3 = new JPanel();
    p3.setLayout( new GridLayout(2,1) );
    p3.setBorder(border1);
    p3.add(p1);
    p3.add(p2);

    JPanel p4 = new JPanel();
    p4.setLayout( new GridLayout(4,1) );
    p4.setBorder(border2);
    p4.add(read);
    p4.add(write);
    p4.add(bound);
    p4.add(constrained);
    bound.setEnabled(false);
    constrained.setEnabled(false);
    write.addItemListener(this);

    JPanel p5 = new JPanel();
    p5.setLayout( new FlowLayout(FlowLayout.CENTER) ); 
    p5.add(p3);
    p5.add(p4);

    //button
    JPanel p6 = new JPanel();
    p6.setLayout( new FlowLayout(FlowLayout.CENTER) );
    p6.add(ok);
    p6.add(cancel);
    ok.addActionListener(this);
    cancel.addActionListener(this);

    getContentPane().setLayout( new BorderLayout() );
    getContentPane().add(p5, BorderLayout.CENTER);
    getContentPane().add(p6, BorderLayout.SOUTH);
    getContentPane().add(new JPanel(), BorderLayout.EAST);
    getContentPane().add(new JPanel(), BorderLayout.WEST);
    getContentPane().add(new JPanel(), BorderLayout.NORTH);

  }

  public boolean isOk(){
    return isOk;
  }

  //event handler-------------------------------------------------------
  public void actionPerformed( ActionEvent e ){
    if( e.getSource() == ok ){
      isOk = true;
      dispose();
    }else if( e.getSource() == cancel ){
      dispose();
    }
  }

  public void itemStateChanged( ItemEvent e ){
    if( write.isSelected() ){
      bound.setEnabled(true);
      constrained.setEnabled(true);
    }else{
      bound.setEnabled(false);
      constrained.setEnabled(false);
    }
  }
}

