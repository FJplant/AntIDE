/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/classdesigner/CDOperInfoContainer.java,v 1.3 1999/07/22 03:37:36 multipia Exp $
 * $Revision: 1.3 $
 * Part : Class Designer Operation Information Container.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 1999.1.21.22.
 */

package com.antsoft.ant.designer.classdesigner;

import java.util.Hashtable;
import java.util.Enumeration;

/**
  @author Kim, Sung-hoon.
  */
public class CDOperInfoContainer {
	private Hashtable operInfos;

	/**
	  adding the operation information into the information list.

	  @param info the operation information as the CDOperInfo value.
	  */
	public void addOperInfo(CDOperInfo info) {
		if (operInfos==null) operInfos=new Hashtable();

		try {
			operInfos.put(info.getMethodName(),info);
		} catch (NullPointerException e) {
			System.out.println(" Just, Parameter is nullable...."+e.toString());
		}
	}

	/**
	  getting the operation information list.

	  @return the operation information list as the Enumeration value.
	  */
	public Enumeration getOperInfos() {
		if (operInfos==null) return null;

		return operInfos.elements();
	}

	/** 
	  removing the specified operation from the operation information list.
	  */
	public void delOperInfo(String name) {
		operInfos.remove(name);
	}

	/**
	  getting the specified operation from the operation information list.

	  @return the operation information that you wanna get as the CDOperInfo type.
	  */
	public CDOperInfo getOperInfo(String name) {
		return (CDOperInfo)operInfos.get(name);
	}
}
