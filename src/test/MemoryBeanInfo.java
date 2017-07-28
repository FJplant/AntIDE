/*
 *	package : test
 *	source  : MemoryBeanInfo.java
 *	date    : 1999.5.28
 */

package com.antsoft.ant.util;

import java.beans.*;
import java.awt.event.*;

public class MemoryBeanInfo extends SimpleBeanInfo {
  public MemoryBeanInfo(){
	}

	public BeanDescriptor getBeanDescriptor(){
		BeanDescriptor bd = new BeanDescriptor(MemoryBean.class);

		return bd;
	}
	//property descriptor
	public PropertyDescriptor[] getPropertyDescriptors(){
		try {
			PropertyDescriptor totalMemory = new PropertyDescriptor("totalMemory", MemoryBean.class);
			totalMemory.setBound(false);
			totalMemory.setConstrained(false);

			PropertyDescriptor freeMemory = new PropertyDescriptor("freeMemory", MemoryBean.class);
			freeMemory.setBound(false);
			freeMemory.setConstrained(false);

			PropertyDescriptor[] pds = new PropertyDescriptor[]{totalMemory,freeMemory};
			return pds;
		}catch(IntrospectionException e){
			return null;
		}

	}

	//event set descriptors
	public EventSetDescriptor[] getEventSetDescriptors(){
		try{
			String[] names1 = {"heapAllocated"};
			EventSetDescriptor ed1 = new EventSetDescriptor( MemoryBean.class,
									"MemoryEvent",
									MemoryListener.class,
									names1,
									"addMemoryListener",
									"removeMemoryListener");

			String[] names2 = {"MouseClicked","MousePressed","MouseEntered","MouseExited","MouseReleased"};
			EventSetDescriptor ed2 = new EventSetDescriptor( MemoryBean.class,
									"MouseEvent",
									MouseListener.class,
									names2,
									"addMouseListener",
									"removeMouseListener");


			EventSetDescriptor[] eda = {ed1,ed2};

			return eda;
		}catch(IntrospectionException e){
			return null;
		}
	}
}
