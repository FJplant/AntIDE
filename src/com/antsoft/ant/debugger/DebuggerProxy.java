/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/DebuggerProxy.java,v 1.5 1999/08/25 10:40:51 strife Exp $
 * $Revision: 1.5 $
 * $History: DebuggerProxy.java $
 * 
 * *****************  Version 8  *****************
 * User: Bezant       Date: 99-06-27   Time: 1:41p
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 7  *****************
 * User: Bezant       Date: 99-06-19   Time: 1:10a
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 6  *****************
 * User: Bezant       Date: 99-06-18   Time: 1:56a
 * Updated in $/AntIDE/source/ant/debugger
 * 실행 프로그램이 종료되었을때 reset시켜주는
 * 루틴을 넣었습니다.
 * 
 * *****************  Version 5  *****************
 * User: Bezant       Date: 99-06-17   Time: 11:39p
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 4  *****************
 * User: Bezant       Date: 99-06-07   Time: 12:43a
 * Updated in $/AntIDE/source/ant/debugger
 * 이렇게 만 진도가 팍팍 나가주면 좋겠구만.
 * 그리고 전체적인 구조가 좀 삐뚤어지고 있음
 * 셤끝나고 함 정리 하는 것이 좋을거 같음
 * 중간 점검 정도로..
 * 보람찬 하루일을 끝마치고서~~~
 * 
 * *****************  Version 3  *****************
 * User: Bezant       Date: 99-06-05   Time: 1:59a
 * Updated in $/AntIDE/source/ant/debugger
 * 오늘도 보람찬하루..
 * 내일은 더 열심히 해야쥐.
 * 
 * *****************  Version 2  *****************
 * User: Bezant       Date: 99-06-04   Time: 12:49a
 * Updated in $/AntIDE/source/ant/debugger
 * 조매바꿨음
 * 
 * *****************  Version 1  *****************
 * User: Multipia     Date: 99-06-02   Time: 10:46p
 * Created in $/AntIDE/source/ant/debugger
 * Ant Debugger Class
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

package com.antsoft.ant.debugger;

//import javax.swing.*;
import java.util.*;
import java.util.StringTokenizer;
import sun.tools.debug.*;


public abstract interface DebuggerProxy {
  public void step( StringTokenizer tn ) throws Exception;
  public void cont() throws Exception;
  public void next() throws Exception;
  public void stepinto() throws Exception;
  public void stepup() throws Exception;
  public String loadClass(String classId) throws Exception;
  public String runClass(String classId) throws Exception;
  public RemoteDebugger getRemoteDebugger();
  public RemoteThread getCurrentThread();
  public RemoteThreadGroup getCurrentThreadGroup();
  public BreakpointManager getBreakpointManager();
  public WatchManager getWatchManager();
  public int getMethodLineNumber( String methodId );
  public void close();
  public void UpdateAllViews();
  public Vector where(boolean b);
  public void setBreakpointManager(BreakpointManager bm);
  public void setWatchManager(WatchManager wm);
  //public String print(StringTokenizer t, boolean dumpObject, Object result);
  String print(StringTokenizer t, boolean dumpObject) throws Exception; 
  //public void usage();
  public void catchException(String idClass) throws Exception;
  public void ignoreException(String idClass) throws Exception;
}
