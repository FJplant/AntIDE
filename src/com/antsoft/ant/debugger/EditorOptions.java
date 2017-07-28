package com.antsoft.ant.debugger;

import java.awt.Color;
import java.awt.Font;

/**
 *  interface EditorOptions
 *
 *  @author Jinwoo Baek
 */
public interface EditorOptions {
  public Color getStringColor();
  public Color getBGColor();
  public Color getKeywordColor();
  public Color getConstantColor();
  public Color getCommentColor();
  public Font getFont();
  public void addBreakpoint(int line);
  public void removeBreakpoint(int line);
}