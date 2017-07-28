/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/AntDebuggerPanel.java,v 1.33 1999/09/01 02:01:20 itree Exp $
 * $Revision: 1.33 $
 * $History: AntDebuggerPanel.java $
 * 
 * *****************  Version 15  *****************
 * User: Bezant       Date: 99-06-23   Time: 1:49a
 * Updated in $/AntIDE/source/ant/debugger
 * watchPanel을 고쳤읍니다.
 * 더이상은.. 잠이와서 못하겠음... 하암.
 * 
 * *****************  Version 14  *****************
 * User: Bezant       Date: 99-06-20   Time: 10:06p
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 13  *****************
 * User: Bezant       Date: 99-06-19   Time: 1:10a
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 12  *****************
 * User: Bezant       Date: 99-06-18   Time: 1:56a
 * Updated in $/AntIDE/source/ant/debugger
 * 실행 프로그램이 종료되었을때 reset시켜주는
 * 루틴을 넣었습니다.
 * 
 * *****************  Version 11  *****************
 * User: Bezant       Date: 99-06-17   Time: 11:39p
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 10  *****************
 * User: Bezant       Date: 99-06-17   Time: 12:09a
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 9  *****************
 * User: Bezant       Date: 99-06-16   Time: 12:18a
 * Updated in $/AntIDE/source/ant/debugger
 * 별루 못했음. -_-;
 * 
 * *****************  Version 8  *****************
 * User: Bezant       Date: 99-06-15   Time: 12:58a
 * Updated in $/AntIDE/source/ant/debugger
 * ThreadPanel과 commandPanel을 좀 손봤슴다.
 * 
 * *****************  Version 7  *****************
 * User: Bezant       Date: 99-06-13   Time: 10:28p
 * Updated in $/AntIDE/source/ant/debugger
 * nd pnl...
 * 
 * *****************  Version 6  *****************
 * User: Bezant       Date: 99-06-12   Time: 12:00p
 * Updated in $/AntIDE/source/ant/debugger
 * 역시 Breakpoint와 breakpoint 발생시 SourcePanel
 * 에 그부분이 보이도록 하는 것을 구현.. 흠.
 * 
 * *****************  Version 5  *****************
 * User: Bezant       Date: 99-06-12   Time: 2:45a
 * Updated in $/AntIDE/source/ant/debugger
 * BreakpointPanel과 여러가지 손봄
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
 * User: Bezant       Date: 99-06-03   Time: 12:23a
 * Updated in $/AntIDE/source/ant/debugger
 * ThreadsPanel 하고 LocalVariablesPanel을 했는데
 * 그건 예전에 있던것을 list로 일단 붙여만 놨음.
 * localViariablesPanel은 버그도 있음. 이만.. 오바
 *
 * *****************  Version 1  *****************
 * User: Multipia     Date: 99-06-02   Time: 10:46p
 * Created in $/AntIDE/source/ant/debugger
 * Ant Debugger Class
 */
package com.antsoft.ant.debugger;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.Vector;
import java.util.Properties;

import com.antsoft.ant.util.plaf.*;
import com.antsoft.ant.util.theme.*;
import com.antsoft.ant.util.ColorList;
import com.antsoft.ant.util.FontList;
/**
 * Ant Debugger의 Main Frame 역할을 하는 Class입니다.
 *
 * @author Kwon, Young Mo
 */
class AntDebuggerPanel extends JPanel {
  public static AntDebuggerProperty property ;

  private AntDebuggerMainFrame mainFrame;
  private BreakpointManager breakpointManager;
  private WatchManager watchManager;
  private DebuggerProxy debuggerProxy;
  private LoadedClassesPanel loadedClassesPanel;
  private MethodPanel methodPanel;
  private TabbedSourceListPanel tabbedSourceListPanel;
  // 우선에는 SourcePanel을 사용
  private SourcePanel sourcePanel;
  private LocalVariablesPanel localVariablesPanel;
  private ThreadCallStackFrame threadCallStackFrame;
  private DebuggerStatusBar debuggerStatusBar;
  private WatchPanel watchPanel;
  private BreakpointPanel breakpointPanel;
  private DebuggerCommandPanel debuggerCommandPanel;
  // by strife
  private ExceptionPanel exceptionPanel;
  private OutputFrame output;

  private JSplitPane classMethodPane, classSourcePane, localWatchPane, listWatchPane;

  ////////////////////////////////////////////////////
  private String host;
  private String password;
  private String javaArgs;
  private String args;
  private PrintStream outStream;
  private PrintStream consoleStream;
  private boolean verbose;
  ////////////////////////////////////////////////////

  static ImageIcon logoIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/antdebugger.gif"));
  private static final String progname = "Ant Debugger 1.0";
  private static final String version = "99/06/05";
  static final String printDelimiters = ".[(";

  public boolean isReset;
  private boolean isShowWatchPanel;
  private boolean isShowLoadedVariablePanel;
  private Vector userLoadedClass = new Vector();
  private String runParam = "";

  /**
   * Only classes in same package can instaciate me
   */
  AntDebuggerPanel( String host, String password, String javaArgs, String args,
               PrintStream outStream, PrintStream consoleStream,
               boolean verbose ) throws Exception {
    this.host = host;
    this.password = password;
    this.javaArgs = javaArgs;
    this.args = args;
    this.outStream = outStream;
    this.consoleStream = consoleStream;


    SplashScreenDebugger splash = new SplashScreenDebugger(logoIcon);
    splash.showStatus("Loading Debugger");
    //progressLabel.setText("Loading Debugger");
    property = AntDebuggerPropertyManager.loadProperty();
    output = new OutputFrame();
    outStream = output.getOutStream();
    consoleStream = output.getConsoleStream();
    
    //기존의 jdk1.1.8\lib\classes.zip 이 classpath에 잡히지 않아서,
    //디버깅 VM을 생성을 하지 못한다.
    //this.javaArgs = "-classpath \"" + getUserClassPath() + "\"";

    debuggerProxy =  new AntDebuggerProxy(host, password, this.javaArgs, args,
              outStream, consoleStream, verbose ,this);

    breakpointManager = new BreakpointManager(debuggerProxy);
    watchManager = new WatchManager(debuggerProxy);
    debuggerProxy.setBreakpointManager(breakpointManager);
    debuggerProxy.setWatchManager(watchManager);

    //progressLabel.setText("Loading.. Property");
    splash.showStatus("Loading.. Property");

    //progressLabel.setText("Loading.. Program");
    splash.showStatus("Loading.. Program");

    sourcePanel = new SourcePanel(debuggerProxy);
    sourcePanel.addBreakpointEventListener(debuggerProxy.getBreakpointManager());
    methodPanel = new MethodPanel(debuggerProxy, sourcePanel);
    methodPanel.addBreakpointEventListener(debuggerProxy.getBreakpointManager());
    localVariablesPanel = new LocalVariablesPanel(debuggerProxy, this);
    watchPanel = new WatchPanel(watchManager, this);
    loadedClassesPanel = new LoadedClassesPanel(debuggerProxy, sourcePanel, methodPanel,watchPanel);
    debuggerStatusBar = new DebuggerStatusBar(debuggerProxy);
    threadCallStackFrame = new ThreadCallStackFrame(debuggerProxy);
    breakpointPanel = new BreakpointPanel(debuggerProxy.getBreakpointManager(), sourcePanel);
    debuggerCommandPanel = new DebuggerCommandPanel(debuggerProxy, this);
    exceptionPanel = new ExceptionPanel(debuggerProxy);

    initSourcePath();
    uiInit();

    //progressLabel.setText("Loading.. Ok");
    splash.showStatus("Loading.. Ok");
    splash.showStatus("AntDebugger1.0 Start..");

    splash.close();
    //progressFrame.setVisible(false);
    isReset = true;

    mainFrame = new AntDebuggerMainFrame(this, debuggerCommandPanel);
//    mainFrame.pack();
    mainFrame.setVisible(true);
  }

  void uiInit() {

    // Ant Look And Feel 적용.
    AntTheme antTheme = new AntTheme();
    AntLookAndFeel.setCurrentTheme(antTheme);

    try {

      //AntLookAndFeel로 세팅
    	//UIManager.setLookAndFeel("com.antsoft.ant.util.plaf.AntLookAndFeel");

      //각 UI Component들의 property를 조정한다
      UIManager.put("ScrollBar.width", new Integer(19));
      UIManager.put("ScrollBar.foreground", ColorList.scrollBarForegroundColor);
      UIManager.put("ScrollBar.background", ColorList.scrollBarBackgroundColor);
      UIManager.put("ScrollBar.thumb", ColorList.scrollBarThumbColor);
      UIManager.put("ScrollBar.thumbDarkShadow", ColorList.scrollBarThumbDarkShadowColor);
      UIManager.put("ScrollBar.thumbLightShadow", ColorList.scrollBarThumbLightShadowColor);
      UIManager.put("ScrollBar.thumbHighlight", Color.white);
      UIManager.put("ScrollBar.track", new Color(230, 230, 230));

      UIManager.put("MenuBar.font", FontList.MenuBarFont);
      UIManager.put("MenuBar.selectionForeground", ColorList.menuBarSelectionForegroundColor);
      UIManager.put("MenuBar.selectionBackground", ColorList.menuBarSelectionBackgroundColor);

      UIManager.put("Menu.selectionForeground", ColorList.menuSelectionForegroundColor);
      UIManager.put("Menu.selectionBackground", ColorList.menuSelectionBackgroundColor);
      UIManager.put("Menu.font", FontList.MenuFont);

      UIManager.put("MenuItem.font", FontList.MenuItemFont);
      UIManager.put("MenuItem.selectionForeground", ColorList.menuItemSelectionForegroundColor );
      UIManager.put("MenuItem.selectionBackground", ColorList.menuItemSelectionBackgroundColor );
      UIManager.put("MenuItem.acceleratorFont", FontList.shotKeyFont);

      UIManager.put("CheckBoxMenuItem.font", FontList.MenuItemFont);
      UIManager.put("CheckBoxMenuItem.selectionForeground", ColorList.menuItemSelectionForegroundColor );
      UIManager.put("CheckBoxMenuItem.selectionBackground", ColorList.menuItemSelectionBackgroundColor );

      UIManager.put("Tree.selectionForeground", ColorList.treeSelectionForegroundColor );
      UIManager.put("Tree.selectionBackground", ColorList.treeSelectionBackgroundColor );

      UIManager.put("Table.selectionForeground", ColorList.listSelectionForegroundColor );
      UIManager.put("Table.selectionBackground", ColorList.listSelectionBackgroundColor );

      UIManager.put("List.selectionForeground", ColorList.listSelectionForegroundColor);
      UIManager.put("List.selectionBackground", ColorList.listSelectionBackgroundColor);

      UIManager.put("ComboBox.selectionForeground", ColorList.comboBoxSelectionForegroundColor);
      UIManager.put("ComboBox.selectionBackground", ColorList.comboBoxSelectionBackgroundColor);
      UIManager.put("ComboBox.background", Color.white);

      UIManager.put("ToolTip.background", ColorList.tooltipBackgroundColor);
      UIManager.put("ToolTip.foreground", Color.black);

      UIManager.put("EditorPane.selectionForeground", Color.white);
      UIManager.put("EditorPane.selectionBackground", ColorList.darkBlue);

//      UIManager.put("Button.border", new AntButtonBorder());


      //UIManager.put("PopupMenu.font", FontList.MenuFont);
      UIManager.put("TabbedPane.tabShadow", Color.lightGray);
      UIManager.put("Label.foreground", ColorList.labelForegroundColor);

      UIManager.put("ProgressBar.selectionForeground", Color.white);
      UIManager.put("ProgressBar.selectionBackground", Color.black);

    } catch (Exception ex) {
      System.out.println("Failed loading Metal");
      System.out.println(ex);
    }
    UIManager.addPropertyChangeListener(new UISwitchListener((JComponent)getRootPane()));
    // Ant Look And Feel 적용 END
    ///////////////////////////////////////////////////////////////////


    int cmp,csp,lwp,liwp;
    setLayout(new BorderLayout());
    // ClassPanel과 MethodPanel을 가르는 SplitPane 설정
    classMethodPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, loadedClassesPanel, methodPanel );
    classMethodPane.setDividerSize(4);
    classMethodPane.setOneTouchExpandable(true);

    // Class, Method Panel과 Source Panel을 가르는 SplitPane 설정
    classSourcePane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, classMethodPane, sourcePanel );
    classSourcePane.setDividerSize(4);
    classSourcePane.setOneTouchExpandable(true);

    // LocalVariablesPanel과 WatchPanel을 가르는 SplitPane 설정
    localWatchPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, localVariablesPanel, watchPanel );
    isShowWatchPanel = true;
    isShowLoadedVariablePanel = true;
    localWatchPane.setDividerSize(4);
    localWatchPane.setOneTouchExpandable(true);

    // 두개의 SplitPane을 가르는 것을 설정
    listWatchPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, classSourcePane, localWatchPane );
    listWatchPane.setDividerSize(4);
    listWatchPane.setOneTouchExpandable(true);

    cmp = property.getClassMethodPaneSize();
    csp = property.getClassSourcePaneSize();
    lwp = property.getLocalWatchPaneSize();
    liwp = property.getListWatchPaneSize();

    if (cmp > 0)
      classMethodPane.setDividerLocation(cmp);
    if (csp > 0)
      classSourcePane.setDividerLocation(csp);
    if (lwp > 0)
      localWatchPane.setDividerLocation(lwp);
    if (liwp > 0)
      listWatchPane.setDividerLocation(liwp);


    add(debuggerCommandPanel, BorderLayout.NORTH);
    JPanel centerPane = new JPanel( new BorderLayout() );
    centerPane.add(listWatchPane, BorderLayout.CENTER);
    centerPane.add(debuggerStatusBar, BorderLayout.SOUTH);
    add( centerPane, BorderLayout.CENTER );

    //add(listWatchPane, BorderLayout.CENTER);
    //add(debuggerStatusBar, BorderLayout.SOUTH);
  }
  public JFrame getMainFrame() {
    return mainFrame;
  }
  public WatchPanel getWatchPanel() {
    return watchPanel;
  }

  public void updateViews() throws Exception {
    // Update하는 순서도 잘알아야 함다.
    initSourcePath();
    showStatusMessage("Updating Loaded Classes...");
    loadedClassesPanel.update();
    System.out.println("[AntDebuggerPanel] LoadedClassesPanel.update ok");

    showStatusMessage("Updating Methods Info...");
    methodPanel.update();
    //System.out.println("[AntDebuggerPanel] MethodPanel.update OK");
    sourcePanel.update();
    //System.out.println("[AntDebuggerPanel] sourcePanel.update OK");
    //listSourc e();
    showStatusMessage("Updating Threads Info...");
    threadCallStackFrame.update();
    System.out.println("[AntDebuggerPanel] threadCallStackFrame.update OK");
    showStatusMessage("Updating Local Variables Info...");
    localVariablesPanel.update();
    System.out.println("[AntDebuggerPanel] localVariablesPanel.update OK");
    showStatusMessage("Updating Breakpoint Lists...");
    breakpointPanel.update();
    System.out.println("[AntDebuggerPanel] breakpointPane.update OK");
    showStatusMessage("Updating Debugger Status Bar...");
    debuggerStatusBar.update();
    //System.out.println("[AntDebuggerPanel] debuggerStatusBar.update OK");
    watchPanel.update();
    exceptionPanel.update();
    //classes();
    //listBreakpoints();
    //where(false);
    mainFrame.requestFocus();
  }

  boolean loadClass() {
    /*
    boolean flag = false;
    boolean load = false;
    while(!flag){
      String classId = JOptionPane.showInputDialog(this, "Type class name to load.", "Load class", JOptionPane.OK_CANCEL_OPTION );
      if ( classId == null ){
        // user가 cancel를 눌렸을때 입니다.
        flag = true;
      }else{
        try {
          String result = debuggerProxy.loadClass(classId);
          if( result != null){
            // DebuggerProxy에서 class Load에 성공해서 RemoteClass의 toString()이
            // 리턴되어 옵니다.
            //antDebuggerPanel.updateViews();
            debuggerProxy.UpdateAllViews();
            flag = true;
            load = true;
          }else{
            // class Load에서 실패한 경우.
            // user에게 로드가 실패 했음을 알리고
            // 다시 할것을 지시합니다.
             String temp = classId + " not found";
            temp += (((classId.indexOf('.') > 0) ? " (try the full name)" : ""));
            JOptionPane.showMessageDialog(this, temp, "Load class failed.. Try Again", JOptionPane.WARNING_MESSAGE);
            flag = false; // 명시적으로 나타내기 위해서..
          }
        } catch ( Exception e ) {
          System.err.println(e.toString());
        }
      }
    } */

    Vector sourcePath = property.getUserClassPath();   // 이름은 sourcePath이지만 사실은 classPath임
    //boolean flag = false;
    boolean load = false;
    Vector classPath = null;

    if (sourcePath == null)
      sourcePath = new Vector();
    
    ClassAddDlg dlg = new ClassAddDlg(mainFrame, "Select package or class file", sourcePath, true);
    dlg.setVisible(true);

    if (dlg.isOK()) {
      classPath = dlg.getClassPath();
      if (classPath == null || classPath.size() == 0)
        return false;
      for(int i = 0; i < classPath.size(); i++) {
        try {
          String result = debuggerProxy.loadClass((String)classPath.elementAt(i));
          if (result != null) {
            userLoadedClass.addElement((String)classPath.elementAt(i));
            load = true;
          } else {
            System.out.println(debuggerProxy.getRemoteDebugger().getSourcePath());
            String msg = (String)classPath.elementAt(i) + " loading failed.. continue? ";
            int j = JOptionPane.showConfirmDialog(mainFrame, msg, "Class Load Failed", JOptionPane.OK_CANCEL_OPTION);
            if (j == JOptionPane.OK_OPTION)
              continue;
            else
              break;
          }
        } catch (Exception e) {
          System.err.println(e.toString());
        }
      }
      if (load)
        debuggerProxy.UpdateAllViews();
    }

    return load;
  }


  boolean runClass() {
    /*
    String classId = null;
    boolean flag = false;
    boolean result = false;
    String error = null;

    while(!flag){
      classId = JOptionPane.showInputDialog(this, "Type class name to run.", "Load class", JOptionPane.OK_CANCEL_OPTION );
      if ( classId == null ){
        flag = true;
      }
      try {
        error = debuggerProxy.runClass(classId);
        if( error != null){
          //실패하면 error 코드가 옵니다.
          JOptionPane.showMessageDialog(this, error, "Running Error", JOptionPane.WARNING_MESSAGE);
          flag = false;
          result = false;
        }else{
          debuggerProxy.UpdateAllViews();
          flag = true;
          result = true;
        }
      } catch ( Exception e ) {
        System.err.println(e.toString());
        flag = true;
        result = false;
      }
    }
	  return result;
    */
    debuggerStatusBar.setStatusMessage("Running...");
    if (userLoadedClass.size() == 0) {
    }
    boolean result = false;
    String error = null;

    RunClassDlg dlg = new RunClassDlg(mainFrame, "ha", userLoadedClass, true);
    dlg.setVisible(true);
    if (dlg.isOK()) {
    	String classId = dlg.getRunClassPath();
      if (classId == null) {
        result = false;
      } else {
        try {
          error = debuggerProxy.runClass(classId + " " + runParam);
          if (error != null) {
            //실패하면 error코드가 나옵니다.
            JOptionPane.showMessageDialog(this, error, "Running Error", JOptionPane.WARNING_MESSAGE);
          } else {
            debuggerProxy.UpdateAllViews();
            result = true;
          }
        } catch (Exception e) {
          System.err.println(e.toString());
          result = false;
        }
      }
    }
    showStatusMessage("Ready");
    return result;

  }

  // 이제부터는 프로그램 실행하기전에 classpath를 잡아줄 필요가 없게 된다
  // 한번 등록해주면 그다음부터 계속 기억하게 됨.
  private String getUserClassPath() {
    StringBuffer classbuf = new StringBuffer();
    Vector classPath = property.getUserClassPath();
    if (classPath == null) {
      classPath = new Vector();
    }
    for(int i=0; i<classPath.size(); i++) {
      classbuf.append((String)classPath.elementAt(i));
      classbuf.append(";");
    }

    return classbuf.toString();
  }

  void initSourcePath () {
    StringBuffer sourcebuf = new StringBuffer();
    Vector sourcePath = property.getUserSourcePath();

    if (sourcePath == null) {
      sourcePath = new Vector();
    }

    try {
      for(int i=0; i<sourcePath.size(); i++) {
        sourcebuf.append((String)sourcePath.elementAt(i));
        sourcebuf.append(";");
      }

      debuggerProxy.getRemoteDebugger().setSourcePath(sourcebuf.toString());

    } catch (Exception e) {
      System.err.println( e.toString());
    }
  }

  // 사용자가 직접 path를 등록하는 경우 실행.
  void setSourcePath( ) {

    try {
      Vector sourcePath, classPath;
      sourcePath = property.getUserSourcePath();
      classPath = property.getUserClassPath();

      if (sourcePath == null)
        sourcePath = new Vector();
      if (classPath == null)
        classPath = new Vector();

      SourcePathDlg dlg = new SourcePathDlg(mainFrame, "hi", sourcePath,classPath,true);
      dlg.setVisible(true);

      if(dlg.isOK()){
        sourcePath = dlg.getSourcePath();
        classPath = dlg.getClassPath();
        property.setUserSourcePath(sourcePath);
        property.setUserClassPath(classPath);
        AntDebuggerPropertyManager.saveProperty(property);

        StringBuffer buf = new StringBuffer();
        for(int i=0; i<sourcePath.size(); i++){
          buf.append((String)sourcePath.elementAt(i));
          buf.append(";");
        }
        for(int i=0; i<classPath.size(); i++){
          buf.append((String)classPath.elementAt(i));
          buf.append(";");
        }

        //debuggerProxy.getRemoteDebugger().setSourcePath(buf.toString());
        //updateViews();
        javaArgs = this.javaArgs = "-classpath \"" + getUserClassPath() + "\"";
        debuggerProxy.close();
        reset();
      }

    } catch (Exception e) {
      System.err.println( e.toString());
    }
  }

  public void exit(){
    property.setLastFrameBound(mainFrame.getBounds());

    property.setClassMethodPaneSize(classMethodPane.getDividerLocation());
    property.setClassSourcePaneSize(classSourcePane.getDividerLocation());
    property.setLocalWatchPaneSize(localWatchPane.getDividerLocation());
    property.setListWatchPaneSize(listWatchPane.getDividerLocation());
    AntDebuggerPropertyManager.saveProperty(property);

    isReset = false;
    if (debuggerProxy != null) debuggerProxy.close();
    System.exit(0);
  }

  public void breakpointHitUpdateViews() throws Exception {
    System.out.println("Breakpoint Hit: ");
    sourcePanel.printSourceCode();
    updateViews();
//    localVariablesPanel.update();
//    threadCallStackFrame.update();
//    watchPanel.update();
  }

  public void showBreakpointFrame(){
    breakpointPanel.showBreakpointFrame();
  }
  public void showThreadCallStackFrame(){
    threadCallStackFrame.showThreadCallStackFrame();
  }
  public void showCallStackPanel(){
    threadCallStackFrame.showCallStack();
  }
  public void showLoadedVariablePane(){
    if( isShowLoadedVariablePanel ){
      // varialbePanel이 보여지고 있는 상황
      if( isShowWatchPanel == true){
        // watchpanel이 보여지고 있으므로
        // watchpanel은 놔두고 variablepanel만 없앤다.
      }else{
        // watchpanel이 보여지고 있지 않은 상태
        // 즉 모두 안보이는 상태로 만들면 된다.
      }
      isShowLoadedVariablePanel = false;
    }else{
      // variablePanel이 보여 지고 있지 않는 상황
      if( isShowWatchPanel == true ){
        // watchpanel이 보여 지고 있으므로
        // variablePanel을 옆에 넣으면 된다.
      }else{
        // watchPanel이 보여지고 있지 않은 상황
        // variablePanel만 보여지면된다.
      }
      isShowLoadedVariablePanel = true;
    }

  }
  public void showWatchPanel() {
    if (isShowWatchPanel) {
      //watchPanel이 보여지고 있는 상태
      if (isShowLoadedVariablePanel) {
        // VariablePanel이보여지고있는 상태이므로
        // watchPanel만 빼내면 된다.
      } else {
        // 둘다 안보이는 상태로 만들면 된다.
      }
      isShowWatchPanel = false;
    } else {
      //watchPanel이 안보이는 상태
      if (isShowLoadedVariablePanel) {
        // 둘다 보이는 상태로 만든다.
      } else {
        // variablePanel만 보이는 상태로 만들면 된다.
      }
      isShowWatchPanel = true;
    }
  }

  public void showExceptionPanel() {
    exceptionPanel.showFrame();
  }

  public void showOutput() {
    output.setBounds(300, 300, 600, 200);
    output.setVisible(true);
  }

  public void setRunning( boolean b ){
    debuggerCommandPanel.setRunning( b );
  }

	public static void main(String argv[]) {
    // Get host attribute, if any.
    String localhost;
    try {
      localhost = InetAddress.getLocalHost().getHostName();
      System.out.println("Localhost name: " + localhost);
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
        //usage();
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
         // usage();
          System.exit(1);
        }
        javaArgs += token + " " + argv[++i] + " ";
      } else if (token.equals("-host")) {
        if (i == (argv.length - 1)) {
          System.out.println("No host specified.");
         // usage();
          System.exit(1);
        }
        host = argv[++i];
      } else if (token.equals("-password")) {
        if (i == (argv.length - 1)) {
          System.out.println("No password specified.");
         // usage();
          System.exit(1);
        }
        password = argv[++i];
      } else if (token.equals("-help")) {
       // usage();
        System.exit(0);
      } else if (token.equals("-version")) {
        System.out.println(progname + " version " + version);
        System.exit(0);
      } else if (token.startsWith("-")) {
        System.out.println("invalid option: " + token);
       // usage();
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
      System.out.println("Host: " + host);
      System.out.println("Password: " + password);
      System.out.println("Java Args: " + javaArgs);
      System.out.println("Cmd Line: " + cmdLine);
      System.out.println("Verbose: " + verbose);
      AntDebuggerPanel antDebuggerPanel = new AntDebuggerPanel(host, password, javaArgs, cmdLine,
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
      System.out.print("Internal exception:  " + e.getMessage());
      System.out.flush();
      e.printStackTrace();
    }
	}

  public void showStatusMessage(String msg) {
    debuggerStatusBar.setStatusMessage(msg);
  }

  // 실행 파라미터 설정
  public void setRunParam() {
    RunParamDlg dlg = new RunParamDlg(mainFrame, "Run Parameter", true);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = dlg.getSize();
    if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width) dlgSize.width = screenSize.width;
    dlg.setLocation((screenSize.width - dlgSize.width) / 2, (screenSize.height - dlgSize.height) / 2);
    dlg.setParamText(runParam);

    dlg.setVisible(true);
    if (dlg.isOk()) {
      runParam = dlg.getParamText();
    }
  }

  // 리모트 디버깅을 할 경우
  public void setRemoteDebug() {
    RemoteDebugDlg dlg = new RemoteDebugDlg(mainFrame, "Remote Debugging...", true);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlgSize = dlg.getSize();
    if (dlgSize.height > screenSize.height) dlgSize.height = screenSize.height;
    if (dlgSize.width > screenSize.width) dlgSize.width = screenSize.width;
    dlg.setLocation((screenSize.width - dlgSize.width) / 2, (screenSize.height - dlgSize.height) / 2);
    dlg.setVisible(true);

    if (dlg.isOk()) {
      // 기본적으로 떠 있는 로컬 디버거를 종료하고 새로운 정보(리모트 디버깅 정보)를
      // 가지고 리셋할 필요가 있다.
      try {
        if (debuggerProxy != null) debuggerProxy.close();
        debuggerProxy = null;
        System.gc();

        debuggerProxy = new AntDebuggerProxy(dlg.getHostName(), null, dlg.getJVMArgs(), args,
                outStream, consoleStream, verbose ,this);
        breakpointManager.reset(debuggerProxy);
        watchManager.reset(debuggerProxy);

        debuggerProxy.setBreakpointManager(breakpointManager);
        debuggerProxy.setWatchManager(watchManager);
      } catch (Exception e) {
        outStream.println("[AntDebuggerPanel] reset throws Exception");
        e.printStackTrace();
        reset();
        return;
      }
      sourcePanel.reset(debuggerProxy);
      sourcePanel.addBreakpointEventListener(debuggerProxy.getBreakpointManager());
      methodPanel.reset(debuggerProxy);
      methodPanel.addBreakpointEventListener(debuggerProxy.getBreakpointManager());
      localVariablesPanel.reset(debuggerProxy,this);
      watchPanel.reset(watchManager,this);
      loadedClassesPanel.reset(debuggerProxy, sourcePanel, methodPanel, watchPanel);
      debuggerStatusBar.reset(debuggerProxy);
      threadCallStackFrame.reset(debuggerProxy);
      breakpointPanel.reset(debuggerProxy.getBreakpointManager(), sourcePanel);
      debuggerCommandPanel.reset(debuggerProxy,this);
      exceptionPanel.reset(debuggerProxy);
      System.out.println("end reset");
      initSourcePath();
      debuggerProxy.UpdateAllViews();
    }
  }

  public final void reset() {
    // debuggerProxy를 다시 생성하고 다른 Panel들을 모두 reset시킨다.
    System.out.println("Starting reset...");
    try {
//      if (debuggerProxy != null) debuggerProxy.close();
      debuggerProxy = null;
      System.gc();
      debuggerProxy =  new AntDebuggerProxy(host, password, javaArgs, args,
                outStream, consoleStream, verbose ,this);
      breakpointManager.reset(debuggerProxy);
      watchManager.reset(debuggerProxy);

      debuggerProxy.setBreakpointManager(breakpointManager);
      debuggerProxy.setWatchManager(watchManager);

    } catch (Exception e) {
      outStream.println("[AntDebuggerPanel] reset throws Exception");
      return;
    }
    sourcePanel.reset(debuggerProxy);
    sourcePanel.addBreakpointEventListener(debuggerProxy.getBreakpointManager());
    methodPanel.reset(debuggerProxy);
    methodPanel.addBreakpointEventListener(debuggerProxy.getBreakpointManager());
    localVariablesPanel.reset(debuggerProxy,this);
    watchPanel.reset(watchManager,this);
    loadedClassesPanel.reset(debuggerProxy, sourcePanel, methodPanel, watchPanel);
    debuggerStatusBar.reset(debuggerProxy);
    threadCallStackFrame.reset(debuggerProxy);
    breakpointPanel.reset(debuggerProxy.getBreakpointManager(), sourcePanel);
    debuggerCommandPanel.reset(debuggerProxy,this);
    exceptionPanel.reset(debuggerProxy);
    System.out.println("end reset");
    initSourcePath();
    debuggerProxy.UpdateAllViews();
  }
}
