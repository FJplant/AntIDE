/*
 *	package : test
 *	source  : MemoryListener.java
 *	date    : 1999.5.28
 */

package com.antsoft.ant.util;

import java.util.*;

public interface MemoryListener extends EventListener {
	public void heapAllocated(MemoryEvent e);
}
