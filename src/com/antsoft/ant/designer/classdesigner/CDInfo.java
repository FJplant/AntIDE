/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/designer/classdesigner/CDInfo.java,v 1.3 1999/07/22 03:37:36 multipia Exp $
 * $Revision: 1.3 $
 * Part : entire class designer information including all sub information.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 1999.1.21.22.
 */

package com.antsoft.ant.designer.classdesigner;

/**
  @author Kim, Sung-hoon.
  */
public class CDInfo {
	/** This field contains the information of the all attributes. */
	private CDAttrInfoContainer cdAttrInfoContainer;

	/** This field contains the information of the all operations. */
	private CDOperInfoContainer cdOperInfoContainer;

	/** This field contains the information of the class, except the
	  information of the attributes and operations. */
	private CDGeneralInfo cdGeneralInfo;

	/**
	  setting the class designer attribute information container.

	  @param info the attribute information container reference.
	  */
	public void setCDAttrInfoContainer(CDAttrInfoContainer info) {
		cdAttrInfoContainer=info;
	}

	/**
	  setting the class designer operation information container.

	  @param info the operation information container reference.
	  */
	public void setCDOperInfoContainer(CDOperInfoContainer info) {
		cdOperInfoContainer=info;
	}

	/**
	  setting the class designer general information container.

	  @param info the general information container reference.
	  */
	public void setCDGeneralInfo(CDGeneralInfo info) {
		cdGeneralInfo=info;
	}

	/**
	  getting the class designer attribute information container.

	  @return the attribute information container reference.
	  */
	public CDAttrInfoContainer getCDAttrInfoContainer() {
		return cdAttrInfoContainer;
	}

	/**
	  getting the class designer operation information container.

	  @return the operation information container reference.
	  */
	public CDOperInfoContainer getCDOperInfoContainer() {
		return cdOperInfoContainer;
	}

	/**
	  getting the class designer general information container.

	  @return the general information container reference.
	  */
	public CDGeneralInfo getCDGeneralInfo() {
		return cdGeneralInfo;
	}
}
