/*
 *	package : test
 *	source  : MemoryBean.java
 *	date    : 1999.5.28
 */

package com.antsoft.ant.util;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.beans.*;

/**
 * 현재 Virtual Machine상의 메모리 할당 상태를 표시해 주는 Bean입니다.
 * 추후, Graphical하게 변경할 예정입니다.
 */
public class MemoryBean extends JPanel implements Runnable {
	//property
	long totalMemory;
	long freeMemory;
	/** 화면 갱신 간격 시간 */
	long updateInterval;

	private volatile boolean running = true;
	private Thread myThread;
	protected JLabel memoryStatus;

	/**
	 * 생성자
	 */
  public MemoryBean() {
    memoryStatus = new JLabel("/ KB");
    updateInterval = 1000;
    uiInit();    
		myThread = new Thread( this, "Memory-Status-Thread" );
		myThread.setPriority(Thread.NORM_PRIORITY);
		myThread.setDaemon(true);
		myThread.start();
  }
  
  protected void uiInit() {
    setLayout( new BorderLayout() );
    add( memoryStatus, BorderLayout.CENTER );    
  }
  
  //methods
	public long getTotalMemory(){
		return totalMemory;
	}

	public long getFreeMemory(){
		return freeMemory;
	}

  public void setUpdateInterval( long mili ) {
  	updateInterval = mili;
  }
  
	public void stop() {
		running = false;
	}
	
  /**
   * running thread
   */
  public void run( ) {
		while ( running ) {
			totalMemory = Runtime.getRuntime().totalMemory();
			freeMemory = Runtime.getRuntime().freeMemory();
			int usage = (int)((double)(totalMemory-freeMemory)/totalMemory*100);
			memoryStatus.setText( usage + "%" + ", allocated: " + totalMemory/1024 + "KB" );
			try {
				Thread.currentThread().sleep(updateInterval);
			} catch ( InterruptedException e ) {
				// just skip....
			}
		}		
	}
	
  private transient Vector memoryListeners;
	private transient Vector mouseListeners;

	public synchronized void addMemoryListener(MemoryListener l){
		if(memoryListeners == null)
			memoryListeners = new Vector();
		memoryListeners.addElement(l);
	}

	public synchronized void removeMemoryListener(MemoryListener l){
		if(memoryListeners != null && memoryListeners.contains(l))
			memoryListeners.removeElement(l);
	}

	protected void fireHeapAllocated(MemoryEvent e){
		if(memoryListeners != null){
			int count = memoryListeners.size();
			for(int i = 0; i < count; i++)
				((MemoryListener)memoryListeners.elementAt(i)).heapAllocated(e);
		}
	}

	protected void processMemoryListener(MemoryEvent e){
		switch( e.getID() ){
		case MemoryEvent.HEAP_ALLOCATED:
			for(int i=0; i<memoryListeners.size(); i++)
				((MemoryListener)memoryListeners.elementAt(i)).heapAllocated(e);
			break;
		}
	}

	public synchronized void addMouseListener(MouseListener l){
		if(mouseListeners == null)
			mouseListeners = new Vector();
		mouseListeners.addElement(l);
	}

	public synchronized void removeMouseListener(MouseListener l){
		if(mouseListeners != null && mouseListeners.contains(l))
			mouseListeners.removeElement(l);
	}

	protected void fireMouseClicked(MouseEvent e){
		if(mouseListeners != null){
			int count = mouseListeners.size();
			for(int i = 0; i < count; i++)
				((MouseListener)mouseListeners.elementAt(i)).mouseClicked(e);
		}
	}

	protected void fireMousePressed(MouseEvent e){
		if(mouseListeners != null){
			int count = mouseListeners.size();
			for(int i = 0; i < count; i++)
				((MouseListener)mouseListeners.elementAt(i)).mousePressed(e);
		}
	}

	protected void fireMouseReleased(MouseEvent e){
		if(mouseListeners != null){
			int count = mouseListeners.size();
			for(int i = 0; i < count; i++)
				((MouseListener)mouseListeners.elementAt(i)).mouseReleased(e);
		}
	}

	protected void fireMouseEntered(MouseEvent e){
		if(mouseListeners != null){
			int count = mouseListeners.size();
			for(int i = 0; i < count; i++)
				((MouseListener)mouseListeners.elementAt(i)).mouseEntered(e);
		}
	}

	protected void fireMouseExited(MouseEvent e){
		if(mouseListeners != null){
			int count = mouseListeners.size();
			for(int i = 0; i < count; i++)
				((MouseListener)mouseListeners.elementAt(i)).mouseExited(e);
		}
	}
}
