/*
 *	package : com.antsoft.ant.debugger
 *	source  : MethodEntry.java
 *	date    : 1999.8.10
 *  
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:    Lee, Chul Mok
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/MethodEntry.java,v 1.1 1999/08/11 07:21:00 itree Exp $
 * $Revision: 1.1 $
 * $History: MethodEntry.java $
 */

package com.antsoft.ant.debugger;

import sun.tools.debug.*;

public class MethodEntry{ 

	/**
	 *	MethodEntry -  constructor 
	 */
	public static final int PUBLIC 			= 1;
	public static final int PRIVATE			= 2;
	public static final int PROTECTED 	= 3;

	private String totalname = null;
	private String name = null;
	private RemoteField remoteField = null;
	private String modifiers = null;
	private boolean isStatic = false;
	private boolean isConstructor = false;
	private int modifierType;
	
	public MethodEntry(String tn, String n, RemoteField rf, String m, boolean isS, boolean isC, int mt) {
		this.totalname = tn;
		this.name = n;
		this.remoteField = rf;
		this.modifiers = m;
		this.isStatic = isS;
		this.isConstructor = isC;
		this.modifierType = mt;		
	}
	//////////////////////////////////////////////////////////////
	// Get Functions
	/**
	 * Gets the isConstructor
	 * 
	 * @return isConstructor
	 */
	public boolean getIsConstructor ()
	{
		// TO DO 
		return isConstructor;
	}

	/**
	 * Gets the isStatic
	 * 
	 * @return isStatic
	 */
	public boolean getIsStatic ()
	{
		// TO DO 
		return isStatic;
	}

	/**
	 * Gets the modifiers
	 * 
	 * @return modifiers
	 */
	public String getModifiers ()
	{
		// TO DO 
		return modifiers;
	}

	/**
	 * Gets the modifierType
	 * 
	 * @return modifierType
	 */
	public int getModifierType ()
	{
		// TO DO 
		return modifierType;
	}
	/**
	 * Gets the remoteFiled
	 * 
	 * @return remoteFiled
	 */
	public RemoteField getRemoteField ()
	{
		// TO DO 
		return remoteField;
	}

	
	/**
	 * Gets the totalname
	 * 
	 * @return totalname
	 */
	public String getTotalname ()
	{
		// TO DO 
		return totalname;
	}
	// End Get Functions
	/////////////////////////////////////////////////////////////////
	
	public String toString() {
		return totalname;
	}	

	/**
	 * Gets the name
	 * 
	 * @return name
	 */
	public String getName ()
	{
		// TO DO 
		return name;
	}
}
