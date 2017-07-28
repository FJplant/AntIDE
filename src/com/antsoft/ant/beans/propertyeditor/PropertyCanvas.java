package com.antsoft.ant.beans.propertyeditor;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;

public class PropertyCanvas extends Canvas implements MouseListener{
  private PropertyEditor editor;
  private Frame frame;

  public PropertyCanvas(Frame frame,PropertyEditor editor){
    this.frame = frame;
    this.editor = editor;
    addMouseListener(this);
  }

  public void paint(Graphics g){
    Rectangle box = new Rectangle(2,2,getSize().width-4,getSize().height-4);
    editor.paintValue(g,box);

    System.out.println("paint called");
  }

  public void mouseClicked(MouseEvent e){
    PropertyDialog dlg = new PropertyDialog(frame,editor);
    dlg.setVisible(true);
  }
  public void mouseEntered(MouseEvent e){
  }
  public void mousePressed(MouseEvent e){
  }
  public void mouseReleased(MouseEvent e){
  }
  public void mouseExited(MouseEvent e){
  }
}
