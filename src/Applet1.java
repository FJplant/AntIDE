/**
 *  @source  : Applet1.java
 *  @date    : 1999.7.28
 */
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Applet1 extends JApplet {
	/**
	 *  init 
	 */
	public void init() {
		// parameter parsing
	}

	/**
	 *  start
	 */
	public void start() {
		this.add();
	}

	/**
	 *  paint
	 */
	public void paint() {
	}

	/** WindowEvent Handler Inner Class */
	class WindowEventHandler extends WindowAdapter
	{
		public void windowOpened( WindowEvent e )
		{
			//TO DO (implementation here)
		}

		public void windowClosing( WindowEvent e )
		{
			//TO DO (implementation here)
		}

		public void windowClosed( WindowEvent e )
		{
			//TO DO (implementation here)
		}

		public void windowIconified( WindowEvent e )
		{
			//TO DO (implementation here)
		}

		public void windowDeiconified( WindowEvent e )
		{
			//TO DO (implementation here)
		}

		public void windowActivated( WindowEvent e )
		{
			//TO DO (implementation here)
		}

		public void windowDeactivated( WindowEvent e )
		{
			//TO DO (implementation here)
			
		}
	}

	/** 이름	 */
	public String name = "권영모";

	/**
	 * Sets the name
	 * 
	 * @param name
	 */
	public void setName ( String name )	
	{
		//TO DO (implementation here)
		this.name = name;
	}

	/**
	 * Gets the name
	 * 
	 * @return name
	 */
	public String getName ()
	{
		// TO DO 
		return name;
	}

	public String toString()
	{
		//TO DO (implementation here) 
		return super.toString();
	}
}
