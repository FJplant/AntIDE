/*
 *  MyCellRenderer - property list의 오른쪽 칼럼을 위한 랜더러
 *  designed by Kim Yun Kyung
 *  date 1999.7.6
 */
package com.antsoft.ant.uidesigner;

import javax.swing.table.*;
import java.awt.*;
import javax.swing.*;
import java.lang.*;

public class MyCellRenderer extends JLabel implements TableCellRenderer{

  private Component[] com;

  public MyCellRenderer(Component[] com){
    this.com = com;
    JPanel p = new JPanel();
    setBackground(p.getBackground());
    setFont(new Font("Arial",Font.PLAIN,11));
    setForeground(Color.black);
  }
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    //TextField인 경우
    if(com[row] instanceof JTextField)
      setText(" "+((JTextField)com[row]).getText());

    //PropertySelection인 경우
    else if(com[row] instanceof PropertySelection)
      setText(" "+(String)((JComboBox)com[row]).getSelectedItem());

    //JComboBox(Color)인 경우
    else if(com[row] instanceof JComboBox){
       JComboBox box = (JComboBox)com[row];
       ComboBoxRenderer p = (ComboBoxRenderer)box.getRenderer();
       Component r = p.getLastSelected();
       if(r != null) return r;
       else setText("");

    //PropertyPanel(Font)인 경우
    }else if(com[row] instanceof PropertyPanel){
      PropertyPanel panel = (PropertyPanel)com[row];
      Font font = panel.getFont();
      int style = font.getStyle();
      String styleName = null;
      if(style == Font.BOLD) styleName = "BOLD";
      else if(style == Font.ITALIC) styleName = "ITALIC";
      else if(style == Font.PLAIN) styleName = "PLAIN";
      else System.out.println("font styleName is null");
      setText(" "+font.getName()+", "+styleName+", "+Integer.toString(font.getSize()));
    }else setText("");
    return this;
  }
}
