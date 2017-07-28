package com.antsoft.ant.uidesigner;

import javax.swing.*;
import java.awt.*;
import java.beans.*;
import java.awt.event.*;

public class PropertyDimension extends JTextField implements ActionListener,FocusListener{

  private PropertyEditor editor;
  private String sel;
  private Dimension dim;

  public PropertyDimension(PropertyEditor editor,String sel) {
    this.editor = editor;
    this.sel = sel;
    dim = (Dimension)editor.getValue();
    if(sel.equals("width")){
      setText(Integer.toString(dim.width));
    }else if(sel.equals("height")){
      setText(Integer.toString(dim.height));
    }else{
      System.out.println("Dimension - invailed sel");
    }
  }

  public void focusLost(FocusEvent e){
    if(sel.equals("width"))
      ((Dimension)(editor.getValue())).width = Integer.parseInt(getText());
    else if(sel.equals("height"))
      ((Dimension)(editor.getValue())).height = Integer.parseInt(getText());
    else System.out.println("Dimension - invailed sel");
  }

  public void focusGained(FocusEvent e){
  }

  public void actionPerformed(ActionEvent e){
    if(sel.equals("width"))
      ((Dimension)(editor.getValue())).width = Integer.parseInt(getText());
    else if(sel.equals("height"))
      ((Dimension)(editor.getValue())).height = Integer.parseInt(getText());
    else System.out.println("Dimension - invailed sel");
  }
}