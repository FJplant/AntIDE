/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/DebuggerStatusBar.java,v 1.8 1999/08/31 07:12:41 itree Exp $
 * $Revision: 1.8 $
 * $History: DebuggerStatusBar.java $
 *
 * *****************  Version 4  *****************
 * User: Bezant       Date: 99-06-19   Time: 1:10a
 * Updated in $/AntIDE/source/ant/debugger
 *
 * *****************  Version 3  *****************
 * User: Bezant       Date: 99-06-18   Time: 1:56a
 * Updated in $/AntIDE/source/ant/debugger
 * 실행 프로그램이 종료되었을때 reset시켜주는
 * 루틴을 넣었습니다.
 *
 * *****************  Version 2  *****************
 * User: Bezant       Date: 99-06-13   Time: 10:28p
 * Updated in $/AntIDE/source/ant/debugger
 * nd pnl...
 *
 * *****************  Version 1  *****************
 * User: Multipia     Date: 99-06-02   Time: 10:46p
 * Created in $/AntIDE/source/ant/debugger
 * Ant Debugger Class
 */
package com.antsoft.ant.debugger;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import sun.tools.debug.*;

/**
 * Debugger의 상태바입니다.
 *
 * @author
 */
class DebuggerStatusBar extends JPanel {
  //private RemoteDebugger remoteDebugger;
  private DebuggerProxy debuggerProxy;
  private JLabel statusMessage;
  private JLabel totalMemory;
  private JLabel currentThread;
  private JLabel currentThreadGroup;
  // by strife
  private JProgressBar progress;

  PrintStream out = System.out;
  PrintStream console = System.out;

  /**
   * Only classes in same package can instaciate me
   */
  DebuggerStatusBar(DebuggerProxy proxy) {
    this.debuggerProxy = proxy;
    uiInit();
  }
  public final void reset(DebuggerProxy debuggerProxy){
    this.debuggerProxy = debuggerProxy;
    totalMemory.setText(" Total : 0K");
  }

  void uiInit() {
    //setLayout(new FlowLayout(FlowLayout.LEFT));
    setLayout(new BorderLayout());
//    setLayout( new FlowLayout(FlowLayout.LEFT,0,0) );

    JButton b = new JButton();
    Font f = new Font(b.getFont().getName(), Font.PLAIN, 13);

    JPanel pane = new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));

      statusMessage = new JLabel(" Ready");
      statusMessage.setFont(f);
      statusMessage.setForeground(Color.black);
      //statusMessage.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.white, Color.gray));
      //statusMessage.setPreferredSize(new Dimension(300,21));

      currentThread = new JLabel(" current thread");
      currentThread.setFont(f);
      currentThread.setForeground(Color.black);
      currentThread.setPreferredSize(new Dimension(120,21));
      currentThread.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.white, Color.gray));
      currentThreadGroup = new JLabel(" current thread group");
      currentThreadGroup.setFont(f);
      currentThreadGroup.setForeground(Color.black);
      currentThreadGroup.setPreferredSize(new Dimension(180,21));
      currentThreadGroup.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.white, Color.gray));
      totalMemory = new JLabel(" Memory : 0");
      totalMemory.setFont(f);
      totalMemory.setForeground(Color.black);
      totalMemory.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.white, Color.gray));
      totalMemory.setPreferredSize(new Dimension(90,21));
      progress = new JProgressBar(0, 100);
      progress.setPreferredSize(new Dimension(150,21));
      progress.setStringPainted(true);

    add(statusMessage,BorderLayout.CENTER);
    pane.add(currentThread);
    pane.add(currentThreadGroup);
    pane.add(totalMemory);
    pane.add(progress);

    add(pane, BorderLayout.EAST);
  }

  public void setStatusMessage(String msg) {
    statusMessage.setText(" " + msg);
    statusMessage.repaint();
  }

  public void update() {
//    statusMessage.setText("Ready");
    try {
      if (debuggerProxy != null) {
        RemoteDebugger debugger = debuggerProxy.getRemoteDebugger();
        if (debugger != null) {
//          totalMemory.setText("Memory : " + debugger.totalMemory());
          int total = debugger.totalMemory();
          int use = total - debugger.freeMemory();
          totalMemory.setText(" Total : " + (total / 1024) + "K");
          progress.setValue(use * 100 / total);
          RemoteThread cThread = debuggerProxy.getCurrentThread();
          if(cThread != null) {
            currentThread.setText(" " + cThread.getName());
          }
          RemoteThreadGroup cGroup = debuggerProxy.getCurrentThreadGroup();
          if (cGroup != null) {
            currentThreadGroup.setText(" " + cGroup.getName());
          }
        }
      }
    } catch (Exception e) {
      //console.println("[DebuggerStatusBar] update Exception occur : ");
      e.printStackTrace();
      out.println("[DebuggerStatusBar] update Exception occur : " + e.toString());
    }
  }
}

