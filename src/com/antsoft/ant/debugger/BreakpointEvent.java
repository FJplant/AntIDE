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
 * Breakpoint Manage에 대한 성능개선이 좀 있었습니다.
 * 흠..
 * 
 * *****************  Version 5  *****************
 * User: Bezant       Date: 99-06-10   Time: 8:39p
 * Updated in $/AntIDE/source/ant/debugger
 * 쪼메 바꿨음.
 * breakpointEntity를 빼고 BreakpointEvent로 대체
 * 
 * *****************  Version 4  *****************
 * User: Bezant       Date: 99-06-09   Time: 2:17a
 * Updated in $/AntIDE/source/ant/debugger
 * 이거 수행과정이 장난이 아니군..
 * 이거 빨리 정리 안하면 오만상 헤깔리겠는데.  TT;
 * 아자 힘내자!!!
 * 
 * *****************  Version 3  *****************
 * User: Bezant       Date: 99-06-07   Time: 12:43a
 * Updated in $/AntIDE/source/ant/debugger
 * 이렇게 만 진도가 팍팍 나가주면 좋겠구만.
 * 그리고 전체적인 구조가 좀 삐뚤어지고 있음
 * 셤끝나고 함 정리 하는 것이 좋을거 같음
 * 중간 점검 정도로..
 * 보람찬 하루일을 끝마치고서~~~
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
 * Break Point를 설정, 해재할 때에 사용을 하고,
 * Break Point가 걸렸을 때에 발생한다.
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
    //여기서는 두 BreakpointEvent가 같은지를 판별하면 됩니다.
    // 먼저 두 객체가 모두 stopAt인지를 보고 같은 line인지를 보고
    // 아니면 두 객체가 모두 stopAt이 아닌지를 보고 method를 비교하면
    // 된다.
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

