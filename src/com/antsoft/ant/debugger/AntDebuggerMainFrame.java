/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/AntDebuggerMainFrame.java,v 1.10 1999/08/27 06:41:13 strife Exp $
 * $Revision: 1.10 $
 * $History: AntDebuggerMainFrame.java $
 * 
 * *****************  Version 8  *****************
 * User: Bezant       Date: 99-06-19   Time: 1:10a
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 7  *****************
 * User: Bezant       Date: 99-06-17   Time: 12:09a
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 6  *****************
 * User: Bezant       Date: 99-06-16   Time: 12:18a
 * Updated in $/AntIDE/source/ant/debugger
 * 별루 못했음. -_-;
 * 
 * *****************  Version 5  *****************
 * User: Bezant       Date: 99-06-13   Time: 10:28p
 * Updated in $/AntIDE/source/ant/debugger
 * nd pnl...
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
 * User: Bezant       Date: 99-06-04   Time: 12:49a
 * Updated in $/AntIDE/source/ant/debugger
 * 조매바꿨음
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
import java.awt.event.*;
import javax.swing.*;

/**
 * Ant Debugger의 Main Frame 역할을 하는 Class입니다.
 *
 * @author Kwon, Young Mo
 */
class AntDebuggerMainFrame extends JFrame implements WindowListener {
  private DebuggerCommandPanel commandPanel;
  private AntDebuggerPanel antDebuggerPanel;

  /**
   * Only classes in same package can instaciate me
   */
  AntDebuggerMainFrame(AntDebuggerPanel antDebuggerPanel, DebuggerCommandPanel commandPanel) {
    super( "Ant Debugger 1.0" );
    this.commandPanel = commandPanel;
    this.antDebuggerPanel = antDebuggerPanel;
    uiInit();

    Rectangle lastBounds = antDebuggerPanel.property.getLastFrameBound();
    if(lastBounds != null) {
       setBounds(lastBounds);
    }
    else{
      // Set Main Frame's Size
      setSize(new Dimension(800, 600));
      // Center the window
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = getSize();
      if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
      if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
      setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
      // End ------- center the window ------------
    }

    getContentPane().add(antDebuggerPanel, BorderLayout.CENTER);
    //pack();
    setVisible(true);

    addWindowListener( this );
  }

  void uiInit() {
    // build menus
    JMenuBar menuBar = new JMenuBar();
    menuBar.add( new FileMenu() );
    menuBar.add( new DebugMenu() );
    menuBar.add( new ViewMenu() );
    menuBar.add( new HelpMenu() );
    setJMenuBar(menuBar);
    getContentPane().setLayout(new BorderLayout());
  }

  protected void loadClass(){
    //antDebuggerPanel.loadClass();
    commandPanel.load();
  }

  protected void runClass() {
    //antDebuggerPanel.runClass();
    commandPanel.run();
  }

  protected void setSourcePath( ) {
    //antDebuggerPanel.setSourcePath();
    commandPanel.use();
  }
  protected void showBreakpointPanel(){
    //여기서는 BreakpointPanel에 프레임으로 보여지고 있으면 감추고
    //아니면 보여주는 역할을 하면 된다.
    antDebuggerPanel.showBreakpointFrame();
  }
  protected void showThreadPanel(){
    antDebuggerPanel.showThreadCallStackFrame();
  }
  protected void showCallStackPanel(){
    antDebuggerPanel.showCallStackPanel();
  }
  protected void showWatchPanel(){
    antDebuggerPanel.showWatchPanel();
  }
  protected void showLocalVariablesPanel(){
    antDebuggerPanel.showLoadedVariablePane();
  }

  class FileMenu extends JMenu implements ActionListener {
    JMenuItem remoteDebug, loadClass, runClass, setSourcePath, exit;
    FileMenu() {
      super("File");
      remoteDebug = new JMenuItem("Remote Debugging...");
      loadClass = new JMenuItem("Load Class...");
      runClass = new JMenuItem("Run Class...");
      setSourcePath = new JMenuItem("Set Source Path...");
      exit = new JMenuItem("Exit");

      remoteDebug.addActionListener(this);
      loadClass.addActionListener(this);
      runClass.addActionListener(this);
      setSourcePath.addActionListener(this);
      exit.addActionListener(this);

      add(remoteDebug);
      add(loadClass);
      add(runClass);
      add(setSourcePath);
      addSeparator();
      add(exit);
    }

    public void actionPerformed( ActionEvent e ) {
      if (e.getSource() == remoteDebug) {
        // 리모트 디버깅을 할 경우
        antDebuggerPanel.setRemoteDebug();
      } else if (e.getSource() == loadClass) {
        loadClass();
      } else if (e.getSource() == runClass) {
        runClass();
      } else if (e.getSource() == setSourcePath) {
        setSourcePath();
      } else if (e.getSource() == exit) {
        // 디버거의 상태를 저장한다.
        antDebuggerPanel.exit();
        //debuggerProxy.close();
        //System.exit(0);
      }
    }
  }

  class DebugMenu extends JMenu implements ActionListener {
    JMenuItem run, reset;
    JMenuItem stopIn, stopAt;
    JMenuItem cont, stepOver, stepInto, stepUp;
    JMenuItem runParam;

    DebugMenu() {
      super("Debug");
      run = new JMenuItem("Run");//, KeyEvent.VK_F9);
      //reset = new JMenuItem("Reset");
      //stopIn = new JMenuItem("Stop In...");
      //stopAt = new JMenuItem("Stop At...");
      cont = new JMenuItem("Cont");
      stepOver = new JMenuItem("Next");//, KeyEvent.VK_F8);
      stepInto = new JMenuItem("Step Into");//, KeyEvent.VK_F7);
      stepUp = new JMenuItem("Step Up");
      runParam = new JMenuItem("Run Parameter");

      run.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
      cont.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, Event.CTRL_MASK));
      stepOver.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
      stepInto.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
      stepUp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, Event.CTRL_MASK));

      run.addActionListener(this);
      cont.addActionListener(this);
      stepOver.addActionListener(this);
      stepInto.addActionListener(this);
      stepUp.addActionListener(this);
      runParam.addActionListener(this);

      add(run);
      //add(reset);
      //addSeparator();
      //add(stopIn);
      //add(stopAt);
      addSeparator();
      add(cont);
      add(stepOver);
      add(stepInto);
      add(stepUp);
      addSeparator();
      add(runParam);
    }

    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == run) {
        commandPanel.run();
      //} else if ( e.getSource() == reset ) {
      //} else if ( e.getSource() == stopIn ) {
      //} else if ( e.getSource() == stopAt ) {
      } else if (e.getSource() == cont) {
        commandPanel.cont();
      } else if (e.getSource() == stepOver) {
        commandPanel.next();
      } else if (e.getSource() == stepInto) {
        commandPanel.stepInto();
      } else if (e.getSource() == stepUp) {
        commandPanel.stepUp();
      } else if (e.getSource() == runParam) {
        antDebuggerPanel.setRunParam();
      }
    }
  }

  class ViewMenu extends JMenu implements ActionListener {
    JMenuItem watch, localVariables, breakpoint, thread, callStack, exceptions, output;
    //commandFrame;

    ViewMenu() {
      super("View");
      watch = new JMenuItem("View Watch");
      watch.addActionListener(this);
      localVariables = new JMenuItem("View LocalVariables");
      localVariables.addActionListener(this);
      breakpoint = new JMenuItem("View Breakpoints");
      breakpoint.addActionListener(this);
      thread = new JMenuItem("View Threads");
      thread.addActionListener(this);
      callStack = new JMenuItem("View Call Stack");
      callStack.addActionListener(this);
      exceptions = new JMenuItem("View Exceptions be Caught");
      exceptions.addActionListener(this);
      output = new JMenuItem("View Output");
      output.addActionListener(this);
      //commandFrame = new JMenuItem("View Command");
      //commandFrame.addActionListener(this);

      add(watch);
      add(localVariables);
      add(breakpoint);
      add(thread);
      add(callStack);
      add(exceptions);
      add(output);
      //add(commandFrame);
    }

    public void actionPerformed( ActionEvent e ) {
      if ( e.getSource() == watch ) {
        //WatchPanel이 보여지고 있는지 살펴 보고 보여지고 있으면 setVisible(false)
        //하고 아니면 setVisible(true)함다.
        showWatchPanel();
      } else if ( e.getSource() == localVariables ) {
        //localVariables에 대해서 보여지고 아니고 결정
        showLocalVariablesPanel();
      } else if ( e.getSource() == breakpoint ) {
        //breakpointPanel에 대해서 보여지고 아니고를 결정
        System.out.println("[ViewMenu] showBreakpointPanel()");
        showBreakpointPanel();
      } else if ( e.getSource() == thread ) {
        //ThreadPanel에 대해서 serVisible을 결정
        showThreadPanel();
      } else if ( e.getSource() == callStack ) {
        //CallStack에 대해서
        showCallStackPanel();
      //} else if ( e.getSource() == commandFrame ) {
      //  antDebuggerPanel.showDebuggerCommandFrame();
      } else if (e.getSource() == exceptions) {
        antDebuggerPanel.showExceptionPanel();
      } else if (e.getSource() == output) {
        antDebuggerPanel.showOutput();
      }
    }
  }

  class HelpMenu extends JMenu {
    JMenuItem about;

    HelpMenu() {
      super("Help");
      about = new JMenuItem("About...");

      add(about);
    }
  }

  public void windowOpened(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowClosing(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
    //debuggerProxy.close();
    //System.exit(0);
    antDebuggerPanel.exit();
  }

  public void windowClosed(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;

  }

  public void windowIconified(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowDeiconified(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowActivated(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }

  public void windowDeactivated(WindowEvent e) {
    //TODO: implement this java.awt.event.WindowListener method;
  }
}
