/*
 * $Header: /AntIDE/source/ant/codecontext/ClassDesignerListener.java 3     99-05-17 12:41a Multipia $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 3 $
 * author: Kim, Sung-hoon(kahn@antj.com)
 */

package com.antsoft.ant.codecontext;

/**
  The listener interface for receiving the ClassDesinger action.
  The some inner class of the CodeContext must implement this interface,
  and the object created with that class is regitered with the component,
  using the addClassDesignerListener method. When the action occurred,
  the corresponding method is invoked.

  @author Kim, Sung-Hoon.
  @see CodeContext class
  */
public abstract interface ClassDesignerListener {

	/**
	  When the ClassDesigner create the OK action, this method is invoked.
	  */
	public void okAction(CDListenerEvent e);
}

