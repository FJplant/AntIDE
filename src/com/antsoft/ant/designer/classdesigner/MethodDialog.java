/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/classdesigner/MethodDialog.java,v 1.9 1999/08/19 03:49:20 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.9 $
 * $History: MethodDialog.java $
 * 
 * *****************  Version 6  *****************
 * User: Remember     Date: 99-05-20   Time: 4:57p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 5  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:44p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 99-05-07   Time: 2:34p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 3  *****************
 * User: Remember     Date: 99-05-06   Time: 2:46p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 2  *****************
 * User: Remember     Date: 99-05-02   Time: 1:34a
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 8  *****************
 * User: Remember     Date: 98-10-10   Time: 3:17a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * com.sun.java -> javax
 * 
 * *****************  Version 7  *****************
 * User: Remember     Date: 98-09-29   Time: 1:41a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * class name 변경
 * 
 * *****************  Version 6  *****************
 * User: Remember     Date: 98-09-23   Time: 6:35a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * Class 이름 바꾸었음.
 * 
 * *****************  Version 5  *****************
 * User: Remember     Date: 98-09-22   Time: 12:43a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * ClassName 바뀜, 전체적으로 Dialog 처리방식 바뀜
 * 
 * *****************  Version 4  *****************
 * User: Remember     Date: 98-09-21   Time: 9:45p
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
import java.awt.event.*;
import java.util.StringTokenizer;
import javax.swing.*;

import com.antsoft.ant.util.WindowDisposer;

/**
 * @author  Kim sang kyun
 */
public class MethodDialog extends JDialog {
  public static final int ADD = 0;
  public static final int UPDATE = 1;

  private JTabbedPane tabbedPane;
  private MethodGeneralPanel generalTab;
  private MethodParamPanel paramTab;
  private JButton okOrupdateBtn, cancelBtn, helpBtn;

  /** update row number */
  private int updateRow;
  private String updateMethodName;

  /** dialog mode */
  private int mode;

  /** dialog 종료시 ok, cancel 선택 여부를 표시하는 flag */
  private boolean isOK = false;

  /** ok 선택시 채워져야 할 data */
  private boolean isStatic, isFinal, isAbstract, isNative, isSync;
  private String accessType, name, returnType;
  private MyParamTableModel paramModel;
  private JavaDocInfo javaDoc = new JavaDocInfo();

  private String firstIndent = "\t";

  public MethodDialog(JFrame parent, String title, boolean modal, int mode) {
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

    tabbedPane = new JTabbedPane();
    ActionListener actionEventHandler = new ActionEventHandler();

    //General Tab
    generalTab = new MethodGeneralPanel();
    tabbedPane.addTab("General", generalTab);
    tabbedPane.setSelectedIndex(0);

    //Param Tab
    paramTab = new MethodParamPanel();
    tabbedPane.addTab("Param", paramTab);

    if(getMode() == UPDATE){
      okOrupdateBtn = new JButton("Update");
      okOrupdateBtn.setActionCommand("UPDATE");
      okOrupdateBtn.addActionListener(actionEventHandler);
    }
    else{
      okOrupdateBtn = new JButton("OK");
      okOrupdateBtn.setActionCommand("OK");
      okOrupdateBtn.addActionListener(actionEventHandler);
    }
    cancelBtn = new JButton("Cancel");
    cancelBtn.setActionCommand("CANCEL");
    cancelBtn.addActionListener(actionEventHandler);

    helpBtn = new JButton("Help");
    helpBtn.setActionCommand("HELP");
    helpBtn.addActionListener(actionEventHandler);

    //BUTTONS
    JPanel buttonP = new JPanel();
    buttonP.setLayout(new FlowLayout(FlowLayout.CENTER));
    buttonP.add(okOrupdateBtn);
    buttonP.add(cancelBtn);
    buttonP.add(helpBtn);

    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    this.getContentPane().add(buttonP, BorderLayout.SOUTH);
    this.getContentPane().add(new JPanel(),BorderLayout.WEST);
    this.getContentPane().add(new JPanel(),BorderLayout.EAST);

    setSize(358, 358);
    this.setResizable(false);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = getSize();
    if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width) dlgSize.width = screenSize.width;
    setLocation((screenSize.width - dlgSize.width) / 2,
                (screenSize.height - dlgSize.height) / 2);
  }

  /** 만들어진 소스를 반환 */
  public String getGeneratedSource(){
    String gap = "\t";

    StringBuffer source = new StringBuffer();
    String paramStr = "";

    for(int i=0; i<paramModel.getRowCount(); i++)
    {
      paramStr += (String)paramModel.getValueAt(i, 0) + " " + (String)paramModel.getValueAt(i, 1);
      if(i != paramModel.getRowCount()-1) paramStr += ", ";
    }

    source.append("\n\n");
    source.append(firstIndent + "/**" + "\n");

    if(javaDoc.getDescription() != null) source.append(firstIndent + " * " + javaDoc.getDescription() + "\n");
    else source.append(firstIndent + " * " + "\n");

    source.append(firstIndent + " * " + "\n");
    for(int j=0; j<paramModel.getRowCount(); j++)
    {
      source.append(firstIndent + " * " + "@param " + paramModel.getValueAt(j, 1) + " " + paramModel.getValueAt(j, 2) +  "\n");
    }

    if(javaDoc.getReturns() != null)
    source.append("\t" + " * " + "@return " + javaDoc.getReturns() + "\n");

    if(javaDoc.getSee() != null)
    source.append("\t" + " * " + "@see " + javaDoc.getSee() + "\n");

    if(javaDoc.getSince() != null)
    source.append("\t" + " * " + "@since " + javaDoc.getSince() + "\n");

    if(javaDoc.getDepricated() != null)
    source.append("\t" + " * " + "@depricated " + javaDoc.getDepricated() + "\n");
    source.append(firstIndent + " */" + "\n");
    source.append(firstIndent + generalTab.getAccessType() + " " + generalTab.getModifierStr() + generalTab.getReturnType() + " " +
                  generalTab.getName() + " " + "( " + paramStr + " )" + "\n");
    source.append(firstIndent + "{" + "\n");
    source.append(firstIndent + gap + "//TO DO (implementation here)" + "\n");
    source.append(firstIndent + "}" + "\n");

    return source.toString();
  }

  public void setOK(boolean flag){
    this.isOK = flag;
  }


  public boolean isOK(){
    return this.isOK;
  }

  public boolean isStatic(){
    return this.isStatic;
  }

  public boolean isFinal(){
    return this.isFinal;
  }

  public boolean isAbstract(){
    return this.isAbstract;
  }

  public boolean isNative(){
    return this.isNative;
  }

  public boolean isSync(){
    return this.isSync;
  }

  public String getAccessType(){
    return this.accessType;
  }

  public String getName(){
    return this.name;
  }

  public String getReturnType(){
    return this.returnType;
  }

  public MyParamTableModel getParamModel(){
    return this.paramModel;
  }

  public JavaDocInfo getJavaDoc(){
    return javaDoc;
  }

  /**
   * mode를 알아낸다
   *
   * @return current mode
   */
  public int getMode(){
    return this.mode;
  }

  public void setUpdateRow(int row){
    this.updateRow = row;
  }

  public void setUpdateMethodName(String name){
    this.updateMethodName = name;
  }

  public int getUpdateRow(){
    return this.updateRow;
  }

  public String getUpdateMethodName(){
    return this.updateMethodName;
  }  

  public MethodGeneralPanel getGeneralPanel(){
    return generalTab;
  }

  public MethodParamPanel getParamPanel(){
    return paramTab;
  }

  //Action event handler
  class ActionEventHandler implements ActionListener {
    private String actionCommand;
    public void actionPerformed(ActionEvent e){
      actionCommand = e.getActionCommand();

      if(actionCommand.equals("OK")){

         setOK(true);
         accessType = generalTab.getAccessType();
         name = generalTab.getName();
         returnType = generalTab.getReturnType();
         isStatic = generalTab.getStatic();
         isFinal = generalTab.getFinal();
         isAbstract = generalTab.getAbstract();
         isNative = generalTab.getNative();
         isSync = generalTab.getSync();

         paramModel = paramTab.getParamModel();

         javaDoc = generalTab.getJavaDoc();
         javaDoc.removeAllParams();

         for(int i=0; i<paramModel.getRowCount(); i++){
           javaDoc.addParam((String)paramModel.getValueAt(i, 3));
         }

         if(name.equals("") || returnType.equals("")){
          JOptionPane.showMessageDialog(null, "Name or Return Type not Specified!!", "Error", JOptionPane.ERROR_MESSAGE, ClassDesigner.errorIcon );
         }
         else{
          dispose();
         }
      }
      else if(actionCommand.equals("UPDATE")){
         setOK(true);
         accessType = generalTab.getAccessType();
         name = generalTab.getName();
         returnType = generalTab.getReturnType();
         isStatic = generalTab.getStatic();
         isFinal = generalTab.getFinal();
         isAbstract = generalTab.getAbstract();
         isNative = generalTab.getNative();
         isSync = generalTab.getSync();

         paramModel = paramTab.getParamModel();

         //method general panel로 부터 javadoc을 얻는다
         javaDoc = generalTab.getJavaDoc();
         javaDoc.removeAllParams();

         //param javadoc info를 넣는다
         for(int i=0; i<paramModel.getRowCount(); i++){
           javaDoc.addParam((String)paramModel.getValueAt(i, 3));
         }

         if(name.equals("") || returnType.equals("")){
          JOptionPane.showMessageDialog(null, "Name or Return Type not Specified!!", "Error", JOptionPane.ERROR_MESSAGE, ClassDesigner.errorIcon );
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
