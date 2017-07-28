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
 * ���� ���α׷��� ����Ǿ����� reset�����ִ�
 * ��ƾ�� �־����ϴ�.
 * 
 * *****************  Version 9  *****************
 * User: Bezant       Date: 99-06-12   Time: 12:01p
 * Updated in $/AntIDE/source/ant/debugger
 * ���� Breakpoint�� breakpoint �߻��� SourcePanel
 * �� �׺κ��� ���̵��� �ϴ� ���� ����.. ��.
 * 
 * *****************  Version 8  *****************
 * User: Bezant       Date: 99-06-12   Time: 2:45a
 * Updated in $/AntIDE/source/ant/debugger
 * BreakpointPanel�� �������� �պ�
 * 
 * *****************  Version 7  *****************
 * User: Bezant       Date: 99-06-11   Time: 8:36p
 * Updated in $/AntIDE/source/ant/debugger
 * Breakpoint Manage�� ���� ���ɰ����� �� �־����ϴ�.
 * ��..
 * 
 * *****************  Version 6  *****************
 * User: Bezant       Date: 99-06-10   Time: 8:39p
 * Updated in $/AntIDE/source/ant/debugger
 * �ɸ� �ٲ���.
 * breakpointEntity�� ���� BreakpointEvent�� ��ü
 * 
 * *****************  Version 5  *****************
 * User: Bezant       Date: 99-06-09   Time: 2:17a
 * Updated in $/AntIDE/source/ant/debugger
 * �̰� ��������� �峭�� �ƴϱ�..
 * �̰� ���� ���� ���ϸ� ������ ��򸮰ڴµ�.  TT;
 * ���� ������!!!
 * 
 * *****************  Version 4  *****************
 * User: Bezant       Date: 99-06-07   Time: 12:43a
 * Updated in $/AntIDE/source/ant/debugger
 * �̷��� �� ������ ���� �����ָ� ���ڱ���.
 * �׸��� ��ü���� ������ �� �߶Ծ����� ����
 * �ɳ����� �� ���� �ϴ� ���� ������ ����
 * �߰� ���� ������..
 * ������ �Ϸ����� ����ġ��~~~
 * 
 * *****************  Version 3  *****************
 * User: Bezant       Date: 99-06-05   Time: 1:59a
 * Updated in $/AntIDE/source/ant/debugger
 * ���õ� �������Ϸ�..
 * ������ �� ������ �ؾ���.
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
          JOptionPane.showMessageDialog(null, "Breakpoint�� �̹� " + idClass + "Ŭ������ ����" + e.getLine() +
                     " �� set�Ǿ� �ֽ��ϴ�.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
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
      // BreakpointEvent�� method�� ���� ���� �ʾ�����
      JOptionPane.showMessageDialog(null, "Method�� ���� ���� �ʾҽ��ϴ�.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    try {
      cls = getClassFromToken(idClass);
    //} catch (IllegalArgumentException err) {
    } catch (Exception err) {
      // Try stripping method from class.method token.
      int idot = idClass.lastIndexOf(".");
      if (idot == -1) {
        //�� ���¸� �޽��� �ڽ� ���·� �����ָ� �˴ϴ�.
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
          //���⼭�� �����Ѱ��� �˴ϴ�.
          // method�� line�� ã�Ƽ� e�� �־� �ݴϴ�.
          int line = debuggerProxy.getMethodLineNumber( method.getName() );
          if( line != -1){
            e.setLine(line);
          }
          if( addElement( e ) ){
            //JOptionPane.showMessageDialog(null, "Breakpoint set in " + cls.getName() +
            //            "." + idMethod + argSpec, "Breakpoint Error", JOptionPane.INFORMATION_MESSAGE);
          } else {
            JOptionPane.showMessageDialog(null, "Breakpoint�� �̹� " + idClass + "Ŭ������ �޼ҵ� " + idMethod +
                       " �� set�Ǿ� �ֽ��ϴ�.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
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
      // BreakpointEvent�� method�� ���� ���� �ʾ�����
    //  JOptionPane.showMessageDialog(null, "Method�� ���� ���� �ʾҽ��ϴ�.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
    //  return false;
    //}

    // Class�� ���� �ɴϴ�.
    try {
      cls = getClassFromToken(idClass);
    //} catch (IllegalArgumentException err) {
    } catch (Exception err) {
      // Try stripping method from class.method token.
      int idot = idClass.lastIndexOf(".");
      if (idot == -1) {
        //�� ���¸� �޽��� �ڽ� ���·� �����ָ� �˴ϴ�.
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

    // Method�� �� �Ķ���Ͱ� �ִ��� �˾� ���ϴ�.
    String argSpec;
    try {
      StringTokenizer t = new StringTokenizer(e.getArgs());
      argSpec = t.nextToken("").trim();
    } catch (Exception err) {
      argSpec = "";     // No argument types specified
    }

    //���⼭ idMethod�� null�̸� �ʱ⿡ BreakpointEvent�� line���� �Ǿ �԰�
    // �׸��� �� Ŭ������ ã�Ҵٴ� ���Դϴ�. line���� ������ Ŭ������ ��ã���� ��쿡��
    // �� ���ܰ��� Ŭ������ ã�� �޼ҵ带 ã�� �ݴϴ�. ������ �׷���
    // idMethod�� null������ ���� üũ�� ���� �մϴ�.
    if( idMethod != null ) {
      //Method�� breakpoint���� �������� ��.
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
            JOptionPane.showMessageDialog(null, "Breakpoint�� �̹� " + idClass + "Ŭ������ �޼ҵ� " + idMethod +
                       " ���� clear�Ǿ� �ֽ��ϴ�.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
          }

        }
      } catch ( Exception setBPmethodError){
        JOptionPane.showMessageDialog(null, "setBreakpointMethod throws Exception.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }else{
      // breakpoint�� line���� �Ǿ� ������.
      int lineno = e.getLine();

      String err;
      try{
        err = cls.clearBreakpointLine( lineno );
        if (err.length() > 0) {
          // Break point add Complete..!!
          JOptionPane.showMessageDialog(null, err, "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
          //out.println(err);
        } else {
          // breakpoint�� ���� �����Ƿ�
          // breakpointHashtable���� �װ��� ���� �ؾ� �մϴ�.
          if( deleteElement( e ) ){
          //  JOptionPane.showMessageDialog(null, "Breakpoint clear at " + e.getLine() +
          //             " in class " + idClass, "Breakpoint Success", JOptionPane.INFORMATION_MESSAGE);
          } else {
            JOptionPane.showMessageDialog(null, "Breakpoint�� �̹� " + idClass + "Ŭ������ �޼ҵ� " + idMethod +
                       " ���� clear�Ǿ� �ֽ��ϴ�.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
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
    // BreakPoint�� �߰� �Ǿ����Ƿ� �̰��� hashtable�� �ֽ��ϴ�.
    // ���� �� Ŭ������ Ű�� �ϴ� vector�� ������ �װ��� �ְ� �ƴϸ�
    // idClass�� Ű�� �ϴ� Vector�� �ϳ� ����� �װ��� BreakpointEntity�� ����� �ִ´�.
    if( e.isStopAt() ){
      int line = e.getLine();
      String idClass = e.getClazz();

      //BreakpointEntity entity = new BreakpointEntity(line);
      Vector breakpointVector = null;
      if(breakpointHashtable.containsKey(idClass)){
        breakpointVector = (Vector)breakpointHashtable.get(idClass);
        //System.out.println("[B_PManager] Hashtable�� " + idClass + "�� �־ �� ���� ���� �ɴϴ�.");
      }else{
        breakpointVector = new Vector();
        breakpointHashtable.put(idClass, breakpointVector);
        //System.out.println("[B_PManager] Hashtable�� " + idClass + "�� ��� ���� �߰� �մϴ�.");
      }
      //System.out.println("[B_P_Manager] breakpointVector�� �ִ� ���� : " + breakpointVector);
      //�߰� ���� ���� �ȿ� �� �ִ����� ���� ���� ����.
      //System.out.println("[B_P_Manager] entity : " + entity);
      //System.out.println("[B_P_Manager] breakpointVector.contains(entity) : " + breakpointVector.contains(entity));
      if( isElementInVector(breakpointVector , e ) == -1 ){
        breakpointVector.addElement( e );
      }else{
        return false;
      }
      //for debugging..
      //System.out.println("[B_P_Manager] breakpointVector�� �ִ� ����(�߰���) : " + breakpointVector);
    }else{
      // BreakPoint�� �߰� �Ǿ����Ƿ� �̰��� hashtable�� �ֽ��ϴ�.
      // ���� �� Ŭ������ Ű�� �ϴ� vector�� ������ �װ��� �ְ� �ƴϸ�
      // idClass�� Ű�� �ϴ� Vector�� �ϳ� ����� �װ��� BreakpointEntity�� ����� �ִ´�.
      String idMethod = e.getMethod();
      String idClass = e.getClazz();

      //BreakpointEntity entity = new BreakpointEntity(idMethod);
      Vector breakpointVector = null;
      if(breakpointHashtable.containsKey(idClass)){
        breakpointVector = (Vector)breakpointHashtable.get(idClass);
        //System.out.println("[B_PManager] Hashtable�� " + idClass + "�� �־ �� ���� ���� �ɴϴ�.");
      }else{
        breakpointVector = new Vector();
        breakpointHashtable.put(idClass, breakpointVector);
        //System.out.println("[B_PManager] Hashtable�� " + idClass + "�� ��� ���� �߰� �մϴ�.");
      }
      //System.out.println("[B_P_Manager] breakpointVector�� �ִ� ���� : " + breakpointVector);
      //�߰� ���� ���� �ȿ� �� �ִ����� ���� ���� ����.
      if( isElementInVector(breakpointVector , e ) == -1 ){
        breakpointVector.addElement( e );
      }else{
        return false;
      }
      //for debugging..
      //System.out.println("[B_P_Manager] breakpointVector�� �ִ� ����(�߰���) : " + breakpointVector);
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
        //System.out.println("[B_PManager] deleteEle : Hashtable�� " + idClass + "�� �־ �� ���� ���� �ɴϴ�.");
      }else{
        // �ش� Ŭ������ ������ �ִ� BreakpointEntity�� �����ϴ�.
        return false;
      }
      //System.out.println("[B_P_Manager] vector : " + breakpointVector);
      //System.out.println("[B_P_Manager] breakpointVector�� �ִ� ���� : " + breakpointVector);
      //�߰� ���� ���� �ȿ� �� �ִ����� ���� ���� ����.
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
      //System.out.println("[B_P_Manager] breakpointVector�� �ִ� ����(������) : " + breakpointVector);
    }else{
      // BreakPoint�� �߰� �Ǿ����Ƿ� �̰��� hashtable�� �ֽ��ϴ�.
      // ���� �� Ŭ������ Ű�� �ϴ� vector�� ������ �װ��� �ְ� �ƴϸ�
      // idClass�� Ű�� �ϴ� Vector�� �ϳ� ����� �װ��� BreakpointEntity�� ����� �ִ´�.
      String idMethod = e.getMethod();
      String idClass = e.getClazz();

      //BreakpointEntity entity = new BreakpointEntity(idMethod);
      Vector breakpointVector = null;
      if(breakpointHashtable.containsKey(idClass)){
        breakpointVector = (Vector)breakpointHashtable.get(idClass);
        //System.out.println("[B_PManager] deleteEle : Hashtable�� " + idClass + "�� �־ �� ���� ���� �ɴϴ�.");
      }else{
        return false;
      }
      //System.out.println("[B_P_Manager] breakpointVector�� �ִ� ���� : " + breakpointVector);
      //�߰� ���� ���� �ȿ� �� �ִ����� ���� ���� ����.
      int index = isElementInVector(breakpointVector , e );
      //System.out.println("[B_P_Manager] index : " + index);
      if( index != -1){
        //breakpointVector.addElement(entity);
        breakpointVector.removeElementAt(index);
      }else{
        return false;
      }
      //for debugging..
      //System.out.println("[B_P_Manager] breakpointVector�� �ִ� ����(������) : " + breakpointVector);
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
