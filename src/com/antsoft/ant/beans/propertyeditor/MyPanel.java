package com.antsoft.ant.beans.propertyeditor;

import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel{

  public int x;
  public int y;
  public int width;
  public int height;
  
  public void paint(Graphics g){
    g.drawRect(x,y,width,height);
  }
}
