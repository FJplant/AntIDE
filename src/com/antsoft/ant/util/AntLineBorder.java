/*
 * $Id: AntLineBorder.java,v 1.3 1999/08/25 10:53:14 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 */
package com.antsoft.ant.util;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class AntLineBorder extends AbstractBorder {
  private Color color;
  private int thick;
  private boolean left, right, top, bottom;

  public AntLineBorder(Color color, int thickness, boolean left, boolean right, boolean top, boolean bottom){
    this.color = color;
    this.thick = thickness;
    this.left = left;
    this.right = right;
    this.bottom = bottom;
    this.top = top;
  }

  public AntLineBorder(Color color, boolean left, boolean right, boolean top, boolean bottom){
    this(color, 1, left, right, top, bottom);
  }

  public AntLineBorder(boolean left, boolean right, boolean top, boolean bottom){
    this(Color.black, 1, left, right, top, bottom);
  }

  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height){

    Color oldColor = g.getColor();
    g.setColor(color);
    int x1 = x;
    int y1 = y;
    int x2 = x + width;
    int y2 = y + height;

    for(int i=0; i<thick; i++){

      if(right) g.drawLine(x2-i-1, y1, x2-i-1, y2);
      if(top) g.drawLine(x1, y1+i+1, x2, y1+i+1);
      if(bottom) g.drawLine(x1, y2-i-1, x2, y2-i-1);
      if(left) g.drawLine(x1+i+1, y1, x1+i+1, y2);
    }
  }
} 
