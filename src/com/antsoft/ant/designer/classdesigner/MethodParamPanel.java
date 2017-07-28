/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/classdesigner/MethodParamPanel.java,v 1.6 1999/08/20 06:27:33 remember Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.6 $
 * $History: MethodParamPanel.java $
 * 
 * *****************  Version 3  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:44p
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
 * User: Remember     Date: 98-09-29   Time: 1:57a
 * Updated in $/JavaProjects/src/ant/designer/classdesigner
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
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import com.antsoft.ant.util.*;

/**
 * @author  Kim sang kyun
 */
public class MethodParamPanel extends JPanel {

  private MyParamTableModel model;
  private JTable paramTable;
  private JButton addBtn, removeBtn;

  public MethodParamPanel() {
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
    //param table
    model = new MyParamTableModel();
    paramTable = new JTable(model);

    paramTable.setRowSelectionAllowed(true);
    paramTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    paramTable.addMouseListener(mouseEventHandler);
    paramTable.setBorder(BorderList.bebelBorder);

    paramTable.getColumn("Type").setPreferredWidth(80);
    paramTable.getColumn("Name").setPreferredWidth(80);
    paramTable.getColumn("JavaDoc Comment").setPreferredWidth(120);


    JScrollPane scrollpane = new JScrollPane();
    scrollpane.setPreferredSize(new Dimension(280,190));
    scrollpane.setViewportView(paramTable);

    //add, remove buttons
    addBtn = new JButton("Add");
    addBtn.setActionCommand("Add");
    addBtn.addActionListener(actionEventHandler);

    removeBtn = new JButton("Remove");
    removeBtn.setActionCommand("Remove");
    removeBtn.addActionListener(actionEventHandler);

    //by lila
    JPanel p1 = new JPanel();
    p1.setLayout(new FlowLayout(FlowLayout.CENTER));
    p1.add(addBtn);
    p1.add(removeBtn);

    JPanel p2 = new JPanel();
    p2.setLayout(new BorderLayout());
    TitledBorder border = new TitledBorder(new EtchedBorder(),"Method Parameter Table");
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

  public void addParamData(String type, String name, String desc){
    int row = model.getRowCount();
    model.setValueAt(type, row, 0);
    model.setValueAt(name, row, 1);
    model.setValueAt(desc, row, 2);
  }

  public void updateParamData(int row, String type, String name, String desc){
    model.setValueAt(type, row, 0);
    model.setValueAt(name, row, 1);
    model.setValueAt(desc, row, 2);
 }

  public void setParamModel(MyParamTableModel model){
    int rowCount = model.getRowCount();
    for(int i=0; i<rowCount; i++){
      addParamData((String)model.getValueAt(i, 0), (String)model.getValueAt(i, 1),
                   (String)model.getValueAt(i, 2));
    }
  }

  public MyParamTableModel getParamModel(){
    return model;
  }
    //Action event handler
  class ActionEventHandler implements ActionListener {
    private String actionCommand;
    public void actionPerformed(ActionEvent e){
      actionCommand = e.getActionCommand();
      if(actionCommand.equals("Add")){
        MethodParamDialog dlg = new MethodParamDialog(ClassDesigner.parent, "Param Add Dialog", true,
                                    MethodParamDialog.ADD);
        dlg.setVisible(true);

        if(dlg.isOK() == true){
            addParamData( dlg.getType(), dlg.getName(), " " + dlg.getDesc());
        }
      }
      else if(actionCommand.equals("Remove")){
        int index = paramTable.getSelectedRow();
        if(index > -1){
          model.removeValueAt(index);
        }
      }
    }
  }

  class MouseEventHandler extends MouseAdapter{
    private String type, name, initval, desc;

    public void mouseClicked(MouseEvent e){
      if(e.getClickCount() == 2){
        int row = paramTable.getSelectedRow();
        if(row > -1){
          MethodParamDialog dlg = new MethodParamDialog(ClassDesigner.parent, "Param Update Dialog",
                                        true, MethodParamDialog.UPDATE);
          type = (String)model.getValueAt(row, 0);
          name = (String)model.getValueAt(row, 1);
          desc = (String)model.getValueAt(row, 2);

          dlg.setUpdateRow(row);
          dlg.restoreData(type, name, desc);
          dlg.setBounds(150, 150, 250, 200);
          dlg.setVisible(true);

          if(dlg.isOK() == true){
            updateParamData( row, dlg.getType(), dlg.getName(),
                             dlg.getDesc());
          }
        }
      }
    }
  }
}

