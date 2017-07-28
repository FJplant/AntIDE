/*
 * $Id: SymbolTableEntry.java,v 1.6 1999/08/20 01:43:36 kahn Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.6 $
 * Part : Symbol Table Entry Class.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 99.2.4.
 *
 * Version 1.0.0 (starting)
 */

package com.antsoft.ant.codecontext;

import java.util.Hashtable;
import java.io.Serializable;
import java.util.Vector;

/**
  @author Kim, Sung-Hoon.
  */
public class SymbolTableEntry {  //implements Serializable {
	protected String typ;							// the type of the data 
	protected String accessTyp = "";	// the access type(modifier) of the data.

  protected String superClass = null;	// if this is class, then it is superclass
																			// otherwise, it is null.

  protected Vector implementsInterfaces = null;
															// if this is class, and implements other
															// interfaces, then this vector will has
															// values with string object.

  protected Vector parameters = null;
															// if this is method or constructor,
															// , then this vector will has
															// values with string object.

  private int startline;	// start line.
  private int endline;		// end line.
	protected int depth;			// the depth that this is located.

	private boolean realEnd = false;	// if this is class and '{' is appeared
																		// at the end of class, then readEnd 
																		// flag will be set.

	protected Hashtable table = null;	// if this is block, then table isn't null
																		// otherwise, table is null.

  protected int memberSort;	// the sort of the member.
														// It can have following constants as value.

	private boolean isStatic = false;

	// constants definition for member sort variable.
  public static final int IMPORT=0;
  public static final int CLASS=1;
  public static final int FIELD=2;
  public static final int METHOD=3;
  public static final int CONSTRUCTOR=4;
  public static final int PACKAGE=5;
  public static final int INTERFACE=6;
  public static final int OTHERS=8;
	//////////////////////////////////////////////////////

  public SymbolTableEntry() {
	}

  public SymbolTableEntry(SymbolTableEntry e) {
    typ 				= e.typ;
    accessTyp 	= e.accessTyp;
    memberSort 	= e.memberSort;

    isStatic = e.isStatic;

    superClass 	= e.superClass;
    implementsInterfaces = e.implementsInterfaces;
    parameters 	= e.parameters;

    depth 			= e.depth;
    startline 	= e.startline;
    endline 		= e.endline;

    table 			= e.table;
  }

	/////////////////////////////////////////////////////////
	// set and get method..
	// set super class when this entry is Class Declaration.
  public void setSuperClass(String su) {
    superClass=su;
  }

	// get super class name.
  public String getSuperClass() {
    return superClass;
  }

	/**
	  set the specified type.

	  @param typ type name.
	  */
	public void setType(String typ) {
		this.typ=typ;
	}

	/**
	  set the specified access type.

	  @param accessTyp access type name.
	  */
	public void setAccessType(String accessTyp) {
		this.accessTyp=accessTyp;
	}

	/**
	  set the member sort in the whole document.

	  @param memberSort member sort as the int.
	  */
	public void setMemberSort(int memberSort) {
		this.memberSort=memberSort;
	}

  public void setStatic(boolean isStatic) {
  	this.isStatic = isStatic;
  }

  public boolean isStatic() {
  	return isStatic;
  }

	/**
	  get the the type.

	  @return the type as String.
	  */
	public String getType() {
		return typ;
	}

	/**
	  get the the access type.

	  @return the access type as String.
	  */
	public String getAccessType() {
		return accessTyp;
	}

	/**
	  get the member sort in the whole document.

	  @return member sort as the final variable.
	  */
	public int getMemberSort() {
		return memberSort;
	}

  /**
     get the depth of this element.
     @return depth as int value.
    */
  public int getDepth() {
  	return depth;
  }

  /**
    set the depth of this element.

    @param d depth as int value.
    */
  public void setDepth(int d) {
  	depth=d;
  }

	// add implemented interface.
  public void addImplementsInterface(String interfaceName) {
    if (implementsInterfaces==null) implementsInterfaces = new Vector();
    implementsInterfaces.addElement(interfaceName);
  }

	// get implemented interfaces.
  public Vector getImplementsInterfaces() {
    return implementsInterfaces;
  }

	// add parameter.
  public void addParameter(String param) {
    if (parameters==null) parameters = new Vector();
    parameters.addElement(param);
  }

	// get parameters.
  public Vector getParameters() {
    return parameters;
  }

  public int getStartLine() {
    return startline;
  }

  public int getEndLine() {
    return endline;
  }

  public void setStartLine(int start) {
    startline = start;
  }

  public void setEndLine(int end) {
    endline = end;
  }

	public boolean isReallyEnd() {
	  return realEnd;
	}

	public void setReallyEnd(boolean b) {
		realEnd = b;
	}

	//////////////////////////////////////////////////////////

  public String toString() {
		return typ + " " + accessTyp + " " + memberSort;
  }

	public String toSig(String key) {
		if (parameters == null)
			return accessTyp+" "+typ+" "+key;

		int size = parameters.size();
		StringBuffer buf = new StringBuffer();
		for (int i=0;i<size;++i) {
			buf.append((String)parameters.elementAt(i));
			if (i!=size-1) buf.append(",");
		}

		return accessTyp+" "+typ+" "+key.substring(0,key.indexOf("("))+"("+buf.toString()+")";
	}
}
