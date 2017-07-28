/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/classdesigner/FieldPanel.java,v 1.8 1999/08/20 06:27:28 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.8 $
 * $History: FieldPanel.java $
 * 
 * *****************  Version 3  *****************
 * User: Remember     Date: 99-05-20   Time: 4:57p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:44p
 * Updated in $/AntIDE/source/ant/designer/classdesigner
 *                          
 * *****************  Version 2  *****************
 * User: Remember     Date: 98-10-10   Time: 3:17a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * com.sun.java -> javax
 *
 * *****************  Version 1  *****************
 * User: Remember     Date: 98-09-29   Time: 1:56a
 * Created in $/JavaProjects/src/ant/designer/classdesigner
 *
 * *****************  Version 9  *****************
 * User: Remember     Date: 98-09-23   Time: 6:35a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
 * Class 이름 바꾸었음.
 *
 * *****************  Version 8  *****************
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
 * User: Remember     Date: 98-09-07   Time: 10:06p
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
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.border.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.AbstractTableModel;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;

import com.antsoft.ant.designer.classdesigner.MethodParamPanel;
import com.antsoft.ant.util.*;

/**
 * @author  Kim sang kyun
 */
public class FieldPanel extends JPanel {

  private MyFieldTableModel model;
  private JTable fieldTable;
  private JScrollPane scrollpane;
  private JButton addButton, removeButton;
  private JTextArea docTa;

  /** get, set method생성을 위해 갖는다 */
  private MethodPanel methodPanel;

  private CDAttrInfoContainer cdAttrInfoContainer;
  public FieldPanel(CDAttrInfoContainer cdAttrInfoContainer) {
    this.cdAttrInfoContainer = cdAttrInfoContainer;
    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * method panel object의 참조 설정
   *
   * @param p methodpanel
   */
  public void setMethodPanel(MethodPanel p){
    this.methodPanel = p;
  }

  private MethodPanel getMethodPanel(){
    return this.methodPanel;
  }

  void jbInit() throws Exception {
    setBorder(BorderList.etchedBorder5);
    ActionListener actionEventHandler = new ActionEventHandler();
    MouseListener mouseEventHandler = new MouseEventHandler();

    addButton = new JButton("Add");
    addButton.setActionCommand("Add");
    addButton.addActionListener(actionEventHandler);

    removeButton = new JButton("Remove");
    removeButton.setActionCommand("Remove");
    removeButton.addActionListener(actionEventHandler);

    //by lila
    JPanel p1 = new JPanel();
    p1.setLayout(new FlowLayout(FlowLayout.CENTER));
    p1.add(addButton);
    p1.add(removeButton);

    model = new MyFieldTableModel();
    fieldTable = new JTable(model);

    fieldTable.setRowSelectionAllowed(true);
    fieldTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    fieldTable.addMouseListener(mouseEventHandler);
    fieldTable.setBorder(BorderList.bebelBorder);

    scrollpane = new JScrollPane(fieldTable);
    scrollpane.setPreferredSize(new Dimension(550,220));

    //by lila
    JPanel p2 = new JPanel();
    p2.setLayout(new BorderLayout(0,5));
    TitledBorder border = new TitledBorder(new EtchedBorder(),"Field Table");
    border.setTitleFont(FontList.regularFont);
    border.setTitleColor(Color.black);
    p2.setBorder(border);
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

  public void updateFieldData(int row, String prevName, boolean isStatic, boolean isFinal,
                              boolean isTransient, boolean isVolatile,
                              String accessType, String type, String name,
                              String initial, boolean isGetter, boolean isSetter, String doc){

    model.setValueAt(new Boolean(isStatic), row, 0);
    model.setValueAt(new Boolean(isFinal), row, 1);
    model.setValueAt(new Boolean(isTransient), row, 2);
    model.setValueAt(new Boolean(isVolatile), row, 3);

    if(accessType != null) model.setValueAt(accessType, row, 4);
    else model.setValueAt("", row, 4);

    if(type != null) model.setValueAt(type, row, 5);
    else model.setValueAt("", row, 5);

    if(name != null) model.setValueAt(name, row, 6);
    else model.setValueAt("", row, 6);

    if(initial != null) model.setValueAt(initial, row, 7);
    else model.setValueAt("", row, 7);

    model.setValueAt(new Boolean(isGetter), row, 8);
    model.setValueAt(new Boolean(isSetter), row, 9);

    //java doc comment
    if(doc != null) model.setDocValueAt(doc, row);
    else model.setDocValueAt("", row);

    //CDAttrInfo setting
    CDAttrInfo attrInfo = new CDAttrInfo();
    attrInfo.setStatic(isStatic);
    attrInfo.setFinal(isFinal);
    attrInfo.setTransient(isTransient);
    attrInfo.setVolatile(isVolatile);
    attrInfo.setAccessType(accessType);
    attrInfo.setType(type);
    attrInfo.setAttrName(name);
    attrInfo.setInitValue(initial);
    attrInfo.setGetter(isGetter);
    attrInfo.setSetter(isSetter);

    cdAttrInfoContainer.delAttrInfo(prevName);
    cdAttrInfoContainer.addAttrInfo(attrInfo);
  }

  public void addFieldData(boolean isStatic, boolean isFinal, boolean isTransient,
                           boolean isVolatile, String accessType, String type,
                           String name, String initial, boolean isGetter,
                           boolean isSetter, String doc){

    int row = model.getRowCount();

    model.setValueAt(new Boolean(isStatic), row, 0);
    model.setValueAt(new Boolean(isFinal), row, 1);
    model.setValueAt(new Boolean(isTransient), row, 2);
    model.setValueAt(new Boolean(isVolatile), row, 3);
    if(accessType != null) model.setValueAt(accessType, row, 4);
    else model.setValueAt("", row, 4);

    if(type != null) model.setValueAt(type, row, 5);
    else model.setValueAt("", row, 5);

    if(name != null) model.setValueAt(name, row, 6);
    else model.setValueAt("", row, 6);

    if(initial != null) model.setValueAt(initial, row, 7);
    else model.setValueAt("", row, 7);

    model.setValueAt(new Boolean(isGetter), row, 8);
    model.setValueAt(new Boolean(isSetter), row, 9);

    //java doc comment
    if(doc != null) model.setDocValueAt(doc, row);
    else model.setDocValueAt("", row);

    model.fireTableRowsInserted(row, row);

    //CDAttrInfo setting
    CDAttrInfo attrInfo = new CDAttrInfo();
    attrInfo.setStatic(isStatic);
    attrInfo.setFinal(isFinal);
    attrInfo.setTransient(isTransient);
    attrInfo.setVolatile(isVolatile);
    attrInfo.setAccessType(accessType);
    attrInfo.setType(type);
    attrInfo.setAttrName(name);
    attrInfo.setInitValue(initial);
    attrInfo.setGetter(isGetter);
    attrInfo.setSetter(isSetter);
    attrInfo.setJavaDoc(doc);

    cdAttrInfoContainer.addAttrInfo(attrInfo);
  }

  class MyFieldTableModel extends AbstractTableModel {
    final String[] columnNames = {"static", "final", "transient", "volatile",
                                  "access type", "type", "name", "initial",
                                  "getter", "setter"};

    Hashtable data = new Hashtable(200);
    Hashtable doc = new Hashtable();

    public MyFieldTableModel(){}

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

    public Object getDocValueAt(int row){
      String key = new Integer(row).toString();
      return doc.get(key);
    }

    public Class getColumnClass(int c) {
      return getValueAt(0, c).getClass();
    }

    public void setValueAt(Object value, int row, int col) {
      String key = makeKey(row, col);
      data.put(key, value);
      fireTableCellUpdated(row, col);
    }

    public void setDocValueAt(Object value, int row){
      String key = new Integer(row).toString();
      doc.put(key, value);
    }

    public void removeValueAt(int row){
      int rowCount = getRowCount();
      if(row != rowCount-1){
        for(int i=row; i<rowCount-1; i++){
          for(int j=0; j<10; j++){
            setValueAt(getValueAt(i+1, j), i, j);
          }
        }
      }
      for(int i=0; i<10; i++){
        String lastKey = makeKey(rowCount-1, i);
        data.remove(lastKey);
      }
      fireTableRowsDeleted(row-1, row);
      doc.remove(new Integer(row).toString());
    }

    private String makeKey(int row, int col){
      return  new Integer(row).toString() + "***" + new Integer(col).toString();
    }
  }

  //Action event handler
  class ActionEventHandler implements ActionListener {
    private String actionCommand;
    public void actionPerformed(ActionEvent e){
      actionCommand = e.getActionCommand();
      if(actionCommand.equals("Add")){
        FieldDialog dlg = new FieldDialog(ClassDesigner.parent, "Field Add Dialog", true, FieldDialog.ADD);
        dlg.setVisible(true);

        if(dlg.isOK() == true){
          MethodPanel p = getMethodPanel();

          if(dlg.isGetter()){
            String attrname = dlg.getName();
            char firstCh = attrname.charAt(0);
            char upperCh = Character.toUpperCase(firstCh);
            String getMethodname = "get"+upperCh+attrname.substring(1);

            MyParamTableModel param = new MyParamTableModel();

            p.addMethodData(false, false, false, false, false, "public", dlg.getType(), getMethodname,  param, new JavaDocInfo());
          }
          if(dlg.isSetter()){
            String attrname = dlg.getName();
            char firstCh = attrname.charAt(0);
            char upperCh = Character.toUpperCase(firstCh);
            String setMethodname = "set"+upperCh+attrname.substring(1);

            MyParamTableModel param = new MyParamTableModel();
            param.setValueAt(dlg.getType(), 0, 0);
            param.setValueAt(attrname, 0, 1);
            param.setValueAt("", 0, 2);
            param.setValueAt("", 0, 3);

            p.addMethodData(false, false, false, false, false, "public", "void", setMethodname,
                            param, new JavaDocInfo());
          }
          addFieldData(dlg.isStatic(), dlg.isFinal(), dlg.isTransient(), dlg.isVolatile(),
                       dlg.getAccessType(), dlg.getType(), dlg.getName(), dlg.getInitial(),
                       dlg.isGetter(), dlg.isSetter(),dlg.getDoc());
        }
      }

      else if(actionCommand.equals("Remove")){
        int index = fieldTable.getSelectedRow();
        if(index > -1){
          cdAttrInfoContainer.delAttrInfo((String)model.getValueAt(index, 6));
          model.removeValueAt(index);
        }
      }
    }
  }

  class MouseEventHandler extends MouseAdapter{
    private boolean isStatic, isFinal, isTransient, isVolatile, isGetter, isSetter;
    private String accessType, type, name, initval, doc;

    public void mouseClicked(MouseEvent e){
      int row = fieldTable.getSelectedRow();
      int lowHeight = fieldTable.getRowHeight();
      int clickedRow = (int)e.getY()/lowHeight;

      if(row > -1 && clickedRow < model.getRowCount()){
        if(e.getClickCount() == 2){
          FieldDialog dlg = new FieldDialog(ClassDesigner.parent, "Field Update Dialog", true,
                                            FieldDialog.UPDATE);

          isStatic = ((Boolean)model.getValueAt(row, 0)).booleanValue();
          isFinal = ((Boolean)model.getValueAt(row, 1)).booleanValue();
          isTransient = ((Boolean)model.getValueAt(row, 2)).booleanValue();
          isVolatile = ((Boolean)model.getValueAt(row, 3)).booleanValue();
          accessType = ((String)model.getValueAt(row, 4)).toString();
          type = ((String)model.getValueAt(row, 5)).toString();
          name = ((String)model.getValueAt(row, 6)).toString();
          initval = ((String)model.getValueAt(row, 7)).toString();
          isGetter = ((Boolean)model.getValueAt(row, 8)).booleanValue();
          isSetter = ((Boolean)model.getValueAt(row, 9)).booleanValue();

          doc = (String)model.getDocValueAt(row);
          dlg.setUpdateRow(row);
          dlg.setUpdateFieldName(name);
          dlg.restoreData(isStatic, isFinal, isTransient, isVolatile, accessType, type, name, initval, isGetter, isSetter, doc);
          dlg.setVisible(true);

          if(dlg.isOK() == true){
            updateFieldData(dlg.getUpdateRow(), dlg.getUpdateFieldName(), dlg.isStatic(), dlg.isFinal(), dlg.isTransient(), dlg.isVolatile(),
                            dlg.getAccessType(), dlg.getType(), dlg.getName(), dlg.getInitial(),
                            dlg.isGetter(), dlg.isSetter(), dlg.getDoc());
          }
        }
        else if(e.getClickCount() == 1){
          String field = (String)model.getValueAt(row, 4);
          String doc = (String)model.getDocValueAt(row);
        }
      }
    }
  }
}
