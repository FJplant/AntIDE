/*
 *	package : test
 *	source  : Application.java
 *	date    : 1999.5.16
 */

package test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Application { 

	Frame1 frame;

	/**
	 *  Application -  constructor 
	 */
	public Application() {

		frame = new Frame1();

		frame.pack();
		frame.setVisible( true );

	}

	public static void main( String args[] ) {

		Application app = new Application();

	}
}

