/*
 *	package : test
 *	source  : CustomListener.java
 *	date    : 1999.8.29
 */

package test;

import java.util.*;

public interface CustomListener extends EventListener {
	public void windowClosing(CustomEvent e);
	public void windowClosed(CustomEvent e);
}
