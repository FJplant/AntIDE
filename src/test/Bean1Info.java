/*
 *	package : test
 *	source  : Bean1Info.java
 *	date    : 1999.8.29
 */

package test;

import java.beans.*;
import java.awt.event.*;

public class Bean1Info extends SimpleBeanInfo {

	public Bean1Info(){
	}

	/**
	 *	bean descriptor
	 */
	public BeanDescriptor getBeanDescriptor(){
		BeanDescriptor bd = new BeanDescriptor(Bean1.class);

		return bd;
	}

	/**
	 *	property descriptor
	 */
	public PropertyDescriptor[] getPropertyDescriptors(){
		try {
			PropertyDescriptor name = new PropertyDescriptor("name", Bean1.class);
			name.setBound(true);
			name.setConstrained(false);

			PropertyDescriptor address = new PropertyDescriptor("address", Bean1.class);
			address.setBound(false);
			address.setConstrained(false);

			PropertyDescriptor[] pds = new PropertyDescriptor[]{name,address};
			return pds;
		}catch(IntrospectionException e){
			return null;
		}

	}

