/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/MethodPanel.java,v 1.10 1999/09/01 02:01:21 itree Exp $
 * $Revision: 1.10 $
 * $History: MethodPanel.java $
 * 
 * *****************  Version 10  *****************
 * User: Bezant       Date: 99-06-20   Time: 10:06p
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 9  *****************
 * User: Bezant       Date: 99-06-18   Time: 1:56a
 * Updated in $/AntIDE/source/ant/debugger
 * ���� ���α׷��� ����Ǿ����� reset�����ִ�
 * ��ƾ�� �־����ϴ�.
 * 
 * *****************  Version 8  *****************
 * User: Bezant       Date: 99-06-17   Time: 11:39p
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 7  *****************
 * User: Bezant       Date: 99-06-15   Time: 12:58a
 * Updated in $/AntIDE/source/ant/debugger
 * ThreadPanel�� commandPanel�� �� �պý���.
 * 
 * *****************  Version 6  *****************
 * User: Bezant       Date: 99-06-11   Time: 8:38p
 * Updated in $/AntIDE/source/ant/debugger
 * Breakpoint Manage�� ���� ���ɰ����� �� �־����ϴ�.
 * ��..
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
 * User: Bezant       Date: 99-06-04   Time: 1:42a
 * Updated in $/AntIDE/source/ant/debugger
 * ���� �ھ߰���.. ����..
 *
 * *****************  Version 1  *****************
 * User: Multipia     Date: 99-06-02   Time: 10:46p
 * Created in $/AntIDE/source/ant/debugger
 * Ant Debugger Class
 */

package com.antsoft.ant.debugger;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.StringTokenizer;
import javax.swing.*;

import sun.tools.debug.*;

public class MethodPanel extends JPanel implements ActionListener{
  private DebuggerProxy debuggerProxy;
  private SourcePanel sourcePanel;
  private BreakpointEventListener breakpointEventListener = null;
  private String currentClassId = null;
  private JButton stopButton = null;
  private JButton clearButton = null;

  //static Icon stopIcon = new ImageIcon(LoadedClassesPanel.class.getResource("image/stop.gif"));

  BorderLayout borderLayout1 = new BorderLayout();
  JList methodList = new JList();
  DefaultListModel methodModel = new DefaultListModel();
  MethodListCellRenderer renderer = null;
  JLabel title = new JLabel(" Method Lists: ");

  private String stopActionCommand = "Set Breakpoint in Selected Method";
  private String clearActionCommand = "Clear Breakpoint in Selected Method";

  public MethodPanel( DebuggerProxy proxy , SourcePanel sourcePanel) {
    debuggerProxy = proxy;
    this.sourcePanel = sourcePanel;
    renderer = new MethodListCellRenderer(methodList);
    methodList.setCellRenderer(renderer);
    methodList.setModel(methodModel);

    try  {
      uiInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  public final void reset(DebuggerProxy debuggerProxy) {
    this.debuggerProxy = debuggerProxy;
    renderer.setBreakpointVector(null);
    //methodModel = new DefaultListModel();
    methodModel.removeAllElements();

    setEnablePanel(false);
    currentClassId = null;
  }
  // The add/remove methods provide the signature for the IDE to recognize
  // these events and show them in the event list
  public synchronized void addBreakpointEventListener(BreakpointEventListener l) {
    //listenerList.addElement(l);
    //System.out.println("[MethodPanel] : listener add");
    breakpointEventListener = l;
  }
  public synchronized void removeBreakpointEventListener(BreakpointEventListener l){
    //listenerList.removeElement(l);
    breakpointEventListener = null;
  }

  void uiInit() throws Exception {
    this.setLayout(borderLayout1);
    //JButton stopButton = new JButton(stopIcon);
    stopButton = new JButton("Stop");
    stopButton.setMargin( new Insets(0,0,0,0) );
    stopButton.setActionCommand(stopActionCommand);
    stopButton.setToolTipText(stopActionCommand);
    stopButton.addActionListener(this);

    clearButton = new JButton("Clear");
    clearButton.setMargin( new Insets(0, 0, 0, 0));
    clearButton.setActionCommand(clearActionCommand);
    clearButton.setToolTipText(clearActionCommand);
    clearButton.addActionListener(this);

    JPanel upLeftPane = new JPanel();
    upLeftPane.add(stopButton);
    upLeftPane.add(clearButton);

    JPanel upPane = new JPanel();
    upPane.setLayout(new BorderLayout());
    upPane.add( title, BorderLayout.WEST );
    upPane.add( new JPanel(), BorderLayout.CENTER);
    upPane.add( upLeftPane, BorderLayout.EAST );
    add( upPane , BorderLayout.NORTH);
    setPreferredSize( new Dimension( 200, 150 ) );
    add(new JScrollPane(methodList), BorderLayout.CENTER);
    setEnablePanel( false );

    MouseListener mouseListener = new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 2){
          int index = methodList.locationToIndex(e.getPoint());
          if( index != -1 ){
            methodList.setSelectedIndex(index);            
            MethodEntry entry = null;
            entry = (MethodEntry)methodList.getSelectedValue();
            if (entry == null) {
            	System.out.println("Remote Field Null : MethodPanel.java");
            	return;
            }            
            
            String methodName = entry.getName();

            if ( methodName != null ) {
              int line = debuggerProxy.getMethodLineNumber( methodName );
              if( line != -1){
                sourcePanel.showLine( line, false );
              }

            }
          }
        }
      }
    };
    methodList.addMouseListener(mouseListener);


  }
  private void setEnablePanel( boolean b ){
    stopButton.setEnabled( b );
    clearButton.setEnabled( b );
  }
  public void update(){
    if( currentClassId != null)
      setClass(currentClassId);
  }
  public void setClass( String classId ) {
    currentClassId = classId;
    methodModel.removeAllElements();
    //title.setText("Method Lists: " + classId);

    if (classId == null ) {
      JOptionPane.showMessageDialog(this, "No class specified.", "Warning", JOptionPane.WARNING_MESSAGE );
      return;
    }

    try {
      RemoteClass cls = (RemoteClass)debuggerProxy.getRemoteDebugger().findClass(classId);
	    /* It's a class */
	    if (cls == null) {
	    	throw new IllegalArgumentException();
	    }

      //���⼭ BreakpointManager���� classId�� Ű�� �ϴ� breakpointEntity���� ������ �ִ�
      //vector�� �ִ��� �˾ƺ���.
      Vector breakpointVector = breakpointEventListener.getBreakpointVector(classId);
      //System.out.println("[MethodPanel] BPManager���� �޾ƿ� BPVector : " + breakpointVector);
      renderer.setBreakpointVector(breakpointVector);
      RemoteField methods[] = cls.getMethods();

      for (int i = 0; i < methods.length; i++) {
        //methodModel.addElement(methods[i].getTypedName());
        //methodModel.addElement(methods[i].getName());
        //methodModel.addElement(methods[i]);
        boolean isStatic = false;
        boolean isConstructor = false;
        int modifierType;
        
        if(methods[i].isStatic())
        	isStatic = true;
        if(methods[i].getName().equals("<init>"))
        	isConstructor = true;
        if(methods[i].getModifiers().indexOf("protected") != -1)
        	modifierType = MethodEntry.PROTECTED;
        else if(methods[i].getModifiers().indexOf("private") != -1)
        	modifierType = MethodEntry.PRIVATE;
        else
        	modifierType = MethodEntry.PUBLIC;
        if (isConstructor) {
        	//StringTokenizer t = new StringTokenizer(methods[i].getTypedName(),"<init>");
        	//StringBuffer name = new StringBuffer();
        	//name.append(t.nextToken());
        	//name.append(classId);
        	//name.append(t.nextToken()); 
        	int index = methods[i].getTypedName().indexOf("<init>");
        	String name = methods[i].getTypedName().substring(0,index);
        	String name2 = methods[i].getTypedName().substring(index+6);
        	String name3 = classId.substring(classId.lastIndexOf(".")+1); 
	        MethodEntry entry = new MethodEntry(name + name3 + name2 , methods[i].getName(), methods[i], 
        								methods[i].getModifiers(), isStatic, isConstructor, modifierType);
         	methodModel.insertElementAt(entry,0);
        }
        else { 
         	MethodEntry entry = new MethodEntry(methods[i].getTypedName(), methods[i].getName(), methods[i], 
        								methods[i].getModifiers(), isStatic, isConstructor, modifierType);
	        methodModel.addElement(entry);
	      }  
      }

      /*if( Vector == null ){
        //���� �� Ŭ�������� breakpoint�� ���ٴ� ���Դϴ�.
        RemoteField methods[] = cls.getMethods();
        for (int i = 0; i < methods.length; i++) {
          //methodModel.addElement(methods[i].getTypedName());
         methodModel.addElement(methods[i].getName());
        }
      }else{
        //���� �� Ŭ������ breakpoint�� �����Ƿ� �� �޼ҵ� ���� �ڱⰡ breakpoint��
        //�ɷ� �ִ����� ������� �մϴ�.
        for(int i = 0; i < methods.length; i++){
          if(){
            methodModel.addElement(methods[i].getName());
            methodList.s
          }
        }
      } */
    } catch (IllegalArgumentException e) {
      JOptionPane.showMessageDialog(this, "\"" + classId +
                                    "\" is not a valid id or class name.", "Warning", JOptionPane.WARNING_MESSAGE );
    } catch (Exception e) {
      System.err.println(e.toString());

    }
    // setClass�� �����ϸ� Panel�� �ִ� ��ɵ���
    // Enable ��Ų��.
    setEnablePanel( true );
  }
  public void actionPerformed(ActionEvent e){
    if(e.getActionCommand().equals(stopActionCommand)){
      // ����� stop button�� Ŭ���Ǿ����� �߻��մϴ�.
      //System.out.println(stopActionCommand);
      //System.out.println(methodList.getSelectedValue());// �̰� method�� �ɰ̴ϴ�.
      //System.out.println(currentClassId + "." + methodList.getSelectedValue());
      //String method = "" + methodList.getSelectedValue();
      this.requestFocus();
      MethodEntry entry = (MethodEntry)methodList.getSelectedValue();
      if (entry == null) {
      	System.out.println("MethodPanel.. entry is null ");
      	return;
      }
      RemoteField remoteField = entry.getRemoteField();
      if( remoteField == null ){
        //���� ���õ� ���� ������.
        JOptionPane.showMessageDialog(this,"Method�� ���� ������ �ֽʽÿ�","Stop Error",JOptionPane.ERROR_MESSAGE);
        return;
      }
      String method = remoteField.getName();
      BreakpointEvent event = new BreakpointEvent(this, BreakpointEvent.BREAKPOINT_ADDED, null, currentClassId, method, null);
      if(breakpointEventListener != null){
        breakpointEventListener.breakpointAdded(event);
      }else{
        System.out.println("[MethodPanel] BreakpointManager is null!");
      }
    }else if( e.getActionCommand().equals(clearActionCommand)){
      this.requestFocus();
      MethodEntry entry = (MethodEntry)methodList.getSelectedValue();
      if (entry == null) {
      	System.out.println("MethodPanel.. entry is null ");
      	return;
      }
      RemoteField remoteField = entry.getRemoteField();
      if( remoteField == null ){
        //���� ���õ� ���� ������.
        JOptionPane.showMessageDialog(this,"Method�� ���� ������ �ֽʽÿ�","Clear Error",JOptionPane.ERROR_MESSAGE);
        return;
      }
      String method = remoteField.getName();
      BreakpointEvent event = new BreakpointEvent(this, BreakpointEvent.BREAKPOINT_ADDED, null, currentClassId, method, null);
      if(breakpointEventListener != null){
        breakpointEventListener.breakpointRemoved(event);
      }else{
        System.out.println("[MethodPanel] BreakpointManager is null!");
      }

    }
  }

}

