/*
 *  MyTableCellEditor - property list의 오른쪽 칼럼을 위한 에디터
 *                      각 cell마다 다른 종류의 에디터를 가지기 위해 구현한 클래스
 *  designed by Kim Yun Kyung
 *  date 1999.7.6
 */
 
package com.antsoft.ant.uidesigner;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import javax.swing.event.*;
import java.util.*;

public class MyTableCellEditor implements TableCellEditor {

  private Component[] com;
  private Object value;
  protected transient Vector listeners;

  public MyTableCellEditor(Component[] com){
    this.com = com;
    listeners = new Vector();
  }
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    this.value = value;
    //setBackground(Color.white);
    //com[row].setBackground(Color.white);
    return com[row];
  }

  public Object getCellEditorValue() {
    return value;
  }

  public boolean isCellEditable(EventObject anEvent) {
    return true;
  }

  public boolean shouldSelectCell(EventObject anEvent) {
    return true;
  }

  public boolean stopCellEditing() {
    fireEditingStopped();
    return true;
  }

  public void cancelCellEditing() {

  }

  public void addCellEditorListener(CellEditorListener l) {
    listeners.addElement(l);
  }

  public void removeCellEditorListener(CellEditorListener l) {
    listeners.removeElement(l);
  }

  protected void fireEditingStopped(){
    ChangeEvent ce = new ChangeEvent(this);
    for(int i=listeners.size()-1; i>=0; i--){
      ((CellEditorListener)listeners.elementAt(i)).editingStopped(ce);
    }
  }

}
