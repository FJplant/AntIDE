/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/classdesigner/MethodPanel.java,v 1.6 1999/08/20 06:27:28 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.6 $
 * $History: MethodPanel.java $
 * 
 * *****************  Version 3  *****************
 * User: Remember     Date: 99-05-20   Time: 4:57p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:44p
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
 * no change
 * 
 * *****************  Version 8  *****************
 * User: Remember     Date: 98-09-23   Time: 6:35a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * Class 이름 바꾸었음.
 * 
 * *****************  Version 7  *****************
 * User: Remember     Date: 98-09-22   Time: 12:43a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * ClassName 바뀜, 전체적으로 Dialog 처리방식 바뀜
 *
 * *****************  Version 6  *****************
 * User: Remember     Date: 98-09-21   Time: 9:45p
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
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
import java.awt.event.*;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.AbstractTableModel;

import com.antsoft.ant.util.*;

/**
 * @author  Kim sang kyun
 */
public class MethodPanel extends JPanel {
  private JButton addButton, removeButton;
  private MyMethodTableModel model;
  private JTable methodTable;
  private JScrollPane scrollpane;
  private CDOperInfoContainer cdOperInfoContainer;

  public MethodPanel(CDOperInfoContainer cdOperInfoContainer) {
    this.cdOperInfoContainer = cdOperInfoContainer;

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
    MouseListener mouseEventHandler = new MouseEventHandler();

    addButton = new JButton("Add");
    addButton.setFont(FontList.regularFont);
    addButton.setActionCommand("Add");
    addButton.addActionListener(actionEventHandler);

    removeButton = new JButton("Remove");
    removeButton.setFont(FontList.regularFont);
    removeButton.setActionCommand("Remove");
    removeButton.addActionListener(actionEventHandler);

    //by lila
    JPanel p1 = new JPanel();
    p1.setLayout(new FlowLayout(FlowLayout.CENTER));
    p1.add(addButton);
    p1.add(removeButton);

    model = new MyMethodTableModel();
    methodTable = new JTable(model);

    methodTable.setRowSelectionAllowed(true);
    methodTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    methodTable.addMouseListener(mouseEventHandler);
    methodTable.setBorder(BorderList.bebelBorder);

    scrollpane = new JScrollPane(methodTable);
    scrollpane.setPreferredSize(new Dimension(550,220));

    //by lila
    JPanel p2 = new JPanel();
    p2.setLayout(new BorderLayout(0,5));
    TitledBorder titleB = new TitledBorder(new EtchedBorder(),"Method Table");
    titleB.setTitleFont(FontList.regularFont);
    titleB.setTitleColor(Color.black);
    p2.setBorder(titleB);
    p2.add(p1,BorderLayout.SOUTH);
    p2.add(scrollpane,BorderLayout.CENTER);
    p2.add(new JPanel(),BorderLayout.EAST);
    p2.add(new JPanel(),BorderLayout.WEST);

    setLayout(new FlowLayout(FlowLayout.CENTER));
    add(p2);

  }

  Component makeDummyPanel() {
    JPanel dummy = new JPanel();
    return dummy;
  }

  public void updateMethodData(String prevName, int row, boolean isStatic, boolean isFinal, boolean isAbstract,
                               boolean isNative, boolean isSync, String accessType, String returnType,
                               String name, MyParamTableModel paramModel, JavaDocInfo javaDoc){

    model.setValueAt(new Boolean(isStatic), row, 0);
    model.setValueAt(new Boolean(isFinal), row, 1);
    model.setValueAt(new Boolean(isAbstract), row, 2);
    model.setValueAt(new Boolean(isNative), row, 3);
    model.setValueAt(new Boolean(isSync), row, 4);
    model.setValueAt(accessType, row, 5);
    model.setValueAt(returnType, row, 6);
    model.setValueAt(name, row, 7);

    //param data
    model.setParamValueAt(paramModel, row);

    cdOperInfoContainer.delOperInfo(prevName);

    CDOperInfo operInfo = new CDOperInfo();
    operInfo.setStatic(isStatic);
    operInfo.setFinal(isFinal);
    operInfo.setAbstract(isAbstract);
    operInfo.setNative(isNative);
    operInfo.setSynchronous(isSync);

    operInfo.setAccessType(accessType);
    operInfo.setReturnType(returnType);
    operInfo.setMethodName(name);
    operInfo.setJavaDoc(javaDoc);

    if(paramModel != null)
      for(int j=0; j<paramModel.getRowCount(); j++){
        CDParamInfo paramInfo = new CDParamInfo();
        paramInfo.setType((String)paramModel.getValueAt(j, 0));
        paramInfo.setParamName((String)paramModel.getValueAt(j, 1));

        operInfo.addParamInfo(paramInfo);
      }
    cdOperInfoContainer.addOperInfo(operInfo);
  }

  public void addMethodData(boolean isStatic, boolean isFinal, boolean isAbstract, boolean isNative,
                            boolean isSync, String accessType, String returnType, String name,
                            MyParamTableModel paramModel, JavaDocInfo javaDoc){

    int row = model.getRowCount();

    model.setValueAt(new Boolean(isStatic), row, 0);
    model.setValueAt(new Boolean(isFinal), row, 1);
    model.setValueAt(new Boolean(isAbstract), row, 2);
    model.setValueAt(new Boolean(isNative), row, 3);
    model.setValueAt(new Boolean(isSync), row, 4);
    model.setValueAt(accessType, row, 5);
    model.setValueAt(returnType, row, 6);
    model.setValueAt(name, row, 7);

    //param data
    model.setParamValueAt(paramModel, row);

    CDOperInfo operInfo = new CDOperInfo();
    operInfo.setStatic(isStatic);
    operInfo.setFinal(isFinal);
    operInfo.setAbstract(isAbstract);
    operInfo.setNative(isNative);
    operInfo.setSynchronous(isSync);

    operInfo.setAccessType(accessType);
    operInfo.setReturnType(returnType);
    operInfo.setMethodName(name);
    operInfo.setJavaDoc(javaDoc);

    if(paramModel != null)
      for(int j=0; j<paramModel.getRowCount(); j++){
        CDParamInfo paramInfo = new CDParamInfo();
        paramInfo.setType((String)paramModel.getValueAt(j, 0));
        paramInfo.setParamName((String)paramModel.getValueAt(j, 1));

        operInfo.addParamInfo(paramInfo);
      }

    cdOperInfoContainer.addOperInfo(operInfo);
  }

  //Action event handler
  class ActionEventHandler implements ActionListener {
    private String actionCommand;
    public void actionPerformed(ActionEvent e){
      actionCommand = e.getActionCommand();
      if(actionCommand.equals("Add")){
          MethodDialog dlg = new MethodDialog(ClassDesigner.parent, "Method Add Dialog", true, MethodDialog.ADD);
          dlg.setVisible(true);

          if(dlg.isOK() == true){
            addMethodData(dlg.isStatic(), dlg.isFinal(), dlg.isAbstract(), dlg.isNative(),
                          dlg.isSync(), dlg.getAccessType(), dlg.getReturnType(), dlg.getName(),
                          dlg.getParamModel(), dlg.getJavaDoc());
          }
      }
      else if(actionCommand.equals("Remove")){
        int index = methodTable.getSelectedRow();
        if(index > -1){
          cdOperInfoContainer.delOperInfo((String)model.getValueAt(index, 7));
          model.removeValueAt(index);
        }
      }
    }
  }

  class MyMethodTableModel extends AbstractTableModel {
    final String[] columnNames = {"static", "final", "abstract", "native", "sync",
                                "access type", "return type", "name"};

    Hashtable data = new Hashtable();
    Hashtable paramData = new Hashtable();

    public MyMethodTableModel(){}

    public int getColumnCount() {
      return columnNames.length;
    }

    public int getRowCount() {
      return data.size() / getColumnCount();
    }

    public String getColumnName(int col) {
      return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
      String key = makeKey(row, col);
      return data.get(key);
    }

    public Class getColumnClass(int c) {
      return getValueAt(0, c).getClass();
    }

    public void setValueAt(Object value, int row, int col) {
      String key = makeKey(row, col);
      data.put(key, value);

      fireTableCellUpdated(row, col);
    }

    public MyParamTableModel getParamValueAt(int row){
      return (MyParamTableModel)paramData.get(new Integer(row).toString());
    }

    public void setParamValueAt(Object value, int row){
      String key = new Integer(row).toString();
      paramData.put(key, value);

      fireTableRowsInserted(row-1,row);
    }

    public void removeValueAt(int row){
      int rowCount = getRowCount();
      if(row != rowCount-1){
        for(int i=row; i<rowCount-1; i++){
          for(int j=0; j<8; j++){
            setValueAt(getValueAt(i+1, j), i, j);
          }
        }
      }

      for(int i=0; i<8; i++){
        String lastKey = makeKey(rowCount-1, i);
        data.remove(lastKey);
      }
      fireTableRowsDeleted(row-1, row);
      //param data
      paramData.remove(new Integer(row).toString());
    }

    private String makeKey(int row, int col){
      return  new Integer(row).toString() + "***" + new Integer(col).toString();
    }
  }

  class MouseEventHandler extends MouseAdapter{
    private boolean isStatic, isFinal, isAbstract, isNative, isSync;
    private String accessType, returnType, name, doc;

    public void mouseClicked(MouseEvent e){
      int row = methodTable.getSelectedRow();
      int lowHeight = methodTable.getRowHeight();
      int clickedRow = (int)e.getY()/lowHeight;

      if(row > -1 && clickedRow < model.getRowCount()){
        if(e.getClickCount() == 2){
          MethodDialog dlg = new MethodDialog(ClassDesigner.parent, "Method Update Dialog", true,
                                              MethodDialog.UPDATE);
          isStatic = ((Boolean)model.getValueAt(row, 0)).booleanValue();
          isFinal = ((Boolean)model.getValueAt(row, 1)).booleanValue();
          isAbstract = ((Boolean)model.getValueAt(row, 2)).booleanValue();
          isNative = ((Boolean)model.getValueAt(row, 3)).booleanValue();
          isSync = ((Boolean)model.getValueAt(row, 4)).booleanValue();
          accessType = ((String)model.getValueAt(row, 5)).toString();
          returnType = ((String)model.getValueAt(row, 6)).toString();
          name = ((String)model.getValueAt(row, 7)).toString();

          dlg.setUpdateRow(row);
          dlg.setUpdateMethodName(name);

          dlg.getGeneralPanel().restoreData(isStatic, isFinal, isAbstract, isNative, isSync,
                                            accessType, returnType, name, cdOperInfoContainer.getOperInfo(name).getJavaDoc());

          dlg.getParamPanel().setParamModel((model.getParamValueAt(row)));
          dlg.setVisible(true);
          if(dlg.isOK() == true){
            updateMethodData(dlg.getUpdateMethodName(), dlg.getUpdateRow(), dlg.isStatic(), dlg.isFinal(), dlg.isAbstract(),
                             dlg.isNative(), dlg.isSync(), dlg.getAccessType(),
                             dlg.getReturnType(), dlg.getName(), dlg.getParamModel(), dlg.getJavaDoc());
          }
        }
        else if(e.getClickCount() == 1){
        }
      }
    }
  }
}
