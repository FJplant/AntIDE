/*
 *	package : test
 *	source  : HeavyTestFrame.java
 *	date    : 1999.8.15
 */

package test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HeavyTestFrame extends JFrame implements ActionListener { 
	JButton testDialog, testFrame, testWindow, gcBtn;
	/**
	 *	HeavyTestFrame -  constructor 
	 */
	public HeavyTestFrame() {
		super("HeavyTestFrame");
		testDialog = new JButton("Test Dialog");
		testFrame = new JButton("Test Frame");
		testWindow = new JButton("Test Window");
		gcBtn = new JButton("Run GC");
		
		testDialog.addActionListener(this);
		testFrame.addActionListener(this);
		testWindow.addActionListener(this);
		gcBtn.addActionListener(this);
		
		addWindowListener(  new WindowAdapter() {
			public void windowClosing( WindowEvent e ) {
				//TO DO (implementation here)
				dispose();
				System.exit(0);
			}
		});

		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(testDialog);
		getContentPane().add(testFrame);
		getContentPane().add(testWindow);
		getContentPane().add(gcBtn);
		pack();
		setVisible(true);
	}

	public void actionPerformed( ActionEvent e ) {
		if ( e.getSource() == testDialog ) {
			new TestDialog(this);
		} else if ( e.getSource() == testFrame ) {
			new TestFrame();
		} else if ( e.getSource() == testWindow ) {
			new TestWindow(this);
		} else if ( e.getSource() == gcBtn ) {
			System.gc();
			System.runFinalization();			
		}		
	}
	
	/**
	 *	Main method
	 */
	public static void main(String[] args) {
		HeavyTestFrame frame = new HeavyTestFrame();		
	}
}

class TestDialog extends JDialog implements ActionListener {
	static int instance = 0;
	JButton ok = new JButton("Ok");
	public TestDialog(Frame parent) {
		super(parent, "Test Dialog");

		System.out.println("Test Dialog: " + ++instance);
		ok.addActionListener(this);
		getContentPane().setLayout( new BorderLayout() );
		getContentPane().add(BorderLayout.CENTER, ok);
		pack();
		setVisible(true);
	}
	
	public void dispose()	{
		//TO DO (implementation here)
		super.dispose();
	}

	public void actionPerformed( ActionEvent e ) {
		//ok.removeActionListener(this);
		dispose();
	}

	protected void finalize()
	{
		//TO DO (implementation here) 		
		System.out.println("Test Dialog: " + --instance);
	}
}

class TestFrame extends JFrame implements ActionListener {
	static int instance = 0;
	JButton ok = new JButton("Ok");
	public TestFrame() {
		super("Test Frame");
		System.out.println("Test Frame: " + ++instance);

		ok.addActionListener(this);
		getContentPane().setLayout( new BorderLayout() );
		getContentPane().add(BorderLayout.CENTER, ok);
		pack();
		setVisible(true);		
	}
	
	public void dispose()	{
		//TO DO (implementation here)
		super.dispose();
	}

	public void actionPerformed( ActionEvent e ) {
		//ok.removeActionListener(this);
		dispose();
	}

	protected void finalize()
	{
		//TO DO (implementation here) 		
		System.out.println("Test Frame: " + --instance);
	}
}

class TestWindow extends JWindow implements ActionListener {
	static int instance = 0;
	JButton ok = new JButton("Ok");	
	public TestWindow(Frame parent) {
		super(parent);
		System.out.println("Test Window: " + ++instance);

		ok.addActionListener(this);
		getContentPane().setLayout( new BorderLayout() );
		getContentPane().add(BorderLayout.CENTER, ok);
		pack();
		setVisible(true);
	}
	
	public void actionPerformed( ActionEvent e ) {
		//ok.removeActionListener(this);
		dispose();
	}

	public void dispose()	{
		//TO DO (implementation here) 
		super.dispose();
	}
	
	protected void finalize()	{
		//TO DO (implementation here) 		
		System.out.println("Test Window: " + --instance);
	}
}

