/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/codecontext/codeeditor/NewCodeNotificationListener.java,v 1.3 1999/07/22 03:17:51 multipia Exp $
 * $Revision: 1.3 $
 *
 * Part : Notification Listener of the New Generated Code.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 1999.1.29.
 */

package com.antsoft.ant.codecontext.codeeditor;

/**
  The Interface NewCodeNotificationListener....

  @author Kim, Sung-Hoon.
  */
public interface NewCodeNotificationListener {
	public void handleNotification(NewCodeNotificationEvent evt);
}
