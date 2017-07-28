/*
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/codecontext/ClassMemberContainerList.java,v 1.3 1999/07/22 06:02:08 multipia Exp $
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Revision: 1.3 $
 * Part : Class Member Container List Class for the intellisense search.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Starting at 1999. 2. 10.
 */

package com.antsoft.ant.codecontext;

import java.util.Hashtable;

/**
  @author Kim, sung-hoon.
  */
public class ClassMemberContainerList {
	private Hashtable table;

	/**
	  default Constructor.
	  */
	public ClassMemberContainerList() {}

	/**
	  putting the Object with specified key string into the table.

	  @param key the key of the hash table(full name with package name).
	  @param object the object to be stored.
	  */
	public void putClassMemberList(String key,ClassMemberContainer object) {
		if (table==null) table=new Hashtable();

		try {
			table.remove(key);
			table.put(key,object);
		} catch (NullPointerException e) {
			System.out.println("ClassMemberContainerList putElement()..NullPointerException.");
		}
	}

	/**
	  getting the object of the specified full name key from the hashtable.

	  @param key the using key for getting ClassMemberList
	  @return the Class Member Container Object.
	  */
	public ClassMemberContainer getClassMemberList(String key) {
		if (key == null) return null;
		if (table == null) return null;
		return (ClassMemberContainer)table.get(key);
	}

	public void removeClassMemberList(String key) {
		if (table != null) table.remove(key);
	}
}
