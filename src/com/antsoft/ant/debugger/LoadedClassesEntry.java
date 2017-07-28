/*
 *	package : com.antsoft.ant.debugger
 *	source  : LoadedClassesEntry.java
 *	date    : 1999.8.9
 *  
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:    Lee, Chul Mok
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/LoadedClassesEntry.java,v 1.1 1999/08/09 05:41:13 itree Exp $
 * $Revision: 1.1 $
 * $History: LoadedClassesEntry.java $
 */

package com.antsoft.ant.debugger;

public class LoadedClassesEntry{ 

	public static final int ROOT 				= 1;
	public static final int ROOTJAVA  	= 2;
	public static final int ROOTUSER  	= 3;
	public static final int ROOTSUN   	= 4;
	public static final int JDKPACKAGE  = 5;
	public static final int SUNPACKAGE  = 6;
	public static final int USERPACKAGE = 7;	
	public static final int JDK   			= 8;
	public static final int SUN					= 9;
	public static final int USER  			= 10;
	
	private String name = null;
	private String fullName = null;
	private int type;
	/**
	 *	LoadedClassesEntry -  constructor 
	 */	 
	public LoadedClassesEntry(String name, String fullName, int type) {
		this.name = name;
		this.fullName = fullName;
		this.type = type;		
	}
	
	//////////////////////////////////////////////////////
	// Get Set Functions
	public String getFullName ()
	{
		// TO DO 
		return fullName;
	}
	
	public void setFullName ( String fullName )
	{
		// TO DO 
		this.fullName = fullName;
	}
	
	public String getName ()
	{
		// TO DO 
		return name;
	}
	
	public void setName ( String name )
	{
		// TO DO 
		this.name = name;
	}
	
	public int getType ()
	{
		// TO DO 
		return type;
	}
	
	public void setType ( int type )
	{
		// TO DO 
		this.type = type;
	}
	// End of Get Set Functions
	//////////////////////////////////////////////////////
	public String toString() {
		return name;
	}
}
