/*
 * $Id: TestHeavyFrame.java,v 1.6 1999/08/19 05:44:51 multipia Exp $
 *	package : test
 *	source  : HeavyTestFrame.java
 *	date    : 1999.8.16
 */

package test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TestHeavyFrame extends JFrame implements ActionListener {
	JButton testWindow, testFrame, testDialog, runGC;
	WindowCloser closer;

	/**
	 *	TestHeavyFrame -  constructor 
	 */
	public TestHeavyFrame() {
		super("Test Heavy Frame");
		testWindow = new JButton("Test Window");
		testFrame = new JButton("Test Frame");
		testDialog = new JButton("Test Dialog");
		runGC = new JButton("Run GC");
		closer = new WindowCloser();
		testWindow.addActionListener(this);
		testFrame.addActionListener(this);
		testDialog.addActionListener(this);
		runGC.addActionListener(this);

		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(testWindow);
		getContentPane().add(testFrame);
		getContentPane().add(testDialog);
		getContentPane().add(runGC);
		
		addWindowListener( new WindowAdapter() {
			public void windowClosing( WindowEvent e ) {
				//TO DO (implementation here)
				dispose();
			}
			
			public void windowClosed( WindowEvent e ) {
				System.out.println("window closed");
				System.exit(0);
			}
		});	
	}

	public void actionPerformed( ActionEvent e )	{
		//TO DO 
		Window newWindow = null;
		if ( e.getSource() == testWindow ) {
			newWindow = new TestWindow(this);
		} else if ( e.getSource() == testDialog ) {
			newWindow = new TestDialog(this);
		} else if ( e.getSource() == testFrame ) {
			newWindow = new TestFrame();
		} else if ( e.getSource() == runGC ) {
			System.gc();
			System.runFinalization();
		}
		if (newWindow != null)
			newWindow.addWindowListener(closer);
	}

	/**
	 *	Main method
	 */
	public static void main(String[] args) {
		TestHeavyFrame frame = new TestHeavyFrame();
		frame.pack();
		frame.setVisible(true);		
	}
}

class TestFrame extends JFrame implements ActionListener {
	static int instance = 0;
	JButton ok;
	public TestFrame() {
		super("Frame Test");
		System.out.println("Test Frame: " + ++instance);
		ok = new JButton("Ok");
		ok.addActionListener(this);
		getContentPane().add(BorderLayout.CENTER, ok);
		pack();
		setVisible(true);
	}
	
	public void actionPerformed( ActionEvent e )	{
		//TO DO 
		ok.removeActionListener(this);
		dispose();
	}

	protected void finalize()
	{
		//TO DO (implementation here) 
		System.out.println("Test Frame: " + --instance);
	}
}

class TestWindow extends JWindow implements ActionListener {
	JButton ok;
	static int instance = 0;
	
	public TestWindow( Frame parent ) {
		super(parent);
		System.out.println("Test Window: " + ++instance);		
		ok = new JButton("Ok");
		ok.addActionListener(this);
		getContentPane().add(BorderLayout.CENTER, ok);
		pack();
		setVisible(true);
	}
	
	public void actionPerformed( ActionEvent e )	{
		//TO DO 
		ok.removeActionListener(this);
		dispose();
	}

	protected void finalize()
	{
		//TO DO (implementation here) 
		System.out.println("Test Window: " + --instance);
	}
}

class TestDialog extends JDialog implements ActionListener {
	JButton ok;
	static int instance = 0;
	
	public TestDialog( Frame parent ) {
		super(parent, "Dialog Test");
		System.out.println("Dialog Test: " + ++instance);
		ok = new JButton("Ok");
		ok.addActionListener(this);
		getContentPane().add(BorderLayout.CENTER, ok);		
		pack();
		setVisible(true);
	}
	
	public void actionPerformed( ActionEvent e ) {
		//TO DO
		ok.removeActionListener(this);
		dispose();
	}

	protected void finalize()
	{
		//TO DO (implementation here) 
		System.out.println("Dialog Test: " + --instance);
	}
}

class WindowCloser extends WindowAdapter {
	public void windowClosing( WindowEvent e )
	{
		//TO DO (implementation here)
		((Window)e.getSource()).dispose();
	}
}

