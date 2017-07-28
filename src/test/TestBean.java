/*
 *	package : test
 *	source  : TestBean.java
 *	date    : 1999.8.8
 */
package test;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.beans.*;

public class TestBean extends JLabel
											implements Runnable {
	//properties
	String state;

	/**
	 *	get method
	 */
	public String getState(){
		return state;
	}

	/**
	 *	set method
	 */
	public void setState(String state){
		this.state = state;
	}

	/**
	 *	overring method
	 */
	public void paint(Graphics g){

	}

	///////////////////// event 처리 부분 /////////////////////////
	public void addPropertyChangeListener(PropertyChangeListener l){
		if(changes == null)
			changes = new PropertyChangeSupport(this);
		changes.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l){
		if(changes != null)
			changes.removePropertyChangeListener(l);
	}

	public void addVetoableChangeListener(VetoableChangeListener l){
		if (veto == null)
			veto = new VetoableChangeSupport(this);
		veto.addVetoableChangeListener(l);
	}

	public void removeVetoableChangeListener(VetoableChangeListener l){
		if (veto != null)
			veto.removeVetoableChangeListener(l);
	}

	private PropertyChangeSupport changes;
	private VetoableChangeSupport veto;

	private transient Vector testListeners;
	private transient Vector focusListeners;

	public synchronized void addTestListener(TestListener l){
		if(testListeners == null)
			testListeners = new Vector();
		testListeners.addElement(l);
	}

	public synchronized void removeTestListener(TestListener l){
		if(testListeners != null && testListeners.contains(l))
			testListeners.removeElement(l);
	}

	public void fireActionPerformed(TestEvent e){
		processTestListener(new TestEvent(this,TestEvent.ACTION_EVENT));
	}

	protected void processTestListener(TestEvent e){
		switch( e.getID() ){
		case TestEvent.ACTION_EVENT:
			for(int i=0; i<testListeners.size(); i++)
				((TestListener)testListeners.elementAt(i)).actionPerformed(e);
			break;
		}
	}

	public synchronized void addFocusListener(FocusListener l){
		if(focusListeners == null)
			focusListeners = new Vector();
		focusListeners.addElement(l);
	}

	public synchronized void removeFocusListener(FocusListener l){
		if(focusListeners != null && focusListeners.contains(l))
			focusListeners.removeElement(l);
	}

	protected void fireFocusGained(FocusEvent e){
		if(focusListeners != null){
			int count = focusListeners.size();
			for(int i = 0; i < count; i++)
				((FocusListener)focusListeners.elementAt(i)).focusGained(e);
		}
	}

	protected void fireFocusLost(FocusEvent e){
		if(focusListeners != null){
			int count = focusListeners.size();
			for(int i = 0; i < count; i++)
				((FocusListener)focusListeners.elementAt(i)).focusLost(e);
		}
	}
}
