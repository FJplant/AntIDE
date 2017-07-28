/*
 *	package : test
 *	source  : App2.java
 *	date    : 1999.8.26
 */
package test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class App2 { 
	Frame1 frame;

	/**
	 *  App2 -  constructor 
	 */
	public App2() {
		frame = new Frame1();

		frame.pack();
		frame.setVisible( true );
	}

	public static void main( String args[] ) {
		App2 app = new App2();
	}
}
