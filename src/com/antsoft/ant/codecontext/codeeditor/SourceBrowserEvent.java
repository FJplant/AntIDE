/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/codecontext/codeeditor/SourceBrowserEvent.java,v 1.4 1999/08/27 01:57:30 kahn Exp $
 * $Revision: 1.4 $
 * Part : Source Browser Event Class.
 * Author: Kim, Sung-hoon(kahn@antj.com)
 * Dated at 1999.2.2.
 */

package com.antsoft.ant.codecontext.codeeditor;

import java.util.Vector;

import com.antsoft.ant.util.QuickSorter;

/**
  @author Kim, Sung-Hoon.
  */
public class SourceBrowserEvent {
	// vector of the EventContent class
	Vector events;

  /**
    It aligns the events list as the order of class, field, method, import...
    
  public void alignment() {
    if (events==null) return;

    EventContent ec=null;

    Vector cls=new Vector(5);
    Vector fld=new Vector();
    Vector mth=new Vector();
    Vector imp=new Vector();
    EventContent pack=null;

    Vector result=new Vector();

    boolean inClass=false;

    int count=events.size();
    for (int i=0;i<count;++i) {
    	ec=(EventContent)events.elementAt(i);
     	switch (ec.getContentType()) {
       	case EventContent.ATTR : fld.addElement(ec.getContent()); break;
       	case EventContent.CLASS :
       	case EventContent.INTERFACE :
       		if (!inClass) {
       			inClass=true;
       			cls.insertElementAt(ec,0);
       		}
       		else {
    				for (int j=0;j<cls.size();++j) result.addElement(cls.elementAt(j));
    				mth=getSortedList(mth);
    				for (int j=0;j<mth.size();++j) {
   					EventContent eventContent=
    							new EventContent((String)mth.elementAt(j),EventContent.OPER);
    					result.addElement(eventContent);
    				}
    				fld=getSortedList(fld);
    				for (int j=0;j<fld.size();++j) {
    					EventContent eventContent=
    							new EventContent((String)fld.elementAt(j),EventContent.ATTR);
    					result.addElement(eventContent);
    				}

    				cls=new Vector();
    				mth=new Vector();
    				fld=new Vector();

       			cls.insertElementAt(ec,0);
       		}
    			break;
       	case EventContent.OPER : mth.addElement(ec.getContent()); break;
       	case EventContent.PACKAGE : pack=ec; break;
       	case EventContent.INNER :
       		if (cls.size()<2) cls.addElement(ec);
       		else cls.insertElementAt(ec,1);
       		break;

       	case EventContent.IMPORT : //imp.addElement(ec); break;
          imp.addElement(ec.getContent());
          break;
     	}
   	}

    for (int j=0;j<cls.size();++j) result.addElement(cls.elementAt(j));
    mth=getSortedList(mth);
    for (int j=0;j<mth.size();++j) {
    	EventContent eventContent=new EventContent((String)mth.elementAt(j),EventContent.OPER);
    	result.addElement(eventContent);
    }
    fld=getSortedList(fld);
    for (int j=0;j<fld.size();++j) {
    	EventContent eventContent=new EventContent((String)fld.elementAt(j),EventContent.ATTR);
    	result.addElement(eventContent);
    }
    imp=getSortedList(imp);
    for (int j=0;j<imp.size();++j) {
    	EventContent eventContent=new EventContent((String)imp.elementAt(j),EventContent.IMPORT);
    	result.addElement(eventContent);
    }

    if (pack!=null) result.addElement(pack);
    //for (int i=0;i<imp.size();++i) result.addElement(imp.elementAt(i));

    events=result;
  }
  */

  private Vector getSortedList(Vector v) {
    return QuickSorter.sort(v, QuickSorter.LESS_STRING);
  }

	/**
	  add the EventContent object into the event list.

	  @param ec the EventContent Object.
	  */
	public void addEvent(EventContent ec) {
		if (events==null) events=new Vector();

		events.addElement(ec);
	}

	/**
	  add the Event List object into the event list.

	  @param ec the Event List Object as the Vector class.
	  */
	public void addEvents(Vector ec) {
		if (events==null) events=new Vector();

        for (int i=0;i<ec.size();++i) events.addElement(ec.elementAt(i));
	}

	/**
	  add the EventContent object into the event list with specified string and int value.

	  @param content the event content.
	  @param type the event type.
      */
	public void addEvent(String content,int type) {
		EventContent ec=new EventContent();
		ec.setContent(content);
		ec.setContentType(type);

		addEvent(ec);
	}

	// counter(index of the current event in the event list).
	int evtCounter;

  public void setEventCounter(int count) {
    evtCounter=count;
  }

	/**
	  returns the first element of the event list as the EventContent Object.

	  @return the EventContent Object.
	  */
	public EventContent firstEvent() {
		evtCounter=0;

		if (evtCounter<events.size()) return (EventContent)events.elementAt(evtCounter);
		else return null;
	}

	/**
	  returns the next element of the event list as the EventContent Object.

	  @return the EventContent Object.
	  */
	public EventContent nextEvent() {
		evtCounter++;

		if (evtCounter<events.size()) return (EventContent)events.elementAt(evtCounter);
		else return null;
	}

	/**
	  verifies the event list has more element or not.

	  @return thue if has more element, otherwise false.
	  */
	public boolean hasMoreEvents() {
		int cnt=evtCounter+1;

		if (cnt<events.size()) return true;
		else return false;
	}

  public Vector getEvents() {
    return events;
  }

	/**
	  set the events as the Vector Object.

	  @param obj the events object as the Vector class type.
	  */
	public void setEvents(Vector obj) {
		events=obj;
	}
}
