/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/BreakpointEvent.java,v 1.4 1999/07/29 01:27:08 itree Exp $
 * $Revision: 1.4 $
 * $History: BreakpointEvent.java $
 * 
 * *****************  Version 7  *****************
 * User: Bezant       Date: 99-06-19   Time: 1:10a
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 6  *****************
 * User: Bezant       Date: 99-06-11   Time: 8:36p
 * Updated in $/AntIDE/source/ant/debugger
 * Breakpoint Manage�� ���� ���ɰ����� �� �־����ϴ�.
 * ��..
 * 
 * *****************  Version 5  *****************
 * User: Bezant       Date: 99-06-10   Time: 8:39p
 * Updated in $/AntIDE/source/ant/debugger
 * �ɸ� �ٲ���.
 * breakpointEntity�� ���� BreakpointEvent�� ��ü
 * 
 * *****************  Version 4  *****************
 * User: Bezant       Date: 99-06-09   Time: 2:17a
 * Updated in $/AntIDE/source/ant/debugger
 * �̰� ��������� �峭�� �ƴϱ�..
 * �̰� ���� ���� ���ϸ� ������ ��򸮰ڴµ�.  TT;
 * ���� ������!!!
 * 
 * *****************  Version 3  *****************
 * User: Bezant       Date: 99-06-07   Time: 12:43a
 * Updated in $/AntIDE/source/ant/debugger
 * �̷��� �� ������ ���� �����ָ� ���ڱ���.
 * �׸��� ��ü���� ������ �� �߶Ծ����� ����
 * �ɳ����� �� ���� �ϴ� ���� ������ ����
 * �߰� ���� ������..
 * ������ �Ϸ����� ����ġ��~~~
 * 
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:42p
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 1  *****************
 * User: Multipia     Date: 99-05-11   Time: 6:48p
 * Created in $/AntIDE/source/ant/debugger
 * Initial Version.
 */

package  com.antsoft.ant.debugger;

import java.util.EventObject;

// This defines a new event, with minimum state.
/**
 * Break Point�� ����, ������ ���� ����� �ϰ�,
 * Break Point�� �ɷ��� ���� �߻��Ѵ�.
 */
public class BreakpointEvent extends EventObject {
  static final int BREAKPOINT_ADDED=1;
  static final int BREAKPOINT_REMOVED=2;
  private int id=0;
  private boolean stopAt;
  private BreakpointCallback callback;

  // Used when stopAt is false;
  private String clazz;
  private String method;
  private String args;
  //private boolean isEventInMethod;

  // used when stopAt is true;
  private String filename;
  private int line;

  public int getID() {return id;};

  BreakpointEvent(Object source,int i, BreakpointCallback callback) {
    super(source);
    this.callback = callback;
    id=i;
  }

  BreakpointEvent(Object source, int i, BreakpointCallback callback, String clazz, String method) {
    this(source, i, callback);
    stopAt = false;

    this.clazz = clazz;
    this.method = method;
  }

  BreakpointEvent(Object source, int i, BreakpointCallback callback, String clazz, String method, String args) {
    this(source, i, callback, clazz, method);
    this.args = args;
  }

  BreakpointEvent(Object source, int i, BreakpointCallback callback, String clazz, int line) {
    this(source, i, callback);
    stopAt = true;

    //this.filename = filename;
    this.clazz = clazz;
    this.line = line;
  }

  public boolean isStopAt() {
    return stopAt;
  }
  public boolean isClearAt(){
    return stopAt;
  }

  public String getClazz() {
    return clazz;
  }

  public String getMethod() {
    return method;
  }

  public String getArgs() {
    return args;
  }

  public String getFilename() {
    return filename;
  }

  public int getLine() {
    return line;
  }
  public void setLine(int line ){
    if( !stopAt ){
      this.line = line;
    }
  }

  public BreakpointCallback getBreakpointCallback() {
    return callback;
  }
  public final boolean equals(BreakpointEvent event){
    //���⼭�� �� BreakpointEvent�� �������� �Ǻ��ϸ� �˴ϴ�.
    // ���� �� ��ü�� ��� stopAt������ ���� ���� line������ ����
    // �ƴϸ� �� ��ü�� ��� stopAt�� �ƴ����� ���� method�� ���ϸ�
    // �ȴ�.
    if( this.stopAt == event.isStopAt() ){
      if( this.stopAt ){
        if( line == event.getLine() ){
          return true;
        }else{
          return false;
        }
      }else{
        if( method.equals( event.getMethod() ) ){
          return true;
        }else{
          return false;
        }
      }
    }else{
      return false;
    }
    /*if( event.isStopAt() && stopAt ){
      if( line == event.getLine()){
        return true;
      }else{
        return false;
      }
    }else if( !(event.isStopAt()) && !(event.isStopAt())){
      if( method.equals(event.getMethod()) ){
        return true;
      }else{
        return false;
      }
    }
    return false;*/
  }

}

