/*
 *  PaintablePropertyManager - paintable�� property�� editor�� ����� Ŭ����
 *  designed by Kim Yun Kyung
 *  date 1999.7.7
 */

package com.antsoft.ant.uidesigner;

import java.beans.*;
import java.awt.*;
import javax.swing.*;

public class PaintablePropertyManager {
  private PropertyEditor editor;
  public static String[] colorName = {"black","blue","cyan","darkGray","gray","green","lightGray","magenta","orange",
                "pink","red","white","yellow"};
  private Color[] color = {Color.black,Color.blue,Color.cyan,Color.darkGray,Color.gray,Color.green,Color.lightGray,
                Color.magenta,Color.orange,Color.pink,Color.red,Color.white,Color.yellow};
  public static String[] fontList = {"BOLD","ITALIC","PLAIN"};
            
  public PaintablePropertyManager(PropertyEditor editor) {
    this.editor = editor;

  }
  /**
   *  getColorComponent - property�� color type�϶� editor component�� ����� �Ѱ��ش�
   */
  public JComboBox getColorComponent(){
    JComboBox combo = null;
    try{
      //ComboBox�� �����
      combo = new JComboBox();
      combo.setRenderer(new ComboBoxRenderer(colorName));
      for(int j=0; j<color.length; j++){
        combo.addItem(colorName[j]);
      }
    }catch(Exception ex){
      ex.printStackTrace();

    }
    return combo;
  }

  /**
   *  getFontComponent - property�� font type�϶� editor component�� ����� �Ѱ��ش�
   */
  public JPanel getFontComponent(){
    PropertyPanel panel = new PropertyPanel(editor,this);
    return panel;
  }

  /**
   *  getFontType - font type�� �̸��� ��ȯ�Ѵ�
   *  @type - int type�� font type
   */
  public String getFontType(int type){
    String typeName = null;
    if(type == Font.BOLD) typeName = fontList[0];
      else if(type == Font.ITALIC) typeName = fontList[1];
      else if(type == Font.PLAIN) typeName = fontList[2];
      else System.out.println("font styleName is null");
    return typeName;
  }
}