/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Part : Insight Event Wrapper Class
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 1999.7.
 */

package com.antsoft.ant.codecontext.codeeditor;

/**
  @author Kim, Sung-Hoon.
  */
public class InsightEvent {
	// for Code Completion Insight
	public static final int INNERCLASS=0;
	public static final int METHOD=1;
	public static final int MEMBER=2;
	public static final int LOCAL=3;

	// for Import Insight
	public static final int PACKAGE=4;
	public static final int CLASS=5;
	public static final int ALL=6;

	public static final int INTERFACE = 7;

	// No type for Parameterizing and Newing Insight.


	// vector of string with value to be displayed.
	private String event; // event content.
	private int type = 0; // event type(using above constants)

	private boolean isMine = false;

	// constructor.
	public InsightEvent(int i,String e,boolean b) {
	  event = e;
	  type = i;
	  isMine = b;
	}

	public InsightEvent(int i,String e) {
	  event = e;
	  type = i;
	}

	// constructor.
	public InsightEvent(String e) {
	  event = e;
	  type = 0;
	}

	public String toString() {
	  return event;
	}

	public int getEventType() {
	  return type;
	}

	public String getEvent() {
	  return event;
	}

	public boolean equals(Object o) {
	  if (!(o instanceof InsightEvent)) return false;

	  if (event == null || o.toString() == null) return false;

	  if (event.equals(o.toString())) return true;
	  else return false;
	}

	public void setIsMine(boolean b) {
		isMine = b;
	}

	public boolean isMine() {
		return isMine;
	}
}
