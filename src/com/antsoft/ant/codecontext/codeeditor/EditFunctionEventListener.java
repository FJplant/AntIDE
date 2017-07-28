/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/codecontext/codeeditor/EditFunctionEventListener.java,v 1.3 1999/07/22 03:17:51 multipia Exp $
 * $Revision: 1.3 $
 * Part : Edit Functoin Event Listener Interface.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 1999.2.2.
 */

package com.antsoft.ant.codecontext.codeeditor;

/**
  @author Kim, Sung-Hoon.
  */
public interface EditFunctionEventListener {
	/**
	  the event is occurred, this method is invoked.
	  */
	public void showEventList(EditFunctionEvent e);
}
