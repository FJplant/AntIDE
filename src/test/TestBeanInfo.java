/*
 *	package : test
 *	source  : TestBeanInfo.java
 *	date    : 1999.8.8
 */

package test;

import java.beans.*;
import java.awt.event.*;

public class TestBeanInfo extends SimpleBeanInfo {

	public TestBeanInfo(){
	}

	/**
	 *	bean descriptor
	 */
	public BeanDescriptor getBeanDescriptor(){
		BeanDescriptor bd = new BeanDescriptor(TestBean.class);

		return bd;
	}

	/**
	 *	property descriptor
	 */
	public PropertyDescriptor[] getPropertyDescriptors(){
		try {
			PropertyDescriptor state = new PropertyDescriptor("state", TestBean.class);
			state.setBound(true);
			state.setConstrained(true);

			PropertyDescriptor[] pds = new PropertyDescriptor[]{state};
			return pds;
		}catch(IntrospectionException e){
			return null;
		}

	}

	/**
	 *	event set descriptors
	 */
	public EventSetDescriptor[] getEventSetDescriptors(){
		try{
			String[] names1 = {"actionPerformed"};
			EventSetDescriptor ed1 = new EventSetDescriptor( TestBean.class,
									"TestEvent",
									TestListener.class,
									names1,
									"addTestListener",
									"removeTestListener");

			String[] names2 = {"FocusGrained","FocusLost"};
			EventSetDescriptor ed2 = new EventSetDescriptor( TestBean.class,
									"FocusEvent",
									FocusListener.class,
									names2,
									"addFocusListener",
									"removeFocusListener");


			EventSetDescriptor[] eda = {ed1,ed2};

			return eda;
		}catch(IntrospectionException e){
			return null;
		}
	}
}
