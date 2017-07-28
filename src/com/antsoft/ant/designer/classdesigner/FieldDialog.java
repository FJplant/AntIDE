/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/classdesigner/FieldDialog.java,v 1.9 1999/08/19 03:47:15 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.9 $
 * $History: FieldDialog.java $
 * 
 * *****************  Version 7  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:44p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 6  *****************
 * User: Remember     Date: 99-05-13   Time: 1:02a
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 5  *****************
 * User: Remember     Date: 99-05-11   Time: 2:06p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 99-05-07   Time: 10:59p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 3  *****************
 * User: Remember     Date: 99-05-07   Time: 2:34p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 2  *****************
 * User: Remember     Date: 99-05-06   Time: 2:46p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 10  *****************
 * User: Remember     Date: 98-10-10   Time: 3:17a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * com.sun.java -> javax
 * 
 * *****************  Version 9  *****************
 * User: Remember     Date: 98-09-29   Time: 1:41a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * static mode variable 삭제
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
 * User: Remember     Date: 98-09-21   Time: 9:45p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 98-09-18   Time: 10:28p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * 
 * *****************  Version 3  *****************
 * User: Remember     Date: 98-09-18   Time: 12:15a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 *
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-09-16   Time: 12:03p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * 
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-09-15   Time: 3:08a
 * Created in $/JavaProjects/src/ant/designer/classdesigner
 *
 */

package com.antsoft.ant.designer.classdesigner;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;

import com.antsoft.ant.util.*;

/**
 * @author  Kim sang kyun
 */
public class FieldDialog extends JDialog {

  public static final int ADD = 0;
  public static final int UPDATE = 1;

  private JRadioButton publicRBtn, protectedRBtn, privateRBtn, packageRBtn;
  private JTextField typeTf, nameTf, initValTf, docTf;
  private JButton javadocBtn, okBtn, cancelBtn, helpBtn, okOrupdateBtn, docBtn;
  private ButtonGroup accessTypeBtnGrp;
  private JCheckBox staticCbox, finalCbox, getterCbox, setterCbox,
                    transientCbox, volatileCbox;
  private int updateRow;
  private String updateFieldName;

  /** Dialog mode */
  private int mode;

  /** Dialog 종료시 OK, Cancel 선택 여부를 표시하는 flag */
  private boolean isOK = false;

  /** OK 선택시 채워질 정보들 */
  private boolean isStatic, isFinal, isTransient, isVolatile, isGetter, isSetter;
  private String accessType = null;
  private String type = null;
  private String name = null;
  private String initial = null;
  private String desc = null;
  private String doc = null;
  private String firstIndent = "\t";

  /**
   * constructor
   *
   * @param  parent  parent frame
   * @param  title  dialog title
   * @param  modal  modal or modaless flag
   */
  public FieldDialog(JFrame parent, String title, boolean modal, int mode) {
    super(parent, title, modal);
    this.mode = mode;
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setFirstIndent(String indent){
    firstIndent = indent;
  }

  void jbInit() throws Exception {
    // add window disposer
		addWindowListener(WindowDisposer.getDisposer());
		addKeyListener(WindowDisposer.getDisposer()); 		

    ActionListener actionHandler = new ActionEventHandler();

    //////////////////////// FIRST LINE //////////////////////////
    //access type label
    //JLabel accessTypeLbl = new JLabel("Access Type");

    //public, protected, private radio button
    accessTypeBtnGrp = new ButtonGroup();

    publicRBtn = new JRadioButton("public");
    publicRBtn.setPreferredSize(new Dimension(90,20));
    publicRBtn.setActionCommand("Public");
    publicRBtn.setSelected(true);
    publicRBtn.setToolTipText("public access modifier setting");
    accessTypeBtnGrp.add(publicRBtn);

    protectedRBtn = new JRadioButton("protected");
    protectedRBtn.setPreferredSize(new Dimension(90,20));
    protectedRBtn.setActionCommand("Protected");
    protectedRBtn.setToolTipText("protected access modifier setting");
    accessTypeBtnGrp.add(protectedRBtn);

    privateRBtn = new JRadioButton("private");
    privateRBtn.setPreferredSize(new Dimension(90,20));
    privateRBtn.setActionCommand("Private");
    privateRBtn.setToolTipText("private access modifier setting");
    accessTypeBtnGrp.add(privateRBtn);

    packageRBtn = new JRadioButton("package");
    packageRBtn.setPreferredSize(new Dimension(90,20));
    packageRBtn.setActionCommand("Package");
    packageRBtn.setToolTipText("package access modifier setting");
    accessTypeBtnGrp.add(packageRBtn);

    //by lila
    JPanel p1 = new JPanel();
    p1.setPreferredSize(new Dimension(131,130));
    TitledBorder border1 = new TitledBorder(new EtchedBorder(),"Access Type");
    border1.setTitleFont(FontList.regularFont);
    border1.setTitleColor(Color.black);
    p1.setBorder(border1);
    p1.setLayout(new FlowLayout(FlowLayout.LEFT));
    p1.add(publicRBtn);
    p1.add(protectedRBtn);
    p1.add(privateRBtn);
    p1.add(packageRBtn);

    //modifier panel buttons
    //JLabel modifierLbl = new JLabel("Modifier");
    staticCbox = new JCheckBox("static");
    finalCbox = new JCheckBox("final");
    transientCbox = new JCheckBox("transient");
    volatileCbox = new JCheckBox("volatile");
    staticCbox.setPreferredSize(new Dimension(90,20));
    finalCbox.setPreferredSize(new Dimension(90,20));
    transientCbox.setPreferredSize(new Dimension(90,20));
    volatileCbox.setPreferredSize(new Dimension(90,20));

    JPanel p2 = new JPanel();
    p2.setPreferredSize(new Dimension(131,130));
    TitledBorder border2 = new TitledBorder(new EtchedBorder(),"Modifier");
    border2.setTitleFont(FontList.regularFont);
    border2.setTitleColor(Color.black);
    p2.setBorder(border2);
    p2.setLayout(new FlowLayout(FlowLayout.LEFT));
    p2.add(staticCbox);
    p2.add(finalCbox);
    p2.add(transientCbox);
    p2.add(volatileCbox);

    //check boxs (getter, setter)
    getterCbox = new JCheckBox("Create get Method");
    setterCbox = new JCheckBox("Create set Method");
    getterCbox.setPreferredSize(new Dimension(250,20));
    setterCbox.setPreferredSize(new Dimension(250,20));

    //by lila
    JPanel p3 = new JPanel();
    p3.setBorder(BorderList.optionBorder);
    p3.setLayout(new GridLayout(2,1));
    p3.add(getterCbox);
    p3.add(setterCbox);

    //name label & textfield
    JLabel typeLbl = new JLabel("Type");
    typeLbl.setPreferredSize(new Dimension(55,20));
    //name label & textfield
    JLabel nameLbl = new JLabel("Name");
    nameLbl.setPreferredSize(new Dimension(55,20));
    //initVal label & textfield
    JLabel initValLbl = new JLabel("InitValue");
    initValLbl.setPreferredSize(new Dimension(55,20));

    typeTf = new JTextField(16);
    nameTf = new JTextField(16);
    initValTf = new JTextField(16);

    //docp
    JLabel docL = new JLabel("Comment");
    docL.setPreferredSize(new Dimension(55,20));
    docTf = new JTextField(16);

    JPanel p4 = new JPanel();
    p4.setLayout(new FlowLayout(FlowLayout.LEFT));
    p4.add(typeLbl);
    p4.add(typeTf);

    JPanel p5 = new JPanel();
    p5.setLayout(new FlowLayout(FlowLayout.LEFT));
    p5.add(nameLbl);
    p5.add(nameTf);

    JPanel p6 = new JPanel();
    p6.setLayout(new FlowLayout(FlowLayout.LEFT));
    p6.add(docL);
    p6.add(docTf);

    JPanel p10 = new JPanel();
    p10.setLayout(new FlowLayout(FlowLayout.LEFT));
    p10.add(initValLbl);
    p10.add(initValTf);

    JPanel p7 = new JPanel();
    p7.setLayout(new GridLayout(1,2));
    p7.add(p1);
    p7.add(p2);

    JPanel p8 = new JPanel();
    p8.setLayout(new GridLayout(4,1));
    p8.add(p4);
    p8.add(p5);
    p8.add(p10);
    p8.add(p6);

    JPanel p11 = new JPanel();
    p11.setBorder(new EtchedBorder());
    p11.setLayout(new FlowLayout(FlowLayout.CENTER));
    p11.add(p8);

    JPanel p9 = new JPanel();
    p9.setLayout(new FlowLayout(FlowLayout.LEFT));
    p9.add(p7);
    p9.add(p3);
    p9.add(p11);

    if(getMode() == UPDATE){
      okOrupdateBtn = new JButton("Update");
      okOrupdateBtn.setActionCommand("UPDATE");
      okOrupdateBtn.addActionListener(actionHandler);
    }
    else {
      okOrupdateBtn = new JButton("OK");
      okOrupdateBtn.setActionCommand("OK");
      okOrupdateBtn.addActionListener(actionHandler);
    }
    cancelBtn = new JButton("Cancel");
    cancelBtn.setActionCommand("CANCEL");
    cancelBtn.addActionListener(actionHandler);

    helpBtn = new JButton("Help");
    helpBtn.setActionCommand("HELP");
    helpBtn.addActionListener(actionHandler);

    //bottom buttons panel
    JPanel buttonsP = new JPanel();
    FlowLayout buttonsL = new FlowLayout(FlowLayout.CENTER);
    buttonsP.setLayout(buttonsL);
    buttonsP.add(okOrupdateBtn);
    buttonsP.add(cancelBtn);
    buttonsP.add(helpBtn);

    //by lila
    getContentPane().setLayout(new BorderLayout(0,5));
    getContentPane().add(p9,BorderLayout.CENTER);
    getContentPane().add(buttonsP,BorderLayout.SOUTH);
    getContentPane().add(new JPanel(),BorderLayout.EAST);
    getContentPane().add(new JPanel(),BorderLayout.WEST);

    setSize(300, 430);
    this.setResizable(false);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height)
      dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width)
      dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2,
                (screenSize.height - dlgSize.height) / 2);

  }

  private String getModifierStr(){
    String str = "";
    if(isTransient()) {
      str += "transient";
      if(isVolatile()) str += " volatile";
    }
    if(isVolatile()) str += "volatile";

    return str.trim();
  }

  public String getGeneratedFieldSource(){
    String fieldName = nameTf.getText();
    String fieldType = typeTf.getText();
    String initValue = initValTf.getText();
    String gap = "\t";

    StringBuffer source = new StringBuffer();

    //javadoc part
    source.append("\n\n");
    source.append(firstIndent + "/**" + "\n");
    source.append(firstIndent + " * "+ docTf.getText() + "\n");
    source.append(firstIndent + " */" + "\n");

    if(!packageRBtn.isSelected()) source.append(firstIndent + accessType);
    else source.append(firstIndent);

    if(getModifierStr().length() > 0) source.append(" " + getModifierStr() + " ");
    else if(!packageRBtn.isSelected()) source.append(" ");

    source.append(fieldType + " " + fieldName);

    if(initValue.length() > 0) source.append(" = " + initValue + ";");
    else source.append(";");
    source.append("\n");

    return source.toString();
  }

  public String getGeneratedGetterMethodSource(){
    if(!getterCbox.isSelected()) return null;

    String fieldName = nameTf.getText();
    String fieldType = typeTf.getText();
    String initValue = initValTf.getText();

    String gap = "\t";

    StringBuffer source = new StringBuffer();

    //javadoc part
    source.append("\n\n");
    source.append(firstIndent + "/**" + "\n");
    source.append(firstIndent + " * " + "Gets the " + fieldName + "\n");
    source.append(firstIndent + " * " + "\n");
    source.append(firstIndent + " * " + "@return " + fieldName + "\n");
    source.append(firstIndent + " */" + "\n");

    source.append(firstIndent + "public " + fieldType + " " + "get" + fieldName.substring(0,1).toUpperCase()+fieldName.substring(1) + " ()" + "\n");
    source.append(firstIndent + "{" + "\n");
    source.append(firstIndent + gap + "//TO DO (implementation here)" + "\n");
    source.append(firstIndent + gap + "return " + fieldName + ";" + "\n");
    source.append(firstIndent + "}");
    source.append("\n");

    return source.toString();
  }

  public String getGeneratedSetterMethodSource(){
    if(!setterCbox.isSelected()) return null;

    String fieldName = nameTf.getText();
    String fieldType = typeTf.getText();
    String initValue = initValTf.getText();

    String gap = "\t";

    StringBuffer source = new StringBuffer();

    //javadoc part
    source.append("\n\n");
    source.append(firstIndent + "/**" + "\n");
    source.append(firstIndent + " * " + "Sets the " + fieldName + "\n");
    source.append(firstIndent + " * " + "\n");
    source.append(firstIndent + " * " + "@param " + fieldName + "\n");
    source.append(firstIndent + " */" + "\n");

    source.append(firstIndent + "public" + " void" + " " + "set" + fieldName.substring(0,1).toUpperCase()+fieldName.substring(1) + " ( " + fieldType + " " + fieldName + " )" + "\n");
    source.append(firstIndent + "{" + "\n");
    source.append(firstIndent + gap + "//TO DO (implementation here)" + "\n");
    source.append(firstIndent + gap + "this." + fieldName + " = " + fieldName + ";" + "\n");
    source.append(firstIndent + "}");
    source.append("\n");

    return source.toString();
  }



  /**
   * field가 static인지 아닌지 정보를 제공한다
   *
   * @return static인지 아닌지 판별하는 boolean value
   */
  public boolean isStatic(){
    return this.isStatic;
  }

  /**
   * field가 final인지 아닌지 정보를 제공한다
   *
   * @return  final인지 아닌지 판별하는 boolean value
   */
  public boolean isFinal(){
    return this.isFinal;
  }

  /**
   * field가 transient인지 아닌지 정보를 제공한다
   *
   * @return transient인지 아닌지 판별하는 boolean value
   */
  public boolean isTransient(){
    return this.isTransient;
  }

  /**
   * field가 volatile인지 아닌지 정보를 제공한다
   *
   * @return  volatile인지 아닌지 판별하는 boolean value
   */
  public boolean isVolatile(){
    return this.isVolatile;
  }

  /**
   * field가 getter인지 아닌지 정보를 제공한다
   *
   * @return getter 인지 아닌지 판별하는 boolean value
   */
  public boolean isGetter(){
    return this.isGetter;
  }

  /**
   * field가 setter인지 아닌지 정보를 제공한다
   *
   * @return setter 인지 아닌지 판별하는 boolean value
   */
  public boolean isSetter(){
    return this.isSetter;
  }

  /**
   * field의 access type을 제공한다
   *
   * @return field의 access type
   */
  public String getAccessType(){
    return this.accessType;
  }

  /**
   * field의 type을 제공한다
   *
   * @return field의 type
   */
  public String getType(){
    return this.type;
  }

  /**
   * field의 name을 제공한다
   *
   * @return field의 name
   */
  public String getName(){
    return this.name;
  }

  /**
   * field의 initvalue를 제공한다
   *
   * @return field의 initvalue
   */
  public String getInitial(){
    return this.initial;
  }

  /**
   * field의 javadoc comment를 제공한다
   *
   * @return field의 javadoc comment
   */
  public String getDoc(){
    return this.doc;
  }

  /**
   * field의 description을 제공한다
   *
   * @return field의 description
   */
  public String getDesc(){
    return this.desc;
  }

  /**
   * update할 row를 설정한다
   *
   * @param row update할 row
   */
  public void setUpdateRow(int row){
    this.updateRow = row;
  }

  /**
   * update할 field name을 설정
   *
   * @param name update할 field name
   */
  public void setUpdateFieldName(String name){
    this.updateFieldName = name;
  }

  /**
   * update row를 제공한다
   *
   * @return update row
   */
  public int getUpdateRow(){
    return this.updateRow;
  }

  /**
   * update field name을 제공한다
   *
   * @return update field name
   */
  public String getUpdateFieldName(){
    return this.updateFieldName;
  }

  /**
   * mode를 알아낸다
   *
   * @return current mode
   */
  public int getMode(){
    return this.mode;
  }

  /**
   * 사용자가 Ok를 선택여부를 설정한다
   *
   * @param flag OK선택 여부
   */
  public void setOK(boolean flag){
    this.isOK = flag;
  }

  /**
   * 사용자가 Ok를 선택했는지 여부를 제공한다
   *
   * @return OK : true, CANCEL : false
   */
  public boolean isOK(){
    return this.isOK;
  }

  /**
   * accesstype을 설정한다
   *
   * @param comm accesstype
   */
  public void setAccessType(String comm){
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
  }

  /**
   * update mode시 dialog의 데이타를 이전 상태로 복구한다
   *
   * @param  isStatic   static여부를 나타내는 boolean value
   * @param  isFinal   final여부를 나타내는 boolean value
   * @param  accessType   field의 accesstype
   * @param  type   field의 type
   * @param  name   field의 name
   * @param  inital   field의 inital value
   * @param  isGetter   getter여부를 나타내는 boolean value
   * @param  isSetter    getter여부를 나타내는 boolean value
   * @param  desc   field의 description
   * @param  doc   field의 javadoc comment
   */
  public void restoreData(boolean isStatic, boolean isFinal, boolean isTransient,
                          boolean isVolatile, String accessType, String type, String name,
                          String initial, boolean isGetter, boolean isSetter, String doc){
    staticCbox.setSelected(isStatic);
    finalCbox.setSelected(isFinal);
    transientCbox.setSelected(isTransient);
    volatileCbox.setSelected(isVolatile);

    if(accessType.equals("public")){
      publicRBtn.setSelected(true);
    }else if(accessType.equals("protected")){
      protectedRBtn.setSelected(true);
    }else if(accessType.equals("private")){
      privateRBtn.setSelected(true);
    }else if(accessType.equals("")){
      packageRBtn.setSelected(true);
    }

    typeTf.setText(type);
    nameTf.setText(name);
    initValTf.setText(initial);

    getterCbox.setSelected(isGetter);
    setterCbox.setSelected(isSetter);
    docTf.setText(doc);
  }

  //Action event handler
  class ActionEventHandler implements ActionListener {
    private String actionCommand;

    public void actionPerformed(ActionEvent e){
      actionCommand = e.getActionCommand();
      if(actionCommand.equals("OK")){
        setOK(true);

        //accessType setting
        ButtonModel accessBtnModel = accessTypeBtnGrp.getSelection();
        String accessComm = accessBtnModel.getActionCommand();
        setAccessType(accessComm);

        //type setting
        type = typeTf.getText();

        //name setting
        name = nameTf.getText();

        //initvalue setting
        initial = initValTf.getText();

        //getter setting
        isGetter = getterCbox.isSelected();

        //setter setting
        isSetter = setterCbox.isSelected();

        //isStatic setting
        isStatic = staticCbox.isSelected();

        //isFinal setting
        isFinal = finalCbox.isSelected();

        //isTransient setting
        isTransient = transientCbox.isSelected();

        //isVolatile setting
        isVolatile = volatileCbox.isSelected();

        //doc comment
        if(!docTf.getText().equals("")) doc = docTf.getText();

        if(type.equals("") || name.equals("")){
          JOptionPane.showMessageDialog(null, "Type or Name not Specified!!", "Error", JOptionPane.ERROR_MESSAGE, ClassDesigner.errorIcon );
        }
        else{
          dispose();
        }
      }
      else if(actionCommand.equals("UPDATE")){
        setOK(true);
        //accessType setting
        ButtonModel accessBtnModel = accessTypeBtnGrp.getSelection();
        String accessComm = accessBtnModel.getActionCommand();
        setAccessType(accessComm);

        //type setting
        type = typeTf.getText();

        //name setting
        name = nameTf.getText();

        //initvalue setting
        initial = initValTf.getText();

        //getter setting
        isGetter = getterCbox.isSelected();

        //setter setting
        isSetter = setterCbox.isSelected();

        //isStatic setting
        isStatic = staticCbox.isSelected();

        //isFinal setting
        isFinal = finalCbox.isSelected();

        //isTransient setting
        isTransient = transientCbox.isSelected();

        //isVolatile setting
        isVolatile = volatileCbox.isSelected();

        //doc comment
        if(!docTf.getText().equals("")) doc = docTf.getText();

        if(type.equals("") || name.equals("")){
          JOptionPane.showMessageDialog(null, "Type or Name not Specified!!", "Error", JOptionPane.ERROR_MESSAGE, ClassDesigner.errorIcon );
        }
        else{
          dispose();
        }
      }
      else if(actionCommand.equals("CANCEL")){
        isOK = false;
        dispose();
      }
      else if(actionCommand.equals("HELP")){
      }
    }
  }
}
