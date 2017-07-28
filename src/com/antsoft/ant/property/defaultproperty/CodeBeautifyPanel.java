/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/property/defaultproperty/CodeBeautifyPanel.java,v 1.2 1999/07/20 09:18:45 lila Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author: Kim, Sang Kyun
 * $Revision: 1.2 $
 */
package com.antsoft.ant.property.defaultproperty;
import java.awt.*;import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import com.antsoft.ant.util.*;

public class CodeBeautifyPanel extends JPanel {
  private JRadioButton tabRadioBtn, spaceRadioBtn;  private JComboBox tabSizeCombo, spaceSizeCombo;
  private DefaultComboBoxModel tabM, spaceM;
  private JCheckBox switchCbx, closeCbx;
  private JTextArea sampleTa;

  private static final StringBuffer switchClose = new StringBuffer();  private static final StringBuffer switchNothing = new StringBuffer();
  private static final StringBuffer nothingClose = new StringBuffer();
  private static final StringBuffer nothing = new StringBuffer();

  static {    nothing.append("\n");
    nothing.append("  public String methodA(int param) {\n");
    nothing.append("    switch(param) {\n");
    nothing.append("    case 'A' :\n");
    nothing.append("         statementA();\n");
    nothing.append("         break;\n");
    nothing.append("    case default :\n");
    nothing.append("         statementB();\n");
    nothing.append("         break;\n");
    nothing.append("    }\n");
    nothing.append("  }");

    switchClose.append("\n");    switchClose.append("  public String methodA(int param) {\n");
    switchClose.append("    switch(param) {\n");
    switchClose.append("      case 'A' :\n");
    switchClose.append("         statementA();\n");
    switchClose.append("         break;\n");
    switchClose.append("      case default :\n");
    switchClose.append("         statementB();\n");
    switchClose.append("         break;\n");
    switchClose.append("      }\n");
    switchClose.append("    }");

    switchNothing.append("\n");    switchNothing.append("  public String methodA(int param) {\n");
    switchNothing.append("    switch(param) {\n");
    switchNothing.append("      case 'A' :\n");
    switchNothing.append("         statementA();\n");
    switchNothing.append("         break;\n");
    switchNothing.append("      case default :\n");
    switchNothing.append("         statementB();\n");
    switchNothing.append("         break;\n");
    switchNothing.append("    }\n");
    switchNothing.append("  }");

    nothingClose.append("\n");    nothingClose.append("  public String methodA(int param) {\n");
    nothingClose.append("    switch(param) {\n");
    nothingClose.append("    case 'A' :\n");
    nothingClose.append("         statementA();\n");
    nothingClose.append("         break;\n");
    nothingClose.append("    case default :\n");
    nothingClose.append("         statementB();\n");
    nothingClose.append("         break;\n");
    nothingClose.append("      }\n");
    nothingClose.append("    }");
  }

  public CodeBeautifyPanel(){    setBorder(BorderList.etchedBorder5);

    ButtonGroup bg = new ButtonGroup();
    tabRadioBtn = new JRadioButton("Tab Size");    tabRadioBtn.setPreferredSize(new Dimension(80,20));    tabRadioBtn.setSelected(true);
    bg.add(tabRadioBtn);
    spaceRadioBtn = new JRadioButton("Space Size");
    spaceRadioBtn.setPreferredSize(new Dimension(80,20));
    bg.add(spaceRadioBtn);

    tabM = new DefaultComboBoxModel();    spaceM = new DefaultComboBoxModel();

    for(int i=2; i<=10; i+=2){      tabM.addElement(new Integer(i));
    }

    for(int j=1; j<=10; j++){      spaceM.addElement(new Integer(j));
    }

    tabSizeCombo = new JComboBox(tabM);    tabSizeCombo.setPreferredSize(new Dimension(50,20));    spaceSizeCombo = new JComboBox(spaceM);
    spaceSizeCombo.setPreferredSize(new Dimension(50,20));

    JPanel tabComboP = new JPanel(new FlowLayout(FlowLayout.LEFT));    tabComboP.add(tabRadioBtn);    tabComboP.add(tabSizeCombo);

    JPanel spaceComboP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    spaceComboP.add(spaceRadioBtn);
    spaceComboP.add(spaceSizeCombo);

    JPanel tabP = new JPanel(new FlowLayout(FlowLayout.LEFT));    tabP.add(tabRadioBtn);
    tabP.add(tabComboP);

    JPanel spaceP = new JPanel(new FlowLayout(FlowLayout.LEFT));    spaceP.add(spaceRadioBtn);
    spaceP.add(spaceComboP);

    JPanel leftP = new JPanel(new GridLayout(2,1));
    leftP.setPreferredSize(new Dimension(170,90));
    TitledBorder border1 = new TitledBorder(new EtchedBorder(),"Size");
    border1.setTitleColor(Color.black);
    border1.setTitleFont(FontList.regularFont);
    leftP.setBorder(border1);
    leftP.add(tabP);
    leftP.add(spaceP);

    switchCbx = new JCheckBox("Switch Case Indent");    switchCbx.setPreferredSize(new Dimension(160,20));    switchCbx.addItemListener(new ItemListener(){
      public void itemStateChanged(ItemEvent e){
        itemChanged();
      }});
    closeCbx = new JCheckBox("Blocket Indent");
    closeCbx.setPreferredSize(new Dimension(160,20));
    closeCbx.addItemListener(new ItemListener(){
      public void itemStateChanged(ItemEvent e){
        itemChanged();
      }});

    JPanel cbxP = new JPanel(new GridLayout(2,1));
    cbxP.setPreferredSize(new Dimension(170,90));
    cbxP.setBorder(BorderList.optionBorder);
    cbxP.add(switchCbx);
    cbxP.add(closeCbx);

    JPanel topP = new JPanel(new GridLayout(1,2));
    topP.add(leftP);
    topP.add(cbxP);

    sampleTa = new JTextArea(10, 1);    sampleTa.setEditable(false);
    sampleTa.setFont(new Font("Courier New", Font.BOLD, 14));
    sampleTa.setText(nothing.toString());

    JScrollPane samplePane = new JScrollPane(sampleTa);    JPanel bottomP = new JPanel(new BorderLayout());    bottomP.setBorder(BorderList.previewBorder);    bottomP.add(sampleTa,BorderLayout.CENTER);
    bottomP.add(new JPanel(),BorderLayout.EAST);
    bottomP.add(new JPanel(),BorderLayout.WEST);
    bottomP.add(new JPanel(),BorderLayout.SOUTH);

    Box box = Box.createVerticalBox();    box.add(topP);    box.add(bottomP);    setLayout(new BorderLayout());    add(box, BorderLayout.CENTER);//    add(new JPanel(), BorderLayout.NORTH);
    add(new JPanel(), BorderLayout.WEST);
    add(new JPanel(), BorderLayout.EAST);
    add(new JPanel(), BorderLayout.SOUTH);
  }


  private void itemChanged(){
    if(switchCbx.isSelected()){
      if(closeCbx.isSelected()) sampleTa.setText(switchClose.toString());
      else sampleTa.setText(switchNothing.toString());
    }
    else{
      if(closeCbx.isSelected()) sampleTa.setText(nothingClose.toString());
      else sampleTa.setText(nothing.toString());
    }
  }

  public boolean isTab(){
    return tabRadioBtn.isSelected();
  }

  public int getMSize(){
    if(isTab()) return ((Integer)tabM.getElementAt(tabSizeCombo.getSelectedIndex())).intValue();
    else return ((Integer)spaceM.getElementAt(spaceSizeCombo.getSelectedIndex())).intValue();
  }

  public void setTab(boolean isTab){
    if(isTab) tabRadioBtn.setSelected(true);
    else spaceRadioBtn.setSelected(true);
  }

  public void setMSize(int size){
    if(isTab()) tabM.setSelectedItem(new Integer(size));
    else spaceM.setSelectedItem(new Integer(size));
  }

  public void setSwitchIndent(boolean flag){
    switchCbx.setSelected(flag);
  }

  public boolean isSwitchIndent(){
    return switchCbx.isSelected();
  }

  public void setCloseIndent(boolean flag){
    closeCbx.setSelected(flag);
  }

  public boolean isCloseIndent(){
    return closeCbx.isSelected();
  }
}



