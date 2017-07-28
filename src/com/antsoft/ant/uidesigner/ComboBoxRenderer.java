/*
 *  ComboRenderer - ComboBox의 리스트 색깔을 바꾸기 위한 renderer
 *  designed by Kim Yun Kyung
 *  date 1999.7.7
 */

package com.antsoft.ant.uidesigner;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;

public class ComboBoxRenderer extends BasicComboBoxRenderer{

  private ImageIcon[] images;
  private String[] colorName;
  private JLabel lastSel;

  public ComboBoxRenderer(String[] colorName){
    this.colorName = colorName;
    images = new ImageIcon[colorName.length];
    for( int i=0; i<colorName.length; i++){
      images[i] = new ImageIcon(getClass().getResource(colorName[i]+".jpg"));
    }
  }

  public JLabel getLastSelected(){
    return lastSel;
  }

  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
    list.setBackground(Color.white);
    //System.out.println("index - "+index);
    if(index == -1){
      int i = list.getSelectedIndex();
      setIcon(images[i]);
      setText((String)list.getSelectedValue());
      setHorizontalTextPosition(SwingConstants.RIGHT);
    }else{
      //System.out.println("index - "+index);
      setIcon(images[index]);
      setText((String)value);
      setHorizontalTextPosition(SwingConstants.RIGHT);
    }

    if(isSelected) lastSel = this;
    return this;
  }


}