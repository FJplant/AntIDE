package com.antsoft.ant.beans.propertyeditor;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;

public class PropertySelection extends JComboBox implements ItemListener{

  PropertyEditor editor;

  public PropertySelection(PropertyEditor editor){
    super();
    this.editor = editor;
    String[] tags = editor.getTags();
    for(int i=0; i<tags.length; i++){
      addItem(tags[i]);
    }
    setSelectedItem(editor.getAsText());
    addItemListener(this);
  }

  public void itemStateChanged(ItemEvent e){
    Object obj = getSelectedItem();
    editor.setAsText((String) obj);
  }

  /*public void repaint(){
    if(editor.getAsText() != null)
      setSelectedItem(editor.getAsText());
  }*/
}
