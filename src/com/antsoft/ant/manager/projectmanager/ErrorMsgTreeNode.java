/*
 * $Id: ErrorMsgTreeNode.java,v 1.4 1999/08/31 04:58:34 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.4 $
 */
package com.antsoft.ant.manager.projectmanager;

import javax.swing.tree.*;

/**
 *  class ErrorMsgTreeNode
 *
 *  @author Jinwoo Baek
 */
public class ErrorMsgTreeNode extends DefaultMutableTreeNode {
	// Message Type
	public static final int ERROR   = 1;
  public static final int WARNING = 2;
  public static final int CUSTOM  = 3;

	private String file = "";
  private int line = -1;
  private String msg = "";                           
  private int type = -1;

  /**
   *  Constructor
   */
	public ErrorMsgTreeNode(String file, int line, String msg, int msgType) {
  	this.file = file;
    this.line = line;
    this.msg = msg;
    this.type = msgType;
  }

  public String toString() {
  	switch (type) {
    case ERROR:
	  	return "Error:[" + line + "]" + msg;
    case WARNING:
	  	return "Warning:[" + line + "]" + msg;
    case CUSTOM:
	  	return msg;
    }
    return "";
  }

  public String getFile() {
  	return file;
  }

  public int getLine() {
  	return line;
  }

  public String getMessage() {
  	return msg;
  }

  public int getType() {
  	return type;
  }

  public boolean equals(Object obj) {
  	if (obj instanceof ErrorMsgTreeNode) {
    	ErrorMsgTreeNode emtn = (ErrorMsgTreeNode)obj;
			if ((file != null) && file.equalsIgnoreCase(emtn.getFile()) &&
      		(type == emtn.getType()) && (line == emtn.getLine()) &&
          (msg != null) && msg.equalsIgnoreCase(emtn.getMessage()))
      	return true;
    }
    return false;
  }
}
/*
 * $Log: ErrorMsgTreeNode.java,v $
 * Revision 1.4  1999/08/31 04:58:34  multipia
 * no message
 *
 */
