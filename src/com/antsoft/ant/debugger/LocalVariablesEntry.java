/*
 *	package : com.antsoft.ant.debugger
 *	source  : LocalVariablesEntry.java
 *	date    : 1999.8.14
 *
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:    Lee, Chul Mok
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/LocalVariablesEntry.java,v 1.3 1999/08/24 05:50:22 itree Exp $
 * $Revision: 1.3 $
 * $History: LocalVariablesEntry.java $
 */

package com.antsoft.ant.debugger;

import sun.tools.debug.*;

public class LocalVariablesEntry {
  public static final int GENERAL = 100;
  public static final int ARRAY   = 101;
  public static final int OBJECT  = 102;
  public static final int STRING  = 103;

  public static final int ROOT    = 200;
  public static final int LOCAL   = 201;
  public static final int METHOD  = 202;

  private String name = null;
  private String value = null;
  private String typeName = null;
  private String description = null;
  private RemoteObject object = null;
  private int entryType;

  public LocalVariablesEntry(String name, String value, String typeName, String description, RemoteObject obj, int entryType) {
    this.name = name;
    this.value = value;
    this.typeName = typeName;
    this.description = description;
    this.object = obj;
    this.entryType = entryType;
  }

  public String getName() {
    return name;
  }
  public String getValue() {
    return value;
  }
  public void setValue(String str) {
    value = str;
  }
  public String getTypeName() {
    return typeName;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String str) {
    description = str;
  }
  public RemoteObject getRemoteObject() {
    return object;
  }
  
  public int getEntryType() {
    return entryType;
  }

  public String toString() {
    return name + " : ";
  }
}

