/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/classdesigner/MyParamTableModel.java,v 1.3 1999/07/22 03:37:44 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 */
package com.antsoft.ant.designer.classdesigner;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

/**
 * Param Table Model class
 *
 * @author ±è»ó±Õ
 */

  class MyParamTableModel extends AbstractTableModel {

    final String[] columnNames = {"Type", "Name", "JavaDoc Comment"};
    Hashtable data = new Hashtable();

    public MyParamTableModel(){}

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
      String key = new Integer(row).toString() + new Integer(col).toString();
      return data.get(key);
    }

    public Class getColumnClass(int c) {
      return getValueAt(0, c).getClass();
    }

    public void setValueAt(Object value, int row, int col) {
      String key = new Integer(row).toString() + new Integer(col).toString();
      data.put(key, value);
      fireTableRowsInserted(row-1,row);
    }

    public void removeValueAt(int row){

      int rowCount = getRowCount();

      if(row != rowCount-1){
        for(int i=row; i<rowCount-1; i++){
          for(int j=0; j<4; j++){
            setValueAt(getValueAt(i+1, j), i, j);
          }
        }
      }

      for(int i=0; i<3; i++){
        String lastKey = new Integer(rowCount-1).toString() + new Integer(i).toString();
        data.remove(lastKey);
      }

      fireTableRowsDeleted(row-1, row);
    }
  }


