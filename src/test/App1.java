/*
 *	package : test
 *	source  : App1.java
 *	date    : 1999.9.19
 */

package test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class App1 { 

	Frame1 frame;

	/**
	 *  App1 -  constructor 
	 */
	public App1() {
		frame = new Frame1();

		frame.pack();
		frame.setVisible( true );
	}

	public static void main( String args[] ) {
		App1 app = new App1();
	}

}
