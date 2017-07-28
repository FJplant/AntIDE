/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/codecontext/codeeditor/EventContent.java,v 1.10 1999/08/30 07:59:07 remember Exp $
 * $Revision: 1.10 $
 * Part : Event Content Class.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 1999.2.8.
 */

package com.antsoft.ant.codecontext.codeeditor;

import java.util.Vector;

/**
  @author Kim, Sung-Hoon.
  */
public class EventContent {
	public static final int CLASS=0;
	public static final int INTERFACE=1;
	public static final int INNER=2;
	public static final int OPER=3;
	public static final int ATTR=4;
	public static final int IMPORT=5;
	public static final int PACKAGE=6;
  public static final int FILEROOTNODE=7;
  public static final int IMPORTROOTNODE=8;

	// has content as the String.
	String content;
	// has content type(i.e CLASS, ATTR, OPER, INNER, IMPORT...)
	int contentType;

  private EventContent parent;

	public EventContent(String c,int t) {
		content=c;
		contentType=t;
    parent = null;
	}

	public EventContent() {
  	content = null;
    parent = null;
  }

  public EventContent getParent() {
  	return parent;
  }

  public void setParent(EventContent p) {
		parent = p;
  }

	/**
	  setting the content with specified String value.

	  @param content the specified content.
	  */
	public void setContent(String content) {
		this.content=content;
	}

	/**
	  setting the content type with specified int value.

	  @param content the specified content.
	  */
	public void setContentType(int type) {
		contentType=type;
	}

	/**
	  getting the content of this Object.

	  @return the this content value as the String value.
	  */
	public String getContent() {
		return content;
	}

	/**
	  getting the content type of this Object.

	  @return the the content type as the int value.
	  */
	public int getContentType() {
		return contentType;
	}

  public String toString() {
    if(contentType == INNER) return content.substring(content.lastIndexOf(".")+1);
    return content;
  }

  public boolean equals(Object o){
    if(o == null) return false;
    if(o instanceof EventContent) {
      EventContent comp = (EventContent)o;
      if(comp.getContent().equals(content)) return true;
      else return false;
    }
    else return false;
  }

  /**
   * hashtable에서 사용할 키를 리턴한다
   */
  public String getHashtableKey(){
    String key = content;
    EventContent parent = getParent();
    while(parent != null){
      key = parent.getContent() + "." + key;
      parent = parent.getParent();
    }
    return key;
  }
}
