/*
 *  DimensionEditor - int type¿« property editor
 *  designed by Kim Yun Kyung
 *  date 1999.7.10
 */
package com.antsoft.ant.uidesigner;

import java.awt.*;
import java.beans.*;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Component;

public class DimensionEditor extends PropertyEditorSupport{
  protected int width;
  protected int height;

  public Object getValue(){
    return new Dimension(width,height);
  }

  public void setValue(Object value){
    width = ((Dimension)value).width;
    height = ((Dimension)value).height;
  }
}
