/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/AntDebuggerProxy.java,v 1.12 1999/08/26 05:08:02 itree Exp $
 * $Revision: 1.12 $
 * $History: AntDebuggerProxy.java $
 * 
 * *****************  Version 4  *****************
 * User: Bezant       Date: 99-06-27   Time: 1:41p
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 3  *****************
 * User: Bezant       Date: 99-06-20   Time: 10:06p
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 2  *****************
 * User: Bezant       Date: 99-06-19   Time: 1:10a
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 1  *****************
 * User: Bezant       Date: 99-06-18   Time: 1:48a
 * Created in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 9  *****************
 * User: Bezant       Date: 99-06-17   Time: 11:39p
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 8  *****************
 * User: Bezant       Date: 99-06-16   Time: 12:18a
 * Updated in $/AntIDE/source/ant/debugger
 * 별루 못했음. -_-;
 *
 * *****************  Version 7  *****************
 * User: Bezant       Date: 99-06-15   Time: 12:58a
 * Updated in $/AntIDE/source/ant/debugger
 * ThreadPanel과 commandPanel을 좀 손봤슴다.
 * 
 * *****************  Version 6  *****************
 * User: Bezant       Date: 99-06-13   Time: 10:28p
 * Updated in $/AntIDE/source/ant/debugger
 * nd pnl...
 * 
 * *****************  Version 5  *****************
 * User: Bezant       Date: 99-06-12   Time: 11:59a
 * Updated in $/AntIDE/source/ant/debugger
 * 역시 Breakpoint와 breakpoint 발생시 SourcePanel
 * 에 그부분이 보이도록 하는 것을 구현.. 흠.
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
 */
package com.antsoft.ant.debugger;

import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

import java.awt.*;
import sun.tools.debug.*;

import com.antsoft.ant.debugger.Debugger;

/**
 * The start up module of Ant Debugger
 *
 * @author Kwon, Young Mo
 */
class AntDebuggerProxy implements DebuggerProxy, DebuggerCallback {
  //private AntDebuggerMainFrame mainFrame;
  private AntDebuggerPanel antDebuggerPanel;
  private BreakpointManager breakpointManager;
  private WatchManager watchManager;
  //private DebuggerCommandPanel debuggerCommandPanel;

  private RemoteDebugger remoteDebugger;
  RemoteThread currentThread;
  RemoteThreadGroup currentThreadGroup;
  PrintStream out = null;
  PrintStream console = null;
  private String lastArgs = null;

  private static final String progname = "Ant Debugger 1.0";
  private static final String version = "99/06/05";
  static final String printDelimiters = ".[(";

	public AntDebuggerProxy( String host, String password, String javaArgs, String args,
               PrintStream outStream, PrintStream consoleStream,
               boolean verbose ,
               AntDebuggerPanel antDebuggerPanel) throws Exception {
  //  breakpointManager = new BreakpointManager(this);
  //  watchManager = new WatchManager(this);
  //  antDebuggerPanel = new AntDebuggerPanel(this);
    //debuggerCommandPanel = new DebuggerCommandPanel(this, antDebuggerPanel);
  //  mainFrame = new AntDebuggerMainFrame(this, antDebuggerPanel);
    /*mainFrame.getContentPane().add(antDebuggerPanel, BorderLayout.CENTER);
    mainFrame.pack();
    mainFrame.setVisible(true);
    */

    this.antDebuggerPanel = antDebuggerPanel;
    initRemoteDebugger( host, password, javaArgs, args, outStream, consoleStream, verbose );
	}
  public final void setBreakpointManager(BreakpointManager breakpointManager){
    this.breakpointManager = breakpointManager;
  }
  public final void setWatchManager(WatchManager watchManager){
    this.watchManager = watchManager;
  }

  private void initRemoteDebugger( String host, String password, String javaArgs, String args,
               PrintStream outStream, PrintStream consoleStream,
               boolean verbose ) throws Exception {

    System.out.println("Initializing " + progname + "...");
    out = outStream;
    console = consoleStream;
    if (password == null) {
      remoteDebugger = new RemoteDebugger(javaArgs, this, verbose);      
    } else {
      remoteDebugger = new RemoteDebugger(host, password, this, verbose);
    }
    DataInputStream in = new DataInputStream(System.in);
    String lastLine = null;

    if (args != null && args.length() > 0) {
        StringTokenizer t = new StringTokenizer(args);
        String idToken = t.nextToken();
        loadClass(idToken);
        lastArgs = args;
    }

    Thread.currentThread().setPriority(Thread.NORM_PRIORITY);

    // Try reading user's startup file.
    File f = new File(System.getProperty("user.home") +
        System.getProperty("file.separator") + "jdb.ini");
          if (!f.canRead()) {
              // Try opening $HOME/jdb.ini
              f = new File(System.getProperty("user.home") +
                           System.getProperty("file.separator") + ".jdbrc");
          }
          readCommandFile(f);

    // Try opening local jdb.ini
    f = new File(System.getProperty("user.dir") +
        System.getProperty("file.separator") + "startup.jdb");
          readCommandFile(f);
  }

  public void UpdateAllViews(){
    try {
      antDebuggerPanel.updateViews();
    } catch( Exception e ){
      System.out.println("[AntDebuggerProxy] AntDebuggerPanel의 updateViews에서 Exceptoin발생!!");
      e.printStackTrace();
    }
  }

  void readCommandFile(File f) {
    try {
      if (f.canRead()) {
        // Process initial commands.
        //DataInputStream inFile =
        //    new DataInputStream(new FileInputStream(f));
        BufferedReader inFile = new BufferedReader(
                                new InputStreamReader( new FileInputStream(f)));
        String ln;
        while ((ln = inFile.readLine()) != null) {
          StringTokenizer t = new StringTokenizer(ln);
          if (t.hasMoreTokens()) {
            //executeCommand(t);
          }
        }
      }
    } catch (IOException e) {}
  }

  public RemoteDebugger getRemoteDebugger() {
    return remoteDebugger;
  }

  public BreakpointManager getBreakpointManager(){
    return breakpointManager;
  }
  public WatchManager getWatchManager(){
    return this.watchManager;
  }

  public RemoteThread getCurrentThread() {
    return currentThread;
  }
  public RemoteThreadGroup getCurrentThreadGroup(){
    return currentThreadGroup;
  }

  public void step(StringTokenizer t) throws java.lang.Exception {
    antDebuggerPanel.showStatusMessage("Running...");
    if (currentThread == null) {
        out.println("Nothing suspended.");
        return;
    }
    try {
      if (t.hasMoreTokens()) {
        String nt = t.nextToken().toLowerCase();
        if (nt.equals("up")) {
          currentThread.stepOut();
        } else {
          currentThread.step(true);
        }
      } else {
        currentThread.step(true);
      }
    } catch (IllegalAccessError e) {
      out.println("Current thread is not suspended.");
    }
    antDebuggerPanel.showStatusMessage("Ready");
  }

  public void cont() throws java.lang.Exception {
    antDebuggerPanel.showStatusMessage("Continuing...");
    remoteDebugger.cont();
//    UpdateAllViews();
    antDebuggerPanel.showStatusMessage("Ready");
  }

  public void next() throws java.lang.Exception {
    antDebuggerPanel.showStatusMessage("Running...");
    if( currentThread == null){
      out.println("[AntDebuggerMain] Nothing suspended");
      return;
    }
    try {
      currentThread.next();
    }catch( IllegalAccessError e){
      out.println("[AntDebuggerMain] current thread is not suspended");
    }
//    UpdateAllViews();
    antDebuggerPanel.showStatusMessage("Ready");
  }

  public String loadClass(String classId) throws Exception {
    RemoteClass cls = remoteDebugger.findClass(classId);
    if (cls == null) {
      //현재는 디버깅을 위해서 그냥 출력하도록 둡니다.
      //나중에는 필요 없겠죠. ^^;
      out.print(classId + " not found");
      out.println((classId.indexOf('.') > 0) ? " (try the full name)" : "");
      return null;
    } else {
      //현재는 디버깅을 위해서 그냥 출력하도록 둡니다.
      //나중에는 필요 없겠죠. ^^;
      out.println(cls.toString());

      return cls.toString();
    }
  }

  public String runClass(String command) throws Exception {
    antDebuggerPanel.showStatusMessage("Running...");
  	String argv[] = new String[100];
  	int argc = 0;

    StringTokenizer t = new StringTokenizer(command);

  	if (!t.hasMoreTokens() && lastArgs != null) {
	    t = new StringTokenizer(lastArgs);
      out.println("run " + lastArgs);
  	}
   	while (t.hasMoreTokens()) {
      argv[argc++] = t.nextToken();
      if (argc == 1) {
        // Expand name, if necessary.
        RemoteClass cls = remoteDebugger.findClass(argv[0]);
        if (cls == null) {
          out.println("Could not load the " + argv[0] + " class.");
          return "Could not load the " + argv[0] + " class.";
        }
        argv[0] = cls.getName();
      }
  	}

  	if (argc > 0) {
	    RemoteThreadGroup newGroup = remoteDebugger.run(argc, argv);
	    if (newGroup != null) {
    		currentThreadGroup = newGroup;
    		setThread(1);
    		out.println("running ...");
	    } else {
    		out.println(argv[0] + " failed.");
        return argv[0] + " failed.";
	    }
  	} else {
	    out.println("No class name specified.");
      return "No class name specified.";
  	}
//    antDebuggerPanel.updateViews();
    return null;
  }

  public void close() {
    antDebuggerPanel.showStatusMessage("Closing...");
    remoteDebugger.close();
    antDebuggerPanel.showStatusMessage("Ready");
//    this.antDebuggerPanel.reset();
  }

  void setThread(int threadId) throws Exception {
  	setDefaultThreadGroup();
  	RemoteThread thread = indexToThread(threadId);
  	if (thread == null) {
	    out.println("\"" + threadId +
			       "\" is not a valid thread id.");
	    return;
  	}
  	currentThread = thread;
  }

  private RemoteThread indexToThread(int index) throws Exception {
  	setDefaultThreadGroup();
    RemoteThread list[] = currentThreadGroup.listThreads(true);
  	if (index == 0 || index > list.length) {
	    return null;
  	}
  	return list[index-1];
  }

  private void setDefaultThreadGroup() throws Exception {
    System.out.println("[proxy] setDefaultthreadGroup");
  	if (currentThreadGroup == null) {
	    RemoteThreadGroup tglist[] = remoteDebugger.listThreadGroups(null);
	    currentThreadGroup = tglist[0];	// system threadgroup
      System.out.println("[Proxy] currentThreadGroup : " + currentThreadGroup);
	  }
  }

  public static void usage() {
    String separator = System.getProperty("path.separator");
    System.out.println("Usage: " + progname + " <options> <class> <arguments>");
    System.out.println();
    System.out.println("where options include:");
    System.out.println("    -help             print out this message and exit");
    System.out.println("    -version          print out the build version and exit");
    System.out.println("    -host <hostname>  host machine of interpreter to attach to");
    System.out.println("    -password <psswd> password of interpreter to attach to (from -debug)");
    System.out.println("    -dbgtrace         print info for debugging " + progname);
    System.out.println();
    System.out.println("options forwarded to debuggee process:");
    System.out.println("    -D<name>=<value>  set a system property");
    System.out.println("    -classpath <directories separated by \"" +
                       separator + "\">");
    System.out.println("                      list directories in which to look for classes");
    System.out.println("    -X<option>        non-standard debuggee VM option");
    System.out.println();
    System.out.println("<class> is the name of the class to begin debugging");
    System.out.println("<arguments> are the arguments passed to the main() method of <class>");
    System.out.println();
    System.out.println("For command help type 'help' at " + progname + " prompt");
  }

  public void stepinto() throws java.lang.Exception {
    //TODO: implement this com.antsoft.ant.debugger.DebuggerProxy method;
    antDebuggerPanel.showStatusMessage("Running...");
    if (currentThread == null) {
        out.println("Nothing suspended.");
        return;
    }
    try {
      currentThread.step(true);
    } catch (IllegalAccessError e) {
      out.println("Current thread is not suspended.");
    }
//    antDebuggerPanel.updateViews();
    antDebuggerPanel.showStatusMessage("Ready");
  }

  public void stepup() throws java.lang.Exception {
    //TODO: implement this com.antsoft.ant.debugger.DebuggerProxy method;
    antDebuggerPanel.showStatusMessage("Running...");
    if (currentThread == null) {
        out.println("Nothing suspended.");
        return;
    }
    try {
      currentThread.stepOut();
    } catch (IllegalAccessError e) {
      out.println("Current thread is not suspended.");
    }
//    antDebuggerPanel.updateViews();
    antDebuggerPanel.showStatusMessage("Ready");
  }

  // From here, DebuggerCallback Methods.
  public void printToConsole(String text) throws java.lang.Exception {
    //TODO: implement this sun.tools.debug.DebuggerCallback method;
    console.print(text);
    console.flush();
  }

  public void breakpointEvent(RemoteThread t) throws java.lang.Exception {
    //TODO: implement this sun.tools.debug.DebuggerCallback method;
    out.print("\nBreakpoint hit: ");
    RemoteStackFrame[] stack = t.dumpStack();
    if (stack.length > 0) {
      out.println(stack[0].toString());
        currentThread = t;
    } else {
      currentThread = null; // Avoid misleading results on future "where"
      out.println("Invalid thread specified in breakpoint.");
    }
    //printPrompt();
    //antDebuggerPanel.updateViews();
    antDebuggerPanel.breakpointHitUpdateViews();
    antDebuggerPanel.setRunning( false );
    //debuggerCommandPanel.setRunning( false );
  }

  public void exceptionEvent(RemoteThread t, String errorText) throws java.lang.Exception {
    //TODO: implement this sun.tools.debug.DebuggerCallback method;
	  out.println("\n" + errorText);
	  t.setCurrentFrameIndex(0);
  	currentThread = t;
    //printPrompt();
    antDebuggerPanel.updateViews();
    antDebuggerPanel.setRunning( false );
    //debuggerCommandPanel.setRunning( false );
  }

  public void threadDeathEvent(RemoteThread t) throws java.lang.Exception {
    //TODO: implement this sun.tools.debug.DebuggerCallback method;
    if (t == currentThread) {
      String currentThreadName;

      // Be careful getting the thread name. If this event happens
      // as part of VM termination, it may be too late to get the
      // information, and an exception will be thrown.
      try {
         currentThreadName = " \"" + t.getName() + "\"";
      } catch (Exception e) {
         currentThreadName = "";
      }

      currentThread = null;
      out.println();
      out.println("Current thread" + currentThreadName + " died. Execution continuing...");
      //printPrompt();
    }
  }

  public void quitEvent() throws java.lang.Exception {
    //TODO: implement this sun.tools.debug.DebuggerCallback method;
    String msg = null;
    if (lastArgs != null) {
      StringTokenizer t = new StringTokenizer(lastArgs);
      if (t.hasMoreTokens()) {
        msg = new String("\n" + t.nextToken() + " exited");
      }
    }
    if (msg == null) {
      msg = new String("\nThe application exited (DebuggerProxy)");
    }
    out.println(msg);
    currentThread = null;
//    close();
    //System.exit(0);
//    if(antDebuggerPanel.isReset){
    System.out.println("before Reset");
      antDebuggerPanel.reset();
    System.out.println("after reset");
//    }
//    UpdateAllViews();
  }

  public int getMethodLineNumber( String methodId ){
    int lineNum = -1;
    RemoteStackFrame frame = null;
    if( currentThread == null){
      return 0;
    }
    try{
      frame = currentThread.getCurrentFrame();
    }catch(IllegalAccessError err){
      out.println("[AntDebuggerMain]Current thread isn't suspend");
      err.printStackTrace();
      return -1;
    }catch(ArrayIndexOutOfBoundsException err2){
      out.println("[AntDebuggerMain] Thread is not running (no Stack). ");
      err2.printStackTrace();
      return -1;
    }catch(Exception ex){
      out.println("[AntDebuggerMain] getMethodLineNumber method throws Exception");
      ex.printStackTrace();
      return -1;
    }
    try{
      lineNum = frame.getRemoteClass().getMethodLineNumber(methodId);
    } catch (NoSuchMethodException iobe){
      try{
        out.println("[AntDebuggerMain] " + methodId + " is not a valid line number or " +
                      "Method name for class " +
                      frame.getRemoteClass().getName() );
      }catch(Exception ex1){
        ex1.printStackTrace();
      }
      return -1;
    } catch (NoSuchLineNumberException nse){
      try{
        out.println("[AntDebuggerMain] Line Number information not found in " +
                    frame.getRemoteClass().getName() );
      }catch(Exception ex2){
        ex2.printStackTrace();
      }
      return -1;
    } catch (Exception e3){
      out.println("[AntDebuggerMain] remote Class's getMethodLineNumber throws Exception");
      e3.printStackTrace();
      return -1;
    }

    lineNum--;
    lineNum--;
    lineNum--;
    System.out.println("[AntDebuggerMain] Method( " + methodId + " )의 라인은 " + lineNum + " 입니다.");
    return lineNum;
  }
  void dumpStack(RemoteThread thread, boolean showPC, Vector callstack) throws Exception {
    RemoteStackFrame[] stack = thread.dumpStack();
    if (stack.length == 0) {
      callstack.addElement("Thread is not running (no stack).");
    } else {
      int nFrames = stack.length;
      for (int i = thread.getCurrentFrameIndex(); i < nFrames; i++) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("  [" + (i + 1) + "] ");
        buffer.append(stack[i].toString());
        if (showPC) {
            buffer.append(", pc = " + stack[i].getPC());
        }
        callstack.addElement(buffer.toString());;
      }
    }
  }

  public Vector where( boolean showPC){
    try{
      Vector callstack = new Vector();
      //System.out.println("[proxy] where " + showPC);
      setDefaultThreadGroup();
      RemoteThread list[] = currentThreadGroup.listThreads(true);
      //System.out.println("[Proxy] list : " + list);
      for (int i = 0; i < list.length; i++) {
        callstack.addElement(list[i].getName() + ": ");
        dumpStack(list[i], showPC, callstack);
      }
      //callstackPanel.setCallStack(callstack);
      return callstack;
    } catch(Exception e){
      out.println("AntDebuggerMain] method where throws Exception!!");
      return null;
    }
  }
  /* Print a specified reference.
   * New print() implementation courtesy of S. Blackheath of IBM
   */
  public String print(StringTokenizer t, boolean dumpObject) throws Exception {
    String result = null;
  	if (!t.hasMoreTokens()) {
	    out.println("No objects specified.");
      return "No objects specified.";
  	}

	  int id;
  	RemoteValue obj = null;

//    while (t.hasMoreTokens()) {
	    String expr = t.nextToken();
	    StringTokenizer pieces =
	       new StringTokenizer(expr, printDelimiters, true);

	    String idToken = pieces.nextToken(); // There will be at least one.
	    if (idToken.startsWith("t@")) {
	      /* It's a thread */
	      setDefaultThreadGroup();
	      RemoteThread tlist[] = currentThreadGroup.listThreads(true);
	      try {
	        id = Integer.valueOf(idToken.substring(2)).intValue();
	      } catch (NumberFormatException e) {
	  	    id = 0;
  	    }
  	    if (id <= 0 || id > tlist.length) {
	        out.println("\"" + idToken + "\" is not a valid thread id.");
          return "\"" + idToken + "\" is not a valid thread id.";
        }
        obj = tlist[id - 1];
     } else if (idToken.startsWith("$s")) {
        int slotnum;
        try {
          slotnum = Integer.valueOf(idToken.substring(2)).intValue();
        } catch (NumberFormatException e) {
  	      out.println("\"" + idToken + "\" is not a valid slot.");
          return "\"" + idToken + "\" is not a valid slot.";
  	    }
  	    if (currentThread != null) {
	        RemoteStackVariable rsv[] = currentThread.getStackVariables();
  		    if (rsv == null || slotnum >= rsv.length) {
	          out.println("\"" + idToken + "\" is not a valid slot.");
            return "\"" + idToken + "\" is not a valid slot.";
  		    }
	  	    obj = rsv[slotnum].getValue();
  	    }
	    } else if (idToken.startsWith("0x") ||
  	    Character.isDigit(idToken.charAt(0))) {
	      /* It's an object id. */
  	    try {
  	      id = RemoteObject.fromHex(idToken);
  	    } catch (NumberFormatException e) {
  	      id = 0;
  	    }
        if (id == 0 || (obj = remoteDebugger.get(new Integer(id))) == null) {
  	      out.println("\"" + idToken + "\" is not a valid id.");
          return "\"" + idToken + "\" is not a valid id.";
  	    }
  	  } else {
        /* See if it's a local stack variable */
        RemoteStackVariable rsv = null;
        if (currentThread != null) {
          rsv = currentThread.getStackVariable(idToken);
  		    if (rsv != null && !rsv.inScope()) {
  		      out.println(idToken + " is not in scope.");
            return idToken + " is not in scope.";
  		    }
	  	    obj = (rsv == null) ? null : rsv.getValue();
  	    }
  	    if (rsv == null) {
          String error = null;
          /* See if it's an instance variable */
          String instanceStr = idToken;
          try {
            instanceStr = instanceStr + pieces.nextToken("");
          }
          catch (NoSuchElementException e) {}

          if (currentThread != null)
            rsv = currentThread.getStackVariable("this");
          if (rsv != null && rsv.inScope()) {
            obj = rsv.getValue();
            error = printModifiers(expr,
                                   new StringTokenizer("."+instanceStr, printDelimiters, true),
                                   dumpObject, obj, true, result);
            if (error == null){
              return result;
            }
          }

          // If the above failed, then re-construct the same
          // string tokenizer we had before.
          pieces = new StringTokenizer(instanceStr, printDelimiters, true);
          idToken = pieces.nextToken();

          /* Try interpreting it as a class */
          while (true) {
	          obj = remoteDebugger.findClass(idToken);
  	        if (obj != null)             // break if this is a valid class name
              break;
            if (!pieces.hasMoreTokens()) // break if we run out of input
              break;
            String dot = pieces.nextToken();
            if (!dot.equals("."))        // break if this token is not a dot
              break;
            if (!pieces.hasMoreTokens())
              break;
            // If it is a dot, then add the next token, and loop
            idToken = idToken + dot + pieces.nextToken();
          }
          if (obj == null) {
            if (error == null)
              error = "\"" + expr + "\" is not a " + "valid local or class name.";
          }
          else {
            String error2 = printModifiers(expr, pieces, dumpObject, obj, false, result);
            if (error2 == null){
              return result;
            }else{
              return error2;
            }
          }
          out.println(error);
          return error;
        }
      }
      String error = printModifiers(expr, pieces, dumpObject, obj, false, result);
      if (error != null){
        out.println(error);
        return error;
      }else{
        return result;
      }
//    }
  }

  String printModifiers(String expr, StringTokenizer pieces, boolean dumpObject, RemoteValue obj,
                        boolean could_be_local_or_class, String result)
                        throws Exception
  {
    RemoteInt noValue = new RemoteInt(-1);
    RemoteValue rv = noValue;

    // If the object is null, or a non-object type (integer, array, etc...)
    // then the value must be in rv.
    if (obj == null)
      rv = null;
    else
      if (!obj.isObject())
        rv = obj;

	  String lastField = "";
  	String idToken = pieces.hasMoreTokens() ? pieces.nextToken() : null;
	  while (idToken != null) {
      if (idToken.equals(".")) {
        if (pieces.hasMoreTokens() == false) {
   		    return "\"" + expr + "\" is not a valid expression.";
    		}
    		idToken = pieces.nextToken();

    		if (rv != noValue) {
  		    /* attempt made to get a field on a non-object */
	  	    return "\"" + lastField + "\" is not an object.";
    		}
    		lastField = idToken;

        if (obj instanceof RemoteArray) {
          if (idToken.equals("length")) {
            int size = ((RemoteArray)obj).getSize();
            rv = new RemoteInt(size);
          } else {
            return "\"" + idToken + "\" is not a valid array field";
          }
        } else {
          /* Rather than calling RemoteObject.getFieldValue(), we do this so
           * that we can report an error if the field doesn't exist. */
	        RemoteField fields[] = ((RemoteObject)obj).getFields();
          boolean found = false;
          for (int i = fields.length-1; i >= 0; i--)
            if (idToken.equals(fields[i].getName())) {
              rv = ((RemoteObject)obj).getFieldValue(i);
              found = true;
              break;
            }
          if (!found) {
            if (could_be_local_or_class)
             /* expr is used here instead of idToken, because:
              *   1. we know that we're processing the first token in the line,
              *   2. if the user specified a class name with dots in it, 'idToken'
              *      will only give the first token. */
              return "\"" + expr + "\" is not a valid local, class name, or field of "
                     + obj.description();
            else
              return "\"" + idToken + "\" is not a valid field of "
                     + obj.description();
          }
        }

        // don't give long error message next time round the loop
        could_be_local_or_class = false;
     		if (rv != null && rv.isObject()) {
  		    obj = rv;
	  	    rv = noValue;
    		}
    		idToken =
  		    pieces.hasMoreTokens() ? pieces.nextToken() : null;
 	    } else if (idToken.equals("[")) {
    		if (pieces.hasMoreTokens() == false) {
  		    return "\"" + expr +
  					"\" is not a valid expression.";
     		}
    		idToken = pieces.nextToken("]");
    		try {
  		    int index = Integer.valueOf(idToken).intValue();
  		    rv = ((RemoteArray)obj).getElement(index);
    		} catch (NumberFormatException e) {
  		    return "\"" + idToken +
    					   "\" is not a valid decimal number.";
		    } catch (ArrayIndexOutOfBoundsException e) {
  		    return idToken + " is out of bounds for " +
	  			obj.description();
    		}
    		if (rv != null && rv.isObject()) {
  		    obj = rv;
  		    rv = noValue;
    		}
    		if (pieces.hasMoreTokens() == false ||
  		    (idToken = pieces.nextToken()).equals("]") == false) {
  		    return "\"" + expr +
  				        "\" is not a valid expression.";
	    	}
    		idToken = pieces.hasMoreTokens() ?
		    pieces.nextToken(printDelimiters) : null;

	    } else if (idToken.equals("(")) {
	      return "print <method> not supported yet.";
	    } else {
    		/* Should never get here. */
    		return "invalid expression";
	    }
  	}

  	out.print(expr + " = ");
  	if (rv != noValue) {
	    out.println((rv == null) ? "null" : rv.description());
      result = (rv == null) ? "null" : rv.description();
  	} else if (dumpObject && obj instanceof RemoteObject) {
	    out.println(obj.description() + " {");
      result = obj.description();

	    if (obj instanceof RemoteClass) {
    		RemoteClass cls = (RemoteClass)obj;

    		out.print("    superclass = ");
    		RemoteClass superClass = cls.getSuperclass();
    		out.println((superClass == null) ?
				   "null" : superClass.description());

    		out.print("    loader = ");
    		RemoteObject loader = cls.getClassLoader();
    		out.println((loader == null) ?
				   "null" : loader.description());

    		RemoteClass interfaces[] = cls.getInterfaces();
	    	if (interfaces != null && interfaces.length > 0) {
  		    out.println("    interfaces:");
		      for (int i = 0; i < interfaces.length; i++) {
		        out.println("        " + interfaces[i]);
		      }
    		}
	    }

	    RemoteField fields[] = ((RemoteObject)obj).getFields();
	    if (obj instanceof RemoteClass && fields.length > 0) {
		    out.println();
	    }
	    for (int i = 0; i < fields.length; i++) {
    		String name = fields[i].getTypedName();
    		String modifiers = fields[i].getModifiers();
		    out.print("    " + modifiers + name + " = ");
    		RemoteValue v = ((RemoteObject)obj).getFieldValue(i);
     		out.println((v == null) ? "null" : v.description());
      }
	    out.println("}");
   	} else {
      out.println(obj.toString());
      result = obj.toString();
    }
    //return null;
    return result;
  }

  public void gc() throws Exception {
    RemoteObject[] save_list = new RemoteObject[2];
    save_list[0] = currentThread;
    save_list[1] = currentThreadGroup;
    remoteDebugger.gc(save_list);
  }

  public void catchException(String idClass) throws Exception {
    try {
   		RemoteClass cls = remoteDebugger.findClass(idClass);
	    cls.catchExceptions();
    } catch (Exception e) {
   		out.println("Invalid exception class name: " + idClass);
    }
  }

  public void ignoreException(String idClass) throws Exception {
    String exceptionList[] = remoteDebugger.getExceptionCatchList();

    try {
	    RemoteClass cls = remoteDebugger.findClass(idClass);

      /* Display an error if exception not currently caught */
      boolean caught = false;
      for (int i = 0; i < exceptionList.length; i++) {
        if (idClass.equals(exceptionList[i])) {
          caught = true;
          break;
        }
      }
      if (!caught) {
        out.println("Exception not currently caught: " + idClass);
      } else {
        cls.ignoreExceptions();
      }
    } catch (Exception e) {
    	out.println("Invalid exception class name: " + idClass);
	  }
  }
}
