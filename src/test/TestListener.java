/*
 *	package : test
 *	source  : TestListener.java
 *	date    : 1999.8.8
 */

package test;

import java.util.*;

public interface TestListener extends EventListener {
	public void actionPerformed(TestEvent e);
}

