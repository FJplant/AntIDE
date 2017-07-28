package com.antsoft.ant.uidesigner;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;

public class PropertyText extends JTextField implements ActionListener,FocusListener{

  private PropertyEditor editor;
  private String type;

  public PropertyText(PropertyEditor newEditor,String type){
    try{
      this.editor = newEditor;
      this.type = type;
      if(type.equals("int")){
        if(editor.getValue() != null){
          System.out.println("int");
          setText(" "+(String)editor.getValue());
        }else{
          System.out.println("int null");
          setText("");
        }
      }else{
        if(newEditor.getAsText() == null)setText("");
        else setText(" "+editor.getAsText());
      }

      addActionListener(this);
      addFocusListener(this);
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  public void repaint(){
  /*
    if(editor == null)System.out.println("editor is null");
    System.out.println("get text-"+editor.getAsText());
    setText(editor.getAsText());
    */
  }

  public void focusLost(FocusEvent e){
    if(type.equals("int"))
      editor.setValue(getText());

    else
      editor.setAsText(getText());
  }

  public void focusGained(FocusEvent e){
  }
  public void actionPerformed(ActionEvent e){
    if(type.equals("int"))
      editor.setValue(getText());
    else
      editor.setAsText(getText());
  }

}
