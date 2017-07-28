/*
 *  table row���� �ٸ� render�� ������ ���� ������ Ŭ����
 *  Designed by Kim Yun Kyung
 *  Date : 1999.6.28
 */
package com.antsoft.ant.uidesigner;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class MyTableCellRenderer extends DefaultTableCellRenderer{
  private Component[] com;

  public MyTableCellRenderer() {
  }

  /**
   *  overriden method
   */
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
    return com[row];
  }
  
  /**
   *  setComponent
   */
  public void setComponent(Component[] com){
     this.com = com;
  }
} 