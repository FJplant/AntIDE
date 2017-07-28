/*
 *	package : test
 *	source  : MyWizard.java
 *	date    : 1999.8.7
 */

package test;

import java.awt.*;
import javax.swing.*;
import java.io.*;

import com.antsoft.ant.wizard.customwizard.AbstractCustomWizard;

public class MyWizard extends AbstractCustomWizard {

	/**
	 *	constructor
	 */
	public MyWizard() {
		super(title);
		aInit();
		pack();
	}

	private void aInit() {

	}

}
