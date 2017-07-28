/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/defaultproperty/FontPanel.java,v 1.3 1999/07/20 09:18:45 lila Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 */
package com.antsoft.ant.property.defaultproperty;
import java.awt.*;import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import com.antsoft.ant.util.BorderList;
import com.antsoft.ant.util.FontList;

/** * Font Option Panel
 *
 * @author kim sang kyun
 */
public class FontPanel extends JPanel {

  private JComboBox fontCombox, sizeCombox;  private JCheckBox boldCbx, italicCbx;
  private JTextArea ta;
  private static DefaultComboBoxModel fontM, sizeM;
  private JScrollPane pane;
  static {
    String [] fontFamilyNames = Toolkit.getDefaultToolkit().getFontList();

    fontM = new DefaultComboBoxModel();    for(int i=0; i<fontFamilyNames.length; i++){
      fontM.addElement(fontFamilyNames[i]);
    }
    sizeM = new DefaultComboBoxModel();
    for(int i=5; i<31; i++){
      sizeM.addElement("" + i);
    }
  }

  public FontPanel() {    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    setBorder(BorderList.etchedBorder5);

    ItemListener il = new ItemEventHandler();    ChangeListener cl = new ChangeEventHandler();

    JLabel fontLbl = new JLabel("Font List");    fontLbl.setPreferredSize(new Dimension(50,20));    fontCombox = new JComboBox(fontM);
    fontCombox.setPreferredSize(new Dimension(100,20));
    fontCombox.addItemListener(il);

    JPanel p1 = new JPanel();
    p1.setLayout(new FlowLayout(FlowLayout.LEFT));
    p1.add(fontLbl);
    p1.add(fontCombox);

    JLabel sizeLbl = new JLabel("Size");    sizeLbl.setPreferredSize(new Dimension(50,20));    sizeCombox = new JComboBox(sizeM);
    sizeCombox.setPreferredSize(new Dimension(100,20));
    sizeM.setSelectedItem("12");
    sizeCombox.addItemListener(il);

    JPanel p2 = new JPanel();
    p2.setLayout(new FlowLayout(FlowLayout.LEFT));
    p2.add(sizeLbl);
    p2.add(sizeCombox);

    JPanel p3 = new JPanel(new GridLayout(2,1));
    p3.setPreferredSize(new Dimension(200,60));
    p3.add(p1);
    p3.add(p2);

    //JLabel styleLbl = new JLabel("Style");
    boldCbx = new JCheckBox("Bold");
    boldCbx.addChangeListener(cl);
    italicCbx = new JCheckBox("Italic");
    italicCbx.addChangeListener(cl);
    boldCbx.setPreferredSize(new Dimension(100,20));
    italicCbx.setPreferredSize(new Dimension(100,20));

    JPanel p4 = new JPanel(new GridLayout(2,1));
    //p4.setPreferredSize(new Dimension(170,60));
    TitledBorder border = new TitledBorder(new EtchedBorder(),"Font Style");
    border.setTitleColor(Color.black);
    border.setTitleFont(FontList.regularFont);
    p4.setBorder(border);
    p4.add(boldCbx);
    p4.add(italicCbx);

    //test area    StringBuffer text = new StringBuffer();
    text.append("\n");
    text.append("   public static void main(String[] args){\n");
    text.append("      MyClass obj = new MyClass();\n");
    text.append("      System.out.println(obj.toString());\n");
    text.append("      System.out.println(\"Good Bye\");\n");
    text.append("   }");

    //JLabel sampleLbl = new JLabel("Preview", JLabel.LEFT);    ta = new JTextArea(11, 5);
    ta.setFont(getSelectedFont());
    ta.setText(text.toString());
    ta.setEditable(false);

    pane = new JScrollPane(ta);    pane.setPreferredSize(new Dimension(330,205));
    JPanel p5 = new JPanel(new GridLayout(1,1));
    p5.add(pane);

    JPanel p6 = new JPanel();
    p6.setBorder(BorderList.previewBorder);
    p6.add(p5);

    JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p7.add(p3);
    p7.add(p4);

    setLayout(new BorderLayout());
    add(p7,BorderLayout.NORTH);
    add(p6,BorderLayout.CENTER);
    add(new JPanel(),BorderLayout.EAST);
    add(new JPanel(),BorderLayout.WEST);
    add(new JPanel(),BorderLayout.SOUTH);
  }

  /**
   * return selected Font
   *
   * @return selected Font
   */
  public Font getSelectedFont(){
    String fontName = (String)fontM.getSelectedItem();
    int fontSize = Integer.parseInt((String)sizeM.getSelectedItem());
    int style = 0;

    if(boldCbx.isSelected()){
      style = Font.BOLD;
      if(italicCbx.isSelected()) {
        style = Font.BOLD + Font.ITALIC;
      }
    }
    else if(italicCbx.isSelected()) {
        style = Font.ITALIC;
    }
    else{
      style = Font.PLAIN;
    }
    return new Font(fontName, style, fontSize);
  }

  public void setSelectedFont(Font newFont){
    fontM.setSelectedItem(newFont.getName());
    sizeM.setSelectedItem(String.valueOf(newFont.getSize()));
    if(newFont.isBold()) boldCbx.setSelected(true);
    if(newFont.isItalic()) italicCbx.setSelected(true);

    pane.repaint();

  }

  class ItemEventHandler implements ItemListener{
    public void itemStateChanged(ItemEvent e){
      ta.setFont(getSelectedFont());
      ta.repaint();
    }
  }

  class ChangeEventHandler implements ChangeListener{
    public void stateChanged(ChangeEvent e){
      ta.setFont(getSelectedFont());
      ta.invalidate();
      ta.validate();
      ta.repaint();

    }
  }
}





