package com.antsoft.ant.uidesigner;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;

public class PropertySelection extends JComboBox implements ItemListener{

  PropertyEditor editor;

  public PropertySelection(PropertyEditor editor){
    super();
    this.editor = editor;
    setRenderer(new SelectionRenderer());
    String[] tags = editor.getTags();
    for(int i=0; i<tags.length; i++){
      addItem(tags[i]);
    }
    try{
      setSelectedItem(editor.getAsText());

    }catch(Exception ex){
      this.setSelectedIndex(0);
    }
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

  class SelectionRenderer extends BasicComboBoxRenderer {
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){
      Component com = super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
      list.setBackground(Color.white);
      return com;
    }
  }
}
