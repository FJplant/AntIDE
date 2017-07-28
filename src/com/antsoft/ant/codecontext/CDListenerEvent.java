/*
 * $Header: /AntIDE/source/ant/codecontext/CDListenerEvent.java 3     99-05-17 12:41a Multipia $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 3 $
 * author: Kim, Sung-hoon(kahn@antj.com)
 * Starting at 1999.1.23.
 */

package com.antsoft.ant.codecontext;

import java.util.EventObject;
import com.antsoft.ant.codecontext.classdesigner.CDInfo;

/**
  @author Kim, Sung-Hoon.

  Event Class for Class Designer Listener.
  */
public class CDListenerEvent extends EventObject {
	CDInfo event;

	/**
	  Constructor with the event contents.

	  @param s the object on which the Event initially occurred.
	  @param e the event specification.
	  */
	public CDListenerEvent(Object s,CDInfo e) {
		this(s);
		event=e;
	}

	/**
	  Constructor only with the source object.

	  @param s the object on which the Event initially occurred.
	  @param e the event specification.
	  */
	public CDListenerEvent(Object s) {
		super(s);
	}

	/**
	  Setting the event after Constructor only with the source object
	  is invoked.

	  @param e the event specification.
	  */
	public void setEvent(CDInfo e) {
		event=e;
	}

	/**
	  Getting the event on the object that implements the Listener interface.

	  @param e the event specification.
	  */
	public CDInfo getEvent() {
		return event;
	}
}

