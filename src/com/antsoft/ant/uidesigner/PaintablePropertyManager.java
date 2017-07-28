/*
 *  PaintablePropertyManager - paintable한 property의 editor를 만드는 클래스
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
   *  getColorComponent - property가 color type일때 editor component를 만들어 넘겨준다
   */
  public JComboBox getColorComponent(){
    JComboBox combo = null;
    try{
      //ComboBox를 만든다
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
   *  getFontComponent - property가 font type일때 editor component를 만들어 넘겨준다
   */
  public JPanel getFontComponent(){
    PropertyPanel panel = new PropertyPanel(editor,this);
    return panel;
  }

  /**
   *  getFontType - font type의 이름을 반환한다
   *  @type - int type의 font type
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