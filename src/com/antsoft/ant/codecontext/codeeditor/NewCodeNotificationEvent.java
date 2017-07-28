/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/codecontext/codeeditor/NewCodeNotificationEvent.java,v 1.3 1999/07/22 03:17:51 multipia Exp $
 * $Revision: 1.3 $
 * Part : Notification Event Class of the new Generated Code.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 1999.1.29.
 */

package com.antsoft.ant.codecontext.codeeditor;

/**
  The Notification Event Class....

  @author Kim, Sung-Hoon.
  */
public class NewCodeNotificationEvent {
	// Event - new generated Class name.
	String event=null;
	String packName=null;
  String content=null;

  public void setContent(String str) {
    content = str;
  }

  public String getContent() {
    return content;
  }

	/**
	  getting the event for the event handler.

	  @return the event, class name, as the String.
	  */
	public String getEvent() {
		return event;
	}

	/**
	  setting the event for the event maker.

	  @param evt the event to be created newly, class name, as the String.
	  */
	public void setEvent(String evt) {
		event=evt;
	}

	/**
	  getting the event for the package name.

	  @return the package name.
	  */
	public String getPackageName() {
		return packName;
	}

	/**
	  setting the event for the event maker.

	  @param evt the event to be created newly, class name, as the String.
	  */
	public void setPackageName(String pack) {
		packName=pack;
	}
}
