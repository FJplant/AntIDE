/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/DebuggerCommandPanel.java,v 1.12 1999/08/31 05:22:57 itree Exp $
 * $Revision: 1.12 $
 * $History: DebuggerCommandPanel.java $
 *
 * *****************  Version 9  *****************
 * User: Bezant       Date: 99-06-18   Time: 1:56a
 * Updated in $/AntIDE/source/ant/debugger
 * 실행 프로그램이 종료되었을때 reset시켜주는
 * 루틴을 넣었습니다.
 *
 * *****************  Version 8  *****************
 * User: Bezant       Date: 99-06-17   Time: 9:49a
 * Updated in $/AntIDE/source/ant/debugger
 * Icon으로 바꾸었음.
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
 * User: Bezant       Date: 99-06-15   Time: 12:58a
 * Updated in $/AntIDE/source/ant/debugger
 * ThreadPanel과 commandPanel을 좀 손봤슴다.
 *
 * *****************  Version 4  *****************
 * User: Bezant       Date: 99-06-13   Time: 10:28p
 * Updated in $/AntIDE/source/ant/debugger
 * nd pnl...
 *
 * *****************  Version 3  *****************
 * User: Multipia     Date: 99-06-02   Time: 10:46p
 * Updated in $/AntIDE/source/ant/debugger
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

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.StringTokenizer;

public class DebuggerCommandPanel extends JToolBar
                                implements ActionListener {
  DebuggerProxy debugger = null;
  AntDebuggerPanel antDebuggerPanel;
  BorderLayout borderLayout1 = new BorderLayout();
//  JFrame myWrapperFrame;
  JPanel commandP;
  JLabel running;
  JButton runBtn;
  JButton suspendBtn, resumeBtn;
  //JButton stopInBtn, stopAtBtn, clearBtn;
  JButton stepintoBtn, stepupBtn, stepiBtn, nextBtn, contBtn;
  JButton gcBtn, loadBtn, useBtn;
  JButton breakpointViewBtn, threadViewBtn, callStackViewBtn;

  static Icon loadIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/load.gif"));
  static Icon useIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/path.gif"));
  static Icon runIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/run.gif"));
  static Icon suspendIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/suspend.gif"));
  static Icon resumeIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/resume.gif"));
  static Icon stepIntoIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/stepinto.gif"));
  static Icon stepUpIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/stepup.gif"));
  static Icon nextIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/next.gif"));
  static Icon contIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/cont.gif"));
  static Icon breakpointViewIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/breakpointview.gif"));
  static Icon threadViewIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/threadview.gif"));
  static Icon callStackViewIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/callstackview.gif"));
  //static Icon Icon = new ImageIcon(LoadedClassesPanel.class.getResource("image/.gif"));

  public DebuggerCommandPanel(DebuggerProxy debugger, AntDebuggerPanel antDebuggerPanel) {
//    myWrapperFrame = new JFrame("Debug Commands");
    super();
    this.antDebuggerPanel = antDebuggerPanel;
    try  {
      jbInit();
      this.debugger = debugger;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  public final void reset(DebuggerProxy debuggerProxy, AntDebuggerPanel antDebuggerPanel){
    this.debugger = debuggerProxy;
    this.antDebuggerPanel = antDebuggerPanel;
    loadBtn.setEnabled(true);
    runBtn.setEnabled(false);
    setRunning(true);
  }

  void jbInit() throws Exception {
    //this.setLayout(borderLayout1);
    //commandP = new JPanel(new GridLayout( 0, 3 ));

    loadBtn = new JButton( loadIcon );
    runBtn = new JButton( runIcon );
    useBtn = new JButton( useIcon );

    loadBtn.setToolTipText("Load Classes");
    runBtn.setToolTipText("Run Class");
    useBtn.setToolTipText("Set / View Classpath");

    loadBtn.addActionListener( this );
    runBtn.addActionListener( this );
    useBtn.addActionListener( this );

    suspendBtn = new JButton( suspendIcon );
    resumeBtn = new JButton( resumeIcon );

    suspendBtn.setToolTipText("Refresh all Views");
    resumeBtn.setToolTipText("Program Reset");

    suspendBtn.addActionListener( this );
    resumeBtn.addActionListener( this );

    stepintoBtn = new JButton( stepIntoIcon );
    stepupBtn = new JButton( stepUpIcon );
    nextBtn = new JButton( nextIcon );
    contBtn = new JButton( contIcon );

    stepintoBtn.setToolTipText("Step Into");
    stepupBtn.setToolTipText("Step Up");
    nextBtn.setToolTipText("Next");
    contBtn.setToolTipText("Continue");

    stepintoBtn.addActionListener( this );
    stepupBtn.addActionListener( this );
    nextBtn.addActionListener( this );
    contBtn.addActionListener( this );

    /*
    commandP.add( loadBtn );
    commandP.add( useBtn );
    commandP.add( runBtn );
    commandP.add( suspendBtn );
    commandP.add( resumeBtn );
    commandP.add( stepintoBtn );
    commandP.add( stepupBtn );
    commandP.add( nextBtn );
    commandP.add( contBtn );
    Border border = new TitledBorder(new LineBorder(Color.black), "Command");
    commandP.setBorder( border );
    */

    //JPanel viewPane = new JPanel();

    breakpointViewBtn = new JButton(breakpointViewIcon);
    threadViewBtn = new JButton(threadViewIcon);
    callStackViewBtn = new JButton(callStackViewIcon);

    breakpointViewBtn.setToolTipText("Toggle Viewer for Breakpoint list");
    threadViewBtn.setToolTipText("Toggle Viewer for Threads");
    callStackViewBtn.setToolTipText("Toggle Viewer for CallStack with Threads");

    breakpointViewBtn.addActionListener( this );
    threadViewBtn.addActionListener( this );
    callStackViewBtn.addActionListener( this );

    /*
    viewPane.add( breakpointViewBtn );
    viewPane.add( threadViewBtn );
    viewPane.add( callStackViewBtn );
    border = new TitledBorder(new LineBorder(Color.black), "View");
    viewPane.setBorder( border );
    */

    //commandP.add( viewPane );
    /*
    JPanel pane = new JPanel();
    pane.setLayout(new BorderLayout() );
    pane.add(commandP, BorderLayout.NORTH);
    pane.add(viewPane, BorderLayout.SOUTH);
    */

    //add( commandP, BorderLayout.CENTER );
    //add( pane, BorderLayout.CENTER );

    // add running label
    running = new JLabel("stopped");
    //add( running, BorderLayout.NORTH );

    add( loadBtn );
    add( useBtn );
    add( runBtn );
    addSeparator();
    add( suspendBtn );
    add( resumeBtn );
    addSeparator();
    add( stepintoBtn );
    add( stepupBtn );
    add( nextBtn );
    add( contBtn );
    addSeparator();
    add( breakpointViewBtn );
    add( threadViewBtn );
    add( callStackViewBtn );
    add( Box.createHorizontalGlue());

    //myWrapperFrame.getContentPane().add(this, BorderLayout.CENTER);
    //myWrapperFrame.pack();
    //myWrapperFrame.setVisible(true);
    runBtn.setEnabled(false);
    setRunning(true);
  }

  public void setRunning(boolean b) {
    if (b)
      running.setText("Running");
    else
      running.setText("Stopped");

    // 실행중일때에는 Button을 Disable 한다.

    nextBtn.setEnabled(!b);
    contBtn.setEnabled(!b);
    stepintoBtn.setEnabled(!b);
    stepupBtn.setEnabled(!b);
    suspendBtn.setEnabled(!b);
    resumeBtn.setEnabled(!b);
  }

  void stepInto() {
    setRunning(true);
    try {
      debugger.stepinto();
    } catch(Exception e) {
      e.printStackTrace();
    }
    /*
    String str = "";
    StringTokenizer tn = new StringTokenizer(str, " :\t\n\r");
    setRunning(true);
    try{
      debugger.step(tn);
    }catch(Exception e){
      e.printStackTrace();
    }
    */
  }

  void stepUp() {
    setRunning(true);
    try {
      debugger.stepup();
    } catch(Exception e) {
      e.printStackTrace();
    }
    /*
    String str = "up";
    StringTokenizer tn = new StringTokenizer(str, " :\t\n\r");
    setRunning(true);
    try {
      debugger.step(tn);
    } catch(Exception e) {
      e.printStackTrace();
    }
    */
  }

  void next() {
    setRunning(true);
    try {
      debugger.next();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  void cont() {
    setRunning(true);
    try {
      debugger.cont();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  void load() {
    if (antDebuggerPanel.loadClass()) {
      //setRunning( false );
//      loadBtn.setEnabled( false );
      runBtn.setEnabled(true);
    }
  }

  void run() {
    if (antDebuggerPanel.runClass()) {
      runBtn.setEnabled(false);
      setRunning(false);
    }
  }

  void use() {
    antDebuggerPanel.setSourcePath();
  }
  void suspend() {
    debugger.UpdateAllViews();
    JOptionPane.showMessageDialog(this, "아직 구현되지 않았습니다.", "Sorry..", JOptionPane.INFORMATION_MESSAGE);
  }
  void resume() {
    debugger.close();
    antDebuggerPanel.reset();
    JOptionPane.showMessageDialog(this, "아직 구현되지 않았습니다.", "Sorry..", JOptionPane.INFORMATION_MESSAGE);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == stepintoBtn) {
      stepInto();
    } else if (e.getSource() == stepupBtn) {
      stepUp();
    } else if (e.getSource() == nextBtn) {
      next();
    } else if (e.getSource() == contBtn) {
      cont();
    } else if (e.getSource() == loadBtn) {
      load();
    } else if (e.getSource() == runBtn) {
      run();
    } else if (e.getSource() == useBtn) {
      use();
    } else if (e.getSource() == breakpointViewBtn) {
      antDebuggerPanel.showBreakpointFrame();
    } else if (e.getSource() == threadViewBtn) {
      antDebuggerPanel.showThreadCallStackFrame();
    } else if (e.getSource() == callStackViewBtn) {
      antDebuggerPanel.showCallStackPanel();
    } else if (e.getSource() == suspendBtn ) {
      suspend();
    } else if (e.getSource() == resumeBtn ) {
      resume();
    }
  }
  /*public void showDebuggerCommandFrame() {
    //System.out.println("[Breakpoint] visible : " + myWrapperFrame.isShowing());
    if (myWrapperFrame.isShowing()) {
      myWrapperFrame.setVisible(false);
    } else {
      myWrapperFrame.setVisible(true);
    }
  } */
}

