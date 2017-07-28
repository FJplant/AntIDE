/*
 *	package : test
 *	source  : TestEvent.java
 *	date    : 1999.8.8
 */

package test;

import java.util.*;

public class TestEvent extends EventObject {

	static final int ACTION_EVENT = 1;

	private int id = 0;

	/**
	 *	constructor
	 */
	TestEvent(Object source, int i) {		
		super(source);
		id = i;
	}

	public int getID() {return id;}
}
