/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/BreakpointManager.java,v 1.4 1999/07/29 01:27:08 itree Exp $
 * $Revision: 1.4 $
 * $History: BreakpointManager.java $
 * 
 * *****************  Version 12  *****************
 * User: Bezant       Date: 99-06-20   Time: 10:06p
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 11  *****************
 * User: Bezant       Date: 99-06-19   Time: 1:10a
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 10  *****************
 * User: Bezant       Date: 99-06-18   Time: 1:56a
 * Updated in $/AntIDE/source/ant/debugger
 * 실행 프로그램이 종료되었을때 reset시켜주는
 * 루틴을 넣었습니다.
 * 
 * *****************  Version 9  *****************
 * User: Bezant       Date: 99-06-12   Time: 12:01p
 * Updated in $/AntIDE/source/ant/debugger
 * 역시 Breakpoint와 breakpoint 발생시 SourcePanel
 * 에 그부분이 보이도록 하는 것을 구현.. 흠.
 * 
 * *****************  Version 8  *****************
 * User: Bezant       Date: 99-06-12   Time: 2:45a
 * Updated in $/AntIDE/source/ant/debugger
 * BreakpointPanel과 여러가지 손봄
 * 
 * *****************  Version 7  *****************
 * User: Bezant       Date: 99-06-11   Time: 8:36p
 * Updated in $/AntIDE/source/ant/debugger
 * Breakpoint Manage에 대한 성능개선이 좀 있었습니다.
 * 흠..
 * 
 * *****************  Version 6  *****************
 * User: Bezant       Date: 99-06-10   Time: 8:39p
 * Updated in $/AntIDE/source/ant/debugger
 * 쪼메 바꿨음.
 * breakpointEntity를 빼고 BreakpointEvent로 대체
 * 
 * *****************  Version 5  *****************
 * User: Bezant       Date: 99-06-09   Time: 2:17a
 * Updated in $/AntIDE/source/ant/debugger
 * 이거 수행과정이 장난이 아니군..
 * 이거 빨리 정리 안하면 오만상 헤깔리겠는데.  TT;
 * 아자 힘내자!!!
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
 * User: Multipia     Date: 99-05-16   Time: 11:42p
 * Updated in $/AntIDE/source/ant/debugger
 *
 * *****************  Version 1  *****************
 * User: Multipia     Date: 99-05-11   Time: 6:48p
 * Created in $/AntIDE/source/ant/debugger
 * Initial Version.
 */

package  com.antsoft.ant.debugger;

import java.util.Vector;
import java.util.*;
import javax.swing.*;

import sun.tools.debug.*;

// This is an example of a non-visual Bean that fires the new events
public class BreakpointManager implements BreakpointEventListener {
  DebuggerProxy debuggerProxy = null;
  Hashtable breakpointHashtable = new Hashtable();
  public BreakpointManager(DebuggerProxy debuggerProxy) {
    this.debuggerProxy = debuggerProxy;
  }
  public final void reset(DebuggerProxy debuggerProxy) {
    this.debuggerProxy = debuggerProxy;
    breakpointHashtable.clear();
    /*Enumeration enum = breakpointHashtable.elements();
    Object a;
    for(;enum.hasMoreElements();){
      a = enum.nextElement();
      breakpointHashtable.remove(a);
      a = null;
    }*/
  }

  // A single process method keeps all event dispatching in one place.
  // Separate processEVENT1, processEVENT2, etc methods could also be used.
  protected void processBreakpointEvent(BreakpointEvent e) {
    switch (e.getID()) {
      case BreakpointEvent.BREAKPOINT_ADDED:
        //for (int i=0; i<listenerList.size(); i++)
          //Send event to all registered listeners
          //((BreakpointEventListener)listenerList.elementAt(i)).breakpointAdded(e);
        break;
      case BreakpointEvent.BREAKPOINT_REMOVED:
       // for (int i=0; i<listenerList.size(); i++)
          //((BreakpointEventListener)listenerList.elementAt(i)).breakpointRemoved(e);
        break;
    }
  }


  public void breakpointAdded(BreakpointEvent e){
    boolean result;
    if(e.isStopAt()){
      result = stopAt(e);
    }else{
      result = stopIn(e);
    }
    if( result ){
      //System.out.println("[B_P_Manager] stop success ");
      debuggerProxy.UpdateAllViews();

    }
  }
  public boolean stopAt(BreakpointEvent e){
   	String  idClass = e.getClazz();
    RemoteClass cls = null;
    int line = e.getLine();
    try{
      //System.out.println("[BreakpointManager] StopAt : class - " + idClass + ", line - " + line);
      cls = getClassFromToken(idClass);

      String errmessage = cls.setBreakpointLine(e.getLine());
      //System.out.println("[BreakpointManager] StopAt success");

      if( errmessage.length() > 0 ){
        JOptionPane.showMessageDialog(null, errmessage, "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
      }else{
        if( addElement( e ) ){
          //JOptionPane.showMessageDialog(null, "Breakpoint set at " + e.getLine() +
          //           " in class " + idClass, "Breakpoint Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
          JOptionPane.showMessageDialog(null, "Breakpoint가 이미 " + idClass + "클래스의 라인" + e.getLine() +
                     " 에 set되어 있습니다.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    } catch(IllegalArgumentException err){
      JOptionPane.showMessageDialog(null, "\"" + idClass + "\" is not a valid id or class name", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
      return false;
    } catch(Exception err2){
      JOptionPane.showMessageDialog(null, "setBreakpointLine throws Exception.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;

  }
  public boolean stopIn(BreakpointEvent e){

  	String idClass = e.getClazz();
    RemoteClass cls = null;
    String idMethod = e.getMethod();
    if (idMethod == null) {
      // BreakpointEvent에 method를 정의 하지 않았을때
      JOptionPane.showMessageDialog(null, "Method가 정의 되지 않았습니다.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    try {
      cls = getClassFromToken(idClass);
    //} catch (IllegalArgumentException err) {
    } catch (Exception err) {
      // Try stripping method from class.method token.
      int idot = idClass.lastIndexOf(".");
      if (idot == -1) {
        //이 형태를 메시지 박스 형태로 보여주면 됩니다.
        //out.println("\"" + idClass +
        //            "\" is not a valid id or class name.");
        JOptionPane.showMessageDialog(null, "\"" + idClass + "\" is not a valid id or class name.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      idMethod = idClass.substring(idot + 1);
      idClass = idClass.substring(0, idot);
      try{
        cls = getClassFromToken(idClass);
      } catch(Exception err2) {
        JOptionPane.showMessageDialog(null, "\"" + idClass + "\" is not a valid id or class name.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    String argSpec;
    try {
      StringTokenizer t = new StringTokenizer(e.getArgs());
      argSpec = t.nextToken("").trim();
    } catch (Exception err) {
      argSpec = "";     // No argument types specified
    }
    RemoteField method = null;
    try{
      method = findMatchingMethod(cls, idMethod, argSpec);
    } catch( Exception e2){
      JOptionPane.showMessageDialog(null, "FindMatchingMethod throws Exception.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
      return false;
    }

    try {
      if (method != null) {
        // Set the breakpoint
        String err;
        err = cls.setBreakpointMethod(method);
        if (err.length() > 0) {
          // Break point add Complete..!!
          JOptionPane.showMessageDialog(null, err, "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
          //out.println(err);
        } else {
          //여기서는 성공한것이 됩니다.
          // method의 line을 찾아서 e에 넣어 줍니다.
          int line = debuggerProxy.getMethodLineNumber( method.getName() );
          if( line != -1){
            e.setLine(line);
          }
          if( addElement( e ) ){
            //JOptionPane.showMessageDialog(null, "Breakpoint set in " + cls.getName() +
            //            "." + idMethod + argSpec, "Breakpoint Error", JOptionPane.INFORMATION_MESSAGE);
          } else {
            JOptionPane.showMessageDialog(null, "Breakpoint가 이미 " + idClass + "클래스의 메소드 " + idMethod +
                       " 에 set되어 있습니다.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    } catch ( Exception setBPmethodError){
      JOptionPane.showMessageDialog(null, "setBreakpointMethod throws Exception.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  public void breakpointRemoved(BreakpointEvent e){
    boolean result;
    result = clear(e);
    if( result ){
      debuggerProxy.UpdateAllViews();
    }
  }
  public boolean clear( BreakpointEvent e ) {
  	String idClass = e.getClazz();
    RemoteClass cls = null;
    String idMethod = e.getMethod();
    //if (idMethod == null) {
      // BreakpointEvent에 method를 정의 하지 않았을때
    //  JOptionPane.showMessageDialog(null, "Method가 정의 되지 않았습니다.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
    //  return false;
    //}

    // Class를 가져 옵니다.
    try {
      cls = getClassFromToken(idClass);
    //} catch (IllegalArgumentException err) {
    } catch (Exception err) {
      // Try stripping method from class.method token.
      int idot = idClass.lastIndexOf(".");
      if (idot == -1) {
        //이 형태를 메시지 박스 형태로 보여주면 됩니다.
        //out.println("\"" + idClass +
        //            "\" is not a valid id or class name.");
        JOptionPane.showMessageDialog(null, "\"" + idClass + "\" is not a valid id or class name.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      idMethod = idClass.substring(idot + 1);
      idClass = idClass.substring(0, idot);
      try{
        cls = getClassFromToken(idClass);
      } catch(Exception err2) {
        JOptionPane.showMessageDialog(null, "\"" + idClass + "\" is not a valid id or class name.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }

    // Method에 들어갈 파라미터가 있는지 알아 봅니다.
    String argSpec;
    try {
      StringTokenizer t = new StringTokenizer(e.getArgs());
      argSpec = t.nextToken("").trim();
    } catch (Exception err) {
      argSpec = "";     // No argument types specified
    }

    //여기서 idMethod가 null이면 초기에 BreakpointEvent에 line으로 되어서 왔고
    // 그리고 그 클래스를 찾았다는 뜻입니다. line으로 왔지만 클래스를 못찾았을 경우에는
    // 그 윗단계의 클래스를 찾고 메소드를 찾아 줍니다. 위에서 그래서
    // idMethod가 null인지를 먼저 체크해 봐야 합니다.
    if( idMethod != null ) {
      //Method가 breakpoint에서 지워지는 것.
      RemoteField method = null;
      try{
        method = findMatchingMethod(cls, idMethod, argSpec);
      } catch( Exception e2){
        JOptionPane.showMessageDialog(null, "FindMatchingMethod throws Exception.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
        return false;
      }

      try {
        // Set the breakpoint
        String err = cls.clearBreakpointMethod(method);
        if (err.length() > 0) {
          // Break point add Complete..!!
          JOptionPane.showMessageDialog(null, err, "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
          //out.println(err);
        } else {
          //out.println("Breakpoint set in " + cls.getName() +
            //          "." + idMethod + argSpec);
          if( deleteElement( e ) ){
            //JOptionPane.showMessageDialog(null, "Breakpoint clear in " + cls.getName() +
            //            "." + idMethod + argSpec, "Breakpoint Success", JOptionPane.INFORMATION_MESSAGE);
          } else {
            JOptionPane.showMessageDialog(null, "Breakpoint가 이미 " + idClass + "클래스의 메소드 " + idMethod +
                       " 에서 clear되어 있습니다.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
          }

        }
      } catch ( Exception setBPmethodError){
        JOptionPane.showMessageDialog(null, "setBreakpointMethod throws Exception.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }else{
      // breakpoint가 line으로 되어 있을때.
      int lineno = e.getLine();

      String err;
      try{
        err = cls.clearBreakpointLine( lineno );
        if (err.length() > 0) {
          // Break point add Complete..!!
          JOptionPane.showMessageDialog(null, err, "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
          //out.println(err);
        } else {
          // breakpoint가 지워 졌으므로
          // breakpointHashtable에서 그것을 제거 해야 합니다.
          if( deleteElement( e ) ){
          //  JOptionPane.showMessageDialog(null, "Breakpoint clear at " + e.getLine() +
          //             " in class " + idClass, "Breakpoint Success", JOptionPane.INFORMATION_MESSAGE);
          } else {
            JOptionPane.showMessageDialog(null, "Breakpoint가 이미 " + idClass + "클래스의 메소드 " + idMethod +
                       " 에서 clear되어 있습니다.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
          }
        }
      } catch ( Exception err2 ){
        JOptionPane.showMessageDialog(null, "clearBreakpointLine throws Exception.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
        return false;
      }

    }
    return true;
  }

  private boolean addElement( BreakpointEvent e ) {
    // BreakPoint가 추가 되었으므로 이것을 hashtable에 넣습니다.
    // 현재 그 클래스를 키로 하는 vector가 있으면 그곳에 넣고 아니면
    // idClass를 키로 하는 Vector를 하나 만들고 그곳에 BreakpointEntity를 만들어 넣는다.
    if( e.isStopAt() ){
      int line = e.getLine();
      String idClass = e.getClazz();

      //BreakpointEntity entity = new BreakpointEntity(line);
      Vector breakpointVector = null;
      if(breakpointHashtable.containsKey(idClass)){
        breakpointVector = (Vector)breakpointHashtable.get(idClass);
        //System.out.println("[B_PManager] Hashtable에 " + idClass + "가 있어서 그 것을 가져 옵니다.");
      }else{
        breakpointVector = new Vector();
        breakpointHashtable.put(idClass, breakpointVector);
        //System.out.println("[B_PManager] Hashtable에 " + idClass + "가 없어서 생성 추가 합니다.");
      }
      //System.out.println("[B_P_Manager] breakpointVector에 있는 내용 : " + breakpointVector);
      //추가 하지 전에 안에 들어가 있느지를 살펴 봐야 겠죠.
      //System.out.println("[B_P_Manager] entity : " + entity);
      //System.out.println("[B_P_Manager] breakpointVector.contains(entity) : " + breakpointVector.contains(entity));
      if( isElementInVector(breakpointVector , e ) == -1 ){
        breakpointVector.addElement( e );
      }else{
        return false;
      }
      //for debugging..
      //System.out.println("[B_P_Manager] breakpointVector에 있는 내용(추가후) : " + breakpointVector);
    }else{
      // BreakPoint가 추가 되었으므로 이것을 hashtable에 넣습니다.
      // 현재 그 클래스를 키로 하는 vector가 있으면 그곳에 넣고 아니면
      // idClass를 키로 하는 Vector를 하나 만들고 그곳에 BreakpointEntity를 만들어 넣는다.
      String idMethod = e.getMethod();
      String idClass = e.getClazz();

      //BreakpointEntity entity = new BreakpointEntity(idMethod);
      Vector breakpointVector = null;
      if(breakpointHashtable.containsKey(idClass)){
        breakpointVector = (Vector)breakpointHashtable.get(idClass);
        //System.out.println("[B_PManager] Hashtable에 " + idClass + "가 있어서 그 것을 가져 옵니다.");
      }else{
        breakpointVector = new Vector();
        breakpointHashtable.put(idClass, breakpointVector);
        //System.out.println("[B_PManager] Hashtable에 " + idClass + "가 없어서 생성 추가 합니다.");
      }
      //System.out.println("[B_P_Manager] breakpointVector에 있는 내용 : " + breakpointVector);
      //추가 하지 전에 안에 들어가 있느지를 살펴 봐야 겠죠.
      if( isElementInVector(breakpointVector , e ) == -1 ){
        breakpointVector.addElement( e );
      }else{
        return false;
      }
      //for debugging..
      //System.out.println("[B_P_Manager] breakpointVector에 있는 내용(추가후) : " + breakpointVector);
    }
    return true;
  }
  private boolean deleteElement( BreakpointEvent e ) {
    if( e.isStopAt() ){
      int line = e.getLine();
      String idClass = e.getClazz();
      //System.out.println("[B_P_Manager] deleteEle- class : " + idClass + " : Line : " + line);

      //BreakpointEntity entity = new BreakpointEntity(line);
      Vector breakpointVector = null;
      if(breakpointHashtable.containsKey(idClass)){
        breakpointVector = (Vector)breakpointHashtable.get(idClass);
        //System.out.println("[B_PManager] deleteEle : Hashtable에 " + idClass + "가 있어서 그 것을 가져 옵니다.");
      }else{
        // 해당 클래스가 가지고 있는 BreakpointEntity가 없습니다.
        return false;
      }
      //System.out.println("[B_P_Manager] vector : " + breakpointVector);
      //System.out.println("[B_P_Manager] breakpointVector에 있는 내용 : " + breakpointVector);
      //추가 하지 전에 안에 들어가 있느지를 살펴 봐야 겠죠.
      //System.out.println("[B_P_Manager] ee : " + e);
      //System.out.println("[B_P_Manager] breakpointVector.contains(entity) : " + breakpointVector.contains(e));
      int index = isElementInVector(breakpointVector , e );
      //System.out.println("[B_P_Manager] index : " + index);
      if( index != -1){
        //breakpointVector.addElement(entity);
        breakpointVector.removeElementAt(index);
      }else{
        return false;
      }
      //for debugging..
      //System.out.println("[B_P_Manager] breakpointVector에 있는 내용(제거후) : " + breakpointVector);
    }else{
      // BreakPoint가 추가 되었으므로 이것을 hashtable에 넣습니다.
      // 현재 그 클래스를 키로 하는 vector가 있으면 그곳에 넣고 아니면
      // idClass를 키로 하는 Vector를 하나 만들고 그곳에 BreakpointEntity를 만들어 넣는다.
      String idMethod = e.getMethod();
      String idClass = e.getClazz();

      //BreakpointEntity entity = new BreakpointEntity(idMethod);
      Vector breakpointVector = null;
      if(breakpointHashtable.containsKey(idClass)){
        breakpointVector = (Vector)breakpointHashtable.get(idClass);
        //System.out.println("[B_PManager] deleteEle : Hashtable에 " + idClass + "가 있어서 그 것을 가져 옵니다.");
      }else{
        return false;
      }
      //System.out.println("[B_P_Manager] breakpointVector에 있는 내용 : " + breakpointVector);
      //추가 하지 전에 안에 들어가 있느지를 살펴 봐야 겠죠.
      int index = isElementInVector(breakpointVector , e );
      //System.out.println("[B_P_Manager] index : " + index);
      if( index != -1){
        //breakpointVector.addElement(entity);
        breakpointVector.removeElementAt(index);
      }else{
        return false;
      }
      //for debugging..
      //System.out.println("[B_P_Manager] breakpointVector에 있는 내용(제거후) : " + breakpointVector);
    }
    return true;
  }
  private int isElementInVector(Vector v, BreakpointEvent b){
    Enumeration enum = v.elements();
    BreakpointEvent bpEvent = null;
    int count = 0;
    boolean existFlag = false;
    for(; enum.hasMoreElements(); ){
      bpEvent = (BreakpointEvent)enum.nextElement();
      //System.out.println("[B_P_Mananger] isElement.. : bpEvent : " + bpEvent);
      if( bpEvent.equals(b) == true ){
        return count;
      }
      count++;
    }
    return -1;
  }
  /*
   * Attempt an unambiguous match of the method name and argument specification to
   * to a method. If no arguments are specified, the method must not be overloaded.
   * Otherwise, the argument types much match exactly
   */
  RemoteField findMatchingMethod(RemoteClass clazz, String methodName,
                                 String argSpec) throws Exception {
    if ( (argSpec.length() > 0) &&
         (!argSpec.startsWith("(") || !argSpec.endsWith(")")) ) {
      //out.println("Invalid method specification: '" + methodName + argSpec + "'");
      JOptionPane.showMessageDialog(null, "Invalid method specification: '" + methodName + argSpec + "'", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
      return null;
    }

    // Parse the argument string once before looping below.
    StringTokenizer tokens = new StringTokenizer(argSpec, "(,)");
    Vector argTypeNames = new Vector();
    String name = null;
    try {
      while (tokens.hasMoreTokens()) {
        name = tokens.nextToken();
        name = normalizeArgTypeName(name);
        argTypeNames.addElement(name);
      }
    } catch (IllegalArgumentException e) {
      JOptionPane.showMessageDialog(null, "Invalid Argument Type: '" + name + "'", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
      //out.println("Invalid Argument Type: '" + name + "'");
      return null;
    }

    // Check each method in the class for matches
    RemoteField methods[] = clazz.getMethods();
    RemoteField firstMatch = null;  // first method with matching name
    RemoteField exactMatch = null;  // (only) method with same name & sig
    int matchCount = 0;             // > 1 implies overload
    for (int i = 0; i < methods.length; i++) {
      RemoteField candidate = methods[i];

      if (candidate.getName().equals(methodName)) {
        matchCount++;

        // Remember the first match in case it is the only one
        if (matchCount == 1) {
          firstMatch = candidate;
        }

        // If argument types were specified, check against candidate
        if (! argSpec.equals("")
                && compareArgTypes(candidate, argTypeNames) == true) {
          exactMatch = candidate;
          break;
        }
      }
    }

    // Determine method for breakpoint
    RemoteField method = null;
    if (exactMatch != null) {
      // Name and signature match
      method = exactMatch;
    } else if (argSpec.equals("") && (matchCount > 0)) {
      // At least one name matched and no arg types were specified
      if (matchCount == 1) {
        method = firstMatch;       // Only one match; safe to use it
      } else {
        // Method is overloaded, but no arg types were specified
        //out.println(clazz.getName() + "." + methodName +
          //          " is overloaded, use one of the following:");
        String temp = "";
        for (int i = 0; i < methods.length; i++) {
          if (methodName.equals(methods[i].getName())) {
            //out.println("  " + methods[i].getTypedName());
            temp += "  " + methods[i].getTypedName();
          }
        }
        JOptionPane.showMessageDialog(null, clazz.getName() + "." + methodName + " is overloaded, use one of the following:" + "\r\n" + temp
                                      , "FindMatchingMethod Error", JOptionPane.ERROR_MESSAGE);
      }
    } else {
      // No match with unspecified args or no exact match with specified args
      //out.println("Class " + clazz.getName() + " doesn't have a method " + methodName + argSpec);
        JOptionPane.showMessageDialog(null, "Class " + clazz.getName() + " doesn't have a method " + methodName + argSpec
                                      , "FindMatchingMethod Error", JOptionPane.ERROR_MESSAGE);
    }
    return method;
  }

  private RemoteClass getClassFromToken(String idToken) throws Exception {
  	RemoteObject obj;
    obj = debuggerProxy.getRemoteDebugger().findClass(idToken);
    if (obj == null) {
    	throw new IllegalArgumentException();
    }
  	return (RemoteClass)obj;
  }
  /*
   * Remove unneeded spaces and expand class names to fully qualified names,
   * if necessary and possible.
   */
  private String normalizeArgTypeName(String name) throws Exception {
    /*
     * Separate the type name from any array modifiers, stripping whitespace
     * after the name ends
     */
    int i = 0;
    StringBuffer typePart = new StringBuffer();
    StringBuffer arrayPart = new StringBuffer();
    name = name.trim();
    while (i < name.length()) {
      char c = name.charAt(i);
      if (Character.isWhitespace(c) || c == '[') {
        break;      // name is complete
      }
      typePart.append(c);
      i++;
    }
    while (i < name.length()) {
      char c = name.charAt(i);
      if ( (c == '[') || (c == ']') ) {
        arrayPart.append(c);
      } else if (!Character.isWhitespace(c)) {
        throw new IllegalArgumentException("Invalid argument type name");
      }
      i++;
    }
    name = typePart.toString();

    /*
     * When there's no sign of a package name already, try to expand the
     * the name to a fully qualified class name
     */
    if (name.indexOf('.') == -1) {
      try {
        RemoteClass argClass = getClassFromToken(name);
        name = argClass.getName();
      } catch (IllegalArgumentException e) {
        // We'll try the name as is
      }
    }
    name += arrayPart.toString();
    return name;
  }


  /*
   * Compare a method's argument types with a Vector of type names.
   * Return true if each argument type has a name identical to the corresponding
   * string in the vector and if the number of arguments in the method matches
   * the number of names passed
   */
  private boolean compareArgTypes(RemoteField method, Vector nameVector) {
    String nameString = method.getTypedName();

    // Skip to the argument types and tokenize them
    int index = nameString.indexOf("(");
    if (index == -1) {
      throw new IllegalArgumentException("Method expected");
    }
    StringTokenizer tokens = new StringTokenizer(nameString.substring(index),
                                                 "(,) \t\n\r");

    // If argument counts differ, we can stop here
    if (tokens.countTokens() != nameVector.size()) {
      return false;
    }

    // Compare each argument type's name
    Enumeration enum = nameVector.elements();
    while (tokens.hasMoreTokens()) {
      String comp1 = (String)enum.nextElement();
      String comp2 = tokens.nextToken();
      if (! comp1.equals(comp2)) {
        return false;
      }
    }

    return true;
  }

  public Vector getBreakpointVector(String className){
    if(breakpointHashtable.containsKey(className)){
      return (Vector)breakpointHashtable.get(className);
    }else{
      return null;
    }
  }
  public Vector getAllElement(){
    Enumeration enum = breakpointHashtable.elements();
    Vector list = new Vector();
    Vector source = null;
    int len = 0;
    for(; enum.hasMoreElements();){
      source = (Vector)enum.nextElement();
      len = source.size();
      for(int i = 0; i < len; i++){
        list.addElement(source.elementAt(i));
      }
    }
    return list;
  }
}
