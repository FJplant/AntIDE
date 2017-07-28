/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/SourcePanel.java,v 1.17 1999/08/31 08:09:33 itree Exp $
 * $Revision: 1.17 $
 * $History: SourcePanel.java $
 * 
 * *****************  Version 14  *****************
 * User: Bezant       Date: 99-06-18   Time: 1:56a
 * Updated in $/AntIDE/source/ant/debugger
 * ���� ���α׷��� ����Ǿ����� reset�����ִ�
 * ��ƾ�� �־����ϴ�.
 * 
 * *****************  Version 13  *****************
 * User: Bezant       Date: 99-06-16   Time: 12:18a
 * Updated in $/AntIDE/source/ant/debugger
 * ���� ������. -_-;
 * 
 * *****************  Version 12  *****************
 * User: Bezant       Date: 99-06-15   Time: 12:58a
 * Updated in $/AntIDE/source/ant/debugger
 * ThreadPanel�� commandPanel�� �� �պý���.
 * 
 * *****************  Version 11  *****************
 * User: Bezant       Date: 99-06-12   Time: 12:03p
 * Updated in $/AntIDE/source/ant/debugger
 * ���� Breakpoint�� breakpoint �߻��� SourcePanel
 * �� �׺κ��� ���̵��� �ϴ� ���� ����.. ��.
 * 
 * *****************  Version 10  *****************
 * User: Bezant       Date: 99-06-12   Time: 2:45a
 * Updated in $/AntIDE/source/ant/debugger
 * BreakpointPanel�� �������� �պ�
 * 
 * *****************  Version 9  *****************
 * User: Bezant       Date: 99-06-11   Time: 8:38p
 * Updated in $/AntIDE/source/ant/debugger
 * Breakpoint Manage�� ���� ���ɰ����� �� �־����ϴ�.
 * ��..
 * 
 * *****************  Version 8  *****************
 * User: Bezant       Date: 99-06-10   Time: 8:40p
 * Updated in $/AntIDE/source/ant/debugger
 * �ɸ� �ٲ���.
 * breakpointEntity�� ���� BreakpointEvent�� ��ü
 * 
 * *****************  Version 7  *****************
 * User: Bezant       Date: 99-06-09   Time: 2:17a
 * Updated in $/AntIDE/source/ant/debugger
 * �̰� ��������� �峭�� �ƴϱ�..
 * �̰� ���� ���� ���ϸ� ������ ��򸮰ڴµ�.  TT;
 * ���� ������!!!
 * 
 * *****************  Version 6  *****************
 * User: Bezant       Date: 99-06-07   Time: 12:43a
 * Updated in $/AntIDE/source/ant/debugger
 * �̷��� �� ������ ���� �����ָ� ���ڱ���.
 * �׸��� ��ü���� ������ �� �߶Ծ����� ����
 * �ɳ����� �� ���� �ϴ� ���� ������ ����
 * �߰� ���� ������..
 * ������ �Ϸ����� ����ġ��~~~
 * 
 * *****************  Version 5  *****************
 * User: Bezant       Date: 99-06-05   Time: 1:59a
 * Updated in $/AntIDE/source/ant/debugger
 * ���õ� �������Ϸ�..
 * ������ �� ������ �ؾ���.
 * 
 * *****************  Version 4  *****************
 * User: Multipia     Date: 99-06-02   Time: 10:46p
 * Updated in $/AntIDE/source/ant/debugger
 *
 * *****************  Version 3  *****************
 * User: Multipia     Date: 99-05-16   Time: 11:42p
 * Updated in $/AntIDE/source/ant/debugger
 *
 * *****************  Version 2  *****************
 * User: Multipia     Date: 99-05-11   Time: 8:33p
 * Updated in $/AntIDE/source/ant/debugger
 * ���� ����θ� ����
 */

package com.antsoft.ant.debugger;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import javax.swing.*;

import sun.tools.debug.*;

public class SourcePanel extends JPanel implements ActionListener, EditorOptions {
  private DebuggerProxy debuggerProxy;
  private BreakpointEventListener breakpointEventListener;
  private BorderLayout borderLayout1 = new BorderLayout();
  private String currentClassId;
  private String stopActionCommand = "Stop in Selected Line";
  private String clearActionCommand = "Clear in Selected Line";
  private JButton stopButton;
  private JButton clearButton;
//  private JScrollPane listScrollPane;
  private SourceView sView;
  private JLabel titleLbl;

  PrintStream out = System.out;
  PrintStream console = System.out;

  //���� �������� ������ �ʾ���.
  //static Icon stopIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/stop.gif"));

  public SourcePanel( DebuggerProxy proxy ) {
    debuggerProxy = proxy;
    try  {
      uiInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public final void reset(DebuggerProxy debuggerProxy) {
    this.debuggerProxy = debuggerProxy;
    this.currentClassId = null;
    sView.clearDocument();
    sView.setBreakpointableLines(null);
    setEnablePanel(false);
  }

  void uiInit() throws Exception {
    this.setLayout(borderLayout1);
    JPanel upPane = new JPanel();
    titleLbl = new JLabel("Source View");
    upPane.setLayout(new BorderLayout());
    upPane.add(titleLbl, BorderLayout.WEST);

    //stopButton = new JButton(stopIcon);
    stopButton = new JButton("Stop");
    stopButton.addActionListener(this);
    stopButton.setActionCommand(stopActionCommand);
    stopButton.setToolTipText(stopActionCommand);
    stopButton.setMargin( new Insets(0, 0, 0, 0));

    clearButton = new JButton("Clear");
    clearButton.addActionListener(this);
    clearButton.setActionCommand(clearActionCommand);
    clearButton.setToolTipText(clearActionCommand);
    clearButton.setMargin( new Insets(0, 0, 0, 0));

    JPanel upLeftPane = new JPanel();
    upLeftPane.add( stopButton );
    upLeftPane.add( clearButton );

    upPane.add(upLeftPane, BorderLayout.EAST);

    add(upPane, BorderLayout.NORTH);
    sView = new SourceView(this);
    add( sView, BorderLayout.CENTER);

    setPreferredSize( new Dimension( 300, 200 ) );
    setEnablePanel( false );
  }

  public void showEmptyDocument() {
    if (currentClassId == null)
      return;
      
    this.currentClassId = null;
    sView.clearDocument();
    sView.setBreakpointableLines(null);
    setEnablePanel(false);
  }
  public void showLine(int line, boolean isProcessing) {
    if (isProcessing) sView.showCurrentProcessingLine(line - 1);
    else sView.moveLine(line - 1);
  }

  public void printSourceCode() {
    RemoteThread currentThread = debuggerProxy.getCurrentThread();
    RemoteStackFrame frame = null;
    if (currentThread == null) {
      out.println("[SourcePanel] CurrentThread is null");
      return;
    }
    try {
      frame = currentThread.getCurrentFrame();
    } catch (IllegalAccessError e) {
      out.println("[SourcePanel] Current thread isn't suspended.");
      update();
      return;
    } catch (ArrayIndexOutOfBoundsException e) {
      out.println("[SourcePanel] Thread is not running (no stack).");
      update();
      return;
    } catch (Exception err) {
      out.println("[SourcePanel] currentThred.getCurrentFrame throws Exception");
      update();
      return;
    }
    if (frame.getPC() == -1) {
      out.println("[SourcePanel] Current method is native");
      update();
      return;
    }

    try {
      setClass(frame.getRemoteClass().getName());
      int lineNumber = frame.getLineNumber();
      showLine(lineNumber, true);
      out.println("[SourcePanel] hit line line : " + (lineNumber - 1));
    } catch (Exception e) {
      out.println("[SourcePanel] frame.getRemoteClass().getName() throws Exception");
      update();
      return;
    }
  }

  public void update() {
    if (currentClassId != null) {
      /*boolean flag = false;
      RemoteStackFrame frame = null;
      try {
        frame = currentThread.getCurrentFrame();
      } catch (IllegalAccessError e) {
        out.println("Current thread isn't suspended.");
        flag = true;
      } catch (ArrayIndexOutOfBoundsException e) {
        out.println("Thread is not running (no stack).");
        flag = true;
      }
      if( frame.getPC() == -1 ){
        out.println("[SourcePanel] Current method is native");
        flag = true;
      }*/
      setClass(currentClassId);
    }
  }

  public void setClass(String classId) {
    //System.out.println("SourcePanel : setClass start");
    Vector bpVector = breakpointEventListener.getBreakpointVector( classId );
    if ((currentClassId == null) || (!currentClassId.equals(classId))) {
      currentClassId = classId;

//    System.out.println("[Sourcepanel] setclass : " + currentClassId);
      sView.clearDocument();
      RemoteClass remoteClass = null;
      try{
        titleLbl.setText("Source View");
        remoteClass = debuggerProxy.getRemoteDebugger().findClass(classId);
        titleLbl.setText("Source View (" + remoteClass.getName() + ")");
      } catch( Exception e){
        System.err.println("[SourcePanel] " + e.toString());
        return;
      }

      InputStream rawSourceFile = null;
      //DataInputStream sourceFile = null;
      BufferedReader sourceFile = null;
      try {
        rawSourceFile = remoteClass.getSourceFile();
        if (rawSourceFile == null) {
          sView.addLine("[SourcePanel] " + "Unable to find " + remoteClass.getSourceFileName());
          return;
        }
        //sourceFile = new DataInputStream(rawSourceFile);
        sourceFile = new BufferedReader(new InputStreamReader(rawSourceFile));
      } catch ( Exception e ) {
        e.printStackTrace();
        return;
      }

      String sourceLine = null;
      int lineCount = 0;
      Vector bList = new Vector();
      /* Print lines */
      try {
        sourceLine = sourceFile.readLine();
//        System.out.println("[SourcePanel] firstLine : " + sourceLine);
        while ( sourceLine != null ) {
          lineCount++;
          sView.addLine(sourceLine);
          // ���� ������ Breakpoint�� �ɼ� �ִ��� ������ üũ�Ѵ�.
          try{
            String errmessage = remoteClass.setBreakpointLine(lineCount);
            // Breakpoint�� �� �� ����.
//            if (errmessage.length() > 0 ) {
//            } else { // Breakpoint�� �ɼ� �ִ�.
            if (errmessage.length() == 0 ) {
              // ���� �Ѵ�.
              remoteClass.clearBreakpointLine(lineCount);
              // ����Ʈ�� �߰��Ѵ�.
              bList.addElement(new Integer(lineCount));
            }
          } catch(IllegalArgumentException err){
            err.printStackTrace();
//            JOptionPane.showMessageDialog(null, "\"" + idClass + "\" is not a valid id or class name", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
//            return false;
          } catch(Exception err2){
            err2.printStackTrace();
//            JOptionPane.showMessageDialog(null, "setBreakpointLine throws Exception.", "Breakpoint Error", JOptionPane.ERROR_MESSAGE);
//            return false;
          }

          sourceLine = sourceFile.readLine();
        }
        sView.setBreakpointableLines(bList);
        // Comment (1) -------------- Start (1)
        // ������ �극��ũ ����Ʈ ������ �׽�Ʈ �� �� ��� �����ϱ� ������
        // ���� �ִ� �극��ũ ����Ʈ���� �����Ǿ� ������.
        // ���� ���� �ɷ��ִ� ���� ���� �극��ũ ����Ʈ�� ������ ����Ѵ�.
        if (bpVector != null) {
          for (int i = 0; i < bpVector.size(); i++) {
            BreakpointEvent bevt = (BreakpointEvent)bpVector.elementAt(i);
            if (bevt != null) remoteClass.setBreakpointLine(bevt.getLine());
          }
        }
        // Comment (1) -------------- End (1)
        sView.moveInit();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (bpVector != null) sView.setBreakPointVector(bpVector);

    setEnablePanel( true );
  }
  private void setEnablePanel( boolean b ){
    stopButton.setEnabled( b );
    clearButton.setEnabled( b );
  }

  // The add/remove methods provide the signature for the IDE to recognize
  // these events and show them in the event list
  public synchronized void addBreakpointEventListener(BreakpointEventListener l) {
    //listenerList.addElement(l);
    breakpointEventListener = l;
  }

  public synchronized void removeBreakpointEventListener(BreakpointEventListener l){
    //listenerList.removeElement(l);
    breakpointEventListener = null;
  }

  public void addBreakpoint(int line) {
    int lineNumber = line + 1;
    BreakpointEvent event = new BreakpointEvent(this, BreakpointEvent.BREAKPOINT_ADDED, null, currentClassId, lineNumber);
    if (breakpointEventListener != null) {
      breakpointEventListener.breakpointAdded(event);
      sView.addBreakpoint(lineNumber);
    } else {
      System.out.println("[MethodPanel] BreakpointManager is null!");
    }
  }

  public void removeBreakpoint(int line) {
    int lineNumber = line + 1;
    BreakpointEvent event = new BreakpointEvent(this, BreakpointEvent.BREAKPOINT_REMOVED, null, currentClassId, lineNumber);
    if (breakpointEventListener != null) {
      breakpointEventListener.breakpointRemoved(event);
      sView.removeBreakpoint(lineNumber);
    } else {
      System.out.println("[MethodPanel] BreakpointManager is null!");
    }
  }

  public void requestFocus() {
    sView.requestFocus();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(stopActionCommand)) {
      // ����� stop button�� Ŭ���Ǿ����� �߻��մϴ�.
      //System.out.println(stopActionCommand);
      //System.out.println(methodList.getSelectedValue());// �̰� method�� �ɰ̴ϴ�.
      //System.out.println(currentClassId + "." + methodList.getSelectedValue());
      int lineNumber = sView.getLine();
      if( lineNumber == -1 ) {
        //���� SourcePanel�� list�� �������� ���� �����Դϴ�.
        JOptionPane.showMessageDialog(this, "Source Code�� Line�� ���� ���� �ϼ���.", "Stop Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      lineNumber++;
      System.out.println("[Sourcepanel] C_ID : " + currentClassId + ", Line : " + lineNumber);
      BreakpointEvent event = new BreakpointEvent(this, BreakpointEvent.BREAKPOINT_ADDED, null, currentClassId, lineNumber);
      if (breakpointEventListener != null) {
        breakpointEventListener.breakpointAdded(event);
        sView.addBreakpoint(lineNumber);
      } else {
        System.out.println("[MethodPanel] BreakpointManager is null!");
      }
    } else if (e.getActionCommand().equals(clearActionCommand)) {
      int lineNumber = sView.getLine();
      if (lineNumber == -1) {
        //���� SourcePanel�� list�� �������� ���� �����Դϴ�.
        JOptionPane.showMessageDialog(this, "Source Code�� Line�� ���� ���� �ϼ���.", "Clear Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      lineNumber++;
      System.out.println("[Sourcepanel] C_ID : " + currentClassId + ", Line : " + lineNumber);
      BreakpointEvent event = new BreakpointEvent(this, BreakpointEvent.BREAKPOINT_REMOVED, null, currentClassId, lineNumber);
      if (breakpointEventListener != null) {
        breakpointEventListener.breakpointRemoved(event);
        sView.removeBreakpoint(lineNumber);
      } else {
        System.out.println("[MethodPanel] BreakpointManager is null!");
      }
    }
  }

  // EditorOptions interface
  public Color getStringColor() {
    return new Color(0, 0, 0);
  }

  public Color getBGColor() {
    return new Color(255, 255, 255);
  }

  public Color getKeywordColor() {
    return new Color(0, 0, 255);
  }

  public Color getConstantColor() {
    return new Color(255, 0, 0);
  }

  public Color getCommentColor() {
    return new Color(0, 153, 0);
  }

  public Font getFont() {
    return new Font("DialogInput", Font.PLAIN, 12);
  }
}
