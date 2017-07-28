/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/classdesigner/MethodGeneralPanel.java,v 1.6 1999/07/29 08:14:14 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.6 $
 * $History: MethodGeneralPanel.java $
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 99-05-20   Time: 4:57p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 3  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:44p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 2  *****************
 * User: Remember     Date: 99-05-06   Time: 2:46p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 *                                      
 * *****************  Version 8  *****************
 * User: Remember     Date: 98-10-10   Time: 3:17a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * com.sun.java -> javax
 * 
 * *****************  Version 7  *****************
 * User: Remember     Date: 98-09-23   Time: 6:35a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * Class 이름 바꾸었음.
 *
 * *****************  Version 6  *****************
 * User: Remember     Date: 98-09-22   Time: 12:43a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * ClassName 바뀜, 전체적으로 Dialog 처리방식 바뀜
 *
 * *****************  Version 5  *****************
 * User: Remember     Date: 98-09-18   Time: 10:28p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 98-09-16   Time: 12:03p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 *
 * *****************  Version 3  *****************
 * User: Remember     Date: 98-09-15   Time: 3:07a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 *
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-09-07   Time: 10:07p
 * Updated in $/Ant/src/ant/designer/classdesigner
 * package name 변경
 *
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-09-03   Time: 12:59a
 * Created in $/Ant/src
 * Attrubute add GUI
 *
 */
package com.antsoft.ant.designer.classdesigner;

import java.awt.*;
import java.util.Vector;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import com.antsoft.ant.util.*;

/**
 * @author  Kim sang kyun
 */
public class MethodGeneralPanel extends JPanel {
  private JRadioButton publicRBtn, protectedRBtn, privateRBtn, packageRBtn;
  private JTextField nameTf, returntypeTf, docTf;
  private JCheckBox staticCbox, finalCbox, syncCbox, abstractCbox, nativeCbox;
  private ButtonGroup accessTypeBtnGrp;
  private Vector docM;
  private JComboBox javaDocCombo;
  private JavaDocInfo javaDoc = new JavaDocInfo();

  public MethodGeneralPanel() {
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    setBorder(BorderList.etchedBorder5);
    ActionListener actionEventHandler = new ActionEventHandler();
    DocumentListener documentEventHandler = new DocumentEventHandler();

    //////////////////////// FIRST LINE //////////////////////////
    //access type label
    //JLabel accessTypeLbl = new JLabel("Access Type");

    //public, protected, private radio button
    accessTypeBtnGrp = new ButtonGroup();
    publicRBtn = new JRadioButton("public");
    publicRBtn.setPreferredSize(new Dimension(100,20));
    publicRBtn.setActionCommand("Public");
    publicRBtn.setSelected(true);
    publicRBtn.setToolTipText("public access modifier setting");
    accessTypeBtnGrp.add(publicRBtn);

    protectedRBtn = new JRadioButton("protected");
    protectedRBtn.setPreferredSize(new Dimension(100,20));
    protectedRBtn.setActionCommand("Protected");
    protectedRBtn.setToolTipText("protected access modifier setting");
    accessTypeBtnGrp.add(protectedRBtn);

    privateRBtn = new JRadioButton("private");
    privateRBtn.setPreferredSize(new Dimension(100,20));
    privateRBtn.setActionCommand("Private");
    privateRBtn.setToolTipText("private access modifier setting");
    accessTypeBtnGrp.add(privateRBtn);

    packageRBtn = new JRadioButton("package");
    packageRBtn.setPreferredSize(new Dimension(100,20));
    packageRBtn.setActionCommand("Package");
    packageRBtn.setToolTipText("package access modifier setting");
    accessTypeBtnGrp.add(packageRBtn);

    //by lila
    JPanel p1 = new JPanel();
    p1.setPreferredSize(new Dimension(150,160));
    TitledBorder border1 = new TitledBorder(new EtchedBorder(),"Access Type");
    border1.setTitleFont(FontList.regularFont);
    border1.setTitleColor(Color.black);
    p1.setBorder(border1);
    p1.setLayout(new FlowLayout(FlowLayout.LEFT));
    p1.add(publicRBtn);
    p1.add(protectedRBtn);
    p1.add(privateRBtn);
    p1.add(packageRBtn);

    //JLabel modLbl = new JLabel("Modifier");

    staticCbox = new JCheckBox("static");
    staticCbox.setPreferredSize(new Dimension(80,20));
    finalCbox = new JCheckBox("final");
    finalCbox.setPreferredSize(new Dimension(80,20));
    abstractCbox = new JCheckBox("abstract");
    abstractCbox.setPreferredSize(new Dimension(80,20));
    nativeCbox = new JCheckBox("native");
    nativeCbox.setPreferredSize(new Dimension(80,20));
    syncCbox = new JCheckBox("synchronized");
    syncCbox.setPreferredSize(new Dimension(100,20));

    //by lila
    JPanel p2 = new JPanel();
    p2.setPreferredSize(new Dimension(150,160));
    p2.setLayout(new FlowLayout(FlowLayout.LEFT));
    TitledBorder border2 = new TitledBorder(new EtchedBorder(),"Modifier");
    border2.setTitleFont(FontList.regularFont);
    border2.setTitleColor(Color.black);
    p2.setBorder(border2);
    p2.add(staticCbox);
    p2.add(finalCbox);
    p2.add(abstractCbox);
    p2.add(nativeCbox);
    p2.add(syncCbox);

    /*Box box = Box.createVerticalBox();
    box.add(topRapP);
    box.add(cbxP);*/

    /////////////////////////SECOND LINE //////////////////////
    //labels
    JLabel nameLbl = new JLabel("Name");
    nameLbl.setPreferredSize(new Dimension(85,20));
    JLabel returnLbl = new JLabel("Return Type");
    returnLbl.setPreferredSize(new Dimension(85,20));


    nameTf = new JTextField(18);
    returntypeTf = new JTextField(18);

    //by lila
    JPanel p3 = new JPanel();
    p3.setLayout(new FlowLayout(FlowLayout.LEFT));
    p3.add(nameLbl);
    p3.add(nameTf);

    JPanel p4 = new JPanel();
    p4.setLayout(new FlowLayout(FlowLayout.LEFT));
    p4.add(returnLbl);
    p4.add(returntypeTf);

    //javadoc
    docM = new Vector();
    docM.addElement("JavaDoc");
    docM.addElement("Desc");
    docM.addElement("See");
    docM.addElement("Since");
    docM.addElement("Depricated");
    docM.addElement("Return");

    javaDocCombo = new JComboBox(docM);
    javaDocCombo.setSelectedItem(docM.elementAt(0));
    javaDocCombo.setActionCommand("JavaDoc");
    javaDocCombo.addActionListener(actionEventHandler);

    docTf = new JTextField(18);
    docTf.getDocument().putProperty("TYPE", "JAVADOC");
    docTf.getDocument().addDocumentListener(documentEventHandler);
    docTf.setText("JavaDoc Comment Setting");
    docTf.setEditable(false);

    //by lila
    JPanel p5 = new JPanel();
    p5.setLayout(new FlowLayout(FlowLayout.LEFT));
    p5.add(javaDocCombo);
    p5.add(docTf);

    JPanel p6 = new JPanel();
    p6.setLayout(new FlowLayout(FlowLayout.CENTER));
    p6.add(p1);
    p6.add(p2);

    JPanel p7 = new JPanel();
    p7.setLayout(new GridLayout(2,1));
    p7.add(p3);
    p7.add(p4);

    //font 지정
    publicRBtn.setFont(FontList.regularFont);
    protectedRBtn.setFont(FontList.regularFont);
    privateRBtn.setFont(FontList.regularFont);
    packageRBtn.setFont(FontList.regularFont);
    staticCbox.setFont(FontList.regularFont);
    finalCbox.setFont(FontList.regularFont);
    abstractCbox.setFont(FontList.regularFont);
    nativeCbox.setFont(FontList.regularFont);
    syncCbox.setFont(FontList.regularFont);
    nameLbl.setFont(FontList.regularFont);
    returnLbl.setFont(FontList.regularFont);
    javaDocCombo.setFont(FontList.regularFont);
    nameLbl.setForeground(Color.black);
    returnLbl.setForeground(Color.black);

    setLayout(new BorderLayout());
    add(p6,BorderLayout.NORTH);
    add(p7,BorderLayout.CENTER);
    add(p5,BorderLayout.SOUTH);
    add(new JPanel(),BorderLayout.WEST);
    add(new JPanel(),BorderLayout.EAST);

  }

  public String getAccessType(){
    ButtonModel model = accessTypeBtnGrp.getSelection();
    String comm = model.getActionCommand();
    String accessType = "";

    if(comm.equals("Public")){
      accessType = "public";
    }
    else if(comm.equals("Protected")){
      accessType = "protected";
    }
    else if(comm.equals("Private")){
      accessType = "private";
    }
    else if(comm.equals("Package")){
      accessType = "";
    }

    return accessType;
  }

  public String getName(){
    return nameTf.getText();
  }

  public String getReturnType(){
    return returntypeTf.getText();
  }

  public String getModifierStr(){
    String str = "";
    if(getStatic()) str += "static ";
    if(getAbstract()) str += "abstract ";
    if(getFinal()) str += "final ";

    return str;
  }

  public boolean getStatic(){
    return staticCbox.isSelected();
  }

  public boolean getAbstract(){
    return abstractCbox.isSelected();
  }

  public boolean getNative(){
    return nativeCbox.isSelected();
  }

  public boolean getFinal(){
    return finalCbox.isSelected();
  }

  public boolean getSync(){
    return syncCbox.isSelected();
  }

  public JavaDocInfo getJavaDoc(){
    return this.javaDoc;
  }

  public void restoreData(boolean isStatic, boolean isFinal, boolean isSync, boolean isAbstract,
                          boolean isNative, String accessType, String returnType, String name, JavaDocInfo javaDoc){
    if(accessType.equals("public")){
      publicRBtn.setSelected(true);
    }else if(accessType.equals("protected")){
      protectedRBtn.setSelected(true);
    }else if(accessType.equals("private")){
      privateRBtn.setSelected(true);
    }

    nameTf.setText(name);
    returntypeTf.setText(returnType);
    staticCbox.setSelected(isStatic);
    finalCbox.setSelected(isFinal);
    syncCbox.setSelected(isSync);
    abstractCbox.setSelected(isAbstract);
    nativeCbox.setSelected(isNative);

    this.javaDoc = javaDoc;
  }

  class DocumentEventHandler implements DocumentListener{
    public void process(DocumentEvent e){
      String type = (String)e.getDocument().getProperty("TYPE");
      if(type.equals("JAVADOC")){
        String item = (String)javaDocCombo.getSelectedItem();

        if(item.equals("Desc") && !docTf.getText().equals("")){
           javaDoc.setDescription(docTf.getText());
        }
        else if(item.equals("See") && !docTf.getText().equals("")){
           javaDoc.setSee(docTf.getText());
        }
        else if(item.equals("Since") && !docTf.getText().equals("")){
           javaDoc.setSince(docTf.getText());
        }
        else if(item.equals("Depricated") && !docTf.getText().equals("")){
           javaDoc.setDepricated(docTf.getText());
        }
        else if(item.equals("Return") && !docTf.getText().equals("")){
           javaDoc.setReturn(docTf.getText());
        }
        else if(item.equals("Desc") && !docTf.getText().equals("")){
           javaDoc.setDescription(docTf.getText());
        }
      }
    }

    public void insertUpdate(DocumentEvent e){ process(e);  }

    public void changedUpdate(DocumentEvent e){ process(e); }

    public void removeUpdate(DocumentEvent e){ process(e); }
  }

    //Action event handler
  class ActionEventHandler implements ActionListener {
    private String actionCommand;
    public void actionPerformed(ActionEvent e){
      actionCommand = e.getActionCommand();
      if(actionCommand.equals("JavaDoc")){
        String item = (String)javaDocCombo.getSelectedItem();

        if(item.equals("See")){
          docTf.setEditable(true);
          docTf.setText(javaDoc.getSee());
        }
        else if(item.equals("Since")){
          docTf.setEditable(true);
          docTf.setText(javaDoc.getSince());
        }
        else if(item.equals("Desc")){
          docTf.setEditable(true);
          docTf.setText(javaDoc.getDescription());
        }
        else if(item.equals("Depricated")){
          docTf.setEditable(true);
          docTf.setText(javaDoc.getDepricated());
        }
        else if(item.equals("Return")){
          docTf.setEditable(true);
          docTf.setText(javaDoc.getReturns());
        }
        else if(item.equals("JavaDoc")){
          docTf.setEditable(false);
          docTf.setText("JavaDoc Comment Setting");
        }
      }
    }
  }
}
