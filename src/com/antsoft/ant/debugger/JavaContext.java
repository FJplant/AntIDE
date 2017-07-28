/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/JavaContext.java,v 1.2 1999/08/05 05:09:43 strife Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.2 $
 * $History: JavaContext.java $
 * 
 * *****************  Version 8  *****************
 * User: Multipia     Date: 99-05-17   Time: 12:22a
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * 
 * *****************  Version 7  *****************
 * User: Multipia     Date: 99-04-29   Time: 12:33p
 * Updated in $/AntIDE/source/ant/pool/sourcepool
 * JavaView 클래스를 밖으로 빼내었습니다
 *
 * *****************  Version 1  *****************
 * User: Insane       Date: 98-09-12   Time: 5:18p
 * Created in $/Ant/src/ant/sourcepool
 * 소스 관리자
 *                              
 */
package com.antsoft.ant.debugger;

import java.awt.*;
import java.util.Vector;

import javax.swing.text.*;

/**
 *  class JavaContext
 *
 *  @author Jinwoo Baek
 */
public class JavaContext extends StyleContext implements ViewFactory {
  // --- ViewFactory methods -------------------------------------

  public View create(Element elem) {
    return new JavaView(this, elem);
  }

  Color string = new Color(0, 0, 0);
	Color background = new Color(255, 255, 255);
	Color keyword = new Color(0, 0, 255);
	Color comment = new Color(0, 153, 0);
	Color constant = new Color(255, 0, 0);
	Font font = new Font("Courier", Font.PLAIN, 12);

  public Color getStringColor() {
    return string;
  }

  public Color getBGColor() {
    return background;
  }

  public Color getKeywordColor() {
    return keyword;
  }

  public Color getCommentColor() {
    return comment;
  }

  public Color getConstantColor() {
    return constant;
  }

  public void setStringColor(Color clr) {
    this.string = clr;
  }

  public void setBGColor(Color clr) {
    this.background = clr;
  }

  public void setKeywordColor(Color clr) {
    this.keyword = clr;
  }

  public void setConstantColor(Color clr) {
    this.constant = clr;
  }

  public void setCommentColor(Color clr) {
    this.comment = clr;
  }
}
