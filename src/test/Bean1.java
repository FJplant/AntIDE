/*
 *	package : test
 *	source  : Bean1.java
 *	date    : 1999.8.29
 */

package test;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.beans.*;

public class Bean1
{
	//properties
	String name;
	String address;
	
	/**
	 *	get method
	 */
	public String getName(){
		return name;
	}

	/**
	 *	set method
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 *	get method
	 */
	public String getAddress(){
		return address;
	}

	/**
	 *	set method
	 */
	public void setAddress(String address){
		this.address = address;
	}

	///////////////////// event ?й°вм? им?имив /////////////////////////
	public void addPropertyChangeListener(PropertyChangeListener l){
		if(changes == null)
			changes = new PropertyChangeSupport(this);
		changes.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l){
		if(changes != null)
			changes.removePropertyChangeListener(l);
	}

	private PropertyChangeSupport changes;
}
