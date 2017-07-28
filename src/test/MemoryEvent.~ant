/*
 *	package : test
 *	source  : MemoryEvent.java
 *	date    : 1999.5.28
 */

package com.antsoft.ant.util;

import java.util.*;

public class MemoryEvent extends EventObject {
	static final int HEAP_ALLOCATED = 1;
	private int id = 0;

	public int getID() {return id;};

	MemoryEvent(Object source, int i){
		super(source);
		id = i;
	}
}
