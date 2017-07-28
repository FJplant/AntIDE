/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /AntIDE/source/ant/debugger/AntDebuggerMain.java 9     99-06-17 11:39p Bezant $
 * $Revision: 9 $
 * $History: AntDebuggerMain.java $
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
class AntDebuggerMain implements DebuggerProxy, DebuggerCallback {
  private AntDebuggerMainFrame mainFrame;
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

	public AntDebuggerMain( String host, String password, String javaArgs, String args,
               PrintStream outStream, PrintStream consoleStream,
               boolean verbose ) throws Exception {
    breakpointManager = new BreakpointManager(this);
    watchManager = new WatchManager(this);
    antDebuggerPanel = new AntDebuggerPanel(this);
    //debuggerCommandPanel = new DebuggerCommandPanel(this, antDebuggerPanel);
    mainFrame = new AntDebuggerMainFrame(this, antDebuggerPanel);
    /*mainFrame.getContentPane().add(antDebuggerPanel, BorderLayout.CENTER);
    mainFrame.pack();
    mainFrame.setVisible(true);
    */

    initRemoteDebugger( host, password, javaArgs, args, outStream, consoleStream, verbose );
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
      System.out.println("[AntDebuggerMain] AntDebuggerPanel의 updateViews에서 Exceptoin발생!!");
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

	public static void main( String argv[] ) {
    // Get host attribute, if any.
    String localhost;
    try {
      localhost = InetAddress.getLocalHost().getHostName();
    } catch (Exception ex) {
      localhost = null;
    }
    if (localhost == null) {
      localhost = "localhost";
    }
    String host = null;
    String password = null;
    String cmdLine = "";
    String javaArgs = "";

    boolean verbose = false;

    for (int i = 0; i < argv.length; i++) {
      String token = argv[i];
      if (token.equals("-dbgtrace")) {
        verbose = true;
      } else if (token.equals("-X")) {
        System.out.println(
               "Use 'java -X' to see the available non-standard options");
        System.out.println();
        usage();
        System.exit(1);
      } else if (
        // Standard VM options passed on
        token.startsWith("-D") ||
        // NonStandard options passed on
        token.startsWith("-X") ||
        // Old-style options (These should remain in place as long as
        //  the standard VM accepts them)
        token.equals("-noasyncgc") || token.equals("-prof") ||
        token.equals("-verify") || token.equals("-noverify") ||
        token.equals("-verifyremote") ||
        token.startsWith("-ms") || token.startsWith("-mx") ||
        token.startsWith("-ss") || token.startsWith("-oss") ) {

        javaArgs += token + " ";
      } else if (token.equals("-classpath")) {
        if (i == (argv.length - 1)) {
          System.out.println("No classpath specified.");
          usage();
          System.exit(1);
        }
        javaArgs += token + " " + argv[++i] + " ";
      } else if (token.equals("-host")) {
        if (i == (argv.length - 1)) {
          System.out.println("No host specified.");
          usage();
          System.exit(1);
        }
        host = argv[++i];
      } else if (token.equals("-password")) {
        if (i == (argv.length - 1)) {
          System.out.println("No password specified.");
          usage();
          System.exit(1);
        }
        password = argv[++i];
      } else if (token.equals("-help")) {
        usage();
        System.exit(0);
      } else if (token.equals("-version")) {
        System.out.println(progname + " version " + version);
        System.exit(0);
      } else if (token.startsWith("-")) {
        System.out.println("invalid option: " + token);
        usage();
        System.exit(1);
      } else {
        // Everything from here is part of the command line
        cmdLine = token + " ";
        for (i++; i < argv.length; i++) {
          cmdLine += argv[i] + " ";
        }
        break;
      }
    }
    if (host != null && password == null) {
      System.out.println("A debug password must be specified for " +
                         "remote debugging.");
      System.exit(1);
    }
    if (host == null) {
      host = localhost;
    }

    try {
      if (!host.equals(localhost) && password.length() == 0) {
        System.out.println(
            "No password supplied for accessing remote " +
            "Java interpreter.");
        System.out.println(
            "The password is reported by the remote interpreter" +
            "when it is started.");
        System.exit(1);
      }
      // Show command frame
      AntDebuggerMain antDebuggerMain = new AntDebuggerMain(host, password, javaArgs, cmdLine,
              System.out, System.out, verbose );
      //Thread command = new Thread( antDebuggerMain );
      //command.start();
    } catch(SocketException se) {
      System.out.println("Failed accessing debugging session on " +
                           host + ": invalid password.");
    } catch(NumberFormatException ne) {
      System.out.println("Failed accessing debugging session on " +
                           host + ": invalid password.");
    } catch(Exception e) {
      System.out.print("Internal exception:  ");
      System.out.flush();
      e.printStackTrace();
    }
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

  public void step(StringTokenizer t) throws java.lang.Exception {
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
  }

  public void cont() throws java.lang.Exception {
    remoteDebugger.cont();
  }

  public void next() throws java.lang.Exception {
    if( currentThread == null){
      out.println("[AntDebuggerMain] Nothing suspended");
      return;
    }
    try {
      currentThread.next();
    }catch( IllegalAccessError e){
      out.println("[AntDebuggerMain] current thread is not suspended");
    }
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

  public void runClass(String command) throws Exception {
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
          return;
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
	    }
  	} else {
	    out.println("No class name specified.");
  	}

    antDebuggerPanel.updateViews();
  }

  public void close() {
    remoteDebugger.close();
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
  	if (currentThreadGroup == null) {
	    RemoteThreadGroup tglist[] = remoteDebugger.listThreadGroups(null);
	    currentThreadGroup = tglist[0];	// system threadgroup
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
  }

  public void stepup() throws java.lang.Exception {
    //TODO: implement this com.antsoft.ant.debugger.DebuggerProxy method;
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
      msg = new String("\nThe application exited");
    }
    out.println(msg);
    currentThread = null;
    close();
    System.exit(0);
    //antDebuggerPanel.updateViews();
  }

  public int getMethodLineNumber( String methodId ){
    int lineNum = -1;
    RemoteStackFrame frame = null;
    try{
      frame = currentThread.getCurrentFrame();
    }catch(IllegalAccessError err){
      out.println("[AntDebuggerMain]Current thread isn't suspend");
      return -1;
    }catch(ArrayIndexOutOfBoundsException err2){
      out.println("[AntDebuggerMain] Thread is not running (no Stack). ");
      return -1;
    }catch(Exception ex){
      out.println("[AntDebuggerMain] getMethodLineNumber method throws Exception");
      return -1;
    }
    try{
      lineNum = frame.getRemoteClass().getMethodLineNumber(methodId);
    } catch (NoSuchMethodException iobe){
      try{
        out.println("[AntDebuggerMain] " + methodId + " is not a valid line number or " +
                      "Method name for class " +
                      frame.getRemoteClass().getName() );
      }catch(Exception ex1){}
      return -1;
    } catch (NoSuchLineNumberException nse){
      try{
        out.println("[AntDebuggerMain] Line Number information not found in " +
                    frame.getRemoteClass().getName() );
      }catch(Exception ex2){}
      return -1;
    } catch (Exception e3){
      out.println("[AntDebuggerMain] remote Class's getMethodLineNumber throws Exception");
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

      setDefaultThreadGroup();
      RemoteThread list[] = currentThreadGroup.listThreads(true);
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

}

