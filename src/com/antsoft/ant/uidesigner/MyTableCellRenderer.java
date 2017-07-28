/*
 *  table row마다 다른 render를 가지기 위해 구현된 클래스
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