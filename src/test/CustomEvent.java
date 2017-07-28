/*
 *	package : test
 *	source  : CustomEvent.java
 *	date    : 1999.8.29
 */

package test;

import java.util.*;

public class CustomEvent extends EventObject {
	static final int WINDOW_CLOSING = 1;
	static final int WINDOW_CLOSED = 2;

	private int id = 0;

	CustomEvent(Object source, int i){
		super(source);
		id = i;
	}

	public int getID() {return id;}
}
