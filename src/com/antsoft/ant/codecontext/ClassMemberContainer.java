/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/codecontext/ClassMemberContainer.java,v 1.3 1999/07/22 06:00:58 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * author: Kim, Sung-hoon(kahn@antj.com)
 * Starting at 1999. 2. 10.
 */

package com.antsoft.ant.codecontext;

import java.util.*;

/**
  @author Kim, sung-hoon.
  */
public class ClassMemberContainer {
	/**
	  default Constructor.
	  */
	public ClassMemberContainer() {}

	private Vector container = null;

	/**
	  setting the Class Member Container with the given object.

	  @param object the Class Member Container Object.
	  */
	public void setContainer(Vector object) {
		container=object;
	}

	/**
	  adding the Class Member with the given ClassMember Object.

	  @param object Class Member Object.
	  */
	public void addContainer(ClassMember object) {
		if (container==null) container=new Vector();
		container.addElement(object);
	}

	/**
	  getting the ClassMemberContainer with Vector object.

   @return the Class Memebr Container as Vector.
	  */
	public Vector getContainer() {
		return container;
	}

	/**
	  getting the ClassMemberContainer with Enumeration type.

	  @return the Class Memebr Container as Enumeration.
	  */
	public Enumeration getContainerAsEnum() {
		if (container != null) return container.elements();
		else return null;
	}
}
