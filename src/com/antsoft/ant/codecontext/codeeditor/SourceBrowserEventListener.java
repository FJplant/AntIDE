/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/codecontext/codeeditor/SourceBrowserEventListener.java,v 1.4 1999/08/02 00:30:16 kahn Exp $
 * $Revision: 1.4 $
 * Part : Source Browser Event Listener Interface.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 1999.2.2.
 */

package com.antsoft.ant.codecontext.codeeditor;

/**
  @author Kim, Sung-Hoon.
  */
public interface SourceBrowserEventListener {
	/**
	  when insertEvent occurs, this method is invoked.
	  */
	public void insertEvent(SourceBrowserEvent e);

	/*
	public void replaceEvent(SourceBrowserEvent e);
	public void removeEvent(SourceBrowserEvent e);
  public void clearEvent();
   */
}
