/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/classdesigner/CDAttrInfoContainer.java,v 1.3 1999/07/22 03:37:36 multipia Exp $
 * $Revision: 1.3 $
 * Part : Class Designer Attribute Information Container.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 1999.1.21.22.
 */

package com.antsoft.ant.designer.classdesigner;

import java.util.Hashtable;
import java.util.Enumeration;

/**
  @author Kim, Sung-hoon.
  */
public class CDAttrInfoContainer {
	private Hashtable attrInfos;

	/**
	  adding the attribute information into the information list.

	  @param info the attribute information to be added.
	  */
	public void addAttrInfo(CDAttrInfo info) {
		if (attrInfos==null) attrInfos=new Hashtable();

		try {
			attrInfos.put(info.getAttrName(),info);
		} catch (NullPointerException e) {
			System.out.println(" Just, Parameter is nulluable");
		}
	}

	/**
	  getting the attribute information list as the Enumeration object.

	  @return the attribute information list as the enumeration.
	  */
	public Enumeration getAttrInfos() {
		if (attrInfos==null) return null;

		return attrInfos.elements();
	}

	/**
	  removing the specified parameter into the information list.
	  */
	public void delAttrInfo(String name) {
		attrInfos.remove(name);
	}

	/**
	  getting the attribute information by one as the CDAttrInfo type.

	  @return the attribute information.
	  */
	public CDAttrInfo getAttrInfo(String name) {
		return (CDAttrInfo)attrInfos.get(name);
	}
}
