/*
 * Ant ( JDK wrapper Java IDE )
 * Version 1.0
 * Copyright (c) 1998-1999 Antsoft Co. All rights reserved.
 *  This program and source file is protected by Korea and international
 * Copyright laws.
 *
 * Author:       Kwon, Young Mo
 * $Header: /usr/cvsroot/AntIDE/source/com/antsoft/ant/debugger/CallStackPanel.java,v 1.4 1999/07/29 01:27:08 itree Exp $
 * $Revision: 1.4 $
 * $History: CallStackPanel.java $
 * 
 * *****************  Version 8  *****************
 * User: Bezant       Date: 99-06-19   Time: 1:10a
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 7  *****************
 * User: Bezant       Date: 99-06-18   Time: 1:56a
 * Updated in $/AntIDE/source/ant/debugger
 * ���� ���α׷��� ����Ǿ����� reset�����ִ�
 * ��ƾ�� �־����ϴ�.
 * 
 * *****************  Version 6  *****************
 * User: Bezant       Date: 99-06-17   Time: 11:39p
 * Updated in $/AntIDE/source/ant/debugger
 * 
 * *****************  Version 5  *****************
 * User: Bezant       Date: 99-06-04   Time: 12:49a
 * Updated in $/AntIDE/source/ant/debugger
 * ���Źٲ���
 * 
 * *****************  Version 4  *****************
 * User: Bezant       Date: 99-06-03   Time: 12:23a
 * Updated in $/AntIDE/source/ant/debugger
 * ThreadsPanel �ϰ� LocalVariablesPanel�� �ߴµ�
 * �װ� ������ �ִ����� list�� �ϴ� �ٿ��� ����.
 * localViariablesPanel�� ���׵� ����. �̸�.. ����
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
import java.util.Vector;
import javax.swing.*;
import sun.tools.debug.*;

public class CallStackPanel extends JPanel {
  DebuggerProxy debuggerProxy = null;
  BorderLayout borderLayout1 = new BorderLayout();
  JList callStackList = new JList();
  public CallStackPanel(DebuggerProxy proxy) {
    super();
    this.debuggerProxy = proxy;

    try  {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  public final void reset(DebuggerProxy debuggerRroxy) {
    this.debuggerProxy = debuggerProxy;
    callStackList.removeAll();
  }

  private final void jbInit() throws Exception {
    setLayout(borderLayout1);
    add(new JLabel("Call Stack View"), BorderLayout.NORTH);
    add(new JScrollPane(callStackList), BorderLayout.CENTER);
    setPreferredSize( new Dimension( 300, 200 ) );
  }
  public final void update() throws Exception{
    Vector v = debuggerProxy.where(false);
    if( v != null ){
      setCallStack(v);
    }
  }

  private final void setCallStack( Vector list ) {
    callStackList.setListData( list );
  }
}

