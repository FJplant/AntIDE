/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/codecontext/codeeditor/EditFunctionEvent.java,v 1.4 1999/07/23 05:28:18 kahn Exp $
 * $Revision: 1.4 $
 * Part : Edit Functoin Event Class.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 1999.2.2.
 */

package com.antsoft.ant.codecontext.codeeditor;

import java.util.Vector;

/**
  @author Kim, Sung-Hoon.
  */
public class EditFunctionEvent {
	public static final int INTELLISENSE=1;
	public static final int PARAMETERING=2;
	public static final int IMPORTING=3;
	public static final int NEWING=4;
	public static final int IMPLEMENTS = 5;

	// vector of string with value to be displayed.
	Vector events;

	public void setEvents(Vector v) {
	  events = v;
	}

	public Vector getEvents() {
	  return events;
	}

	/**
	  adding the one value into the string list.

	  @param e the string value to be added.
	  */
	public void addEvent(InsightEvent e) {
		if (events==null) events=new Vector();

		events.addElement(e);
	}

	int evtCounter;

	/**
	  get the first value from the string list.

	  @return the string value to be displayed.
	  */
	public InsightEvent firstEvent() {
		evtCounter=0;

        if (events==null) return null;

		if (evtCounter<events.size()) return (InsightEvent)events.elementAt(evtCounter);
		else return null;
	}

	/**
	  get the next value from the string list.

	  @return the string value to be displayed.
	  */
	public InsightEvent nextEvent() {
		evtCounter++;

        if (events==null) return null;

		if (evtCounter<events.size()) return (InsightEvent)events.elementAt(evtCounter);
		else return null;
	}

	/**
	  verifies that the events field has more element(event) or not.

	  @return true if has more element, otherwise false.
	  */
	public boolean hasMoreEvents() {
		int cnt=evtCounter+1;

        if (events==null) return false;

		if (cnt<events.size()) return true;
		else return false;
	}

	// the event type.
	int eventType;

	/**
	  sets the event type with the specified type.

	  @param type the specified event type as int value.
	  */
	public void setEventType(int type) {
		eventType=type;
	}

	/**
	  gets the event type.

	  @return the event type with int value.
	  */
	public int getEventType() {
		return eventType;
	}
}
