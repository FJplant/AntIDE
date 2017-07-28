package com.antsoft.ant.beans.propertyeditor;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;

public class PropertyText extends JTextField implements ActionListener,FocusListener{

  private PropertyEditor editor;

  public PropertyText(PropertyEditor newEditor){
    super(newEditor.getAsText());
    System.out.println("newEditor-"+newEditor);
    this.editor = newEditor;
    addActionListener(this);
    addFocusListener(this);
  }

  public void repaint(){
  /*
    if(editor == null)System.out.println("editor is null");
    System.out.println("get text-"+editor.getAsText());
    setText(editor.getAsText());
    */
  }

  public void focusLost(FocusEvent e){

    editor.setAsText(getText());
  }

  public void focusGained(FocusEvent e){
  }
  public void actionPerformed(ActionEvent e){
    editor.setAsText(getText());
  }

}
