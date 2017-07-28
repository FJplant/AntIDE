
/*
 *  LabelCellRenderer - property list의 왼쪽 칼럼을 위한 랜더러 클래스
 *  designed by Kim Yun Kyung
 *  date 1999.7.6
 */
package com.antsoft.ant.uidesigner;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

public class LabelCellRenderer extends JLabel implements TableCellRenderer{

  private SoftBevelBorder border1;
  private SoftBevelBorder border2;

  public LabelCellRenderer(){

    border1 = new SoftBevelBorder(SoftBevelBorder.RAISED);
    border2 = new SoftBevelBorder(SoftBevelBorder.LOWERED);
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    setFont(new Font("Arial",Font.PLAIN,12));
    setForeground(Color.black);
    setText(" "+(String)value);

    if(isSelected){
      setBorder(border2);
    }else{
      setBorder(border1);
    }
    return this;
  }
}
