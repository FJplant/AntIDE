/*
 *	package : test
 *	source  : Dialog1.java
 *	date    : 1999.7.29
 */

package test;

import java.awt.Graphics;
import javax.swing.*;

public class Dialog1 extends JDialog{ 
	public void update( Graphics p0 )
	{
		//TO DO (implementation here) 
		p0.draw3DRect(10, 15, 30, 20, true);
	}
}
